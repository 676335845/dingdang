package me.ywork.ticket.suite.service;

import java.util.List;

import me.ywork.base.service.BizService;
import me.ywork.ticket.suite.entity.DingSuiteApp;


public interface DingSuiteAppService extends BizService{
	
	List<DingSuiteApp> findBySuiteId(String suiteId);
	
	List<Integer> findplatformAppIdsByAppIds(String suiteId,List<String> appIds);
}
