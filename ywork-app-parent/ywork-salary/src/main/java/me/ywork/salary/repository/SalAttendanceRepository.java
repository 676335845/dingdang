package me.ywork.salary.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.salary.entity.SalCorpAttenEntity;
import me.ywork.salary.entity.SalStaffAttenDayEntity;
import me.ywork.salary.model.SalCorpAttenModel;
import me.ywork.salary.model.SalStaffAttenDayModel;
import me.ywork.salary.model.SalStaffAttendanceModel;

/**
 * 考勤信息的数据访问层
 * 
 * @author xiaobai
 *
 */
@Repository
public interface SalAttendanceRepository extends IRepository<SalCorpAttenEntity> {
	/**
	 * 返回所有月份的的考勤数据
	 * 
	 * @param corpId  钉钉企业号
	 * @return 得到结果
	 */
	List<SalCorpAttenModel> getAllMonthesAttendanceData (@Param("corpId") String corpId);

	/**
	 * 查询企业考勤表的总数
	 * 
	 * @param corpId 钉钉企业号
	 * @return 查询考勤总数的结果
	 */
	Integer getAttendanceDataCountByCorpId(@Param("corpId") String corpId);

	/**
	 * 获取该月所有人员考勤详情:
	 * 
	 * @param reportId  考勤报表的ID
	 * @param beginNum  开始的记录数
	 * @param endNum  限定返回的总数
	 * @return   员工的考勤情况的集合
	 */
	List<SalStaffAttendanceModel> getAllStaffAttendanceByReportId(@Param("reportId") String reportId,@Param("corpId")String corpId,@Param("monthTime")Date monthTime,
			@Param("beginNum") Integer beginNum , @Param("endNum") Integer endNum,@Param("type")Short usePage);	

	/**
	 * 考勤表ID来获得总数
	 * 
	 * @param reportId  考勤报表的ID
	 * @return  考勤报表下的员工考勤数量
	 */
	Integer getAllStaffAttendanceCountByReportId(@Param("reportId") String reportId);

	/**
	 * 获取选中人员该月考勤详情(编辑考勤):管理员可以选择多个员工和该月考勤的ID来得到这些员工的考勤详情
	 * 
	 * @param staffIds 钉钉员工ID的集合
	 * @param reportId  考勤报表的ID
	 * @return   选中人员该月考勤详情的集合
	 */
	List<SalStaffAttendanceModel> getMutiStaffAttendanceDetail(@Param("staffIds") List<String> staffIds,
			@Param("reportId") String reportId);

	/**
	 * 修改某员工的考勤数据后可以保存到数据库
     * 
     * @param staffAttendance 考勤数据的数据包
     * @return  更改的结果
     */
	Integer updateMutiStaffAttendance( SalCorpAttenEntity staffAttendance);

	/**
	 * 根据员工考勤标识得到该员工的考勤数据
	 * 
	 * @param id  员工考勤标识
	 * @return   员工考勤的实体
	 */
	SalCorpAttenEntity getStaffAttendanceEntityById(@Param("id") String id);
	
	/**
	 *  根据考勤报表的时间查询是否存在该月的考勤报表
	 * 
	 * @param corpId   钉钉企业号
	 * @param monthTime  月度时间
	 * @return  月度考勤报表的数据包
	 */
	SalCorpAttenModel getAttenByMonthTime(@Param("corpId") String corpId ,@Param("monthTime")Date monthTime);
	
	/**
	 * 根据考勤报表的时间删除用户的考勤数据
	 * 
	 * @param reportId  考勤报表的ID
	 * @return  删除用户考勤数据的结果
	 */
	Integer deleteCorpMonthAtten(@Param("reportId")String reportId);
	
	/**
	 * 删除考勤报表的所有员工的请假天数
	 * 
	 * @param reportId  考勤报表的ID
	 * @return 删除的结果
	 */
    Integer deleteAttenday(@Param("reportId")String reportId);
    
    /**
     * 根据考勤报表的ID去删除所有员工的考勤信息
     * 
     * @param reportId  考勤报表的ID
     * @return  删除的结果
     */
    Integer deleteStaffAttence(@Param("reportId")String reportId);
    
	/**
	 * 插入企业的考勤报表的数据
     * 
     * @param salCorpAttenModel 企业考勤报表的ID
     * @return   新增的结果
     */
	Integer insertCorpAttenReportData(SalCorpAttenModel salCorpAttenModel);
	
	/**
	 * 插入员工的考勤报表的数据
	 * 
	 * @param salCorpAttenEntity  员工考勤报表的实体
	 * @return   新增的结
	 */
	Integer insertStaffAttenData(SalCorpAttenEntity salCorpAttenEntity);
	
	/**
	 * 插入员工自定义请假的天数的记录
	 * 
	 * @param salStaffAttenDay 自定义请假天数的实体
	 * @return  插入的结果
	 */
	Integer insertStaffAttenDay(SalStaffAttenDayEntity salStaffAttenDay);
	
	/**
	 * 得到员工自定义请假信息列表
	 * 
	 * @param corpId   钉钉企业号
	 * @param reportId  考勤报表的ID
	 * @param staffId  钉钉员工ID
	 * @return  自定义请假信息列表
	 */
	List<SalStaffAttenDayModel> getSalStaffAttenDayInfos(@Param("corpId")String corpId,@Param("reportId")String reportId ,@Param("staffId") String staffId);
	
	/**
	 * 得到员工该月的考勤信息
	 * 
	 * @param corpId   钉钉企业号
	 * @param staffId 钉钉员工ID
	 * @param monthTime 月度时间
	 * @return  员工该月的考勤信息的实体
	 */
	SalCorpAttenEntity getStaffAttendanceByMonthTime(@Param("corpId")String corpId , @Param("staffId")String staffId,@Param("monthTime")Date monthTime);

   /**
    *  删除指定考勤报表下指定员工的考勤数据
    *  
    *  @param staffId 钉钉员工号
    *  @param reportId 考勤报表ID
    *  @return 删除的结果
    */
	Integer deleteStaffAttenDataUnderReportId(@Param("reportId")String reportId , @Param("staffId")String taffId);
	
	   /**
	    *  得到指定考勤报表下指定员工的考勤数据
	    *  
	    *  @param staffId 钉钉员工号
	    *  @param reportId 考勤报表ID
	    *  @return查询的结果
	    */
	SalCorpAttenEntity  getStaffAttenDataUnderReportId(@Param("reportId")String reportId , @Param("staffId")String staffId);
		
		/**
		 * 删除指定考勤报表下指定员工的天数
		 * 
		 * @param staffId 钉钉员工号
		 *  @param reportId 考勤报表ID
		 *  @return 删除的结果
		 */
		Integer deleteStaffAttenDayUnderReportId(@Param("reportId")String reportId , @Param("staffId")String staffId);
		
}
