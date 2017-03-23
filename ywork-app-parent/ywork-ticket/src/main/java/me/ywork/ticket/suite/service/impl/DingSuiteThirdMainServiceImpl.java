package me.ywork.ticket.suite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ywork.ticket.suite.entity.DingSuiteThirdMain;
import me.ywork.ticket.suite.repository.DingSuiteThirdMainRepositroy;
import me.ywork.ticket.suite.service.DingSuiteThirdMainService;

@Service
public class DingSuiteThirdMainServiceImpl implements DingSuiteThirdMainService {

	@Autowired
	private DingSuiteThirdMainRepositroy opPlatformSuiteThirdRepositroy;

	@Override
	public DingSuiteThirdMain findDingSuiteThirdByCorpId(String corpId, String suiteId) {
		return opPlatformSuiteThirdRepositroy.findDingSuiteThirdByCorpId(corpId, suiteId);
	}



	/**
	 * 简单分页
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<DingSuiteThirdMain> findByPage(String suiteId,int startIndex, int pageSize) {
		return opPlatformSuiteThirdRepositroy.findByPage(suiteId,startIndex, pageSize);
	}

	@Override
	public long countAll() {
		return 0l;//opPlatformSuiteThirdRepositroy.countAll();
	}

	@Override
	public int updateOrgCallBack(Boolean fdIsOrgCallBack, String id) {
		return 0;//opPlatformSuiteThirdRepositroy.updateOrgCallBack(fdIsOrgCallBack, id);
	}

	@Override
	public List<DingSuiteThirdMain> findEnabledOpPlatformSuiteThirds(String corpId) {
		return null;//opPlatformSuiteThirdRepositroy.findEnabledOpPlatformSuiteThirds(corpId);
	}
}
