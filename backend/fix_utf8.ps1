# Fix UTF-8 encoding issues in Java files

# Set encoding to UTF-8
$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# Get all Java files
$javaFiles = Get-ChildItem -Path "src\main\java" -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    Write-Host "Processing: $($file.FullName)"
    
    try {
        # Read file content with UTF-8 encoding
        $content = Get-Content -Path $file.FullName -Encoding UTF8 -Raw
        
        # Skip if content is empty
        if ([string]::IsNullOrEmpty($content)) {
            continue
        }
        
        $modified = $false
        
        # Fix common garbled characters using regex patterns
        # Replace question mark patterns that indicate encoding issues
        if ($content -match '[\u4e00-\u9fff]\?') {
            # Find Chinese characters followed by question marks
            $content = $content -replace '([\u4e00-\u9fff]+)\?', '$1'
            $modified = $true
        }
        
        # Specific replacements for known issues
        $patterns = @(
            @{ Pattern = '控制\?'; Replacement = '控制器' },
            @{ Pattern = '工具\?'; Replacement = '工具类' },
            @{ Pattern = '实现\?'; Replacement = '实现类' },
            @{ Pattern = '服务\?'; Replacement = '服务类' },
            @{ Pattern = '管理\?'; Replacement = '管理器' },
            @{ Pattern = '不存\?'; Replacement = '不存在' },
            @{ Pattern = '已存\?'; Replacement = '已存在' },
            @{ Pattern = '用户\?'; Replacement = '用户名' },
            @{ Pattern = '角\?'; Replacement = '角色' },
            @{ Pattern = '状\?'; Replacement = '状态' },
            @{ Pattern = '字\?'; Replacement = '字段' },
            @{ Pattern = '目\?'; Replacement = '目录' },
            @{ Pattern = '任\?'; Replacement = '任务' },
            @{ Pattern = '是否被使\?'; Replacement = '是否被使用' }
        )
        
        foreach ($pattern in $patterns) {
            if ($content -match $pattern.Pattern) {
                $content = $content -replace $pattern.Pattern, $pattern.Replacement
                $modified = $true
            }
        }
        
        # Write back if modified
        if ($modified) {
            # Remove BOM
            $utf8NoBom = New-Object System.Text.UTF8Encoding $false
            [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
            Write-Host "Fixed: $($file.FullName)" -ForegroundColor Green
        }
    }
    catch {
        Write-Host "Error processing $($file.FullName): $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "UTF-8 encoding fix completed!" -ForegroundColor Green