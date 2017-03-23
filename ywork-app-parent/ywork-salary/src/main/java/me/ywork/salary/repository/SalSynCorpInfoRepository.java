package me.ywork.salary.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.salary.entity.SalCorpBaseRuleEntity;
import me.ywork.salary.entity.SalCorpDeductEntity;
import me.ywork.salary.entity.SalCorpInfoEntity;
import me.ywork.salary.entity.SalCorpWhpRuleEntity;
import me.ywork.salary.entity.SalReportEntity;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.entity.SalStaffSalReportEntity;

/**
 * 同步企业基本信息的数据返回层
 * 
 * @author xiaobai
 *
 */
@Repository
public interface SalSynCorpInfoRepository extends IRepository<SalCorpInfoEntity> {
	
    /**
     * 查找企业员工的基本信息
	 * 
	 * @param corpId 钉钉企业ID
	 * @return 企业员工基本信息的集合
	 */
	List<SalStaffBaseInfoEntity> getCorpBaseInfoFromElement(@Param("corpId")String corpId);
	
	/**
	 * 增加企业社保公积金的规则
	 * 
	 * @param salCorpWhpRule  企业社保公积金的规则实体
	 * @return   增加的结果
	 */
	Integer synchCorpWhpRule(SalCorpWhpRuleEntity salCorpWhpRule);
	
	/**
	 * 增加企业的代缴代扣的规则
	 * 
	 * @param salCorpWhpRule 代缴代扣的规则的实体
	 * @return   增加的结果
	 */
	Integer synchCorpDeductRule(SalCorpDeductEntity salCorpWhpRule);
	
	/**
	 * 增加企业的基本薪资规则配置信息
	 * 
	 * @param salCorpBaseRule 企业基本薪资规则的实体
	 * @return  增加的结果
	 */
	Integer synchCorpBaseRule (SalCorpBaseRuleEntity salCorpBaseRule);
	
	/**
	 * 增加员工的基本信息
	 * 
	 * @param salStaffBaseInfo 员工基本信息的实体
	 * @return   增加的结果
	 */
	Integer synchStaffBaseInfo(SalStaffBaseInfoEntity salStaffBaseInfo);
	
	/**
	 * 增加企业的基本信息 
	 * 
	 * @param salStaffBaseInfo  企业基本信息的实体
	 * @return  增加的结果
	 */
	Integer synchCorpBaseInfo(SalCorpInfoEntity salStaffBaseInfo);
	
	/**
	 *  初始化企业月度工资报表的信息
	 * 
	 * @param salReportEntity  企业薪资报表的实体
	 * @return  增加的结果
	 */
	Integer addCorpSalReport(SalReportEntity salReportEntity);
	
	/**
	 *   初始化员工的月度薪资的信息
	 * 
	 * @param salReportEntity   企业薪资报表的实体
	 * @return  增加的结果
	 */
	Integer addStaffMonthSal(SalStaffSalReportEntity salReportEntity);
}
