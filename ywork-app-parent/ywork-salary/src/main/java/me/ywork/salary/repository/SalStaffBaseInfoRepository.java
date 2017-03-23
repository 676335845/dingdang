package me.ywork.salary.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.salary.entity.SalElementInfoEntity;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.model.SalChoiceDeptUserInfoModel;
import me.ywork.salary.model.SalCorpInfoModel;
import me.ywork.salary.model.SalDeptInfoModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalSysFieldItemModel;

/**
 * 
 * 员工基本信息的数据访问层
 * 
 * @author xiaobai
 *
 */
@Repository
public interface SalStaffBaseInfoRepository extends IRepository<SalStaffBaseInfoEntity> {
	/**
	 * 获取所有员工的薪资详情，要分页
	 * 
	 * @param corpId 钉钉企业号
	 * @param beginNum 查询的开始记录数
	 * @param endNum 查询的最大个数
	 * @return 员工薪资数据的集合
	 */
	List<SalStaffBaseInfoModel> getStaffSalInfos(@Param("corpId") String corpId, @Param("beginNum") Integer beginNum,
			@Param("endNum") Integer endNum);
	
	/**
	 * 获取企业所有员工的信息
	 * 
	 * @param corpId 钉钉企业号
	 * @return 所有员工信息的数据集合 
	 */
	List<SalStaffBaseInfoModel> getStaffInfos(@Param("corpId")String corpId);
	
	/**
	 * 返回企业所有员工的薪资信息的总数 
	 * 
	 * @param corpId  钉钉企业号
	 * @return 有员工的薪资信息的总数 
	 */
	Integer getStaffSalInfosCount(@Param("corpId")String corpId);
	
	/**
	 * 查询企业具体员工的信息
	 * 
	 * @param corpId   钉钉企业号
	 * @param staffId 钉钉员工号
	 * @return  企业具体员工的信息的数据包
	 */
	SalStaffBaseInfoModel getStaffBaseInfo(@Param("corpId")String corpId,@Param("staffId")String staffId);
	
	/**
	 * 查询在组织架构中员工中的具体信息
	 * 
	 * @param corpId  钉钉企业号
	 * @param staffId 钉钉员工号
	 * @return 组织架构中员工中的具体信息的数据包
	 */
	SalStaffBaseInfoModel getOrgStaffInfo(@Param("corpId")String corpId,@Param("staffId")String staffId);

	/**
	 * 新增员工自定义薪资字段
	 * 
	 * @param fieldItemModel  自定义薪资字段的数据包
	 * @return  新增的结果 
	 */
	Integer addNewSalInfoField(SalSysFieldItemModel fieldItemModel);
	
	/**
	 * 删除员工的自定义的薪资字段
	 * 
	 * @param staffId 钉钉员工号  
	 * @return  删除的结果
	 */
	Integer deleteStaffSalField(@Param("staffId")String staffId);
	
	/**
	 * 新增企业考勤请假的自定义的字段
	 * 
	 * @param salSysFieldItemModel   自定义的字段的数据包
	 * @return  新增的结果
	 */
	Integer addNewAttendanceVacationField(SalSysFieldItemModel salSysFieldItemModel);

	/**
	 * 更新字段
	 * 
	 * @param fieldItemModel  要更新字段的数据包
	 * @return 更新的结果
	 */
	Integer updateField(SalSysFieldItemModel fieldItemModel);

