package me.ywork.suite.service;

import java.util.List;

import me.ywork.suite.entity.DingSuiteThirdApp;



public interface DingSuiteThirdAppService  {
	
	List<DingSuiteThirdApp> findBySuiteThirdId(String suiteThirdId);
	
	String getAgentId(String corpId,String suiteId,String appId);
	
	Integer updateAgentStatus(String corpId,String suiteId,List<String> agentIds);
	
}
