package me.ywork.org.realtime.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import me.ywork.org.api.model.DingOrgUserVo;
import me.ywork.org.realtime.entity.DingOrgActor;
import me.ywork.org.realtime.entity.DingOrgElement;
import me.ywork.org.realtime.entity.DingOrgUser;


public interface IDingOrgUserService extends IDingOrgElementService{
	
	String SERVICE_NAME = "dingOrgUserService";
	
	DingOrgElement loadUser(String orgId, String userId);
	
	String updateUser(DingOrgUser user);
	
	public DingOrgUserVo findDingOrgUserById(String corpId, String userId);
	
	/**
	 * 将钉钉返回的员工userId转换成蓝凌平台的人员ID
	 * @param corpId   钉钉商户ID
	 * @param userIds  钉钉员工ID列表
	 * @return 钉钉员工ID-蓝凌内部人员ID键值对象列表
	 */
	public Map<String, String> getPersonIdsByCorpIdUserIds(String corpId,
			List<String> userIds);
	
	/**
	 * 获取指定企业部门下面的所有人员列表
	 * @param comapnyId  企业ID
	 * @param deptId  部门ID
	 * @param count   返回的最大人数量
	 * @return 该部门下面所有的人员列表
	 */
	public List<String> getDingOrgUserVosByCompanyIdDeptId(
			String comapnyId, String deptId, int count);

	
	/**
	 * 获取人员在多个部门中的身份
	 * @param companyId
	 * @param fdUserId
	 * @return
	 */
	List<DingOrgActor> getActorsForUser(String companyId, String fdUserId);
	
	public String updateUser(String suiteId , String corpId, JSONObject obj,
			Date alterTime,Boolean isAdmin);
	
	/**
	 * 监听离职事件
	 * @param suiteId
	 * @param corpId
	 * @param userId
	 * @return
	 */
	public String removeUser(String suiteId , String corpId,String[] userId);
}
