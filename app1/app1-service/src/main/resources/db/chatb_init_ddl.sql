-- ======================================================================
-- MySQL企业级系统数据库表结构
-- 创建日期: 2023-07-10
-- 初始部门架构设计：
-- AI APPS (001, Jackpot)
-- │
-- ├── AI APPS全球销售 (002, Thomas)
-- │   ├── Emma Johnson (EMP_006, 高级销售经理)
-- │   ├── Michael Smith (EMP_007, 销售经理)
-- │   └── Sophie Williams (EMP_008, 销售专员)
-- │
-- ├── AI APPS国内销售 (003, 张云逸)
-- │   ├── 王建国 (EMP_009, 销售经理)
-- │   ├── 刘芳 (EMP_010, 高级销售代表)
-- │   └── 陈志强 (EMP_011, 销售代表)
-- │
-- ├── 产品研发部 (004, 李俊)
-- │   ├── 郑小龙 (EMP_012, 架构师)
-- │   ├── 林晓彤 (EMP_013, 高级开发工程师)
-- │   ├── 黄伟 (EMP_014, 开发工程师)
-- │   └── 马思远 (EMP_015, 初级开发工程师)
-- │
-- └── 人力组织&行政部 (005, 赵明月)
--     ├── 朱丽 (EMP_016, HR经理)
--     ├── 吴建华 (EMP_017, 行政主管)
--     └── 苏梦琪 (EMP_018, HR专员)
-- ======================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 员工基本信息表
-- ----------------------------
DROP TABLE IF EXISTS `emp_basic`;
CREATE TABLE `emp_basic`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `work_no`      varchar(32) NOT NULL COMMENT '工号',
    `name`         varchar(64) NOT NULL COMMENT '姓名',
    `gender`       char(1)              DEFAULT NULL COMMENT '性别(M/F)',
    `birth_date`   date                 DEFAULT NULL COMMENT '出生日期',
    `dept_no`      varchar(32)          DEFAULT NULL COMMENT '部门编号',
    `entry_date`   date                 DEFAULT NULL COMMENT '入职日期',
    `position`     varchar(64)          DEFAULT NULL COMMENT '岗位',
    `level`        varchar(32)          DEFAULT NULL COMMENT '职级',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_work_no` (`work_no`),
    KEY `idx_dept_no` (`dept_no`),
    KEY `idx_entry_date` (`entry_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='员工基本信息表';

-- ----------------------------
-- 2. 员工学历背景表
-- ----------------------------
DROP TABLE IF EXISTS `emp_edu`;
CREATE TABLE `emp_edu`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `work_no`      varchar(32) NOT NULL COMMENT '工号',
    `college`      varchar(128)         DEFAULT NULL COMMENT '毕业院校',
    `coll_level`   varchar(32)          DEFAULT NULL COMMENT '院校级别(C9/985/211/普通)',
    `degree`       varchar(32)          DEFAULT NULL COMMENT '最高学历(博士/硕士/本科/专科)',
    `major`        varchar(64)          DEFAULT NULL COMMENT '专业',
    `grad_date`    date                 DEFAULT NULL COMMENT '毕业日期',
    `edu_years`    int(11)              DEFAULT NULL COMMENT '教育年限',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_work_no` (`work_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='员工学历背景表';

