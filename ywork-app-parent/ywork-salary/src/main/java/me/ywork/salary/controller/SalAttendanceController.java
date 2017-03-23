package me.ywork.salary.controller;

import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.redisson.cache.CacheableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.page.PageRequest;
import me.ywork.salary.enumeration.SalReturnType;
import me.ywork.salary.exception.ExcelUnNormalException;
import me.ywork.salary.exception.MonthUnNormalException;
import me.ywork.salary.exception.SalFieldRepeatedException;
import me.ywork.salary.exception.StaffNotExistException;
import me.ywork.salary.model.SalAttenExcelModel;
import me.ywork.salary.model.SalCorpAttenModel;
import me.ywork.salary.model.SalCorpDeductModel;
import me.ywork.salary.model.SalCustomizedAttenFieldModel;
import me.ywork.salary.model.SalMutiStaffInfoModel;
import me.ywork.salary.model.SalStaffAttendanceModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.model.SalUpdateMutiStaffModel;
import me.ywork.salary.service.SalAttendanceService;
import me.ywork.salary.service.SalCalcuSalService;
import me.ywork.salary.util.ValidateUtils;

/**
  * 考勤模块的控制器
 *
 **/
@Controller
@RequestMapping(value = { "**/app/calsalary/attendance" })
public class SalAttendanceController extends RestController<SalAttendanceService> {
	@Autowired
	private SalAttendanceService salAttendanceService;
	
	@Autowired
	private SalCalcuSalService salCalcuSalService;

	@Autowired
	private CacheableService cacheableService;	// 缓存机制
	
