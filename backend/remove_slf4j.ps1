# PowerShell script to remove Slf4j annotations and log calls with proper UTF-8 encoding

# Get all Java files in the project
$javaFiles = Get-ChildItem -Path "src\main\java" -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    # Read content with UTF-8 encoding
    $content = Get-Content $file.FullName -Encoding UTF8 -Raw
    $modified = $false
    
    # Remove Slf4j import
    if ($content -match "import lombok\.extern\.slf4j\.Slf4j;") {
        $content = $content -replace "import lombok\.extern\.slf4j\.Slf4j;\r?\n", ""
        $modified = $true
    }
    
    # Remove @Slf4j annotation
    if ($content -match "@Slf4j\r?\n") {
        $content = $content -replace "@Slf4j\r?\n", ""
        $modified = $true
    }
    
    # Remove log.error, log.warn, log.info calls (keep the return statement)
    if ($content -match "log\.(error|warn|info)\([^;]+;\r?\n") {
        $content = $content -replace "\s+log\.(error|warn|info)\([^;]+;\r?\n", ""
        $modified = $true
    }
    
    if ($modified) {
        # Write content with UTF-8 encoding without BOM
        $utf8NoBom = New-Object System.Text.UTF8Encoding $false
        [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        Write-Host "Updated: $($file.FullName)"
    }
}

Write-Host "Slf4j removal completed with proper encoding!"