-- ----------------------------
-- 3. 员工业绩收入和成本表
-- ----------------------------
DROP TABLE IF EXISTS `emp_income`;
CREATE TABLE `emp_income`
(
    `id`             bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `work_no`        varchar(32) NOT NULL COMMENT '工号',
    `gaap_income`    decimal(12, 2)       DEFAULT NULL COMMENT 'GAAP收入',
    `month_gaap`     decimal(12, 2)       DEFAULT NULL COMMENT '月度GAAP收入',
    `gaap_target`    decimal(12, 2)       DEFAULT NULL COMMENT 'GAAP目标',
    `gaap_comp_rate` decimal(5, 2)        DEFAULT NULL COMMENT 'GAAP目标完成率',
    `travel_days`    int(11)              DEFAULT NULL COMMENT '出差天数',
    `travel_cost`    decimal(10, 2)       DEFAULT NULL COMMENT '差旅费',
    `proj_income`    decimal(12, 2)       DEFAULT NULL COMMENT '项目收入',
    `cost_total`     decimal(12, 2)       DEFAULT NULL COMMENT '总成本',
    `gmt_create`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_work_no` (`work_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='员工业绩收入和成本表';

-- ----------------------------
-- 4. 员工绩效表现表
-- ----------------------------
DROP TABLE IF EXISTS `emp_perf`;
CREATE TABLE `emp_perf`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `work_no`      varchar(32) NOT NULL COMMENT '工号',
    `perf_year`         int(4)      NOT NULL COMMENT '年份',
    `perf_level`   char(1)              DEFAULT NULL COMMENT '绩效等级(A/B/C/D)',
    `perf_score`   decimal(5, 2)        DEFAULT NULL COMMENT '绩效分数',
    `bonus_rate`   decimal(5, 2)        DEFAULT NULL COMMENT '奖金系数',
    `evaluation`   text COMMENT '评价',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_work_no_perf_year` (`work_no`, `perf_year`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='员工绩效表现表';

-- ----------------------------
-- 5. 部门基本信息表
-- ----------------------------
DROP TABLE IF EXISTS `dept_info`;
CREATE TABLE `dept_info`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dept_no`      varchar(32) NOT NULL COMMENT '部门编号',
    `dept_name`    varchar(64) NOT NULL COMMENT '部门名称',
    `mgr_no`       varchar(32)          DEFAULT NULL COMMENT '部门主管工号',
    `mgr_name`     varchar(64)          DEFAULT NULL COMMENT '部门主管姓名',
    `create_date`  date                 DEFAULT NULL COMMENT '创建日期',
    `parent_dept`  varchar(32)          DEFAULT NULL COMMENT '上级部门编号',
    `dept_level`   int(11)              DEFAULT NULL COMMENT '部门级别',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_no` (`dept_no`),
    KEY `idx_parent_dept` (`parent_dept`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门基本信息表';

-- ----------------------------
-- 6. 部门业绩收入表
-- ----------------------------
DROP TABLE IF EXISTS `dept_income`;
CREATE TABLE `dept_income`
(
    `id`             bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dept_no`        varchar(32) NOT NULL COMMENT '部门编号',
    `period_code`    varchar(20) NOT NULL COMMENT '统计周期编码(如fy2026、202502、20250213)',
    `period_type`    varchar(10) NOT NULL COMMENT '统计周期类型(fy/month/day)',
    `year`           int(4)               DEFAULT NULL COMMENT '年份',
    `quarter`        int(1)               DEFAULT NULL COMMENT '季度',
    `month`          int(2)               DEFAULT NULL COMMENT '月份',
    `gaap_income`    decimal(12, 2)       DEFAULT NULL COMMENT 'GAAP收入',
    `q_gaap`         decimal(12, 2)       DEFAULT NULL COMMENT '季度GAAP',
    `month_gaap`     decimal(12, 2)       DEFAULT NULL COMMENT '月度GAAP收入',
    `gaap_target`    decimal(12, 2)       DEFAULT NULL COMMENT 'GAAP目标',
    `gaap_comp_rate` decimal(5, 2)        DEFAULT NULL COMMENT 'GAAP目标完成率',
    `target_rate`    decimal(5, 2)        DEFAULT NULL COMMENT '目标完成率',
    `profit`         decimal(12, 2)       DEFAULT NULL COMMENT '利润额',
    `gmt_create`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_dept_period` (`dept_no`, `period_code`, `period_type`),
    KEY `idx_period` (`period_code`, `period_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门业绩收入表';

-- ----------------------------
-- 7. 部门人员入离职统计信息表
-- ----------------------------
DROP TABLE IF EXISTS `dept_staff`;
CREATE TABLE `dept_staff`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dept_no`      varchar(32) NOT NULL COMMENT '部门编号',
    `period_code`  varchar(20) NOT NULL COMMENT '统计周期编码(如fy2026、202502、20250213)',
    `period_type`  varchar(10) NOT NULL COMMENT '统计周期类型(fy财年/month月/day日)',
    `year`         int(4)               DEFAULT NULL COMMENT '年份',
    `turnover`     decimal(5, 2)        DEFAULT NULL COMMENT '离职率',
    `hire_count`   int(11)              DEFAULT NULL COMMENT '入职人数',
    `leave_count`  int(11)              DEFAULT NULL COMMENT '离职人数',
    `avg_tenure`   decimal(5, 2)        DEFAULT NULL COMMENT '平均司龄',
    `headcount`    int(11)              DEFAULT NULL COMMENT '总人数',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_dept_period` (`dept_no`, `period_code`, `period_type`),
    KEY `idx_period` (`period_code`, `period_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门人员入离职统计信息表';

-- ----------------------------
-- 8. 部门学历背景统计表
-- ----------------------------
DROP TABLE IF EXISTS `dept_edu`;
CREATE TABLE `dept_edu`
(
    `id`               bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dept_no`          varchar(32) NOT NULL COMMENT '部门编号',
    `period_code`      varchar(20) NOT NULL COMMENT '统计周期编码(如fy2026、202502、20250213)',
    `period_type`      varchar(10) NOT NULL COMMENT '统计周期类型(fy财年/month月/day日)',
    `year`             int(4)               DEFAULT NULL COMMENT '年份',
    `school_c9_count`  int(11)              DEFAULT NULL COMMENT 'C9院校人数',
    `school_985_count` int(11)              DEFAULT NULL COMMENT '985院校人数',
    `school_211_count` int(11)              DEFAULT NULL COMMENT '211院校人数',
    `master_above`     int(11)              DEFAULT NULL COMMENT '硕士及以上人数',
    `gmt_create`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_dept_period` (`dept_no`, `period_code`, `period_type`),
    KEY `idx_period` (`period_code`, `period_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门学历背景统计表';

SET FOREIGN_KEY_CHECKS = 1;






-- 为AI APPS国内销售部门(003)生成年度部门业绩统计数据
INSERT INTO dept_income
(
    dept_no,
    period_code,
    period_type,
    year,
    gaap_income,
    month_gaap,
    gaap_target,
    gaap_comp_rate,
    target_rate,
    profit
)
SELECT
    '003' AS dept_no,
    'fy2023' AS period_code,
    'fy' AS period_type,
    2023 AS year,
    SUM(gaap_income) AS gaap_income,
    SUM(month_gaap) AS month_gaap,
    SUM(gaap_target) AS gaap_target,
    (SUM(gaap_income) / SUM(gaap_target)) * 100 AS gaap_comp_rate,
    (SUM(gaap_income) / SUM(gaap_target)) * 100 AS target_rate,
    SUM(proj_income) - SUM(cost_total) AS profit
FROM emp_income
WHERE work_no IN ('EMP_003', 'EMP_009', 'EMP_010', 'EMP_011');