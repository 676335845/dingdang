package me.ywork.salary.service;

import me.ywork.base.service.BizService;

public interface SalCalcuSalService extends BizService {
   
	/**
	 * 计算状态为预估的月度薪资报表
	 * 
	 * @param corpId 钉钉企业ID
	 * @return 计算的结果
	 */
	Boolean calcuSalReportAutomatic(String corpId);
}
