package me.ywork.ticket.suite.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.redisson.cache.CacheableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.messagebus.kafka.KafkaProducer;
import com.heyi.utils.IdGenerator;

import me.ywork.message.topic.KafkaTopics;
import me.ywork.ticket.DingTicketConfigure;
import me.ywork.ticket.aes.DingTalkEncryptor;
import me.ywork.ticket.constants.DingConstants;
import me.ywork.ticket.message.DingOrgModifyMessage;
import me.ywork.ticket.suite.entity.DingSuiteMain;
import me.ywork.ticket.suite.entity.DingSuiteThirdApp;
import me.ywork.ticket.suite.entity.DingSuiteThirdMain;
import me.ywork.ticket.suite.model.QySuiteAgent;
import me.ywork.ticket.suite.model.QySuiteAuthApp;
import me.ywork.ticket.suite.model.QySuitePermanentCode;
import me.ywork.ticket.suite.repository.DingSuiteThirdAppRepositroy;
import me.ywork.ticket.suite.repository.DingSuiteThirdMainRepositroy;
import me.ywork.ticket.suite.service.DingSuiteMainService;
import me.ywork.ticket.suite.service.DingSuiteThirdAppService;
import me.ywork.ticket.suite.service.DingSuiteThirdMainService;
import me.ywork.ticket.suite.service.IAccessTokenService;
import me.ywork.ticket.suite.service.IDingApiService;
import me.ywork.ticket.util.DingAPIHttpUtil;

@Controller
@RequestMapping("**/alid/dingservice")
public class DingServiceController {

	private static Logger logger = LoggerFactory.getLogger(DingServiceController.class);

	@Autowired
	private IDingApiService dingApiService;

	@Autowired
	private IAccessTokenService accessTokenService;

	@Autowired
	private DingSuiteMainService dingSuiteMainService;

	@Autowired
	private DingSuiteThirdMainService dingSuiteThirdMainService;
	
	@Autowired
	private DingSuiteThirdMainRepositroy dingSuiteThirdRepositroy;
	
	@Autowired
	private DingSuiteThirdAppRepositroy dingSuiteThirdappRepositroy;

	@Autowired
	private DingSuiteThirdAppService dingSuiteThirdappService;
	
	@Autowired
	private CacheableService cacheableService;
	
	private Map<String,DingTalkEncryptor> dmap = new HashMap<String,DingTalkEncryptor>();
	
	@RequestMapping(value = "/index/testorg", method = RequestMethod.GET)
	public void testorg(@RequestParam("suiteid") String sid,@RequestParam("corpId") String corpId,  HttpServletRequest request,
			HttpServletResponse response) {
		//通讯录全量实时同步
		DingOrgModifyMessage orgModifyMessage = new DingOrgModifyMessage();
		orgModifyMessage.setEventType("sync_org");
		orgModifyMessage.setSuiteId(sid);
		orgModifyMessage.setCorpId(corpId);
		orgModifyMessage.setChannel(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic());
		KafkaProducer.getInstance().sendMessage(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic(), orgModifyMessage);
		
		logger.info(JSONObject.toJSONString(orgModifyMessage));
	}
	
