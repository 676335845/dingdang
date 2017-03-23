package me.ywork.salary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.MD5Util;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.page.PageData;
import me.ywork.page.PageRequest;
import me.ywork.salary.enumeration.SalDetailShowType;
import me.ywork.salary.model.SalCorpMbSalListModel;
import me.ywork.salary.model.SalCorpReportModel;
import me.ywork.salary.model.SalInfoDetailModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalStaffMbSalInfoModel;
import me.ywork.salary.model.SalStaffSalReportModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.service.SalInfoService;
import me.ywork.salary.service.SalPassService;
import me.ywork.salary.service.SalReportService;
import me.ywork.salary.util.ValidateUtils;
/**
 * 手机端的控制器
 * 
 */
@Controller
@RequestMapping(value = { "**/app/calsalary/mobile" })
public class SalMbController extends RestController<SalInfoService> {

	@Autowired
	private SalReportService salReportService;
	
	@Autowired
	private SalInfoService salInfoService;
	
	@Autowired
	private SalPassService salPassService;
	
	private static final Logger logger = LoggerFactory.getLogger(SalMbController.class);
	
    /**
     *  获取员工所有工资月份
     * 
     * @param request
     * @param response
     */
	@RequestMapping(value = "/getUserSalDates**" , method = RequestMethod.GET)
	public void getUserSalDates(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext = this.getCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getUserSalDates -- corpId or userId is null");
			this.writeFailJsonToClient(response);
			return;
		}
		String staffId = callContext.getUserId();
		String corpId = callContext.getCorpId();
		logger.info("getUserSalDates-- 请求获得企业：".concat(corpId).concat("手机端员工ID：").concat(staffId).concat("的所有有薪资的月份！"));
		try{
			List<SalCorpReportModel> salReportModels = salReportService.getUserSalDates(corpId , staffId);
			this.writeSuccessJsonToClient(response, salReportModels);
		}catch(Exception e){
		      logger.error("getUserSalDates -- ".concat(corpId).concat("出现异常："),e);
			  this.writeFailJsonToClient(response);
		}
	}
	 /**
	  * 获取该月公司员工工资详情
	 * 
	 * @param request
	 * @param response
	 * @param reportId 薪资报表的主键
	 */
	@RequestMapping(value = "/getUserSalDetail**" , method = RequestMethod.GET)
	public void getUserSalDetail(HttpServletRequest request, HttpServletResponse response,
			                                             @RequestParam(value = "reportId")String reportId){
		CallContext callContext = this.getCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getUserSalDetail -- corpId or userId is null");
			this.writeFailJsonToClient(response);
			return;
		}
		String staffId = callContext.getUserId();
		String corpId =callContext.getCorpId();
		logger.info("getUserSalDetail-- 请求获得企业：".concat(corpId).concat("手机端员工ID：")
				.concat(staffId).concat("薪资报表ID为："+reportId).concat("的薪资数据"));
		try{
			SalStaffMbSalInfoModel  salStaffMbSalInfo =  salReportService.getStaffMbSalInfo(corpId , staffId, reportId);
			this.writeSuccessJsonToClient(response , salStaffMbSalInfo);
		}catch(Exception e){
		      logger.error("getUserSalDetail -- ".concat(corpId).concat("出现异常："),e);
			  this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 获取手机端员工薪资详情的列表
	 * 
	 * @param request
	 * @param response
	 * @param reportId  月度新增报表的主键
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	@RequestMapping(value = "/getUserSalList**", method = RequestMethod.GET)
	public void getSalaryDatailByMonthId(HttpServletRequest request, HttpServletResponse response,
			                                                            @RequestParam(value = "reportId") String reportId,
																		@RequestParam(value = "pageNo") Integer pageNo,
																		@RequestParam(value = "pageSize") Integer pageSize,
																		@RequestParam(value ="totalCount") Long totalCount) {
		PageRequest pageable = new PageRequest(pageNo , pageSize , totalCount);
		CallContext callContext = this.getCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getUserSalList -- corpId or userId is null");
			this.writeFailJsonToClient(response);
			return;
		}
		String corpId = callContext.getCorpId();
		logger.info("getUserSalList --".concat(corpId).concat("管理员请求得到薪资报表ID为：").concat(reportId).concat("的员工薪资数据"));
		
		SalCorpReportModel salCorpReportModel =	salReportService.getSalReportByReportId(reportId);
		if(salCorpReportModel==null){
			logger.error("--getUserSalList -- salCorpReportModel is null");
			this.writeFailJsonToClient(response);
			return;
		}
		try {
			PageData<SalStaffSalReportModel> monthStaffModels = salReportService.getSalDatailByReportId(reportId,corpId,pageable);
			if(monthStaffModels==null){
				logger.error("--getUserSalList -- monthStaffModels is null");
				this.writeFailJsonToClient(response);
				return;
			}
			List<SalStaffSalReportModel> datas =monthStaffModels.getDatas();
			if(datas==null){
				logger.error("--getUserSalList -- datas is null");
				this.writeFailJsonToClient(response);
				return;
			}
			/**
			 * 
			 *  数字方面的都除以100
			 * 
			 */
			Double actualSal=null;
			Double replaceDeduct=null;
			Double shouldPaySal=null;
			Double salDeduct=null;
			for(SalStaffSalReportModel salStaffSalReportModel:datas){
				actualSal =salStaffSalReportModel.getActualSal();
				replaceDeduct=salStaffSalReportModel.getReplaceDeduct();
				shouldPaySal = salStaffSalReportModel.getShouldPaySal();
				salDeduct = salStaffSalReportModel.getSalDeduct();
				if(actualSal!=null){
				   salStaffSalReportModel.setActualSal(actualSal/100);
				}
				if(replaceDeduct!=null){
				   salStaffSalReportModel.setReplaceDeduct(replaceDeduct/100);
				}
				if(shouldPaySal!=null){
				   salStaffSalReportModel.setShouldPaySal(shouldPaySal/100l);
				}
				if(salDeduct!=null){
				   salStaffSalReportModel.setSalDeduct(salDeduct/100);
				}
			}
		   totalCount = monthStaffModels.getTotalCount();
		  Integer totalPages = monthStaffModels.getTotalPages();
		  SalCorpMbSalListModel salCorpMbSalListModel =new SalCorpMbSalListModel();
		  salCorpMbSalListModel.setCorpId(corpId);
		  salCorpMbSalListModel.setCreateDate(salCorpReportModel.getCreateDate());
		  salCorpMbSalListModel.setDatas(datas);
		  salCorpMbSalListModel.setShouldPaySal(salCorpReportModel.getShouldPaySal()/100);
		  salCorpMbSalListModel.setFileUrl(salCorpReportModel.getFileUrl());
		  salCorpMbSalListModel.setId(salCorpReportModel.getId());
		  salCorpMbSalListModel.setActualSal(salCorpReportModel.getActualPaySal());
		  salCorpMbSalListModel.setInsuranceSal(salCorpReportModel.getInsuranceSal()/100);
		  salCorpMbSalListModel.setModifiedDate(salCorpReportModel.getModifiedDate());
		  salCorpMbSalListModel.setMonthTime(salCorpReportModel.getMonthTime());
		  salCorpMbSalListModel.setStaffCost(salCorpReportModel.getStaffCost()/100);
		  salCorpMbSalListModel.setPageNo(pageNo);
		  salCorpMbSalListModel.setPageSize(pageSize);
		  salCorpMbSalListModel.setTotalCount(totalCount);
		  salCorpMbSalListModel.setTotalPages(totalPages);
			this.writeSuccessJsonToClient(response, salCorpMbSalListModel);
		} catch (Exception e) {
		      logger.error("getUserSalList -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}

	/**
	 * 获取所有月份的工资报表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getAllSalReports**", method = RequestMethod.GET)
	public void getAllSalReports(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext = this.getCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getAllSalReports -- corpId or userId is null");
			this.writeFailJsonToClient(response);
			return;
		}
		String corpId = callContext.getCorpId();
		try {
			logger.info("开始获得钉钉企业号为：".concat(corpId).concat("的所有月份的工资报表"));
			List<SalCorpReportModel> salReportModels = salReportService.getAllSalReports(corpId);
			
			/**
			 * 
			 *  对企业每月薪资涉及到数字方面的都除以100
			 * 
			 */
			Double actualPaySal=null;
			Double insuranceSal=null;
			Double shouldPaySal=null;
			Double staffPay=null;
			for(SalCorpReportModel salCorpReportModel:salReportModels){
				actualPaySal = salCorpReportModel.getActualPaySal();
				insuranceSal = salCorpReportModel.getInsuranceSal();
				shouldPaySal = salCorpReportModel.getShouldPaySal();
				staffPay = salCorpReportModel.getStaffCost();
				if(actualPaySal!=null){
			    	salCorpReportModel.setActualPaySal(actualPaySal/100);
				}
				if(insuranceSal !=null){
				   salCorpReportModel.setInsuranceSal(insuranceSal/100);
				}
				if(shouldPaySal !=null){
				   salCorpReportModel.setShouldPaySal(shouldPaySal/100);
				}
				if(staffPay != null){
					salCorpReportModel.setStaffCost(staffPay/100);
				}
			}
			if(salReportModels!=null&&!salReportModels.isEmpty()){
			   logger.info("getAllSalReports -- 钉钉号为".concat(corpId).concat("返回").concat(""+salReportModels.size()+"").concat("个月份的数据"));
			}
			this.writeSuccessJsonToClient(response, salReportModels);
		} catch (Exception e) {
		      logger.error("getAllSalReports -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 通过员工ID来获取该员工的薪资详情
	 * 
	 * @param request
	 * @param response
	 * @param dingStaffId 员工钉钉ID
	 * @param reportId  企业薪资报表的ID
	 */
	@RequestMapping(value = "/getSalInfosByStaffId**", method = RequestMethod.GET)
	public void getSalaryInfosByStaffId(HttpServletRequest request, HttpServletResponse response,
																	@RequestParam(value="reportId")String reportId) {
		CallContext callContext = this.getCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getSalInfosByStaffId -- corpId or userId is null");
			this.writeFailJsonToClient(response);
			return;
		}
		String corpId = callContext.getCorpId();
		String dingStaffId=callContext.getUserId();
		Short showTyppe=SalDetailShowType.MonthSalType.getCode();
		logger.info("getSalInfosByStaffId-- 请求获得企业：".concat(corpId).concat("手机端员工ID：")
				.concat(dingStaffId).concat("薪资报表ID为："+reportId).concat("的薪资数据"));
		try {
			SalInfoDetailModel staffSalInfoDetailModel = salInfoService.getSalInfosByStaffId(corpId ,reportId,showTyppe, dingStaffId);
			double itemValue =0.0;
			List<SalSysFieldItemModel> sysFieldItemList  = staffSalInfoDetailModel.getItemModels();
			for(SalSysFieldItemModel salSysFieldItem:sysFieldItemList){//循环将员工工资组成部分除以100
				itemValue = salSysFieldItem.getItemValue();;
				salSysFieldItem.setItemValue(itemValue/100);
			}
			staffSalInfoDetailModel.setItemModels(sysFieldItemList);
			SalStaffMbSalInfoModel  salStaffMbSalInfoModel =salReportService.getStaffMbSalInfo(corpId,dingStaffId,reportId);
			salStaffMbSalInfoModel.setDetail(sysFieldItemList);
			this.writeSuccessJsonToClient(response, salStaffMbSalInfoModel);
		} catch (Exception e) {
		      logger.error("getSalInfosByStaffId -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 判断是否设置过密码
	 * 
     * @param request
     * @param response
     */
	@RequestMapping(value = "/everUserSetPwd**" , method =RequestMethod.GET)
	public void everSetPwd(HttpServletRequest request , HttpServletResponse response){
		CallContext callContext = this.getCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--everUserSetPwd -- corpId or userId is null");
			this.writeFailJsonToClient(response);
			return;
		}
		String corpId = callContext.getCorpId();
		String userId=callContext.getUserId();
		try{
			logger.info("everUserSetPwd --".concat(corpId).concat("--用户ID：").concat(userId).concat("-- 请求判断该员工是否有设置过密码"));
			SalStaffBaseInfoModel salStaffBaseInfoModel = salPassService.everStaffsetpwd(callContext);
			this.writeSuccessJsonToClient(response, salStaffBaseInfoModel);
		}catch(Exception e){
		      logger.error("everUserSetPwd -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}


	/**
	 *密码验证
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/checkUserPwd**" , method =RequestMethod.POST)
	public void checkUserPwd(HttpServletRequest request , HttpServletResponse response){
		CallContext callContext = this.getCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--checkUserPwd -- corpId or userId is null");
			this.writeFailJsonToClient(response);
			return;
		}
		String corpId = callContext.getCorpId();
		String userId = callContext.getUserId();
		try{
			SalStaffBaseInfoModel salStaffBaseInfoModel = this.fromInputJson(request , SalStaffBaseInfoModel.class);
			if(salStaffBaseInfoModel==null){
				logger.error("checkUserPwd ".concat(corpId).concat("--salStaffBaseInfoModel is null"));
      			this.writeFailJsonToClient(response);
      			return;
			}
			
			 String password =salStaffBaseInfoModel.getStaffPass();
			 password = MD5Util.getMD5String(MD5Util.getMD5String(password));
			 
			 if(password.equals(salPassService.getUserPwd(callContext))){//发送请求得到用户密码
				 logger.info("checkUserPwd --".concat(corpId).concat("--用户ID：").concat(userId).concat("--密码验证成功！"));
				 this.writeSuccessJsonToClient(response, Boolean.TRUE);
			 }else{
				 logger.info("checkUserPwd --".concat(corpId).concat("--用户ID：").concat(userId).concat("--密码验证失败！"));
				 this.writeSuccessJsonToClient(response, Boolean.FALSE);
			 }
			 
		}catch(Exception e){
		      logger.error("checkUserPwd -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}


		/**
		 *  设置用户密码
		 * 
         * @param request
         * @param response
         */
		@RequestMapping(value = "/setUserPwd**" , method =RequestMethod.POST)
		public void setUserPwd(HttpServletRequest request , HttpServletResponse response){
			CallContext callContext = this.getCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--setUserPwd -- corpId or userId is null");
				this.writeFailJsonToClient(response);
				return;
			}
			String corpId = callContext.getCorpId();
			
			try{
				SalStaffBaseInfoModel salStaffBaseInfoModel = this.fromInputJson(request , SalStaffBaseInfoModel.class);
				String password=salStaffBaseInfoModel.getStaffPass();
			    String newPass =  MD5Util.getMD5String(MD5Util.getMD5String(password));
		        Boolean rs = salPassService.setUserPwd(callContext , newPass);//发送请求设置用户密码
		        
		        logger.info("setUserPwd -- ".concat(corpId).concat("用户ID：")
		        		.concat(callContext.getUserId()).concat("设置密码的结果是：").concat(""+rs));
		        
		        this.writeSuccessJsonToClient(response, rs);
			}catch(Exception e){
			      logger.error("setUserPwd -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}
		}
		/**
		 * 开启或关闭密码查看
		 * 
         * @param request
         * @param response
         */
		@RequestMapping(value = "/managerUserPwdState**" , method =RequestMethod.POST)
		public void managerUserPwdState(HttpServletRequest request , HttpServletResponse response){
			CallContext callContext = this.getCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--managerUserPwdState -- corpId or userId is null");
				this.writeFailJsonToClient(response);
				return;
			}
			String corpId = callContext.getCorpId();
			String userId=callContext.getUserId();
			
			try{
				SalStaffBaseInfoModel salStaffBaseInfoModel = this.fromInputJson(request , SalStaffBaseInfoModel.class);
				if(salStaffBaseInfoModel==null){
					logger.warn("managerUserPwdState --".concat(corpId)
							.concat("-- 用户ID：").concat(userId).concat("salStaffBaseInfoModel is null"));
					this.writeFailJsonToClient(response);
					return;
				}
				System.out.println("密码锁："+salStaffBaseInfoModel.getPassState());
                Boolean rs = salPassService.managerUserPwdState(callContext, salStaffBaseInfoModel.getPassState());
            	logger.warn("managerUserPwdState --".concat(corpId)
						.concat("-- 用户ID：").concat(userId).concat("更改密码锁：".concat(""+rs)));
            	
                this.writeSuccessJsonToClient(response , rs);
			}catch(Exception e){
			      logger.error("managerUserPwdState -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}
		}
		public static Boolean existCorpIdAndStaffId(CallContext callContext){
			String corpId = callContext.getCorpId();
			String staffId = callContext.getUserId();
			if(StringUtils.isBlank(corpId)){
				logger.info("existCorpIdAndStaffId --  corpId 为空");
			    return Boolean.FALSE;
			}
			if(StringUtils.isBlank(staffId)){
				logger.info("existCorpIdAndStaffId --  staffId 为空");
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
	
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

			//logger.debug("即将转向主页：{}", homeUrl);

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
				//agentid = salaryBillService.findAgentId(corpid, suiteid, appid);
				//param += "&agentid="+agentid;
			}
		}catch(Exception e2){
			e2.printStackTrace();
			logger.error("salarybill get agentid error"+corpid+","+appid+","+suiteid,e2);
		}

	
		if (StringUtils.isBlank(serverName)) {
			serverName = "https://dd.ywork.me";
		}
		return serverName + "/" + "/yw/mb/salary/index.html"
				+ (param == null ? "" : "?" + param+"&v=20170316" );
		
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
