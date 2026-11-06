@echo off
REM ============================================================
REM split_nginx_log.bat - split nginx error.log / access.log by date
REM Requires: Windows PowerShell (built-in on Win7+/Server 2008+)
REM nginx does not write date-stamped files, so this script archives
REM historical logs by date. Lines dated today or later stay in the
REM source file so nginx can keep appending to it.
REM
REM Usage:
REM   split_nginx_log.bat                                     split default LOG_DIR
REM   split_nginx_log.bat <log_dir> <out_dir>                custom paths
REM   split_nginx_log.bat "" "" archive|full|watch           specify mode
REM
REM Modes:
REM   archive   stream-split, archive < today, source file preserved (default)
REM   full      same as archive (reserved)
REM   watch     monitor mode, incremental archive by file offset
REM ============================================================
setlocal EnableDelayedExpansion

set "LOG_DIR=%~1"
if "%LOG_DIR%"=="" set "LOG_DIR=D:\nginx-1.24.0\nginx-1.24.0\logs"
set "OUT=%~2"
if "%OUT%"=="" set "OUT=D:\nginx-1.24.0\nginx-1.24.0\logs"
set "MODE=%~3"
if "%MODE%"=="" set "MODE=archive"
set "INTERVAL=%~4"
if "%INTERVAL%"=="" set "INTERVAL=30"

echo [INFO] log dir:    %LOG_DIR%
echo [INFO] out dir:     %OUT%
echo [INFO] mode:        %MODE%
if /i "%MODE%"=="watch" echo [INFO] interval:    %INTERVAL% sec

where powershell >nul 2>&1
if errorlevel 1 (
    echo [ERROR] PowerShell not found. Win7/Server2008 need manual install of PS 3.0+
    exit /b 1
)

set "PS_SCRIPT=%~dp0split_nginx_log.ps1"
if not exist "%PS_SCRIPT%" (
    echo [ERROR] core script not found: %PS_SCRIPT%
    exit /b 1
)

if not exist "%OUT%" (
    echo [INFO] create out dir: %OUT%
    mkdir "%OUT%" >nul 2>&1
)

set "HAS_ANY=0"
for %%L in (error.log access.log) do (
    set "FULL=%LOG_DIR%\%%L"
    if exist "!FULL!" (
        echo.
        echo ========== Processing %%L ==========
        powershell -NoProfile -ExecutionPolicy Bypass -File "%PS_SCRIPT%" -Src "!FULL!" -Out "%OUT%" -Mode "%MODE%" -Interval %INTERVAL%
        set "HAS_ANY=1"
    ) else (
        echo [SKIP] !FULL! (not found)
    )
)

if "%HAS_ANY%"=="0" (
    echo.
    echo [WARN] no logs (error.log / access.log) found under %LOG_DIR%
)

echo.
echo ========== ALL DONE ==========
exit /b 0
