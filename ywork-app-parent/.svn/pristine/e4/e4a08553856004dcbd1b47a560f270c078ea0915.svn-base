package me.ywork.salary.service;

import me.ywork.base.service.BizService;
import me.ywork.context.CallContext;
import me.ywork.page.PageData;
import me.ywork.page.Pageable;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.exception.ExcelUnNormalException;
import me.ywork.salary.exception.StaffNotExistException;
import me.ywork.salary.model.SalInfoDetailModel;
import me.ywork.salary.model.SalInfoExcelModel;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 员工薪资信息业务管理
 */
public interface SalInfoService extends BizService {

	/**
	 * 获取所有员工的薪资详情，要分页
	 * 
	 * @param corpId   钉钉企业号
	 * @param pageable 分页的Pageable
	 * @return   分页的薪资详情的数据
	 */
	PageData<SalStaffBaseInfoModel> getStaffSalInfos(String corpId, Pageable pageable);

	/**
	 * 通过员工ID来获取该员工的薪资详情
	 * 
	 * @param corpId  企业钉钉ID
	 * @param reportId 企业薪资报表ID
	 * @param showType  基本的显示或为月度工资报表中员工薪资详情的显示
	 * @param dingStaffId  员工钉钉ID
	 * @return 员工薪资详情的数据包
	 */
	SalInfoDetailModel getSalInfosByStaffId(String corpId ,String reportId, Short showType,String dingStaffId);

	/**
	 * 获取选中人员工资详情详情(编辑规则):通过编辑可以获取多个人的薪资详情
	 * 
	 * @param corpId 钉钉企业号
	 * @param staffids 员工ID的集合
	 * @return 员工薪资详细的集合
	 */
	List<SalInfoDetailModel> getMutiStaffSalInfosDetail(String corpId, List<String> staffids);

	
	/**
	 * 计算未设置薪资详情的人数
	 * 
	 * @param corpId 钉钉企业标识
	 * @return  未设置薪资的人数
	 */
	Integer calcuUnSetSalStaffNum(String corpId);
	
	/**
	 * 解析薪资模板的信息
	 * 
	 * @param callContext 存储在session中的值的集合
	 * @param fileId 用户上传到OSS文件的ID
	 * @return  解析薪资模块的数据包
	 */
	SalInfoExcelModel parseSalInfoExcel(CallContext callContext , String fileId) throws StaffNotExistException,ExcelUnNormalException;
	/**
	 * 提交薪资模板的信息
	 * 
	 * @param callContext 存储在session中的值的集合
	 * @param cacheKey 缓存在redis的key
	 * @return  将员工薪资数据更新到数据库的结果
	 */
	Boolean commitSalInfoExcel(CallContext callContext , String cacheKey);	
	
	/**
	 * 企业下载自己的薪资模板：根据企业的组织架构自动生成
	 * 
	 * @param corpId  钉钉企业ID
	 * @return 薪资模板
	 */
	HSSFWorkbook exportToExcel(String corpId);
	
}
