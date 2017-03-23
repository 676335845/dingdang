package me.ywork.salarybill.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.salarybill.entity.SalaryBillAdmin;
import me.ywork.salarybill.model.SalaryBillAdminViewModel;

public interface SalaryBillAdminRepository extends IRepository<SalaryBillAdmin>{
	
	/**
	 * 子管理员列表
	 * @param companyId
	 * @param salaryBillId
	 * @return
	 */
	public List<SalaryBillAdminViewModel> getSalaryBillAdmins(
			@Param(value = "companyId") String companyId
	);
	
	/**
	 *保存薪酬管理员
	 * @param companyId
	 * @param salaryBillId
	 * @return
	 */
	public Boolean saveSalaryBillAdmins(
			@Param(value = "companyId") String companyId,
			@Param(value = "salaryBillAdmins") List<SalaryBillAdmin> salaryBillAdmins
	);
	
	/**
	 *保存薪酬管理员
	 * @param companyId
	 * @param salaryBillId
	 * @return
	 */
	public Boolean batchUpdateSalaryBillAdmins(
			@Param(value = "companyId") String companyId,
			@Param(value = "insertAdmins") List<SalaryBillAdmin> insertAdmins,
			@Param(value = "updateAdmins") List<SalaryBillAdmin> updateAdmins
	);
	
	
	/**
	 * 薪酬管理员列表
	 * @param companyId
	 * @param salaryBillId
	 * @return
	 */
	public List<SalaryBillAdmin> getAllSalaryAdmins(@Param(value = "companyId") String companyId
	);
	
	
	public SalaryBillAdmin isSalaryBillAdmin(@Param(value = "companyId") String companyId, @Param(value = "userId") String userId) ;
	
}
