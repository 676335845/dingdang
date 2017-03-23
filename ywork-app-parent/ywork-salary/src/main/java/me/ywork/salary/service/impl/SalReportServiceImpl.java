package me.ywork.salary.service.impl;

import me.ywork.context.CallContext;
import me.ywork.page.PageData;
import me.ywork.page.PageDataImpl;
import me.ywork.page.Pageable;
import me.ywork.salary.model.SalCorpAttenModel;
import me.ywork.salary.model.SalCorpBaseSalRuleModel;
import me.ywork.salary.model.SalCorpDeductModel;
import me.ywork.salary.model.SalCorpInfoModel;
import me.ywork.salary.model.SalCorpReportModel;
import me.ywork.salary.model.SalCorpWhpRuleModel;
import me.ywork.salary.model.SalStaffAttenDayModel;
import me.ywork.salary.model.SalStaffAttendanceModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.entity.SalCorpAttenEntity;
import me.ywork.salary.entity.SalElementInfoEntity;
import me.ywork.salary.entity.SalReportEntity;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.entity.SalStaffSalReportEntity;
import me.ywork.salary.enumeration.SalDeductType;
import me.ywork.salary.enumeration.SalLackDeductType;
import me.ywork.salary.enumeration.SalLateEarlyDeductType;
import me.ywork.salary.enumeration.SalCalRuleType;
import me.ywork.salary.enumeration.SalStaffDeptType;
import me.ywork.salary.enumeration.SalStayAwayDeductType;
import me.ywork.salary.enumeration.SalUsePageType;
import me.ywork.salary.model.SalStaffSalReportDetailModel;
import me.ywork.salary.model.SalStaffSalReportModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.model.SalStaffMbSalInfoModel;
import me.ywork.salary.repository.SalReportRepository;
import me.ywork.salary.repository.SalRuleRepository;
import me.ywork.salary.repository.SalStaffBaseInfoRepository;
import me.ywork.salary.repository.SalSynCorpInfoRepository;
import me.ywork.salary.repository.SalAttendanceRepository;
import me.ywork.salary.repository.SalInfoRepository;
import me.ywork.salary.service.SalBaseService;
import me.ywork.salary.service.SalCalcuSalService;
import me.ywork.salary.service.SalReportService;
import me.ywork.salary.service.SalRuleService;
import me.ywork.salary.util.PersonalTaxUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.heyi.utils.IdGenerator;

import java.io.InputStream;
import java.text.SimpleDateFormat;
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
public class SalReportServiceImpl implements SalReportService {
	@Autowired
	private SalReportRepository salReportRepository;
	@Autowired
	private SalInfoRepository salInfoRepository;
	@Autowired
	private SalAttendanceRepository salAttendanceRepository;
	@Autowired
	private SalSynCorpInfoRepository salSynCorpInfo;
	@Autowired
	private SalStaffBaseInfoRepository salStaffBaseInfoRepository;
	@Autowired
	private SalRuleRepository salRuleRepository;
	@Autowired
	private SalRuleService salRuleService;
	@Autowired
	private SalCalcuSalService saCalcuSalService;
	@Autowired
	private SalBaseService salBaseService;
	

	private static final Logger logger = LoggerFactory.getLogger(SalReportServiceImpl.class);

