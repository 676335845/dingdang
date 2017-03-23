package me.ywork.salary.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.spring.context.AppContext;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.salary.enumeration.SalOptPersonType;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalStaffSalReportDetailModel;
import me.ywork.salary.model.SalUpdateMutiStaffModel;
import me.ywork.salary.service.SalInfoService;
import me.ywork.salary.service.SalReportService;
import me.ywork.salary.service.SalSynService;
import me.ywork.salary.util.ValidateUtils;
import me.ywork.suite.api.model.CorpAdminRpcModel;
import me.ywork.suite.api.rpc.IDingAPIRpcService;
import me.ywork.util.AESUtil;

/**
 * PC端界面，一些公共的接口的控制器
 * 
 */
@Controller
@RequestMapping(value = { "**/app/calsalary/pc" })
public class SalPcController extends RestController<SalInfoService> {
	@Autowired
	private SalReportService salReportService;
	@Autowired
	private SalSynService salSynService;
	private static final Logger logger = LoggerFactory.getLogger(SalPcController.class);
	
	private IDingAPIRpcService dingAPIRpcService;
	
	private IDingAPIRpcService getDingAPIRpcService() {
		if (dingAPIRpcService == null) {
			dingAPIRpcService = (IDingAPIRpcService) AppContext.getBean("dingAPIRpcService");
		}
		return dingAPIRpcService;
	}
	
	private static final String PC_TOKEN = "salarymanager";
	
	private static final String AES_KEY = "c5a1149f163dd7a072e235ccc2566c98";

