package me.ywork.salary.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.heyi.utils.IdGenerator;

import me.ywork.context.CallContext;
import me.ywork.salary.entity.SalCorpAttenEntity;
import me.ywork.salary.entity.SalReportEntity;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.entity.SalStaffSalReportEntity;
import me.ywork.salary.enumeration.SalDeductType;
import me.ywork.salary.enumeration.SalLackDeductType;
import me.ywork.salary.enumeration.SalLateEarlyDeductType;
import me.ywork.salary.enumeration.SalCalRuleType;
import me.ywork.salary.enumeration.SalReportStateType;
import me.ywork.salary.enumeration.SalStayAwayDeductType;
import me.ywork.salary.enumeration.SalUsePageType;
import me.ywork.salary.model.SalCorpAttenModel;
import me.ywork.salary.model.SalCorpBaseSalRuleModel;
import me.ywork.salary.model.SalCorpDeductModel;
import me.ywork.salary.model.SalCorpReportModel;
import me.ywork.salary.model.SalCorpWhpRuleModel;
import me.ywork.salary.model.SalStaffAttenDayModel;
import me.ywork.salary.model.SalStaffAttendanceModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalStaffSalReportDetailModel;
import me.ywork.salary.model.SalStaffSalReportModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.repository.SalAttendanceRepository;
import me.ywork.salary.repository.SalInfoRepository;
import me.ywork.salary.repository.SalReportRepository;
import me.ywork.salary.repository.SalRuleRepository;
import me.ywork.salary.repository.SalStaffBaseInfoRepository;
import me.ywork.salary.repository.SalSynCorpInfoRepository;
import me.ywork.salary.service.SalBaseService;
import me.ywork.salary.service.SalCalcuSalService;
import me.ywork.salary.service.SalRuleService;
import me.ywork.salary.util.PersonalTaxUtils;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalCalcuSalServiceImpl implements SalCalcuSalService {

	@Autowired 
	private SalAttendanceRepository salAttendanceRepository;
	@Autowired
	private SalInfoRepository salInfoRepository;
	@Autowired
	private SalReportRepository salReportRepository;
	@Autowired
	private SalRuleRepository salRuleRepository;
	@Autowired
	private SalStaffBaseInfoRepository salStaffBaseInfoRepository;
	@Autowired
	private SalSynCorpInfoRepository salSynCorpInfoRepository;
	@Autowired
	private SalRuleService salRuleService;
	@Autowired
	private SalBaseService salBaseService;
	
	private static final Logger logger = LoggerFactory.getLogger(SalCalcuSalServiceImpl.class);
	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean calcuSalReportAutomatic(String corpId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("calcuSalReportAutomatic -- corpId is null or empty!");
		}
		logger.info("updateSalsOnStaffes --  为".concat(corpId).concat("重新计算薪资报表为预估的工资！"));
		List<SalCorpReportModel>  salCorpReportModels =salReportRepository.getAllSalReportsByState(corpId, SalReportStateType.ESTIMATE.getCode());
		if(salCorpReportModels==null){
		 return Boolean.TRUE;
		}
		/**
		 * 企业基本信息准备
		 */
    	/**
    	 * 得到企业的考勤请假字段的扣款信息
    	 */
    	List<SalSysFieldItemModel>  approveVactionDeductList = salStaffBaseInfoRepository.getCorpVacations(corpId);
    	/**
    	 * 得到企业基本考勤的扣款信息
    	 */
    	SalCorpDeductModel  salCorpDeduct =salRuleRepository.getCorpdeductRule(corpId);
    	if(salCorpDeduct == null){//若企业考勤扣款规则为空，则要初始化考勤扣款规则 
    		salBaseService.initCorpAttenDeduct(corpId);
    		salCorpDeduct=salRuleRepository.getCorpdeductRule(corpId);
    	}

    	
    	/**
    	 * 得到企业所有的计薪规则的列表
    	 */
    	List<SalCorpBaseSalRuleModel> salSysRuleList = salRuleRepository.getAllSalRules(corpId);
    	Map<String,SalCorpBaseSalRuleModel> salCorpBaseSalRulMap= new HashMap<String,SalCorpBaseSalRuleModel>();//存储基本计薪规则的HashMap
    	
    	/**
    	 * 得到企业社保和公积金的扣款规则
    	 */
    	List<SalCorpWhpRuleModel> salCwhpRuleList=	salRuleService.getCwhpRuleList(corpId);
    	SalStaffSalReportEntity salStaffSalReportEntity = null;
    	List<SalStaffAttendanceModel> salStaffAttens=null;
    	for(SalCorpBaseSalRuleModel salSysRuleModel:salSysRuleList){
    		salCorpBaseSalRulMap.put(salSysRuleModel.getId(), salSysRuleModel);
    	}
    	Date nowDate = new Date();
		/**
		 * 生成员工的月度薪资  
		 */
	//	List<SalStaffBaseInfoEntity> staffSalInfos = staffSalInfoRepository.getAllStaffSalInfo(corpId);
	    //得到在算工资应用中拥有薪资信息的员工的信息
			List<SalStaffBaseInfoEntity> staffSalInfos =salInfoRepository.getAllStaffSalInfo(corpId);
			Set<String> staffIds=null; 
			 Date monthTime = null;
			 String attenReportId = null;
		for(SalCorpReportModel salCorpReportModel:salCorpReportModels){//循环得到状态为预估的月度工资表
			Double corpShoudPaySal = 0.0;
			Double corpActualPaySal = 0.0;
			Double corpSocialPaySal = 0.0;
			Double corpStaffPay = 0.0;
			Double salDeduct =0.0;
		    String salReportId=salCorpReportModel.getId();//得到月度工资表的主键ID
		    monthTime = salCorpReportModel.getMonthTime();
		    SalCorpAttenModel  salCorpAttenModel =  salAttendanceRepository.getAttenByMonthTime(corpId, monthTime);
		    attenReportId = salCorpAttenModel.getId();
		    //得到该工资表下员工的薪资信息
		    List<SalStaffSalReportModel> salStaffReportInfos =salReportRepository.getSalDatailByReportIdNotPage(salReportId, corpId);
		    //初始化存储该月员工ID的哈希集合
		    staffIds =new HashSet<String>();
		    for(SalStaffSalReportModel salStaffSalReportModel:salStaffReportInfos){
		    	staffIds.add(salStaffSalReportModel.getDingStaffId());
		    }
		    salStaffAttens=salAttendanceRepository.getAllStaffAttendanceByReportId(null,corpId,monthTime, null, null, SalUsePageType.NotUsePage.getCode());
		    
       //     for(SalStaffBaseInfoEntity salStaffBaseInfoEntity:staffSalInfos){
		  //循环得到所有员工的薪资信息
		    for(SalStaffAttendanceModel salStaffAtten:salStaffAttens){
            	String staffId=salStaffAtten.getDingStaffId();
				SalStaffBaseInfoModel  salStaffBaseInfoEntity =salStaffBaseInfoRepository.getStaffBaseInfo(corpId, staffId);
            	//查询该员工该月的月度薪资
            	salReportRepository.getSelectedStaffSalDetail(staffId, salReportId, corpId);
            	if(staffIds.contains(staffId)){//如果该员工在该月存在薪资数据
            		//重新计算该员工该月的薪资数据
    				String corpBsRuleId=salStaffBaseInfoEntity.getCorpBaseRuleId();
        			Double actualSal = 0.0;
        			Double attendanceDeduct =0.0;
        			Double replaceDeduct=0.0;
        			Double corpStaffSocialSal =0.0;
        			Double shouldPaySalDouble =0.0;
        			Double shouldPaySal = 0.0;
        			Double staffInsuranceSal =0.0;
    				Double  taxReplace =0.0;
    				Double frontAttenDeduct = 0.0;
        			if(corpBsRuleId != null){//若指定了基本薪资规则，也参与薪资计算
        				//判断该薪资规则是正算还是反算
        				Short salRuleType = salRuleRepository.getRuleTypeByCorpBsRuleId(corpBsRuleId);
        				
            				/**
            				 * 得到该员工的参与社保和公积金的状态
            				 */
            				Integer attenSocialStatus = salStaffBaseInfoEntity.getAttenSocial();
            				/**
            				 * 得到该员工的参与个人所得税的状态
            				 */
            				Integer attenPersonTax = salStaffBaseInfoEntity.getAttenPersonalTax();
            				staffId = salStaffBaseInfoEntity.getDingStaffId();
            				if(StringUtils.isBlank(staffId)){       				
            					continue;
            				}
            				shouldPaySal = salStaffBaseInfoEntity.getShouldPaySal();//基本工资
            				if(shouldPaySal == null){
            					continue;
            				}
            				//计算该员工的日薪
            				Double calSalDays = salCorpBaseSalRulMap.get(corpBsRuleId).getCalSalDays();
            				shouldPaySalDouble = shouldPaySal;
            			    double daySal =  shouldPaySalDouble/calSalDays;
            				/**
            				 * 查询该员工的基本考勤信息
            				 */
            				SalCorpAttenEntity  salStaffAttenEntity = salAttendanceRepository.getStaffAttendanceByMonthTime(corpId, staffId, monthTime);
            				if(salStaffAttenEntity == null||shouldPaySal==0){                               
            					 salStaffSalReportEntity =salReportRepository.getStaffMonthSalEntity(salReportId,staffId);
                				salStaffSalReportEntity.setActualSal(0.0);
                				salStaffSalReportEntity.setShouldPaySal(0.0);
                				salStaffSalReportEntity.setDingStaffId(staffId);
                				salStaffSalReportEntity.setModifiedDate(nowDate);
                				salStaffSalReportEntity.setSalDeduct(0.0);
                				salStaffSalReportEntity.setAttenDeduct(0.0);
                				salStaffSalReportEntity.setReplaceDeduct(0.0);
                 				salStaffSalReportEntity.setTaxSal(0.0);
                  				salStaffSalReportEntity.setStaffInsuranceSal(0.0);
                  				salStaffSalReportEntity.setCorpId(corpId);
                  				salStaffSalReportEntity.setCorpInsuranceSal(0.0);
                 				salStaffSalReportEntity.setSalReportId(salReportId);
                				salReportRepository.updateStaffMonthSalEntity(salStaffSalReportEntity);
            					continue;
            				}
            				/**
            				 * 得到员工的出勤天数
            				 */
            				Double attendanceDay = salStaffAttenEntity.getAttendanceDays();		        				
            				double voctionDeduct = 0.0;
        				      if(salRuleType  == SalCalRuleType.OPPOSITE.getCode()){//如果该计算规则是反算,则从总工资中减去请假的天数
         					        //shouldPaySal =(long)Math.round ((calSalDays- attendanceDay)*daySal*100);//反算规则下的月度基本工资
        	        				/**
        	        				 * 查询该员工的请假的扣款天数
        	        				 */
        	        				List<SalStaffAttenDayModel> salStaffAttenDayModels = salAttendanceRepository.getSalStaffAttenDayInfos(corpId, attenReportId, staffId);
        	        				/**
        	        				 * 计算该员工该月的考勤请假的扣款金额
        	        				 */				        			
        	        				for(SalStaffAttenDayModel salStaffAttenDay:salStaffAttenDayModels){
        	        					String vactionId=salStaffAttenDay.getFieldId();
        	        					for(SalSysFieldItemModel salSysFieldItem:approveVactionDeductList){
        	            					if(salSysFieldItem.getItemId().equals(vactionId)){
        	            						Short deductType = salSysFieldItem.getDeductType();
        	            						double deductValue=salSysFieldItem.getItemValue();
        	            						if(deductType == SalDeductType.FIXED.getCode()){//如果扣款类型是固定类型
        	            							if(salStaffAttenDay.getFieldDay()!=null){
        	            							voctionDeduct +=salStaffAttenDay.getFieldDay()/100*deductValue/100;
        	            							}
        	            						}else if(deductType == SalDeductType.PERCENT.getCode()){
        	            							if(salStaffAttenDay.getFieldDay()!=null){
        	            							  voctionDeduct +=daySal*salStaffAttenDay.getFieldDay()/100*deductValue/10000;
        	            							}
        	            						}			            						
        	            					}
        	            				}
        	        				}
        	        				double shouldPaySal3 = shouldPaySalDouble;
        	        				shouldPaySal3 =shouldPaySalDouble-voctionDeduct*100;//计算请假扣款后的员工的薪资
        	        				if(shouldPaySal3*100<0){//如果工资额小于0，则采取正算的方式
        	        					double 	shouldPaySal2 = 0.0;
        	        					if(attendanceDay>calSalDays){    						
        	        					 	shouldPaySalDouble=calSalDays*daySal;	        			
        	        					}else{
        	        						shouldPaySal2 = attendanceDay/100*daySal;	        	
        	        						shouldPaySalDouble =shouldPaySal2*100;
        	        					}
        	        				}
        				}else{//若是正算
        					double 	shouldPaySal2 = 0.0;
        					if(attendanceDay>calSalDays){    						
            					 	shouldPaySalDouble= calSalDays*daySal;	        			
        					}else{
        						shouldPaySal2 = attendanceDay/100*daySal;	        	
        						shouldPaySalDouble = shouldPaySal2*100;
        					}
        					frontAttenDeduct=shouldPaySal-shouldPaySalDouble;
        				}		        		
            				/**
            				 * 计算该员工的基本考勤的扣款信息
            				 */
            				double seriousLateDeduct=salStaffAttenEntity.getSeriousLateTimes()*salCorpDeduct.getSeriousLateDeduct()/100;//严重迟到的扣款
            				double lateDeduct = 0;//迟到的扣款
            				if(salCorpDeduct.getLateEarlyDeductType() == SalLateEarlyDeductType.Duration.getCode()){
            					lateDeduct = salStaffAttenEntity.getLateHours()*salCorpDeduct.getLateEarlyDeduct()/100;
            				}else if(salCorpDeduct.getLateEarlyDeductType() == SalLateEarlyDeductType.Times.getCode()){
            					lateDeduct = salStaffAttenEntity.getLateTimes()*salCorpDeduct.getLateEarlyDeduct()/100;
            				}
            				double earlyLeaveDeduct = 0;//早退扣款
            				if(salCorpDeduct.getLateEarlyDeductType() == SalLateEarlyDeductType.Duration.getCode()){
            					earlyLeaveDeduct = salStaffAttenEntity.getEarlyLeaveHours()*salCorpDeduct.getLateEarlyDeduct()/100;
            				}else if(salCorpDeduct.getLateEarlyDeductType() == SalLateEarlyDeductType.Times.getCode()){
            					earlyLeaveDeduct = salStaffAttenEntity.getEarlyLeaveTimes()*salCorpDeduct.getLateEarlyDeduct()/100;
            				}
	        				double stayAwayDeduct = 0;//旷工扣款
	        				double stayAwayLateDeduct = 0;//旷工迟到扣款
	        				double workLackDeduct = 0;//上班缺卡扣款 
	        				double offWorkLackDeduct =0;//下班缺卡扣款
	        				if(salRuleType  == SalCalRuleType.OPPOSITE.getCode()){
		        				if(salCorpDeduct.getStayAwayDeductType() == SalStayAwayDeductType.FIXED.getCode()){
		        					stayAwayDeduct = salStaffAttenEntity.getUnWorkDays()*salCorpDeduct.getStayAwayDeduct()/100;
		        				}else if(salCorpDeduct.getStayAwayDeductType() == SalStayAwayDeductType.PERCENT.getCode()){
		        					stayAwayDeduct = daySal*salStaffAttenEntity.getUnWorkDays()*salCorpDeduct.getStayAwayDeduct()/10000;
		        				}
		        				if(salCorpDeduct.getStayAwayDeductType()== SalStayAwayDeductType.FIXED.getCode()){
		        					stayAwayLateDeduct = salStaffAttenEntity.getUnWorkLateDays()*salCorpDeduct.getStayAwayDeduct()/100;
		        				}else if(salCorpDeduct.getStayAwayDeductType() == SalStayAwayDeductType.PERCENT.getCode()){
		        					stayAwayLateDeduct =daySal* salStaffAttenEntity.getUnWorkLateDays()*salCorpDeduct.getStayAwayDeduct()/10000;
		        				}
		        				if(salCorpDeduct.getLackDeductType() == SalLackDeductType.FIXED.getCode()){
		        					workLackDeduct = salStaffAttenEntity.getWorkAbsenceTimes()*salCorpDeduct.getLackDeduct()/100;
		        				}else if(salCorpDeduct.getLackDeductType() == SalLackDeductType.PERCENT.getCode()){
		        					workLackDeduct = daySal*salStaffAttenEntity.getWorkAbsenceTimes()*salCorpDeduct.getLackDeduct()/10000;
		        				}
		        				if(salCorpDeduct.getLackDeductType() == SalLackDeductType.FIXED.getCode()){
		        					offWorkLackDeduct = salStaffAttenEntity.getOffWorkAbsenceTimes()*salCorpDeduct.getLackDeduct()/100;
		        				}else if(salCorpDeduct.getLackDeductType() == SalLackDeductType.PERCENT.getCode()){
		        					offWorkLackDeduct = daySal*salStaffAttenEntity.getOffWorkAbsenceTimes()*salCorpDeduct.getLackDeduct()/10000;
		        				}
	        				}
            				attendanceDeduct =offWorkLackDeduct+workLackDeduct+stayAwayDeduct+stayAwayLateDeduct
            						+earlyLeaveDeduct+lateDeduct+seriousLateDeduct+voctionDeduct;
            				/**
            				 * 得到该员工的所有的浮动款项
            				 */
            				SalStaffSalReportDetailModel  staffFloatSal = salReportRepository.getFloatSalDataByStaffId(staffId, corpId, salReportId);
            				Double annualBonus = staffFloatSal.getAnnualBonus();
            				if(annualBonus==null){
            					annualBonus=0.0;
            				}
            				Double monthBonus = staffFloatSal.getMonthBonus();
            				if(monthBonus==null){
            					monthBonus=0.0;
            				}
            				Double otherPretaxSal = staffFloatSal.getOtherPretaxSal();
            				if(otherPretaxSal == null){
            					otherPretaxSal=0.0;
            				}
            				Double otherPretaxDeduct = staffFloatSal.getOtherPretaxDeduct();
            				if(otherPretaxDeduct == null){
            					otherPretaxDeduct=0.0;
            				}
            				Double otherAftertaxSal = staffFloatSal.getOtherAftertaxSal();
            				if(otherAftertaxSal==null){
            					otherAftertaxSal=0.0;
            				}
            				Double otherAftertaxDeduct = staffFloatSal.getOtherAftertaxDeduct();
            				if(otherAftertaxDeduct==null){
            					otherPretaxDeduct=0.0;
            				}
            				shouldPaySalDouble = shouldPaySalDouble -attendanceDeduct*100+annualBonus+monthBonus+otherAftertaxSal-otherPretaxDeduct
            						+otherAftertaxSal-otherAftertaxDeduct;
            				salDeduct =attendanceDeduct*100+otherPretaxDeduct+otherAftertaxDeduct;
            			 /**
            		 	 * 实发工资=应发工资-社保公积金-税款
            			 */
            				/**
            				 *  判断该员工是否参与社保公积金
            				 */
        			   actualSal=shouldPaySalDouble;
        				Boolean  attenSocial =true;
        				if(attenSocialStatus == null||attenSocialStatus==0){
        					attenSocial = false;
        				}
        				if(attenSocial){//若员工要参与社保和公积金
        					Double whpActualSal = new Double(0);
        					Double whpHighSal = new Double(0);
        					Double whpLowSal = new Double(0);
        					whpActualSal = actualSal-annualBonus-monthBonus+otherAftertaxDeduct-otherPretaxSal;//因为年终奖和当月奖金、税后补款和扣款、不参与不参与社保和公积金，所以要除去这部分,
        					//TODO:计算社保和公积金的扣款额
        					for(SalCorpWhpRuleModel salCwhpRule:salCwhpRuleList){
        						whpHighSal = salCwhpRule.getBaseHigh();
        						whpLowSal = salCwhpRule.getBaseLow();
        						if( whpHighSal != null){
	        						if(whpActualSal>=whpHighSal){
	        							whpActualSal = whpHighSal;
	        						}
        						}
        						if(whpLowSal!=null){
        							if(whpActualSal<=whpLowSal){
        								whpActualSal=whpLowSal;
        							}
        						}
        						corpStaffSocialSal +=whpActualSal/100*(salCwhpRule.getCorpPercent()/10000);
        					}
        					for(SalCorpWhpRuleModel salCwhpRule :salCwhpRuleList){
        						double personPercent =salCwhpRule.getPersonalPercent();
        						whpHighSal = salCwhpRule.getBaseHigh();
        						whpLowSal = salCwhpRule.getBaseLow();
        						if( whpHighSal != null){
	        						if(whpActualSal>=whpHighSal){
	        							whpActualSal = whpHighSal;
	        						}
        						}
        						if(whpLowSal!=null){
        							if(whpActualSal<=whpLowSal){
        								whpActualSal=whpLowSal;
        							}
        						}
        						staffInsuranceSal +=whpActualSal/100*(personPercent/10000);				
        					}		
        				}
        				replaceDeduct=staffInsuranceSal;
        				actualSal=actualSal-replaceDeduct*100+otherAftertaxDeduct-otherPretaxSal;//因为税后扣款和补款不参与交税，所以在交税前要出去这部分
        				/**
        				 * 判断该员工是否参与个税
        				 */		   

        				Boolean  attenTax =true;
        				if(attenPersonTax == null||attenPersonTax==0){
        					attenTax = false;
        				}
        				if(attenTax){//若员工要参与个人所得税
        					//TODO:计算个税的扣款额
        					taxReplace = PersonalTaxUtils.getPersonalTax(actualSal/100);
        				}
        				replaceDeduct +=taxReplace;
        				actualSal=actualSal-taxReplace*100+otherAftertaxSal-otherAftertaxDeduct;  //税后再加上补款，减去扣款
        			}else{
    					shouldPaySalDouble = 0.0;
    					actualSal=0.0;
    					staffInsuranceSal=0.0;
    					taxReplace=0.0;
    					attendanceDeduct=0.0;
    					frontAttenDeduct=0.0;
    					replaceDeduct=0.0;
    					corpStaffSocialSal=0.0;
        			}
        			 salStaffSalReportEntity =salReportRepository.getStaffMonthSalEntity(salReportId,staffId);
        			 salStaffSalReportEntity.setActualSal(actualSal);
     				salStaffSalReportEntity.setShouldPaySal(shouldPaySalDouble);
     				salStaffSalReportEntity.setStaffInsuranceSal(staffInsuranceSal*100);
    				salStaffSalReportEntity.setTaxSal(taxReplace*100);
    				salStaffSalReportEntity.setAttenDeduct(attendanceDeduct*100+frontAttenDeduct);
     				salStaffSalReportEntity.setDingStaffId(staffId);
     				salStaffSalReportEntity.setModifiedDate(nowDate);
     				salStaffSalReportEntity.setSalReportId(salReportId);
     				salStaffSalReportEntity.setMonthTime(monthTime);
     				salStaffSalReportEntity.setCorpId(corpId);
     				corpStaffSocialSal +=replaceDeduct;
     				salStaffSalReportEntity.setCorpInsuranceSal(corpStaffSocialSal*100);
     				salStaffSalReportEntity.setSalDeduct(salDeduct+frontAttenDeduct);
     				salStaffSalReportEntity.setReplaceDeduct(replaceDeduct*100);
     				salReportRepository.updateStaffMonthSalEntity(salStaffSalReportEntity);
     				corpShoudPaySal +=shouldPaySalDouble;
    				corpActualPaySal +=actualSal;				
    				corpSocialPaySal +=corpStaffSocialSal*100;			
            	}else{//否则，再次为该员工初始化该月的薪资数据
            		salStaffSalReportEntity = new SalStaffSalReportEntity();
    				salStaffSalReportEntity.setId(IdGenerator.newId());
    				salStaffSalReportEntity.setActualSal(0.0);
    				salStaffSalReportEntity.setShouldPaySal(0.0);
    				salStaffSalReportEntity.setDingStaffId(staffId);
    				salStaffSalReportEntity.setCreateDate(nowDate);
    				salStaffSalReportEntity.setModifiedDate(nowDate);
    				salStaffSalReportEntity.setSalReportId(salReportId);
    				salStaffSalReportEntity.setMonthTime(monthTime);
    				salStaffSalReportEntity.setSalDeduct(0.0);
    				salStaffSalReportEntity.setCorpId(corpId);
    				salStaffSalReportEntity.setReplaceDeduct(0.0);
    				salSynCorpInfoRepository.addStaffMonthSal(salStaffSalReportEntity);  		   				
            	}
            }
            SalReportEntity salReportEntity = new SalReportEntity();
            /**
    		 * 计算企业当月的总工资
    		 */
			corpStaffPay =corpSocialPaySal+corpShoudPaySal;
			salReportEntity.setStaffCost((double)Math.round(corpStaffPay/100)*100);
			salReportEntity.setInsuranceSal((double)Math.round(corpSocialPaySal/100)*100);
			salReportEntity.setActualPaySal((double)Math.round(corpActualPaySal/100)*100);
			salReportEntity.setShouldPaySal((double)Math.round(corpShoudPaySal/100)*100);
			salReportEntity.setModifiedDate(new Date());
			salReportEntity.setId(salCorpReportModel.getId());
			salReportRepository.updateSalReport(salReportEntity);
		}
		return Boolean.TRUE;
	}
	

}
