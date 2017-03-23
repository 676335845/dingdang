package me.ywork.salary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.salary.enumeration.SalStateType;
import me.ywork.salary.model.SalCorpInfoModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalUpdateMutiStaffModel;
import me.ywork.salary.service.SalPassService;
import me.ywork.salary.util.ValidateUtils;
				
				
/**
 * 
* 密码管理的控制器
 *
 **/
@Controller
@RequestMapping(value = {  "**/app/calsalary/salPass" })
public class SalPassController  extends RestController<SalPassService> {
	@Autowired
	private SalPassService salPassService;
	private static final Logger logger = LoggerFactory.getLogger(SalMbController.class);
		 /**
		 * 得到企业密码锁开启的状态
		 *
		 * @param request
		 * @param response
		 */
		@RequestMapping(value = "/getCorpPassOpenStatus**" , method = RequestMethod.GET)
		 public void getCorpPasswordOpenStatus(HttpServletRequest request , HttpServletResponse response){
			CallContext callContext =  this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--getCorpPassOpenStatus -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId = callContext.getCorpId();
				try{
					logger.info("getCorpPassOpenStatus--".concat(corpId).concat("--请求得到企业密码锁开启的状态"));
				     Short openStatus = salPassService.getCorpPassOpenStatus(corpId);
				     logger.info("getCorpPassOpenStatus--".concat(corpId).concat("--企业的密码锁的状态为").concat(""+openStatus+""));
					 this.writeSuccessJsonToClient(response, openStatus);
				}catch(Exception e){
				      logger.error("updateMutiStaffInfos -- ".concat(corpId).concat("出现异常："),e);
					   this.writeFailJsonToClient(response);
				}
		}

		/**
		 * 更新企业密码锁的状态
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping(value = "/updateCorpPassStatus**",method=RequestMethod.POST)
		public void updateCorpPassStatus(HttpServletRequest request , HttpServletResponse response){
			CallContext callContext = this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--updateCorpPassStatus -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId =callContext.getCorpId();
			try{
				SalCorpInfoModel salCorpInfoModel = this.fromInputJson(request , SalCorpInfoModel.class);
				if(salCorpInfoModel!=null){
				   salCorpInfoModel.setCorpId(corpId);
				}
				Short passState=salCorpInfoModel.getPassState();
				String openState=null;
				if(passState ==SalStateType.OPEN.getCode()){
					openState="开启";
				}else{
					openState="关闭";
				}
				logger.info("updateCorpPassStatus--".concat(corpId).concat("--请求").concat(openState).concat("企业密码锁"));
				Boolean rs = salPassService.updateCorpPassStatus(salCorpInfoModel);
				logger.info("updateCorpPassStatus--".concat(corpId).concat("--更新企业密码锁".concat(""+rs)));
				  this.writeSuccessJsonToClient(response , rs);
			}catch(Exception e){
			      logger.error("updateCorpPassStatus -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}
		}

		/**
		 * 根据员工的信息去模糊查询员工的信息
		 * 
		 * @param request
		 * @param response
		 * @param keyword   搜索的关键字
		 */
		@RequestMapping(value = "/getStaffInfoByKeyword**" , method = RequestMethod.GET)
		 public void  getStaffInfoByKeyword(HttpServletRequest request , HttpServletResponse response,
																		 @RequestParam(value = "keyword")String keyword){
			CallContext callContext =  this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--getStaffInfoByKeyword -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId = callContext.getCorpId();
				try{
					   List<SalStaffBaseInfoModel> staffBaseInfos =  salPassService.getStaffInfoByKeyword(corpId,keyword);
					  this.writeSuccessJsonToClient(response, staffBaseInfos);
					}catch(Exception e){
					      logger.error("getStaffInfoByKeyword -- ".concat(corpId).concat("出现异常："),e);
						   this.writeFailJsonToClient(response);
					}
		}

		/**
		 *  对员工的密码进行重置
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping(value = "/resetStaffPass**" , method = RequestMethod.POST)
		public void  resetStaffPassword(HttpServletRequest request , HttpServletResponse response){
			CallContext callContext =  this.getPcCallContext(request);
			if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
				logger.error("--resetStaffPass -- corpId or userId is null");
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			String corpId =callContext.getCorpId();
			try{
				logger.info("resetStaffPass --".concat(corpId).concat("请求对选中的员工的密码进行重置!"));
				SalUpdateMutiStaffModel salStaffs = this.fromInputJson(request, SalUpdateMutiStaffModel.class);
				if(salStaffs==null){
					    logger.error("resetStaffPass ".concat(corpId).concat("--salStaffs is null"));
		      			this.writeFailJsonToClient(response);
		      			return;
				}
				  Boolean resetRs =  salPassService.resetStaffPass(corpId ,salStaffs.getStaffBaseInfoModels());
				  logger.info("resetStaffPass --".concat(corpId).concat("请求重置员工的密码的结果是：").concat(""+resetRs));
				  this.writeSuccessJsonToClient(response, resetRs);
			}catch(Exception e){
			      logger.error("resetStaffPass -- ".concat(corpId).concat("出现异常："),e);
				   this.writeFailJsonToClient(response);
			}
		}

		

		@Override
		protected String getHomePageUrl(String param) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected SalPassService getService() {
			// TODO Auto-generated method stub
			return null;
		}
}
