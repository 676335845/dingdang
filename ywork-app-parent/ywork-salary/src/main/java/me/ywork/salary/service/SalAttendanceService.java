package me.ywork.salary.service;

import me.ywork.base.service.BizService;
import me.ywork.context.CallContext;
import me.ywork.page.Pageable;
import me.ywork.salary.exception.ExcelUnNormalException;
import me.ywork.salary.exception.MonthUnNormalException;
import me.ywork.salary.exception.SalFieldRepeatedException;
import me.ywork.salary.exception.StaffNotExistException;
import me.ywork.salary.model.SalAttenExcelModel;
import me.ywork.salary.model.SalCorpAttenModel;
import me.ywork.salary.model.SalStaffAttendanceModel;
import me.ywork.salary.model.SalUpdateMutiStaffModel;
import java.util.List;

/**
 *  考勤业务层接口
 */
public interface SalAttendanceService extends BizService {
	/**
	 * 获取所有月份考勤
	 * 
	 * @param corpId  钉钉企业号
	 * @return 得到企业所有月度的考勤信息
	 */
	List<SalCorpAttenModel> getAllMonthesAttendanceData(String corpId);

	/**
	 *获取企业月度所有人员的考勤信息
	 * 
	 * @param corpId 钉钉企业ID
	 * @param reportId 考勤报表ID
	 * @param pageable 分页page
	 * @return   返回企业员工的考勤信息
	 */
	SalAttenExcelModel getAllStaffAttendanceByReportId(String corpId , String reportId, Pageable pageable);

	/**
	 * 获取选中人员该月考勤详情(编辑考勤):管理员可以选择多个员工和该月考勤的ID来得到这些员工的考勤详情
	 * 
	 * 
	 * @param staffIds 所有人员钉钉ID的集合 
	 * @param reportId 月度考勤报表的ID
	 * @return 选中人员的考勤信息
	 */
	List<SalStaffAttendanceModel> getMutiStaffAttendanceDetail(List<String> staffIds, String reportId);

	/**
	 * 修改某员工的考勤数据后可以保存到数据库
	 * 
	 * @param staffAttendances 要修改考勤的集合
	 * @return 更新考勤信息的结果
	 */
	Boolean updateMutiStaffAttendance(List<SalStaffAttendanceModel> staffAttendances);
	
	/**
	 * 解析考勤报表的信息
	 * 
	 * @param callContext 存储信息的CallContext
	 * @param fileId 用户上传文件的ID
	 * @return 解析考勤报表的结果
	 */
	SalAttenExcelModel parseAttenExcel(CallContext callContext , String fileId) throws MonthUnNormalException,
	StaffNotExistException,ExcelUnNormalException,SalFieldRepeatedException;
	/**
	 * 提交考勤报表的信息
	 * 
	 * @param callContext 存储信息的CallContext
	 * @param cacheKey  redis缓存中的ID 
	 * @return 存储考勤报表的结果
	 */
	Boolean commitAttenExcel(CallContext callContext , String cacheKey);
	
	/**
	 * 得到企业考勤审批扣款规则的集合
	 * 
	 * @param corpId 钉钉企业ID
	 * @return  存储考勤审批的集合
	 */
	SalUpdateMutiStaffModel getCorpApproveFieldList(String corpId);
	
	/**
	 * 新企业考勤审批数据的扣款规则
	 * 
	 * @param corpId 钉钉企业ID
	 * @param salUpdateMutiStaffModel 要更新企业考勤审批数据的集合
	 * @return  更新企业考勤审批数据的结果
	 */
	Boolean updateCorpApproveField(String corpId , SalUpdateMutiStaffModel salUpdateMutiStaffModel);

}
