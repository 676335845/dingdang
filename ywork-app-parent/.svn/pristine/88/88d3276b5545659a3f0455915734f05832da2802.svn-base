package me.ywork.ticket.suite.service;

import java.util.List;

import me.ywork.ticket.suite.entity.DingSuiteThirdMain;


public interface DingSuiteThirdMainService  {
	
	/**
	 * 根据企业id+套件Id查询第三方授权
	 * @param corpid
	 * @param opPlatformSuiteId
	 * @return
	 */
	DingSuiteThirdMain findDingSuiteThirdByCorpId(String corpid,String suiteId);
	

	/**
	 * 简单分页
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<DingSuiteThirdMain> findByPage(String suiteId,int startIndex , int pageSize);
	
	long countAll();
	
	int updateOrgCallBack(Boolean fdIsOrgCallBack,String id);

	/**
	 * 查找企业下已授权的记录
	 * @param corpId
	 * @return
     */
	List<DingSuiteThirdMain> findEnabledOpPlatformSuiteThirds(String corpId);
}
