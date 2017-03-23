package me.ywork.salarybill.repository;



import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.salarybill.entity.SalaryBillSms;
import me.ywork.salarybill.model.SalaryBillMobileModel;
import me.ywork.salarybill.model.SmsReportModel;

public interface SalaryBillSmsRepository extends IRepository<SalaryBillSms>{
	
	
	SalaryBillSms findByCorpId(@Param(value = "corpId") String corpId);

	/**
	 * 批量查询userid是否存在
	 * @param companyId
	 * @param salaryBills
	 * @return
	 */
	public List<String> notExistsUser(@Param(value = "companyId") String companyId,@Param("userids") List<SalaryBillMobileModel> salaryBills);

	Integer countCorpUsers(@Param(value = "corpId") String corpId);
	
	/**
	 * 更新用户短信数
	 * @param companyId
	 * @param freeCount
	 */
	void updateSms(@Param(value="companyId") String companyId,@Param(value="sendCount") int sendCount);
	
	List<SmsReportModel> getSmsReport(@Param(value="st") Date st, @Param(value="et") Date et) ;
}