	@Override
	public List<SalCorpReportModel> getAllSalReports(String corpId) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getAllSalReports param corpId is null or empty.");
		}
		
		List<SalCorpReportModel> salReportModels = salReportRepository.getAllSalReports(corpId);//得到企业月度工资报表
    	List<SalCorpAttenModel> corpAttenModels = salAttendanceRepository.getAllMonthesAttendanceData(corpId);//得到企业所有的月度考勤报表
    	Set<Date> needInitDate = new HashSet<Date>();

    	if(salReportModels.size()!=corpAttenModels.size()){    		
    		logger.info("getAllSalReports -- 企业".concat(corpId).concat("存在没有初始化的薪资报表！"));    	
			for(SalCorpAttenModel salCorpAttenModel:corpAttenModels){
		    	Boolean needInitRs=Boolean.FALSE;
				for(SalCorpReportModel salCorpReportModel:salReportModels){
					if(salCorpReportModel.getMonthTime().getTime() == salCorpAttenModel.getMonthTime().getTime()){
						needInitRs = Boolean.TRUE;
					}
				}
	    		if(needInitRs==Boolean.FALSE){
	    			needInitDate.add(salCorpAttenModel.getMonthTime());
	    			logger.info("getAllSalReports --  企业:".concat(corpId).concat(""+salCorpAttenModel.getMonthTime()+"").concat("的工资报表需要初始化！"));
	    		}
			}
    	}
        if(needInitDate.size()>0){//如果工资报表为空,则要为企业初始化工资报表
        	/**
        	 * 若底下存在没有员工同步过来，则要主动获取组织架构下的人员信息
        	 */
        	salBaseService.synOrgDeptStaffToApplication(corpId, "1");
    		SalReportEntity salReportEntity =null;
    		Integer initNum=0;
    		Date nowDate =new Date();
    		Date monthTime = null; 		
    		String staffId  = null;
    		String attenReportId =null;
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
        	for(SalCorpBaseSalRuleModel salSysRuleModel:salSysRuleList){
        		salCorpBaseSalRulMap.put(salSysRuleModel.getId(), salSysRuleModel);
        	}
        	String salReportId =null;
    		List<SalStaffAttendanceModel> salStaffAttens=null;
        	for(SalCorpAttenModel salCorpAttenModel:corpAttenModels){//循环得到企业的月度考勤报表      
        		if(!needInitDate.contains(salCorpAttenModel.getMonthTime())){//如果在需要初始化的日期中不存在该企业，则检测下一条数据
        			continue;
        		}    		
        		logger.info("getAllSalReports --开始为企业".concat(corpId).concat("初始化").concat(""+salCorpAttenModel.getMonthTime()+"").concat("的工资报表！"));
        		attenReportId =salCorpAttenModel.getId();
        		salStaffAttens=salAttendanceRepository.getAllStaffAttendanceByReportId(attenReportId,corpId,null, null, null, SalUsePageType.NotUsePage.getCode());
        		 monthTime=salCorpAttenModel.getMonthTime();
        		salReportEntity =new SalReportEntity();
        		salReportId = IdGenerator.newId();
        		salReportEntity.setId(salReportId);
        		salReportEntity.setCorpId(corpId);
        		salReportEntity.setMonthTime(monthTime);
        		salReportEntity.setCreateDate(nowDate);
        		salReportEntity.setModifiedDate(nowDate);
        		Double corpShoudPaySal = 0.0;
        		Double corpActualPaySal = 0.0;
        		Double corpSocialPaySal = 0.0;
        		Double corpStaffPay = 0.0;
        		
        		if(salSynCorpInfo.addCorpSalReport(salReportEntity)>0)//先初始化月度薪资报表
        		{
        			initNum++;//将初始化薪资报表的数据量+1      			
        			/**
        			 * 
        			 *   生成员工的月度薪资  
        			 * 
        			 */
        	//		List<SalStaffBaseInfoEntity> staffSalInfos = salInfoRepository.getAllStaffSalInfo(corpId);
        			//for(SalStaffBaseInfoEntity salStaffBaseInfoEntity:staffSalInfos){//循环员工得到员工的信息
        			for(SalStaffAttendanceModel salStaffAtten:salStaffAttens){
        				/**
        				 * 得到该员工的计薪规则
        				 */
        				staffId = salStaffAtten.getDingStaffId();
        				//String corpBsRuleId =salStaffBaseInfoEntity.getCorpBaseRuleId();
        				SalStaffBaseInfoModel  salStaffBaseInfoEntity =salStaffBaseInfoRepository.getStaffBaseInfo(corpId, staffId);
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
        				
        				/**
        				 * 先判断该员工基本薪资规则是否存在，若不存在，所有的信息都是为空
        				 */
        				if(corpBsRuleId != null){
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
		        				 *   查询该员工的基本考勤信息
		        				 */
		        				SalCorpAttenEntity  salStaffAttenEntity = salAttendanceRepository.getStaffAttendanceByMonthTime(corpId, staffId, monthTime);
		        				if(salStaffAttenEntity == null||shouldPaySal==0){//若基本考勤信息为空，工资也是为0
		            				salStaffSalReportEntity = new SalStaffSalReportEntity();
		            				salStaffSalReportEntity.setId(IdGenerator.newId());
		            				salStaffSalReportEntity.setActualSal(0.0);
		            				salStaffSalReportEntity.setShouldPaySal(0.0);
		            				salStaffSalReportEntity.setDingStaffId(staffId);
		            				salStaffSalReportEntity.setCreateDate(nowDate);
		            				salStaffSalReportEntity.setModifiedDate(nowDate);
		            				salStaffSalReportEntity.setSalReportId(salReportId);
		            				salStaffSalReportEntity.setMonthTime(monthTime);
	                				salStaffSalReportEntity.setAttenDeduct(0.0);
	                				salStaffSalReportEntity.setCorpId(corpId);
		            				salStaffSalReportEntity.setSalDeduct(0.0);
		            				salStaffSalReportEntity.setReplaceDeduct(0.0);
		            				salStaffSalReportEntity.setTaxSal(0.0);
	                  				salStaffSalReportEntity.setStaffInsuranceSal(0.0);
	                  				salStaffSalReportEntity.setCorpInsuranceSal(0.0);
	                 				salStaffSalReportEntity.setSalReportId(salReportId);
		            				salSynCorpInfo.addStaffMonthSal(salStaffSalReportEntity);  			
		        					continue;
		        				}
		        				/**
		        				 * 得到员工的出勤天数
		        				 */
		        				Double attendanceDay = salStaffAttenEntity.getAttendanceDays();		        				
		        				double voctionDeduct = 0.0;
           				        if(salRuleType  == SalCalRuleType.OPPOSITE.getCode()){//如果该计算规则是反算,则从总工资中减去请假的天数
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
		        				 * 得到该员工的所有的浮动款项----->初次，因为管理员没有设置浮动款项，所以不用计算
		        				 */
			        		shouldPaySalDouble = shouldPaySalDouble -attendanceDeduct*100;
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
	        					whpActualSal = actualSal;
	        					//计算企业要缴纳社保和公积金的扣款额
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
	        					//计算企业要为员工缴纳社保和公积金的扣款额
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
	        				actualSal=actualSal-replaceDeduct*100;
	        				/**
	        				 * 判断该员工是否参与个税
	        				 */		   
	        				Boolean  attenTax =true;
	        				if(attenPersonTax == null||attenPersonTax==0){
	        					attenTax = false;
	        				}
	        				/**
	        				 * 计算员工的个人所得税
	        				 */
	        				if(attenTax){//若员工要参与个人所得税
	        					taxReplace = PersonalTaxUtils.getPersonalTax(actualSal/100);
	        				}
	        				replaceDeduct +=taxReplace;
	        				actualSal=actualSal-taxReplace*100;        				
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
        				/**
        				 * 更新员工的应发工资和实发工资,扣款和代缴
        				 */
        				 salStaffSalReportEntity = new SalStaffSalReportEntity();
        				 
        				salStaffSalReportEntity.setId(IdGenerator.newId());
        				salStaffSalReportEntity.setActualSal(actualSal);
        				salStaffSalReportEntity.setShouldPaySal(shouldPaySalDouble);
        				salStaffSalReportEntity.setDingStaffId(staffId);
        				salStaffSalReportEntity.setCreateDate(nowDate);
        				salStaffSalReportEntity.setStaffInsuranceSal(staffInsuranceSal*100);
        				salStaffSalReportEntity.setTaxSal(taxReplace*100);
        				salStaffSalReportEntity.setAttenDeduct(attendanceDeduct*100+frontAttenDeduct);
        				salStaffSalReportEntity.setModifiedDate(nowDate);
        				salStaffSalReportEntity.setSalReportId(salReportId);
        				salStaffSalReportEntity.setMonthTime(monthTime);
        				salStaffSalReportEntity.setCorpId(corpId);
        				corpStaffSocialSal +=replaceDeduct;
        				salStaffSalReportEntity.setCorpInsuranceSal(corpStaffSocialSal*100);
        				salStaffSalReportEntity.setSalDeduct(attendanceDeduct*100+frontAttenDeduct);
        				salStaffSalReportEntity.setReplaceDeduct(replaceDeduct*100);
        				salSynCorpInfo.addStaffMonthSal(salStaffSalReportEntity);  			
        				
        				corpShoudPaySal +=shouldPaySalDouble;
        				corpActualPaySal +=actualSal;				
        				corpSocialPaySal +=corpStaffSocialSal*100;			
        			}    			
        		}
        		/**
        		 * 计算企业当月的总工资
        		 */
				corpStaffPay =corpSocialPaySal+corpShoudPaySal;
				salReportEntity.setStaffCost((double)Math.round(corpStaffPay/100)*100);
				salReportEntity.setInsuranceSal((double)Math.round(corpSocialPaySal/100)*100);
				salReportEntity.setActualPaySal((double)Math.round(corpActualPaySal/100)*100);
				salReportEntity.setShouldPaySal((double)Math.round(corpShoudPaySal/100)*100);
				salReportEntity.setModifiedDate(new Date());
				salReportRepository.updateSalReport(salReportEntity);
        	}
        	if(initNum>0){
        		salReportModels=salReportRepository.getAllSalReports(corpId);//重新去数据库抓取数据
        	}
        }
		return salReportModels;
	}

	@Override
	public PageData<SalStaffSalReportModel> getSalDatailByReportId(String reportId , String corpId , Pageable pageable) {
		if (StringUtils.isBlank(reportId)) {
			throw new IllegalArgumentException("getSalaryDatailByMonthId param reportId is null or empty.");
		}
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getSalaryDatailByMonthId param corpId is null or empty.");
		}
		if (pageable == null) {
			throw new IllegalArgumentException("getSalaryDatailByMonthId param pageable  is null or empty.");
		}
		Integer pageSize = pageable.getPageSize();
		Integer beginNum = (pageable.getPageNo()-1)*pageSize;
		List<SalStaffSalReportModel> monthStaffSalModels = salReportRepository.getSalDatailByReportId(reportId,corpId,beginNum,pageSize);
		/**
		 *  拼接员工的所有部门
		 */
		for(SalStaffSalReportModel salReportModel:monthStaffSalModels){
			salReportModel.setDeptName(salBaseService.getStaffAllDeptName(corpId, salReportModel.getDingStaffId()));//拼接员工所有的部门 
		}
		
		Integer  count = salReportRepository.getSalDatailCountByReportId(reportId,corpId);//计算总数
		pageable.setTotalCount(count);
	
		return new PageDataImpl<SalStaffSalReportModel>(monthStaffSalModels , pageable);
	}


	@Override
	public Boolean updateSalsOnStaffes(String corpId,List<SalStaffSalReportDetailModel> monthStaffSalDetailModels) {
		if (monthStaffSalDetailModels == null) {
			throw new IllegalArgumentException("updateSalariesOnStaffes param monthStaffSalDetailModels is null");
		}
		if (monthStaffSalDetailModels.isEmpty()) {
			throw new IllegalArgumentException("updateSalariesOnStaffes param monthStaffSalDetailModels is empty.");
		}
		int size = 0;
		SalStaffSalReportDetailModel monthStaffSalDetailModelUseUpdate= new SalStaffSalReportDetailModel();	
		/**
		 * 循环得到员工的浮动工资数据，并将结果的数字*100
		 */
		for (SalStaffSalReportDetailModel monthStaffSalDetailModel : monthStaffSalDetailModels) {
			SalStaffSalReportEntity salStaffSalReportEntity = salReportRepository
					.getSalDetailEntityById(monthStaffSalDetailModel.getId());
			if (salStaffSalReportEntity != null) {				
				monthStaffSalDetailModelUseUpdate.setId(monthStaffSalDetailModel.getId());
				Double annusBonus = monthStaffSalDetailModel.getAnnualBonus();
				if(annusBonus!=null){
					annusBonus=annusBonus*100;
				}
				monthStaffSalDetailModelUseUpdate.setAnnualBonus(annusBonus);
				Double monthBonus = monthStaffSalDetailModel.getMonthBonus();
				if(monthBonus!=null){
					monthBonus = monthBonus*100;
				}
				monthStaffSalDetailModelUseUpdate.setMonthBonus(monthBonus);
				Double otherPrextaxDeduct = monthStaffSalDetailModel.getOtherPretaxDeduct();
				if(otherPrextaxDeduct!=null){
					otherPrextaxDeduct=otherPrextaxDeduct*100;
				}
				monthStaffSalDetailModelUseUpdate.setOtherPretaxDeduct(otherPrextaxDeduct);
				Double otherAftertaxDeduct =monthStaffSalDetailModel.getOtherAftertaxDeduct(); 
				if(otherAftertaxDeduct!=null){
					otherAftertaxDeduct=otherAftertaxDeduct*100;
				}
				monthStaffSalDetailModelUseUpdate.setOtherAftertaxDeduct(otherAftertaxDeduct);
				Double otherPretaxSal = monthStaffSalDetailModel.getOtherPretaxSal();
				if(otherPretaxSal!=null){
					otherPretaxSal=otherPretaxSal*100;
				}
				monthStaffSalDetailModelUseUpdate.setOtherPretaxSal(otherPretaxSal);
				Double otherAftertaxSal = monthStaffSalDetailModel.getOtherAftertaxSal();
				if(otherAftertaxSal!=null){
					otherAftertaxSal = otherAftertaxSal*100;
				}
				monthStaffSalDetailModelUseUpdate.setOtherAftertaxSal(otherAftertaxSal);
				monthStaffSalDetailModelUseUpdate.setModifiedDate(new Date());
			}
			if (salReportRepository.updateStaffSalOnMonth(monthStaffSalDetailModelUseUpdate) > 0) {
				size++;
			}
		}
		if (size == monthStaffSalDetailModels.size()) {			
			/**
			 * 重新计算企业员工的工资和薪资报表的总工资
			 */		
			saCalcuSalService.calcuSalReportAutomatic(corpId);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean lockSalReportById(String reportId, Short reportState) {
		if (StringUtils.isBlank(reportId)) {
			throw new IllegalArgumentException("lockSalReportById param reportId is null or empty.");
		}
		if (reportState == null) {
			throw new IllegalArgumentException("lockSalReportById param reportState is null or empty.");
		}
		Integer rs = null;
		rs = salReportRepository.lockSalReportById(reportId, reportState);

		return rs > 0;
	}


	@Override
	public List<SalCorpReportModel> getUserSalDates(String corpId, String staffId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("getUserSalDates param corpId is null or empty.");
		}
		if(StringUtils.isBlank(staffId)){
			throw new IllegalArgumentException("getUserSalDates param staffId is null or empty.");
		}
		List<SalCorpReportModel> salReportModels = salReportRepository.getUserSalDates(corpId, staffId);
		
		return salReportModels;
	}

	@Override
	public SalStaffMbSalInfoModel getStaffMbSalInfo(String corpId , String staffId, String reportId) {
		if(StringUtils.isBlank(staffId)){
			throw new IllegalArgumentException("getStaffMbSalInfo param staffId is null or empty.");
		}
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("getStaffMbSalInfo param corpId is null or empty.");
		}
		if(StringUtils.isBlank(reportId)){
			reportId =null;
		}
		SalStaffMbSalInfoModel salStaffMbSalInfo = salReportRepository.getStaffMbSalInfo(staffId,reportId,corpId);
		if(salStaffMbSalInfo!=null){
		  salStaffMbSalInfo.setDeptName(salBaseService.getStaffAllDeptName(corpId, staffId));
		}
		if(salStaffMbSalInfo.getDj()!=null){
			salStaffMbSalInfo.setDj(salStaffMbSalInfo.getDj()/100);
		}
		if(salStaffMbSalInfo.getKk()!=null){
			salStaffMbSalInfo.setKk(salStaffMbSalInfo.getKk()/100);
		}
		if(salStaffMbSalInfo.getSfgz()!=null){
			salStaffMbSalInfo.setSfgz(salStaffMbSalInfo.getSfgz()/100);
		}
		if(salStaffMbSalInfo.getYfgz()!=null){
			salStaffMbSalInfo.setYfgz(salStaffMbSalInfo.getYfgz()/100);
		}
		return salStaffMbSalInfo;
	}

	@Override
	public SalCorpReportModel getSalReportByReportId(String reportId) {
	    if(StringUtils.isBlank(reportId)){
	    	throw new IllegalArgumentException("getSalReportByReportId param reportId is null or empty.");
	    }
		SalCorpReportModel  salReportModel= salReportRepository.getSalReportByReportId(reportId);
		return salReportModel;
	}

	@Override
	public List<SalStaffSalReportDetailModel> getSelectStaffFloatSalData(List<SalStaffBaseInfoModel> staffs , String corpId , String reportId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("getSelectStaffFloatSalData param corpId is null or empty.");
		}
		if(StringUtils.isBlank(reportId)){
			throw new IllegalArgumentException("getSelectStaffFloatSalData param reportId is null or empty.");
		}
		if(staffs == null){
			throw new IllegalArgumentException("getSelectStaffFloatSalData param staffs is null.");
		}
		List<SalStaffSalReportDetailModel> salStaffs = new ArrayList<SalStaffSalReportDetailModel>();//用于存储员工的浮动薪资数据	
		String id=null;
		Set<String> unRepeatSet = new HashSet<String>();
		for(SalStaffBaseInfoModel model:staffs){//循环得到部门或员工的信息
			id = model.getId();
			if(model.getType() == SalStaffDeptType.DEPT.getCode()){//如果ID的类型是部门
				 //找到部门下的所有子部门
				List<String> deptIds = salStaffBaseInfoRepository.getDeptInfoByParentId(corpId, id);
				//找到子部门下的所有人员
				List<SalStaffSalReportDetailModel>  salStaffs2= salReportRepository.getDeptAllStaffFloatSal(deptIds,reportId,corpId);
				for(SalStaffSalReportDetailModel sal:salStaffs2){
					if(!unRepeatSet.contains(sal.getId())){//过滤掉存在重复的员工浮动工资数据
					    salStaffs.add(sal);
					    unRepeatSet.add(sal.getId());
					}
				}
			}else if(model.getType() == SalStaffDeptType.STAFF.getCode()){//如果ID的类型是员工
				//直接得到员工的信息
				SalStaffSalReportDetailModel  selectedStaffSalDetaiModel= salReportRepository.getSelectedStaffSalDetail(id, reportId, corpId);
				if(selectedStaffSalDetaiModel!=null){
					if(!unRepeatSet.contains(selectedStaffSalDetaiModel.getId())){//过滤掉存在重复的员工浮动工资数据
						salStaffs.add(selectedStaffSalDetaiModel);
						unRepeatSet.add(selectedStaffSalDetaiModel.getId());
					}
				
				}
			}
		}
		/**
		 * 拼接该人员的所有部门名字
		 */
		for(SalStaffSalReportDetailModel  salStaff:salStaffs){
			salStaff.setDeptName(salBaseService.getStaffAllDeptName(corpId, salStaff.getDingStaffId()));
		}
		return salStaffs;
	}

	@Override
	public HSSFWorkbook exportToExcel(String corpId ,String reportId) {
		 if(StringUtils.isBlank(reportId)){
		    	throw new IllegalArgumentException("exportToExcel param reportId is null or empty.");
		 }
		 if(StringUtils.isBlank(corpId)){
		       throw new IllegalArgumentException("exportToExcel param corpId is null or empty.");
		 }
		 String staffId = null;
		 HSSFWorkbook wb = null;
		 List<SalStaffSalReportModel>  salStaffReports = salReportRepository.getSalDatailByReportIdNotPage(reportId, corpId);//查询出企业员工当月的所有工资数据
	 	 for(SalStaffSalReportModel salStaffSalReportModel:salStaffReports){//拼接员工所有的部门
			staffId=salStaffSalReportModel.getDingStaffId();
			salStaffSalReportModel.setDeptName(salBaseService.getStaffAllDeptName(corpId, staffId));
		 }  
	 	 SalCorpInfoModel salCorpInfoModel=salStaffBaseInfoRepository.getCorpBsInfo(corpId);
	 	 String corpName=null;
	 	 if(salCorpInfoModel!=null){
	 		corpName = salCorpInfoModel.getCorpName();
	 	 }
	 	SalCorpReportModel salCorpReportModel= salReportRepository.getSalReportByReportId(reportId);
	 	Double corpActualSal = salCorpReportModel.getActualPaySal();//公司实发工资
	 	Double corpShouldPaySal = salCorpReportModel.getShouldPaySal();//公司应发工资
	 	Double corpStaffCost=salCorpReportModel.getStaffCost();//公司员工成本
	 	Double corpInsuranceSal=salCorpReportModel.getInsuranceSal();//公司缴金
	 	try {
			InputStream instream = ClassLoader.getSystemResourceAsStream("SalExport.xls");
			POIFSFileSystem fs=new POIFSFileSystem(instream);
			
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			int size = salStaffReports.size();
			HSSFRow row = sheet.getRow(0);
			HSSFCell cell = row.getCell(0);
			/**
			 * 模板设置为当前月
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
			cell.setCellValue(corpName+sdf.format(salCorpReportModel.getMonthTime())+"工资报表");  	 	
			/**
			 * 设置当月企业的薪资明细
			 */
			 row = sheet.getRow(2);
			 cell = row.getCell(0);
			 cell.setCellValue("应发工资：".concat(""+corpShouldPaySal/100).concat("    实发工资：").concat(""+corpActualSal/100).concat("    公司交金：") .concat(""+corpInsuranceSal/100).concat("    员工成本：").concat(""+corpStaffCost/100));
			//填充员工信息
			HSSFRow userRow= null;
			SalStaffSalReportModel salStaffSalReportModel = null;
			SalStaffBaseInfoModel salStaffBaseInfoModel = null;


			for(int i=0;i<size;i++){
				userRow= sheet.getRow(i+6);
				if(userRow==null){
					userRow = sheet.createRow(i+6);
				}
				salStaffSalReportModel = salStaffReports.get(i);
				/**
				 *  钉钉ID
				 */
				cell=userRow.getCell(0);
				if(cell==null){
					cell = userRow.createCell(0);
				}
				cell.setCellValue(salStaffSalReportModel.getDingStaffId());
				/**
				 * 部门ID
				 */
				cell=userRow.getCell(1);
				if(cell==null){
					cell = userRow.createCell(1);
				}
				cell.setCellValue(salStaffSalReportModel.getDeptName());
				/**
				 * 用户名
				 */
				cell=userRow.getCell(2);
				if(cell==null){
					cell = userRow.createCell(2);
				}
				cell.setCellValue(salStaffSalReportModel.getStaffName());
				/**
				 * 工号
				 */
				cell=userRow.getCell(3);
				if(cell==null){
					cell = userRow.createCell(3);
				}
				cell.setCellValue(salStaffSalReportModel.getJobNum());
				/**
				 * 应发工资
				 */
				cell=userRow.getCell(4);
				if(cell==null){
					cell = userRow.createCell(4);
				}
				cell.setCellValue(salStaffSalReportModel.getShouldPaySal()/100);
				/**
				 * 扣款
				 */
				cell=userRow.getCell(5);
				if(cell==null){
					cell = userRow.createCell(5);
				}
				cell.setCellValue(salStaffSalReportModel.getSalDeduct()/100);
				/**
				 * 代缴
				 */
				cell=userRow.getCell(6);
				if(cell==null){
					cell = userRow.createCell(6);
				}
				cell.setCellValue(salStaffSalReportModel.getReplaceDeduct()/100);
				/**
				 * 实发
				 */
				cell=userRow.getCell(7);
				if(cell==null){
					cell = userRow.createCell(7);
				}
				cell.setCellValue(salStaffSalReportModel.getActualSal()/100);
				/**
				 * 基本工资
				 */
				salStaffBaseInfoModel = salStaffBaseInfoRepository.getStaffBaseInfo(corpId, staffId);
				if(salStaffBaseInfoModel !=null){
					cell=userRow.getCell(8);
					if(cell==null){
						cell = userRow.createCell(8);
					}
					cell.setCellValue(salStaffBaseInfoModel.getShouldPaySal()/100);
				}
	
				/**
				 * 考勤扣款
				 */
				cell=userRow.getCell(9);
				if(cell==null){
					cell = userRow.createCell(9);
				}
				cell.setCellValue(salStaffSalReportModel.getAttenDeduct()/100);
				/**
				 * 社保公积金扣款
				 */
				cell=userRow.getCell(10);
				if(cell==null){
					cell = userRow.createCell(10);
				}
				cell.setCellValue(salStaffSalReportModel.getStaffInsuranceSal()/100);
				
				/**
				 * 个人所得税扣款
				 */
				cell=userRow.getCell(11);
				if(cell==null){
					cell = userRow.createCell(11);
				}
				cell.setCellValue(salStaffSalReportModel.getTaxSal()/100);
				/**
				 * 月度奖金
				 */
				cell=userRow.getCell(12);
				if(cell==null){
					cell = userRow.createCell(12);
				}
				cell.setCellValue(salStaffSalReportModel.getMonthBonus()/100);
				/**
				 * 年终奖金
				 */
				cell=userRow.getCell(13);
				if(cell==null){
					cell = userRow.createCell(13);
				}
				cell.setCellValue(salStaffSalReportModel.getAnnualBonus()/100);
				/**
				 * 其他税前补款
				 */
				cell=userRow.getCell(14);
				if(cell==null){
					cell = userRow.createCell(14);
				}
				cell.setCellValue(salStaffSalReportModel.getOtherPretaxSal()/100);
				/**
				 * 其他税前扣款
				 */
				cell=userRow.getCell(15);
				if(cell==null){
					cell = userRow.createCell(15);
				}
				cell.setCellValue(salStaffSalReportModel.getOtherPretaxDeduct()/100);
				/**
				 * 其他税后补款
				 */
				cell=userRow.getCell(16);
				if(cell==null){
					cell = userRow.createCell(16);
				}
				cell.setCellValue(salStaffSalReportModel.getOtherAftertaxSal()/100);
				/**
				 * 其他税后扣款 
				 */
				cell=userRow.getCell(17);
				if(cell==null){
					cell = userRow.createCell(17);
				}
				cell.setCellValue(salStaffSalReportModel.getOtherAftertaxDeduct()/100);
				/**
				 * 公司缴险与公积金
				 */
				cell=userRow.getCell(18);
				if(cell==null){
					cell = userRow.createCell(18);
				}
				cell.setCellValue((salStaffSalReportModel.getCorpInsuranceSal()-salStaffSalReportModel.getTaxSal()-salStaffSalReportModel.getStaffInsuranceSal())/100);
			}
	 	   }catch(Exception e){
	 		  logger.error("exportToExcel --".concat(corpId).concat("在解析工资报表ID:").concat(reportId)+"产生异常",e);
	 	  }
		return wb;
	}

}
