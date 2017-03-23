-- 系统的基本薪资规则表
CREATE TABLE `dingsal_sys_salrule` (
	`id` VARCHAR(32) NOT NULL COMMENT '薪资规则的标识 -- id',
	`sal_rule_des` VARCHAR(200) NOT NULL COMMENT '薪资规则描述 -- salRuleDes',
	`sal_rule_name` VARCHAR(50) NOT NULL COMMENT '薪资规则的名字 -- salRuleName',
	`sal_rule_type` INT(11) NULL DEFAULT NULL COMMENT '薪资规则的类型 正算是1 ，反算是0-- salRuleType ',
	`create_date` DATETIME NOT NULL COMMENT '系统薪资规则创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '系统薪资规则更改时间 -- modifiedDate',
	PRIMARY KEY (`id`)
)
COMMENT='系统薪资规则--叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



-- 插入系统的考勤规则
INSERT INTO `dingsal_sys_salrule` VALUES ('sys_front_ruleid', '日工资=固定工资/21.75（符合法规要求的简单算法，应发工资=日工资*出勤天数,如果计算结果低日工资，将自动使用反算）', '简单计算-正选', 1, now(), now(), NULL);
INSERT INTO `dingsal_sys_salrule` VALUES ('sys_inverse_ruleid', '日工资=固定工资/21.75（符合法规要求的简单算法，应发工资=月工资-日工资*未出勤天数,如果计算结果低于0，将自动使用正算））', '简单计算-反选', 0,now(),now(), NULL);
INSERT INTO `dingsal_sys_salrule` VALUES ('sys_whp_ruleid', '代缴代扣', '代缴代扣', 2, now(), now(), NULL);



-- 企业或系统的自定义的字段表
CREATE TABLE `dingsal_sys_fielditem` (
	`id` VARCHAR(32) NOT NULL COMMENT '字段标识 -- itemId',
	`item_name` VARCHAR(50) NOT NULL COMMENT '员工薪资字段名字 -- itemName',
	`item_value` DOUBLE NULL DEFAULT NULL COMMENT '字段值 -- itemValue',
	`item_type` INT(11) NULL DEFAULT NULL COMMENT '字段的类型',
	`relative_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '与之绑定的ID -- relativeId',
	`create_date` DATETIME NOT NULL COMMENT '创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '更新时间 -- modifiedDate',
	`deduct_type` INT(11) NULL DEFAULT NULL COMMENT '扣款方式 -- deductType',
	`corp_id` VARCHAR(60) NULL DEFAULT NULL COMMENT '企业钉钉ID-- corp_id',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_sysFieldItem_corpId_relativeId` (`corp_id`, `relative_id`) USING BTREE
)
COMMENT='自定义字段表--叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


-- 插入基本社保公积金的一些固定的字段
INSERT INTO `dingsal_sys_fielditem` VALUES ('endowment_insurance', '养老', NULL, NULL, NULL, now(), now(), NULL, NULL);
INSERT INTO `dingsal_sys_fielditem` VALUES ('industrial_injury_insurance', '工伤', NULL, NULL, NULL, now(),now(), NULL, NULL);
INSERT INTO `dingsal_sys_fielditem` VALUES ('maternity insurance', '生育', NULL, NULL, NULL, now(),now(), NULL, NULL);
INSERT INTO `dingsal_sys_fielditem` VALUES ('medical_insurance', '医疗', NULL, NULL, NULL,now(),now(), NULL, NULL);
INSERT INTO `dingsal_sys_fielditem` VALUES ('pub_funds', '公积金', NULL, NULL, NULL,now(),now(), NULL, NULL);
INSERT INTO `dingsal_sys_fielditem` VALUES ('serious_illness_insurance', '大病医疗', NULL, NULL, NULL,now(),now(), NULL, NULL);
INSERT INTO `dingsal_sys_fielditem` VALUES ('unemploy_insurance', '失业', NULL, NULL, NULL,now(),now(), NULL, NULL);

