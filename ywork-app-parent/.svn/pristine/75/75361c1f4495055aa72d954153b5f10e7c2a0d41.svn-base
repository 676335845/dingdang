package me.ywork.suite.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.core.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.messagebus.kafka.KafkaProducer;
import com.heyi.framework.session.Config;
import com.heyi.framework.session.ltpatoken.LtpaToken;
import com.heyi.framework.spring.context.AppContext;

import me.ywork.message.topic.KafkaTopics;
import me.ywork.org.api.message.DingAdminLogonMessage;
import me.ywork.org.api.model.DingOrgUserVo;
import me.ywork.org.api.rpc.ISysOrgManageService;
import me.ywork.suite.constants.DingAPIConstants;
import me.ywork.suite.service.DingCorpTokenService;
import me.ywork.suite.util.DingAPIHttpUtil;
import me.ywork.suite.webapi.service.IDingApiService;
import me.ywork.ticket.message.DingOrgModifyMessage;

@Controller
@RequestMapping("**/alid")
public class DingAuth20Controller {

	private static Logger logger = LoggerFactory.getLogger(DingAuth20Controller.class);

	// 客户端cookie
	private static final String DINGCOOKIENAME = "LWEQYTOKEN";

	@Autowired
	private Redisson redisson;

	// 获取企业Token信息接口
	@Autowired
	private DingCorpTokenService dingCorpTokenService;
	
	private ISysOrgManageService sysOrgManageService;
	
