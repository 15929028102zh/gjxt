# ============================================================
# split_nginx_log.ps1 - 按日期拆分 nginx error.log / access.log
# 通常由 split_nginx_log.bat 调用,也可直接:
#   powershell -NoProfile -ExecutionPolicy Bypass -File split_nginx_log.ps1 -Src D:\...\access.log
#
# 设计要点:
#   - nginx 默认不会按日期写日志文件,所以本脚本按日期归档历史日志
#   - 默认只归档 < today 的行,today 的行留在源文件,让 nginx 继续追加
#   - 兼容 error.log(行首 YYYY/MM/DD)和 access.log(行内 [DD/Mon/YYYY:...])
#   - 归档用 Append 模式,重复跑会累积历史日期(重复问题用户自行调度)
# ============================================================
param(
    [Parameter(Mandatory)]
    [string]$Src,
    [string]$Out = 'D:\nginx-1.24.0\nginx-1.24.0\logs',
    [ValidateSet('full','archive','watch')]
    [string]$Mode = 'archive',
    [int]$Interval = 30,
    [string]$LogType = ''
)

$ErrorActionPreference = 'Stop'
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

if (-not (Test-Path -LiteralPath $Src)) {
    Write-Host "[错误] 源文件不存在: $Src" -ForegroundColor Red
    exit 1
}

# 自动识别 LogType(error / access)
if ([string]::IsNullOrEmpty($LogType)) {
    $name = [System.IO.Path]::GetFileName($Src).ToLower()
    if ($name -like 'access*') { $LogType = 'access' }
    elseif ($name -like 'error*') { $LogType = 'error' }
    else { $LogType = 'log' }
}

# today 用系统日期,run 时刻的"今天"=服务器当天 0 点之后
$today = Get-Date -Format 'yyyy-MM-dd'

# 复合日期正则
# error.log: 2026/08/14 13:59:10 [error] ...
# access.log: 10.116.40.170 - - [14/Aug/2026:13:59:10 +0800] "GET /api/..." 200 ...
$isoDateRe = '^(\d{4})/(\d{2})/(\d{2})'
$accessDateRe = '\[(\d{2})/([A-Z][a-z]{2})/(\d{4})'
$monMap = @{
    'Jan'='01'; 'Feb'='02'; 'Mar'='03'; 'Apr'='04'; 'May'='05'; 'Jun'='06'
    'Jul'='07'; 'Aug'='08'; 'Sep'='09'; 'Oct'='10'; 'Nov'='11'; 'Dec'='12'
}

function Get-DateStr($line) {
    if ($line -match $isoDateRe) {
        return "$($matches[1])-$($matches[2])-$($matches[3])"
    }
    if ($line -match $accessDateRe) {
        $year = $matches[3]
        $mon = $monMap[$matches[2]]
        $day = $matches[1]
        if ($mon) { return "$year-$mon-$day" }
    }
    return $null
}

$writerBaseName = "nginx-$LogType"
$writerExt = ".log"

