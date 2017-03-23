package me.ywork.org.service;

import me.ywork.base.service.BizService;
import me.ywork.org.entity.DingOrgCorp;

public interface IDingOrgCorpService extends BizService{
	
	public void updateLastSyncTime(DingOrgCorp dingOrgCorp);
	
	public DingOrgCorp getCorpByAppKey(String fdAppkey);

	public String updateDingOrgCorp(DingOrgCorp model);
	
	/**
	 * 用于压力测试，随机从组织架构中获取一个企业ID来进行测试
	 * <br/>由于该查询无分库键作为条件，所以只能从ding_suite_third表中查询
	 * @return  随机获得的企业ID
	 */
	public String getRandomCompanyId();
	
	/**
	 * 企业解散
	 * @param dingOrgCorp
	 */
	void updateAbandoned(DingOrgCorp dingOrgCorp);
}
