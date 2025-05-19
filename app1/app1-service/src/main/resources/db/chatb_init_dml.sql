-- AI APPS及下属部门数据插入SQL
-- ----------------------------

-- 1. AI APPS(顶层组织)
INSERT INTO `dept_info` (`dept_no`, `dept_name`, `mgr_no`, `mgr_name`, `create_date`, `parent_dept`, `dept_level`, `gmt_create`, `gmt_modified`)
VALUES ('001', 'AI APPS', 'EMP_001', 'Jackpot', '2023-01-01', NULL, 1, NOW(), NOW());

-- 2. AI APPS全球销售
INSERT INTO `dept_info` (`dept_no`, `dept_name`, `mgr_no`, `mgr_name`, `create_date`, `parent_dept`, `dept_level`, `gmt_create`, `gmt_modified`)
VALUES ('002', 'AI APPS全球销售', 'EMP_002', 'Thomas', '2023-01-01', '001', 2, NOW(), NOW());

-- 3. AI APPS国内销售
INSERT INTO `dept_info` (`dept_no`, `dept_name`, `mgr_no`, `mgr_name`, `create_date`, `parent_dept`, `dept_level`, `gmt_create`, `gmt_modified`)
VALUES ('003', 'AI APPS国内销售', 'EMP_003', '张云逸', '2023-01-01', '001', 2, NOW(), NOW());

-- 4. 产品研发部
INSERT INTO `dept_info` (`dept_no`, `dept_name`, `mgr_no`, `mgr_name`, `create_date`, `parent_dept`, `dept_level`, `gmt_create`, `gmt_modified`)
VALUES ('004', '产品研发部', 'EMP_004', '李俊', '2023-01-01', '001', 2, NOW(), NOW());

-- 5. 人力组织&行政部
INSERT INTO `dept_info` (`dept_no`, `dept_name`, `mgr_no`, `mgr_name`, `create_date`, `parent_dept`, `dept_level`, `gmt_create`, `gmt_modified`)
VALUES ('005', '人力组织&行政部', 'EMP_005', '赵明月', '2023-01-01', '001', 2, NOW(), NOW());



#
-- 员工基本信息表数据插入SQL
# AI APPS (001, Jackpot)
# │
# ├── AI APPS全球销售 (002, Thomas)
# │   ├── Emma Johnson (EMP_006, 高级销售经理)
# │   ├── Michael Smith (EMP_007, 销售经理)
# │   └── Sophie Williams (EMP_008, 销售专员)
# │
# ├── AI APPS国内销售 (003, 张云逸)
# │   ├── 王建国 (EMP_009, 销售经理)
# │   ├── 刘芳 (EMP_010, 高级销售代表)
# │   └── 陈志强 (EMP_011, 销售代表)
# │
# ├── 产品研发部 (004, 李俊)
# │   ├── 郑小龙 (EMP_012, 架构师)
# │   ├── 林晓彤 (EMP_013, 高级开发工程师)
# │   ├── 黄伟 (EMP_014, 开发工程师)
# │   └── 马思远 (EMP_015, 初级开发工程师)
# │
# └── 人力组织&行政部 (005, 赵明月)
#     ├── 朱丽 (EMP_016, HR经理)
#     ├── 吴建华 (EMP_017, 行政主管)
#     └── 苏梦琪 (EMP_018, HR专员)
-- ----------------------------

-- 部门主管信息插入
-- 1. AI APPS主管
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_001', 'Jackpot', 'M', '1975-06-15', '001', '2020-01-01', '首席执行官', 'E5', NOW(), NOW());

-- 2. AI APPS全球销售主管
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_002', 'Thomas', 'M', '1978-03-22', '002', '2020-02-15', '全球销售总监', 'E4', NOW(), NOW());

-- 3. AI APPS国内销售主管
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_003', '张云逸', 'M', '1980-11-08', '003', '2020-03-01', '国内销售总监', 'E4', NOW(), NOW());

-- 4. 产品研发部主管
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_004', '李俊', 'M', '1982-05-19', '004', '2021-01-10', '技术总监', 'E4', NOW(), NOW());

-- 5. 人力组织&行政部主管
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_005', '赵明月', 'F', '1983-09-27', '005', '2021-02-01', 'HR总监', 'E4', NOW(), NOW());

-- 普通员工信息插入
-- AI APPS全球销售部员工
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_006', 'Emma Johnson', 'F', '1985-04-12', '002', '2021-03-15', '高级销售经理', 'E3', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_007', 'Michael Smith', 'M', '1988-07-23', '002', '2021-06-01', '销售经理', 'E3', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_008', 'Sophie Williams', 'F', '1990-02-18', '002', '2022-01-10', '销售专员', 'E2', NOW(), NOW());

