package me.ywork.org.realtime.service;

import com.alibaba.fastjson.JSONObject;

import me.ywork.org.realtime.entity.DingOrgElement;


public interface IDingOrgDeptService extends IDingOrgElementService{
	
	String SERVICE_NAME = "dingOrgDeptService";
	
	DingOrgElement loadDept(String orgId, String deptId);
	
	DingOrgElement getDeptByDingId(String orgId,String dingId);
	
	/**
	 * 用于压力测试，随机获取指定企业的部门ID
	 * @param companyId   企业ID
	 * @return  部门ID
	 */
	public String getRandomDeptId(String companyId);
	
	
	public String updateDept(String suiteId,String corpId,JSONObject deptJson);
	
}
