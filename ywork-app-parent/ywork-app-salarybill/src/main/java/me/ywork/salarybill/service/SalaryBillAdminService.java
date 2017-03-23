package me.ywork.salarybill.service;




import java.util.List;

import me.ywork.base.service.BizService;
import me.ywork.salarybill.model.SalaryBillAdminViewModel;

public interface SalaryBillAdminService extends BizService{

	public List<SalaryBillAdminViewModel> getSalaryBillAdmins(String companyId);
	
	public boolean saveSalaryBillAdmins(String companyId,String userid,boolean adminFlag);
	
	public boolean isSalaryBillAdmin(String companyId,String userId);
	
}
