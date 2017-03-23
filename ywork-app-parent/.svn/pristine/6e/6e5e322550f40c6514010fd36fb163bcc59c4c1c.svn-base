
	  /*==============================================================*/
	 /* Table: sal_report 创建企业的月度工资报表                                 */
	/*==============================================================*/
	CREATE TABLE `dingsal_sal_report` (
		`id` VARCHAR(32) NOT NULL COMMENT '键，工资报表的唯一标识 -- id',
		`corp_id` VARCHAR(32) NOT NULL COMMENT '企业唯一标识 -- corpId',
		`sal_report_state` INT(11) NOT NULL DEFAULT '0' COMMENT '工资报表的状态,分为预估，待审批和锁定 -- salReportState',
		`should_pay_sal` BIGINT(20) NOT NULL COMMENT '应发工资 -- shouldPaySal',
		`insurance_sal` BIGINT(20) NULL DEFAULT NULL COMMENT '公司交金 -- insuranceSal',
		`staff_cost` BIGINT(20) NULL DEFAULT NULL COMMENT '员工成本 -- staffCost',
		`create_time` DATETIME NOT NULL COMMENT '工资报表的创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '工资报表的更改时间 -- updateTime',
		`month_time` DATE NOT NULL COMMENT '工资报表的月份 -- monthTime',
		PRIMARY KEY (`id`)
	)
	COMMENT='月度工资报表--叮当薪资'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;



   	  /*==============================================================*/
	 /* Table: staff_month_sal  创建企业人员的月度工资详情表                  */
	/*==============================================================*/
	CREATE TABLE `dingsal_staff_month_sal` (
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
		`create_time` DATETIME NOT NULL COMMENT '员工月度工资详情的创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '员工月度工资详情的更改时间 -- updateTime',
		`month_time` DATE NOT NULL COMMENT '员工月度工资详情的月份 -- monthTime',
		PRIMARY KEY (`id`)
	)
	COMMENT='企业人员的月度工资详情表--叮当薪资'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;


