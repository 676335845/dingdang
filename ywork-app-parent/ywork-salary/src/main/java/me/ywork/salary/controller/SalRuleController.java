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

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.salary.enumeration.SalReturnType;
import me.ywork.salary.enumeration.SalRuleType;
import me.ywork.salary.enumeration.SalStaffDeptType;
import me.ywork.salary.model.SalCorpBaseSalRuleModel;
import me.ywork.salary.model.SalCorpWhpRuleModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalUpdateMutiStaffModel;
import me.ywork.salary.model.SalUpdateStaffesModel;
import me.ywork.salary.service.SalRuleService;
import me.ywork.salary.util.ValidateUtils;

/**
 * 薪资规则的控制器
 * 
 */

@Controller
@RequestMapping("**/app/calsalary/salrule")
public class SalRuleController extends RestController<SalRuleService> {
	@Autowired
	private SalRuleService salRuleService;;
	private static final Logger logger = LoggerFactory.getLogger(SalRuleController.class);

	/**
	 * 得到企业所有的薪资规则
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getAllSalRules**", method = RequestMethod.GET)
	public void getAllSalRules(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getAllSalRules -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try {
			logger.info("getAllSalRules --".concat(corpId).concat("--开始请求得到所有的基本薪资规则列表"));
			List<SalCorpBaseSalRuleModel> salRuleModels = salRuleService.getAllSalRules(corpId);
			Double calSalDays = null;
			/**
			 * 将所有的数字除以100
			 */
		   for(SalCorpBaseSalRuleModel salCorpBaseSalRuleModel:salRuleModels){
			   calSalDays = salCorpBaseSalRuleModel.getCalSalDays();
			  if(calSalDays!=null){
			   salCorpBaseSalRuleModel.setCalSalDays(calSalDays/100);
			  }
		   }
		   if(salRuleModels!=null){
			   logger.info("getAllSalRules --".concat(corpId).concat("--返回").concat(""+salRuleModels.size()+"条基本薪资规则！"));
		   }else{
			   logger.info("getAllSalRules --".concat(corpId).concat("--返回0条基本薪资规则！"));
		   }
			this.writeSuccessJsonToClient(response, salRuleModels);
		  } catch (Exception e) {
		       logger.error("getAllSalRules -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}

	/**
	 * 根据薪资规则主键得到企业的薪资规则明细
	 * 
	 * @param request
	 * @param response
	 * @param ruleId  企业薪资规则表的主键
	 */
	@RequestMapping(value = "/getSalRuleByRuleId**", method = RequestMethod.GET)
	public void getSalRuleByRuleId(HttpServletRequest request, HttpServletResponse response,
															@RequestParam(value = "ruleId") String ruleId) {
		CallContext callContext = this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getSalRuleByRuleId -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try {
			logger.info("getSalRuleByRuleId".concat(corpId).concat("-- 开始请求得到ruleId--".concat(ruleId).concat("的基本薪资规则详情")));
			SalCorpBaseSalRuleModel salRuleDetailModel = salRuleService.getSalRuleByRuleId(corpId , ruleId);
			/**
			 * 所有的数字都除以100
			 */
			if(salRuleDetailModel!=null){//如果企业的基本薪资规则不为空
					Double calSalDays = salRuleDetailModel.getCalSalDays();
			  	   if(calSalDays!=null){
					  salRuleDetailModel.setCalSalDays(calSalDays/100);
				   }
			}
			if(salRuleDetailModel==null){
			    logger.warn("getSalRuleByRuleId".concat(corpId).concat("--得到0条数据"));
			}
			this.writeSuccessJsonToClient(response, salRuleDetailModel);
		} catch (Exception e) {
		      logger.error("getSalRuleByRuleId -- ".concat(corpId).concat("出现异常："),e);
			   this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 根据企业标识和的薪资规则的类型来获得相应的人员信息
	 * 
	 * @param request
	 * @param response
	 * @param ruleId 企业薪资规则表的主键
	 */
	@RequestMapping(value = "/getStafiesByCRI**", method = RequestMethod.GET)
	public void getStafiesByCRI(HttpServletRequest request, HttpServletResponse response,
			                                                      @RequestParam(value="ruleId")String ruleId){
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getStafiesByCRI -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try{
			logger.info("getStafiesByCRI -- ".concat(corpId).concat("--请求获得选择基本薪资规则为：".concat(ruleId).concat("的所有人员")));
			List<SalStaffBaseInfoModel> staffBaseInfoModels =salRuleService.getStaffAddress(corpId,ruleId,SalRuleType.BSRULE.getCode());
			if(staffBaseInfoModels==null){
				logger.info("getStafiesByCRI -- ".concat(corpId).concat("--没有选择任何人员选择基本薪资规则!"));
			}
			this.writeSuccessJsonToClient(response, staffBaseInfoModels);
		}catch(Exception e){
			logger.error("getStafiesByCRI -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 根据企业标识和的薪资规则的类型来获得相应的人员信息
	 * 
	 * @param request
	 * @param response
	 * @param ruleId 企业薪资规则表的主键
	 */
	@RequestMapping(value = "/getStafiesByWhpRule**", method = RequestMethod.GET)
	public void getStafiesByWhpRule(HttpServletRequest request, HttpServletResponse response,
																			@RequestParam(value ="ruleType") Short whpRuleType){
		CallContext callContext = this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getStafiesByWhpRule -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try{
			if(whpRuleType==SalRuleType.SOCIAL.getCode()){
			    logger.info("getStafiesByWhpRule -- ".concat(corpId).concat("--请求获得选择社保公积金的所有人员"));
			}else if(whpRuleType==SalRuleType.PERSONALTAX.getCode()){
			    logger.info("getStafiesByWhpRule -- ".concat(corpId).concat("--请求获得选择 个人所得税的所有人员"));
			}
			List<SalStaffBaseInfoModel> staffBaseInfoModels = salRuleService.getStaffAddress(corpId , null ,whpRuleType);
			if(staffBaseInfoModels==null){
				if(whpRuleType==SalRuleType.SOCIAL.getCode()){
				     logger.info("getStafiesByWhpRule -- ".concat(corpId).concat("--没有任何人员选择社保和公积金!"));
				}else if(whpRuleType==SalRuleType.PERSONALTAX.getCode()){
					 logger.info("getStafiesByWhpRule -- ".concat(corpId).concat("--没有任何人员选择个人所得税!"));
				}
			}
			this.writeSuccessJsonToClient(response, staffBaseInfoModels);
		}catch(Exception e){
			logger.error("getStafiesByWhpRule -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 *   更新企业薪资规则下的人员
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateSalRuleStafies**", method = RequestMethod.POST)
	public void updateSalRuleStafies(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext= this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--updateSalRuleStafies -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try{
			    SalUpdateStaffesModel  salUpdateStaffesModel= this.fromInputJson(request , SalUpdateStaffesModel.class);
			    if(salUpdateStaffesModel.getRuleType() == SalRuleType.SOCIAL.getCode()){
			    	logger.info("updateSalRuleStafies --".concat(corpId).concat("--请求更新选择社保公积金的所有人员"));
			    }else if(salUpdateStaffesModel.getRuleType() == SalRuleType.PERSONALTAX.getCode()){
			    	logger.info("updateSalRuleStafies --".concat(corpId).concat("--请求更新选择个人所得税的所有人员"));
			    }
			    String cbRuleId=salUpdateStaffesModel.getCorpBaseRuleId();
			    if(StringUtils.isNotBlank(cbRuleId)){
			    	logger.info("updateSalRuleStafies --".concat(corpId).concat("--请求更新选择基本薪资规则").concat(cbRuleId).concat("的所有人员"));
			    }
				Boolean updateSalRuleRs = salRuleService.updateSalRuleStaffies(salUpdateStaffesModel,corpId);
				this.writeSuccessJsonToClient(response, updateSalRuleRs);
		}catch(Exception e){
			logger.error("updateSalRuleStafies -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 *   删除企业员工的薪资规则
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/deleteStaffSalRule**",method=RequestMethod.POST)
	public void deleteStaffBaseSalRule(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--deleteStaffSalRule -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try{
			SalStaffBaseInfoModel salStaffBaseInfoModel = this.fromInputJson(request , SalStaffBaseInfoModel.class);
			if(salStaffBaseInfoModel==null){
				this.writeFailJsonToClient(response);
				logger.info("deleteStaffSalRule --".concat(corpId).concat("-salStaffBaseInfoModel is null"));
				return;
			}
			String typeName=salStaffBaseInfoModel.getType()==SalStaffDeptType.DEPT.getCode()?"部门":"人员";
			String handleId=salStaffBaseInfoModel.getId();
			if(salStaffBaseInfoModel.getSalRuleHandleType() == SalRuleType.BSRULE.getCode()){		
				logger.info("deleteStaffSalRule --".concat(corpId).concat("--发送请求删除基本规则下"
						.concat(typeName).concat("的Id--".concat(handleId).concat("人员"))));
			}else if(salStaffBaseInfoModel.getSalRuleHandleType() == SalRuleType.PERSONALTAX.getCode()){
				logger.info("deleteStaffSalRule --".concat(corpId).concat("--发送请求删除个人所得税下"
						.concat(typeName).concat("的Id--".concat(handleId).concat("人员"))));
			}else if(salStaffBaseInfoModel.getSalRuleHandleType() == SalRuleType.SOCIAL.getCode()){
				logger.info("deleteStaffSalRule --".concat(corpId).concat("--发送请求删除社保公积金下"
						.concat(typeName).concat("的Id--".concat(handleId).concat("人员"))));
			}
			Boolean rs = salRuleService.deleteStaffSalRule(corpId,salStaffBaseInfoModel);
			this.writeSuccessJsonToClient(response, rs);
		}catch(Exception e){
			logger.error("deleteStaffSalRule -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}	

	/**
	 * 更新企业的薪资规则
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateBaseSalRule**", method = RequestMethod.POST)
	public void updateSalRule(HttpServletRequest request, HttpServletResponse response) {
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--updateBaseSalRule -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		 SalCorpBaseSalRuleModel salRuleDetailModel=null;
		try{
		      salRuleDetailModel=this.fromInputJson(request, SalCorpBaseSalRuleModel.class);
		}catch(Exception e){
			logger.error("updateBaseSalRule -- ".concat(corpId).concat("输入了非数字！"),e);
			this.writeJsonToClient(response, SalReturnType.PARAMETER_INCORRECT.getCode(),"输入了非数字！");
			return;
		}
		try {			
			 if(salRuleDetailModel==null){
				 logger.error("updateBaseSalRule ".concat(corpId).concat("--salRuleDetailModel is null"));
      			this.writeFailJsonToClient(response);
      			return; 
			 }
			 if(salRuleDetailModel.getCalSalDays()==null||salRuleDetailModel.getCalSalDays()<0||salRuleDetailModel.getCalSalDays()>31){
			    logger.error("updateBaseSalRule ".concat(corpId).concat("--calSalDays is null or 为负数或超过31天"));
      			this.writeJsonToClient(response,SalReturnType.PARAMETER_INCORRECT.getCode(),"计薪天数不能输入非数字且不能小于0大于31");
      			return; 
			 }
			  Boolean salRuleUpdateRs = salRuleService.updateSalRule(corpId, salRuleDetailModel);
			  logger.info("updateBaseSalRule--".concat(corpId).concat("--更新企业基本薪资规则的结果是：").concat(""+salRuleUpdateRs+""));
			this.writeSuccessJsonToClient(response, salRuleUpdateRs);
		}
		catch (Exception e) {
			logger.error("updateBaseSalRule -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 *  得到企业社保公积金及个人所得税的规则列表
	 *  
	 * @param request
	 * @param response
	 * @param corpId 企业标识
	 */
	@RequestMapping(value = "/getCwhpRuleList**",method=RequestMethod.GET)
	public void  getCwhpRuleList(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext = this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--getCwhpRuleList -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		try{
			logger.info("getCwhpRuleList --".concat(corpId).concat("--请求获得社保公积金的列表"));
			List<SalCorpWhpRuleModel> salCwhpRuleModels = salRuleService.getCwhpRuleList(corpId);
			/**
			 * 如果是数字则要除以100
			 */
			Double baseHigh =null;
			Double baseLow =null;
			Double corpPercent = null;
			Double personalPercent = null;
			for(SalCorpWhpRuleModel salCorpWhpRule:salCwhpRuleModels){
				baseHigh = salCorpWhpRule.getBaseHigh();
				baseLow = salCorpWhpRule.getBaseLow();
				corpPercent = salCorpWhpRule.getCorpPercent();
				personalPercent = salCorpWhpRule.getPersonalPercent();
				if(baseHigh!=null){
					salCorpWhpRule.setBaseHigh(baseHigh/100);
				}
				if(baseLow!=null){
					salCorpWhpRule.setBaseLow(baseLow/100);
				}
				if(corpPercent!=null){
					salCorpWhpRule.setCorpPercent(corpPercent/100);
				}
				if(personalPercent!=null){
					salCorpWhpRule.setPersonalPercent(personalPercent/100);
				}
			}
			if(salCwhpRuleModels!=null){
				logger.info("getCwhpRuleList --".concat(corpId).concat("--返回社保公积金的").concat(""+salCwhpRuleModels.size()+"条数据！"));
			}else{
				logger.info("getCwhpRuleList --".concat(corpId).concat("--返回社保公积金的0条数据！"));
			}			
			this.writeSuccessJsonToClient(response, salCwhpRuleModels);
		}catch(Exception e){
			logger.error("getCwhpRuleList -- ".concat(corpId).concat("出现异常："),e);
			this.writeFailJsonToClient(response);
		}
	}	
	
	/**
	 *  更新企业代缴代扣规则
	 *  
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateCwhpRuleList**" , method=RequestMethod.POST)
	public void updateCwhpRuleList(HttpServletRequest request, HttpServletResponse response){
		CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--updateCwhpRuleList -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		String corpId = callContext.getCorpId();
		
		   logger.info("updateCwhpRuleList--".concat(corpId).concat("--开始请求更新企业代缴代扣的薪资规则！"));
		   SalUpdateMutiStaffModel salUpdateMutiStaffModel =null;
		   try{
    		     salUpdateMutiStaffModel = this.fromInputJson(request, SalUpdateMutiStaffModel.class);
		   }catch(Exception e){
    			logger.error("updateCwhpRuleList ".concat(corpId).concat("--只能为数字！"));
    			this.writeJsonToClient(response, SalReturnType.PARAMETER_INCORRECT.getCode(),"只能为数字！");
    			return;
		   }
         	try{	
         		if(salUpdateMutiStaffModel==null){
         			logger.error("updateCwhpRuleList ".concat(corpId).concat("--salUpdateMutiStaffModel is null"));
         			this.writeFailJsonToClient(response);
         			return;
         		}
         		List<SalCorpWhpRuleModel>  salCwhpRuleModels = salUpdateMutiStaffModel.getSalCwhpRuleModels();
         		if(salCwhpRuleModels==null){
         			logger.error("updateCwhpRuleList ".concat(corpId).concat("--salCwhpRuleModels is null"));
         			this.writeFailJsonToClient(response);
         			return;
         		}
         		String validateCorpCwhpRuleRs =invalidateCorpCwhpRule(salCwhpRuleModels);
         		if(StringUtils.isNotBlank(validateCorpCwhpRuleRs)){
         			logger.error("updateCwhpRuleList ".concat(corpId).concat(validateCorpCwhpRuleRs));
         			this.writeJsonToClient(response, SalReturnType.PARAMETER_INCORRECT.getCode(), validateCorpCwhpRuleRs);
         			return;
         		}
         		Boolean rs =salRuleService.updateCwhpRuleList(corpId,salCwhpRuleModels);
         	   logger.info("updateCwhpRuleList--".concat(corpId).concat("--更新企业代缴代扣的薪资规则的结果是：").concat(""+rs+""));
         		this.writeSuccessJsonToClient(response, rs);
			}catch(Exception e){
			      logger.error("updateCwhpRuleList -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}
	}
		private String invalidateCorpCwhpRule(List<SalCorpWhpRuleModel> salCwhpRuleModels ){
			if(salCwhpRuleModels==null){
				throw new IllegalArgumentException("invalidateCorpCwhpRule -- salCwhpRuleModels is null ");
			}
			String rs="";
			 Double baseLow =null;
			 Double baseHigh=null;
			 Double corpPercent=null;
			 Double personalPercent=null;
			for(SalCorpWhpRuleModel salCorpWhpRuleModel:salCwhpRuleModels){
				baseLow=salCorpWhpRuleModel.getBaseLow();
				baseHigh=salCorpWhpRuleModel.getBaseHigh();
				corpPercent=salCorpWhpRuleModel.getCorpPercent();
				personalPercent=salCorpWhpRuleModel.getPersonalPercent();
				if(baseLow!=null&&baseHigh!=null&&corpPercent!=null&&personalPercent!=null){
					if(baseLow<0||baseHigh<0||corpPercent<0||personalPercent<0){
					   rs="存在为负数的数字！";
					   break;
					}
				}
				if(baseLow!=null&&baseHigh!=null){
					if(baseHigh<baseLow){
                        rs="存在基数上限大于基数下限的情况！";
                        break;
					}
				}
				if(corpPercent==null||personalPercent==null){
                      rs="存在公司比例或个人比例为空的情况！";
                      break;
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
		protected SalRuleService getService() {
			// TODO Auto-generated method stub
			return null;
		}
}
