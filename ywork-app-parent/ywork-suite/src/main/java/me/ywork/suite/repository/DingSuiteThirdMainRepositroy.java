package me.ywork.suite.repository;


import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.suite.entity.DingSuiteThirdMain;


public interface DingSuiteThirdMainRepositroy extends IRepository<DingSuiteThirdMain>{
	
	/**
	 * 
	 * @param fdCorpId
	 * @return
	 */
	DingSuiteThirdMain findDingSuiteThirdByCorpId(@Param("corpId") String corpId,@Param("suiteId") String suiteId);
	
	
}
