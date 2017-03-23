package me.ywork.org.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import me.ywork.org.entity.DingOrgDept;
import me.ywork.org.service.IDingOrgDeptService;

@Service(IDingOrgDeptService.SERVICE_NAME)
public class DingOrgDeptServiceImpl extends DingOrgElementServiceImpl implements IDingOrgDeptService{

	@Override
	public DingOrgDept loadDept(String orgId, String deptId) {
		DingOrgDept dingOrgDept = (DingOrgDept)getRepository().loadDept(orgId, deptId);
		return dingOrgDept;
	}

	@Override
	public String getRandomDeptId(String companyId) {
		// 循环随机取企业号ID，尝试5次
		String deptId = null;
		int count = 0;
		while (count < 5) {
			count ++;
			// 产生随机数
			double random = Math.random();
			deptId = getRepository().getRandomDeptId(companyId, random);
			if (StringUtils.isNotBlank(deptId)) {
				break;
			}
		}
		return deptId;
	}
}
