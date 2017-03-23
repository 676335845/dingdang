package me.ywork.salary.service;

import java.util.List;

import me.ywork.base.service.BizService;
import me.ywork.salary.entity.SalStaffSalReportEntity;
import me.ywork.salary.model.SalBaseStateModel;
import me.ywork.salary.model.SalSysFieldItemModel;

public interface SalBaseService extends BizService{
	/**
	 * 得到企业基本的一些信息，如各种人数
	 * 
	 * @param corpId 钉钉企业ID
	 * @return 存储企业基本信息的集合
	 */
	SalBaseStateModel getCorpBaseInfo(String corpId);
	
	/**
	 * 同步组织架构部门下所有的人员到应用下
	 * 
	 * @param corpId 钉钉企业ID
	 * @param deptId 钉钉部门ID
	 * @return  同步的结果
	 */
	 Boolean synOrgDeptStaffToApplication(String corpId,String deptId);
	 
	 /**
	  * 同步组织架构部门下的人员到应用下
	  * 
	  * @param corpId 钉钉企业ID
	  * @param staffId 钉钉员工ID
	  * @return  同步的结果
	  */
	 Boolean synOrgStaffToApplication(String corpId,String staffId);
	 
	 /**
	  * 初始化企业的考勤扣款规则
	  * 
	  * @param corpId 钉钉企业ID
	  * @return  初始化考勤扣款规则的结果
	  */
	 Boolean initCorpAttenDeduct(String corpId);
	 
	 /**
	  * 初始化企业的基本薪资规则
	  * 
	  * @param corpId 钉钉企业ID
	  * @return  初始化的结果
	  */
	 Boolean initCorpBsRule(String corpId);
	 
	 /**
	  * 初始化企业的社保公积金的薪资规则
	  * 
	  * @param corpId  钉钉企业ID
	  * @return  初始化的结果
	  */
	 Boolean initCorpWhpRule(String corpId);
	 
	 /**
	  * 初始化企业的基本信息
	  * 
	  * @param corpId 钉钉企业ID
	  * @return  初始化的结果
	  */
	 Boolean initCorpBaseInfo(String corpId);
	 	 
	 /**
	  * 得到企业员工的所有部门，并拼接
	  * 
	  * @param corpId  钉钉企业ID
	  * @param staffId  钉钉员工ID
	  * @return  拼接的员工部门
	  */
	 String getStaffAllDeptName(String corpId , String staffId);
	 
	 /**
	  * 初始化员工月度薪资报表下薪资的组成部分
	  * 
	  * @param salStaffSalReportEntity 员工月度薪资数据
	  * @param salSysFieldItemList 存储自定义字段的容器
	  */
	 void initCorpStaffReportSalItem(SalStaffSalReportEntity salStaffSalReportEntity , List<SalSysFieldItemModel> salSysFieldItemList);
	
	
}
