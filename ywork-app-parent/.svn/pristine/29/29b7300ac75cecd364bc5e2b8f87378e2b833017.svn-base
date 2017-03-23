package me.ywork.suite.service.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.redisson.cache.CacheableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.spring.context.AppContext;

import me.ywork.suite.constants.DingAPIConstants;
import me.ywork.suite.service.DingCorpTokenService;
import me.ywork.suite.util.DingAPIHttpUtil;
import me.ywork.ticket.api.rpc.SuiteTokenRpcService;

/**
 * 企业token
 * @author Think
 *
 */

@Service
public class DingCorpTokenServiceImpl implements DingCorpTokenService {

    private SuiteTokenRpcService suiteTokenRpcService;
	
	public SuiteTokenRpcService getSuiteTokenRpcService() {
		if(suiteTokenRpcService==null)
			suiteTokenRpcService = (SuiteTokenRpcService) AppContext.getBean("suiteTokenRpcService");
		return suiteTokenRpcService;
	}
	
	@Autowired
    private CacheableService cacheableService;
	
	private static final String token_key = "token_redis";
	
	
	@Override
	public String getToken(String corpId, String suiteId) {
		String accessToken = "";
		Object cache = cacheableService.getRawObjectFromCache(corpId + suiteId + token_key);
		if (cache != null) {
			accessToken = (String) cache;
		}
		if (StringUtils.isBlank(accessToken)) {
			accessToken = getSuiteTokenRpcService().getToken(corpId, suiteId);
		}
		return accessToken;
	}

	public static final String PC_TOKEN_KEY = "pc_token_redis";// 钉钉后台token
	
	@Override
	public String getPcToken(String corpid, String sercet) {
		String key = corpid + PC_TOKEN_KEY;
		Object cache = cacheableService.getRawObjectFromCache(key);
		String pcToken = "";
		if (cache != null) {
			pcToken = (String) cache;
		}
		if (StringUtils.isBlank(pcToken)) {
			String access_token_url = DingAPIConstants.GET_PC_ACCESS_TOKEN.replace("CORPID", corpid).replace("PCSECRET", sercet);
			JSONObject jsonObject = DingAPIHttpUtil.httpRequest(access_token_url, "GET", null);
			pcToken = jsonObject.getString("access_token");
			int expireIn = jsonObject.getIntValue("expires_in");
			cacheableService.setRawObjectInCache(key, pcToken, expireIn == 0 ? 6600 : expireIn - 600,TimeUnit.SECONDS);
		}
		return pcToken;
	}

	
}
