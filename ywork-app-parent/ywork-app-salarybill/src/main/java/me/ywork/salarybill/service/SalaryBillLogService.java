package me.ywork.salarybill.service;




import me.ywork.base.service.BizService;
import me.ywork.page.PageData;
import me.ywork.page.Pageable;
import me.ywork.salarybill.model.SalaryBillLogModel;
import me.ywork.salarybill.model.SalaryBillReadRecordModel;

public interface SalaryBillLogService extends BizService{

	public PageData<SalaryBillLogModel> getSalaryBillLog(String companyId, String userId,Boolean isBoss,Pageable pageable );
	
	public String getFileIdByLogId(String companyId, String logId);
	
	public Boolean delLog(String companyId, String logId);
	
	public PageData<SalaryBillReadRecordModel> getReadRecord(String companyId, String logid,Pageable pageable );
	
	
}
