package me.ywork.suite.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.suite.entity.DingSuiteThirdApp;


@Repository
public interface DingSuiteThirdAppRepositroy extends IRepository<DingSuiteThirdApp>{

	
	String findAgentId(@Param("corpId") String corpId,@Param("suiteId") String suiteId,@Param("appId") String appId);
	
	Integer updateAgentStatus(@Param("corpId") String corpId, @Param("suiteId") String suiteId,
			@Param("agents") List<String> agentIds);
	
}