	/**
	 * 根据父字段ID去查询该员工所有自定义的字段
	 * 
	 * @param corpId  钉钉企业号
	 * @param dingStaffId  钉钉员工号
	 * @return  自定义字段的集合
	 */
	List<SalSysFieldItemModel> getStaffFieldItems(@Param("corpId") String corpId , @Param("dingStaffId") String dingStaffId);
	
	
	 /**
	  * 删除企业对应的薪资规则
	 * 
	 * @param corpId   钉钉企业号
	 * @param cbRuleId  基本薪资规则的ID
	 * @param updateType  更新企业规则的类型
	 * @return  删除的结果
	 */
	 Integer deleteCorpSalRule(@Param("corpId")String corpId,	
			                                      @Param("cbRuleId") String cbRuleId ,
											   	@Param("updateType")Short updateType);
	/**
	 *  更新企业人员对应的规则
	  * 
	  * @param salStaffBaseInfoModel  存储企业规则的数据包
	  * @param cbRuleId    基本薪资规则的ID
	  * @param updateType  更新规则的类型
	  * @param corpId   钉钉企业号
	  * @return   更新的结果
	  */
	Integer updateSalRuleStaffies(@Param("sal")SalStaffBaseInfoModel salStaffBaseInfoModel ,
														@Param("cbRuleId") String cbRuleId ,
														@Param("updateType")Short updateType,
														@Param("corpId")String corpId);
	/**
	 * 删除企业员工的基本薪资规则
	 * 
	 * @param salStaffBaseInfoModel   存储员工基本薪资规则的数据包
	 * @param deleteType   删除规则的类型
	 * @return  删除规则的结果
	 */
	Integer deleteStaffSalRule(@Param("sal")SalStaffBaseInfoModel salStaffBaseInfoModel , @Param("type")Short deleteType);
	
	
	/**
	 * 根据部门的ID得到企业所有的子部门ID的集合
	 * 
	 * @param corpId   钉钉企业号
	 * @param deptId  钉钉部门号
	 * @return    企业所有的子部门ID的集合
	 */
	List<String> getDeptInfoByParentId(@Param("corpId") String corpId , @Param("deptId")String deptId);	

	
	/**
	 * 得到企业的请假类型的字段集合
	 * 
	 * @param corpId   钉钉企业号
	 * @return   请假类型的字段集合
	 */
	List<SalSysFieldItemModel> getCorpVacations(@Param("corpId")String corpId);

	
	/**
	 * 得到员工所有的部门的名字
	 * 
	 * @param corpId   钉钉企业号
	 * @param staffId  钉钉员工号
	 * @return   存储员工所有部门名字的集合
	 */
	List<String> getStaffAllDeptName(@Param("corpId")String corpId , @Param("staffId")String staffId);

	
	/**
	 * 根据企业的钉钉ID去获取企业组织架构中员工的总数
	 * 
	 * @param corpId   钉钉企业号
	 * @return   企业组织架构中员工的总数
	 */
	Integer getCorpStaffOrgCount(@Param("corpId")String corpId);
	
	/**
	 * 得到组织架构下指定部门下的所有人员的信息
	 * 
	 * @param corpId   钉钉企业号
	 * @param deptId  钉钉部门号
	 * @return 人员信息的集合
	 */
	List<SalElementInfoEntity> getOrgStaffInfosUnderDept(@Param("corpId")String corpId , @Param("deptId")String deptId);
	
	/**
	 * 得到选中的部门和用户的信息
	 * 
	 * @param corpId  钉钉企业号
	 * @param deptId 钉钉部门号
	 * @param type   薪资规则的类型
	 * @param cbRuleId 基本薪资规则的ID
	 * @return  得到选中的部门和用户的信息的结果
	 */
	List<SalChoiceDeptUserInfoModel> getChoiceDeptUserInfo(@Param("corpId")String corpId , 
			                                                                                                 @Param("deptId")String deptId,
			                                                                                                 @Param("type") Short  type,
			                        			                                                             @Param("cbRuleId") String cbRuleId);
	
	/**
	 * 得到某部门下在某薪资规则下的员工人数
	 * 
	 * @param corpId  钉钉企业号
	 * @param deptId 钉钉部门号
	 * @param type 薪资规则的类型
	 * @param cbRuleId 基本薪资规则的ID
	 * @return  员工人数
	 */
	Integer getStaffCountUnderDeptAndRule(@Param("corpId")String corpId , 
                                                                             @Param("deptId")String deptId,
                                                                             @Param("type") Short  type,
    			                                                             @Param("cbRuleId") String cbRuleId);
	
	/**
	 * 判断某人员是在某规则下是否存在
	 * 
	 * @param corpId  钉钉企业号
	 * @param staffId 钉钉员工号
	 * @param type 薪资规则的类型
	 * @param cbRuleId 基本薪资规则的ID
	 * @param 判断的结果
	 */
	Integer getStaffCountUnderRule(@Param("corpId")String corpId , 
																            @Param("staffId")String deptId,
																            @Param("type") Short  type,
																            @Param("cbRuleId") String cbRuleId);
	
	/**
	 * 得到公司的一些基本信息
	 */
	SalCorpInfoModel getCorpBsInfo(@Param("corpId")String corpId);
}
