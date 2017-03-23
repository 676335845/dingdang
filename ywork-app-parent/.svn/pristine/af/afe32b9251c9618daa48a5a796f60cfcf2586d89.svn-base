
	  /*==============================================================*/
	 /* Table: sal_rule 创建系统薪资规则-                  */
	/*==============================================================*/
	CREATE TABLE `dingsal_sal_rule` (
		`id` VARCHAR(32) NOT NULL COMMENT '薪资规则的标识 -- id',
		`sal_rule_des` VARCHAR(200) NOT NULL COMMENT '薪资规则描述 -- salRuleDes',
		`sal_rule_name` VARCHAR(50) NOT NULL COMMENT '薪资规则的名字 -- salRuleName',
		`sal_rule_type` INT(11) NULL DEFAULT NULL COMMENT '薪资规则的类型 -- salRuleType',
		`create_time` DATETIME NOT NULL COMMENT '系统薪资规则创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '系统薪资规则更改时间 -- updateTime',
		PRIMARY KEY (`id`)
	)
	COMMENT='系统薪资规则--叮当薪资'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;

    
	    

	  /*==============================================================*/
	 /* Table: corp_sal_rule 创建企业基本薪资规则表                                                   */
	/*==============================================================*/
	CREATE TABLE `dingsal_corpsal_rule` (
		`id` VARCHAR(32) NOT NULL COMMENT '具体企业的薪资规则标识 -- id',
		`sal_rule_id` VARCHAR(32) NOT NULL COMMENT '薪资规则的标识 -- salRuleId',
		`cal_sal_days` INT(11) NOT NULL DEFAULT '22' COMMENT '计算薪资规则的天数 -- calSalDays',
		`fit_nums` INT(11) NOT NULL COMMENT '薪资规则的适用人数 -- fit_nums',
		`late_early_deduct_type` INT(11) NOT NULL COMMENT '迟到早退扣款类型 -- lateEarlyDeductType',
		`late_early_deduct` INT(11) NOT NULL COMMENT '迟到早退扣款 -- lateEarlyEduct',
		`serious_late_deduct` INT(11) NOT NULL COMMENT '严重迟到扣款 -- seriousLateDeduct',
		`stay_away_deduct` INT(11) NOT NULL COMMENT '旷工扣款 -- stayAwayDeduct',
		`corp_id` INT(11) NOT NULL COMMENT '叮当薪资企业标识 -- corpId',
		`create_time` DATETIME NOT NULL COMMENT '企业薪资规则创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '企业薪资规则更改时间 -- updateTime',
		PRIMARY KEY (`id`)
	)
	COMMENT='企业薪资规则表--叮当薪资'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;


	/*============================================================== */
	/* Table: dingsal_withholding_payment 创建企业代缴代扣薪资规则标识  */
	/*============================================================== */
	CREATE TABLE `dingsal_withhold_pay_salrule` (
		`id` VARCHAR(32) NOT NULL COMMENT '代缴代扣薪资规则标识 -- id',
		`subject_id` VARCHAR(30) NOT NULL COMMENT '缴纳科目标识 -- subjectId',
		`base_low` INT(11) NULL DEFAULT NULL COMMENT '基数下限 -- baseLow',
		`base_high` INT(11) NULL DEFAULT NULL COMMENT '基数上限 -- baseHigh',
		`corp_percent` INT(11) NULL DEFAULT NULL COMMENT '公司比例 -- corpPercent',
		`personal_percent` INT(11) NULL DEFAULT NULL COMMENT '个人比例 -- personalPercent',
		`corp_id` VARCHAR(32) NOT NULL COMMENT '钉钉企业标识 -- corpId',
		`create_time` DATETIME NOT NULL COMMENT '代缴代扣薪资规则创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '代缴代扣薪资规则更新时间 -- updateTime',
		PRIMARY KEY (`id`)
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;

	
	

	 
--
--	           
--    <!--插入企业的薪资规则-->		     
--     insert into corp_sal_rule (id,sal_rule_id,cal_sal_days,fit_nums,late_early_deduct_type,late_early_educt,serious_late_deduct,stay_away_deduct)
--    values("3243546","213446221",22,2134,1,13,23,133);
--    <!--插入企业的薪资规则-->
--    insert into corp_sal_rule (id,sal_rule_id,cal_sal_days,fit_nums,late_early_deduct_type,late_early_educt,serious_late_deduct,stay_away_deduct)
--    values("98543245678","21345564523",22,2134,1,13,23,133);
