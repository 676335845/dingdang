package me.ywork.salary.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.model.SalInfoDetailModel;

@Repository
public interface SalInfoRepository extends IRepository<SalStaffBaseInfoEntity> {

	/**
	 * 获取员工规则详情：通过员工薪资ID来获取该员工的薪资详情
	 * 
	 * @param staffSalId 员工薪资标识
	 * @return   员工薪资信息的详情
	 */
	SalInfoDetailModel getSalInfosByStaffSalId(@Param("staffSalId") String staffSalId);

	/**
	 * 获取选中人员工资详情详情：通过编辑可以获取多个人的薪资详情
	 * 
	 * @param corpId 钉钉企业号
	 * @param staffids 员工ID的集合
	 * @return 员工薪资详细的集合
	 */
	List<SalInfoDetailModel> getMutiStaffSalInfosDetail(@Param("corpId") String corpId,
			@Param("staffIds") List<String> staffids);

	/**
	 * 修改多个人的薪资
	 * 
	 * @param staffSalInfo   员工薪资信息   
	 * @return 更新的结果
	 */
	Integer updateStaffInfo( SalInfoDetailModel staffSalInfo);
	
	/**
	 * 计算未设置薪资详情的人数
	 * 
	 * @param corpId 钉钉企业标识
	 * @return 未设置薪资的人数
	 */
	Integer calcuUnSetSalStaffNum(@Param("corpId")String corpId);

	
	/**
	 * 存储该员工的工资总数
	 * 
	 * @param corpId 钉钉企业标识
	 * @param staffId  钉钉员工的ID
	 * @param shouldPaySal 应发工资
	 * @return   更新的结果
	 */
	Integer saveStaffShouldPaySal(@Param("corpId")String corpId , @Param("staffId")String staffId,@Param("shouldPaySal")Double shouldPaySal);
	
	/**
	 * 得到企业所有员工的薪资信息
	 * 
	 * @param corpId 钉钉企业标识
	 * @return  员工薪资规则的实体集合
	 */
	List<SalStaffBaseInfoEntity> getAllStaffSalInfo(@Param("corpId")String corpId);
}
