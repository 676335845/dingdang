/**
 * 套件表
 */
CREATE TABLE ding_suite_main (
	id varchar(32) NOT NULL, 
	suiteId varchar(50) comment '套件ID',  
	suiteName varchar(50) comment '套件名称',  
	corpId varchar(64) not null comment '套件id',  
	secret varchar(64) not null comment '套件secret', 
	token varchar(10) not null comment '套件Token',
	encodingAESKey varchar(50) not null comment '回调消息加解密参数',
	receiveEventUrl varchar(128) comment '服务事件接收URL',
	authBackUrl varchar(128) comment '授权回调URl', 
	launchAuthDomain varchar(30) comment '发起授权域名',  
	receiveAuthDomain varchar(30) comment '授权完成回调域名',
	specialPermissions varchar(30) comment '特殊权限',
	whiteList varchar(160) comment 'ip访问白名单', 
	logoUrl varchar(128) comment '套件logo',  
	createDate datetime DEFAULT NULL,
	modifiedDate datetime DEFAULT NULL,
	PRIMARY KEY (id) 
);

/**
 * 套件内应用
 */
CREATE TABLE ding_suite_app ( 
	id varchar(32) NOT NULL, 
	suitId varchar(32) not null comment '所属套件id' , 
	appId varchar(32) not null comment '套件应用ID', 
	appName varchar(32) not null comment '应用名', 
	specialPermission varchar(30) comment '特殊权限',  
	createDate datetime DEFAULT NULL,
	modifiedDate datetime DEFAULT NULL,
	PRIMARY KEY (id) 
);

/**
 * 第三方
 */
CREATE TABLE ding_suite_third_main (
	id varchar(32) NOT NULL,
	corpId varchar(64) not null comment '企业id',
	suiteId varchar(32) not null comment '所属套件id' , 
	permanentCode varchar(70) not null comment '永久授权码',
	corpName varchar(100) comment '企业名称', 
	industry varchar(20) comment '行业', 
	inviteCode varchar(20) comment '邀请码', 
	licenseCode varchar(20) comment '序列号', 
	authChannel varchar(20) comment '渠道码', 
	inviteUrl varchar(20) comment '邀请链接', 
	isAuthenticated  tinyint default 1 comment '是否认证', 
	authLevel int comment '认证等级',  
	logoUrl varchar(128) comment '企业图像',
	enabled   tinyint not null default 1,
	createDate datetime DEFAULT NULL,
	modifiedDate datetime DEFAULT NULL,
	PRIMARY KEY (id),
	KEY idx_dsuite_third_corpid (corpId)
);

/**
 * 第三方应用
 */
CREATE TABLE ding_suite_third_app ( 
	id varchar(32) NOT NULL, 
	thirdId varchar(32) not null comment '所属第三方ID',
	agentId int not null comment '授权应用id',
	appId varchar(32) NOT NULL comment '套件应用appid',  
	agentName varchar(50) not null comment '应用名字', 
	logoUrl varchar(128) comment '应用图像',
	enabled   tinyint not null default 1,
	createDate datetime DEFAULT NULL,
	modifiedDate datetime DEFAULT NULL,
	PRIMARY KEY (id),
	KEY idx_dsuite_thirdapp_thirdid (thirdId)
) ;





