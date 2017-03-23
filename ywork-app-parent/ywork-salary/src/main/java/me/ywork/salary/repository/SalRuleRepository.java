package me.ywork.salary.repository;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.salary.entity.SalCorpBaseRuleEntity;
import me.ywork.salary.entity.SalCorpWhpRuleEntity;
import me.ywork.salary.model.SalCorpWhpRuleModel;
import me.ywork.salary.model.SalCorpBaseSalRuleModel;
import me.ywork.salary.model.SalCorpDeductModel;

/**
 * 薪资规则的数据访问层
 * @author xiaobai
 *
 */
@Repository
public interface SalRuleRepository extends IRepository<SalCorpBaseRuleEntity> {
	/**
	 * 获取规则列表:获取算工资应用的所有的记薪规则
	 * 
	 * @param corpId 钉钉企业号
	 * @return 得到的所有的薪资规则
	 */
	List<SalCorpBaseSalRuleModel> getAllSalRules(@Param("corpId") String corpId);



	/**
	 * 根据企业计薪规则ID得到企业的计薪规则
	 * 
	 * @param corpId 钉钉企业ID
	 * @param ruleId  企业薪资规则的ID
	 * @return   基本薪资规则的数据包
	 */
	SalCorpBaseSalRuleModel getCorpSalRuleByRuleId(@Param("corpId") String corpId , @Param("ruleId") String ruleId);

	/**
	 * 保存计薪规则
	 * 
	 * @param salaryRule    基本薪资规则的数据包
	 * @return 保存薪资规则的结果
	 */
	Integer updateSalRule( SalCorpBaseSalRuleModel salaryRule);
	
	/**
	 * 更新企业代缴代扣规则
	 * 
	 * @param salCwhpRuleModel 代缴代扣的数据包
	 * @return  更新的结果
	 */
	 Integer updateCwhpRule(SalCorpWhpRuleModel  salCwhpRuleModels);
	 
	 /**
	  * 得到代缴代扣的规则
	  * 
	  * @param id 代缴代扣薪资规则的主键
	  * @return  代缴代扣的数据包
	  */
	 SalCorpWhpRuleEntity getCwhpRuleEntity(@Param("id")String id);
	 
	/**
	 *   得到企业代缴代扣规则
	 *   
	 * @param corpId  钉钉企业ID
	 * @return  得到企业代缴代扣的列表
	 */
	List<SalCorpWhpRuleModel> getCwhpRuleList(@Param("corpId") String corpId);
	
	/**
	 *  得到企业扣款的规则列表
	 * 
	 * @param corpId 钉钉企业ID
	 * @return  考勤扣款的数据包
	 */
	SalCorpDeductModel getCorpdeductRule(@Param("corpId") String corpId);

	/**
	 * 更新企业扣款的规则
	 * 
	 * @param salCorpDeduct 企业扣款的规则的数据包
	 * @return  更新的结果
	 */
	Integer updateCorpDeductRule(SalCorpDeductModel salCorpDeduct);
	
	/**
	 * 得到该企业的基本薪资规则的ID是正算还是反算的值
	 * 
	 * @param corpBsRuleId  基本薪资规则的ID
	 * @return     正算还是反算
	 */
	Short getRuleTypeByCorpBsRuleId(@Param("id")String corpBsRuleId);
	
	/**
	 * 得到企业该规则下的适用人数
	 * 
	 * @param corpId  钉钉企业ID
	 * @param bsRuleId  基本薪资规则的ID
	 * @param type  规则的类型
	 * @return  规则下适用的人数
	 */
	Integer getCorpRuleFitNum(@Param("corpId")String corpId,@Param("bsRuleId")String bsRuleId,@Param("type")Short type);
}