-- AI APPS国内销售部员工
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_009', '王建国', 'M', '1986-10-01', '003', '2021-04-12', '销售经理', 'E3', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_010', '刘芳', 'F', '1989-12-05', '003', '2021-07-15', '高级销售代表', 'E2', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_011', '陈志强', 'M', '1992-08-30', '003', '2022-02-01', '销售代表', 'E1', NOW(), NOW());

-- 产品研发部员工
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_012', '郑小龙', 'M', '1984-05-17', '004', '2021-03-01', '架构师', 'E3', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_013', '林晓彤', 'F', '1987-11-22', '004', '2021-08-15', '高级开发工程师', 'E3', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_014', '黄伟', 'M', '1991-03-07', '004', '2022-01-18', '开发工程师', 'E2', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_015', '马思远', 'M', '1993-07-14', '004', '2022-03-10', '初级开发工程师', 'E1', NOW(), NOW());

-- 人力组织&行政部员工
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_016', '朱丽', 'F', '1986-08-25', '005', '2021-05-17', 'HR经理', 'E3', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_017', '吴建华', 'M', '1988-09-30', '005', '2021-09-01', '行政主管', 'E2', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_018', '苏梦琪', 'F', '1992-12-17', '005', '2022-02-15', 'HR专员', 'E1', NOW(), NOW());

-- AI APPS直属员工
INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_019', 'David Chen', 'M', '1983-01-05', '001', '2020-05-01', '战略总监', 'E4', NOW(), NOW());

INSERT INTO `emp_basic` (`work_no`, `name`, `gender`, `birth_date`, `dept_no`, `entry_date`, `position`, `level`, `gmt_create`, `gmt_modified`)
VALUES ('EMP_020', '何佳宁', 'F', '1987-04-19', '001', '2021-02-25', '财务总监', 'E4', NOW(), NOW());




#---------------------------------------------------------------------------------------------
#----------------------------       业绩收入成本          --------------------------------------
#---------------------------------------------------------------------------------------------

-- 张云逸 (EMP_003, 国内销售总监) 的业绩数据
INSERT INTO `emp_income`
(`work_no`, `gaap_income`, `month_gaap`, `gaap_target`, `gaap_comp_rate`, `travel_days`, `travel_cost`, `proj_income`, `cost_total`)
VALUES
    ('EMP_003', 12500000.00, 1041666.67, 12000000.00, 104.17, 85, 127500.00, 10800000.00, 420000.00);

-- 王建国 (EMP_009, 销售经理) 的业绩数据
INSERT INTO `emp_income`
(`work_no`, `gaap_income`, `month_gaap`, `gaap_target`, `gaap_comp_rate`, `travel_days`, `travel_cost`, `proj_income`, `cost_total`)
VALUES
    ('EMP_009', 6800000.00, 566666.67, 6500000.00, 104.62, 62, 93000.00, 5850000.00, 245000.00);

-- 刘芳 (EMP_010, 高级销售代表) 的业绩数据
INSERT INTO `emp_income`
(`work_no`, `gaap_income`, `month_gaap`, `gaap_target`, `gaap_comp_rate`, `travel_days`, `travel_cost`, `proj_income`, `cost_total`)
VALUES
    ('EMP_010', 4200000.00, 350000.00, 4000000.00, 105.00, 48, 72000.00, 3600000.00, 180000.00);

-- 陈志强 (EMP_011, 销售代表) 的业绩数据
INSERT INTO `emp_income`
(`work_no`, `gaap_income`, `month_gaap`, `gaap_target`, `gaap_comp_rate`, `travel_days`, `travel_cost`, `proj_income`, `cost_total`)
VALUES
    ('EMP_011', 2850000.00, 237500.00, 3000000.00, 95.00, 36, 54000.00, 2565000.00, 128250.00);



#---------------------------------------------------------------------------------------------
#----------------------------       学历背景信息          --------------------------------------
#---------------------------------------------------------------------------------------------

-- 张云逸 (EMP_003, 国内销售总监) 的学历背景
INSERT INTO `emp_edu`
(`work_no`, `college`, `coll_level`, `degree`, `major`, `grad_date`, `edu_years`)
VALUES
    ('EMP_003', '清华大学', 'C9', '博士', '工商管理', '2005-07-01', 22);

-- 王建国 (EMP_009, 销售经理) 的学历背景
INSERT INTO `emp_edu`
(`work_no`, `college`, `coll_level`, `degree`, `major`, `grad_date`, `edu_years`)
VALUES
    ('EMP_009', '上海交通大学', '985', '硕士', '市场营销', '2010-07-01', 17);

-- 刘芳 (EMP_010, 高级销售代表) 的学历背景
INSERT INTO `emp_edu`
(`work_no`, `college`, `coll_level`, `degree`, `major`, `grad_date`, `edu_years`)
VALUES
    ('EMP_010', '南京大学', '211', '本科', '国际商务', '2015-06-15', 16);

