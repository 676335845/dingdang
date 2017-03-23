package me.ywork.ticket.suite.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import me.ywork.context.CallContext;
import me.ywork.ticket.suite.entity.DingSuiteApp;
import me.ywork.ticket.suite.service.DingSuiteAppService;

@Service
public class DingSuiteAppServiceImpl implements DingSuiteAppService{
	
	@Override
	public List<DingSuiteApp> findBySuiteId(String suiteId) {
		return null;//dingSuiteQyappMapper.findBySuiteId(suiteId);
	}
	
	
	@Override
	public List<Integer> findplatformAppIdsByAppIds(String suiteId, List<String> appIds) {
		return null;//dingSuiteQyappMapper.findpaltformAppIdsByAppIds(suiteId,appIds);
	}


	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
