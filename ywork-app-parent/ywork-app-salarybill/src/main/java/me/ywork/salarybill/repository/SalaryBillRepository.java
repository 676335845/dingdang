package me.ywork.salarybill.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.salarybill.entity.SalaryBill;
import me.ywork.salarybill.entity.SalaryBillItem;
import me.ywork.salarybill.model.CorpAdminMessage;
import me.ywork.salarybill.model.OrgDeptModel;
import me.ywork.salarybill.model.OrgTreeModel;
import me.ywork.salarybill.model.SalaryBillModel;
import me.ywork.salarybill.model.SalaryBillNoPwdModel;
import me.ywork.salarybill.model.SalaryBillTemplateModel;
import me.ywork.salarybill.model.SalaryHistoryDispalyModel;
import me.ywork.salarybill.model.UserModel;

public interface SalaryBillRepository extends IRepository<SalaryBill>{

//	/**
//	 * 显示员工某月份工资信息
//	 * @param companyId
//	 * @param userId
//	 * @param salaryMonth
//	 * @return
//	 */
//	public List<SalaryBillModel> viewSalary(
//			@Param(value = "companyId") String companyId,
//			@Param(value = "userId") String userId,
//			@Param(value = "salaryMonth") Integer salaryMonth,
//			@Param(value = "salaryLogId") String salaryLogId
//	);
	
	/**
	 * 显示员工某月份工资信息
	 * @param companyId
	 * @param userId
	 * @param salaryMonth
	 * @return
	 */
	public SalaryBillModel viewSalary(
			@Param(value = "companyId") String companyId,
			@Param(value = "userId") String userId,
			@Param(value = "salaryMonth") Integer salaryMonth,
			@Param(value = "salaryLogId") String salaryLogId,
			@Param(value = "templateId") String templateId
	);
	
	/**
	 * 置为已读
	 * @param companyId
	 * @param id
	 * @return
	 */
	public Integer setReaded(
			@Param(value = "companyId") String companyId,
			@Param(value = "id") String id
	);
	
	/**
	 * 显示历史记录
	 * @param companyId
	 * @param userId
	 * @param salaryMonth
	 * @return
	 */
	public List<SalaryHistoryDispalyModel> historySalary(
			@Param(value = "companyId") String companyId,
			@Param(value = "userId") String userId
	);
	
	/**
	 * 显示员工某月份工资信息
	 * @param companyId
	 * @param userId
	 * @param salaryMonth
	 * @return
	 */
	public SalaryBillModel viewSalary4other(
			@Param(value = "companyId") String companyId,
			@Param(value = "userId") String userId,
			@Param(value = "salaryMonth") Integer salaryMonth,
			@Param(value = "templateId") String templateId
	);
	
	/**
	 * 获取公司下的员工信息
	 * @param companyId
	 * @return
	 */
	public List<UserModel> getUserInfoByCompany(
			@Param(value = "companyId") String companyId
	);
	
	
	/**
	 * 获取公司下的所有部门
	 * @param companyId
	 * @return
	 */
	public List<OrgDeptModel> getDeptInfoByCompany(
			@Param(value = "companyId") String companyId
	);
	
	
	
	/**
	 * 批量保存数据
	 * @param salaryBills 人员投票权列表
	 * @param salaryBillItems
	 * @return 保存成功的数量
	 */
	public int saveSalaryBills(@Param("salaryBills") List<SalaryBill> salaryBills,@Param("salaryBillItems") List<SalaryBillItem> salaryBillItems);
	
	
	
	/**
	 * 批量查询userid是否存在
	 * @param companyId
	 * @param salaryBills
	 * @return
	 */
	public List<String> notExistsUser(@Param(value = "companyId") String companyId,@Param("userids") List<SalaryBillModel> salaryBills);
	
	
	String findAgentId(@Param("corpId") String corpId,@Param("suiteId") String suiteId,@Param("appId") String appId);
	
	
	List<SalaryBillTemplateModel> hasDataSystemTemplate(@Param("companyId") String companyId,@Param(value = "userId") String userId,@Param(value = "salaryMonth") String salaryMonth);
	
	List<SalaryBillTemplateModel> myTemplates(@Param("companyId") String companyId);
	
	
	public List<CorpAdminMessage> getCorpByAppId(@Param("appid") String appId);
	
	/**
	 * 子部门列表及人员统计
	 * @param companyId
	 * @param deptId
	 * @return
	 */
	public List<OrgTreeModel> findOrgSubDetpByDeptId(@Param("companyId") String companyId,@Param("deptId") String deptId);
	
	/**
	 * 人员信息
	 * @param companyId
	 * @param deptId
	 * @return
	 */
	public List<OrgTreeModel> findOrgUserByDeptId(@Param("companyId") String companyId,@Param("deptId") String deptId);
	
	/**
	 * 公司名
	 * @param companyId
	 * @param deptId
	 * @return
	 */
	public String findCorpName(@Param("companyId") String companyId);
	
	
   // public List<OrgPcModel> findOrgPersonByDeptId(@Param("companyId") String companyId,@Param("deptId") String deptId);

	public SalaryBillNoPwdModel findDingElementById(@Param("companyId") String companyId, @Param("id") String id,@Param("type") String type);

	public void deleteSalaryBillNoPwdSelectedItem(@Param("companyId") String companyId, @Param("userId") String userId);
	
}
