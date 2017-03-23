package me.ywork.ticket.suite.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.ticket.suite.entity.DingSuiteThirdApp;


@Repository
public interface DingSuiteThirdAppRepositroy extends IRepository<DingSuiteThirdApp>{

	List<DingSuiteThirdApp> findBySuiteThirdId(@Param("suiteThirdId") String suiteThirdId);
	
	String findAgentId(@Param("corpId") String corpId,@Param("suiteId") String suiteId,@Param("appId") String appId);
	
	Integer updateAgentStatus(@Param("corpId") String corpId, @Param("suiteId") String suiteId,
			@Param("agents") List<String> agentIds);
	
	//List<CorpAppVo> findCorpAppList(@Param("lanCorpId") String lanCorpId);
	
	//List<CorpAppCountVo> getCorpAppCount(@Param("lanCorpId") String lanCorpId,@Param("enabled") Boolean enabled);
	
	//PlatformSuiteVo findSuiteByLanCorp(@Param("lanCorpId") String lanCorpId);

	//PlatformSuiteVo findSuiteByCorp(@Param("lanCorpId") String lanCorpId, @Param("corpId") String corpId);

//	List<ModulePathModel> getModulePath(@Param("yworkCorpId") String corpId, @Param("modulePath") String modulePath,
//			@Param("thirdSysId") String innerSystemId);
//
//	ModulePathModel getCorpAgent(@Param("corpId") String corpId, @Param("agentId") Long agentId);
}
