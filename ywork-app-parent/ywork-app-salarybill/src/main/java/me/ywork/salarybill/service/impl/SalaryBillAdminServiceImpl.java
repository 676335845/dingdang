package me.ywork.salarybill.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.heyi.utils.IdGenerator;

import me.ywork.context.CallContext;
import me.ywork.salarybill.entity.SalaryBillAdmin;
import me.ywork.salarybill.model.SalaryBillAdminViewModel;
import me.ywork.salarybill.repository.SalaryBillAdminRepository;
import me.ywork.salarybill.repository.SalaryBillLogRepository;
import me.ywork.salarybill.service.SalaryBillAdminService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalaryBillAdminServiceImpl implements SalaryBillAdminService{
	
	private static Logger logger = LoggerFactory.getLogger(SalaryBillAdminServiceImpl.class);
	
	
	@Autowired
	private SalaryBillAdminRepository salaryBillAdminRepository;

	

	@Override
	public List<SalaryBillAdminViewModel> getSalaryBillAdmins(String companyId) {
		return salaryBillAdminRepository.getSalaryBillAdmins(companyId);
	}




	@Override
	public boolean saveSalaryBillAdmins(String companyId, String userid,boolean adminFlag) {
		Date d = new Date();
		SalaryBillAdmin admin = salaryBillAdminRepository.isSalaryBillAdmin(companyId,userid);
		if(admin==null){
			admin = new SalaryBillAdmin();
			admin.setId(IdGenerator.newId());
			admin.setCompanyId(companyId);
			admin.setUserId(userid);
			admin.setAdminFlag(adminFlag);
			admin.setCreateDate(d);
			admin.setModifiedDate(d);
			salaryBillAdminRepository.insert(admin);
		}else{
			admin.setAdminFlag(adminFlag);
			admin.setModifiedDate(d);
			salaryBillAdminRepository.update(admin);
		}
		return true;
	}




	@Override
	public boolean isSalaryBillAdmin(String companyId, String userId) {
		SalaryBillAdmin admin = salaryBillAdminRepository.isSalaryBillAdmin(companyId, userId);
		if(admin == null){
			//查询该用户是否发送过
			List<String> l = salaryBillLogRepository.getStatus(companyId, userId);
			if(l==null || l.size()==0){
				return false;
			}
			return true;
		}
		return admin.isAdminFlag();
	}

@Autowired
private SalaryBillLogRepository salaryBillLogRepository;


	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
