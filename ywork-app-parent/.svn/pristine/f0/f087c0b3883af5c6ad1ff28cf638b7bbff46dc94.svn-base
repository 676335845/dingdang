package me.ywork.salarybill.repository;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.salarybill.entity.SalaryBillMobileNo;

public interface SalaryBillMobileNoRepository extends IRepository<SalaryBillMobileNo>{
	
	
	/**
	 * 批量保存数据
	 * @param salaryBills 人员投票权列表
	 * @param salaryBillItems
	 * @return 保存成功的数量
	 */
	public int batchSaveSalaryBillMobileNos(@Param("salaryBillMobileNos") List<SalaryBillMobileNo> salaryBillMobileNos);
	
	List<String> findNoMobiles(@Param(value = "corpId") String corpId);
}
