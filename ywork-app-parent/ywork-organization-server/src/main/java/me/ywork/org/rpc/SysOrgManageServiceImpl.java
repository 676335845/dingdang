package me.ywork.org.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;

import me.ywork.org.api.model.DingOrgCorpVo;
import me.ywork.org.api.model.DingOrgDeptVo;
import me.ywork.org.api.model.DingOrgUserVo;
import me.ywork.org.api.rpc.ISysOrgManageService;
import me.ywork.org.entity.DingOrgCorp;
import me.ywork.org.entity.DingOrgUser;
import me.ywork.org.repository.DingOrgElementRepository;
import me.ywork.org.service.IDingOrgCorpService;
import me.ywork.org.service.IDingOrgUserService;

/**
 * 组织架构管理服务
 * @author sulta
 *
 */
public class SysOrgManageServiceImpl implements ISysOrgManageService{
	
	
	@Autowired
	private IDingOrgCorpService dingOrgCorpService;
	
	@Autowired
	private IDingOrgUserService dingOrgUserService;
	
	
	@Override
	public String updateUser(JSONObject obj,String suiteId,String corpId,Boolean isAdmin) {
		return dingOrgUserService.updateUser(suiteId ,corpId , obj, new Date(),isAdmin);
	}
	
	@Override
	public String createDingOrgCorp(DingOrgCorpVo corp) {
		DingOrgCorp model = new DingOrgCorp();
		BeanUtils.copyProperties(corp, model);
		return dingOrgCorpService.updateDingOrgCorp(model);
	}
	
	@Override
	public void updateDingOrgCorp(DingOrgCorpVo corp) {
		DingOrgCorp model = new DingOrgCorp();
		BeanUtils.copyProperties(corp, model);
		dingOrgCorpService.updateDingOrgCorp(model);
	}
	
	@Override
	public DingOrgCorpVo findDingOrgCorpById(String corpId) {
		return null;
	}

	@Autowired
	private DingOrgElementRepository dingOrgElementRepository;
	
	@Override
	public DingOrgUserVo findDingOrgUserById(String corpId, String userId) {
		
		DingOrgUser u = (DingOrgUser)dingOrgElementRepository.loadUserByDingId(corpId,  userId);
		if(u!=null){
			return u.toVo(u);
		}
		return null;
	}
	
	@Override
	public DingOrgDeptVo findDingOrgDeptByCorpIdAndDeptId(String corpId,
			String deptId) {
		return null;
	}
	
	
	

	@Override
	public List<String> getDingOrgUserIdsByCompanyIdDeptId(String corpId,
			String deptId, Boolean isFetchAll) {
		Assert.notNull(corpId);
		Assert.notNull(deptId);
		
		if(isFetchAll==null)isFetchAll = false;
		
		List<String> retIds = new ArrayList<>();
		
		
		return retIds;
	}
	
	
	@Override
	public Set<String> getDingOrgUserParentIdList(String corpId, String userId) {
		return Collections.EMPTY_SET;
	}
}
