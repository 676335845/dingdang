
	/*==============================================================*/
    /* Table: ding_org_element    企业部门和员工的信息                                        */
	/*==============================================================*/
	CREATE TABLE `ding_org_element` (
	  `fd_id` varchar(36) NOT NULL comment '部门或者员工的标识 -- id',
	  `fd_orgid` varchar(64) NOT NULL comment '钉钉企业ID -- dingCorpId',
	  `fd_org_type` int(11) NOT NULL comment '部门或者员工类型  2是部门 64是员工 --fdOrgType',
	  `fd_name` varchar(100) DEFAULT NULL comment '部门或者员工名字 -- fdName',
	  `fd_name_pinyin` varchar(200) DEFAULT NULL,
	  `fd_order` int(11) DEFAULT NULL,
	  `fd_no` varchar(100) DEFAULT NULL,
	  `fd_keyword` varchar(100) DEFAULT NULL,
	  `fd_is_available` bit(1) DEFAULT NULL,
	  `fd_is_abandon` bit(1) DEFAULT NULL,
	  `fd_is_business` bit(1) DEFAULT NULL,
	  `fd_import_info` varchar(200) DEFAULT NULL,
	  `fd_flag_deleted` bit(1) DEFAULT NULL,
	  `fd_memo` varchar(4000) DEFAULT NULL,
	  `fd_hierarchy_id` varchar(1000) DEFAULT NULL comment '层级关系 -- fdHierarchyId',
	  `fd_create_time` datetime DEFAULT NULL,
	  `fd_alter_time` datetime DEFAULT NULL,
	  `fd_this_leaderid` varchar(36) DEFAULT NULL,
	  `fd_super_leaderid` varchar(36) DEFAULT NULL,
	  `fd_parentid` varchar(36) DEFAULT NULL comment '上级部门 -- fdParentId',
	  `fd_dingid` varchar(50) DEFAULT NULL comment '个人或部门钉钉上的ID',
	  `fd_is_boss` bit(1) DEFAULT NULL,
	  PRIMARY KEY (`fd_id`),
	  KEY `ding_org_element_orgid` (`fd_orgid`),
	  KEY `ding_org_element_leaderid` (`fd_this_leaderid`),
	  KEY `ding_org_element_parentid` (`fd_parentid`),
	  KEY `ding_org_element_super_leaderid` (`fd_super_leaderid`),
	  KEY `ding_org_element_import_info` (`fd_import_info`(191)),
	  KEY `ding_org_element_hierarchy_id` (`fd_hierarchy_id`(191)),
	  KEY `ding_org_element_keyword` (`fd_keyword`),
	  KEY `ding_org_element_no` (`fd_no`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 dbpartition by hash(`fd_orgid`) tbpartition by hash(`fd_orgid`) tbpartitions 16;
