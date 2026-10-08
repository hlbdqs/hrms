-- ============================================================
-- 智能人事管理系统 数据库初始化脚本（实验一 + 实验四）
-- 说明：幂等脚本，可重复执行（CREATE TABLE IF NOT EXISTS + INSERT IGNORE）
-- 注意：若已按旧版（无登录字段）建库，请先 DROP DATABASE hrms 后重新启动
-- ============================================================

-- 部门表
CREATE TABLE IF NOT EXISTS department (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    name        VARCHAR(64)  NOT NULL COMMENT '部门名称',
    description VARCHAR(255)          DEFAULT NULL COMMENT '部门描述',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_department_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '部门表';

-- 员工表（同时作为登录用户）
CREATE TABLE IF NOT EXISTS employee (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '员工ID',
    employee_no   VARCHAR(32)  NOT NULL COMMENT '工号',
    name          VARCHAR(64)  NOT NULL COMMENT '姓名',
    username      VARCHAR(64)           DEFAULT NULL COMMENT '登录用户名（可空，仅需登录的员工填写）',
    password      VARCHAR(100)          DEFAULT NULL COMMENT '登录密码（BCrypt 加密）',
    gender        TINYINT               DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
    phone         VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    email         VARCHAR(128)          DEFAULT NULL COMMENT '邮箱',
    department_id BIGINT                DEFAULT NULL COMMENT '所属部门ID',
    status        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-在职 0-离职',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_employee_no (employee_no),
    UNIQUE KEY uk_username (username),
    KEY idx_department_id (department_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '员工表';

-- 角色表（RBAC）
CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    code        VARCHAR(32)  NOT NULL COMMENT '角色编码',
    name        VARCHAR(64)  NOT NULL COMMENT '角色名称',
    description VARCHAR(255)          DEFAULT NULL COMMENT '角色描述',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '角色表';

-- 权限表（RBAC）
CREATE TABLE IF NOT EXISTS sys_permission (
    id   BIGINT      NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    code VARCHAR(64) NOT NULL COMMENT '权限编码',
    name VARCHAR(64) NOT NULL COMMENT '权限名称',
    PRIMARY KEY (id),
    UNIQUE KEY uk_permission_code (code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '权限表';

-- 员工-角色 关联表
CREATE TABLE IF NOT EXISTS employee_role (
    employee_id BIGINT NOT NULL COMMENT '员工ID',
    role_id     BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (employee_id, role_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '员工角色关联表';

-- 角色-权限 关联表
CREATE TABLE IF NOT EXISTS role_permission (
    role_id       BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    PRIMARY KEY (role_id, permission_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '角色权限关联表';

-- ============ 种子数据 ============

INSERT IGNORE INTO department (id, name, description) VALUES
    (1, '研发部', '负责产品研发'),
    (2, '人事部', '负责招聘与人事管理'),
    (3, '市场部', '负责市场推广');

INSERT IGNORE INTO employee (id, employee_no, name, gender, phone, email, department_id, status) VALUES
    (1, 'E001', '张三', 1, '13800000001', 'zhangsan@hrms.com', 1, 1),
    (2, 'E002', '李四', 2, '13800000002', 'lisi@hrms.com', 2, 1),
    (3, 'E003', '王五', 1, '13800000003', 'wangwu@hrms.com', 1, 1);

INSERT IGNORE INTO sys_role (id, code, name, description) VALUES
    (1, 'ADMIN', '管理员', '拥有全部权限'),
    (2, 'HR', '人事', '可查看、新增、修改员工'),
    (3, 'EMPLOYEE', '普通员工', '仅可查看');

INSERT IGNORE INTO sys_permission (id, code, name) VALUES
    (1, 'employee:read', '查看员工'),
    (2, 'employee:write', '新增/修改员工'),
    (3, 'employee:delete', '删除员工'),
    (4, 'department:read', '查看部门');

-- 角色 -> 权限 映射
INSERT IGNORE INTO role_permission (role_id, permission_id) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4),   -- ADMIN：全部
    (2, 1), (2, 2), (2, 4),           -- HR：查看/新增/修改员工、查看部门
    (3, 1), (3, 4);                   -- EMPLOYEE：查看员工、查看部门

-- 说明：登录账号（admin/hr/user）由 DataInitializer 在启动时创建，密码经 BCrypt 加密后入库
