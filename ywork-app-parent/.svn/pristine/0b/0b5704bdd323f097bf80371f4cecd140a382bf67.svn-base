package me.ywork.suite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ywork.suite.entity.DingSuiteThirdMain;
import me.ywork.suite.repository.DingSuiteThirdMainRepositroy;
import me.ywork.suite.service.DingSuiteThirdMainService;

@Service
public class DingSuiteThirdMainServiceImpl implements DingSuiteThirdMainService {

	@Autowired
	private DingSuiteThirdMainRepositroy suiteThirdMainRepositroy;

	@Override
	public DingSuiteThirdMain findDingSuiteThirdByCorpId(String corpId, String suiteId) {
		return suiteThirdMainRepositroy.findDingSuiteThirdByCorpId(corpId, suiteId);
	}

	/**
	 * 简单分页
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<DingSuiteThirdMain> findByPage(int startIndex, int pageSize) {
		return null;//opPlatformSuiteThirdRepositroy.findByPage(startIndex, pageSize);
	}

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
