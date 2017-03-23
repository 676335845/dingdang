package me.ywork.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.org.entity.DingOrgCorp;

@Repository
public interface DingOrgCorpRepository extends IRepository<DingOrgCorp> {
	
	void updateLastSyncTime(DingOrgCorp dingOrgCorp);
	
	int countAll();

	List<String> findAllIds();
	
	DingOrgCorp getCorpByAppKey(String fdAppkey);
	
	/**
	 * 用于压力测试，随机从组织架构中获取一个企业ID来进行测试
	 * <br/>由于该查询无分库键作为条件，所以只能从ding_suite_third表中查询
	 * @param  random 控制返回随机企业ID的随机数
	 * @return  随机获得的企业ID
	 */
	public String getRandomCompanyId(double random);
	
	/**
	 * 企业解散
	 * @param dingOrgCorp
	 */
	void updateAbandoned(@Param(value = "id") String id);
}
