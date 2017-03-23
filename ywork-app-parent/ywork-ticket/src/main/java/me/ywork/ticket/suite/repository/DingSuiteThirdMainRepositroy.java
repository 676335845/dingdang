package me.ywork.ticket.suite.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.ticket.suite.entity.DingSuiteThirdMain;


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
	List<DingSuiteThirdMain> findByPage(@Param("suiteId") String suiteId,
										@Param("startIndex") int startIndex ,
										@Param("pageSize") int pageSize);
	
	
	/**
	 * 
	 * @param fdCorpId
	 * @return
	 */
	//List<OpPlatformSuiteThird> findOpPlatformSuiteThirdByLanCorp(@Param("lanCorpId") String lanCorpId,@Param("suiteId") String suiteId);
	
	
	/**
	 * 
	 * @param fdCorpId
	 * @return
	 */
	//OpPlatformSuiteThird findOpPlatformSuiteThirdByCorpType(@Param("corpId") String corpId,@Param("corpType") Short corpType);
	
	
	/**
	 * 
	 * @param
	 * @return
	 */
	//int updateOrgCallBack(@Param("fdIsOrgCallBack") Boolean fdIsOrgCallBack,@Param("id") String id);

	/**
	 * 查找企业所有授权记录
	 * @param corpId
	 * @return
     */
	//List<OpPlatformSuiteThird> findEnabledOpPlatformSuiteThirds(@Param("corpId") String corpId);
}
