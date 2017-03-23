package me.ywork.salary.service;

import me.ywork.base.service.BizService;
import me.ywork.salary.model.SalCorpWhpRuleModel;
import me.ywork.salary.model.SalCorpBaseSalRuleModel;
import me.ywork.salary.model.SalUpdateStaffesModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;

import java.util.List;

/**
  * 企业薪资规则的业务管理
 */
public interface SalRuleService extends BizService {
		/**
		 * 获取规则列表:获取算工资应用的所有的记薪规则
		 * 
		 * @param corpId 钉钉企业号
		 * @return 得到的所有的薪资规则
		 */
		List<SalCorpBaseSalRuleModel> getAllSalRules(String corpId);
	
		/**
		 * 获取规则详情
		 * 
		 * @param corpId 钉钉企业标识
		 * @param ruleId  薪资规则的ID
		 * @return 获取薪资规则的结果
		 */
		SalCorpBaseSalRuleModel getSalRuleByRuleId(String corpId , String ruleId);
	
		/**
		 * 保存计薪规则
		 * 
		 * @param salaryRule  存储企业基本薪资规则的数据包
		 * @param corpId 钉钉企业ID
		 * @return 保存薪资规则的结果
		 */
		Boolean updateSalRule(String corpId , SalCorpBaseSalRuleModel salaryRule);

		
		/**
		 * 更新薪资规则下匹配的人员
		 * 
		 * @param salUpdateStaffesModel 存储要更新企业薪资规则的数据包
		 * @param corpId 钉钉企业ID
		 * @return 更新的结果
		 */
		Boolean updateSalRuleStaffies(SalUpdateStaffesModel salUpdateStaffesModel,String corpId);
		
		/**
		 * 删除企业员工的基本薪资规则
		 * 
		 * @param staffBaseInfoModels  存储要删除员工薪资规则的数据包
		 * @param   钉钉企业ID
		 * @return   删除的结果
		 */
		Boolean deleteStaffSalRule(String corpId ,SalStaffBaseInfoModel salStaffBaseInfoModel);
		
		/**
		 * 更新企业代缴代扣规则
		 * 
		 * @param salCwhpRuleModels 代缴代扣列表
		 * @param 钉钉企业ID
		 * @return  更新的结果
		 */
		Boolean updateCwhpRuleList(String corpId ,List<SalCorpWhpRuleModel>  salCwhpRuleModels);
		
		/**
		 *   得到企业代缴代扣规则
		 *   
		 * @param corpId  钉钉企业ID
		 * @return  得到企业代缴代扣的列表
		 */
		List<SalCorpWhpRuleModel> getCwhpRuleList(String corpId);
		
		/**
		 * 得到地址本选中的人员或者部门的数据集合
		 * 
		 * @param corpId 钉钉企业ID
		 * @param ruleId 基本薪资规则的ID
		 * @param type 要代缴代扣或个人所得税的类型
		 * @return   得到地址本选中的人员或者部门的数据集合
		 */
		List<SalStaffBaseInfoModel> getStaffAddress(String corpId , String ruleId ,Short type);
}
