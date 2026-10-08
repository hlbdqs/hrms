# 智能人事管理系统 - 后端（hrms-backend）

> 实验一：搭建 Web 基础项目（Spring Boot 3.x + MyBatis + MySQL），完成员工/部门 CRUD。

## 技术栈

- JDK 17
- Spring Boot 3.3.x
- MyBatis（`mybatis-spring-boot-starter`）
- MySQL 8
- Maven
- springdoc-openapi（Swagger UI，实验二接口文档）

## 目录结构

```
hrms-backend
├── pom.xml
└── src/main
    ├── java/com/hrms
    │   ├── HrmsApplication.java          # 启动类
    │   ├── common/                       # 通用：统一返回、分页、异常处理
    │   ├── entity/                       # 实体：Employee、Department
    │   ├── mapper/                       # 数据访问层（MyBatis 注解）
    │   ├── service/                      # 业务层接口 + 实现
    │   └── controller/                   # RESTful 接口层
    └── resources
        ├── application.yml               # 数据源、MyBatis 配置
        └── db/schema.sql                 # 建表 + 种子数据（幂等）
```

## 运行前准备

1. 安装 JDK 17、Maven、MySQL（或 Navicat）。
2. 确保本机 MySQL 已启动，并修改 `application.yml` 中的 `username` / `password` 为你本地的账号密码（默认 root / CHANGE_ME_PASSWORD）。
3. 首次运行会自动创建 `hrms` 数据库并初始化表与种子数据（`createDatabaseIfNotExist=true` + 幂等 schema.sql）。

## 运行

```bash
cd hrms-backend
mvn spring-boot:run
```

启动成功后：

- 接口文档（Swagger UI）：<http://localhost:8080/swagger-ui.html>
- OpenAPI JSON：<http://localhost:8080/v3/api-docs>

## 接口自测（curl）

```bash
# 分页查询员工（第 1 页，每页 10 条）
curl "http://localhost:8080/api/employees?page=1&size=10"

# 关键字搜索
curl "http://localhost:8080/api/employees?keyword=张"

# 按 ID 查询
curl http://localhost:8080/api/employees/1

# 新增员工
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"employeeNo":"E100","name":"赵六","gender":1,"phone":"13800000006","email":"zhaoliu@hrms.com","departmentId":1,"status":1}'

# 更新员工
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"employeeNo":"E001","name":"张三丰","gender":1,"phone":"13800000001","email":"zhangsan@hrms.com","departmentId":1,"status":1}'

# 删除员工
curl -X DELETE http://localhost:8080/api/employees/1

# 查询全部部门
curl http://localhost:8080/api/departments
```

统一返回格式：

```json
{ "code": 200, "message": "success", "data": { "total": 3, "list": [ ... ] } }
```

## 后续实验规划

| 实验 | 将在本项目上增加的内容 |
|---|---|
| 实验二 | Vue 前端 + Axios 联调、CORS 跨域、Swagger 接口文档 |
| 实验三 | Git 分支管理、PR/Code Review、GitHub Actions CI |
| 实验四 | 登录、RBAC（用户/角色/权限三表）、JWT、BCrypt、AI 安全防护 |
