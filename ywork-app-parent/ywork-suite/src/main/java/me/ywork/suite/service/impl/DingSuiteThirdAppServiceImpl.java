package me.ywork.suite.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.redisson.cache.CacheableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ywork.suite.entity.DingSuiteThirdApp;
import me.ywork.suite.repository.DingSuiteThirdAppRepositroy;
import me.ywork.suite.service.DingSuiteThirdAppService;

@Service
public class DingSuiteThirdAppServiceImpl  implements DingSuiteThirdAppService {
	
	private static Logger logger = LoggerFactory.getLogger(DingSuiteThirdAppServiceImpl.class);
	
	private static final String agent_key = "app_agent_redis";
	
	@Autowired
    private CacheableService cacheableService;
	
	@Autowired
	private DingSuiteThirdAppRepositroy suiteThirdAppRepositroy;

	@Override
	public List<DingSuiteThirdApp> findBySuiteThirdId(String fdSuiteThirdId) {
		return null;//opPlatformSuiteThirdappRepositroy.findBySuiteThirdId(fdSuiteThirdId);
	}


	@Override
	public String getAgentId(String corpId,String suiteId,String appId) {
		String agentId = "";
		Object cache = cacheableService.getRawObjectFromCache(corpId + suiteId + appId + agent_key);
		
		if (cache != null) {
			agentId = (String) cache;
		}
		
		if(StringUtils.isBlank(agentId)){
			agentId = suiteThirdAppRepositroy.findAgentId(corpId, suiteId, appId);
			if(StringUtils.isNotBlank(agentId)){
				cacheableService.setRawObjectInCache(corpId + suiteId + appId + agent_key, agentId, 1, TimeUnit.DAYS);
			}else{
				logger.error("agentid is null,corpid:"+corpId+",suiteId:"+suiteId+",appid:"+appId);
			}
		}
		return agentId;
	}


	@Override
	public Integer updateAgentStatus(String corpId, String suiteId, List<String> agentIds) {
		return suiteThirdAppRepositroy.updateAgentStatus(corpId, suiteId, agentIds);
	}

}
