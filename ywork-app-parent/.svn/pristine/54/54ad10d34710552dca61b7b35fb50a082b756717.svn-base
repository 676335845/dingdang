package me.ywork.org.realtime.service;

import com.alibaba.fastjson.JSONObject;

import me.ywork.org.realtime.entity.DingSuiteThirdMain;

public interface IDingApiService   {

	public JSONObject getDingData(String url);

	public JSONObject postDingData(String url, String jsonStr);

	// 获取企业号access_token
	public JSONObject getCorpToken(String suiteId, String authCorpid,
			String permanentCode, String suiteToken);

	// 获取部门列表
	JSONObject getDingDeptList(DingSuiteThirdMain suitethird);
	
	// 获取部门详情
	JSONObject getDingDeptDetail(DingSuiteThirdMain suitethird,String deptId);
	
	//部门员工
//	public JSONObject getDetailListDeptUsers(DingSuiteThirdForSync suitethird, String deptId,
//			int fetchClild);
	//部门员工
	public JSONObject getDetailListDeptUsers(DingSuiteThirdMain suitethird, String deptId) ;
	
	//员工信息
	public JSONObject getDingUser(DingSuiteThirdMain suitethird, String userid) throws Exception;
	
	//管理员列表
	public JSONObject getCorpAdmins(String suiteId,String corpid);
}