	private static final Logger logger = LoggerFactory.getLogger(SalAttendanceController.class);
	/**
	 * 得到企业的月度考勤表的分页数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getAllMonthesAttendanceData**", method = RequestMethod.GET)
	public void getAllMonthesAttendanceData(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext =  this.getPcCallContext(request);	
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getAllMonthesAttendanceData -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try {
			logger.info("getAllMonthesAttendanceData -- ".concat(corpId).concat("请求获得所有月度的考勤数据！"));
			List<SalCorpAttenModel> attendanceDatas = salAttendanceService.getAllMonthesAttendanceData(corpId);
			if(attendanceDatas!=null){
				logger.info("getAllMonthesAttendanceData -- ".concat(corpId).concat("返回".concat(""+attendanceDatas.size()+"").concat("个月度的考勤数据！")));
			}
			this.writeSuccessJsonToClient(response, attendanceDatas);
		} catch (Exception e) {
			logger.error("getAllMonthesAttendanceData -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}

	/**
	 * 根据考勤表ID来得到月度考勤表的数据
	 * 
	 * @param request
	 * @param response
	 * @param reportId  考勤报表的ID
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	@RequestMapping(value = "/getAllStaffAttendanceByReportId**", method = RequestMethod.GET)
	public void getAllStaffAttendanceByReportId(HttpServletRequest request, HttpServletResponse response,
				                                                                       @RequestParam(value = "reportId") String reportId,
				                                                                       @RequestParam(value = "pageNo") Integer pageNo,
																						@RequestParam(value = "pageSize") Integer pageSize,
																						@RequestParam(value ="totalCount") Integer totalCount) {
		PageRequest pageable = new PageRequest(pageNo , pageSize , totalCount);
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getAllStaffAttendanceByReportId -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try {
			SalAttenExcelModel attendanceDatas = salAttendanceService.getAllStaffAttendanceByReportId(corpId,reportId,pageable);
			if(attendanceDatas==null){
				logger.warn("getAllStaffAttendanceByReportId -- ".concat(corpId).concat("--返回SalAttenExcelModel为空 "));
				this.writeSuccessJsonToClient(response, new SalAttenExcelModel());
				return;
			}
			List<SalStaffAttendanceModel> attendanceData = attendanceDatas.getStaffAttens();
			if(attendanceData==null){
				logger.warn("getAllStaffAttendanceByReportId -- ".concat(corpId).concat("--返回attendanceData为空 "));
				this.writeSuccessJsonToClient(response, attendanceDatas);
				return;
			}
			/**
			 * 所有的数字除以100
			 */
			Double attendanceDays =null;//出勤天数
			Double restDays=null;//休息天数
			for(SalStaffAttendanceModel salStaffAttendanceModel:attendanceData){
				attendanceDays = salStaffAttendanceModel.getAttendanceDays();
				restDays=salStaffAttendanceModel.getRestDays();
				if(attendanceDays!=null){
			    	salStaffAttendanceModel.setAttendanceDays(attendanceDays/100);
				}
				if(restDays!=null){
					salStaffAttendanceModel.setRestDays(restDays/100);
				}
				List<SalCustomizedAttenFieldModel> attenDays =salStaffAttendanceModel.getDetails();
				Double fieldDay=0.0;
				if(attenDays!=null){
					for(SalCustomizedAttenFieldModel salCustomizedAttenFieldModel:attenDays){
						fieldDay = salCustomizedAttenFieldModel.getFieldDay();
						if(fieldDay != null){
						   salCustomizedAttenFieldModel.setFieldDay(fieldDay/100);
						}else{
							 salCustomizedAttenFieldModel.setFieldDay(0.0);
						}
					}
				}
				salStaffAttendanceModel.setDetails(attenDays);
			}
			logger.info("getAllStaffAttendanceByReportId--".concat(corpId).concat("考勤薪资报表ID为："+reportId)
					.concat("--返回").concat(""+attendanceData.size()+"").concat("条考勤数据！"));
			this.writeSuccessJsonToClient(response, attendanceDatas);
		} catch (Exception e) {
			logger.error("getAllStaffAttendanceByReportId -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}

	/**
	 * 选择多个员工和该月考勤的ID来得到这些员工的考勤详情
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getMutiStaffAttendanceDetail**", method = RequestMethod.POST)
	public void getMutiStaffAttendanceDetail(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext = this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getMutiStaffAttendanceDetail -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		SalMutiStaffInfoModel mutiStaffInfoModel = this.fromInputJson(request, SalMutiStaffInfoModel.class);
		try {
			List<SalStaffAttendanceModel> attendanceDatas = salAttendanceService.getMutiStaffAttendanceDetail(
					mutiStaffInfoModel.getStaffIds(), mutiStaffInfoModel.getAttendanceReportId());
			this.writeSuccessJsonToClient(response, attendanceDatas);
		} catch (Exception e) {
			logger.error("getMutiStaffAttendanceDetail -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}

	/**
	 * 修改某员工的考勤数据后可以保存到数据库
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateMutiStaffAttendance**", method = RequestMethod.POST)
	public void updateMutiStaffAttendance(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--updateMutiStaffAttendance -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		SalUpdateMutiStaffModel updateMutiStaffAttendanceModel = this.fromInputJson(request,
				SalUpdateMutiStaffModel.class);
		try {
			Boolean updateRs = null;
			if (updateMutiStaffAttendanceModel != null) {
				updateRs = salAttendanceService.updateMutiStaffAttendance(updateMutiStaffAttendanceModel.getStaffAttendances());
			}
			this.writeSuccessJsonToClient(response, updateRs);
		} catch (Exception e) {
			logger.error("updateMutiStaffAttendance -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}
	/**
	 * 得到企业审批字段的列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getCorpApproveFieldList**",method=RequestMethod.GET)
	public  void getCorpApproveFieldList(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getCorpApproveFieldList -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId =  callContext.getCorpId();
         try{
        	 SalUpdateMutiStaffModel  salApprove= salAttendanceService.getCorpApproveFieldList(corpId);
        	 if(salApprove != null){
        		 Double itemValue = null;
	        	 List<SalSysFieldItemModel> salFieldList = salApprove.getSaFieldList();
	        	 for(SalSysFieldItemModel salSysFieldItemModel:salFieldList){
	        		 itemValue =salSysFieldItemModel.getItemValue();
	        		 if(itemValue!=null){
	        		   salSysFieldItemModel.setItemValue(itemValue/100);
	        		 }
	        	 }
	        	 salApprove.setSaFieldList(salFieldList);
	        	 SalCorpDeductModel  salCorpDeduct =  salApprove.getSalDeduct();
	        	 if(salCorpDeduct!=null){
	        		 Double lackDeduct=null;
	        		 Double lateEarlyDeduct =null;
	        		 Double seriousLateDeduct = null;
	        		 Double stayAwayDeduct = null;
	        		 lackDeduct=salCorpDeduct.getLackDeduct();
	        		 lateEarlyDeduct =salCorpDeduct.getLateEarlyDeduct();
	        		 seriousLateDeduct = salCorpDeduct.getSeriousLateDeduct();
	        		 stayAwayDeduct = salCorpDeduct.getStayAwayDeduct();
	        		 if(lackDeduct!=null){
	        			 salCorpDeduct.setLackDeduct(lackDeduct/100);
	        		 }
	        		 if(lateEarlyDeduct!=null){
        			    salCorpDeduct.setLateEarlyDeduct(lateEarlyDeduct/100);
	        		 }
	        		 if(seriousLateDeduct!=null){
        			    salCorpDeduct.setSeriousLateDeduct(seriousLateDeduct/100);
	        		 }
	        		 if(stayAwayDeduct!=null){
        			     salCorpDeduct.setStayAwayDeduct(stayAwayDeduct/100);
	        		 }
	        	 }
	        	 salApprove.setSalDeduct(salCorpDeduct);
        	 }
        	 this.writeSuccessJsonToClient(response,salApprove);
         }catch(Exception e){
        	 logger.error("getCorpApproveFieldList -- ".concat(corpId).concat("出现异常："),e);
        	 this.writeFailJsonToClient(response);
         }
	}
	/**
	 * 更新企业审批字段的扣款
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateCorpApproveField**",method=RequestMethod.POST)
	public void updateCorpApproveField(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--updateCorpApproveField -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
	    SalUpdateMutiStaffModel salUpdateMutiStaffModel =null;
		try{
		   salUpdateMutiStaffModel = this.fromInputJson(request , SalUpdateMutiStaffModel.class);
		}
		catch(Exception e){
			logger.warn("updateCorpApproveField -- ".concat(corpId).concat("不能为非数字"));
			this.writeJsonToClient(response, SalReturnType.PARAMETER_INCORRECT.getCode(),"不能为非数字");
			return;
		}
		try{
			if(salUpdateMutiStaffModel==null){
				logger.error("updateCorpApproveField -- ".concat(corpId).concat("请求更新考勤审批的扣款数据出现异常 -- salUpdateMutiStaffModel is null"));
				this.writeFailJsonToClient(response);
				return;
			}else{
				logger.error("updateCorpApproveField -- ".concat(corpId).concat("请求更新考勤审批的扣款数据!"));
			}		
			SalCorpDeductModel salCorpDeductModel = salUpdateMutiStaffModel.getSalDeduct();
		   String validateSalCorpDeductRs = validateSalCorpDeductModel(salCorpDeductModel);
		   if(StringUtils.isNotBlank(validateSalCorpDeductRs)){
				logger.error("updateCorpApproveField -- ".concat(corpId).concat("基本考勤扣款更新出现异常 -- ".concat(validateSalCorpDeductRs)));
				this.writeJsonToClient(response, SalReturnType.PARAMETER_INCORRECT.getCode(), validateSalCorpDeductRs);
				return;
		   }
			List<SalSysFieldItemModel> salSysFieldItemModels=salUpdateMutiStaffModel.getSaFieldList();
			String validateAttenVocationRs = validateAttenVocationField(salSysFieldItemModels);
			if(StringUtils.isNotBlank(validateAttenVocationRs)){
				logger.error("updateCorpApproveField -- ".concat(corpId).concat("考勤请假字段更新出现异常 -- ".concat(validateAttenVocationRs)));
				this.writeJsonToClient(response, SalReturnType.PARAMETER_INCORRECT.getCode(), validateAttenVocationRs);
				return;
			}
			Boolean rs =salAttendanceService.updateCorpApproveField(corpId , salUpdateMutiStaffModel);
			if(rs!=null){
			    logger.error("updateCorpApproveField -- ".concat(corpId).concat("请求更新考勤审批的扣款数据的结果是：".concat(""+rs+"")));
			}
			this.writeSuccessJsonToClient(response, rs);
		}
		catch(Exception e){
			 logger.error("updateCorpApproveField -- ".concat(corpId).concat("出现异常："),e);
			 this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 根据模板去解析考勤数据
	 * 
	 * @param request
	 * @param response
	 * @param fileId  文件的ID
	 */
	@RequestMapping(value="parseAttenExcel" , method =RequestMethod.GET)
	public void parseAttenExcel(HttpServletRequest request , HttpServletResponse response,
														@RequestParam("fileId") String fileId){
			CallContext callContext = this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--parseAttenExcel -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId = callContext.getCorpId();
			fileId = URLDecoder.decode(fileId);
			try{
				logger.info("parseAttenExcel".concat(corpId).concat("-- 开始处理解析上传考勤数据的请求"));
				SalAttenExcelModel salAttenExcelModel =	salAttendanceService.parseAttenExcel(callContext,fileId);
				/**
				 * 对返回的数字都除以100
				 */
				Double attenDay = null;
				Double fieldDay=null;
				Double restDays=null;
				if(salAttenExcelModel !=null){
					List<SalStaffAttendanceModel> salStaffAttens =salAttenExcelModel.getStaffAttens();
					if(salStaffAttens==null||salStaffAttens.isEmpty()==Boolean.TRUE)
					{
						logger.warn("parseAttenExcel--".concat(corpId).concat("--上传的考勤报表为不符合规范或没有数据！"));
						this.writeJsonToClient(response, SalReturnType.EXCEL_INCORRECT.getCode(), "上传的考勤报表为不符合规范或没有数据！");
						return;
					}
					for(SalStaffAttendanceModel salStaffAttendanceModel:salStaffAttens){
						attenDay=salStaffAttendanceModel.getAttendanceDays();
						restDays=salStaffAttendanceModel.getRestDays();
						if(attenDay != null){
							salStaffAttendanceModel.setAttendanceDays(attenDay/100);
						}
						restDays=salStaffAttendanceModel.getRestDays();
						if(restDays!=null){
							salStaffAttendanceModel.setRestDays(restDays/100);
						}
						List<SalCustomizedAttenFieldModel> details =salStaffAttendanceModel.getDetails();
						if(details!=null){
							for(SalCustomizedAttenFieldModel detail:details){
								fieldDay = detail.getFieldDay();
								if(fieldDay !=null){
								   detail.setFieldDay(fieldDay/100);
								}
							}
						}
						salStaffAttendanceModel.setDetails(details);
					}
					salAttenExcelModel.setStaffAttens(salStaffAttens);
				}
				this.writeSuccessJsonToClient(response, salAttenExcelModel);
			} 
			catch(SalFieldRepeatedException e){
				logger.warn("parseAttenExcel--".concat(corpId).concat("--"+e.getMessage()), e);
				this.writeJsonToClient(response, SalReturnType.EXCEL_INCORRECT.getCode(), e.getMessage());
			}
			catch(MonthUnNormalException e){
				logger.warn("parseAttenExcel--".concat(corpId).concat("--"+e.getMessage()), e);
				this.writeJsonToClient(response, SalReturnType.EXCEL_INCORRECT.getCode(), e.getMessage());
			}
			   catch(ExcelUnNormalException e){
				   logger.warn("parseSalInfoExcel--".concat(corpId).concat("--"+e.getMessage()), e);
					this.writeJsonToClient(response, SalReturnType.EXCEL_INCORRECT.getCode(), e.getMessage());
			   }
			catch(StaffNotExistException e){
				logger.warn("parseAttenExcel--".concat(corpId).concat("--"+e.getMessage()), e);
				this.writeJsonToClient(response, SalReturnType.EXCEL_INCORRECT.getCode(), e.getMessage());
			}
			catch (Exception e) {
				 logger.error("parseAttenExcel -- ".concat(corpId).concat("出现异常："),e);
				 this.writeFailJsonToClient(response);
			}
	}
	
	/**
	 *   下一页考勤报表的数据
	 *   
	 * @param request
	 * @param response
	 * @param cacheKey  缓存考勤报表的redis KEY
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	@RequestMapping(value = { "/nextrecord**" }, method = RequestMethod.GET)
	public void nextRecord(HttpServletRequest request, HttpServletResponse response,
												@RequestParam("cacheKey") String cacheKey,
												@RequestParam("pageNo")Integer pageNo,
												@RequestParam("pageSize")Integer pageSize,
												@RequestParam("totalCount")Integer totalCount) {
		CallContext callContext = this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--nextrecord -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try{
			logger.info("--nextrecord--".concat(corpId).concat("-- 开始处理上传考勤报表中下一页考勤数据的请求"));
			SalAttenExcelModel salAttenExcelModel = (SalAttenExcelModel) cacheableService.getRawObjectFromCache(cacheKey);
			if(salAttenExcelModel==null){
				logger.error("--nextrecord -- ".concat(corpId).concat("上传考勤文件不存在!"));
				this.writeFailJsonToClient(response);
				return;
			}
			salAttenExcelModel.setPageNo(pageNo);
			salAttenExcelModel.setPageSize(pageSize);
			totalCount = salAttenExcelModel.getTotalCount();
			salAttenExcelModel.setTotalCount(totalCount);
		   if(totalCount<pageSize&&totalCount>0){//若总数小于页中存在的最大的数量,设置总的页数为1
		    	  salAttenExcelModel.setTotalPages(1);
		    }else{
		    	  salAttenExcelModel.setTotalPages(totalCount/pageSize+1);
		    }
			Integer beginNum=(pageNo-1)*pageSize;
			Integer endNum=beginNum+pageSize;
			if(endNum>totalCount){//若最大的数量大于总数，则最大的数量是总数
				endNum=totalCount;
			}
			List<SalStaffAttendanceModel>  subList=	salAttenExcelModel.getStaffAttens().subList(beginNum, endNum);//截取要指定返回的考勤的数据
			salAttenExcelModel.setStaffAttens(subList);
			if(subList!=null){
				logger.info("--nextrecord--".concat(corpId).concat("--下一页上传考勤报表 返回：").concat(""+subList.size()+"").concat("条数据！"));
			}
			
			/**
			 * 所有的数字都除以100
			 */
			Double attenDay = null;
			
			Double fieldDay=null;
			Double restDays=null;
			if(salAttenExcelModel !=null){
				List<SalStaffAttendanceModel> salStaffAttens =salAttenExcelModel.getStaffAttens();
				for(SalStaffAttendanceModel salStaffAttendanceModel:salStaffAttens){
					attenDay=salStaffAttendanceModel.getAttendanceDays();
					restDays=salStaffAttendanceModel.getRestDays();
					if(attenDay != null){
						salStaffAttendanceModel.setAttendanceDays(attenDay/100);
					}
					restDays=salStaffAttendanceModel.getRestDays();
					if(restDays!=null){
						salStaffAttendanceModel.setRestDays(restDays/100);
					}
					
					List<SalCustomizedAttenFieldModel> details =salStaffAttendanceModel.getDetails();
					if(details!=null){
						for(SalCustomizedAttenFieldModel detail:details){
							fieldDay = detail.getFieldDay();
							if(fieldDay !=null){
							   detail.setFieldDay(fieldDay/100);
							}
						}
					}
					salStaffAttendanceModel.setDetails(details);
				}
				  salAttenExcelModel.setStaffAttens(salStaffAttens);
			}		 
			this.writeSuccessJsonToClient(response, salAttenExcelModel);
		}catch(Exception e){
			 logger.error("nextrecord -- ".concat(corpId).concat("出现异常："),e);
			 this.writeFailJsonToClient(response);
		}		
	}
	/**
	 * 数据提交
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/commit**" }, method = RequestMethod.GET)
	public void commit(HttpServletRequest request, HttpServletResponse response,
			                              @RequestParam("cacheKey")String cacheKey) {
		CallContext callContext =  this.getPcCallContext(request);
		String corpId = callContext.getCorpId();
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--commit -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		try{
			 logger.error("--commit -- ".concat(corpId).concat("--开始处理提交考勤报表的请求！"));
		    Boolean commitRs = salAttendanceService.commitAttenExcel(callContext, cacheKey);
			 logger.error("--commit -- ".concat(corpId).concat("--处理提交考勤报表的请求的结果是：".concat(""+commitRs+"")));
		    this.writeSuccessJsonToClient(response, commitRs);
		}catch(Exception e){
			 logger.error("commit -- ".concat(corpId).concat("出现异常："),e);
			 this.writeFailJsonToClient(response);
		}
	}

	@Override
	protected String getHomePageUrl(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SalAttendanceService getService() {
		// TODO Auto-generated method stub
		return null;
	}
	@RequestMapping(value = { "/test**" }, method = RequestMethod.GET)
	public void test(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext =  this.getPcCallContext(request);
		String corpId = callContext.getCorpId();
		try{
		Boolean s=salCalcuSalService.calcuSalReportAutomatic(callContext.getCorpId());
		this.writeSuccessJsonToClient(response, s);
		}catch(Exception e){
			 logger.error("commit -- ".concat(corpId).concat("出现异常："),e);
			 this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 检测考勤扣款规则是否符合规范
	 * ‘
	 * @param salCorpDeductModel
	 * @return
	 */
	private String validateSalCorpDeductModel(SalCorpDeductModel salCorpDeductModel){
		Double lackDeduct=salCorpDeductModel.getLackDeduct();
		Double lateEarlyDeduct =salCorpDeductModel.getLateEarlyDeduct();
		Double stayAwayDeduct=salCorpDeductModel.getStayAwayDeduct();
		Double seriousLateDeduct=salCorpDeductModel.getSeriousLateDeduct();
		if(seriousLateDeduct==null||seriousLateDeduct<0){
			return "严重迟到扣款不能为空或为负数!";
		}
		if(lateEarlyDeduct==null||lateEarlyDeduct<0){
			return "迟到早退扣款不能为空或为负数!";
		}
		if(stayAwayDeduct==null||stayAwayDeduct<0){
			return "旷工扣款不能为空或为负数!";
		}
		if(lackDeduct==null||lackDeduct<0){
			return "缺卡扣款不能为空或为负数!";
		}	
		return "";
	}
	
	/**
	 * 检测考勤请假字段是否符合规范
	 */
	private String validateAttenVocationField(List<SalSysFieldItemModel> salSysFieldItemModels){
		if(salSysFieldItemModels==null){
			throw new IllegalArgumentException("validateAttenVocationField salSysFieldItemModels is null .");
		}
		Double itemValue=null;
		String itemName=null;
		String rs="";
		for(SalSysFieldItemModel salSysFieldItemModel:salSysFieldItemModels){
			itemName=salSysFieldItemModel.getItemName();
			itemValue= salSysFieldItemModel.getItemValue();
			if(itemValue==null||itemValue<0){
				rs=itemName.concat("不能为空或为负数！");
				break;
			}
		}
		return rs;
	}
	
}
