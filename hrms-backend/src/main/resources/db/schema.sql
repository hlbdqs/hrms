-- ============================================================
-- 智能人事管理系统 数据库初始化脚本（实验一）
-- 说明：幂等脚本，可重复执行（CREATE TABLE IF NOT EXISTS + INSERT IGNORE）
-- ============================================================

CREATE TABLE IF NOT EXISTS department (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    name        VARCHAR(64)  NOT NULL COMMENT '部门名称',
    description VARCHAR(255)          DEFAULT NULL COMMENT '部门描述',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_department_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '部门表';

CREATE TABLE IF NOT EXISTS employee (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '员工ID',
    employee_no   VARCHAR(32)  NOT NULL COMMENT '工号',
    name          VARCHAR(64)  NOT NULL COMMENT '姓名',
    gender        TINYINT               DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
    phone         VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    email         VARCHAR(128)          DEFAULT NULL COMMENT '邮箱',
    department_id BIGINT                DEFAULT NULL COMMENT '所属部门ID',
    status        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-在职 0-离职',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_employee_no (employee_no),
    KEY idx_department_id (department_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '员工表';

-- 种子数据
INSERT IGNORE INTO department (id, name, description) VALUES
    (1, '研发部', '负责产品研发'),
    (2, '人事部', '负责招聘与人事管理'),
    (3, '市场部', '负责市场推广');

INSERT IGNORE INTO employee (id, employee_no, name, gender, phone, email, department_id, status) VALUES
    (1, 'E001', '张三', 1, '13800000001', 'zhangsan@hrms.com', 1, 1),
    (2, 'E002', '李四', 2, '13800000002', 'lisi@hrms.com', 2, 1),
    (3, 'E003', '王五', 1, '13800000003', 'wangwu@hrms.com', 1, 1);
