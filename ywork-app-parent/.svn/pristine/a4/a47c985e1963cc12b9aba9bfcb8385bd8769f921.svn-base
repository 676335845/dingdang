package me.ywork.ticket.suite.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ywork.ticket.suite.entity.DingSuiteThirdApp;
import me.ywork.ticket.suite.repository.DingSuiteThirdAppRepositroy;
import me.ywork.ticket.suite.service.DingSuiteThirdAppService;

@Service
public class DingSuiteThirdAppServiceImpl  implements DingSuiteThirdAppService {
	
	private static Logger logger = LoggerFactory.getLogger(DingSuiteThirdAppServiceImpl.class);
	
	@Autowired
	private DingSuiteThirdAppRepositroy suiteThirdAppRepositroy;

	@Override
	public List<DingSuiteThirdApp> findBySuiteThirdId(String fdSuiteThirdId) {
		return suiteThirdAppRepositroy.findBySuiteThirdId(fdSuiteThirdId);
	}

	@Override
	public String findAgentId(String corpId, String suiteId, String appId) {
		return suiteThirdAppRepositroy.findAgentId(corpId, suiteId, appId);
	}

	@Override
	public Integer updateAgentStatus(String corpId, String suiteId, List<String> agentIds) {
		return suiteThirdAppRepositroy.updateAgentStatus(corpId, suiteId, agentIds);
	}

}
