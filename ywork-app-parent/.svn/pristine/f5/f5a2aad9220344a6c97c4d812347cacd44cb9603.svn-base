package me.ywork.ticket.suite.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.redisson.cache.CacheableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import me.ywork.ticket.constants.DingConstants;
import me.ywork.ticket.suite.entity.DingSuiteMain;
import me.ywork.ticket.suite.entity.DingSuiteThirdMain;
import me.ywork.ticket.suite.model.GetUnactiveCorpReqModel;
import me.ywork.ticket.suite.model.GetUnactiveCorpResModel;
import me.ywork.ticket.suite.model.QySuiteAgent;
import me.ywork.ticket.suite.model.QySuiteAuthApp;
import me.ywork.ticket.suite.model.QySuiteAuthCorpInfo;
import me.ywork.ticket.suite.model.QySuiteAuthInfo;
import me.ywork.ticket.suite.model.QySuitePermanentCode;
import me.ywork.ticket.suite.model.ReAuthCorpReqModel;
import me.ywork.ticket.suite.service.DingSuiteMainService;
import me.ywork.ticket.suite.service.IAccessTokenService;
import me.ywork.ticket.suite.service.IDingApiService;
import me.ywork.ticket.util.DingAPIHttpUtil;

@Service
public class DingApiServiceImpl  implements IDingApiService {
	
	private static Logger logger = LoggerFactory.getLogger(DingApiServiceImpl.class);


	@Autowired
	private IAccessTokenService accessTokenService;

	@Autowired
	private DingSuiteMainService dingSuiteMainService;
	
	@Autowired
	private CacheableService cacheableService;

	@Override
	public JSONObject getDingData(String url) {
		JSONObject josonInfo = DingAPIHttpUtil.httpRequest(url, "GET", null);

		logger.trace("getDingData:{}", new Object[] { josonInfo.toJSONString() });

		return josonInfo;
	}

	@Override
	public JSONObject postDingData(String url, String jsonStr) {
		JSONObject josonInfo = DingAPIHttpUtil.httpRequest(url, "POST", jsonStr);
		logger.trace("getDingData:{}", new Object[] { josonInfo.toJSONString() });
		return josonInfo;
	}

	//获取企业永久授权码
	@Override
	public QySuitePermanentCode getPermanentCode(DingSuiteMain ws, String authCode, String suiteTicket)
			throws Exception {
		QySuitePermanentCode model = new QySuitePermanentCode();
		JSONObject jo = new JSONObject();
		jo.put("tmp_auth_code", authCode);
		String suiteToken = accessTokenService.getSuiteToken(ws, suiteTicket);

		String url = DingConstants.DING_SERVICE_PERMANENT_CODE.replace("SUITE_ACCESS_TOKEN", suiteToken);
		JSONObject jsonObject = this.postDingData(url, jo.toString());
		if (jsonObject != null) {
			if (jsonObject.getInteger("errcode") == 0) {
				// 授权方（企业）永 久授权码
				String perCode = jsonObject.getString("permanent_code");
				model.setPermanentCode(perCode);
				// auth_corp_info 授权方企业信息 corpid & corp_name
				JSONObject authCorpInfojo = jsonObject.getJSONObject("auth_corp_info");
				QySuiteAuthCorpInfo authCorpInfo = new QySuiteAuthCorpInfo();
				String corpId = authCorpInfojo.getString("corpid");
				authCorpInfo.setCorpId(corpId);
				String corpName = authCorpInfojo.getString("corp_name");
				authCorpInfo.setCorpName(corpName);
				model.setAuthCorpInfo(authCorpInfo);
				logger.info("======" + new Date() + "=====获取永久授权码success:" + jsonObject + "====postData" + jo);
			} else {
				model.setErrcode(jsonObject.getInteger("errcode"));
				model.setErrmsg(jsonObject.getString("errmsg"));
				logger.error("======" + new Date() + "=====获取永久授权码error:" + jsonObject + "====postData" + jo);
			}
		}else{
			logger.error("======" + new Date() + "=====获取永久授权码error json is null ====postData" + jo);
		}
		return model;
	}

