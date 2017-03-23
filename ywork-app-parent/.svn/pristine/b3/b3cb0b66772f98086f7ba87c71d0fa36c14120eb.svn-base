package me.ywork.org.service;

import me.ywork.org.entity.DingOrgElement;


public interface IDingOrgDeptService extends IDingOrgElementService{
	
	String SERVICE_NAME = "dingOrgDeptService";
	
	DingOrgElement loadDept(String orgId, String deptId);
	
	/**
	 * 用于压力测试，随机获取指定企业的部门ID
	 * @param companyId   企业ID
	 * @return  部门ID
	 */
	public String getRandomDeptId(String companyId);
}
