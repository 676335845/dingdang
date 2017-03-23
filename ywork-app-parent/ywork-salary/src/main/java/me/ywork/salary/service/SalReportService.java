package me.ywork.salary.service;

import me.ywork.base.service.BizService;
import me.ywork.page.PageData;
import me.ywork.page.Pageable;
import me.ywork.salary.model.SalCorpReportModel;
import me.ywork.salary.model.SalStaffSalReportDetailModel;
import me.ywork.salary.model.SalStaffSalReportModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalStaffMbSalInfoModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 企业薪资报表的业务管理
 */
@Service
public interface SalReportService extends BizService {
	/**
	 *获取所有月份的工资表
	 * 
	 * @param corpId 钉钉企业号
	 * @return 获取的月度薪资报表的集合
	 */
	List<SalCorpReportModel> getAllSalReports(String corpId);
	
	/**
	 * 获取某一月份的工资表：目前用于手机端
	 * 
	 * @param corpId 钉钉企业号
	 * @return 某月份的薪资报表信息
	 */
	SalCorpReportModel getSalReportByReportId(String reportId);

	/**
	 * 根据工资表ID来获得该月的工资详情
	 * 
	 * @param reportId 薪资报表的ID
	 * @param corpId   企业ID
	 * @param pageable  分页的page
	 * @return 分页的集合
	 */
	PageData<SalStaffSalReportModel> getSalDatailByReportId(String reportId,String corpId , Pageable pageable);

	/**
	 * 锁定该月工资表
	 * 
	 * @param reportId 月度工资报表的ID
	 * @param reportState 工资表的状态
	 * @return 锁定的结果
	 */
	Boolean lockSalReportById(String reportId, Short reportState);

	/**
	 *修改员工的浮动工资
	 *
	 * @param corpId  钉钉企业ID
	 * @param monthStaffSalDetailModels  要更新的员工薪资集合
	 * @return    修改浮动工资的结果
	 */
	Boolean updateSalsOnStaffes(String corpId , List<SalStaffSalReportDetailModel> monthStaffSalDetailModels);
	
	/**
	 * 获取员工所有工资月份
	 * 
	 * @param corpId 钉钉企业ID
	 * @param staffId 钉钉员工ID
	 * @return 工资月份的集合
	 */
	List<SalCorpReportModel> getUserSalDates(String corpId , String staffId);
	
	/**
	 * 得到某月员工的薪资信息（包括员工的基本信息及详情）
	 * 
	 * @param corpId 钉钉企业ID
	 * @param staffId 钉钉员工ID
	 * @param reportId 钉钉薪资报表的ID
	 * @return 某月员工的薪资数据
	 */
	SalStaffMbSalInfoModel getStaffMbSalInfo(String corpId ,String staffId , String reportId);
	
	/**
	 * 得到选中员工或部门的浮动款项的数据列表
	 * 
	 * @param staffs 存储员工信息的集合
	 * @param corpId  钉钉企业ID
	 * @param reportId 薪资报表的ID
	 * @return 选中员工的浮动薪资数据的集合
	 */
	List<SalStaffSalReportDetailModel> getSelectStaffFloatSalData(List<SalStaffBaseInfoModel> staffs , String corpId ,String reportId);
	
	/**
	 * 企业下载自己的薪资模板：根据企业的组织架构自动生成
	 * 
	 * @param corpId  钉钉企业ID
	 * @param reportId 企业月度薪资报表ID
	 * @return 薪资模板
	 */
	HSSFWorkbook exportToExcel(String corpId , String reportId);
}

