package me.ywork.org.service;

import com.alibaba.fastjson.JSONObject;

import me.ywork.org.model.DingSuiteThirdForSync;

public interface IDingApiService   {

	public JSONObject getDingData(String url);

	public JSONObject postDingData(String url, String jsonStr);

	// 获取企业号access_token
	public JSONObject getCorpToken(String suiteId, String authCorpid,
			String permanentCode, String suiteToken);

	// 获取部门
	JSONObject getDingDeptList(DingSuiteThirdForSync suitethird);
	
	//部门员工
//	public JSONObject getDetailListDeptUsers(DingSuiteThirdForSync suitethird, String deptId,
//			int fetchClild);
	//部门员工
	public JSONObject getDetailListDeptUsers(DingSuiteThirdForSync suitethird, String deptId) ;
	
	//员工信息
	public JSONObject getDingUser(DingSuiteThirdForSync suitethird, String userid) throws Exception;
	
	//管理员列表
	public JSONObject getCorpAdmins(String suiteId,String corpid);
}
