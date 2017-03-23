package me.ywork.suite.webapi.rpc;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.messagebus.kafka.KafkaProducer;

import me.ywork.kfkmessage.DingHttpPostMessage;
import me.ywork.message.base.AbstractDingMessage;
import me.ywork.message.base.AbstractDingMessageHeader;
import me.ywork.message.base.AbstractDingMessageResult;
import me.ywork.message.base.DingCorpMessageHeader;
import me.ywork.message.base.DingCorpMessageResult;
import me.ywork.message.base.DingGroupMessageResult;
import me.ywork.message.topic.KafkaTopics;
import me.ywork.suite.api.model.CheckDataRpcModel;
import me.ywork.suite.api.rpc.IDingAPIRpcService;
import me.ywork.suite.constants.DingAPIConstants;
import me.ywork.suite.service.DingCorpTokenService;
import me.ywork.suite.service.DingSuiteThirdAppService;
import me.ywork.suite.util.DingAPIHttpUtil;
import me.ywork.suite.webapi.service.IDingApiService;

public class DingAPIRpcServiceImpl implements IDingAPIRpcService{
	
	private Logger logger = LoggerFactory.getLogger(DingAPIRpcServiceImpl.class);
	
	@Autowired
	private DingSuiteThirdAppService dingSuiteThirdAppService;
	
	@Autowired
	private DingCorpTokenService dingCorpTokenService;

	@Override
	public <H extends AbstractDingMessageHeader, M extends AbstractDingMessage> void sendDingGroupMessageAsync(
			String corpid, String suiteId, String appId, H messageHeader,
			M message) {
		if (StringUtils.isBlank(corpid)) {
				throw new IllegalArgumentException(
						"sendDingGroupMessage - parameter companyId is null or empty.");
			}
			
			if (messageHeader == null) {
				throw new NullPointerException(
						"sendDingGroupMessage - parameter messageHeader is null.");
			}
			
			if (message == null) {
				throw new NullPointerException(
						"sendDingGroupMessage - parameter message is null.");
			}
			
			String agentId = dingSuiteThirdAppService.getAgentId(corpid, suiteId, appId);
			
			if(StringUtils.isBlank(agentId)){
				logger.error("sendDingCorpMessage agentid is null , corpId:"+corpid+",suiteId:"+suiteId+",appId:"+appId);
				throw new NullPointerException(
						"sendDingCorpMessage - third app's agent is null.");
			}
			messageHeader.setAgentid(agentId);
			
			// 组装钉消息JSON
			// 消息头
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(messageHeader);
			if (jsonObject == null) {
				throw new IllegalStateException("将钉消息头转换成JSONObject失败。");
			}
			
			// 加入消息体
			jsonObject.put(messageHeader.getMsgtype().getCode(),
					JSON.toJSON(message));
			
			DingHttpPostMessage postMessage = new DingHttpPostMessage();
			
			postMessage.setCorpId(corpid);
			postMessage.setSuiteId(suiteId);
			postMessage.setDingUrl(DingAPIConstants.SEND_MESSAGE_CONVERSATION);
			postMessage.setJsonObject(jsonObject);
			
			KafkaProducer.getInstance().sendMessage(KafkaTopics.YWORK_DING_HTTP_MESSAGE.getTopic(), postMessage);
	}

