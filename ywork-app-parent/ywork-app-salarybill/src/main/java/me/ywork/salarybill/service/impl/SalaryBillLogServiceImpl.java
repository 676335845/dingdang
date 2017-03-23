package me.ywork.salarybill.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.ywork.context.CallContext;
import me.ywork.page.PageData;
import me.ywork.page.PageDataImpl;
import me.ywork.page.Pageable;
import me.ywork.salarybill.entity.SalaryBillLog;
import me.ywork.salarybill.model.SalaryBillLogModel;
import me.ywork.salarybill.model.SalaryBillReadRecordModel;
import me.ywork.salarybill.repository.SalaryBillLogRepository;
import me.ywork.salarybill.service.SalaryBillLogService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalaryBillLogServiceImpl implements SalaryBillLogService{
	
	private static Logger logger = LoggerFactory.getLogger(SalaryBillLogServiceImpl.class);
	
	
	@Autowired
	private SalaryBillLogRepository salaryLogRepository;


	@Override
	public PageData<SalaryBillLogModel> getSalaryBillLog(String companyId,String userId,Boolean isBoss, Pageable pageable) {
		// 当请求第一页时统计满足条件的总数量
		Long totalCount = 0L;
		if (pageable.getPageNo() <= 1) {
			totalCount = this.salaryLogRepository.getSalaryBillLogCount(companyId,userId,isBoss);
			pageable.setTotalCount(totalCount);
		}else{
			totalCount = pageable.getTotalCount();
		}

		// 查询当前页数据
		List<SalaryBillLogModel> salaryBillLogs = null;
		if (totalCount == null || totalCount == 0) {
			salaryBillLogs = new ArrayList<SalaryBillLogModel>();
		} else {
			salaryBillLogs = this.salaryLogRepository
					.getSalaryBillLog(
							companyId,
							pageable.getPageNo() - 1 <= 0 ? 0 : (pageable.getPageNo() - 1) * pageable.getPageSize(),
							pageable.getPageSize(),userId,isBoss);
		}

		return new PageDataImpl<SalaryBillLogModel>(salaryBillLogs, pageable);
	}


	@Override
	public String getFileIdByLogId(String companyId, String logId) {
		SalaryBillLog salaryBillLog =  salaryLogRepository.findById(companyId, logId);
		if(salaryBillLog!=null){
			return salaryBillLog.getFileKey();
		}
		return null;
	}


	@Override
	public Boolean delLog(String companyId, String logId) {
		Boolean f = salaryLogRepository.deleteById(companyId, logId);
		if(f==null || !f){
			return false;
		}
		return f;
	}
	

	@Override
	public PageData<SalaryBillReadRecordModel> getReadRecord(String companyId, String logid, Pageable pageable) {
		// 当请求第一页时统计满足条件的总数量
		Long totalCount = pageable.getTotalCount();
//		if (pageable.getPageNo() <= 1) {
//			pageable.setTotalCount(totalCount);
//		}else{
//			totalCount = pageable.getTotalCount();
//		}

		// 查询当前页数据
		List<SalaryBillReadRecordModel> salaryBillReadRecordModels = null;
		if (totalCount == null || totalCount == 0) {
			salaryBillReadRecordModels = new ArrayList<SalaryBillReadRecordModel>();
		} else {
			salaryBillReadRecordModels = this.salaryLogRepository.getReadRecord(
							companyId,
							logid,
							pageable.getPageNo() - 1 <= 0 ? 0 : (pageable.getPageNo() - 1) * pageable.getPageSize(),
							pageable.getPageSize());
		}

		return new PageDataImpl<SalaryBillReadRecordModel>(salaryBillReadRecordModels, pageable);
	}


	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
