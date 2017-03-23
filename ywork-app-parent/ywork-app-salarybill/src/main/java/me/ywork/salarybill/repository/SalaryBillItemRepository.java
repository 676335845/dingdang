package me.ywork.salarybill.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.salarybill.entity.SalaryBillItem;
import me.ywork.salarybill.model.SalaryBillItemModel;

public interface SalaryBillItemRepository extends IRepository<SalaryBillItem>{
	
	/**
	 * 获取员工工资明细信息
	 * @param companyId
	 * @param salaryBillId
	 * @return
	 */
	public List<SalaryBillItemModel> getSalaryItemsBySalaryBillId(
			@Param(value = "companyId") String companyId,
			@Param(value = "salaryBillId") String salaryBillId
	);
	
	public List<SalaryBillItemModel> getSalaryItemsBySalaryBillLogId(
			@Param(value = "companyId") String companyId,
			@Param(value = "logId") String logId,
			@Param(value = "userId") String userId
	);
	
}
