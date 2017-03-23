package me.ywork.salary.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.oss.OSSObjectService;
import me.ywork.page.PageData;
import me.ywork.page.PageDataImpl;
import me.ywork.page.PageRequest;
import me.ywork.salary.enumeration.SalDetailShowType;
import me.ywork.salary.enumeration.SalReturnType;
import me.ywork.salary.model.SalCorpReportModel;
import me.ywork.salary.model.SalInfoDetailModel;
import me.ywork.salary.model.SalStaffSalReportDetailModel;
import me.ywork.salary.model.SalStaffSalReportModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.model.SalUpdateMutiStaffModel;
import me.ywork.salary.service.SalInfoService;
import me.ywork.salary.service.SalReportService;
import me.ywork.salary.util.ValidateUtils;

/**
 *  薪资报表的控制器
 **/
@Controller
@RequestMapping(value = { "**/app/calsalary/salreport" })
public class SalReportController extends RestController<SalReportService> {	
	@Autowired
	private SalReportService salReportService;
	@Autowired
	private SalInfoService salInfoService;
	private static final Logger logger = LoggerFactory.getLogger(SalReportController.class);
	
	/**
	 * 获取企业所有月份的工资报表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getAllSalReports**", method = RequestMethod.GET)
	public void getAllSalReports(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getAllSalReports -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		
		try {
			logger.info("getAllSalReports -- 开始获得钉钉企业号为：".concat(corpId).concat("的所有月份的工资报表"));
			List<SalCorpReportModel> salReportModels = salReportService.getAllSalReports(corpId);//月度薪资报表的集合	
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
	 *得到所有员工的月度工资
	 * 
	 * @param request
	 * @param response
	 * @param reportId  月度薪资报表的主键
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	@RequestMapping(value = "/getUserSalList**", method = RequestMethod.GET)
	public void getUserSalList(HttpServletRequest request, HttpServletResponse response,
			                                                            @RequestParam(value = "reportId") String reportId,
																		@RequestParam(value = "pageNo") Integer pageNo,
																		@RequestParam(value = "pageSize") Integer pageSize,
																		@RequestParam(value ="totalCount") Integer totalCount) {
			PageRequest pageable = new PageRequest(pageNo , pageSize , totalCount);//构造分页器
			CallContext callContext = this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--getUserSalList -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId = callContext.getCorpId();
			 logger.info("getUserSalList -- ".concat(corpId).concat("请求得到薪资报表主键为：".concat(reportId).concat("的员工薪资数据！")));
			PageData<SalStaffSalReportModel> monthStaffModelNew =null;
			try {
				PageData<SalStaffSalReportModel> monthStaffModels = salReportService.getSalDatailByReportId(reportId,corpId,pageable);
				
				/**
				 * 
				 *  数字方面的都除以100
				 * 
				 */
				Double actualSal=null;
				Double replaceDeduct=null;
				Double shouldPaySal=null;
				Double salDeduct=null;
				
