package me.ywork.salarybill.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.salarybill.SalaryBillConfigure;
import me.ywork.salarybill.base.SalaryPwdType;
import me.ywork.salarybill.model.PwdCheckResult;
import me.ywork.salarybill.model.SalaryBillModel;
import me.ywork.salarybill.model.SalaryBillPwdSetModel;
import me.ywork.salarybill.model.SalaryBillPwdTempModel;
import me.ywork.salarybill.model.SalaryBillTemplateModel;
import me.ywork.salarybill.model.SalaryBillViewModel;
import me.ywork.salarybill.model.SalaryHistoryDispalyModel;
import me.ywork.salarybill.service.SalaryBillPwdSetService;
import me.ywork.salarybill.service.SalaryBillService;


@Controller
@RequestMapping(value = { "**/salarybillbiz" })
public class SalaryBillController extends RestController<SalaryBillService> {
	
	private static Logger logger = LoggerFactory.getLogger(SalaryBillController.class);
	
	@Override
	public void autoAuthenticate(HttpServletRequest request, HttpServletResponse response) {
		//super.autoAuthenticate(request, response);

		try {
			//String url = request.getRequestURI();
			
			String param = request.getQueryString();
			// 获取url后面的参数
//			String param = null;
//			int pos = url.indexOf("?");
//			if (pos > -1) {
//				param = url.substring(pos + 1);
//			}

			String homeUrl = this.getHomePageUrl("//"+request.getServerName(), param);

			logger.debug("即将转向主页：{}", homeUrl);

			response.sendRedirect(homeUrl);

		} catch (Exception e) {
			logger.error("autoAuthenticate", e);
		}
	}

	protected String getHomePageUrl(String serverName, String param) {
		//获取agentid
		String agentid = "";
		String corpid = "";
		String appid = "";
		String suiteid = "";
		try{
			if(StringUtils.isNotBlank(param)){
				String[] params = param.split("&");
				for(String p : params){
					String[] ps = p.split("=");
					String key = ps[0];
					if("corpid".equals(key)){
						corpid = ps[1];
					}else if("appid".equals(key)){
						appid = ps[1];
					}else if("suiteid".equals(key)){
						suiteid = ps[1];
					}
				}
				agentid = salaryBillService.findAgentId(corpid, suiteid, appid);
				param += "&agentid="+agentid;
			}
		}catch(Exception e2){
			e2.printStackTrace();
			logger.error("salarybill get agentid error"+corpid+","+appid+","+suiteid,e2);
		}

	
		if (StringUtils.isBlank(serverName)) {
			serverName = SalaryBillConfigure.domainName;
		}
		return serverName + SalaryBillConfigure.nginxStaticUrl + "/" + SalaryBillConfigure.homePage
				+ (param == null ? "" : "?" + param+"&v=" + SalaryBillConfigure.mobileVersion);
		
	}

	@Override
	protected String getHomePageUrl(String param) {
//		//获取agentid
//		String agentid = "";
//		String corpid = "";
//		String appid = "";
//		String suiteid = "";
//		try{
//			if(StringUtils.isNotBlank(param)){
//				int s = param.indexOf("corpid=")+7;
//				corpid =param.substring(s ,s+20) ;
//				s = param.indexOf("appid=")+6;
//				appid = param.substring(s, s+3);
//				s = param.indexOf("suiteid=")+8;
//				int e = param.indexOf("&", s);
//				suiteid = param.substring(s, e);
//
//				//老用户
//				if("267".equals(appid)||"177".equals(appid)){
//					if(appMap.get(corpid)==null && !"dingbcbdb1b9db741f26".equals(corpid)){
//						Map<String,String> app = new HashMap<String,String>();
//						app.put("appid", appid);
//						app.put("suiteid", suiteid);
//						appMap.put(corpid, app);
//					}
//				}
//
//				agentid = salaryBillService.findAgentId(corpid, suiteid, appid);
//				param += "&agentid="+agentid;
//			}
//		}catch(Exception e2){
//			e2.printStackTrace();
//			logger.error("salarybill get agentid error"+corpid+","+appid+","+suiteid,e2);
//		}
		
		return SalaryBillConfigure.domainName + SalaryBillConfigure.nginxStaticUrl + "/" + SalaryBillConfigure.homePage
				+ (param == null ? "" : "?" + param+"&v=" + SalaryBillConfigure.mobileVersion);
	}

