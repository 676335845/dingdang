package me.ywork.salarybill.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.salarybill.entity.SalaryBill;
import me.ywork.salarybill.entity.SalaryBillLog;
import me.ywork.salarybill.model.SalaryBillLogModel;
import me.ywork.salarybill.model.SalaryBillReadRecordModel;
import me.ywork.salarybill.model.SalarySmsSendMode;

public interface SalaryBillLogRepository extends IRepository<SalaryBillLog>{

	/**
	 * 获取操作记录条数
	 * @param companyId  企业ID
	 * @return  记录数
	 */
	public Long getSalaryBillLogCount(
			@Param(value = "companyId") String companyId,
			@Param(value = "userId") String userId,
			@Param(value = "isBoss") boolean isBoss
			);
	
	/**
	 * 返回操作记录
	 * @param companyId  企业ID
	 * @param skipCount  需要跳过的数量
	 * @param pageSize   每页的数量，即返回的数据行数
	 * @return   投票人员列表
	 */
	public List<SalaryBillLogModel> getSalaryBillLog(
			@Param(value = "companyId") String companyId,
			@Param(value = "skipCount") Integer skipCount,
			@Param(value = "pageSize") Integer pageSize,
			@Param(value = "userId") String userId,
			@Param(value = "isBoss") boolean isBoss);
	
	
	/**
	 * 阅读记录列表
	 * @param companyId
	 * @param logid
	 * @param skipCount
	 * @param pageSize
	 * @return
	 */
	public List<SalaryBillReadRecordModel> getReadRecord(
			@Param(value = "companyId") String companyId,
			@Param(value = "logid") String logid,
			@Param(value = "skipCount") Integer skipCount,
			@Param(value = "pageSize") Integer pageSize);
	
	
	/**
	 * 本次发送悦通知的人员信息
	 * @param companyId
	 * @param logid
	 * @param smsMode = 1 查未读人数
	 *                = 2查所有人
	 * @return
	 */
	public List<SalaryBillReadRecordModel> getSalaryLogUsers(
			@Param(value = "companyId") String companyId,
			@Param(value = "logid") String logid,
			@Param(value = "smsMode") Short smsMode
			);
	
	
	/**
	 * 需要发短信的批次
	 * @param companyId  企业ID
	 * @param logId  记录ID
	 * @return
	 */
	public List<SalaryBillLogModel> getSmsLog();
	
	/**
	 * 更新短信状态以及发送量统计
	 * @param id
	 * @param smsCount
	 */
	public void updateSmsStatus(@Param(value = "companyid") String companyid,@Param(value = "id") String id,@Param(value = "smsCount") int smsCount);
	
	/**
	 * 软删除
	 * @param companyId  企业ID
	 * @param logId  记录ID
	 * @return
	 */
	public Boolean delLog(
			@Param(value = "companyId") String companyId,
			@Param(value = "logId") String logId
			);
	
	
	List<SalaryBillReadRecordModel> getSendSmsUsers(@Param(value = "companyId") String companyId,
							@Param(value = "salaryBills") List<SalaryBill> salaryBills,@Param(value = "smsMode") Short smsMode);
	
	
	public List<SalarySmsSendMode> getSendSmsInfo(@Param(value = "companyId") String companyId);
	
	
	public List<String> getStatus(@Param(value = "companyId") String companyId,@Param(value = "userId") String userId);
}