				List<SalStaffSalReportModel> datas= monthStaffModels.getDatas();
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
				    monthStaffModelNew =new PageDataImpl<>(datas,monthStaffModels.getPageable());
				}
				
				if(datas!=null){
					   logger.info("getUserSalList -- 为企业".concat(reportId).concat("得到").concat(""+datas.size()+"").concat("位员工的薪资数据！"));	
				}
				this.writeSuccessJsonToClient(response, monthStaffModelNew);
			} catch (Exception e) {
			      logger.error("getUserSalList -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}
	}

		/**
		 *  锁定该月工资表
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping(value = "/lockSalReportById**", method = RequestMethod.POST)
		public void lockSalReportById(HttpServletRequest request, HttpServletResponse response) {
			CallContext callContext = this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--lockSalReportById -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId = callContext.getCorpId();
			try {
				SalCorpReportModel salReportModel = this.fromInputJson(request , SalCorpReportModel.class);
				 logger.info("lockSalReportById -- ".concat(corpId).concat("请求锁定工资表主键为".concat(salReportModel.getId()).concat("的工资表。")));
				Boolean updateRs = salReportService.lockSalReportById(salReportModel.getId(), salReportModel.getSalReportState());
				 logger.info("lockSalReportById -- ".concat(corpId).concat("请求锁定工资表主键为".concat(salReportModel.getId()).concat("的工资表的结果是：").concat(""+updateRs+"")));
				this.writeSuccessJsonToClient(response, updateRs);
			} catch (Exception e) {
			      logger.error("lockSalReportById -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}
		}

	/**
	 * 获取员工薪资详情:通过员工ID来获取该员工的薪资详情
	 * 
	 * @param request
	 * @param response
	 * @param dingStaffId 员工钉钉ID
	 * @param reportId  企业工资报表ID
	 */
	@RequestMapping(value = "/getStaffMonthSalInfos**", method = RequestMethod.GET)
	public void getSalaryInfosByStaffId(HttpServletRequest request, HttpServletResponse response,
																	@RequestParam(value = "staffId") String dingStaffId,
																	@RequestParam(value="reportId")String reportId) {
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getStaffMonthSalInfos -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		Short showTyppe=SalDetailShowType.MonthSalType.getCode();
		logger.info("getSalaryInfosByStaffId -- 开始为企业".concat(corpId).concat("获取员工ID为：".concat(dingStaffId).concat("的月度薪资数据")));
		try {
			SalInfoDetailModel staffSalInfoDetailModel = salInfoService.getSalInfosByStaffId(corpId ,reportId,showTyppe, dingStaffId);
			
			/**
			 * 
			 *  数字方面的都除以100
			 * 
			 */
			List<SalSysFieldItemModel> sysFieldItemList = null;
			if(staffSalInfoDetailModel!=null){
				double itemValue =0.0;
				sysFieldItemList  = staffSalInfoDetailModel.getItemModels();
				for(SalSysFieldItemModel salSysFieldItem:sysFieldItemList){//循环将员工工资组成部分除以100
					itemValue = salSysFieldItem.getItemValue();;
					salSysFieldItem.setItemValue(itemValue/100);
				}
			}
			staffSalInfoDetailModel.setItemModels(sysFieldItemList);
			this.writeSuccessJsonToClient(response, staffSalInfoDetailModel.getItemModels());
		} catch (Exception e) {
		      logger.error("getStaffMonthSalInfos -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}
	/**
	 * *修改员工的浮动工资
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateSalsOnStaffes**", method = RequestMethod.POST)
	public void updateSalariesOnStaffes(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext = this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--updateSalsOnStaffes -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		logger.info("updateSalsOnStaffes -- 开始为企业".concat(corpId).concat("更新浮动工资数据"));
		SalUpdateMutiStaffModel updateMutiStaffAttendanceModel =null;
		try{
	       updateMutiStaffAttendanceModel = this.fromInputJson(request,SalUpdateMutiStaffModel.class);
		}catch(Exception e){
			logger.error("updateSalsOnStaffes ".concat(corpId).concat("--只能为数字！"));
			this.writeJsonToClient(response, SalReturnType.PARAMETER_INCORRECT.getCode(),"只能为数字！");
			return;
		}
		if(updateMutiStaffAttendanceModel == null){
		      logger.warn("updateSalsOnStaffes -- ".concat(corpId).concat("updateMutiStaffAttendanceModel is null"));
			   this.writeFailJsonToClient(response);
			   return ;
		}
		List<SalStaffSalReportDetailModel> salStaffSalReportDetailModels = updateMutiStaffAttendanceModel.getMonthStaffSalDetailModels();
		String validateSalStaffSalReportDetailModelRs=validateSalStaffSalReportDetailModels(salStaffSalReportDetailModels);
		if(StringUtils.isNotBlank(validateSalStaffSalReportDetailModelRs)){
			logger.error("updateSalsOnStaffes ".concat(corpId).concat("--存在为空或为负数的字段"));
			this.writeJsonToClient(response, SalReturnType.PARAMETER_INCORRECT.getCode(),"存在为空或为负数的字段！");
			return;
		}
		try {
			Boolean updateRs = null;
			if (updateMutiStaffAttendanceModel != null) {		
				updateRs = salReportService.updateSalsOnStaffes(corpId,salStaffSalReportDetailModels);
			}
			logger.info(corpId.concat("updateSalsOnStaffes -- 企业更新浮动数据的结果是：".concat(""+updateRs+"")));
			this.writeSuccessJsonToClient(response, updateRs);
		} catch (Exception e) {
		      logger.error("updateSalsOnStaffes -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}
	/**
	 * 下载企业的月度薪资报表
	 * 
	 * @param request
	 * @param response
	 * @param reportId 企业月度薪资报表的ID
	 */
	@RequestMapping(value = { "/download**" }, method = RequestMethod.GET)
	public void download(HttpServletRequest request,
			                                  HttpServletResponse response,
			                                 @Param("reportId")String reportId) {
		try{
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition",
					"attachment; filename=temple.xls");
			CallContext callContext= this.getPcCallContext(request);
			//获取该公司的员工信息
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--download -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId=callContext.getCorpId();
			HSSFWorkbook wb =salReportService.exportToExcel(corpId,reportId);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			wb.write(out);
			InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			String fileId = corpId+"/"+sdf.format(new Date())+".xls";
			
			String buckname = "alidsalarybill";
			
			OSSObjectService.uploadExcelFile(buckname,fileId, inputStream);
			
			String fileUrl = OSSObjectService.getFileUrl(buckname, fileId);
	
			if(inputStream!=null){
				inputStream.close();
			}
			if(wb!=null){
				wb.close();
			}
			this.writeSuccessJsonToClient(response, fileUrl);
		}catch(Exception e){
			logger.error("download", e);
			this.writeFailJsonToClient(response);
		}

	}
	/**
	 * 严重浮动款项的字段
	 * 
	 * @param salStaffSalReportDetailModels
	 * @return
	 */
   private String validateSalStaffSalReportDetailModels(List<SalStaffSalReportDetailModel>salStaffSalReportDetailModels ){
	   if(salStaffSalReportDetailModels==null){
		   throw new IllegalArgumentException("validateSalStaffSalReportDetailModels param salStaffSalReportDetailModels");
	   }
	   String rs="";
	   for(SalStaffSalReportModel salStaffSalReportModel:salStaffSalReportDetailModels){
		   Double annualBonus=salStaffSalReportModel.getAnnualBonus();
		   Double monthBonus=salStaffSalReportModel.getMonthBonus();
		   Double otherPretaxSal=salStaffSalReportModel.getOtherPretaxSal();
		   Double otherPretaxDeduct=salStaffSalReportModel.getOtherPretaxDeduct();
		   Double otherAftertaxSal=salStaffSalReportModel.getOtherAftertaxSal();
		   Double otherAftertaxDeduct=salStaffSalReportModel.getOtherAftertaxDeduct();
		   if(annualBonus==null||monthBonus==null||otherPretaxSal==null||
				   otherPretaxDeduct==null||otherAftertaxSal==null||
				   otherAftertaxDeduct==null||annualBonus<0||monthBonus<0||otherPretaxSal<0||
				   otherPretaxDeduct<0||otherAftertaxSal<0||otherAftertaxDeduct<0){
			   rs="存在为空或为负数的字段 ";
		   }	   
	   }
	   return rs;
   }
	

	@Override
	protected String getHomePageUrl(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SalReportService getService() {
		// TODO Auto-generated method stub
		return null;
	}
}