	/**
	 * 获取企业号的授权信息
	 * 
	 * @Description
	 * @param ws
	 * @param authCorpid
	 * @param permanentCode
	 * @return
	 * @throws Exception
	 * @return QySuitePermanentCode
	 */
	@Override
	public QySuitePermanentCode getAuthInfo(DingSuiteMain ws, String authCorpid, String permanentCode,
			String suiteTicket) throws Exception {
		JSONObject dataJson = new JSONObject();
		dataJson.put("suite_key", ws.getSuiteId());
		dataJson.put("auth_corpid", authCorpid);
		dataJson.put("permanent_code", permanentCode);

		String suiteToken = accessTokenService.getSuiteToken(ws, suiteTicket);

		String url = DingConstants.DING_SERVICE_GET_AUTH_INFO.replace("SUITE_ACCESS_TOKEN", suiteToken);
		JSONObject jsonObject = postDingData(url, dataJson.toString());
		QySuitePermanentCode model = new QySuitePermanentCode();
		if (jsonObject != null && jsonObject.getInteger("errcode") == 0) {
			
			//auth_corp_info
			JSONObject authCorpInfoJo = jsonObject.getJSONObject("auth_corp_info");
			String corpId = authCorpInfoJo.getString("corpid");
			String corpName = authCorpInfoJo.getString("corp_name");
			String logoUrl = authCorpInfoJo.getString("corp_logo_url");
			String industry = authCorpInfoJo.getString("industry");
			QySuiteAuthCorpInfo authCorpInfo = new QySuiteAuthCorpInfo();
			authCorpInfo.setCorpId(corpId);
			authCorpInfo.setCorpName(corpName);
			authCorpInfo.setLogoUrl(logoUrl);
			authCorpInfo.setIndustry(industry);
			model.setAuthCorpInfo(authCorpInfo);

			QySuiteAuthInfo qySuiteAuthInfo = new QySuiteAuthInfo();

			JSONObject authInfoJo = jsonObject.getJSONObject("auth_info");
			JSONArray agentInfoJo = authInfoJo.getJSONArray("agent");
			List<QySuiteAgent> al = new ArrayList<QySuiteAgent>();
			for (Iterator iterator = agentInfoJo.iterator(); iterator.hasNext();) {
				JSONObject jo = (JSONObject) iterator.next();
				QySuiteAgent agent = new QySuiteAgent();
				String agentid = jo.getString("agentid");
				String name = jo.getString("agent_name");
				String square_logo_url = jo.getString("logo_url ");
				String app_id = jo.getString("appid");
				agent.setAgentId(agentid);
				agent.setName(name);
				agent.setLogoUrl(square_logo_url);
				agent.setAppid(app_id);
				al.add(agent);
			}
			qySuiteAuthInfo.setQySuiteAgents(al);
			model.setAuthInfo(qySuiteAuthInfo);
			model.setErrcode(jsonObject.getInteger("errcode"));
			model.setErrmsg(jsonObject.getString("errmsg"));
			if (model.getErrcode() != 0) {
				logger.error("======" + new Date() + "=====获取企业号的授权信息:" + jsonObject + "====postData" + dataJson);
			} else {
				logger.info("======" + new Date() + "=====获取企业号的授权信息:" + jsonObject + "====postData" + dataJson);
			}
		} else {
			model.setErrcode(jsonObject == null ? -1 : jsonObject.getInteger("errcode"));
			model.setErrmsg(jsonObject == null ? "null" : jsonObject.getString("errmsg"));
			logger.error("======" + new Date() + "=====getAuthInfo:" + jsonObject + "====postData" + dataJson);
		}

		return model;
	}

	// 获取企业号应用
	@Override
	public QySuiteAuthApp getAgent(DingSuiteMain ws, String authCorpid, String permanentCode, String agentid,
			String suiteTicket) throws Exception {
		JSONObject jo = new JSONObject();
		jo.put("suite_key", ws.getSuiteId());
		jo.put("auth_corpid", authCorpid);
		jo.put("permanent_code", permanentCode);
		jo.put("agentid", agentid);

		String suiteToken = accessTokenService.getSuiteToken(ws, suiteTicket);
		String url = DingConstants.DING_SERVICE_GET_AGENT.replace("SUITE_ACCESS_TOKEN", suiteToken);
		JSONObject jsonObject = postDingData(url, jo.toString());

		QySuiteAuthApp model = new QySuiteAuthApp();
		if (jsonObject != null) {
			String name = jsonObject.getString("name");
			String logo_url = jsonObject.getString("logo_url");
			String description = jsonObject.getString("description");
			int close = jsonObject.get("close") == null ? 0 : jsonObject.getInteger("close");
			model.setAgentid(agentid);
			model.setName(name);
			model.setLogoUrl(logo_url);
			model.setDescription(description);
			model.setClose(close);

			model.setErrcode(jsonObject.getInteger("errcode"));
			model.setErrmsg(jsonObject.getString("errmsg"));
		}

		if (model.getErrcode() != 0) {
			logger.error("======" + new Date() + "=====企业号应用:" + jsonObject + "====postData" + jo.toString());
		} else {
			logger.info("======" + new Date() + "=====企业号应用:" + jsonObject + "====postData" + jo.toString());
		}

		return model;
	}