	@Override
	public <H extends DingCorpMessageHeader, M extends AbstractDingMessage> void sendDingCorpMessageAsync(
			String corpId, String suiteId, String appId, H messageHeader,
			M mesage) {

		String agentId = dingSuiteThirdAppService.getAgentId(corpId, suiteId, appId);
		
		if(StringUtils.isBlank(agentId)){
			logger.error("sendDingCorpMessage agentid is null , corpId:"+corpId+",suiteId:"+suiteId+",appId:"+appId);
			throw new NullPointerException(
					"sendDingCorpMessage - third app's agent is null.");
		}
		messageHeader.setAgentid(agentId);
		// 组装钉消息JSON
		// 消息头
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(messageHeader);
		if (jsonObject == null) {
			throw new IllegalStateException("将钉消息头转换成JSONObject失败。");
		}
		// 加入消息体
		jsonObject.put(messageHeader.getMsgtype().getCode(),
				JSON.toJSON(mesage));
				
		DingHttpPostMessage postMessage = new DingHttpPostMessage();
		
		postMessage.setCorpId(corpId);
		postMessage.setSuiteId(suiteId);
		postMessage.setDingUrl(DingAPIConstants.SEND_MESSAGE);
		postMessage.setJsonObject(jsonObject);
		
		KafkaProducer.getInstance().sendMessage(KafkaTopics.YWORK_DING_HTTP_MESSAGE.getTopic(), postMessage);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R extends AbstractDingMessageResult, H extends AbstractDingMessageHeader, M extends AbstractDingMessage>
	     R sendDingGroupMessage(String corpid,String suiteId,String appId,  H messageHeader, M message) {
		if (StringUtils.isBlank(corpid)) {
			throw new IllegalArgumentException(
					"sendDingGroupMessage - parameter companyId is null or empty.");
		}
		
		if (messageHeader == null) {
			throw new NullPointerException(
					"sendDingGroupMessage - parameter messageHeader is null.");
		}
		
		if (message == null) {
			throw new NullPointerException(
					"sendDingGroupMessage - parameter message is null.");
		}
		
		String agentId = dingSuiteThirdAppService.getAgentId(corpid, suiteId, appId);
		if(StringUtils.isBlank(agentId)){
			logger.error("sendDingCorpMessage agentid is null , corpId:"+corpid+",suiteId:"+suiteId+",appId:"+appId);
			throw new NullPointerException(
					"sendDingCorpMessage - third app's agent is null. corpId:"+corpid+",suiteId:"+suiteId+",appId:"+appId);
		}
		messageHeader.setAgentid(agentId);
		
		// 组装钉消息JSON
		// 消息头
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(messageHeader);
		if (jsonObject == null) {
			throw new IllegalStateException("将钉消息头转换成JSONObject失败。");
		}
		
		// 加入消息体
		jsonObject.put(messageHeader.getMsgtype().getCode(),JSON.toJSON(message));
		
		// 发送钉钉消息, 得到返回结果
		JSONObject resultJson = null;
		
		String token = dingCorpTokenService.getToken(corpid, suiteId);
		String url = "";
		url = DingAPIConstants.SEND_MESSAGE_CONVERSATION;
		url = url.replace("ACCESS_TOKEN", token);
		
		try {
			String rtnStr = DingAPIHttpUtil.httpPost(url, jsonObject.toJSONString()
					.replace("<br/>", "\r\n"), "sendMsgResult");
			resultJson = JSON.parseObject(rtnStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// 将发送接口返回的JSON转换成具体对象
		return (R) JSON.toJavaObject(resultJson, DingGroupMessageResult.class);
	}
	

	@Override
	public <R extends DingCorpMessageResult, H extends DingCorpMessageHeader, M extends AbstractDingMessage> R sendDingCorpMessage(
			String corpId, String suiteId, String appId, H messageHeader,
			M mesage) {
		
		String agentId = dingSuiteThirdAppService.getAgentId(corpId, suiteId, appId);
		
		if(StringUtils.isBlank(agentId)){
			logger.error("sendDingCorpMessage agentid is null , corpId:"+corpId+",suiteId:"+suiteId+",appId:"+appId);
			throw new NullPointerException(
					"sendDingCorpMessage - third app's agent is null.");
		}
		messageHeader.setAgentid(agentId);
		// 组装钉消息JSON
		// 消息头
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(messageHeader);
		if (jsonObject == null) {
			throw new IllegalStateException("将钉消息头转换成JSONObject失败。");
		}
		// 加入消息体
		jsonObject.put(messageHeader.getMsgtype().getCode(),JSON.toJSON(mesage));
		
		// 发送钉钉消息, 得到返回结果
		JSONObject resultJson = null;
			
		String token = dingCorpTokenService.getToken(corpId, suiteId);
		
		String url =DingAPIConstants.SEND_MESSAGE;
		
		url = url.replace("ACCESS_TOKEN", token);
		
		logger.info("---------:"+jsonObject.toJSONString());
		try {
			String rtnStr = DingAPIHttpUtil.httpPost(url, jsonObject.toJSONString().replace("<br/>", "\r\n"), "sendMsgResult");
			resultJson = JSON.parseObject(rtnStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 将发送接口返回的JSON转换成具体对象
		return (R) JSON.toJavaObject(resultJson, DingCorpMessageResult.class);
	}

	
	
	
	/**
	 * 钉钉后台管理员信息
	 */
	@Override
	public JSONObject getPcAdminInfo(String code) {
		// 获取企业管理员的身份信息 蓝凌账号
		String corpid = "ding79e18f8077de6ae2";
		
		String secret = "MgScTVq4mEdIeKwC_Hh2ls0zd6CwytmViVRPYFyZ-xaODfQP5vRAtUMgYsgUfw_2";
		
		String token = dingCorpTokenService.getPcToken(corpid,secret);
		
		String ADMININFOURL = DingAPIConstants.PC_ADMININFOU;
		
		ADMININFOURL = ADMININFOURL.replace("ACCESS_TOKEN", token).replace("CODE", code);
		
		JSONObject adminInfoNew = DingAPIHttpUtil.httpRequest(ADMININFOURL,"GET", null);

		logger.info("获取企业管理员({})的身份信息:{}", code, adminInfoNew);

		return adminInfoNew;
	}
	
	@Autowired
	private IDingApiService dingApiService;

	@Override
	public List<CheckDataRpcModel> getCheckData(String corpid, String suiteid, Long startTime, Long endTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String s = df.format(new Date(startTime));
		String e = df.format(new Date(endTime));
		JSONObject jo = new JSONObject();
		jo.put("workDateFrom", s);
		jo.put("workDateTo", e);
		JSONObject checkDataJson = dingApiService.getCheckData(suiteid, corpid, jo.toJSONString());
		if(checkDataJson==null || checkDataJson.getIntValue("errcode")!=0){
			logger.error(checkDataJson==null?"corpid:"+corpid+",st:"+startTime+",et:"+endTime+"":checkDataJson.toJSONString());
			return null;
		}
		return createCheckDataRpcModel(checkDataJson);
	}

	@Override
	public List<CheckDataRpcModel> getCheckData(String corpid, String suiteid, String userid, Long startTime, Long endTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String s = df.format(new Date(startTime));
		String e = df.format(new Date(endTime));
		JSONObject jo = new JSONObject();
		jo.put("userId", userid);
		jo.put("workDateFrom", s);
		jo.put("workDateTo", e);
		JSONObject checkDataJson = dingApiService.getCheckData(suiteid, corpid, jo.toJSONString());
		if(checkDataJson==null || checkDataJson.getIntValue("errcode")!=0){
			logger.error(checkDataJson==null?"":checkDataJson.toJSONString());
			return null;
		}
		return createCheckDataRpcModel(checkDataJson);
	}
	
	private static List<CheckDataRpcModel> createCheckDataRpcModel(JSONObject checkDataJson){
		JSONArray jarr = checkDataJson.getJSONArray("recordresult");
		List<CheckDataRpcModel> dataList = new ArrayList<CheckDataRpcModel>();
		JSONObject jsonObj = null;
		CheckDataRpcModel model = null;
		for (int i = 0; i < jarr.size(); i++) {
			jsonObj = jarr.getJSONObject(i);
			model = new CheckDataRpcModel();
			model.setBaseCheckTime(jsonObj.getLongValue("baseCheckTime"));
			model.setCheckType(jsonObj.getString("checkType"));
			model.setGroupId(jsonObj.getString("groupId"));
			model.setId(jsonObj.getString("id"));
			model.setLocationResult(jsonObj.getString("locationResult"));
			model.setPlanId(jsonObj.getString("planId"));
			model.setRecordId(jsonObj.getString("recordId"));
			model.setTimeResult(jsonObj.getString("timeResult"));
			model.setUserCheckTime(jsonObj.getLongValue("userCheckTime"));
			model.setUserId(jsonObj.getString("userId"));
			model.setWorkDate(jsonObj.getLongValue("workDate"));
			dataList.add(model);
		}
		return dataList;
	}

}
