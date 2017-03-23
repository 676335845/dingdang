	
	  /*==============================================================*/
	 /* Table: attendance           创建企业月度考勤表                                                     */
	/*==============================================================*/
	CREATE TABLE `dingsal_attendance` (
		`id` VARCHAR(32) NOT NULL COMMENT '企业考勤表唯一标识 -- attendanceId',
		`dingsal_corpid` VARCHAR(32) NOT NULL COMMENT '考勤表所属的企业 -- dingsalCorpId',
		`create_time` DATETIME NOT NULL COMMENT '考勤表创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '考勤表更改时间 -- updateTime',
		`month_time` DATE NOT NULL COMMENT '考勤报表的月份 -- monthTime',
		PRIMARY KEY (`id`)
	)
	COMMENT='企业考勤报表--叮当薪资'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	

		
	  /*==============================================================*/
	 /* Table: staff_attendance 创建员工月度考勤数据                                                           */
	/*==============================================================*/
	CREATE TABLE `dingsal_staff_attendance` (
		`id` VARCHAR(32) NOT NULL COMMENT '员工月度考勤标识 -- staffAttendanceId',
		`attendance_day` INT(11) NULL DEFAULT NULL COMMENT '考勤天数 -- attendanceDay',
		`rest_days` INT(11) NULL DEFAULT NULL COMMENT '休息天数 -- restDays',
		`work_hours` INT(11) NULL DEFAULT NULL COMMENT '工作天数 -- workHours',
		`late_hours` INT(11) NULL DEFAULT NULL COMMENT '迟到时长 -- lateHours',
		`late_times` INT(11) NULL DEFAULT NULL COMMENT '迟到次数 -- lateTimes',
		`seriouslate_hours` INT(11) NULL DEFAULT NULL COMMENT '严重迟到时长  -- seriouslateHours',
		`seriouslate_times` INT(11) NULL DEFAULT NULL COMMENT '严重迟到次数 -- seriousLateTimes',
		`earlyleave_hours` INT(11) NULL DEFAULT NULL COMMENT '早退时长 --eLHours',
		`earlyleave_times` INT(11) NULL DEFAULT NULL COMMENT '早退次数 -- eLTimes',
		`workabsence_times` INT(11) NULL DEFAULT NULL COMMENT '上班缺卡次数 -- workAbsenceTimes',
		`offwork_absence_times` INT(11) NULL DEFAULT NULL COMMENT '下班缺卡次数 -- offAbsenceTimes',
		`unwork_days` INT(11) NULL DEFAULT NULL COMMENT '旷工天数 -- unworkDays',
		`unwork_late_days` INT(11) NULL DEFAULT NULL COMMENT '旷工迟到天数 -- unworkLateDays',
		`corp_id` VARCHAR(32) NOT NULL COMMENT '叮当薪资企业标识 -- corpId',
		`ding_staffid` VARCHAR(32) NOT NULL COMMENT '员工标识 --dingStaffId',
		`atten_report_id` VARCHAR(32) NOT NULL COMMENT '月度考勤报表标识 --reportId',
		`create_time` DATETIME NOT NULL COMMENT '员工月度考勤表创建时间 -- createTime',
		`update_time` DATETIME NOT NULL COMMENT '员工月度考勤表更改时间 -- updateTime',
		`month_time` DATE NOT NULL COMMENT '员工月度考勤报表的月份 -- monthTime',
		`out_days` INT(11) NULL DEFAULT NULL COMMENT '外出天数 -- outDays',
		`busy_away_days` INT(11) NULL DEFAULT NULL COMMENT '出差天数 -- busyAwayDays',
		`affair_leave_days` INT(11) NULL DEFAULT NULL COMMENT '事假天数 -- affairLeaveDays',
		`sick_leave_days` INT(11) NULL DEFAULT NULL COMMENT '病假天数 -- sickLeaveDays',
		`year_leave_days` INT(11) NULL DEFAULT NULL COMMENT '年假天数 -- yearLeaveDays',
		`day_off_days` INT(11) NULL DEFAULT NULL COMMENT '调休天数 -- dayOffDays',
		`marry_off_days` INT(11) NULL DEFAULT NULL COMMENT '婚假天数 -- marryOffDays',
		`maternity leave_days` INT(11) NULL DEFAULT NULL COMMENT '产假天数 -- maternityLeaveDays',
		`acco_maternity_days` INT(11) NULL DEFAULT NULL COMMENT '陪产天数 -- accoMaternityDays',
		`journey_days` INT(11) NULL DEFAULT NULL COMMENT '路途假    -- journeyDays',
		`other_days` INT(11) NULL DEFAULT NULL COMMENT '其他天数 -- otherDays',
		PRIMARY KEY (`id`)
	)
	COMMENT='员工考勤数据 --叮当薪资'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;

	--	insert into attendance (id,attendance_time,corp_id)values("1254","20160820","ding0902");