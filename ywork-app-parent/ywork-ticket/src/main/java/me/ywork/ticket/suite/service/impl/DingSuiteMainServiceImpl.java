package me.ywork.ticket.suite.service.impl;

import java.util.concurrent.TimeUnit;

import org.redisson.cache.CacheableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ywork.ticket.suite.entity.DingSuiteMain;
import me.ywork.ticket.suite.repository.DingSuiteMainRepositroy;
import me.ywork.ticket.suite.service.DingSuiteMainService;

@Service
public class DingSuiteMainServiceImpl implements DingSuiteMainService {
	
	@Autowired
	private DingSuiteMainRepositroy dingSuiteMainRepositroy;

	@Autowired
	private CacheableService cacheableService;

	public DingSuiteMainServiceImpl() {
		
	}

	
	@Override
	public DingSuiteMain getDingSuiteFromCache(String id) {
		DingSuiteMain opPlatformSuite = null;
		Object cache = null;
		try{
			cache = cacheableService.getRawObjectFromCache(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(cache != null){
			opPlatformSuite = (DingSuiteMain)cache;
			return opPlatformSuite;
		}
		
		opPlatformSuite = dingSuiteMainRepositroy.getSuiteById(id);
		if(opPlatformSuite!=null){
			cacheableService.setRawObjectInCache(id, opPlatformSuite,1, TimeUnit.DAYS);
			return opPlatformSuite;
		}
		return null;
	}
}
