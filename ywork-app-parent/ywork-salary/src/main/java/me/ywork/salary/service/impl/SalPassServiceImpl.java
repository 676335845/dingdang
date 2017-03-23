package me.ywork.salary.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

import me.ywork.context.CallContext;
import me.ywork.salary.enumeration.SalCorpPassStateType;
import me.ywork.salary.enumeration.SalStaffDeptType;
import me.ywork.salary.model.SalCorpInfoModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.repository.SalPassRepository;
import me.ywork.salary.repository.SalStaffBaseInfoRepository;
import me.ywork.salary.service.SalBaseService;
import me.ywork.salary.service.SalPassService;

/**
 * Created by xiaobai on 2017/1/11.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalPassServiceImpl implements SalPassService {
	@Autowired
	private SalPassRepository salPassRepository;
	@Autowired
	private SalStaffBaseInfoRepository salStaffBaseInfoRepository;
	@Autowired
	private SalBaseService salBaseService;
	
	private static final Logger logger = LoggerFactory.getLogger(SalPassServiceImpl.class);
	
	@Override
	public Short getCorpPassOpenStatus(String corpId) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getCorpPassOpenStatus param corpId is null or empty");
		}
		Short corpPassOpenStatus = salPassRepository.getCorpPasswordOpenStatus(corpId);
		if(corpPassOpenStatus == null){//如果为null，说明企业的业务表没有创立
			 if(salBaseService.initCorpBaseInfo(corpId)==Boolean.TRUE){
			      corpPassOpenStatus = salPassRepository.getCorpPasswordOpenStatus(corpId);
			 }
		}
		return corpPassOpenStatus;
	}

	@Override
	public List<SalStaffBaseInfoModel> getStaffInfoByKeyword(String corpId, String keyword) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getStaffInfoByKeyword param corpId is null or empty.");
		}
		if (StringUtils.isBlank(keyword)) {
			throw new IllegalArgumentException("getStaffInfoByKeyword param keyword is null or empty.");
		}
		List<SalStaffBaseInfoModel> staffBaseInfoModels = null;
		staffBaseInfoModels = salPassRepository.getStaffInfoByKeyword(corpId, keyword);

		return staffBaseInfoModels;
	}

	@Override
	public Boolean resetStaffPass(String corpId, List<SalStaffBaseInfoModel> salStaffBaseInfoModel) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("resetStaffPass param corpId is null or empty.");
		}
		if(salStaffBaseInfoModel == null){
			throw new IllegalArgumentException("resetStaffPass param salStaffBaseInfoModel is null.");
		}
		String id=null;
		Date nowDate = new Date();
		for(SalStaffBaseInfoModel model:salStaffBaseInfoModel){
			id = model.getId();
			if(model.getType() == SalStaffDeptType.DEPT.getCode()){//如果ID的类型是部门
				 //找到部门下的所有子部门
				List<String> deptIds = salStaffBaseInfoRepository.getDeptInfoByParentId(corpId, id);
				//重置部门下的所有人员的密码
				for(String deptId:deptIds){
					salPassRepository.resetDeptPassword(corpId, deptId, nowDate);
				}
			}
		  else if(model.getType() == SalStaffDeptType.STAFF.getCode()){//如果ID的类型是员工
			  //重置该员工的密码
			  Integer resetRs = salPassRepository.resetStaffPassword(corpId , model , nowDate);
		  }
		}

		return Boolean.TRUE;
	}
	
	@Override
	public Boolean updateCorpPassStatus(SalCorpInfoModel salCorpInfoModel) {
		if (salCorpInfoModel == null) {
			throw new IllegalArgumentException("updateCorpPassStatus param salCorpInfoModel is null or empty.");
		}
		Integer updateRs =null;
		salCorpInfoModel.setModifiedDate(new Date());
	    updateRs = salPassRepository.updateCorpPassStatus(salCorpInfoModel);

		return updateRs == null?Boolean.FALSE:updateRs>0;
	}

	@Override
	public SalStaffBaseInfoModel everStaffsetpwd(CallContext  callContext) {
		if(callContext==null){
			throw new IllegalArgumentException("everStaffsetpwd --param callContext is null");
		}
		String corpId = callContext.getCorpId();//得到企业的钉钉ID
		String userId =callContext.getUserId();//得到用户的钉钉ID
		
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("everStaffsetpwd param corpId is null or empty.");
		}
		if(StringUtils.isBlank(userId)){
			throw new IllegalArgumentException("everStaffsetpwd param dingStaffId is null or empty.");
		}
		Short corpPassOpenStatus = salPassRepository.getCorpPasswordOpenStatus(corpId);//得到企业密码锁开启的状态
		if(corpPassOpenStatus == null){
			logger.info("everStaffsetpwd --".concat(corpId).concat("--初始化企业的信息"));
			salBaseService.initCorpBaseInfo(corpId);			
		}
		SalStaffBaseInfoModel salStaffBaseInfoModel = salStaffBaseInfoRepository.getStaffBaseInfo(corpId, userId);
		if(salStaffBaseInfoModel==null){
			salBaseService.synOrgStaffToApplication(corpId, userId);
		}
		if(salStaffBaseInfoModel == null){//若员工的信息不存在，则要拉取该企业的员工到应用下
			if(salBaseService.synOrgStaffToApplication(corpId, userId)==Boolean.TRUE){
				salStaffBaseInfoModel = salStaffBaseInfoRepository.getStaffBaseInfo(corpId, userId);
			}
		}
		
		salStaffBaseInfoModel.setStaffName(callContext.getUserName());
		salStaffBaseInfoModel.setDeptName(salBaseService.getStaffAllDeptName(corpId, userId));
		salStaffBaseInfoModel.setHeadUrl(callContext.getAvatar());
		salStaffBaseInfoModel.setHavePwd(salStaffBaseInfoModel.getStaffPass() != null);
		
		if(SalCorpPassStateType.OPEN.getCode()==corpPassOpenStatus){//如果企业的密码锁已经开启
			salStaffBaseInfoModel.setNeedPwd(salStaffBaseInfoModel.getPassState() ==null ?Boolean.TRUE:salStaffBaseInfoModel.getPassState()>0);
		}else{
			salStaffBaseInfoModel.setNeedPwd(Boolean.FALSE);
		}

		salStaffBaseInfoModel.setIsAdmin(callContext.isAdmin());
		Boolean sysPassState = this.getCorpPassOpenStatus(corpId) >0;
		salStaffBaseInfoModel.setSysPassState(sysPassState);
		
		return salStaffBaseInfoModel;
	}

	@Override
	public String getUserPwd(CallContext callContext) {
		if(callContext==null){
			throw new IllegalArgumentException("getUserPwd -- callContext is null");
		}
		String corpId = callContext.getCorpId();
		String userId =callContext.getUserId();
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("getUserPwd param corpId is null or empty.");
		}
		if(StringUtils.isBlank(userId)){
			throw new IllegalArgumentException("getUserPwd param dingStaffId is null or empty.");
		}
		String password = salPassRepository.getUserPwd(corpId, userId);
		return password;
	}
	
	

	@Override
	public Boolean setUserPwd(CallContext callContext , String password) {
		if(callContext==null){
			throw new IllegalArgumentException("setUserPwd -- callContext is null");
		}
		if(StringUtils.isBlank(password)){
			throw new  IllegalArgumentException("setUserPwd param password is null or empty.");
		}
		
		String corpId = callContext.getCorpId();
		String userId =callContext.getUserId();
		
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("setUserPwd param corpId is null or empty.");
		}
		if(StringUtils.isBlank(userId)){
			throw new IllegalArgumentException("setUserPwd param dingStaffId is null or empty.");
		}

		Integer rs = salPassRepository.setUserPwd(corpId, userId,password);
		return rs>0;
	}

	@Override
	public Boolean  managerUserPwdState(CallContext callContext , Short passState) {
		if(callContext==null){
			throw new IllegalArgumentException("managerUserPwdState -- callContext is null");
		}
		if(passState==null){
			throw new IllegalArgumentException("managerUserPwdState -- passState is null");
		}
		
		String corpId = callContext.getCorpId();
		String userId =callContext.getUserId();
		
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("managerUserPwdState param corpId is null or empty.");
		}
		if(StringUtils.isBlank(userId)){
			throw new IllegalArgumentException("managerUserPwdState param dingStaffId is null or empty.");
		}
		
	 	Integer rs = salPassRepository.managerUserPwdState(corpId, userId ,passState);
		return rs >0;
	}
	
	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
