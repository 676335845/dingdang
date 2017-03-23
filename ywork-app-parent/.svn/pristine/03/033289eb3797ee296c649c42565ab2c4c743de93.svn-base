package me.ywork.salary.service.impl;

import me.ywork.context.CallContext;
import me.ywork.salary.entity.SalCorpBaseRuleEntity;
import me.ywork.salary.entity.SalCorpWhpRuleEntity;
import me.ywork.salary.entity.SalElementInfoEntity;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.enumeration.SalOptPersonType;
import me.ywork.salary.enumeration.SalRuleType;
import me.ywork.salary.enumeration.SalStateType;
import me.ywork.salary.enumeration.SalStaffDeptType;
import me.ywork.salary.model.SalCorpWhpRuleModel;
import me.ywork.salary.model.SalDeptInfoModel;
import me.ywork.salary.model.SalChoiceDeptUserInfoModel;
import me.ywork.salary.model.SalCorpBaseSalRuleModel;
import me.ywork.salary.model.SalSysRuleModel;
import me.ywork.salary.model.SalUpdateStaffesModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.repository.SalReportRepository;
import me.ywork.salary.repository.SalRuleRepository;
import me.ywork.salary.repository.SalStaffBaseInfoRepository;
import me.ywork.salary.repository.SalSynCorpInfoRepository;
import me.ywork.salary.service.SalBaseService;
import me.ywork.salary.service.SalCalcuSalService;
import me.ywork.salary.service.SalRuleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.IdGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaobai on 2017/1/11.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalRuleServiceImpl implements SalRuleService {
	@Autowired
	private SalRuleRepository salRuleRepository;
	@Autowired
	private SalStaffBaseInfoRepository salStaffBaseInfoRepository;
	@Autowired
    private SalCalcuSalService saCalcuSalService;
	@Autowired
	private SalBaseService salBaseService;
	private static final Logger logger = LoggerFactory.getLogger(SalRuleServiceImpl.class);

	@Override
	public List<SalCorpBaseSalRuleModel> getAllSalRules(String corpId) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getAllSalaryRules param corpId is null or empty");
		}
		List<SalCorpBaseSalRuleModel> salRuleModels = salRuleRepository.getAllSalRules(corpId);
        if(salRuleModels.isEmpty()){//如果该企业没有基本的薪资规则，则系统自动创建薪资规则
        	salBaseService.initCorpBsRule(corpId);
        	salRuleModels = salRuleRepository.getAllSalRules(corpId);
        }else{
        	for(SalCorpBaseSalRuleModel salCorpBaseSalRule:salRuleModels){//计算该规则下的适用人数
        		Integer fitNum = salRuleRepository.getCorpRuleFitNum(corpId,salCorpBaseSalRule.getId(),null);
        		salCorpBaseSalRule.setFitNums(fitNum);
        	}
        }
		return salRuleModels;
	}

	@Override
	public SalCorpBaseSalRuleModel getSalRuleByRuleId(String corpId , String ruleId) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getSalaryRuleByRuleId param corpId is null or empty");
		}
		if (StringUtils.isBlank(ruleId)) {
			throw new IllegalArgumentException("getSalaryRuleByRuleId param ruleId is null or empty");
		}
		SalCorpBaseSalRuleModel salRuleDetailModel =  salRuleRepository.getCorpSalRuleByRuleId(corpId , ruleId);
		return salRuleDetailModel;
	}

	@Override
	public Boolean updateSalRule(String corpId ,SalCorpBaseSalRuleModel salaryRule) {
		if (salaryRule == null) {
			throw new IllegalArgumentException("updateSalRule param salaryRule is null");
		}
		String bsRuleId=salaryRule.getId();
		if(StringUtils.isBlank(bsRuleId)){
			throw new IllegalArgumentException("updateSalRule param salaryRule  bsRuleId is null");
		}
		Integer rs = null;
	
		SalCorpBaseSalRuleModel salRuleDetailModel = salRuleRepository
					.getCorpSalRuleByRuleId(corpId,bsRuleId);
		if (salRuleDetailModel != null) {
			salRuleDetailModel.setCalSalDays(salaryRule.getCalSalDays()*100);
			salRuleDetailModel.setFitNums(salaryRule.getFitNums());
			salRuleDetailModel.setModifiedDate(new Date());
			if(salRuleDetailModel.getHasSet()==SalStateType.CLOSE.getCode()){
				salRuleDetailModel.setHasSet(SalStateType.OPEN.getCode());
			}
			rs = salRuleRepository.updateSalRule(salRuleDetailModel);
		}
		if(rs>0){
			  logger.info("updateSalRule--".concat(corpId).concat("更新企业基本薪规则:").concat(bsRuleId).concat("成功！"));
			/**
			 * 重新计算企业员工的工资和薪资报表的总工资
			 */		
			saCalcuSalService.calcuSalReportAutomatic(corpId);
		}
		return rs>0;
	}

	@Override
	public Boolean updateSalRuleStaffies(SalUpdateStaffesModel salUpdateStaffesModel,String corpId) {
		if(salUpdateStaffesModel == null){
			throw new IllegalArgumentException("updateSalRuleStaffies param model is null or empty");
		}
       String cbRuleId =  salUpdateStaffesModel.getCorpBaseRuleId();
       Short ruleType = salUpdateStaffesModel.getRuleType();
       List<SalStaffBaseInfoModel> staffs= salUpdateStaffesModel.getStaffInfos();
       salStaffBaseInfoRepository.deleteCorpSalRule(corpId, cbRuleId, ruleType);       //删除企业原有的薪资规则
       SalStaffBaseInfoModel salStaffBaseInfo = null;
       for(SalStaffBaseInfoModel salModel:staffs){
    	   if(salModel.getType() == SalStaffDeptType.DEPT.getCode()){//如果是部门
    		   String id= salModel.getId();
    		   /**
    		    * 检测该部门下的人员的信息有没有拉取到我们的应用下，若数据没有抓取过来，则发送请求初始化该人员的信息
    		    */
    		   salBaseService.synOrgDeptStaffToApplication(corpId,id);
				List<String> deptIds = salStaffBaseInfoRepository.getDeptInfoByParentId(corpId,id ); //查找该部门下的所有的子部门信息
				for(String deptId:deptIds){
					salStaffBaseInfo = new SalStaffBaseInfoModel();
					salStaffBaseInfo.setId(deptId);
					salStaffBaseInfo.setType(SalStaffDeptType.DEPT.getCode());
					salStaffBaseInfoRepository.updateSalRuleStaffies(salStaffBaseInfo, cbRuleId, ruleType,corpId);
				}
    	   }else if(salModel.getType() == SalStaffDeptType.STAFF.getCode()){
    		   String staffId=salModel.getId();
    		  salBaseService.synOrgStaffToApplication(corpId, staffId);

    		   salStaffBaseInfoRepository.updateSalRuleStaffies(salModel, cbRuleId, ruleType,corpId);
    	   }
       } 
       
     	/**
		 * 重新计算企业员工的工资和薪资报表的总工资
		 */		
		saCalcuSalService.calcuSalReportAutomatic(corpId);
		return Boolean.TRUE;
	}

	@Override
	public Boolean deleteStaffSalRule(String corpId , SalStaffBaseInfoModel salStaffBaseInfoModel) {
		if(salStaffBaseInfoModel == null){
			throw new IllegalArgumentException("deleteStaffSalRule param salStaffBaseInfoModel  is null or empty");
		}
		Short type=salStaffBaseInfoModel.getType();		
		Integer rs = salStaffBaseInfoRepository.deleteStaffSalRule(salStaffBaseInfoModel,type);
		Boolean updateRs = rs == null ?false:rs>0;
		if(updateRs==Boolean.TRUE){
			/**
			 * 重新计算企业员工的工资和薪资报表的总工资
			 */		
			saCalcuSalService.calcuSalReportAutomatic(corpId);
		}
		return updateRs;  
	}

	@Override
	public Boolean updateCwhpRuleList(String corpId ,List<SalCorpWhpRuleModel> salCwhpRuleModels) {
		if(salCwhpRuleModels == null){
			throw new IllegalArgumentException("updateCwhpRuleList param salCwhpRuleModels  is null or empty");
		}
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("updateCwhpRuleList param corpId  is null or empty");
		}
		Integer  size=0;
		for(SalCorpWhpRuleModel salCwhpRuleModel:salCwhpRuleModels){
			SalCorpWhpRuleModel salCwhpRuleModelUpdate = new SalCorpWhpRuleModel();
			String cwhpRuleId = salCwhpRuleModel.getId();
			SalCorpWhpRuleEntity cwhpRule = salRuleRepository.getCwhpRuleEntity(cwhpRuleId);
			if(cwhpRule ==null){
				return Boolean.FALSE;
			}
			Double 	baseHigh = salCwhpRuleModel.getBaseHigh();
			Double 	baseLow = salCwhpRuleModel.getBaseLow();
			Double corpPercent =salCwhpRuleModel.getCorpPercent();
			Double 	personalPercent =salCwhpRuleModel.getPersonalPercent();
			if(cwhpRule.getHasSet() == SalStateType.CLOSE.getCode()){//若是没有设置过
				salCwhpRuleModelUpdate.setHasSet( SalStateType.OPEN.getCode());
			}
			if(baseHigh != null){
				salCwhpRuleModelUpdate.setBaseHigh(baseHigh*100);
			}
			if(baseLow != null){
				salCwhpRuleModelUpdate.setBaseLow(baseLow*100);
			}
			if(corpPercent!=null){
				salCwhpRuleModelUpdate.setCorpPercent(corpPercent*100);
			}
			if(personalPercent!=null){
				salCwhpRuleModelUpdate.setPersonalPercent(personalPercent*100);
			}
			salCwhpRuleModelUpdate.setModifiedDate(new Date());
			salCwhpRuleModelUpdate.setId(salCwhpRuleModel.getId());
			if(salRuleRepository.updateCwhpRule(salCwhpRuleModelUpdate)>0){
			    size++;
			}
		}	
		if( size==salCwhpRuleModels.size()){
			  logger.info("updateSalRule--".concat(corpId).concat("更新企业代缴代扣的薪资规则成功:"));
			/**
			 * 重新计算企业员工的工资和薪资报表的总工资
			 */		
			saCalcuSalService.calcuSalReportAutomatic(corpId);
		}
		return Boolean.TRUE;
	}

	@Override
	public List<SalCorpWhpRuleModel> getCwhpRuleList(String corpId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("getCwhpRuleList param corpId  is null or empty");
		}
		List<SalCorpWhpRuleModel>salCwhpRuleModels= salRuleRepository.getCwhpRuleList(corpId);
		if(salCwhpRuleModels.isEmpty()){//如果社保公积金的列表为空，则要为其初始化
           if( salBaseService.initCorpWhpRule(corpId)==Boolean.TRUE){	
			    salCwhpRuleModels= salRuleRepository.getCwhpRuleList(corpId);
           }
		}
		return salCwhpRuleModels;
	}


	@Override
	public List<SalStaffBaseInfoModel> getStaffAddress(String corpId , String ruleId ,Short type) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("getStaffAddress param corpId  is null or empty");
		}
		List<SalStaffBaseInfoModel> addressModels =new ArrayList<SalStaffBaseInfoModel>();
		//首先查找企业所有的大部门及无部门的人士
		 String deptId="1";
		 List<SalChoiceDeptUserInfoModel> salChoiceDeptUserInfoModels = salStaffBaseInfoRepository.getChoiceDeptUserInfo(corpId,deptId,type,ruleId);
		 if(salChoiceDeptUserInfoModels.isEmpty() == Boolean.FALSE){
	         mergeStaffAddress(corpId,ruleId,type,deptId,addressModels,salChoiceDeptUserInfoModels);
	 	 }
		return addressModels;
	}
	
	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void mergeStaffAddress(String corpId , String ruleId ,Short type,String deptId,List<SalStaffBaseInfoModel> addressModels,List<SalChoiceDeptUserInfoModel> salChoiceDeptUserInfoModels ){
	    if(StringUtils.isBlank(corpId)){
	    	throw new IllegalArgumentException("mergeStaffAddress -- param corpId is null or empty");
	    }
	    if(StringUtils.isBlank(deptId)){
	     	throw new IllegalArgumentException("mergeStaffAddress -- param deptId is null or empty");
	    }
	    if(addressModels == null){
	    	throw new IllegalArgumentException("mergeStaffAddress -- param addressModels is null");
	    }
	    if(salChoiceDeptUserInfoModels==null){
	    	throw new IllegalArgumentException("mergeStaffAddress -- param salChoiceDeptUserInfoModels is null ");
	    }
		SalStaffBaseInfoModel salStaffBaseInfoModel=null;
		String addressId=null;//存储该人员的部门钉钉ID或人员钉钉ID
		Short orgType =null;
		String orgName = null;
		for(SalChoiceDeptUserInfoModel salChoiceDeptUserInfoModel:salChoiceDeptUserInfoModels){
			salStaffBaseInfoModel =new SalStaffBaseInfoModel();
			addressId=salChoiceDeptUserInfoModel.getDingId();
			orgType = salChoiceDeptUserInfoModel.getOrgType();
			orgName = salChoiceDeptUserInfoModel.getName();
			Integer userCount =salChoiceDeptUserInfoModel.getUserCount();
			Integer applicationCount =null;
			if(SalStaffDeptType.DEPT.getCode() == orgType){
			     applicationCount = salStaffBaseInfoRepository.getStaffCountUnderDeptAndRule(corpId, addressId, type, ruleId);
			}else{
				 applicationCount =salStaffBaseInfoRepository.getStaffCountUnderRule(corpId, addressId, type, ruleId);
			}
			if(userCount==applicationCount&&userCount!=null&&userCount!=0&&applicationCount!=null&&applicationCount!=0){//若该部门下或该人员被选中了，则直接显示该人员或部门
				salStaffBaseInfoModel.setId(addressId);
			    if(SalStaffDeptType.STAFFINDEPT.getCode() == orgType){
			    	salStaffBaseInfoModel.setType(SalStaffDeptType.STAFF.getCode());
			    }else{
			    	salStaffBaseInfoModel.setType(orgType);
			    }
				salStaffBaseInfoModel.setId(addressId);
				salStaffBaseInfoModel.setStaffName(orgName);
				addressModels.add(salStaffBaseInfoModel);
			}
		    if(SalStaffDeptType.DEPT.getCode() == orgType &&userCount!=applicationCount&&userCount!=0&&applicationCount!=null&&applicationCount!=0){
		    	List<SalChoiceDeptUserInfoModel> salChoiceDeptUserInfos = salStaffBaseInfoRepository.getChoiceDeptUserInfo(corpId,addressId,type,ruleId);
		    	if(salChoiceDeptUserInfos.isEmpty() == Boolean.FALSE){
				    mergeStaffAddress(corpId,ruleId,type,addressId,addressModels,salChoiceDeptUserInfos);
		    	}
			}
		}
	}
}
