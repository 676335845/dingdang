package me.ywork.ticket.suite.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.redisson.cache.CacheableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import me.ywork.ticket.api.rpc.SuiteTokenRpcService;
import me.ywork.ticket.constants.DingConstants;
import me.ywork.ticket.suite.entity.DingSuiteMain;
import me.ywork.ticket.suite.entity.DingSuiteThirdMain;
import me.ywork.ticket.suite.service.DingSuiteMainService;
import me.ywork.ticket.suite.service.DingSuiteThirdMainService;
import me.ywork.ticket.suite.service.IAccessTokenService;
import me.ywork.ticket.suite.service.IDingApiService;
import me.ywork.ticket.util.DingAPIHttpUtil;

@Service
public class AccessTokenServiceImpl implements IAccessTokenService, SuiteTokenRpcService {

	@Autowired
	private IDingApiService dingApiService;
	
	@Autowired
	private DingSuiteMainService suiteMainService;
	
	@Autowired
	private DingSuiteThirdMainService suiteThirdMainRepository;
	
	@Autowired
	private CacheableService cacheableService;
	
	
	private static final Map<String, Object> lockMap = new ConcurrentHashMap<String, Object>();
	

	private static Logger logger = LoggerFactory.getLogger(AccessTokenServiceImpl.class);



	@Override
	public String getToken(String corpId, String sid) {
		DingSuiteThirdMain thirdModel = suiteThirdMainRepository.findDingSuiteThirdByCorpId(corpId,sid);
		return getAccessToken(thirdModel);
	}

	
	/**
	 * 获取企业token的方法
	 * 
	 * @Description
	 * @return String
	 */
	@Override
	public String getAccessToken(DingSuiteThirdMain thirdVo) {
		String token = "";
		if (thirdVo != null) {
			if (lockMap.get(thirdVo.getId()) == null) {
				lockMap.put(thirdVo.getId(), new Object());
			}
			synchronized (lockMap.get(thirdVo.getId())) { // FIXME
															// 这地方用synchronized有问题。必须换成分布式锁才能解决同一个企业并发访问的问题
				DingSuiteMain suite = suiteMainService.getDingSuiteFromCache(thirdVo.getSuiteId());
				String suiteTicket = null;
				if (StringUtils.isBlank(suiteTicket)) {
					Object cache = cacheableService.getRawObjectFromCache(suite.getId() + DingConstants.SUITE_TICKET_KEY);
					if (cache != null) {
						suiteTicket = (String) cache;
					}
				}
				// 套件token
				String suiteToken = getSuiteToken(suite, suiteTicket);
				// accessToken 企业token
				token = getCorpAccessToken(thirdVo, suite, suiteToken);
			}

		}
		return token;
	}

	/**
	 * 获取套件token
	 * 
	 * @Description
	 * @param suiteId
	 * @param suiteSecret
	 * @param suiteTicket
	 * @return
	 * @return String
	 */
	@Override
	public String getSuiteToken(DingSuiteMain suite, String suiteTicket) {
		String key = suite.getId() + DingConstants.SUITE_ACCESS_TOKEN_KEY;
		Object cache = cacheableService.getRawObjectFromCache(key);
		String suiteToken = "";
		if (cache != null) {
			suiteToken = (String) cache;
		}
		if (StringUtils.isBlank(suiteToken)) {
			//从钉钉服务器获取
			JSONObject jo = null;
			try{
				jo = getSuiteTokenFormServer(suite.getSuiteId(), suite.getSecret(), suiteTicket);
				suiteToken = jo.getString("suite_access_token");
				int expireIn = jo.getIntValue("expires_in");
				if (StringUtils.isNotBlank(suiteToken)) {
					cacheableService.setRawObjectInCache(key, suiteToken, expireIn == 0 ? 6600 : expireIn - 600,TimeUnit.SECONDS);
				} else {
					logger.error("服务器获取套件token异常，返回data："+jo.toJSONString());
				}
			}catch(Exception e){
				logger.error("服务器获取套件token异常，返回data："+jo==null?"json is null":jo.toJSONString(),e);
			}
		}
		return suiteToken;
	}
	
	/**
	 * 获取企业token 方法
	 * 
	 * @return
	 */
	private String getCorpAccessToken(DingSuiteThirdMain third, DingSuiteMain suite, String suiteToken) {

		String suiteId = suite.getId();

		String corpId = third.getCorpId();
		// 每个套件第三方 不分应用共用一个token
		String key = corpId + suiteId + DingConstants.TOKEN_KEY;

		Object cache = cacheableService.getRawObjectFromCache(key);
		String accessToken = "";
		if (cache != null) {
			accessToken = (String) cache;
		}
		if (StringUtils.isBlank(accessToken)) {
			JSONObject jo = dingApiService.getCorpToken(corpId, third.getPermanentCode(), suiteToken);
			logger.info("获取企业（{}）TOKEN返回结果：{}", corpId, jo.toJSONString());
			accessToken = jo.getString("access_token");
			int expireIn = jo.getIntValue("expires_in");
			if (StringUtils.isNotBlank(accessToken)) {
				cacheableService.setRawObjectInCache(key, accessToken, expireIn == 0 ? 6600 : expireIn - 600,
						TimeUnit.SECONDS);
			} else {
				logger.warn("获取企业Token失败,API返回结果为:{}", jo.toJSONString());
			}
		}
		return accessToken;
	}

	/**
	 * 删除缓存SuiteToken
	 * 
	 * @return
	 */
	@Override
	public void delSuiteAccessToken(String corpId, String suiteId) {
		// 每个套件第三方 不分应用共用一个token
		String key = corpId + suiteId + DingConstants.TOKEN_KEY;
		cacheableService.deleteRawObjectInCache(key);
	}
	
	private static JSONObject getSuiteTokenFormServer(String suiteId, String suiteSecret, String suiteTicket) {
		JSONObject json = new JSONObject();
		json.put("suite_key", suiteId);
		json.put("suite_secret", suiteSecret);
		json.put("suite_ticket", suiteTicket);
		String jsonText = json.toJSONString();
		JSONObject jsonObject = DingAPIHttpUtil.httpRequest(DingConstants.DING_SERVICE_SUITE_ACCESS_TOKEN, "POST", jsonText);
		logger.info("===getSuiteTokenFormServer===" + new Date() + "===调用钉钉服务器获取套件token,返回:" + jsonObject + "===参数:"+ jsonText);
		return jsonObject;
	}
	
}
