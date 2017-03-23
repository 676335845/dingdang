package me.ywork.salary.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.redisson.cache.CacheableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONObject;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.oss.OSSObjectService;
import me.ywork.page.PageData;
import me.ywork.page.PageDataImpl;
import me.ywork.page.PageRequest;
import me.ywork.salary.enumeration.SalReturnType;
import me.ywork.salary.exception.ExcelUnNormalException;
import me.ywork.salary.exception.StaffNotExistException;
import me.ywork.salary.model.SalInfoDetailModel;
import me.ywork.salary.model.SalInfoExcelModel;
import me.ywork.salary.model.SalMutiStaffInfoModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.model.SalUpdateMutiStaffModel;
import me.ywork.salary.service.SalInfoService;
import me.ywork.salary.util.ValidateUtils;
import me.ywork.util.AESUtil;

/**
 * 员工薪资信息模块的控制器
 * 
 **/
@Controller
@RequestMapping(value = { "**/app/calsalary/salInfo" })
public class SalInfoController extends RestController<SalInfoService> {

	@Autowired
	private SalInfoService salStaffSalInfoService;
	@Autowired
	private CacheableService cacheableService;
	private static final String PC_TOKEN = "salarymanager";
	
	private static final String AES_KEY = "c5a1149f163dd7a072e235ccc2566c98";
	private static final Logger logger = LoggerFactory.getLogger(SalAttendanceController.class);

