package me.ywork.salary.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.heyi.utils.IdGenerator;

import me.ywork.context.CallContext;
import me.ywork.salary.entity.SalCorpBaseRuleEntity;
import me.ywork.salary.entity.SalCorpDeductEntity;
import me.ywork.salary.entity.SalCorpInfoEntity;
import me.ywork.salary.entity.SalCorpWhpRuleEntity;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.enumeration.SalLackDeductType;
import me.ywork.salary.enumeration.SalLateEarlyDeductType;
import me.ywork.salary.enumeration.SalStateType;
import me.ywork.salary.enumeration.SalStayAwayDeductType;
import me.ywork.salary.repository.SalSynCorpInfoRepository;
import me.ywork.salary.service.SalSynService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalSynServiceImpl  implements SalSynService{
	
	@Autowired
	private SalSynCorpInfoRepository salSynCorpInfo;

	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void synchCopInfo(List<String> corpList) {
		if(corpList == null || corpList.isEmpty()){
			throw new IllegalArgumentException("getCorpPasswordOpenStatus param corpId is null or empty");
		}
		SalCorpInfoEntity salCorpBaseInfo = null;
		SalCorpWhpRuleEntity salCorpWhpRule = null;
		SalCorpDeductEntity salCorpDeductEntity = null;
		SalCorpBaseRuleEntity salCorpBaseRuleEntity=null;
		Date nowDate = new Date();
		for(String corpId:corpList){			
			//根据企业的标识去查找员工的信息
			List<SalStaffBaseInfoEntity> staffBaseInfos = salSynCorpInfo.getCorpBaseInfoFromElement(corpId);
            //插入企业的基本信息
			salCorpBaseInfo =new SalCorpInfoEntity();
			salCorpBaseInfo.setCorpId(corpId);
			salCorpBaseInfo.setCreateDate(nowDate);
			salCorpBaseInfo.setModifiedDate(nowDate);
			salCorpBaseInfo.setId(IdGenerator.newId());
			salCorpBaseInfo.setPassState(SalStateType.CLOSE.getCode());
			salSynCorpInfo.synchCorpBaseInfo(salCorpBaseInfo);
			//插入员工的基本信息
			for(SalStaffBaseInfoEntity salStaffBaseInfoEntity:staffBaseInfos){
				salStaffBaseInfoEntity.setAttenPersonalTax(0);
				salStaffBaseInfoEntity.setAttenSocial(0);
				salStaffBaseInfoEntity.setCorpId(corpId);
				salStaffBaseInfoEntity.setCreateDate(nowDate);
				salStaffBaseInfoEntity.setModifiedDate(nowDate);
				salStaffBaseInfoEntity.setPassState(SalStateType.CLOSE.getCode());
				salStaffBaseInfoEntity.setId(IdGenerator.newId());
				salSynCorpInfo.synchStaffBaseInfo(salStaffBaseInfoEntity);
			}
			//插入企业的社保公积金的扣款信息
			salCorpWhpRule = new SalCorpWhpRuleEntity();
//			salCorpWhpRule.setBaseHigh(8);
//			salCorpWhpRule.setBaseLow(8);
//			salCorpWhpRule.setCorpId(corpId);
//			salCorpWhpRule.setCorpPercent(8);
//			salCorpWhpRule.setModifiedDate(nowDate);
//			salCorpWhpRule.setCreateDate(nowDate);
//			salCorpWhpRule.setId(IdGenerator.newId());
//			salCorpWhpRule.setSubjectId("endowment_insurance");
//			salSynCorpInfo.synchCorpWhpRule(salCorpWhpRule);
//			
//			salCorpWhpRule = new SalCorpWhpRuleEntity();
//			salCorpWhpRule.setBaseHigh(8);
//			salCorpWhpRule.setBaseLow(8);
//			salCorpWhpRule.setCorpId(corpId);
//			salCorpWhpRule.setCorpPercent(8);
//			salCorpWhpRule.setModifiedDate(nowDate);
//			salCorpWhpRule.setCreateDate(nowDate);
//			salCorpWhpRule.setId(IdGenerator.newId());
//			salCorpWhpRule.setSubjectId("industrial_injury_insurance");
//			salSynCorpInfo.synchCorpWhpRule(salCorpWhpRule);
//			
//			salCorpWhpRule = new SalCorpWhpRuleEntity();
//			salCorpWhpRule.setBaseHigh(8);
//			salCorpWhpRule.setBaseLow(8);
//			salCorpWhpRule.setCorpId(corpId);
//			salCorpWhpRule.setCorpPercent(8);
//			salCorpWhpRule.setModifiedDate(nowDate);
//			salCorpWhpRule.setCreateDate(nowDate);
//			salCorpWhpRule.setId(IdGenerator.newId());
//			salCorpWhpRule.setSubjectId("maternity insurance");
//			salSynCorpInfo.synchCorpWhpRule(salCorpWhpRule);
//			
//			salCorpWhpRule = new SalCorpWhpRuleEntity();
//			salCorpWhpRule.setBaseHigh(8);
//			salCorpWhpRule.setBaseLow(8);
//			salCorpWhpRule.setCorpId(corpId);
//			salCorpWhpRule.setCorpPercent(8);
//			salCorpWhpRule.setModifiedDate(nowDate);
//			salCorpWhpRule.setCreateDate(nowDate);
//			salCorpWhpRule.setId(IdGenerator.newId());
//			salCorpWhpRule.setSubjectId("medical_insurance");
//			salSynCorpInfo.synchCorpWhpRule(salCorpWhpRule);
//			
//			salCorpWhpRule = new SalCorpWhpRuleEntity();
//			salCorpWhpRule.setBaseHigh(8);
//			salCorpWhpRule.setBaseLow(8);
//			salCorpWhpRule.setCorpId(corpId);
//			salCorpWhpRule.setCorpPercent(8);
//			salCorpWhpRule.setModifiedDate(nowDate);
//			salCorpWhpRule.setCreateDate(nowDate);
//			salCorpWhpRule.setId(IdGenerator.newId());
//			salCorpWhpRule.setSubjectId("pub_funds");
//			salSynCorpInfo.synchCorpWhpRule(salCorpWhpRule);
//			
//			salCorpWhpRule = new SalCorpWhpRuleEntity();
//			salCorpWhpRule.setBaseHigh(8);
//			salCorpWhpRule.setBaseLow(8);
//			salCorpWhpRule.setCorpId(corpId);
//			salCorpWhpRule.setCorpPercent(8);
//			salCorpWhpRule.setModifiedDate(nowDate);
//			salCorpWhpRule.setCreateDate(nowDate);
//			salCorpWhpRule.setId(IdGenerator.newId());
//			salCorpWhpRule.setSubjectId("serious_illness_insurance");
//			salSynCorpInfo.synchCorpWhpRule(salCorpWhpRule);
//			
//			salCorpWhpRule = new SalCorpWhpRuleEntity();
//			salCorpWhpRule.setBaseHigh(8);
//			salCorpWhpRule.setBaseLow(8);
//			salCorpWhpRule.setCorpId(corpId);
//			salCorpWhpRule.setCorpPercent(8);
//			salCorpWhpRule.setModifiedDate(nowDate);
//			salCorpWhpRule.setCreateDate(nowDate);
//			salCorpWhpRule.setId(IdGenerator.newId());
//			salCorpWhpRule.setSubjectId("unemploy_insurance");
//			salSynCorpInfo.synchCorpWhpRule(salCorpWhpRule);
			
			/**
			 * 插入企业的考勤扣款信息
			 */
			
			salCorpDeductEntity =new SalCorpDeductEntity();
			salCorpDeductEntity.setModifiedDate(nowDate);
			salCorpDeductEntity.setCreateDate(nowDate);
			salCorpDeductEntity.setId(IdGenerator.newId());
			salCorpDeductEntity.setCorpId(corpId);
			salCorpDeductEntity.setLackDeduct(0.0);
			salCorpDeductEntity.setLackDeductType(SalLackDeductType.PERCENT.getCode());
			salCorpDeductEntity.setLateEarlyDeduct(0.0);
			salCorpDeductEntity.setLateEarlyDeductType(SalLateEarlyDeductType.Duration.getCode());
			salCorpDeductEntity.setSeriousLateDeduct(0.0);
			salCorpDeductEntity.setStayAwayDeduct(0.0);
			salCorpDeductEntity.setStayAwayDeductType(SalStayAwayDeductType.PERCENT.getCode());
			salSynCorpInfo.synchCorpDeductRule(salCorpDeductEntity);
			//增加企业的基本薪资规则配置信息
			salCorpBaseRuleEntity = new SalCorpBaseRuleEntity();
			salCorpBaseRuleEntity.setId(IdGenerator.newId());
			salCorpBaseRuleEntity.setCalSalDays(22.25);
			Integer fitNums =0;
			salCorpBaseRuleEntity.setFitNums(fitNums);
			salCorpBaseRuleEntity.setSalRuleId("sys_front_ruleid");
			salCorpBaseRuleEntity.setCreateDate(nowDate);
			salCorpBaseRuleEntity.setModifiedDate(nowDate);
			salCorpBaseRuleEntity.setCorpId(corpId);
			salSynCorpInfo.synchCorpBaseRule(salCorpBaseRuleEntity);
			
			salCorpBaseRuleEntity = new SalCorpBaseRuleEntity();
			salCorpBaseRuleEntity.setId(IdGenerator.newId());
			salCorpBaseRuleEntity.setCalSalDays(22.25);
			salCorpBaseRuleEntity.setFitNums(fitNums);
			salCorpBaseRuleEntity.setSalRuleId("sys_inverse_ruleid");
			salCorpBaseRuleEntity.setCreateDate(nowDate);
			salCorpBaseRuleEntity.setModifiedDate(nowDate);
			salCorpBaseRuleEntity.setCorpId(corpId);
			salSynCorpInfo.synchCorpBaseRule(salCorpBaseRuleEntity);
			
		}
		
		/**
		 *  在导入员工的具体薪资信息，并指定具体的薪资规则之后，
		 *  再生成：dingsal_corp_salreport 企业月度薪资报表
		 *  再生成：dingsal_staff_monthsal  企业员工的月度薪资具体信息(包括浮动条款)
		 *  再生成：
		 */
		
		/**
		 * 系统建立的时候
		 * 自动生成：dingsal_sys_fielditem (养老、生育、医疗、公积金、大病医疗以及社保等系统字段)
		 * 自定生成：dingsal_sys_salrule(简单计算--正选和反选以及代缴代扣的信息)
		 * 自动生成：
		 */
		
		/**
		 * 当考勤信息导入的时候：
		 * 自动生成：dingsal_corp_attenreport 企业月度考勤报表
		 * 自动生成：dingsal_staff_attendance  企业员工的考勤信息
		 *  自动生成：dingsal_sys_fielditem 企业请假类型的字段（如病假、事假、婚嫁类型的字段）
		 *  自动生成：dingsal_staff_attenday 企业员工对应请假类型字段下的具体天数
		 */
		
		/**
		 * 当员工具体的薪资信息被导入的时候
		 * 自动生成:dingsal_sys_fielditem中员工的具体的薪资字段
		 * 自动计算：员工的总工资
		 * 判断在当月中若薪资规则创立了，有没有生成月度工资报表,若没有，则自动生成当月的工资报表,及每一个员工的工资信息
		 */
		
	}
	
	

}