-- 陈志强 (EMP_011, 销售代表) 的学历背景
INSERT INTO `emp_edu`
(`work_no`, `college`, `coll_level`, `degree`, `major`, `grad_date`, `edu_years`)
VALUES
    ('EMP_011', '华南理工大学', '211', '本科', '电子商务', '2018-06-30', 16);


#---------------------------------------------------------------------------------------------
#----------------------------       学历背景信息          --------------------------------------
#---------------------------------------------------------------------------------------------

-- 张云逸 (EMP_003, 国内销售总监) 的绩效数据
INSERT INTO `emp_perf`
(`work_no`, `perf_year`, `perf_level`, `perf_score`, `bonus_rate`, `evaluation`)
VALUES
    ('EMP_003', 2021, 'A', 95.40, 1.80, '作为销售总监，张云逸带领团队超额完成了年度销售目标，建立了4个战略级客户关系，并成功开拓了西南市场。在疫情期间，快速调整销售策略，确保了业绩稳步增长。'),
    ('EMP_003', 2022, 'A', 93.20, 1.70, '张总监在本年度实现了销售额同比增长15%的优异成绩，成功引进了两个核心客户，为公司贡献了超过3000万的收入。在产品转型期，积极推动团队适应新产品线，培养了多名业绩突出的销售人员。'),
    ('EMP_003', 2023, 'A', 94.10, 1.75, '张总监继续保持卓越的领导力，团队业绩在整个公司排名第一。他个人负责的战略客户续约率达95%，并建立了完善的销售培训体系。在激烈的市场竞争中，成功维护了公司的市场份额和品牌影响力。');

-- 王建国 (EMP_009, 销售经理) 的绩效数据
INSERT INTO `emp_perf`
(`work_no`, `perf_year`, `perf_level`, `perf_score`, `bonus_rate`, `evaluation`)
VALUES
    ('EMP_009', 2021, 'B', 87.50, 1.30, '王经理在本年度表现稳定，团队完成销售目标的103%。他个人开发了5个中型客户，建立了完善的客户跟进机制。需要在大客户开发上进一步提升能力。'),
    ('EMP_009', 2022, 'A', 92.10, 1.65, '王建国在本年度表现突出，成功带领团队突破困境，实现销售额超目标15%的优异成绩。他建立的销售流程优化方案帮助团队提升了30%的效率，并成功开发了两个年合同额过千万的大客户。'),
    ('EMP_009', 2023, 'A', 91.80, 1.60, '王经理继续保持优秀业绩，他负责的销售团队达成目标率104.6%。他改进的客户服务模式获得了客户高度评价，客户满意度提升至92%。在新产品推广中表现积极，为公司贡献了可观的增量收入。');

-- 刘芳 (EMP_010, 高级销售代表) 的绩效数据
INSERT INTO `emp_perf`
(`work_no`, `perf_year`, `perf_level`, `perf_score`, `bonus_rate`, `evaluation`)
VALUES
    ('EMP_010', 2021, 'B', 83.20, 1.15, '刘芳作为资深销售代表，年度业绩达成率为95%，略低于目标。在维护现有客户方面表现出色，但新客户开发数量未达预期。客户满意度评价良好，需加强解决方案销售能力。'),
    ('EMP_010', 2022, 'B', 88.60, 1.35, '刘芳在本年度有明显进步，业绩达成率提升至102%。她成功转化了3个重要潜在客户，并通过交叉销售为现有客户带来增值服务。她的方案销售能力有显著提升，客户反馈积极正面。'),
    ('EMP_010', 2023, 'A', 90.30, 1.50, '刘芳本年度表现优异，业绩完成率105%，成功晋升为高级销售代表。她开发的行业解决方案被公司采纳为标准销售模板。客户续约率高达95%，展现出卓越的客户关系管理能力和专业素养。');

-- 陈志强 (EMP_011, 销售代表) 的绩效数据
INSERT INTO `emp_perf`
(`work_no`, `perf_year`, `perf_level`, `perf_score`, `bonus_rate`, `evaluation`)
VALUES
    ('EMP_011', 2021, 'C', 75.80, 0.80, '陈志强作为新入职的销售代表，在适应期表现尚可，基本掌握了产品知识和销售流程。业绩完成率为85%，未达到预期目标。需要加强销售技巧和客户沟通能力，建议参加公司内部培训项目。'),
    ('EMP_011', 2022, 'C', 78.40, 0.85, '陈志强在第二年有所进步，业绩完成率提升至88%。对产品理解更加深入，客户演示质量有所提高。需要继续加强主动开发客户的能力和销售谈判技巧，建议安排跟随资深销售学习。'),
    ('EMP_011', 2023, 'B', 82.70, 1.10, '陈志强本年度取得明显进步，业绩完成率达到95%。成功独立开发了4个中小型客户，客户反馈积极。销售技巧和解决方案设计能力有显著提升，展现出很好的发展潜力，有望在下一年度实现更大突破。');