	public ISysOrgManageService getSysOrgManageService() {
		if(sysOrgManageService==null)
			sysOrgManageService = (ISysOrgManageService) AppContext.getBean("sysOrgManageService");
		return sysOrgManageService;
	}
	
	
	/**
	 * test
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/testCheck**", method = RequestMethod.GET)
	public void testCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String suiteid = request.getParameter("sid");
		String corpid = request.getParameter("cid");
		String start = request.getParameter("st");
		String end = request.getParameter("et");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String s = df.format(new Date(Long.valueOf(start)));
		String e = df.format(new Date(Long.valueOf(end)));
		JSONObject jo = new JSONObject();
		jo.put("workDateFrom", s);
		jo.put("workDateTo", e);
		dingApiService.getCheckData(suiteid, corpid, jo.toJSONString());
	}
	

	/**
	 * OAuth2免登
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/oauth2**", method = RequestMethod.GET)
	public void dingAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.debug("钉钉应用免登请求url:" + URLDecoder.decode(request.getRequestURL().toString(), "UTF-8"));
		JSONObject rtnObj = new JSONObject();
		rtnObj.put("auth", "0");

		// 前端Ajax请求返回参数
		String corpid = request.getParameter("corpid");
		String suiteid = request.getParameter("suiteid");
		String code = request.getParameter("code");

		// 必须参数检查
		if (StringUtils.isBlank(code) || StringUtils.isBlank(corpid)) {
			logger.error("免登接口， 参数检查错误：corpId{},code{}。", corpid, code);
			return;
		}
		
		JSONObject exsistsUserInfo = null; //token中已存在的用户信息
		
		Cookie[] cookies = request.getCookies();
		String token = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if ((DINGCOOKIENAME).equalsIgnoreCase(cookies[i].getName())) {
					token = cookies[i].getValue();
				}
			}
		}
		
		DingOrgUserVo sessionUser = null;
		boolean isAdmin = false;
		
		if(StringUtils.isNotEmpty(token)){
			try {
				LtpaToken ltpatoken = new LtpaToken(token);
				if(ltpatoken.isValid()){
					JSONObject mutiCorpUserInfo = JSONObject.parseObject(ltpatoken.getUser());
					
					exsistsUserInfo = mutiCorpUserInfo;
					
					if(mutiCorpUserInfo.containsKey(corpid)){ //支持多企业的token
						JSONObject userInfo = mutiCorpUserInfo.getJSONObject(corpid);
						
						String uid = userInfo.getString("u");
						if(!StringUtils.isEmpty(uid)){
							sessionUser = getSysOrgManageService().findDingOrgUserById(corpid, uid);
							logger.trace("免登用户:{}", uid);
						}
					}
				}
			} catch (Exception e) {
				logger.error("解析token时出错",e);
			}
		}
		
		//没有则oauth2.0
		if(sessionUser == null){
			// 通过CODE换取用户身份；
			String corpAccessToken = dingCorpTokenService.getToken(corpid, suiteid); // 企业token
			String codeURL = DingAPIConstants.GETUSER_BYCODE.replace("CODE", code).replace("ACCESS_TOKEN", corpAccessToken);
			
			JSONObject json = DingAPIHttpUtil.httpRequest(codeURL, "GET", null);
			
			if (!json.getString("errcode").equals("0")) {
				logger.error("免登接口，Code换取用户身份错误：错误码-{}，错误信息-{}。", json.getString("errcode"), json.getString("errmsg"));
				return;
			}
			
			String userid = json.getString("userid");
			
			if (StringUtils.isBlank(userid)) {
				logger.error("免登接口，Code换取用户Id为空。");
				return;
			}
			
			isAdmin = json.getBooleanValue("is_sys");
			
			//sys_level 级别，0：非管理员 1：超级管理员（主管理员） 2：普通管理员（子管理员） 100：老板
			int sys_level = json.getIntValue("sys_level");
			
			if(StringUtils.isNotEmpty(userid)){
				DingOrgUserVo user = getSysOrgManageService().findDingOrgUserById(corpid, userid);
				//如果数据库没有user信息，表示第一次登录微应用，将用户信息保存到数据库
				if (user == null) {
					String getUserUrl = DingAPIConstants.GET_USER.replace("ACCESS_TOKEN", corpAccessToken).replace("USERID",URLEncoder.encode(userid, "UTF-8"));
					
					user = updateUser(getUserUrl , suiteid , corpid, isAdmin);
					
//					DingOrgCorpVo corpvo = sysOrgManageService.findDingOrgCorpById(corpid);
//					if(corpvo==null){
//						//导入组织架构
//						//sysOrgManageService.syncDingOrg(DingSuiteThirdMain.toVo(suitethird));
//					}
				} else if((isAdmin && !user.getFdIsAdmin().booleanValue()) || (!isAdmin && user.getFdIsAdmin().booleanValue())){
					if(logger.isTraceEnabled()){
						logger.trace("当前用户是否管理员： " + isAdmin);
					}
					//管理员的同步
					//update user admin ;
					DingAdminLogonMessage dingAdminLogonMessage = new DingAdminLogonMessage();
					dingAdminLogonMessage.setCorpId(corpid);
					dingAdminLogonMessage.setUserId(userid);
					dingAdminLogonMessage.setAdmin(isAdmin);
					KafkaProducer.getInstance().sendMessage(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic(), dingAdminLogonMessage);
				}
				sessionUser = user;
				sessionUser.setFdIsAdmin(isAdmin);
				
			}
		}
		
		if(sessionUser != null){
			//设置session
			RMap<String, Object> map = redisson.getMap(Config.REDISSESSION_PREX.concat(corpid).concat(sessionUser.getFdUserid()));
			map.expire(Config.EXPIRATION, TimeUnit.MINUTES);
			map.put("curUser", sessionUser);
			
			//设置cookie
			if(exsistsUserInfo == null){
				exsistsUserInfo = new JSONObject();
			}
			
			JSONObject userInfo = new JSONObject();
			userInfo.put("u", sessionUser.getFdUserid());
			userInfo.put("un", sessionUser.getFdName()); // username
			userInfo.put("av", sessionUser.getFdAvatar()); //avatar
			
			if(sessionUser.getFdIsAdmin()!=null && sessionUser.getFdIsAdmin().booleanValue()){
				userInfo.put("a", true);
				if(logger.isTraceEnabled()){
					logger.trace("已在token中设置当前用户为管理员 :{}" ,  sessionUser);
				}
			}
			
			exsistsUserInfo.put("c", corpid); //最后一次免登时的企业id,用来作默认企业id
			exsistsUserInfo.put(corpid, userInfo); //追加某企业的用户信息
			String ltpatoken = LtpaToken.generate(exsistsUserInfo.toJSONString());
			
			if(logger.isTraceEnabled()){
				logger.trace("生成Token:,{}: token{}" ,sessionUser.getFdUserid() , ltpatoken);
			}
			Cookie c = new Cookie(DINGCOOKIENAME, ltpatoken);
			c.setPath("/");
			c.setMaxAge(Config.EXPIRATION * 60);//这里是秒,所以要 * 60
			response.addCookie(c);
			
			//返回内容给前端
			rtnObj.put("auth", "1");
			setResponse(response, rtnObj.toJSONString());
			return;
		}
	}
		

	

	/**
	 * jsapi
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/getJsapiSignature**" }, method = RequestMethod.POST)
	public void getJsapiSignature(HttpServletRequest request, HttpServletResponse response) {
		try {
			String corpId = "", suiteId = "", appId = "" ,url;
			InputStream inputStream = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			JSONObject params = DingAPIHttpUtil.getBody(br);
			
			url = params.getString("url");
			corpId = params.getString("corpid");
			suiteId = params.getString("suiteid");
			appId = params.getString("appid");
			String signature = dingApiService.getJsSignature(suiteId, corpId, appId, url);

			setResponse(response, signature);

		} catch (Exception exception) {
			logger.error("获取JSAPI签名异常：{}", exception);
		}
	}
	
	@Autowired
	private IDingApiService dingApiService;

	/**
	 * 更新用户信息
	 */
	private DingOrgUserVo updateUser(String getUserUrl, String suiteId, String corpId,Boolean isAdmin) {
		try {
			
			JSONObject obj = DingAPIHttpUtil.httpRequest(getUserUrl, "GET", null);

			if (obj.getString("errcode").equals("0")) {
				DingOrgUserVo user = new DingOrgUserVo();
				user.setFdName(obj.getString("name"));
				user.setFdUserid(obj.getString("userid"));
				user.setFdPosition(obj.getString("position"));
				user.setFdAvatar(obj.getString("avatar"));
				user.setFdIsAdmin(obj.getBooleanValue("isAdmin"));
				user.setFdIsBoss(obj.getBoolean("isBoss"));
				
				//通知org实时处理组织架构
				DingOrgModifyMessage orgModifyMessage = new DingOrgModifyMessage();
				orgModifyMessage.setEventType("user_add_org");
				orgModifyMessage.setSuiteId(suiteId);
				orgModifyMessage.setCorpId(corpId);
				orgModifyMessage.setUserId(new String[]{user.getFdUserid()});
				orgModifyMessage.setChannel(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic());
				KafkaProducer.getInstance().sendMessage(KafkaTopics.DING_ORG_MANAGER_REAL.getTopic(), orgModifyMessage);
				
				return user;
			}
		} catch (Exception e) {
			logger.error("updateUser exception: {}", e);
		}
		return null;
	}

	protected static String getRequestString(HttpServletRequest request) {
		StringBuilder callBackXmlBuilder = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line = null;

			while ((line = br.readLine()) != null) {
				callBackXmlBuilder.append(line);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return callBackXmlBuilder.toString();
	}

	private void setResponse(final HttpServletResponse response, String words) throws IOException {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			writer.append(words);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error("setResponse exception: {}", e);
		}
	}

	// 检查是不是ajax请求
	static boolean isAjax(HttpServletRequest request) {
		if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
			return true;
		}
		return false;
	}

	static boolean isNull(String str) {
		return StringUtils.isEmpty(str) || "null".equals(str.trim());
	}

}