-- 企业基本信息表
CREATE TABLE `dingsal_corp_info` (
	`id` VARCHAR(32) NOT NULL COMMENT '叮当薪资企业信息的Id -- id',
	`create_date` DATETIME NOT NULL COMMENT '企业记录的创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '企业记录的更改时间 -- modifiedDate',
	`pass_state` INT(11) NULL DEFAULT '0' COMMENT '企业密码锁开启的状态 -- passState',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '企业在钉钉中的id  -- dingCorpId',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_corpInfo_corpId_passState` (`corp_id`, `pass_state`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



-- 企业月度薪资报表
CREATE TABLE `dingsal_corp_salreport` (
	`id` VARCHAR(32) NOT NULL COMMENT '键，工资报表的唯一标识 -- id',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '企业唯一标识 -- corpId',
	`sal_report_state` INT(11) NOT NULL DEFAULT '0' COMMENT '工资报表的状态,分为预估，待审批和锁定 -- salReportState',
	`should_pay_sal` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '应发工资 -- shouldPaySal',
	`insurance_sal` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '公司交金 -- insuranceSal',
	`staff_cost` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '员工成本 -- staffCost',
	`create_date` DATETIME NOT NULL COMMENT '工资报表的创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '工资报表的更改时间 -- modifiedDate',
	`month_time` DATE NOT NULL COMMENT '工资报表的月份 -- monthTime',
	`actual_pay_sal` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '实发工资 -- actualPaySal',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_corpSalReport_corpId_monthTime_salReportState` (`corp_id`, `month_time`, `sal_report_state`) USING BTREE
)
COMMENT='月度工资报表--叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



-- 企业月度考勤报表
CREATE TABLE `dingsal_corp_attenreport` (
	`id` VARCHAR(32) NOT NULL COMMENT '企业考勤表唯一标识 -- attendanceId',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '考勤表所属的企业 -- corpId',
	`create_date` DATETIME NOT NULL COMMENT '考勤表创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '考勤表更改时间 -- modifiedDate',
	`month_time` DATE NOT NULL COMMENT '考勤报表的月份 -- monthTime',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_corpAttenReport_corpId_monthTime` (`corp_id`, `month_time`) USING BTREE
)
COMMENT='企业考勤报表--叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



-- 企业基本薪资规则表
CREATE TABLE `dingsal_corp_baserule` (
	`id` VARCHAR(32) NOT NULL COMMENT '具体企业的薪资规则标识 -- id',
	`cal_sal_days` INT(11) NOT NULL DEFAULT '22' COMMENT '计算薪资规则的天数 -- calSalDays',
	`fit_nums` INT(11) NOT NULL COMMENT '薪资规则的适用人数 -- fit_nums',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '叮当薪资企业标识 -- corpId',
	`create_date` DATETIME NOT NULL COMMENT '企业薪资规则创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '企业薪资规则更改时间 -- modifiedDate',
	`sal_rule_id` VARCHAR(32) NOT NULL COMMENT '薪资规则的标识 -- salRuleId',
	`has_set` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '基本规则是否有设置过 --hasSet',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_corpBaseRule_corpId_salRuleId` (`corp_id`, `sal_rule_id`) USING BTREE
)
COMMENT='企业基本薪资规则表--叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



