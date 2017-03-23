package me.ywork.salary.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.salary.model.SalBaseStateModel;
import me.ywork.salary.service.SalBaseService;
import me.ywork.salary.service.SalPassService;
import me.ywork.salary.util.ValidateUtils;

/**
 *  企业员工的基本信息的控制层
 *  
 */
@Controller
@RequestMapping(value = { "**/app/calsalary/staffInfo" })
public class SalBaseInfoController  extends RestController<SalPassService>{

	@Autowired
	private SalBaseService salBaseService;
	
	private static final Logger logger = LoggerFactory.getLogger(SalAttendanceController.class);

	/**
	 * 得到企业员工的基本信息
	 * 
	 * @param request
	 * @param response
	 */
  @RequestMapping(value="/base**",method = RequestMethod.GET)
	public void getStaffBaseInfo(HttpServletRequest request,HttpServletResponse response){
	  CallContext callContext =  this.getPcCallContext(request);
		if(ValidateUtils.existCorpIdAndStaffId(callContext)==Boolean.FALSE){
			logger.error("--base -- corpId or userId is null");
			this.writePermissionDeniedJsonToClient(response,null);
			return;
		}
		  String corpId=callContext.getCorpId();
	  try{
	      SalBaseStateModel salBaseStateModel =  salBaseService.getCorpBaseInfo(corpId);
	      this.writeSuccessJsonToClient(response, salBaseStateModel);
	  }catch(Exception e){
		      logger.error("base -- ".concat(corpId).concat("出现异常："),e);
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
