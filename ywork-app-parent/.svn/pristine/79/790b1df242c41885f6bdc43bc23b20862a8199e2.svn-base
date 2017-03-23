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
import com.heyi.utils.IdGenerator;

import me.ywork.context.CallContext;
import me.ywork.salary.entity.SalCorpBaseRuleEntity;
import me.ywork.salary.entity.SalCorpDeductEntity;
import me.ywork.salary.entity.SalCorpInfoEntity;
import me.ywork.salary.entity.SalCorpWhpRuleEntity;
import me.ywork.salary.entity.SalElementInfoEntity;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.entity.SalStaffSalReportEntity;
import me.ywork.salary.enumeration.SalLackDeductType;
import me.ywork.salary.enumeration.SalLateEarlyDeductType;
import me.ywork.salary.enumeration.SalRuleType;
import me.ywork.salary.enumeration.SalStateType;
import me.ywork.salary.enumeration.SalStayAwayDeductType;
import me.ywork.salary.model.SalBaseStateModel;
import me.ywork.salary.model.SalCorpAttenModel;
import me.ywork.salary.model.SalCorpBaseSalRuleModel;
import me.ywork.salary.model.SalCorpDeductModel;
import me.ywork.salary.model.SalCorpWhpRuleModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.repository.SalAttendanceRepository;
import me.ywork.salary.repository.SalRuleRepository;
import me.ywork.salary.repository.SalStaffBaseInfoRepository;
import me.ywork.salary.repository.SalSynCorpInfoRepository;
import me.ywork.salary.service.SalBaseService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalBaseInfoServiceImpl implements SalBaseService {

	@Autowired
	private SalAttendanceRepository salAttendanceRepository;
	
	@Autowired
	private SalStaffBaseInfoRepository salStaffBaseInfoRepository;
	
	@Autowired
	private SalRuleRepository salRuleRepository;
	
	@Autowired
	private SalSynCorpInfoRepository salSynCorpInfoRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(SalBaseInfoServiceImpl.class);


	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SalBaseStateModel getCorpBaseInfo(String corpId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("getCorpBaseInfo param corpId is null or empty");
		}
		SalBaseStateModel salBaseStateModel = new SalBaseStateModel();
		/**
		 * 得到企业组织架构中员工的总的数量
		 */
		Integer staffTotalCount = salStaffBaseInfoRepository.getCorpStaffOrgCount(corpId);
		/**
		 * 得到企业员工没有导入考勤数据的数量
		 */
		List<SalCorpAttenModel>  salCorpAttenModels = salAttendanceRepository.getAllMonthesAttendanceData(corpId);
		String attenReprtId = null;
		Integer attenImportCount =null;
		Integer attenNotImportCount =null;
		for(SalCorpAttenModel salCorpAttenModel:salCorpAttenModels){
			attenReprtId = salCorpAttenModel.getId();
			if(attenReprtId!=null){//如果考勤表的主键不为空，则发送请求得到当月导入员工考勤数据的总数
				attenImportCount = salAttendanceRepository.getAllStaffAttendanceCountByReportId(attenReprtId);//导入考勤数据总数
				if(staffTotalCount!=null&&attenImportCount!=null){
				    attenNotImportCount =staffTotalCount-attenImportCount;//未导入员工考勤数据的数量
				}
			}
			break;
		}
		
		/**
		 * 得到企业导入员工薪资数据的数量
		 */
		Integer staffSalCount = salStaffBaseInfoRepository.getStaffSalInfosCount(corpId);
		/**
		 * 得到企业员工未设置薪资数据的数量
		 */
		Integer staffSalUnSetCount = staffTotalCount - staffSalCount;
		/**
		 * 得到企业设置代缴代扣的状态，状态为1是已经设置，为0是没有设置
		 */
		Boolean hasSetReplace= Boolean.FALSE;
		List<SalCorpWhpRuleModel> salCorpWhpRuleModels = salRuleRepository.getCwhpRuleList(corpId);
		for(SalCorpWhpRuleModel salCorpWhpRuleModel:salCorpWhpRuleModels){
			if(salCorpWhpRuleModel.getHasSet() == SalStateType.OPEN.getCode()){//若已经设置过
				hasSetReplace=true;
			}
		}
		
		/**
		 * 得到企业设置了代缴代扣的人数
		 */
		Integer socialNum = salRuleRepository.getCorpRuleFitNum(corpId, null,SalRuleType.SOCIAL.getCode());
		
		/**
		 * 没有设置代缴代扣的人数
		 */
		Integer unSocialNum = staffTotalCount -socialNum;
		/**
		 * 得到企业设置了个人所得税的人数
		 */
		Integer personalTaxNum = salRuleRepository.getCorpRuleFitNum(corpId, null,SalRuleType.PERSONALTAX.getCode());
		/**
		 * 没有设置个人所得税的人数
		 */
		Integer unPersonalTaxNum = staffTotalCount-personalTaxNum;
		
		/**
		 * 判断企业设置了正算或反算的薪资规则，若没有，则引导用户去设置企业的基本薪资规则
		 */
		List<SalCorpBaseSalRuleModel>  salRules = salRuleRepository.getAllSalRules(corpId);		
		/**
		 * 得到企业在某个基本薪资规则或自定义的薪资规则下的人数
		 */
		Integer bsFitNum = 0;
		Boolean hasSetBsSalRule = Boolean.FALSE;
		for(SalCorpBaseSalRuleModel salCorpBaseSalRuleModel:salRules){
			if(salRuleRepository.getCorpRuleFitNum(corpId, salCorpBaseSalRuleModel.getId(),null)!=null){
				bsFitNum+=bsFitNum;
			}
			if(salCorpBaseSalRuleModel.getHasSet() == SalStateType.OPEN.getCode()){//若已经设置过
				hasSetBsSalRule = Boolean.TRUE;
			}
		}
		Integer unBsFitNum=staffTotalCount-bsFitNum;
		/**
		 * 判断企业是否有设置考勤扣款，若没有，则引导其去设置考勤扣款规则
		 */
		SalCorpDeductModel salCorpDeductModel = salRuleRepository.getCorpdeductRule(corpId);
		Boolean hasSetDeductRule =Boolean.FALSE;
		if(salCorpDeductModel !=null&&salCorpDeductModel.getHasSet()==SalStateType.OPEN.getCode()){//若已经设置过
			hasSetDeductRule = true;
		}
		salBaseStateModel.setHasSetBsRule(hasSetBsSalRule);
		salBaseStateModel.setHasSetReplaceRule(hasSetReplace);
		salBaseStateModel.setHasSetAttenDeduct(hasSetDeductRule);
		salBaseStateModel.setHasImportAttenNum(attenImportCount);
		salBaseStateModel.setUnImportAttenNum(attenNotImportCount);
		salBaseStateModel.setImportStaffSalNum(staffSalCount);
		salBaseStateModel.setUnImportStaffSalNum(staffSalUnSetCount);
		salBaseStateModel.setAttenBsRuleNum(bsFitNum);
		salBaseStateModel.setUnAttenBsRuleNum(unBsFitNum);
		salBaseStateModel.setAttenSocialNum(socialNum);
		salBaseStateModel.setUnAttenSocialNum(unSocialNum);
		salBaseStateModel.setAttenTaxNum(personalTaxNum);
		salBaseStateModel.setUnAttenTaxNum(unPersonalTaxNum);
	   return salBaseStateModel;
	}
	
	@Override
	public Boolean synOrgDeptStaffToApplication(String corpId,String deptId){
		       if(StringUtils.isBlank(corpId)){
		    	   throw new IllegalArgumentException("synOrgDeptStaffToApplication  param corpId is null or empty");
		       }
		       if(StringUtils.isBlank(deptId)){
		    	   throw new IllegalArgumentException("synOrgDeptStaffToApplication  param deptId is null or empty");
		       }
				List<SalElementInfoEntity>  salElementInfoEntities=salStaffBaseInfoRepository.getOrgStaffInfosUnderDept(corpId, deptId);
				if(salElementInfoEntities.isEmpty()){
					return Boolean.TRUE;
				}
				String staffId = null;
				Integer successCount=0;
				
				for(SalElementInfoEntity salElementInfoEntity:salElementInfoEntities){
					   staffId = salElementInfoEntity.getFdDingId();
					   if( this.synOrgStaffToApplication(corpId, staffId)==Boolean.TRUE){
						   successCount++;
					   }
				}
			   return  successCount == salElementInfoEntities.size()?Boolean.TRUE:Boolean.FALSE;
	   }
	
	@Override
	public Boolean synOrgStaffToApplication(String corpId,String staffId){
			  if(StringUtils.isBlank(corpId)){
		    	   throw new IllegalArgumentException("synOrgDeptStaffToApplication  param corpId is null or empty");
		       }
			  if(StringUtils.isBlank(staffId)){
		    	   throw new IllegalArgumentException("synOrgDeptStaffToApplication  param staffId is null or empty");
		       }
			  
			  SalStaffBaseInfoModel  salStaffBaseInfoModel = salStaffBaseInfoRepository.getStaffBaseInfo(corpId, staffId);//得到应有下该人员的信息
			  
			  if(salStaffBaseInfoModel !=null){//该人员信息为空，则初始化该人员的信息
				  return Boolean.TRUE;
			  }
			logger.info("synOrgStaffToApplication  -".concat(corpId).concat("开始为").concat(staffId)+"初始化人员的基本信息！");
            Date nowDate = new Date();
	    	SalStaffBaseInfoEntity salStaffBaseInfoEntity = new SalStaffBaseInfoEntity();
		    salStaffBaseInfoEntity.setAttenPersonalTax(0);
			salStaffBaseInfoEntity.setAttenSocial(0);				
			salStaffBaseInfoEntity.setCorpId(corpId);
			salStaffBaseInfoEntity.setCreateDate(nowDate);
			salStaffBaseInfoEntity.setModifiedDate(nowDate);
			salStaffBaseInfoEntity.setPassState(SalStateType.CLOSE.getCode());
			salStaffBaseInfoEntity.setId(IdGenerator.newId());
			salStaffBaseInfoEntity.setDingStaffId(staffId);
			
			return salSynCorpInfoRepository.synchStaffBaseInfo(salStaffBaseInfoEntity)>0?Boolean.TRUE:Boolean.FALSE;
      }

	@Override
	public Boolean initCorpAttenDeduct(String corpId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("initCorpAttenDeduct param corpId is null or empty!");
		}
		if(salRuleRepository.getCorpdeductRule(corpId)!=null){
			return Boolean.TRUE;
		}
		logger.info("initCorpAttenDeduct  - 开始为".concat(corpId)+"初始化考勤扣款规则！");
		Date nowDate = new Date();
		SalCorpDeductEntity salCorpDeductEntity =new SalCorpDeductEntity();
		salCorpDeductEntity.setModifiedDate(nowDate);
		salCorpDeductEntity.setCreateDate(nowDate);
		salCorpDeductEntity.setId(IdGenerator.newId());
		salCorpDeductEntity.setCorpId(corpId);
		salCorpDeductEntity.setLackDeduct(0.0);
		salCorpDeductEntity.setLackDeductType(SalLackDeductType.FIXED.getCode());
		salCorpDeductEntity.setLateEarlyDeduct(0.0);
		salCorpDeductEntity.setLateEarlyDeductType(SalLateEarlyDeductType.Duration.getCode());
		salCorpDeductEntity.setSeriousLateDeduct(0.0);
		salCorpDeductEntity.setStayAwayDeduct(0.0);
		salCorpDeductEntity.setStayAwayDeductType(SalStayAwayDeductType.FIXED.getCode());
		salSynCorpInfoRepository.synchCorpDeductRule(salCorpDeductEntity);
		return Boolean.TRUE;
	}

	@Override
	public Boolean initCorpBsRule(String corpId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("initCorpBsRule  param corpId is null or empty!");
		}
		Integer totalCount = 0;
    	logger.info("getAllSalRules--".concat(corpId).concat("-- 系统自动为其创建基本薪资规则！"));
    	Date nowDate = new Date();
    	SalCorpBaseRuleEntity salCorpBaseRuleEntity = new SalCorpBaseRuleEntity();
		salCorpBaseRuleEntity.setId(IdGenerator.newId());
		salCorpBaseRuleEntity.setCalSalDays((double) 2175);
		Integer fitNums =0;
		salCorpBaseRuleEntity.setFitNums(fitNums);
		salCorpBaseRuleEntity.setSalRuleId("sys_front_ruleid");
		salCorpBaseRuleEntity.setCreateDate(nowDate);
		salCorpBaseRuleEntity.setModifiedDate(nowDate);
		salCorpBaseRuleEntity.setCorpId(corpId);
		if(salSynCorpInfoRepository.synchCorpBaseRule(salCorpBaseRuleEntity)>0){
			totalCount++;
		}
		
		salCorpBaseRuleEntity = new SalCorpBaseRuleEntity();
		salCorpBaseRuleEntity.setId(IdGenerator.newId());
		salCorpBaseRuleEntity.setCalSalDays((double) 2175);
		salCorpBaseRuleEntity.setFitNums(fitNums);
		salCorpBaseRuleEntity.setSalRuleId("sys_inverse_ruleid");
		salCorpBaseRuleEntity.setCreateDate(nowDate);
		salCorpBaseRuleEntity.setModifiedDate(nowDate);
		salCorpBaseRuleEntity.setCorpId(corpId);
		if(salSynCorpInfoRepository.synchCorpBaseRule(salCorpBaseRuleEntity)>0){
			totalCount++;
		}
        return totalCount==2?Boolean.TRUE:Boolean.FALSE;
	}

	@Override
	public Boolean initCorpWhpRule(String corpId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("initCorpWhpRule  param corpId is null or empty!");
		}
		logger.info("initCorpWhpRule --".concat(corpId).concat("--开始为企业代缴代扣的薪资规则初始化！"));
		Integer initCount= 0;
		Date nowDate =new Date();
		SalCorpWhpRuleEntity salCorpWhpRule = new SalCorpWhpRuleEntity();
	//	salCorpWhpRule.setBaseHigh(1000000.0);
		//salCorpWhpRule.setBaseLow(200000.0);
		salCorpWhpRule.setCorpId(corpId);
		salCorpWhpRule.setCorpPercent(0.0);
		salCorpWhpRule.setModifiedDate(nowDate);
		salCorpWhpRule.setCreateDate(nowDate);
		salCorpWhpRule.setId(IdGenerator.newId());
		salCorpWhpRule.setPersonalPercent(0.0);
		salCorpWhpRule.setSubjectId("endowment_insurance");
		if(salSynCorpInfoRepository.synchCorpWhpRule(salCorpWhpRule)>0){
			initCount++;
		}
		
		salCorpWhpRule = new SalCorpWhpRuleEntity();
		//	salCorpWhpRule.setBaseHigh(1000000.0);
		//salCorpWhpRule.setBaseLow(200000.0);
		salCorpWhpRule.setCorpId(corpId);
		salCorpWhpRule.setCorpPercent(0.0);
		salCorpWhpRule.setModifiedDate(nowDate);
		salCorpWhpRule.setCreateDate(nowDate);
		salCorpWhpRule.setId(IdGenerator.newId());
		salCorpWhpRule.setPersonalPercent(0.0);
		salCorpWhpRule.setSubjectId("industrial_injury_insurance");
		if(salSynCorpInfoRepository.synchCorpWhpRule(salCorpWhpRule)>0){
			initCount++;
		}
		
		salCorpWhpRule = new SalCorpWhpRuleEntity();
		//	salCorpWhpRule.setBaseHigh(1000000.0);
		//salCorpWhpRule.setBaseLow(200000.0);
		salCorpWhpRule.setCorpId(corpId);
		salCorpWhpRule.setCorpPercent(0.0);
		salCorpWhpRule.setModifiedDate(nowDate);
		salCorpWhpRule.setCreateDate(nowDate);
		salCorpWhpRule.setId(IdGenerator.newId());
		salCorpWhpRule.setPersonalPercent(0.0);
		salCorpWhpRule.setSubjectId("maternity insurance");
		if(salSynCorpInfoRepository.synchCorpWhpRule(salCorpWhpRule)>0){
			initCount++;
		}
		
		salCorpWhpRule = new SalCorpWhpRuleEntity();
		//	salCorpWhpRule.setBaseHigh(1000000.0);
		//salCorpWhpRule.setBaseLow(200000.0);
		salCorpWhpRule.setCorpId(corpId);
		salCorpWhpRule.setCorpPercent(0.0);
		salCorpWhpRule.setModifiedDate(nowDate);
		salCorpWhpRule.setCreateDate(nowDate);
		salCorpWhpRule.setId(IdGenerator.newId());
		salCorpWhpRule.setPersonalPercent(0.0);
		salCorpWhpRule.setSubjectId("medical_insurance");
		if(salSynCorpInfoRepository.synchCorpWhpRule(salCorpWhpRule)>0){
			initCount++;
		}
		
		salCorpWhpRule = new SalCorpWhpRuleEntity();
		//	salCorpWhpRule.setBaseHigh(1000000.0);
		//salCorpWhpRule.setBaseLow(200000.0);
		salCorpWhpRule.setCorpId(corpId);
		salCorpWhpRule.setCorpPercent(0.0);
		salCorpWhpRule.setModifiedDate(nowDate);
		salCorpWhpRule.setCreateDate(nowDate);
		salCorpWhpRule.setId(IdGenerator.newId());
		salCorpWhpRule.setPersonalPercent(0.0);
		salCorpWhpRule.setSubjectId("pub_funds");
		if(salSynCorpInfoRepository.synchCorpWhpRule(salCorpWhpRule)>0){
			initCount++;
		}
		
		salCorpWhpRule = new SalCorpWhpRuleEntity();
		//	salCorpWhpRule.setBaseHigh(1000000.0);
		//salCorpWhpRule.setBaseLow(200000.0);
		salCorpWhpRule.setCorpId(corpId);
		salCorpWhpRule.setCorpPercent(0.0);
		salCorpWhpRule.setModifiedDate(nowDate);
		salCorpWhpRule.setCreateDate(nowDate);
		salCorpWhpRule.setId(IdGenerator.newId());
		salCorpWhpRule.setPersonalPercent(0.0);
		salCorpWhpRule.setSubjectId("serious_illness_insurance");
		if(salSynCorpInfoRepository.synchCorpWhpRule(salCorpWhpRule)>0){
			initCount++;
		}
		
		salCorpWhpRule = new SalCorpWhpRuleEntity();
		//	salCorpWhpRule.setBaseHigh(1000000.0);
		//salCorpWhpRule.setBaseLow(200000.0);
		salCorpWhpRule.setCorpId(corpId);
		salCorpWhpRule.setCorpPercent(0.0);
		salCorpWhpRule.setModifiedDate(nowDate);
		salCorpWhpRule.setCreateDate(nowDate);
		salCorpWhpRule.setId(IdGenerator.newId());
		salCorpWhpRule.setPersonalPercent(0.0);
		salCorpWhpRule.setSubjectId("unemploy_insurance");
		if(salSynCorpInfoRepository.synchCorpWhpRule(salCorpWhpRule)>0){
			initCount++;
		}
		
		return initCount==7?Boolean.TRUE:Boolean.FALSE;
	}
	
	@Override
	public Boolean initCorpBaseInfo(String corpId){
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("initCorpBaseInfo  param corpId is null or empty!");
		}
		logger.info("initCorpBaseInfo--".concat(corpId).concat("--开始初始化企业的基本信息"));
		Date nowDate = new Date();
		SalCorpInfoEntity salCorpBaseInfo =new SalCorpInfoEntity();
		salCorpBaseInfo.setCorpId(corpId);
		salCorpBaseInfo.setCreateDate(nowDate);
		salCorpBaseInfo.setModifiedDate(nowDate);
		salCorpBaseInfo.setId(IdGenerator.newId());
		salCorpBaseInfo.setPassState(SalStateType.CLOSE.getCode());
		if(salSynCorpInfoRepository.synchCorpBaseInfo(salCorpBaseInfo)>0){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	@Override
	public String getStaffAllDeptName(String corpId , String staffId){
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("initCorpBaseInfo  param corpId is null or empty!");
		}
		if(StringUtils.isBlank(staffId)){
			throw new IllegalArgumentException("initCorpBaseInfo  param staffId is null or empty!");
		}
		String staffName = "";
		//找到该人员对应的所有的部门
		List<String> deptNames = salStaffBaseInfoRepository.getStaffAllDeptName(corpId, staffId);
		for(String s:deptNames){			
			  staffName +=s+"  ";			
		}
		if("".equals(staffName)){
			staffName ="无部门";
		}
		return staffName;
	}
	
	@Override
	public void initCorpStaffReportSalItem(SalStaffSalReportEntity salStaffSalReportEntity , List<SalSysFieldItemModel> salSysFieldItemList){
		if(salStaffSalReportEntity==null){
			throw new IllegalArgumentException("initCorpStaffSal --param salStaffSalReportEntity is null ");
		}
		if(salSysFieldItemList == null){
			throw new IllegalArgumentException("initCorpStaffSal --param salSysFieldItemList is null ");
		}
		SalSysFieldItemModel	salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("考勤扣款");
		salSysFieldItemModel.setItemValue(-salStaffSalReportEntity.getAttenDeduct());
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("社保公积金扣款");
		salSysFieldItemModel.setItemValue(-salStaffSalReportEntity.getStaffInsuranceSal());;
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("个人所得税扣款");
		salSysFieldItemModel.setItemValue(-salStaffSalReportEntity.getTaxSal());
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("月度奖金");
		salSysFieldItemModel.setItemValue(salStaffSalReportEntity.getAnnualBonus());
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("年度奖金");
		salSysFieldItemModel.setItemValue(salStaffSalReportEntity.getMonthBonus());
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("其他税前补款");
		salSysFieldItemModel.setItemValue(salStaffSalReportEntity.getOtherPretaxSal());
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("其他税前扣款");
		salSysFieldItemModel.setItemValue(-salStaffSalReportEntity.getOtherPretaxDeduct());
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("其他税后补款");
		salSysFieldItemModel.setItemValue(salStaffSalReportEntity.getOtherAftertaxSal());
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("其他税后扣款");
		salSysFieldItemModel.setItemValue(-salStaffSalReportEntity.getOtherAftertaxDeduct());	
		salSysFieldItemList.add(salSysFieldItemModel);
		
		salSysFieldItemModel= new SalSysFieldItemModel();
		salSysFieldItemModel.setItemName("公司缴险与公积金");
		salSysFieldItemModel.setItemValue(salStaffSalReportEntity.getCorpInsuranceSal()-salStaffSalReportEntity.getStaffInsuranceSal()-salStaffSalReportEntity.getTaxSal());	
		salSysFieldItemList.add(salSysFieldItemModel);
	}
}

