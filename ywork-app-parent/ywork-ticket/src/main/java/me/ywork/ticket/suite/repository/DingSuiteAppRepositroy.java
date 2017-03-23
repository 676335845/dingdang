package me.ywork.ticket.suite.repository;


import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.ticket.suite.entity.DingSuiteApp;


@Repository
public interface DingSuiteAppRepositroy extends IRepository<DingSuiteApp>{
	
	/**
	 * 
	 * @param fdSuiteId
	 * @return
	 */
	//List<OpPlatformSuiteApp> findBySuiteId(@Param("platformSuitId") String platformSuitId);
	
	//List<Integer> findpaltformAppIdsByAppIds(@Param("platformSuitId") String platformSuitId,@Param("appIds") List<String> appIds);

	//String findPlatformAppId(@Param("platformSuiteId") String platformSuiteId, @Param("appId") String appId);
}