	/**
	 * 获取企业号access_token 用于调用钉钉接口 应用套件id,授权方corpid,永久授权码,suiteToken(套件令牌)
	 */
	@Override
	public JSONObject getCorpToken(String authCorpid, String permanentCode, String suiteToken) {
		JSONObject json = new JSONObject();
		json.put("auth_corpid", authCorpid);
		json.put("permanent_code", permanentCode);
		String jsonText = json.toJSONString();
		String url = DingConstants.DING_SERVICE_GET_CORP_TOKEN.replace("SUITE_ACCESS_TOKEN", suiteToken);
		JSONObject jsonObject = DingAPIHttpUtil.httpRequest(url, "POST", jsonText);
		System.out.println("======" + new Date() + "===获取企业token：" + jsonObject + "===post data:" + jsonText
				+ ",===url:" + url);
		return jsonObject;
	}

	/**
	 * 激活授权方企业的套件微应用,信息接口调用请求说明
	 */
	@Override
	public int activateSuite(String authCorpid, String suiteId, String permanentCode, String suiteToken)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("auth_corpid", authCorpid);
		json.put("suite_key", suiteId);
		json.put("permanent_code", permanentCode);
		String jsonText = json.toJSONString();

		String url = DingConstants.DING_ACTIVATE_SUITE.replace("SUITE_ACCESS_TOKEN", suiteToken);
		JSONObject jsonObject = DingAPIHttpUtil.httpRequest(url, "POST", jsonText);
		if(jsonObject==null){
			logger.error("======" + new Date() + "=====激活套件 error json return null====postData" + jsonText);
			return -1;
		}
		int err = jsonObject.getIntValue("errcode");
		if (err != 0) {
			logger.error("======" + new Date() + "=====激活套件:" + jsonObject + "====postData" + jsonText);
		} else {
			logger.info("======" + new Date() + "=====激活套件:" + jsonObject + "====postData" + jsonText);
		}
		return err;
	}

	/**
	 * 注册事件回调
	 */
	@Override
	public int registerCallBack(JSONObject jsonData, DingSuiteThirdMain suitethird) {
		int err = -1;
		try {
			String jsonText = jsonData.toJSONString();

			String token = accessTokenService.getAccessToken(suitethird);

			String url = DingConstants.DING_ORG_REGISTER_CALL_BACK.replace("ACCESS_TOKEN", token);

			logger.info("POST URL DING_ORG_REGISTER_CALL_BACK:" + url);

			JSONObject jsonObject = DingAPIHttpUtil.httpRequest(url, "POST", jsonText);
			err = jsonObject.getIntValue("errcode");
			if (err != 0) {
				logger.error("======" + new Date() + "=====registerCallBack:" + jsonObject + "====postData" + jsonText+",corpId:"+suitethird.getCorpId());
			}
//			else {
//				logger.info("======" + new Date() + "=====registerCallBack:" + jsonObject + "====postData" + jsonText);
//			}
		} catch (Exception e) {
			logger.error("注册事件回调异常，corpId:"+suitethird.getCorpId(),e);
		}
		return err;

	}

	/**
	 * 更新事件回调接口
	 */
	@Override
	public int updateCallBack(JSONObject jsonData, DingSuiteThirdMain suitethird) {
		int err = -1;
		try {
			String jsonText = jsonData.toJSONString();

			String token = accessTokenService.getAccessToken(suitethird);
			String url = DingConstants.DING_ORG_UPDATE_CALL_BACK.replace("ACCESS_TOKEN", token);

			JSONObject jsonObject = DingAPIHttpUtil.httpRequest(url, "POST", jsonText);
			err = jsonObject.getIntValue("errcode");
			if (err != 0) {
				logger.error("======" + new Date() + "=====updateCallBack:" + jsonObject + "====postData" + jsonText+",corpId:"+suitethird.getCorpId());
			}
//			else {
//				logger.info("======" + new Date() + "=====updateCallBack:" + jsonObject + "====postData" + jsonText);
//			}
		} catch (Exception e) {
			logger.error("更新事件回调异常，corpId:"+suitethird.getCorpId(),e);
		}
		return err;

	}

	@Override
	public GetUnactiveCorpResModel getUnactiveCorp(String suiteId, GetUnactiveCorpReqModel getUnactiveCorpReqModel) {
		if (StringUtils.isBlank(suiteId)) {
			throw new IllegalArgumentException("suiteId is null");
		}

		if (getUnactiveCorpReqModel == null) {
			throw new IllegalArgumentException("getUnactiveCorpReqModel is null");
		}

		String suiteTicket = null;
		Object cache = cacheableService.getRawObjectFromCache(suiteId + DingConstants.SUITE_TICKET_KEY);
		if (cache != null) {
			suiteTicket = (String) cache;
		}

		DingSuiteMain suite = dingSuiteMainService.getDingSuiteFromCache(suiteId);
		String suiteToken = accessTokenService.getSuiteToken(suite,suiteTicket);

		String url = DingConstants.DING_GET_UNACTIVE_CORP.replace("SUITE_ACCESS_TOKEN", suiteToken);
		JSONObject jsonObject = DingAPIHttpUtil.httpRequest(url, "POST", JSON.toJSONString(getUnactiveCorpReqModel));
		if (jsonObject == null) {
			throw new RuntimeException("get_unactive_corp return null");
		}

		if (jsonObject.getInteger("errcode") == 0) {
			GetUnactiveCorpResModel getUnactiveCorpResModel = JSON.parseObject(jsonObject.toJSONString(),
					GetUnactiveCorpResModel.class);
			return getUnactiveCorpResModel;
		} else {
			throw new RuntimeException("api exception: errcode = " + jsonObject.getInteger("errcode"));
		}
	}

	@Override
	public int ReAuthCorp(String suiteId, ReAuthCorpReqModel reAuthCorpReqModel) {
		if (StringUtils.isBlank(suiteId)) {
			throw new IllegalArgumentException("suiteId is null");
		}

		if (reAuthCorpReqModel == null) {
			throw new IllegalArgumentException("reAuthCorpReqModel is null");
		}

		String suiteTicket = null;
		Object cache = cacheableService.getRawObjectFromCache(suiteId + DingConstants.SUITE_TICKET_KEY);
		if (cache != null) {
			suiteTicket = (String) cache;
		}

		DingSuiteMain suite = dingSuiteMainService.getDingSuiteFromCache(suiteId);
		String suiteToken = accessTokenService.getSuiteToken(suite,suiteTicket);

		String url = DingConstants.DING_REAUTH_CORP.replace("SUITE_ACCESS_TOKEN", suiteToken);
		JSONObject jsonObject = DingAPIHttpUtil.httpRequest(url, "POST", JSON.toJSONString(reAuthCorpReqModel));
		if (jsonObject == null) {
			throw new RuntimeException("reauth_corp return null");
		}

		if (jsonObject.getInteger("errcode") == 0) {
			return jsonObject.getInteger("errcode");
		} else {
			throw new RuntimeException("api exception: errcode = " + jsonObject.getInteger("errcode") + ",errmsg="
					+ jsonObject.getString("errmsg"));
		}
	}

	public static void main(String[] args) {

		String authCorpid = "";
		String permanentCode = "";
		String suiteToken = "03d902a50de7063592a7741d60a85f274";// suited6db0pze8yao1b1ysuitetoken_redis
		String suiteId = "suited6db0pze8yao1b1y";

		JSONObject json = new JSONObject();
		json.put("auth_corpid", authCorpid);
		json.put("suite_key", suiteId);
		json.put("permanent_code", permanentCode);
		String jsonText = json.toJSONString();
		String url = DingConstants.DING_ACTIVATE_SUITE.replace("SUITE_ACCESS_TOKEN", suiteToken);

		JSONObject jsonObject = DingAPIHttpUtil.httpRequest(url, "POST", jsonText);
		int err = jsonObject.getIntValue("errcode");
		if (err != 0) {
			logger.error("======" + new Date() + "=====激活套件:" + jsonObject + "====postData" + jsonText);
		} else {
			logger.info("======" + new Date() + "=====激活套件:" + jsonObject + "====postData" + jsonText);
		}
	}

}