	/**
	 * 获取员工薪详情:获取所有员工的薪资详情，要分页
	 * 
	 * @param request
	 * @param response
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	@RequestMapping(value = "/getStaffSalInfos**", method = RequestMethod.GET)
	public void getStaffSalaryInfos(HttpServletRequest request, HttpServletResponse response,
															@RequestParam(value = "pageNo")Integer pageNo,
															@RequestParam(value = "pageSize") Integer pageSize,
															@RequestParam(value = "totalCount") Long totalCount) {
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getStaffSalInfos -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		  logger.info("getStaffSalInfos -- ".concat(corpId).concat("开始请求第")
				  .concat(""+pageNo+"").concat("页的基本薪资数据的列表"));
		try {
			PageRequest pageable = new PageRequest(pageNo, pageSize,totalCount);
			PageData<SalStaffBaseInfoModel> staffSalInfoModels = salStaffSalInfoService.getStaffSalInfos(corpId,pageable);			
			/**
			 * 将所有数字除以100
			 */
			if(staffSalInfoModels==null){
			   logger.error("getStaffSalInfos -- ".concat(corpId).concat("得到所有的员工薪资薪资信息Page器为空!"));
			   this.writeFailJsonToClient(response);
			   return;
			}
			List<SalStaffBaseInfoModel> staffSalInfoList =staffSalInfoModels.getDatas();
			if(staffSalInfoList!=null){
				 logger.info("getStaffSalInfos -- ".concat(corpId).concat("返回本页员工薪资总数为:".concat(""+staffSalInfoList.size()+"")));
			}else{
				 logger.info("getStaffSalInfos -- ".concat(corpId).concat("返回本页员工薪资总数为0:"));
			}
			Double shouldPaySal =null;
			for(SalStaffBaseInfoModel salStaffBaseInfoModel:staffSalInfoList){//循环将员工工资总数除以100
			   shouldPaySal = salStaffBaseInfoModel.getShouldPaySal();
				if(shouldPaySal != null){
					salStaffBaseInfoModel.setShouldPaySal(shouldPaySal/100);
				}			
			}
			PageData<SalStaffBaseInfoModel>staffSalInfoList2= new PageDataImpl<>(staffSalInfoList, staffSalInfoModels.getPageable());		
			this.writeSuccessJsonToClient(response, staffSalInfoList2);
			} catch (Exception e) {
			      logger.error("getStaffSalInfos -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}
	}

	/**
	 * 获取员工薪资详情:通过员工ID来获取该员工的薪资详情
	 * 
	 * @param request
	 * @param response
	 * @param dingStaffId 员工的钉钉ID
	 */
	@RequestMapping(value = "/getSalInfosByStaffId**", method = RequestMethod.GET)
	public void getSalaryInfosByStaffId(HttpServletRequest request, HttpServletResponse response,
																	@RequestParam(value = "staffId") String dingStaffId) {
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getSalInfosByStaffId -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try {
			SalInfoDetailModel staffSalInfoDetailModel = salStaffSalInfoService.getSalInfosByStaffId(corpId ,null,null, dingStaffId);
			double itemValue =0.0;
			List<SalSysFieldItemModel> sysFieldItemList  = staffSalInfoDetailModel.getItemModels();
			for(SalSysFieldItemModel salSysFieldItem:sysFieldItemList){//循环将员工工资组成部分除以100
				itemValue = salSysFieldItem.getItemValue();;
				salSysFieldItem.setItemValue(itemValue/100);
			}
			staffSalInfoDetailModel.setItemModels(sysFieldItemList);
			this.writeSuccessJsonToClient(response, staffSalInfoDetailModel.getItemModels());
		} catch (Exception e) {
		      logger.error("getSalInfosByStaffId -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}

	/**
	 * 获取选中人员工资详情:通过编辑可以获取多个人的薪资详情
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getMutiStaffSalInfosDetail**", method = RequestMethod.POST)
	public void getMutiStaffSalaryInfosDetail(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getMutiStaffSalInfosDetail -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		SalMutiStaffInfoModel mutiStaffInfoModel = this.fromInputJson(request, SalMutiStaffInfoModel.class);
		try {
			List<SalInfoDetailModel> staffSalInfoDetailModels = salStaffSalInfoService
					.getMutiStaffSalInfosDetail(mutiStaffInfoModel.getCorpId(), mutiStaffInfoModel.getStaffIds());
			this.writeSuccessJsonToClient(response, staffSalInfoDetailModels);
		} catch (Exception e) {
		      logger.error("getMutiStaffSalInfosDetail -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}


	
	/**
	 *   计算未设置薪资详情的人数
	 *   
	 * @param request 
	 * @param response
	 */
	@RequestMapping(value = "/calcuUnSetSalStaffNum**",method = RequestMethod.GET)
	public void calcuUnSetSalStaffNum(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext = this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--calcuUnSetSalStaffNum -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		 try{
			Integer unsetNum = salStaffSalInfoService.calcuUnSetSalStaffNum(corpId);
			this.writeSuccessJsonToClient(response , unsetNum);
		 }catch(Exception e){
		      logger.error("calcuUnSetSalStaffNum -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		 }
	}


	/**
	 * 根据模板去解析员工薪资信息数据
	 * 
	 * @param request
	 * @param response
	 * @param fileId 文件的Id
	 */
		@RequestMapping(value="parseSalInfoExcel" , method =RequestMethod.GET)
		public void parseSalInfoExcel(HttpServletRequest request , HttpServletResponse response,
				@RequestParam("fileId") String fileId){
			CallContext callContext = this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--parseSalInfoExcel -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId = callContext.getCorpId();
		    fileId = URLDecoder.decode(fileId);
		    logger.info("parseSalInfoExcel -- ".concat(corpId).concat("开始请求fileId为：").concat(fileId).concat("的员工薪资数据"));
			   try {
				   SalInfoExcelModel salInfo =   salStaffSalInfoService.parseSalInfoExcel(callContext, fileId);
				   /**
				    * 将所有的数字除以100
				    */
				   List<SalStaffBaseInfoModel> salStaffBaseInfo = null;
				   if(salInfo!=null){
					    salStaffBaseInfo =  salInfo.getSalInfos();
					   Double itemValue =null;
					   if(salStaffBaseInfo==null||salStaffBaseInfo.isEmpty()){
						   logger.warn("parseSalInfoExcel--".concat(corpId).concat("--上传的薪资表为不符合规范或没有数据！"));
							this.writeJsonToClient(response, SalReturnType.EXCEL_INCORRECT.getCode(), "上传的薪资表为不符合规范或没有数据！！");
							return;
					   }
					   for(SalStaffBaseInfoModel salStaffBaseInfoModel : salStaffBaseInfo){
						   List<SalSysFieldItemModel> salSysFields= salStaffBaseInfoModel.getSalFields();
						   if(salSysFields == null){
							   continue;
						   }
						   for(SalSysFieldItemModel salSysField:salSysFields){
							   itemValue = salSysField.getItemValue();
							   if(itemValue!=null){
								   salSysField.setItemValue(itemValue/100);
							   }
						   }
						   salStaffBaseInfoModel.setSalFields(salSysFields);
					   }
				   }
				   salInfo.setSalInfos(salStaffBaseInfo);
				  this.writeSuccessJsonToClient(response, salInfo);
				}
			   catch(StaffNotExistException e){
					logger.warn("parseSalInfoExcel--".concat(corpId).concat("--"+e.getMessage()), e);
					this.writeJsonToClient(response, SalReturnType.EXCEL_INCORRECT.getCode(), e.getMessage());
				}
			   catch(ExcelUnNormalException e){
				   logger.warn("parseSalInfoExcel--".concat(corpId).concat("--"+e.getMessage()), e);
					this.writeJsonToClient(response, SalReturnType.EXCEL_INCORRECT.getCode(), e.getMessage());
			   }
			   catch(Exception e){
				      logger.error("parseSalInfoExcel -- ".concat(corpId).concat("出现异常："),e);
					   this.writeFailJsonToClient(response);
				}
	}
		
	
		/**
		 *   下一页上传的薪资数据
		 * @param request
		 * @param response
		 * @param cacheKey
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
			CallContext callContext =  this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--nextrecord -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId = callContext.getCorpId();
			try{
				logger.info("--nextrecord--".concat(corpId).concat("-- 开始处理上传薪资数据中下一页数据的请求"));
				SalInfoExcelModel salInfoExcelModel = (SalInfoExcelModel) cacheableService.getRawObjectFromCache(cacheKey);
				if(salInfoExcelModel == null){
					logger.error("--nextrecord -- ".concat(corpId).concat("上传薪资excel文件不存在!"));
					this.writeFailJsonToClient(response);
					return;
				}
				salInfoExcelModel.setPageNo(pageNo);
				salInfoExcelModel.setPageSize(pageSize);
				totalCount = salInfoExcelModel.getTotalCount();
				salInfoExcelModel.setTotalCount(totalCount);
			   if(totalCount<pageSize&&totalCount>0){
				   salInfoExcelModel.setTotalPages(1);
			    }else{
			    	salInfoExcelModel.setTotalPages(totalCount/pageSize+1);
			    }
			   
				Integer beginNum=(pageNo-1)*pageSize;
				Integer endNum=beginNum+pageSize;
				if(endNum>totalCount){
					endNum=totalCount;
				}
				List<SalStaffBaseInfoModel>  subList=	salInfoExcelModel.getSalInfos().subList(beginNum, endNum);
				salInfoExcelModel.setSalInfos(subList);
				/**
				 * 所有的数字都除以100
				 */
				if(subList!=null){
					logger.info("--nextrecord--".concat(corpId).concat("--下一页上传薪资数据 返回：").concat(""+subList.size()+"").concat("条数据！"));
				}
				 List<SalStaffBaseInfoModel> salStaffBaseInfo = null;
				   if(salInfoExcelModel!=null){
					    salStaffBaseInfo =  salInfoExcelModel.getSalInfos();
					   Double itemValue =null;
					   for(SalStaffBaseInfoModel salStaffBaseInfoModel : salStaffBaseInfo){
						   List<SalSysFieldItemModel> salSysFields= salStaffBaseInfoModel.getSalFields();
						   if(salSysFields!=null){
							   for(SalSysFieldItemModel salSysField:salSysFields){
								   itemValue = salSysField.getItemValue();
								   if(itemValue!=null){
									   salSysField.setItemValue(itemValue/100);
								   }
							   }
						   }
						   salStaffBaseInfoModel.setSalFields(salSysFields);
					   }
				   }
				   salInfoExcelModel.setSalInfos(salStaffBaseInfo);
				this.writeSuccessJsonToClient(response, salInfoExcelModel);
			}catch(Exception e){
				   logger.error("nextrecord -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}		
		}
		/**
		 *上员工薪资信息数据提交
		 * 
		 * @param request
		 * @param response
		 * @param cacheKey  缓存企业薪资报表的标识
		 */
		@RequestMapping(value = { "/commit**" }, method = RequestMethod.GET)
		public void commit(HttpServletRequest request, HttpServletResponse response,
				                              @RequestParam("cacheKey")String cacheKey) {
			CallContext callContext = this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--commit -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId = callContext.getCorpId();
			try{
				 logger.error("--commit -- ".concat(corpId).concat("--开始处理提交薪资信息数据excel表的请求！"));
			    Boolean commitRs = salStaffSalInfoService.commitSalInfoExcel(callContext, cacheKey);
				 logger.error("--commit -- ".concat(corpId).concat("--处理提交薪资信息数据excel表的请求的结果是：".concat(""+commitRs+"")));
			    this.writeSuccessJsonToClient(response, commitRs);
			}catch(Exception e){
		      logger.error("commit -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
			}
		}
	
		/**
		 * 下载模板
		 * 
		 * @param request
		 * @param response
		 * @throws Exception
		 */
		@RequestMapping(value = { "/download**" }, method = RequestMethod.GET)
		public void download(HttpServletRequest request, HttpServletResponse response) {
			try{
				response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				response.setHeader("Content-disposition",
						"attachment; filename=temple.xls");
				CallContext callContext=this.getPcCallContext(request);
             
				//获取该公司的员工信息
				if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
					logger.error("--download -- corpId or userId is null");
					this.writePermissionDeniedJsonToClient(response,null);
					return;
				}
				String companyId=callContext.getCorpId();
				HSSFWorkbook wb =salStaffSalInfoService.exportToExcel(companyId);
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				wb.write(out);
				InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				String fileId = companyId+"/"+sdf.format(new Date())+".xls";
				
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
		
		public String getUserCookie(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String cookieToken = "";
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (PC_TOKEN.equals(cookie.getName())) {
						cookieToken = AESUtil.decrypt(cookie.getValue(), AES_KEY);//(,AES_KEY);
						break;
					}
				}
			}
			return cookieToken;
		}
}
