	 /*==============================================================*/
	 /* Table: staff_baseinfo 叮当薪资员工信息表  (在授权的时候就要把这个表给建好来)                                                         */
	/*==============================================================*/
	CREATE TABLE `dingsal_staff_info` (
		`id` VARCHAR(32) NOT NULL COMMENT '叮当薪资员工ID -- dingsalStaffId',
		`corp_id` VARCHAR(32) NOT NULL COMMENT '叮当薪资企业标识 -- dingsalCorpId',
		`sal_rule` INT(11) NOT NULL DEFAULT '0' COMMENT '薪资规则 -- salRule',
		`ding_staffid` VARCHAR(32) NOT NULL COMMENT '钉钉员工号',
		`staff_pass` VARCHAR(30) NULL DEFAULT NULL COMMENT '员工密码锁密码 -- staffPass',
		`atten_social` INT(11) NOT NULL DEFAULT '0' COMMENT '社保公积金是否参与 1是参与,0是不参与 -- attenSocial',
		`atten_personal_tax` INT(11) NULL DEFAULT '0' COMMENT '个人所得税是否参与缴税 1是参与 ,0 是不参与 -- attenPersonalTax',
		`should_pay_sal` INT(11) NOT NULL COMMENT '应发工资 -- shouldPaySal',
		`create_time` DATETIME NOT NULL COMMENT '员工信息的创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '员工信息的更改时间 -- updateTime ',
		 PRIMARY KEY (`id`)
	)
	COMMENT='员工信息表--叮当薪资'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
   
	/*==============================================================*/
	/* Table: staff_baseinfo 企业一授权就新建这个表                                                */
	/*==============================================================*/
	CREATE TABLE `dingsal_corp_info` (
		`id` VARCHAR(32) NOT NULL COMMENT '叮当薪资企业信息的Id -- id',
		`corp_id` VARCHAR(32) NOT NULL COMMENT '企业ID -- corpId',
		`create_time` DATETIME NOT NULL COMMENT '企业记录的创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '企业记录的更改时间 -- updateTime ',
		`pass_state` INT(11) NULL DEFAULT '0' COMMENT '企业密码锁开启的状态 -- passState',
		PRIMARY KEY (`id`)
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;

	CREATE TABLE `dingsal_field_item` (
		`id` VARCHAR(32) NOT NULL COMMENT '字段标识 -- itemId',
		`item_name` VARCHAR(50) NOT NULL COMMENT '员工薪资字段名字 -- itemName',
		`item_value` DOUBLE NULL DEFAULT NULL COMMENT '字段值 -- itemValue',
		`item_type` INT(11) NULL DEFAULT NULL COMMENT '字段的类型',
		`relative_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '与之绑定的ID -- relativeId',
		PRIMARY KEY (`id`)
	)
	COMMENT='自定义字段表--叮当薪资'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	
	
	--	
--    <!--插入企业的基本信息-->
--    insert into staff_sal_info (id,staff_id,sal_rule,total_sal,sal_effect_time)values("123456","1111",1,10000,"20170101");
--    <!-- 增加企业密码锁开启的状态的字段-->
--    alter table corp_baseinfo add column pass_state Integer default 0;

	--
--	    <!--插入部门的基本信息-->
--	    insert into dept_info(id,dept_name)values("5555","产品部");

--	    <!--插入员工基本信息-->
--		insert into staff_baseinfo(id,staff_name,corp_id,dept_id) values ("12354621","大侠","1111","5555");
--        
--		<!--员工基本信息表新增薪资规则字段-->
--		alter table staff_baseinfo change sal_rule sal_rule Integer default 1;
--		
--		<!--新增员工密码字段-->
--		alter table staff_baseinfo add column staff_pass varchar(30);
--		
--		<!--更新员工基本信息表的密码字段为123-->
--		update staff_baseinfo set staff_pass="123";