	@Autowired
	protected SalaryBillService salaryBillService;
	
	@Autowired
	protected SalaryBillPwdSetService salaryBillPwdSetService;
	
	/**
	 * 用户是否设置密码
	 * 
	 * @param request
	 * @param voteModel
	 *            客户端上传的投票内容
	 * @return 创建成功后的ID值
	 */
	//@RestDocExport(exportNo="001")
	//@RestDoc(description = "用户是否设置密码", returnMethodName = "",
	//smapleClassName = "",
	//	requestBodyMethodName = "eversetpwd")
	@RequestMapping(value = { "/eversetpwd**" }, method = RequestMethod.GET)
	public void eversetpwd(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getCallContext(request);
			//是否需要密码
			Boolean result = salaryBillPwdSetService.evensetpwd(callContext.getCorpId(),callContext.getUserId(),SalaryPwdType.User.getCode());
			Boolean havePwd =salaryBillPwdSetService.checkHaveSetPasswd(callContext.getCorpId(),callContext.getUserId(),SalaryPwdType.User.getCode());
			String avatar = callContext.getAvatar();
			String name = callContext.getUserName();
			JSONObject jo = new JSONObject();
			//result=true表示无需密码
			jo.put("result", result);
			jo.put("havePwd", havePwd);
			jo.put("avatar", avatar);
			jo.put("name", name);
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, jo);
			
		} catch (Exception e) {
			logger.error("f eversetpwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}

	
	/**
	 * 用户设置密码
	 * 
	 * @param request
	 * @param voteModel
	 *            客户端上传的投票内容
	 * @return 创建成功后的ID值
	 */
//	@RestDocExport(exportNo="002")
//	@RestDoc(description = "用户设置密码", returnMethodName = "",
//	smapleClassName = "",
//		requestBodyMethodName = "setuserpwd")
	@RequestMapping(value = { "/setuserpwd**" }, method = RequestMethod.POST)
	public void setuserpwd(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getCallContext(request);
			
			SalaryBillPwdSetModel salaryBillPwdSetModel = this.fromInputJson(request, SalaryBillPwdSetModel.class);
			Boolean result = salaryBillPwdSetService.setUserPwd(callContext.getCorpId(), callContext.getUserId(), SalaryPwdType.User.getCode(), salaryBillPwdSetModel);
			
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, result);
			
		} catch (Exception e) {
			logger.error("f setuserpwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	/**
	 * 用户密码校验-显示工资信息
	 * 
	 * @param request
	 * @param salaryBillPwdSetModel
	 *            客户端上传的投票内容
	 * @return 创建成功后的ID值
	 */
//	@RestDocExport(exportNo="003")
//	@RestDoc(description = "用户密码校验", returnMethodName = "",
//	smapleClassName = "",
//		requestBodyMethodName = "comparepwd")
	@RequestMapping(value = { "/comparepwd**" }, method = RequestMethod.POST)
	public void comparepwd(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getCallContext(request);
			
			SalaryBillPwdSetModel salaryBillPwdSetModel = this.fromInputJson(request, SalaryBillPwdSetModel.class);
			PwdCheckResult pwdCheckResult = salaryBillPwdSetService.comparePwd(callContext.getCorpId(),callContext.getUserId(),
					salaryBillPwdSetModel);
			
			this.writeSuccessJsonToClient(response, pwdCheckResult);
		} catch (Exception e) {
			logger.error("f comparepwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	/**
	 * 用户工资信息
	 * 
	 * @param request
	 *            客户端上传的投票内容
	 * @return 创建成功后的ID值
	 */
//	@RestDocExport(exportNo="004")
//	@RestDoc(description = "用户工资信息", returnMethodName = "",
//	smapleClassName = "",
//		requestBodyMethodName = "")
	@RequestMapping(value = { "/viewsalary**" }, method = RequestMethod.GET)
	public void viewsalary(HttpServletRequest request, HttpServletResponse response
			) {
		try {
			CallContext callContext = this.getCallContext(request);
			
			String salaryLogid = request.getParameter("sid");
			if("undefined".equals(salaryLogid)){
				salaryLogid = "";
			}
			
			String templateid = request.getParameter("tid");
			
			String salaryMonths = request.getParameter("salaryMonth");
			Integer salaryMonth = null ;
			if(StringUtils.isNotBlank(salaryMonths)){
				salaryMonth = Integer.valueOf(salaryMonths);
			}
			
			SalaryBillModel salaryBillModel = salaryBillService.viewSalary(callContext,salaryMonth,true,salaryLogid,templateid);
			List<SalaryBillTemplateModel> template = new ArrayList<SalaryBillTemplateModel>();
			if(salaryBillModel != null){
				template = salaryBillService.hasDataSystemTemplate(callContext.getCorpId(), callContext.getUserId(),salaryBillModel.getSalaryMonth());
				int i = salaryBillService.setReaded(callContext.getCorpId(), salaryBillModel.getId());
				salaryBillModel.setReadFlag(true);
			}
			SalaryBillViewModel m = new SalaryBillViewModel();
			m.setSalaryBill(salaryBillModel);
			m.setSalaryTemplates(template);
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, m);
			
		} catch (Exception e) {
			logger.error("f viewsalary", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**用户移动端设置查看数据是否需要密码
	 * 
	 * @param request
	 * @param response
	 */
//	@RestDocExport(exportNo="005")
//	@RestDoc(description = "用户移动端设置查看数据是否需要密码", returnMethodName = "",
//	smapleClassName = "",
//		requestBodyMethodName = "setPasswdOnOf")
	@RequestMapping(value = { "/setPasswdOnOf**" }, method = RequestMethod.POST)
	public void setPasswdOnOf(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getCallContext(request);
			
			SalaryBillPwdTempModel salaryBillPwdTempModel = this.fromInputJson(request, SalaryBillPwdTempModel.class);
			salaryBillPwdTempModel.setPasswordType(SalaryPwdType.User.getCode());
			salaryBillPwdTempModel.setCompanyId(callContext.getCorpId());
			salaryBillPwdTempModel.setUserId(callContext.getUserId());
			PwdCheckResult pwdCheckResult = salaryBillPwdSetService.setPasswdOnOf(callContext.getCorpId(),callContext.getUserId(),
					salaryBillPwdTempModel);
			this.writeSuccessJsonToClient(response, pwdCheckResult);
		} catch (Exception e) {
			logger.error("setPasswdOnOf", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	/**
	 * 获取用户是否设置密码
	 * 
	 * @param request
	 * @param voteModel
	 *            客户端上传的投票内容
	 * @return 创建成功后的ID值
	 */
//	@RestDocExport(exportNo="006")
//	@RestDoc(description = "获取用户是否设置密码", returnMethodName = "",
//	smapleClassName = "",
//		requestBodyMethodName = "getPwdStatus")
	@RequestMapping(value = { "/getPwdStatus**" }, method = RequestMethod.GET)
	public void getPwdStatus(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getCallContext(request);
			
			Boolean pwdOpenStatus =salaryBillPwdSetService.getPWdStatus(callContext.getCorpId(),callContext.getUserId(),SalaryPwdType.User.getCode());
			JSONObject jo = new JSONObject();
			jo.put("pwdOpenStatus", pwdOpenStatus);
			this.writeSuccessJsonToClient(response, jo);
		} catch (Exception e) {
			logger.error("getPwdStatus", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	/**
	 * 用户历史数据
	 * 
	 * @param request
	 *            客户端上传的投票内容
	 * @return 创建成功后的ID值
	 */
//	@RestDocExport(exportNo="004")
//	@RestDoc(description = "用户工资信息", returnMethodName = "",
//	smapleClassName = "",
//		requestBodyMethodName = "")
	@RequestMapping(value = { "/history**" }, method = RequestMethod.GET)
	public void history(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getCallContext(request);
			
			List<SalaryHistoryDispalyModel> historySalarys = salaryBillService.getHistorySalary(callContext);
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response,historySalarys);
			
		} catch (Exception e) {
			logger.error("f viewsalary", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	@Override
	protected SalaryBillService getService() {
		return salaryBillService;
	}

}
