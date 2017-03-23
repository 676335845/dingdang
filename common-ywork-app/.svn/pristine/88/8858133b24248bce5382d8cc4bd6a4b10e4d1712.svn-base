package me.ywork.controller;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;

import me.ywork.base.service.BizService;
import me.ywork.context.CallContext;
import me.ywork.result.PcAdmin;
import me.ywork.util.AESUtil;

/**
 * 主要为手机端提供接口的控制器
 * 
 * @author TangGang 2015年8月11日
 * 
 * @param <S>
 *            控制器对应的主业务接口类
 */
public abstract class RestController<S extends BizService> extends
		ServiceController<S> {
	private static Logger logger = LoggerFactory
			.getLogger(RestController.class);
	

	/**
	 * 钉钉免登接口<br/>通过钉钉点击套件调用，完成登陆认证并跳转到其对应的主页
	 * @param request   请求对象
	 * @param response  响应对象
	 */
	@RequestMapping(value = { "/autologin**" }, method = RequestMethod.GET)
	public void autoAuthenticate(HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String url = request.getRequestURI();
			// 获取url后面的参数
			String param = null;
			int pos = url.indexOf("?");
			if (pos > -1) {
				param = url.substring(pos + 1);
			}
			
			String homeUrl = this.getHomePageUrl(param);

			logger.debug("即将转向主页：{}", homeUrl);

			response.sendRedirect(homeUrl);

		} catch (Exception e) {
			logger.error("autoAuthenticate", e);
		}
	}
	
	/**
	 * @return 在具体的控制器中拼接当前应用所对应的主面链接
	 */
	protected abstract String getHomePageUrl(String param);
	
	
	/**
	 * 信息反馈接口<br/>
	 * @param request   请求对象
	 * @param response  响应对象
	 */
	@RequestMapping(value = { "/basefeed**" }, method = RequestMethod.GET)
	public void basefeed(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("appid") String appid
			) {

		try {
			CallContext callContext = this.getCallContext(request);
			
			String corpId = callContext.getCorpId();
			
			String userId = callContext.getUserId();
			
			long d = System.currentTimeMillis();
			
			String sources = appid+"|"+corpId+"|"+userId+"|"+d;
			
			String aecStr = AESUtil.encrypt(sources,"c5a1149f163dd7a072e235ccc2566c98");
			
			String feedUrl = "";
			
			String homeUrl = this.getHomePageUrl(null);
			
			String domainName =  homeUrl.substring(0,homeUrl.indexOf("/",10));
			
			feedUrl =domainName+"/alid/app/feedback/feedbackmain/toFeedBackPage?token="+aecStr;
			response.sendRedirect(feedUrl);
		} catch (Exception e) {
			logger.error("basefeed", e);
		}
	}
	

	
	/**
	 * 钉钉pc后台免登
	 * @param request   请求对象
	 * @param response  响应对象
	 * @throws Exception
	 */
	@RequestMapping(value = { "/pcautologin**" }, method = RequestMethod.GET)
	public void pcautoAuthenticate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String type = request.getParameter("type");//环境
		
		String params = request.getParameter("forward");//跳转至
		String[] param = params.split("\\?");
		String forward = param[0];
		String code = param[1].substring(5);
		
		PcAdmin paAdmin = getUserFromCookie(request, response);
		JSONObject jo = null;
		String corpId = "";
		String userName = "";
		String userId = "";
		
		boolean isReLogin = false;

		// 判断code是否为空，为空则取cookie里的用户信息
		if (StringUtils.isBlank(code) && paAdmin == null) {
			isReLogin = true;
		}else{
			JSONObject adminInfo = null;
			try {
				//adminInfo = getDingAPIRpcService().getPcAdminInfo(code);
				//throw new UnImplementedException("暂未支持从PC进行免登。");
			} catch (Exception e) {
				logger.error("PC后台免登发生错误", e);
			}
			if (adminInfo != null) {
				JSONObject obj = adminInfo.getJSONObject("corp_info");
				if (obj != null) {
					corpId = obj.getString("corpid");
				}
				JSONObject userinfo = adminInfo.getJSONObject("user_info");
				if (userinfo != null) {
					userName = userinfo.getString("name");
					userId = userinfo.getString("userid");
				}
				jo = new JSONObject();
				jo.put("corpId", corpId);
				jo.put("userName", userName);
				jo.put("userId", userId);
				String cookie = AESUtil.encrypt(jo.toJSONString(),AES_KEY);
				Cookie TOKEN = new Cookie(PC_TOKEN, cookie);
				TOKEN.setPath("/");
				TOKEN.setMaxAge(30 * 60 * 6);
				response.addCookie(TOKEN);
			}
		}

		String domainName = "https://test.ywork.me";
		
		if ("test".equals(type)) {
			domainName = "https://test.ywork.me";
		}
		
		if (isReLogin) {
			response.sendRedirect(domainName +"/alid/static/"+forward
					+ "/_pc/relogin.html");
			return;
		}
		
		response.sendRedirect(domainName +"/alid/static/"+forward
				+ "/_pc/index.html?cropid="+corpId);
		return;
	}
	
	
	public PcAdmin getUserFromCookie(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cookieToken = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (PC_TOKEN.equals(cookie.getName())) {
					try{
						cookieToken = AESUtil.decrypt(cookie.getValue(), AES_KEY);
						//解析json
						JSONObject jo = JSONObject.parseObject(cookieToken);
						String corpId = jo.getString("corpId");
						String userId = jo.getString("userId");
						String userName = jo.getString("userName");
						PcAdmin pcAdmin = new PcAdmin();
						pcAdmin.setCorpid(corpId);
						pcAdmin.setUserid(userId);
						pcAdmin.setName(userName);
						return pcAdmin;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	private static final String PC_TOKEN = "dingPcToken";
	
	private static final String AES_KEY = "c5a1149f163dd7a072e235ccc2566c98";
	
	/*private IDingAPIRpcService dingAPIRpcService;
	
	private IDingAPIRpcService getDingAPIRpcService() {
		if (dingAPIRpcService == null) {
			dingAPIRpcService = (IDingAPIRpcService) AppContext
					.getBean("dingAPIRpcService");
		}
		return dingAPIRpcService;
	}*/
}
