package me.ywork.suite.webapi.service.impl;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.redisson.cache.CacheableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.IdGenerator;

import me.ywork.suite.constants.DingAPIConstants;
import me.ywork.suite.service.DingCorpTokenService;
import me.ywork.suite.service.DingSuiteThirdAppService;
import me.ywork.suite.util.DingAPIHttpUtil;
import me.ywork.suite.webapi.service.IDingApiService;

@Service
public class DingApiServiceImp implements IDingApiService {
	private static Logger logger = LoggerFactory.getLogger(DingApiServiceImp.class);


	
	@Autowired
	private DingSuiteThirdAppService dingSuiteThirdAppService;
	
	@Autowired
	private DingCorpTokenService dingCorpTokenService;
	
	private static final String JSAPI_TICKET = "jsapi_ticket";
	
	@Autowired
	private CacheableService cacheableService;
	
	@Override
	public JSONObject getDingData(String url) {
		JSONObject josonInfo = DingAPIHttpUtil.httpRequest(url, "GET", null);
		if (logger.isTraceEnabled()) {
			logger.trace("getDingData:{}", new Object[] { josonInfo.toJSONString() });
		}
		return josonInfo;
	}

	@Override
	public JSONObject postDingData(String url, String jsonStr) {
		JSONObject josonInfo = DingAPIHttpUtil.httpRequest(url, "POST", jsonStr);
		if (logger.isTraceEnabled()) {
			logger.trace("getDingData:{}", new Object[] { josonInfo.toJSONString() });
		}
		return josonInfo;
	}
	
	

	@Override
	public JSONObject getCheckData(String suiteId, String corpId, String jsonStr) {
		// TODO Auto-generated method stub
		String accessToken = dingCorpTokenService.getToken(corpId, suiteId);
		String url = "https://oapi.dingtalk.com/attendance/list?access_token="+accessToken;
		JSONObject redata = postDingData(url,jsonStr);
		return redata;
	}

	@Override
	public String getJsSignature(String suiteId, String corpId, String appId, String url) {
		
		String agentId = dingSuiteThirdAppService.getAgentId(corpId, suiteId, appId);
		
		String jsapiTicket ="";
	    String key =  suiteId + corpId + JSAPI_TICKET;
	    Object cache = cacheableService.getRawObjectFromCache(key);
	    if (cache != null) {
            jsapiTicket = (String) cache;
	    }
	    
	    if(StringUtils.isBlank(jsapiTicket)){
	    	String accessToken = dingCorpTokenService.getToken(corpId, suiteId);
	    	 String getUrl=   DingAPIConstants.JSAPI_TICKET.replace("ACCESS_TOKEN", accessToken);
	    	 JSONObject jsonObject = DingAPIHttpUtil.httpRequest(getUrl, "GET", null);
	         if (jsonObject!=null && jsonObject.getInteger("errcode") ==0){
	             jsapiTicket = jsonObject.getString("ticket");
	             int expireIn = jsonObject.getIntValue("expires_in");
	             cacheableService.setRawObjectInCache(key, jsapiTicket, expireIn == 0 ? 6600 : expireIn - 600,TimeUnit.SECONDS);
	         }
	    }
	    
	    long timeStamp = System.currentTimeMillis();
        String nonceStr = IdGenerator.newId();
	    String signature = "";
	    String plain = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)+ "&url=" + url;
	    try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			signature = bytesToHex(sha1.digest());
			JSONObject jo = new JSONObject();
			jo.put("mess", "1");
			jo.put("nonceStr", nonceStr);
			jo.put("timeStamp", timeStamp);
			jo.put("signature", signature);
			jo.put("agentId", agentId);
			return jo.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 private String bytesToHex(byte[] hash) {
	        Formatter formatter = new Formatter();
	        for (byte b : hash) {
	            formatter.format("%02x", b);
	        }
	        String result = formatter.toString();
	        formatter.close();
	        return result;
	    }
	

	
}

