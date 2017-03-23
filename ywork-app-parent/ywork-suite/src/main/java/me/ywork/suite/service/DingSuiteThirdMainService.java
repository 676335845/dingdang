package me.ywork.suite.service;

import java.util.List;

import me.ywork.suite.entity.DingSuiteThirdMain;


public interface DingSuiteThirdMainService  {
	
	/**
	 * 根据企业id+套件Id查询第三方授权
	 * @param corpid
	 * @param opPlatformSuiteId
	 * @return
	 */
	DingSuiteThirdMain findDingSuiteThirdByCorpId(String corpid,String suiteId);
	

	int updateOrgCallBack(Boolean fdIsOrgCallBack,String id);

	/**
	 * 查找企业下已授权的记录
	 * @param corpId
	 * @return
     */
	List<DingSuiteThirdMain> findEnabledOpPlatformSuiteThirds(String corpId);
}
