# PowerShell脚本：批量修改Java文件中的Long类型ID参数为String类型

# 定义需要修改的文件路径
$files = @(
    "F:\gjxt\backend\src\main\java\com\gjxt\inspection\controller\SysInspectionRecordController.java",
    "F:\gjxt\backend\src\main\java\com\gjxt\inspection\controller\SysLocationController.java",
    "F:\gjxt\backend\src\main\java\com\gjxt\inspection\controller\SysDeductionTypeController.java",
    "F:\gjxt\backend\src\main\java\com\gjxt\inspection\controller\SysDeptController.java",
    "F:\gjxt\backend\src\main\java\com\gjxt\inspection\controller\SysProfessionController.java",
    "F:\gjxt\backend\src\main\java\com\gjxt\inspection\controller\SysInspectionTaskController.java"
)

# 定义替换规则
$replacements = @(
    @{Pattern = '@PathVariable Long id'; Replacement = '@PathVariable String id'},
    @{Pattern = '@RequestParam\(required = false\) Long excludeId'; Replacement = '@RequestParam(required = false) String excludeId'},
    @{Pattern = '@RequestParam\(required = false\) Long professionId'; Replacement = '@RequestParam(required = false) String professionId'},
    @{Pattern = '@RequestParam\(required = false\) Long locationId'; Replacement = '@RequestParam(required = false) String locationId'},
    @{Pattern = '@RequestParam\(required = false\) Long checkerId'; Replacement = '@RequestParam(required = false) String checkerId'},
    @{Pattern = '@RequestParam\(required = false\) Long assignedUserId'; Replacement = '@RequestParam(required = false) String assignedUserId'},
    @{Pattern = '@RequestParam\(required = false\) Long deptId'; Replacement = '@RequestParam(required = false) String deptId'},
    @{Pattern = '@RequestParam Long professionId'; Replacement = '@RequestParam String professionId'},
    @{Pattern = '@RequestParam Long locationId'; Replacement = '@RequestParam String locationId'},
    @{Pattern = '@PathVariable Long userId'; Replacement = '@PathVariable String userId'}
)

# 执行替换
foreach ($file in $files) {
    if (Test-Path $file) {
        Write-Host "Processing file: $file"
        $content = Get-Content $file -Raw
        
        foreach ($replacement in $replacements) {
            $content = $content -replace $replacement.Pattern, $replacement.Replacement
        }
        
        Set-Content $file $content -Encoding UTF8
        Write-Host "Updated: $file"
    } else {
        Write-Host "File not found: $file"
    }
}

Write-Host "Batch replacement completed!"