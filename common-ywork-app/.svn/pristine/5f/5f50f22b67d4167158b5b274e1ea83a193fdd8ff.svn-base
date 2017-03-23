package me.ywork.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import me.ywork.base.service.BizService;
import me.ywork.context.CallContext;
import me.ywork.context.DefaultCallContext;
import me.ywork.result.JsonResult;
import me.ywork.util.AESUtil;

/**
 * 带有业务服务接口的控制器
 * 
 * @author TangGang 2015年8月11日
 * 
 * @param <S>
 *            控制器对应的主业务接口类
 */
public abstract class ServiceController<S extends BizService> extends
		EventPublisherController {
	private static Logger logger = LoggerFactory.getLogger(ServiceController.class);
	
/*	@Autowired
	private ISysOrgCoreService sysOrgCoreService;*/

	protected S service;

	public ServiceController() {
		super();

		//this.service = this.getService();
	}

	/**
	 * @return 由子类提供注入的service对象
	 */
	protected abstract S getService();

	/**
	 * 获取HttpServletRequest对象
	 * 
	 * @param request
	 *            客户请求
	 * @return
	 */
	@Override
	protected HttpServletRequest getHttpServletRequest(NativeWebRequest request) {
		if (request instanceof HttpServletRequest) {
			return (HttpServletRequest) request;
		}

		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);

		return servletRequest;
	}

	/**
	 * 从客户端请求中解析登陆客户信息
	 * 
	 * @param request
	 *            客户请求
	 * @return
	 */
	protected CallContext getCallContext(NativeWebRequest request) {
		HttpServletRequest servletRequest = this.getHttpServletRequest(request);

		return this.getCallContext(servletRequest);
	}

	/**
	 * 从客户端请求中解析登陆客户信息
	 * 
	 * @param request
	 *            客户请求
	 * @return
	 */
	protected CallContext getCallContext(HttpServletRequest request) {
		DefaultCallContext defaultCallContext = new DefaultCallContext();
		
		defaultCallContext.setCorpId(request.getParameter("_curCorpId"));
		defaultCallContext.setUserId(request.getParameter("_curUserId"));
		defaultCallContext.setUserName(request.getParameter("_curUserName"));
		defaultCallContext.setIsAdmin((Boolean) request.getAttribute("_curUserIsAdmin"));
		defaultCallContext.setAvatar(request.getParameter("_avatar"));
		defaultCallContext.setDomainName(request.getServerName());
		//defaultCallContext.setIsBoss((Boolean) request.getAttribute("_isBoss"));
		//logger.info("defaultCallContext:"+JSONObject.toJSONString(defaultCallContext));
		return defaultCallContext;
	}
	
	private static final String PC_TOKEN = "salarymanager";
	
	private static final String AES_KEY = "c5a1149f163dd7a072e235ccc2566c98";
	
	/**
	 * 从客户端请求中解析登陆客户信息 pc
	 * 
	 * @param request
	 *            客户请求
	 * @return
	 */
	protected CallContext getPcCallContext(HttpServletRequest request) {
		String cookieValue = "";
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (PC_TOKEN.equals(cookie.getName())) {
						cookieValue = AESUtil.decrypt(cookie.getValue(), AES_KEY);
						break;
					}
				}
			}
			
			if(StringUtils.isBlank(cookieValue)){
				return null;
			}
			
			JSONObject jo = JSONObject.parseObject(cookieValue);
			
			String corpId = jo.getString("corpId");
			String userId = jo.getString("userId");
			String userName = jo.getString("userName");
			//String corpName = jo.getString("corpName");
			String avatar = jo.getString("avatar");
			boolean isAdmin = jo.getBoolean("admin");
			
			DefaultCallContext defaultCallContext = new DefaultCallContext();
			
			defaultCallContext.setCorpId(corpId);
			defaultCallContext.setUserId(userId);
			defaultCallContext.setUserName(userName);
			defaultCallContext.setIsAdmin(isAdmin);
			defaultCallContext.setAvatar(avatar);
			defaultCallContext.setDomainName(request.getServerName());
			return defaultCallContext;
		}catch(Exception e){
			logger.error("getPcCallContext error,cookieVale:"+cookieValue,e);
		}
		return null;
	}

	protected String getRequestString(NativeWebRequest request)
			throws Exception {
		return this.getRequestString(this.getHttpServletRequest(request));
	}

	protected String getRequestString(HttpServletRequest request){
		StringBuilder callBackXmlBuilder = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getInputStream(), "UTF-8"));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				callBackXmlBuilder.append(line);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return callBackXmlBuilder.toString();
	}

	protected <E> E fromInputJson(NativeWebRequest request, Class<E> eClass) {
		return this.fromInputJson(this.getHttpServletRequest(request), eClass);
	}

	protected <E> E fromInputJson(HttpServletRequest request, Class<E> eClass)
			 {
		String json = this.getRequestString(request);
		
		logger.trace("接收到客户端上传JSON：{}", json);

		E e = JSON.parseObject(json, eClass);

		
		return e;
	}

	protected <E> E fromInputJson(HttpServletRequest request, TypeReference<E> typeReference)
	{
		String json = this.getRequestString(request);

		logger.trace("接收到客户端上传JSON：{}", json);

		E e = JSON.parseObject(json, typeReference);


		return e;
	}
	
	protected <E> List<E> fromInputJsonToList(HttpServletRequest request, Class<E> eClass) {
		String json = this.getRequestString(request);

		logger.trace("接收到客户端上传JSON：{}", json);

		return JSON.parseArray(json, eClass);
	}

	protected <E> String toOutputJson(E e){

		return JSON.toJSONString(e, SerializerFeature.DisableCircularReferenceDetect);
	}

	protected <E> void writeJsonToClient(
			HttpServletResponse response, E e) {
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		String json = this.toOutputJson(e);
		
		logger.trace("向客户端输出JSON对象:{}", json);
		try {
			response.getWriter().print(json);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

	}

	protected <E> void writeJsonToClient(HttpServletResponse response, Integer errCode, String errMessage, E data) {
		if (errCode == null) {
			throw new NullPointerException("wirteJsonToClient - param errCode is null.");
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		JsonResult<E> jsonResult = JsonResult.getSuccessJsonResult(data);
		jsonResult.setErrcode(errCode);
		jsonResult.setErrmsg(errMessage);

		String json = this.toOutputJson(jsonResult);

		logger.trace("向客户端输出JSON对象:{}", json);
		try {
			response.getWriter().print(json);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected <E> void writeJsonToClient(HttpServletResponse response, Integer errCode, String errMessage) {
		this.writeJsonToClient(response, errCode, errMessage, null);
	}


	protected <E> void writeSuccessJsonToClient(HttpServletResponse response, E model ) {
		this.writeJsonToClient(response, JsonResult.getSuccessJsonResult(model));
	}
	
	protected <E> void writePermissionDeniedJsonToClient(HttpServletResponse response, E model ) {
		this.writeJsonToClient(response, JsonResult.getPermissionDeniedJsonResult(model));
	}
	
	protected <E> void writeFailDataJsonToClient(HttpServletResponse response, E model) {
		this.writeJsonToClient(response, JsonResult.getFailJsonResult(model));
	}
	
	protected void writeFailJsonToClient(HttpServletResponse response) {
		this.writeJsonToClient(response, JsonResult.getFailJsonResult(null));
	}
	
	
}