	public String getUserCookie(HttpServletRequest request) throws Exception {
		String cookieToken = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (PC_TOKEN.equals(cookie.getName())) {
					cookieToken = AESUtil.decrypt(cookie.getValue(), AES_KEY);
					break;
				}
			}
		}
		return cookieToken;
	}

	/**
	 * 进入主页面
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/index**" }, method = RequestMethod.GET)
	public void index(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String code = request.getParameter("code");
		String cookieValue = getUserCookie(request);
		String corpId = "";
		
		boolean isReLogin = false;
		CorpAdminRpcModel adminInfo = null;
		// 判断code是否为空，为空则取cookie里的用户信息
		if (StringUtils.isBlank(code) && StringUtils.isBlank(cookieValue)) {
			isReLogin = true;
		}else{
			try {
				adminInfo = getDingAPIRpcService().getPcAdminInfo(code,"2002");
				corpId = adminInfo.getCorpId();
			} catch (Exception e) {
				logger.error("PC后台免登发生错误", e);
			}
			
			if (adminInfo != null) {
				String cookie = AESUtil.encrypt(JSONObject.toJSONString(adminInfo),AES_KEY);
				Cookie TOKEN = new Cookie(PC_TOKEN, cookie);
				TOKEN.setPath("/");
				TOKEN.setMaxAge(30 * 60 * 6);
				response.addCookie(TOKEN);
			}
		}

		String domainName = request.getServerName();
		if (StringUtils.isBlank(domainName)) {
			domainName = "dd.ywork.me";
		}

		if (isReLogin) {
			//response.sendRedirect("//"+domainName + "/yw/pc/notify/web/" + "/relogin.html");
			return;
		}
		
		response.sendRedirect("//"+domainName + "/yw/pc/salary/index.html?corpid="+corpId);
		return;
	}
	
	
	/**
	 * 获取选中人员该月工资详情(修改浮动款项):传递工资表ID和多个员工ID去返回多个员工的工资数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getSelectedStaff**", method = RequestMethod.POST)
	public void getSelectedStaff(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext = this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getSelectedStaff -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try {
			 List<SalStaffSalReportDetailModel> salStaffSalReportDetailModels = null;
			  SalUpdateMutiStaffModel salUpdateMutiStaffModel = this.fromInputJson(request, SalUpdateMutiStaffModel.class);
			  if(salUpdateMutiStaffModel == null){
				  logger.error("getSelectedStaff -- ".concat(corpId).concat("-- salUpdateMutiStaffModel is null"));
				   this.writeFailJsonToClient(response);
				   return;
			  }
			 if(salUpdateMutiStaffModel.getOptType() == SalOptPersonType.FLOAT.getCode()){
				 List<SalStaffBaseInfoModel> staffList = salUpdateMutiStaffModel.getStaffBaseInfoModels();
				 if(staffList!=null){
					 logger.info("getSelectedStaff 请求获得：".concat(corpId).concat("的").concat("浮动工资列表"));
				 }else{
					 this.writeFailJsonToClient(response);
					 return;
				 }
				 String reportId = salUpdateMutiStaffModel.getReportId();
				 salStaffSalReportDetailModels= salReportService.getSelectStaffFloatSalData(staffList, corpId, reportId);
				 if(salStaffSalReportDetailModels!=null){
				      logger.info("getSelectedStaff 返回获得：".concat(corpId).concat("的").concat(""+salStaffSalReportDetailModels.size()+"").concat("位浮动工资列表"));
				 }
				 
				 /**
					 * 
					 *  数字方面的都除以100
					 * 
					 */
				 Double otherAftertaxDeduct =null;
				 Double annualBonus=null;
				 Double monthBonus=null;
				 Double otherAftertaxSal=null;
				 Double otherPretaxSal=null;
				 Double otherPretaxDeduct=null;
				 for(SalStaffSalReportDetailModel salStaffSalReportDetailModel:salStaffSalReportDetailModels){
					 otherAftertaxDeduct=salStaffSalReportDetailModel.getOtherAftertaxDeduct();
					 if(otherAftertaxDeduct!=null){
					     salStaffSalReportDetailModel.setOtherAftertaxDeduct(otherAftertaxDeduct/100);
					 }
					 annualBonus = salStaffSalReportDetailModel.getAnnualBonus();
					 if(annualBonus!=null){
					     salStaffSalReportDetailModel.setAnnualBonus(annualBonus/100);
					 }
					 monthBonus=salStaffSalReportDetailModel.getMonthBonus();
					 if(monthBonus!=null){
					     salStaffSalReportDetailModel.setMonthBonus(monthBonus/100);
					 }
					 otherAftertaxSal = salStaffSalReportDetailModel.getOtherAftertaxSal();
					 if(annualBonus!=null){
					     salStaffSalReportDetailModel.setOtherAftertaxSal(otherAftertaxSal/100);
					 }
					 otherPretaxSal=salStaffSalReportDetailModel.getOtherPretaxSal();
					 if(otherAftertaxDeduct!=null){
					     salStaffSalReportDetailModel.setOtherPretaxSal(otherPretaxSal/100);
					 }
					 otherPretaxDeduct = salStaffSalReportDetailModel.getOtherPretaxDeduct();
					 if(annualBonus!=null){
					     salStaffSalReportDetailModel.setOtherPretaxDeduct(otherPretaxDeduct/100);
					 }
				 }
			 }else if(salUpdateMutiStaffModel.getOptType() == SalOptPersonType.RESET.getCode()){
				   
			 }
			this.writeSuccessJsonToClient(response, salStaffSalReportDetailModels);
		} catch (Exception e) {
			  logger.error("getSelectedStaff -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 同步企业到我们的叮当薪资
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/synchronizedCorpToDingSal**",method = RequestMethod.POST)
	public void synchronizedCorpToDingSal(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext =  this.getPcCallContext(request);
		String corpId = callContext.getCorpId();
		SalUpdateMutiStaffModel salUpdateMutiStaffModel = this.fromInputJson(request, SalUpdateMutiStaffModel.class);
		List<String> corpList = salUpdateMutiStaffModel.getCorpList();
		try{
			salSynService.synchCopInfo(corpList);
		    this.writeSuccessJsonToClient(response, Boolean.TRUE);
		}catch(Exception e){
			  logger.error("getSelectedStaff -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}
	
	@Override
	protected String getHomePageUrl(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SalInfoService getService() {
		// TODO Auto-generated method stub
		return null;
	}

}
