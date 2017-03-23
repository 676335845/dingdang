package me.ywork.salary.repository;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import me.ywork.base.repository.IRepository;
import me.ywork.salary.entity.SalReportEntity;
import me.ywork.salary.entity.SalStaffSalReportEntity;
import me.ywork.salary.model.SalStaffSalReportDetailModel;
import me.ywork.salary.model.SalStaffSalReportModel;
import me.ywork.salary.model.SalCorpReportModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalStaffMbSalInfoModel;

/**
 * 
 *薪资报表的数据访问层
 * @author xiaobai
 *
 */
@Repository
public interface SalReportRepository extends IRepository<SalReportEntity> {
	/**
	 * 获取所有月份工资表
	 * 
	 * @param corpId  钉钉企业号
	 * @param pageable   分页的pageable
	 * @return 获取的月度薪资报表的集合
	 */
	List<SalCorpReportModel> getAllSalReports(@Param("corpId") String corpId);
	
	/**
	 * 得到某一月份的工资表:目前用于手机端
	 * 
	 * @param reportId 薪资报表的ID
	 * @return  某月份的薪资报表详情
	 */
	SalCorpReportModel getSalReportByReportId(@Param("reportId")String reportId);

	/**
	 * 获取所有月份的工资表的总数
	 * 
	 * @param corpId  钉钉企业号
	 * @return 获取的月度薪资报表的总数
	 */
	Integer getAllMonthSalCount(@Param("corpId") String corpId);

	/**
	 * 根据工资表ID来获得该月的员工的薪资数据 分页
	 * 
	 * @param reportId 工资表ID
	 * @param corpId  钉钉企业ID
	 * @param beginNum  分页开始的下标值
	 * @param pageSize  分页的Size
	 * @return    员工的薪资数据
	 */
	List<SalStaffSalReportModel> getSalDatailByReportId(@Param("reportId") String reportId,
																									@Param("corpId")String corpId,
																									@Param("beginNum") Integer  beginNum  , 
																									@Param("pageSize") Integer pageSize);
	
	/**
	 * 根据工资表ID来获得该月的工资详情
	 * 
	 * @param reportId  薪资报表的ID
	 * @param corpId  钉钉企业ID
	 * @return  该月的工资详情
	 */
	List<SalStaffSalReportModel> getSalDatailByReportIdNotPage(@Param("reportId") String reportId,@Param("corpId")String corpId);
	
	 /**
	  * 获取该月度参与计算薪资的人数 
	 * 
	 * @param reportId 薪资报表的ID
	 * @param corpId 钉钉企业ID
	 * @return  s该月度参与计算薪资的人数 
	 */
	Integer getSalDatailCountByReportId(@Param("reportId") String reportId,
																	@Param("corpId")String corpId);

	
	/**
	 * 根据员工月度薪资数据的主键得到员工的月度薪资数据
	 * 
	 * @param id  员工月度薪资数据的主键
	 * @return  员工的月度薪资数据
	 */
	SalStaffSalReportEntity getSalDetailEntityById(@Param("id") String id);
	
	/**
	 * 锁定该月工资表’
	 * 
	 * @param reportId  月度工资报表的ID
	 * @param reportState 工资表的状态
	 * @return 锁定的结果
	 */
	Integer lockSalReportById(@Param("reportId") String reportId, @Param("reportState") Short reportState);

	/**
	 * 获取指定人员该月工资详情(修改浮动款项):传递工资表ID和多个员工ID去返回多个员工的工资数据
	 * 
	 * @param dingStaffId 员工钉钉ID
	 * @param reportId 薪资报表的ID
	 * @param corpId 钉钉企业ID
	 * @return   指定人员该月工资详情
	 */
	SalStaffSalReportDetailModel getSelectedStaffSalDetail(@Param("dingStaffId")String dingStaffId,
																												@Param("reportId") String reportId,
																												@Param("corpId")String corpId);
	
