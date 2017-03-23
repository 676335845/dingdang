package me.ywork.salarybill.service;




import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import me.ywork.base.service.BizService;
import me.ywork.context.CallContext;
import me.ywork.salarybill.model.CacheSalaryMobileModel;
import me.ywork.salarybill.model.SalaryBillCommitModel;
import me.ywork.salarybill.model.SalarySmsMode;
import me.ywork.salarybill.model.SmsReportModel;

public interface SalaryBillSmsService extends BizService{

	public boolean setSMSMode(String companyId,String userId,SalarySmsMode smsMode);
	
	public HSSFWorkbook exportMobileTempToExcel(String companyId);
	
	public CacheSalaryMobileModel parserMobileTempExcel(String domainName, String companyId,String managerId,String bucketName,String fileId);
	
	/**
	 * 管理员提交上传的手机数据
	 * @param domainName
	 * @param salaryBillCommitModel
	 * @return
	 */
	public boolean commitMobile(CallContext callContext,SalaryBillCommitModel salaryBillCommitModel);
	
	/**
	 * 企业短信设置信息
	 * @param companyId
	 * @param userId
	 */
	public SalarySmsMode getCorpSmsInfo(String companyId,String userId);
	
	public List<SmsReportModel> getSmsReport(Date st,Date et);
}