--	
--	
--	   <!--插入企业的月度工资报表-->
--	   insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("1234324546","1111","20161101",0,122123243,1234,2134);
--	     insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("23435463345223","1111","20161001",0,123454532,1234,2134);
--	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("22522325422321","1111","20160901",0,123454532,1234,2134);
--	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("754233434312","1111","20160801",0,123454532,1234,2134);
--	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("987123432324","1111","20160701",0,123454532,1234,2134);
--	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("06345234523645","1111","20160601",0,123454532,1234,2134);
--	     	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("063453645","1111","20160501",0,123454532,1234,2134);
--	     	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("0634522113645","1111","20160401",0,123454532,1234,2134);
--	     	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("063256453645","1111","20160301",0,123454532,1234,2134);
--	     	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("06345361234345","1111","20160201",0,123454532,1234,2134);
--	      	      insert into sal_report(id,corp_id,create_time,sal_report_state,total_sal,insurance_sal,staff_cost)
--	     values("0634532345645","1111","20160101",0,123454532,1234,2134);
--	     
--	     
--      insert into staff_month_sal (id,staff_id,month_sal_id,annual_bonus,annual_bonus_comment,month_bonus,month_bonus_comment,
--   other_pretax_sal,other_pretax_sal_comment,other_after_pretax_sal,other_after_pretax_sal_comment,
--   other_pretax_deduct,other_pretax_deduct_comment,otherAfterPretaxDeduct,otherAfterPretaxDeductComment
--   )values("23465764321","12354621","1234324546",23443,"这是年终奖备注",123,"这是当月奖金备注",2134,"这是其他税前补款备注",213,
--   "这是其他税后补卡备注",234,"其他税前扣款",987,"其它税后扣款备注");
--   
--      insert into staff_month_sal (id,staff_id,month_sal_id,annual_bonus,annual_bonus_comment,month_bonus,month_bonus_comment,
--   other_pretax_sal,other_pretax_sal_comment,other_after_pretax_sal,other_after_pretax_sal_comment,
--   other_pretax_deduct,other_pretax_deduct_comment,otherAfterPretaxDeduct,otherAfterPretaxDeductComment
--   )values("75643213435","2134356423","1234324546",23443,"这是年终奖备注",123,"这是当月奖金备注",2134,"这是其他税前补款备注",213,
--   "这是其他税后补卡备注",234,"其他税前扣款",987,"其它税后扣款备注");
--   
--   insert into staff_month_sal (id,staff_id,month_sal_id,annual_bonus,annual_bonus_comment,month_bonus,month_bonus_comment,
--   other_pretax_sal,other_pretax_sal_comment,other_after_pretax_sal,other_after_pretax_sal_comment,
--   other_pretax_deduct,other_pretax_deduct_comment,otherAfterPretaxDeduct,otherAfterPretaxDeductComment
--   )values("2fd2436546","213454523","1234324546",23443,"这是年终奖备注",123,"这是当月奖金备注",2134,"这是其他税前补款备注",213,
--   "这是其他税后补卡备注",234,"其他税前扣款",987,"其它税后扣款备注");
--   
--      insert into staff_month_sal (id,staff_id,month_sal_id,annual_bonus,annual_bonus_comment,month_bonus,month_bonus_comment,
--   other_pretax_sal,other_pretax_sal_comment,other_after_pretax_sal,other_after_pretax_sal_comment,
--   other_pretax_deduct,other_pretax_deduct_comment,otherAfterPretaxDeduct,otherAfterPretaxDeductComment
--   )values("6453232435621","213456524","1234324546",23443,"这是年终奖备注",123,"这是当月奖金备注",2134,"这是其他税前补款备注",213,
--   "这是其他税后补卡备注",234,"其他税前扣款",987,"其它税后扣款备注");
--   
--    insert into staff_month_sal (id,staff_id,month_sal_id,annual_bonus,annual_bonus_comment,month_bonus,month_bonus_comment,
--   other_pretax_sal,other_pretax_sal_comment,other_after_pretax_sal,other_after_pretax_sal_comment,
--   other_pretax_deduct,other_pretax_deduct_comment,otherAfterPretaxDeduct,otherAfterPretaxDeductComment
--   )values("gfbdvsaQRT","2136534221","1234324546",23443,"这是年终奖备注",123,"这是当月奖金备注",2134,"这是其他税前补款备注",213,
--   "这是其他税后补卡备注",234,"其他税前扣款",987,"其它税后扣款备注");
--   
--   insert into staff_month_sal (id,staff_id,month_sal_id,annual_bonus,annual_bonus_comment,month_bonus,month_bonus_comment,
--   other_pretax_sal,other_pretax_sal_comment,other_after_pretax_sal,other_after_pretax_sal_comment,
--   other_pretax_deduct,other_pretax_deduct_comment,otherAfterPretaxDeduct,otherAfterPretaxDeductComment
--   )values("as12132456u","2343215343","1234324546",23443,"这是年终奖备注",123,"这是当月奖金备注",2134,"这是其他税前补款备注",213,
--   "这是其他税后补卡备注",234,"其他税前扣款",987,"其它税后扣款备注");
--   
--     insert into staff_month_sal (id,staff_id,month_sal_id,annual_bonus,annual_bonus_comment,month_bonus,month_bonus_comment,
--   other_pretax_sal,other_pretax_sal_comment,other_after_pretax_sal,other_after_pretax_sal_comment,
--   other_pretax_deduct,other_pretax_deduct_comment,otherAfterPretaxDeduct,otherAfterPretaxDeductComment
--   )values("dsawq3wtyu232","234351456","1234324546",23443,"这是年终奖备注",123,"这是当月奖金备注",2134,"这是其他税前补款备注",213,
--   "这是其他税后补卡备注",234,"其他税前扣款",987,"其它税后扣款备注");
--   
--   
--    update staff_month_sal set staff_id="12354621",month_sal_id="1234324546";