-- 企业社保和公积金表
CREATE TABLE `dingsal_corp_whprule` (
	`id` VARCHAR(32) NOT NULL COMMENT '代缴代扣薪资规则标识 -- id',
	`subject_id` VARCHAR(30) NOT NULL COMMENT '缴纳科目标识 -- subjectId',
	`base_low` INT(11) NULL DEFAULT NULL COMMENT '基数下限 -- baseLow',
	`base_high` INT(11) NULL DEFAULT NULL COMMENT '基数上限 -- baseHigh',
	`corp_percent` INT(11) NOT NULL DEFAULT '0' COMMENT '公司比例 -- corpPercent',
	`personal_percent` INT(11) NOT NULL DEFAULT '0' COMMENT '个人比例 -- personalPercent',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '钉钉企业标识 -- corpId',
	`create_date` DATETIME NOT NULL COMMENT '代缴代扣薪资规则创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '代缴代扣薪资规则更新时间 -- modifiedDate',
	`has_set` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已经设置代缴代扣的规则 -- hasSet',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_corpWhpRule_corpId_subjectId` (`corp_id`, `subject_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



-- 企业基本考勤扣款表
CREATE TABLE `dingsal_corp_baserule` (
	`id` VARCHAR(32) NOT NULL COMMENT '具体企业的薪资规则标识 -- id',
	`cal_sal_days` INT(11) NOT NULL DEFAULT '22' COMMENT '计算薪资规则的天数 -- calSalDays',
	`fit_nums` INT(11) NOT NULL COMMENT '薪资规则的适用人数 -- fit_nums',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '叮当薪资企业标识 -- corpId',
	`create_date` DATETIME NOT NULL COMMENT '企业薪资规则创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '企业薪资规则更改时间 -- modifiedDate',
	`sal_rule_id` VARCHAR(32) NOT NULL COMMENT '薪资规则的标识 -- salRuleId',
	`has_set` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '基本规则是否有设置过 --hasSet',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_corpBaseRule_corpId_salRuleId` (`corp_id`, `sal_rule_id`) USING BTREE
)
COMMENT='企业基本薪资规则表--叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


-- 员工基本信息表
CREATE TABLE `dingsal_staff_info` (
	`id` VARCHAR(32) NOT NULL COMMENT '叮当薪资员工ID -- dingsalStaffId',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '叮当薪资企业标识 -- dingsalCorpId',
	`corp_bs_ruleid` VARCHAR(32) NULL DEFAULT NULL COMMENT '薪资规则标识 -- corpBaseRuleId',
	`ding_staffid` VARCHAR(32) NOT NULL COMMENT '钉钉员工号',
	`staff_pass` VARCHAR(32) NULL DEFAULT NULL COMMENT '员工密码锁密码 -- staffPass',
	`atten_social` INT(11) NULL DEFAULT NULL COMMENT '社保公积金是否参与 1是参与,0是不参与 -- attenSocial',
	`atten_personal_tax` INT(11) NULL DEFAULT NULL COMMENT '个人所得税是否参与缴税 1是参与 ,0 是不参与 -- attenPersonalTax',
	`should_pay_sal` INT(11) NULL DEFAULT NULL COMMENT '应发工资 -- shouldPaySal',
	`create_date` DATETIME NOT NULL COMMENT '员工信息的创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '员工信息的更改时间 -- modifiedDate',
	`pass_state` INT(11) NULL DEFAULT NULL COMMENT '员工密码的状态：可选择开启密码或关闭密码 -- passState',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_staffInfo_corpId_staffId_bsRuleId` (`corp_id`, `ding_staffid`, `corp_bs_ruleid`) USING BTREE
)
COMMENT='员工信息表--叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


-- 员工月度考勤数据表
CREATE TABLE `dingsal_staff_attendance` (
	`id` VARCHAR(32) NOT NULL COMMENT '员工月度考勤标识 -- staffAttendanceId',
	`attendance_day` INT(11) NOT NULL DEFAULT '0' COMMENT '考勤天数 -- attendanceDay',
	`rest_days` INT(11) NOT NULL DEFAULT '0' COMMENT '休息天数 -- restDays',
	`work_hours` INT(11) NOT NULL DEFAULT '0' COMMENT '工作天数 -- workHours',
	`late_hours` INT(11) NOT NULL DEFAULT '0' COMMENT '迟到时长 -- lateHours',
	`late_times` INT(11) NOT NULL DEFAULT '0' COMMENT '迟到次数 -- lateTimes',
	`seriouslate_hours` INT(11) NOT NULL DEFAULT '0' COMMENT '严重迟到时长  -- seriouslateHours',
	`seriouslate_times` INT(11) NOT NULL DEFAULT '0' COMMENT '严重迟到次数 -- seriousLateTimes',
	`earlyleave_hours` INT(11) NOT NULL DEFAULT '0' COMMENT '早退时长 --eLHours',
	`earlyleave_times` INT(11) NOT NULL DEFAULT '0' COMMENT '早退次数 -- eLTimes',
	`workabsence_times` INT(11) NOT NULL DEFAULT '0' COMMENT '上班缺卡次数 -- workAbsenceTimes',
	`offwork_absence_times` INT(11) NOT NULL DEFAULT '0' COMMENT '下班缺卡次数 -- offAbsenceTimes',
	`unwork_days` INT(11) NOT NULL DEFAULT '0' COMMENT '旷工天数 -- unworkDays',
	`unwork_late_days` INT(11) NOT NULL DEFAULT '0' COMMENT '旷工迟到天数 -- unworkLateDays',
	`ding_staffid` VARCHAR(32) NOT NULL COMMENT '员工标识 --dingStaffId',
	`atten_report_id` VARCHAR(32) NOT NULL COMMENT '月度考勤报表标识 --reportId',
	`create_date` DATETIME NOT NULL COMMENT '员工月度考勤表创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '员工月度考勤表更改时间 -- modifiedDate',
	`month_time` DATE NOT NULL COMMENT '员工月度考勤报表的月份 -- monthTime',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '企业钉钉ID -- corpId',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_staffAttendance_corpId_staffId_reportId_monthTime` (`corp_id`, `ding_staffid`, `atten_report_id`, `month_time`) USING BTREE
)
COMMENT='员工考勤数据 --叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



-- 员工月度薪资数据表
CREATE TABLE `dingsal_staff_monthsal` (
	`id` VARCHAR(32) NOT NULL COMMENT '员工月度薪资标识 -- id',
	`annual_bonus` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '年终奖 -- annualBonus',
	`month_bonus` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '当月奖金 -- monthBonus',
	`other_pretax_sal` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '其他税前补款 -- otherPretaxSal',
	`other_aftertax_sal` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '其他税后补款 -- otherAftertaxSal',
	`other_pretax_deduct` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '其他税前扣款 -- otherPretaxDeduct',
	`other_aftertax_deduct` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '其他税后扣款 -- otherAftertaxDeduct',
	`ding_staffid` VARCHAR(32) NOT NULL COMMENT '叮当薪资员工标识 -- dingStaffId',
	`sal_report_id` VARCHAR(32) NOT NULL COMMENT '当月薪资报表标识 -- salReportId',
	`should_pay_sal` BIGINT(20) NOT NULL COMMENT '应发工资 -- shouldPaySal',
	`sal_deduct` BIGINT(20) NULL DEFAULT '0' COMMENT '工资扣款 -- salDeduct',
	`replace_deduct` BIGINT(20) NULL DEFAULT '0' COMMENT '代扣代缴 -- replaceDeduct',
	`actual_sal` BIGINT(20) NOT NULL COMMENT '实际工资 -- actualSal',
	`create_date` DATETIME NOT NULL COMMENT '员工月度工资详情的创建时间 -- createDate',
	`modified_date` DATETIME NOT NULL COMMENT '员工月度工资详情的更改时间 -- modifiedDate',
	`month_time` DATE NOT NULL COMMENT '员工月度工资详情的月份 -- monthTime',
	`corp_insurance_sal` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '企业为员工所缴纳的社保和公积金的款项--corpInsuranceSal',
	`staff_insurance_sal` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '员工当月所缴纳的社保和公积金的款项 -- staffInsuranceSal',
	`tax_sal` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '员工当月所缴纳的个人所得税--taxSal',
	`atten_deduct` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '员工单月的考勤扣款 -- attenDeduct',
	`corp_id` VARCHAR(60) NOT NULL COMMENT '钉钉企业ID -- corpId',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_staffMonthSal_corpId_staffId_monthTime` (`corp_id`, `ding_staffid`, `month_time`) USING BTREE
)
COMMENT='企业人员的月度工资详情表--叮当薪资'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;




-- 员工考勤请假天数表
CREATE TABLE `dingsal_staff_attenday` (
	`id` VARCHAR(32) NULL DEFAULT NULL COMMENT '主键',
	`ding_staffid` VARCHAR(32) NULL DEFAULT NULL COMMENT '员工钉钉ID',
	`corp_id` VARCHAR(60) NULL DEFAULT NULL COMMENT '钉钉企业ID',
	`atten_reportid` VARCHAR(32) NULL DEFAULT NULL COMMENT '考勤月度报表ID',
	`field_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '自定义字段的ID',
	`field_day` INT(11) NULL DEFAULT NULL COMMENT '自定义字段的天数',
	`create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
	`modified_date` DATETIME NULL DEFAULT NULL COMMENT '更改时间',
	PRIMARY KEY (`id`),
	INDEX `AK_idx_staffAttenDay_corpId_staffId_reportId_fieldId` (`corp_id`, `ding_staffid`, `atten_reportid`, `field_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
