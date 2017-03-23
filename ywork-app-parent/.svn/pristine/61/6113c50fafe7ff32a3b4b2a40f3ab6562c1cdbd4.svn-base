package me.ywork.salarybill.service;


import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import me.ywork.base.service.BizService;
import me.ywork.context.CallContext;
import me.ywork.salarybill.model.CacheSalaryModel;
import me.ywork.salarybill.model.OrgTreeModel;
import me.ywork.salarybill.model.SalaryBillCommitModel;
import me.ywork.salarybill.model.SalaryBillModel;
import me.ywork.salarybill.model.SalaryBillNoPwdModel;
import me.ywork.salarybill.model.SalaryBillTemplateModel;
import me.ywork.salarybill.model.SalaryHistoryDispalyModel;

public interface SalaryBillService extends BizService{

	
	//public List<SalaryBillModel> viewSalary(CallContext callContext ,Integer salaryMonth, boolean loadItem,String salaryLogId);
	
	/**
	 * 用户数据展示
	 * @param callContext
	 * @param salaryMonth
	 * @param loadItem
	 * @param salaryLogId
	 * @param templateId
	 * @return
	 */
	public SalaryBillModel viewSalary(CallContext callContext ,Integer salaryMonth, boolean loadItem,String salaryLogId,String templateId);
	
	/**
	 * 用户历史数据列表
	 * @param callContext
	 * @return
	 */
	public List<SalaryHistoryDispalyModel> getHistorySalary(CallContext callContext);
	
	public HSSFWorkbook exportToExcel(String companyId);
	
	/**
	 * 置为已读
	 * @param companyId
	 * @param id
	 * @return
	 */
	public Integer setReaded(String companyId,String id);
	
	/**
	 * 解析上传的excel数据
	 * @param callContext
	 * @param fileId
	 * @return
	 */
	public CacheSalaryModel parserExcel(CallContext callContext,String fileId);
	
	/**
	 * 管理员提交上传数据
	 * @param callContext
	 * @param salaryBillCommitModel
	 * @return
	 */
	public boolean commitData(CallContext callContext,SalaryBillCommitModel salaryBillCommitModel);
	
	public String findAgentId(String corpId, String suiteId, String appId);
	
	/**
	 * 用户有下发数据的模板
	 * @param companyId
	 * @param userId
	 * @return
	 */
	public List<SalaryBillTemplateModel> hasDataSystemTemplate(String companyId,String userId,String salaryMonth);
	
	/**
	 * 公司发数据的模板
	 * @param companyId
	 * @param userId
	 * @return
	 */
	public List<SalaryBillTemplateModel> myTemplates(String companyId);
	
	public void sendVote(List<String> appids);
	
	
	/**
	 * 子部门
	 * @param companyId
	 * @param deptId
	 * @return
	 */
	public List<OrgTreeModel> getOrgSubDetpByDeptId(String companyId,String deptId);
	
	/**
	 * 成员
	 * @param companyId
	 * @param deptId
	 * @return
	 */
	public List<OrgTreeModel> getOrgUserByDeptId(String companyId,String deptId);
	
//	/**
//	 * 根据部门id查询部门下的人员
//	 * @param companyId
//	 * @param deptId
//	 * @return
//	 */
//	public List<OrgPcModel> findOrgPersonByDeptId(String companyId,String deptId);

	public SalaryBillNoPwdModel findDingElementById(String companyId,String id,String type);
	
	public String getCorpName(String companyId);
}
