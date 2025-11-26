## 问题定位
- 移动端 `pages/monthly/monthly.uvue` 的统计与列表查询使用了错误的参数名：`status` 与 `departmentId`
- 后端 `GET /monthly-inspection-record/page` 接收的是 `recordStatus` 与 `responsibilityId`
- 由于参数不匹配，后端未进行状态与部门过滤，导致待整改/已整改/已复查数量相同

## 修复方案
1. 修正统计接口参数
- 将三次统计请求的URL从：
  - `/monthly-inspection-record/page?current=1&size=1&status=0${deptParam}`
  - `/monthly-inspection-record/page?current=1&size=1&status=1${deptParam}`
  - `/monthly-inspection-record/page?current=1&size=1&status=2${deptParam}`
- 替换为：
  - `/monthly-inspection-record/page?current=1&size=1&recordStatus=0${deptParam}`
  - `/monthly-inspection-record/page?current=1&size=1&recordStatus=1${deptParam}`
  - `/monthly-inspection-record/page?current=1&size=1&recordStatus=2${deptParam}`
- 其中 `deptParam` 由 `&departmentId=${departmentId}` 改为 `&responsibilityId=${departmentId}`（管理员不传该参数）

2. 修正列表查询参数
- 将 `loadMonthlyRecords()` 的URL从：
  - `/monthly-inspection-record/page?current=1&size=10${deptParam}`（`deptParam` 使用 `departmentId`）
- 替换为：
  - `/monthly-inspection-record/page?current=1&size=10${deptParam}`（`deptParam` 使用 `responsibilityId`）

## 新增移动端页面（UTS）
1. 月检确认整改页面：`pages/monthly/rectify.uvue`
- 功能：
  - 展示问题信息与整改前图片
  - 录入 `rectificationEffect` 文本与上传整改图片（最多3张）
  - 调用后端：`PUT /monthly-inspection-record/{id}/rectify`
- 关键点：
  - `<script lang="uts">`，严格UTS类型定义
  - 图片上传使用 `uni.uploadFile` 到 `/api/upload/image`，收集 `filePath/url` 并以逗号拼接提交 `rectificationImages`

2. 月检确认复查页面：`pages/monthly/review.uvue`
- 功能：
  - 展示问题信息与整改效果与图片（只读）
  - 录入 `reviewEffect` 与上传复查图片（最多3张）
  - 调用后端：`PUT /monthly-inspection-record/{id}/review`
- 关键点：
  - `<script lang="uts">`，严格UTS类型定义
  - 图片上传同上，提交 `reviewImages`

3. 复用/对齐已有页面
- 参考已有 `pages/record/review.uvue` 的图片上传、详情加载与UTS写法
- 按月检接口与字段命名对齐：`rectificationEffect/rectificationImages/reviewEffect/reviewImages`

## 路由与导航
- 在 `pages.json` 新增：
  - `pages/monthly/rectify` 与 `pages/monthly/review` 的页面配置
- 导航策略：
  - 从月检列表或详情跳转：
    - 状态=0（待整改）→ 跳转 `rectify.uvue?id=<recordId>`
    - 状态=1（已整改）→ 跳转 `review.uvue?id=<recordId>`
- 保持与现有 `pages/monthly/task-record.uvue` 导航一致的风格

## 验证方案
- 统计验证：
  - 登录非管理员账号，设置正确部门 `deptId`
  - 依次请求 `recordStatus=0/1/2`，确认三个数量分别对应后端真实 `total`
- 列表验证：
  - `responsibilityId` 过滤后仅展示本部门数据
- 整改与复查：
  - 在移动端提交整改/复查，后端返回 `code=200`
  - Web端查看 `ViewDialog.vue` 显示整改/复查效果与图片正常

## 兼容与注意事项
- 保持UTS语法与类型：定义页面数据、接口返回、图片数组等的类型
- 统一图片URL转换：使用 `apiConfig.baseUrl + '/api' + relativePath`
- 管理员逻辑：管理员不追加部门过滤参数

## 文档与记录
- 变更完成后在 `README.md` 增加“移动端：月检统计参数修复 & 新增月检整改/复查页面（UTS）”更新记录（含接口与验证说明）

## 实施步骤（按顺序）
1. 修正 `monthly.uvue` 中统计与列表查询的参数名
2. 新增 `pages/monthly/rectify.uvue`（UTS）并接入图片上传与整改确认接口
3. 新增 `pages/monthly/review.uvue`（UTS）并接入图片上传与复查确认接口
4. 更新 `pages.json` 路由，接入跳转
5. 本地运行移动端调试，完成验证
6. 补充 `README.md` 更新记录