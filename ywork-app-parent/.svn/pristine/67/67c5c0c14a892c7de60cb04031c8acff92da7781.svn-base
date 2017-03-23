package me.ywork.org.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.org.entity.DingSuiteThirdMain;
import me.ywork.org.model.DingSuiteThirdForSync;


public interface DingSuiteThirdMainRepositroy extends IRepository<DingSuiteThirdMain>{
	
	/**
	 * 
	 * @param fdCorpId
	 * @return
	 */
	DingSuiteThirdMain findDingSuiteThirdByCorpId(@Param("corpId") String corpId,@Param("suiteId") String suiteId);
	

	/**
	 * 简单分页
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<DingSuiteThirdForSync> findByPage(int startIndex , int pageSize);
	
	void updateEnable(@Param("corpId") String corpId,@Param("suiteId") String suiteId);
	
	long countAll();
}