	/**
	 * 套件事件接收URL
	 * 1.1 验证回调URL有效性事件
	 * 1.2 定时推送Ticket
	 * 1.3 回调向ISV推送临时授权码
	 * 1.4 回调向ISV推送授权变更消息
	 * 1.5"套件信息更新"事件
	 * 1.6"解除授权"事件
	 * 1.7"校验序列号"事件
	 */
	@RequestMapping(value = "/index/{suiteid}", method = RequestMethod.POST)
	public void isvReceive(@PathVariable("suiteid") String sid, HttpServletRequest request,
			HttpServletResponse response) {

		String msgSignature = request.getParameter("signature");
		String timeStamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");

		try {
			// 读取DingTalk服务器POST的加密数据
			InputStream inputStream = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			JSONObject jsonEncrypt = DingAPIHttpUtil.getBody(br);
			br.close();
			inputStream.close();
			
			// 取得JSON对象中的encrypt字段
			String encryptStr = jsonEncrypt.getString("encrypt");

			// 蓝桥套件ID:suiteId，套件第一次创建时，使用钉钉默认的ID
			if (StringUtils.isEmpty(sid)) {
				sid = "suite4xxxxxxxxxxxxxxx";
			} else if(sid.indexOf("?")>0){
				sid = sid.substring(0, sid.indexOf("?"));
			}

			// 获取数据库中套件的配置数据
			DingTalkEncryptor dingTalkEncryptor =null;
			DingSuiteMain suite = dingSuiteMainService.getDingSuiteFromCache(sid);
			
			if (suite == null) {
				dingTalkEncryptor = dmap.get(DingTicketConfigure.defaultSuiteKey);
				if(dingTalkEncryptor == null){
					dingTalkEncryptor = new DingTalkEncryptor(DingTicketConfigure.defaultToken,DingTicketConfigure.defaultAESKey, DingTicketConfigure.defaultSuiteKey);
					dmap.put(DingTicketConfigure.defaultSuiteKey, dingTalkEncryptor);
				}
			} else {
				dingTalkEncryptor = dmap.get(suite.getSuiteId());
				if(dingTalkEncryptor == null){
					dingTalkEncryptor = new DingTalkEncryptor(suite.getToken(), suite.getEncodingAESKey(),suite.getSuiteId());
					dmap.put(suite.getSuiteId(), dingTalkEncryptor);
				}
			}

			// 对encrypt进行解密
			String plainText = dingTalkEncryptor.getDecryptMsg(msgSignature, timeStamp, nonce, encryptStr);

			// 对从encrypt解密出来的明文进行处理，不同的eventType的明文数据格式不同
			JSONObject plainTextJson = JSONObject.parseObject(plainText);
			String eventType = plainTextJson.getString("EventType");
			logger.info("套件({})推送事件类型：{}。", sid, eventType);

			switch (eventType) {
			case "suite_ticket":
				// 定时(二十分钟)推送Ticket
				doSuccessResponse(dingTalkEncryptor, response, "success", msgSignature, timeStamp, nonce);
				doSuiteTicket(sid, plainTextJson);
				break;
			case "tmp_auth_code":
				// 推送临时授权码
				doSuccessResponse(dingTalkEncryptor, response, "success", msgSignature, timeStamp, nonce);
				doTmpAuthCode(sid, plainTextJson);
				break;
			case "change_auth":
				// 推送授权变更消息
				doSuccessResponse(dingTalkEncryptor, response, "success", msgSignature, timeStamp, nonce);
				doChangeAuth(sid, plainTextJson);
				break;
			case "check_create_suite_url":
				// 验证回调URL有效性事件
				doSuccessResponse(dingTalkEncryptor, response, plainTextJson.getString("Random"), msgSignature,
						timeStamp, nonce);
				break;
			case "check_update_suite_url":
				// "套件信息更新"事件，无API调用
				doSuccessResponse(dingTalkEncryptor, response, plainTextJson.getString("Random"), msgSignature,
						timeStamp, nonce);
				break;
			case "suite_relieve":
				// "解除授权"事件
				doSuccessResponse(dingTalkEncryptor, response, "success", msgSignature, timeStamp, nonce);
				doSuiteRelieve(sid, plainTextJson);
				break;
			case "check_suite_license_code":
				// "校验序列号"事件，有效则返回"success",否则返回其他值或不返回 未改完 不知道做啥的
				boolean isValid = doCheckSuiteLicenseCode(plainTextJson);
				if (isValid) {
					doSuccessResponse(dingTalkEncryptor, response, "success", msgSignature, timeStamp, nonce);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("回调接口异常{}", e);
		}
	}
	
	
	

	/**
	 * 接收通讯录变更事件回调URL，在第三方授权表中配置
	 */
	@RequestMapping(value = "/orgchange/{suiteid}", method = RequestMethod.POST)
	public void orgchange(@PathVariable("suiteid") String sid, HttpServletRequest request,
			HttpServletResponse response) {

		// 接收请求参数
		String msgSignature = request.getParameter("signature");
		String timeStamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");

		if (StringUtils.isEmpty(sid)) {
			logger.error("通讯录变更回调：执行失败，未能获取正确的套件ID，请检查ding_suite_main配置是否正确。");
			return;
		} else if(sid.indexOf("?") > 0) {
			sid = sid.substring(0, sid.indexOf("?"));
		}

		// 接收POST数据
		try {
			InputStream inputStream = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String buffer = null;
			StringBuffer sb = new StringBuffer();
			while ((buffer = br.readLine()) != null) {
				sb.append(buffer + "\n");
			}
			br.close();
			inputStream.close();
			
			JSONObject json = JSON.parseObject(sb.toString());
			String encryptStr = json.getString("encrypt");
			
			DingSuiteMain suite = dingSuiteMainService.getDingSuiteFromCache(sid);
			
			DingTalkEncryptor dingTalkEncryptor = dmap.get(suite.getSuiteId());
			if(dingTalkEncryptor == null){
				dingTalkEncryptor = new DingTalkEncryptor(suite.getToken(), suite.getEncodingAESKey(),suite.getSuiteId());
				dmap.put(suite.getSuiteId(), dingTalkEncryptor);
			}
			// 只要钉钉推送过来解析正常,就返回SUCCESS标识
			doSuccessResponse(dingTalkEncryptor, response, "success", msgSignature, timeStamp, nonce);
						
			// 对encrypt进行解密
			String plainText = null;
			plainText = dingTalkEncryptor.getDecryptMsg(msgSignature, timeStamp, nonce, encryptStr);
			logger.info("通讯录变更事件回调，推送消息：{}", plainText);
			// 将获取的字符串转json
			JSONObject plainTextJson = JSONObject.parseObject(plainText);
			String eventType = plainTextJson.getString("EventType");
			
			if (eventType.equals("check_url")) {
				// 测试回调url,直接返回
				return;
			}
			// 通知组织架构服务同步处理 实时
			String corpId = plainTextJson.getString("CorpId");
			JSONArray userIds = plainTextJson.getJSONArray("UserId");
			String[] userIdArray = null;
			if (userIds != null && userIds.size() > 0) {
				userIdArray = new String[userIds.size()];
				for (int i = 0; i < userIds.size(); i++) {
					userIdArray[i] = userIds.getString(i);
				}
			}

			JSONArray deptIds = plainTextJson.getJSONArray("DeptId");
			String[] deptIdArray = null;
			if (deptIds != null && deptIds.size() > 0) {
				deptIdArray = new String[deptIds.size()];
				for (int i = 0; i < deptIds.size(); i++) {
					deptIdArray[i] = deptIds.getString(i);
				}
			}

			//通知org实时处理组织架构
			DingOrgModifyMessage orgModifyMessage = new DingOrgModifyMessage();
			orgModifyMessage.setEventType(eventType);
			orgModifyMessage.setSuiteId(sid);
			orgModifyMessage.setCorpId(corpId);
			orgModifyMessage.setDeptId(deptIdArray);
			orgModifyMessage.setUserId(userIdArray);
			orgModifyMessage.setChannel(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic());
			KafkaProducer.getInstance().sendMessage(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic(), orgModifyMessage);
			
		} catch (Exception ex) {
			logger.error("通讯录变更回调异常：", ex);
		}
	}
	
	
	   /**
	    * 注册事件验证 用于初始化老客户
	    * @param suiteid
	    * @param request
	    * @param response
	    * @throws IOException
	    * @throws ServletException
	    */
	   @RequestMapping(value = { "/orgregister/{suiteid}" }, method = RequestMethod.GET)
	   public void orgregister(@PathVariable("suiteid") String suiteid,HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
		   
		   if (StringUtils.isEmpty(suiteid)) {
			  suiteid = "test01"; // TODO
		   }else{
			 // suiteid = suiteid.substring(0,suiteid.indexOf("?"));
		   }
		   DingSuiteMain suite = dingSuiteMainService.getDingSuiteFromCache(suiteid);
		   String corpid = request.getParameter("corpid");
		   if(StringUtils.isNotBlank(corpid)){
			   //单个客户
			   DingSuiteThirdMain dingSuiteThird = dingSuiteThirdMainService.findDingSuiteThirdByCorpId(corpid,suiteid);
			   if(dingSuiteThird==null){
				   logger.error("==="+corpid+"==="+suiteid+"===no auth");
				   return;
			   }
			   registerCallBack(suite,dingSuiteThird);
		   }else{
			   //所有授权客户
			   int pageNo = 1;
			   int pageSize = 300;
			   int startPage = 0;
			   boolean ef = true;
			   do{
				   startPage = pageNo <= 1 ? 0 : (pageNo - 1) * pageSize;
				   List<DingSuiteThirdMain> pageList= dingSuiteThirdMainService.findByPage(suiteid,startPage,pageSize);
				   if(pageList!=null && pageList.size()>0){
					   for (DingSuiteThirdMain dingSuiteThird : pageList) {
						   try{
							   registerCallBack(suite,dingSuiteThird);
						   }catch(Exception e){
							   e.printStackTrace();
						   }
					   }
					   pageNo++;
					   logger.info("==========================================================================page:"+pageNo);
				   }else{
					   ef = false;
				   }
			   }while(ef);
		   }
	   }
	   



	/**
	 * 定时推送Ticket
	 */
	private void doSuiteTicket(String sid, JSONObject json) {
		String ticket = json.getString("SuiteTicket");
		Object cache = cacheableService.getRawObjectFromCache(sid + DingConstants.SUITE_TICKET_KEY);
		if (cache != null) {
			cacheableService.deleteRawObjectInCache(sid + DingConstants.SUITE_TICKET_KEY);
		}
		cacheableService.setRawObjectInCache(sid + DingConstants.SUITE_TICKET_KEY, ticket, 30, TimeUnit.MINUTES);
		logger.info("回调接口，推送Ticket({})。", ticket);
	}

	/**
	 * 回调向ISV推送临时授权码 获取永久授权码 激活企业授权 存db
	 */
	private boolean doTmpAuthCode(String sid, JSONObject json) {

		// 临时授权码，suiteKey
		String tmpAuthCode = json.getString("AuthCode");
		String suiteKey = json.getString("SuiteKey");
		if (StringUtils.isEmpty(tmpAuthCode) || StringUtils.isEmpty(suiteKey)) {
			logger.error("推送临时授权码错误：AuthCode或SuiteKey为空。");
			return false;
		}

		// 从redis获取推送的Ticket
		String suiteTicket = this.getSuiteTicket(sid);
		if (StringUtils.isEmpty(suiteTicket)) {
			logger.error("推送临时授权码错误：获取套件({})Ticket失败,临时授权码：{}", sid,tmpAuthCode);
			return false;
		}

		DingSuiteMain suite = dingSuiteMainService.getDingSuiteFromCache(sid);
		
		// 获取套件Access Token
		String suiteAccessToken = this.getSuiteAccessToken(suite);
		if (StringUtils.isEmpty(suiteAccessToken)) {
			logger.error("推送临时授权码错误：获取套件({})AccessToken失败。", sid);
			return false;
		}

		// 临时授权码获取企业永久授权码(只能换取一次)，第二次需要从数据库获取
		QySuitePermanentCode codeResModel = null;
		String permanentCode = null;
		String corpId = null;
		String corpName = null;

		try {
			codeResModel = dingApiService.getPermanentCode(suite, tmpAuthCode, suiteTicket);
			if (codeResModel == null) {
				logger.error("推送临时授权码错误：套件({})临时授权码({})换企业永久授权码失败，返回结果为Null。", sid, tmpAuthCode);
				return false;
			}

			if (codeResModel.getErrcode() != 0) {
				logger.error("套件({})临时授权码换企业永久授权码错误，errcode={}, errmsg={}。", sid, codeResModel.getErrcode(),
						codeResModel.getErrmsg());
				return false;
			}
		} catch (Exception ex) {
			logger.error("套件({})临时授权码({})换企业永久授权码异常：{}", sid, tmpAuthCode, ex);
			return false;
		}

		// 存储永久授权码
		permanentCode = codeResModel.getPermanentCode();
		corpId = codeResModel.getAuthCorpInfo().getCorpId();
		corpName = codeResModel.getAuthCorpInfo().getCorpName();
		logger.info("套件({})临时授权码({})换企业({})永久授权码({})成功。", sid, tmpAuthCode, corpId + " " + corpName, permanentCode);

		int errCode = -1;
		// 激活套件
		try {
			errCode = dingApiService.activateSuite(corpId, suiteKey, permanentCode, suiteAccessToken);
			if (errCode != 0) {
				logger.warn("回调接口，推送临时授权码错误：对第三方授权企业({})第一次激活套件({})失败，错误码{}。", corpId + " " + corpName, sid, errCode);
				//重试一次
				errCode = dingApiService.activateSuite(corpId, suiteKey, permanentCode, suiteAccessToken);
			} else {
				logger.info("回调接口，推送临时授权码事件：对第三方授权企业({})第一次激活套件({})成功。", corpId + " " + corpName, sid);
			}
			
			if (errCode != 0) {
				logger.error("回调接口，推送临时授权码错误：对第三方授权企业({})第二次激活套件({})失败，错误码{}。", corpId + " " + corpName, sid, errCode);
			} else {
				logger.info("回调接口，推送临时授权码事件：对第三方授权企业({})第二次激活套件({})成功。", corpId + " " + corpName, sid);
			}
		} catch (Exception ex) {
			logger.error("回调接口，推送临时授权码异常：对第三方授权企业({})激活套件({})异常，", ex);
		}

		if (errCode != 0) {
			return false;
		}
		
		boolean isNew = false;
		DingSuiteThirdMain suiteThirdMain = dingSuiteThirdMainService.findDingSuiteThirdByCorpId(corpId,sid);
		if (suiteThirdMain == null) {
			isNew = true;
			suiteThirdMain = new DingSuiteThirdMain();
			suiteThirdMain.setId(IdGenerator.newId());
			suiteThirdMain.setSuiteId(sid);
			suiteThirdMain.setCorpId(corpId);
			suiteThirdMain.setCreateDate(new Date());
		} else {
			suiteThirdMain.setModifiedDate(new Date());
		}
		suiteThirdMain.setPermanentCode(permanentCode);
		suiteThirdMain.setCorpName(corpName);
		suiteThirdMain.setEnabled((short) 1);
		if (isNew) {
			try {
				dingSuiteThirdRepositroy.insert(suiteThirdMain);
				logger.info("回调接口，推送临时授权码事件：新增第三方授权企业({})数据成功。", corpId + " " + corpName);
			} catch (Exception ex) {
				logger.error("回调接口，推送临时授权码异常：新增第三方授权企业({})数据失败，{}", corpId + " " + corpName, ex);
				return false;
			}
		} else {
			try {
				dingSuiteThirdRepositroy.update(suiteThirdMain);
				logger.info("回调接口，推送临时授权码事件：修改第三方授权企业({})数据成功。", corpId + " " + corpName);
			} catch (Exception ex) {
				logger.error("回调接口，推送临时授权码异常：修改第三方授权企业({})数据失败，{}", corpId + " " + corpName, ex);
				return false;
			}
		}
		// 注册事件回调
		registerCallBack(suite, suiteThirdMain);
		
		// 主动调用授权变更信息
		JSONObject changeAuthJson = JSON.parseObject("{'SuiteKey':'" + suiteKey + "','AuthCorpId':'" + corpId + "'}");
		doChangeAuth(sid, changeAuthJson);
		
		//通讯录全量实时同步
		DingOrgModifyMessage orgModifyMessage = new DingOrgModifyMessage();
		orgModifyMessage.setEventType("sync_org");
		orgModifyMessage.setSuiteId(sid);
		orgModifyMessage.setCorpId(corpId);
		orgModifyMessage.setChannel(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic());
		KafkaProducer.getInstance().sendMessage(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic(), orgModifyMessage);
		
		return true;
	}

	/**
	 * 回调向ISV推送授权变更消息
	 */
	private void doChangeAuth(String sid, JSONObject json) {
		// 调用授权变更信息接口，更新数据
		String suiteKey = json.getString("SuiteKey");
		String authCorpId = json.getString("AuthCorpId");

		//
		DingSuiteMain suite = dingSuiteMainService.getDingSuiteFromCache(sid);
		// 获取套件AccessToken
		String suiteAccessToken = this.getSuiteAccessToken(suite);
		if (StringUtils.isEmpty(suiteAccessToken)) {
			logger.error("回调接口，推送授权变更错误：获取套件({})AccessToken失败。", sid);
			return;
		}

		// 获取企业授权信息
		DingSuiteThirdMain suiteThird = dingSuiteThirdMainService.findDingSuiteThirdByCorpId(authCorpId,sid);
		if (suiteThird == null) {
			logger.error("回调接口，推送授权变更消息错误：获取套件({})，授权企业({})永久授权码失败。", sid, authCorpId);
			return;
		}
		String permanentCode = suiteThird.getPermanentCode();

		// 调API查询企业授权数据
		QySuitePermanentCode authInfoModel = null;
		try {
			authInfoModel = dingApiService.getAuthInfo(suite, authCorpId, permanentCode, suiteKey);
		} catch (Exception ex) {
			logger.error("回调接口，推送授权变更消息异常：套件({})API获取企业({})授权信息异常，{}。", sid, authCorpId, ex);
		}

		if (authInfoModel == null) {
			logger.error("回调接口，推送授权变更消息错误 ：套件({})API获取企业({})授权信息返回非预期数据。", sid, authCorpId);
			return;
		}

		if (authInfoModel.getErrcode() != 0) {
			logger.error("回调接口，推送授权变更消息错误 ：套件({})API获取企业({})授权信息错误，{}。", sid, authCorpId, authInfoModel.getErrmsg());
			return;
		}

		// 遍历授权数据，更新DB
		short isSuiteEnabled = 2; // 默认为2：停用状态
		Date now = new Date();
		List<QySuiteAgent> agents = authInfoModel.getAuthInfo().getQySuiteAgents();
		List<DingSuiteThirdApp> thirdApps = dingSuiteThirdappService.findBySuiteThirdId(suiteThird.getId());

		Map<String, DingSuiteThirdApp> map = null;
		if (thirdApps != null && thirdApps.size() > 0) {
			map = new HashMap<String, DingSuiteThirdApp>();
			for (DingSuiteThirdApp thirdApp : thirdApps) {
				map.put(suiteThird.getId() + thirdApp.getAppId(), thirdApp);
			}
		}

		for (QySuiteAgent agent : agents) {
			QySuiteAuthApp resMode = null;
			try {
				resMode = dingApiService.getAgent(suite, authCorpId, permanentCode, agent.getAgentId(), suiteKey);
			} catch (Exception ex) {
				logger.error("回调接口，推送授权变更消息异常：获取企业的应用信息异常，{}", ex);
			}

			if (resMode == null) {
				logger.error("回调接口，推送授权变更消息异常：获取企业的应用信息为空。");
				break;
			}

			boolean isEnabled = resMode.getClose() == 1 ? true : false;
			if (isEnabled) {
				// 只要有一个应用开启，套件状态enabled为1
				isSuiteEnabled = (short) 1;
			}

			DingSuiteThirdApp tapp = map==null?null:map.get(suiteThird.getId() + agent.getAppid());
			boolean isNew = false;
			if (tapp == null) {
				tapp = new DingSuiteThirdApp();
				tapp.setId(IdGenerator.newId());
				tapp.setCreateDate(new Date());
				isNew = true;
			}
			tapp.setAgentId(agent.getAgentId());
			tapp.setAgentName(agent.getName());
			tapp.setLogoUrl(agent.getLogoUrl());
			tapp.setAppId(agent.getAppid());
			tapp.setModifiedDate(new Date());
			tapp.setEnabled(isEnabled);
			tapp.setThirdId(suiteThird.getId());

			if (isNew) {
				dingSuiteThirdappRepositroy.insert(tapp);
				logger.info("回调接口，推送授权变更消息事件：数据库新增第三方企业({})授权应用({})状态({})成功。", authCorpId, agent.getAgentId() + " "
						+ agent.getName(), isEnabled);
			} else {
				dingSuiteThirdappRepositroy.update(tapp);
				logger.info("回调接口，推送授权变更消息事件：数据库更新第三方企业({})授权应用({})状态({})成功。", authCorpId, agent.getAgentId() + " "
						+ agent.getName(), isEnabled);
			}
		}

		// 操作DB，更新表op_platform_suite_third
		suiteThird.setModifiedDate(now);
		suiteThird.setEnabled(isSuiteEnabled);
		dingSuiteThirdRepositroy.update(suiteThird);
		logger.info("回调接口，推送授权变更消息事件：数据库更新第三方企业({})状态({})成功。", authCorpId, isSuiteEnabled);
	}

	/**
	 * "解除授权"事件:更新数据库记录，标记解除授权状态;更新数据库记录，标记解除授权状态
	 */
	private void doSuiteRelieve(String suiteId, JSONObject json) {
		logger.debug("回调接口，开始推送解除授权事件。");
		String authCorpId = json.getString("AuthCorpId");
		DingSuiteThirdMain suiteThird = dingSuiteThirdMainService.findDingSuiteThirdByCorpId(authCorpId,suiteId);
		if (suiteThird != null) {
			suiteThird.setModifiedDate(new Date());
			suiteThird.setEnabled((short) 0);

			// 更新第三方授权应用状态，agentIds为null更新当前套件下的所有应用状态
			dingSuiteThirdappService.updateAgentStatus(authCorpId, suiteId, null);

			// 缓存中清除当前企业的token
			DingSuiteMain suite = dingSuiteMainService.getDingSuiteFromCache(suiteId);
			String tokenKey = suiteThird.getCorpId() + suite.getId() + "token_redis";
			String jsapiKey = suiteThird.getCorpId() + suite.getId() + "jsapi_ticket";
			
			Object cacheToken = cacheableService.getRawObjectFromCache(tokenKey);
			if (cacheToken != null) {
				cacheableService.deleteRawObjectInCache(tokenKey);
			}

			Object cacheJsapiTiekct = cacheableService.getRawObjectFromCache(jsapiKey);
			if (cacheJsapiTiekct != null){
				cacheableService.deleteRawObjectInCache(jsapiKey);
			}
			logger.debug("回调接口，推送解除授权事件：数据库授权状态更新成功，解除套件({})对企业({})授权成功。", suiteId, authCorpId);
		} else {
			logger.error("回调接口，推送解除授权事件：数据库授权状态更新错误，数据库中不存在套件({})授权企业({})的数据。", suiteId, authCorpId);
		}
	}

	/**
	 * "校验序列号"事件 服务提供商在收到"校验序列号"事件推送后,判断该LicenseCode是否合法,如果合法则返回加密"success"。
	 * 返回其它值或者不返回视为LicenseCode不合法
	 */
	private boolean doCheckSuiteLicenseCode(JSONObject json) {

		String suiteKey = json.getString("SuiteKey");
		String authCorpId = json.getString("AuthCorpId");
		String licenseCode = json.getString("LicenseCode");

		if (licenseCode.equals(DingTicketConfigure.verificationCode)) {
			logger.debug("钉钉市场扫码授权流程");

			// 缓存lanCorpId
			String cacheId = authCorpId + suiteKey + DingConstants.CORP_SUITE_SSN;
			Object cache = cacheableService.getRawObjectFromCache(cacheId);
			if (cache != null) {
				cacheableService.deleteRawObjectInCache(cacheId);
			}
			//cacheableService.setRawObjectInCache(cacheId, yworkCorpId, 1, TimeUnit.DAYS);
			return true;
		}
		return false;
	}

	
	/**
	 * 根据套件ID获取套件Ticket
	 */
	private String getSuiteTicket(String suiteId) {
		String suiteTicket = null;
		Object cache = cacheableService.getRawObjectFromCache(suiteId + DingConstants.SUITE_TICKET_KEY);
		if (cache != null) {
			suiteTicket = (String) cache;
		}
		return suiteTicket;
	}

	/**
	 * 根据套件获取套件AccessToken
	 */
	private String getSuiteAccessToken(DingSuiteMain suite) {
		String suiteAccessToken = null;
		String suiteTicket = this.getSuiteTicket(suite.getId());
		if (StringUtils.isEmpty(suiteTicket)) {
			return null;
		}
		suiteAccessToken = accessTokenService.getSuiteToken(suite, suiteTicket);
		return suiteAccessToken;
	}

	
	/**
	 * 成功信息返回
	 */
	private static void doSuccessResponse(DingTalkEncryptor dingTalkEncryptor, HttpServletResponse response,
			String encryptStr, String msgSignature, String timeStamp, String nonceStr) {
		try {
			Long longTime = Long.parseLong(timeStamp);
			Map<String, String> jsonMap = dingTalkEncryptor.getEncryptedMap(encryptStr, longTime, nonceStr);

			PrintWriter printWriter = response.getWriter();
			printWriter.print(JSONObject.toJSONString(jsonMap));
			printWriter.close();
			printWriter = null;
		} catch (Exception e) {
			logger.error("doSuccessResponse", e);
		}
	}
	
	private List<String> registerType = null;
	
	/**
	 * 注册事件回调
	 */
	private void registerCallBack(DingSuiteMain suite, DingSuiteThirdMain suiteThird) {
		JSONObject registerDataJson = new JSONObject();
		if(registerType==null){
			registerType = new ArrayList<String>();
			registerType.add("user_add_org");
			registerType.add("user_modify_org");
			registerType.add("user_leave_org");
			registerType.add("org_admin_add");
			registerType.add("org_admin_remove");
			registerType.add("org_dept_create");
			registerType.add("org_dept_modify");
			registerType.add("org_dept_remove");
			registerType.add("org_remove");
		}
		//chat群会话事件暂未处理注册
		registerDataJson.put("call_back_tag", registerType);
		registerDataJson.put("token", suite.getToken());
		registerDataJson.put("aes_key", suite.getEncodingAESKey());
		registerDataJson.put("url", suite.getAuthBackUrl());
		int errCode = dingApiService.registerCallBack(registerDataJson, suiteThird);
		if (errCode == 71006) {
			//logger.info("注册事件回调：回调地址已经存在，重新调用更新事件回调。");
			errCode = dingApiService.updateCallBack(registerDataJson, suiteThird);
		}
		if(errCode == 0){
			logger.info("---------------success,注册事件回调,公司:"+suiteThird.getCorpName());
		}
		
	}

}
