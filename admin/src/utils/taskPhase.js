/**
 * 月检任务阶段计算，与后端 MonthlyInspectionTask.currentPhase 语义保持一致。
 * 1=录入期、2=录入结束到公示前、3=公示期、4=公示期后、0=其它（不允许回退）
 */
export function computeTaskPhase(task) {
  if (!task) return 0
  const now = new Date()
  const start = task.inspectionStartTime ? new Date(task.inspectionStartTime) : null
  const end = task.inspectionEndTime ? new Date(task.inspectionEndTime) : null
  const pStart = task.publicityStartTime ? new Date(task.publicityStartTime) : null
  const pEnd = task.publicityEndTime ? new Date(task.publicityEndTime) : null
  if (start && end && now >= start && now <= end) return 1
  if (pStart && pEnd && now >= pStart && now <= pEnd) return 3
  if (end && now > end && pStart && now < pStart) return 2
  if (pEnd && now > pEnd) return 4
  return 0
}

/**
 * 计算月检记录行所属任务的阶段。
 * 记录分页接口已关联带出任务时间字段，这里同步计算，
 * 避免异步回填写到原始对象、绕过 Vue 响应式代理导致按钮不渲染。
 */
export function phaseOfRecord(row) {
  if (!row || !row.taskId) return 0
  return computeTaskPhase({
    inspectionStartTime: row.inspectionStartTime,
    inspectionEndTime: row.inspectionEndTime,
    publicityStartTime: row.publicityStartTime,
    publicityEndTime: row.publicityEndTime
  })
}

/**
 * 记录行是否已带出任务时间字段。
 * 后端未升级版本不返回这些字段，此时阶段算不出来（恒为 0），需前端兜底拉任务详情。
 */
export function hasTaskTimes(row) {
  return !!(row && row.inspectionStartTime && row.inspectionEndTime)
}

/**
 * 任务缺失的时间段（中文标签）。
 * computeTaskPhase 的四个分支都要求对应字段非空，任一缺失都会落到 return 0，
 * 与"录入期前"（任务尚未开始）算出来的 0 完全一样，这里把两者区分开。
 */
export function missingTaskTimeRanges(task) {
  if (!task) return []
  const missing = []
  if (!task.inspectionStartTime || !task.inspectionEndTime) missing.push('月检时间段')
  if (!task.publicityStartTime || !task.publicityEndTime) missing.push('公示时间段')
  return missing
}

/**
 * 该行的阶段算不出来，是因为任务时间没配全，而不是任务还没到录入期。
 * 这种情况下操作按钮会整体消失，需要提示管理员去补配时间段。
 */
export function isTaskTimeUnconfigured(row) {
  if (!row || row.taskPhase !== 0) return false
  return missingTaskTimeRanges(row).length > 0
}
