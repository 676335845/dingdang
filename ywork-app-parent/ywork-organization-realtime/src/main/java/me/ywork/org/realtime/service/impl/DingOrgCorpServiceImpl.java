package me.ywork.org.realtime.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heyi.utils.IdGenerator;

import me.ywork.context.CallContext;
import me.ywork.org.realtime.entity.DingOrgCorp;
import me.ywork.org.realtime.repository.DingOrgCorpRepository;
import me.ywork.org.realtime.service.IDingOrgCorpService;

@Service
public class DingOrgCorpServiceImpl implements IDingOrgCorpService{
	
	@Autowired
	private DingOrgCorpRepository dingOrgCorpRepository;
	
	public DingOrgCorpRepository getRepository() {
		return dingOrgCorpRepository;
	}
	
	
	@Override
	public DingOrgCorp getCorpByAppKey(String fdAppkey) {
		return dingOrgCorpRepository.getCorpByAppKey(fdAppkey);
	}

	@Override
	public void updateLastSyncTime(DingOrgCorp dingOrgCorp) {
		dingOrgCorpRepository.updateLastSyncTime(dingOrgCorp);
	}

	@Override
	public String updateDingOrgCorp(DingOrgCorp corp) {
		if(corp.getId()==null){
			corp.setId(IdGenerator.newId());
			dingOrgCorpRepository.insert(corp);
		}else{
			dingOrgCorpRepository.update(corp);
		}
		return corp.getId();
	}

	@Override
	public String getRandomCompanyId() {
		
		// 循环随机取企业号ID，尝试5次
		String companyId = null;
		int count = 0;
		while (count < 5) {
			count ++;
			// 产生随机数
			double random = Math.random();
			companyId = dingOrgCorpRepository.getRandomCompanyId(random);
			if (StringUtils.isNotBlank(companyId)) {
				break;
			}
		}
		return companyId;
	}

	@Override
	public void updateAbandoned(DingOrgCorp dingOrgCorp) {
		dingOrgCorpRepository.updateAbandoned(dingOrgCorp.getId());
	}

	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
}