	/**
	 * 得到部门下的所有员工的浮动款项(修改浮动款项):传递工资表ID和多个员工ID去返回多个员工的工资数据
	 * 
	 * @param deptIds 存储企业部门的集合
	 * @param reportId 薪资报表的ID
	 * @param corpId  钉钉企业ID
	 * @return  得到部门下的所有员工的浮动款项
	 */
	List<SalStaffSalReportDetailModel> getDeptAllStaffFloatSal(@Param("deptIds") List<String> deptIds,
			@Param("reportId") String reportId , @Param("corpId")String corpId);

	/**
	 * 更新某月的员工薪资数据 
	 * 
	 * @param monthStaffSalDetailModels 月度员工工资的数据的集合
	 * @return 得到的更新的结果
	 */
	Integer updateStaffSalOnMonth(SalStaffSalReportDetailModel monthStaffSalDetailModels);
	
	/**
	 * 获取员工所有工资月份
	 * 
	 * @param corpId 钉钉企业ID
	 * @param staffId  钉钉员工ID
	 * @return  员工拥有工资数据的月份列表
	 */
	List<SalCorpReportModel> getUserSalDates(@Param("corpId")String corpId , @Param("staffId")String staffId);
	
	/**
	 * 得到某月员工的薪资信息（包括员工的基本信息及详情）：目前仅用于手机端
	 * 
	 * @param staffId 钉钉员工ID
	 * @param reportId  薪资报表ID
	 * @return  某月员工的薪资信息
	 */
	SalStaffMbSalInfoModel getStaffMbSalInfo( @Param("staffId")String staffId , @Param("reportId")String reportId,@Param("corpId")String corpId);
	
	/**
	 * 得到选择人员该月的浮动款项的详情列表
	 * 
	 * @param staffs 员工的薪资信息列表
	 * @param corpId  钉钉企业ID
	 * @param reportId  薪资报表的ID
	 * @return  员工浮动款项的列表
	 */
	List<SalStaffSalReportDetailModel> getSelectStaffFloatSalData(@Param("staffs")List<SalStaffBaseInfoModel> staffs ,
																												 @Param("corpId")String corpId ,
																												 @Param("reportId")String reportId);
	
	/**
	 * 得到指定人员该月的浮动款项的详情列表
	 * 
	 * @param staffId  员工钉钉ID
	 * @param corpId  钉钉企业ID
	 * @param reportId  薪资报表的ID
	 * @return  员工浮动薪资的数据
	 */
	SalStaffSalReportDetailModel getFloatSalDataByStaffId(@Param("staffId")String staffId,
																									@Param("corpId")String corpId ,
																								    @Param("reportId")String reportId);
	/**
	 * 更新企业的工资报表
	 * 
	 * @param salReportEntity 要更新的工资报表的实体
	 * @return 更新的结果
	 */
	Integer updateSalReport(SalReportEntity salReportEntity);
	
	/**
	 * 得到某种状态的薪资报表的列表
	 * 
	 * @param corpId  钉钉企业号
	 * @param state 企业薪资报表的状态
	 * @return 获取的月度薪资报表的集合
	 */
	List<SalCorpReportModel> getAllSalReportsByState(@Param("corpId") String corpId,@Param("state")Short state);
	
	/**
	 *  得到员工月度工资报表的实体
	 *  
	 * @param corpId  企业钉钉ID
	 * @param staffId  员工钉钉ID
	 * @return  员工薪资报表的实体
	 */
	SalStaffSalReportEntity getStaffMonthSalEntity(@Param("reportId")String reportId,@Param("staffId")String staffId);
	
	/**
	 * 更新员工的月度薪资数据
	 * 
	 * @param salStaffSalReportEntity 要更新的员工薪资数据
	 * @return 更新的结果
	 */
	Integer updateStaffMonthSalEntity(SalStaffSalReportEntity salStaffSalReportEntity);
}
