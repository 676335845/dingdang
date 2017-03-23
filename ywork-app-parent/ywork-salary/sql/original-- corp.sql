
	/*==============================================================*/
	/* Table: ding_org_corp           企业信息表                                                      */
	/*==============================================================*/
	CREATE TABLE `ding_org_corp` (
	  `fd_id` varchar(32) NOT NULL comment '应用企业ID -- corpId',
	  `fd_appkey` varchar(32) DEFAULT NULL comment '钉钉企业ID -- dingCorpId',
	  `fd_corp_name` varchar(256) DEFAULT NULL comment '钉钉企业名称 -- dingCorpName',
	  `fd_picurl` varchar(256) DEFAULT NULL comment '企业图片地址 -- dingPicUrl',
	  `fd_last_sync_time` datetime DEFAULT NULL comment '最后一次同步组织架构的时间 -- lastSyncTime',
	  PRIMARY KEY (`fd_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;