# ============================================================
# 监控模式 (watch) - 增量归档追加内容
# ============================================================
if ($Mode -eq 'watch') {
    if (-not (Test-Path -LiteralPath $Out)) {
        New-Item -ItemType Directory -Force -Path $Out | Out-Null
    }
    Write-Host "watch 模式: 监控 $Src → $Out" -ForegroundColor Cyan
    Write-Host "  today=$today, today 及以后的行不会归档, 源文件保留" -ForegroundColor Cyan

    $pos = (Get-Item -LiteralPath $Src).Length
    $pending = ''

    while ($true) {
        Start-Sleep -Seconds $Interval
        try {
            $size = (Get-Item -LiteralPath $Src).Length
        } catch { continue }
        if ($size -lt $pos) {
            Write-Host "  [$(Get-Date -Format 'HH:mm:ss')] 文件被截断,重置 offset" -ForegroundColor Yellow
            $pos = 0
        }
        if ($size -eq $pos) { continue }

        $fs = [System.IO.File]::Open($Src, 'Open', 'Read', 'ReadWrite')
        try {
            $fs.Seek($pos, 'Begin') | Out-Null
            $sr = New-Object System.IO.StreamReader($fs, [System.Text.Encoding]::UTF8)
            try { $chunk = $sr.ReadToEnd() }
            finally { $sr.Close() }
        } finally { $fs.Close() }
        $pos = $size
        $pending += $chunk

        $lines = $pending -split "`n"
        $pending = ''
        if ($pending -ne '' -and -not $pending.EndsWith("`n")) {
            $pending = $lines[-1]
            $lines = $lines[0..($lines.Length - 2)]
        }

        $writeCount = 0
        $skipCount = 0
        foreach ($line in $lines) {
            if ($line -eq '') { continue }
            $dateStr = Get-DateStr $line
            if ($null -eq $dateStr) { $skipCount++; continue }
            if ($dateStr -ge $today) { $skipCount++; continue }  # today 及以后不归档
            $path = Join-Path $Out "$writerBaseName-$dateStr$writerExt"
            Add-Content -LiteralPath $path -Value $line -Encoding UTF8
            $writeCount++
        }
        Write-Host ("  [{0}] offset={1} (+{2}B), 写入 {3} 行, 跳过 {4} 行(today/未来/无日期)" -f `
            (Get-Date -Format 'HH:mm:ss'), $pos, $chunk.Length, $writeCount, $skipCount)
    }
    return
}

# ============================================================
# full / archive 模式 - 流式拆分
# ============================================================
if (-not (Test-Path -LiteralPath $Out)) {
    New-Item -ItemType Directory -Force -Path $Out | Out-Null
}

$writers = @{}
$counts = @{}
$sizes = @{}
$skippedToday = 0
$unknown = 0
$total = 0
$startTime = Get-Date

# FileShare.ReadWrite: nginx 进程正在 append,共享读写避免 IO 冲突
$fs = [System.IO.File]::Open($Src, [System.IO.FileMode]::Open, [System.IO.FileAccess]::Read, [System.IO.FileShare]::ReadWrite)
$reader = New-Object System.IO.StreamReader($fs, [System.Text.Encoding]::UTF8)
try {
    while (-not $reader.EndOfStream) {
        $line = $reader.ReadLine()
        $total++
        if ([string]::IsNullOrEmpty($line)) { continue }

        $dateStr = Get-DateStr $line
        if ($null -eq $dateStr) { $unknown++; continue }
        # today 及之后的行不归档,保留在源文件让 nginx 继续写
        if ($dateStr -ge $today) { $skippedToday++; continue }

        if (-not $writers.ContainsKey($dateStr)) {
            $path = Join-Path $Out "$writerBaseName-$dateStr$writerExt"
            # Append 模式:如果归档文件已存在,继续追加(多次跑会累积,用户自己调度)
            $fs = [System.IO.File]::Open($path, 'Append', 'Write', 'None')
            $w = New-Object System.IO.StreamWriter($fs, (New-Object System.Text.UTF8Encoding($false)))
            $writers[$dateStr] = $w
            $counts[$dateStr] = 0
            $sizes[$dateStr] = 0L
        }
        $w = $writers[$dateStr]
        $w.WriteLine($line)
        $counts[$dateStr]++
        $sizes[$dateStr] += [long]$line.Length + 2
    }
} finally {
    $reader.Close()
    $fs.Close()
    foreach ($w in $writers.Values) {
        $w.Flush()
        $w.Close()
    }
}

$elapsed = (Get-Date) - $startTime
Write-Host ""
Write-Host "拆分完成: $total 行 → $($counts.Count) 个新日期文件 → $Out" -ForegroundColor Green
Write-Host ("  today=$today, 跳过 today 及以后: $skippedToday 条(留在源文件), 无日期: $unknown 条")
Write-Host ("  耗时: {0:N1} 秒" -f $elapsed.TotalSeconds)
foreach ($d in ($counts.Keys | Sort-Object)) {
    $mb = $sizes[$d] / 1MB
    Write-Host ("  {0}: {1,10} 行  {2,8:N2} MB" -f $d, $counts[$d], $mb)
}

if ($Mode -eq 'archive') {
    Write-Host ""
    Write-Host "源文件保留不动(today=$today 的内容仍在源文件中,nginx 继续追加写入)" -ForegroundColor Cyan
}