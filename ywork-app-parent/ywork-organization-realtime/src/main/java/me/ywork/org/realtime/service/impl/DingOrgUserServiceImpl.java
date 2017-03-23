package me.ywork.org.realtime.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.MD5Util;

import me.ywork.context.CallContext;
import me.ywork.org.api.model.DingOrgUserVo;
import me.ywork.org.realtime.constants.DingOrgConstant;
import me.ywork.org.realtime.entity.DingOrgActor;
import me.ywork.org.realtime.entity.DingOrgCorp;
import me.ywork.org.realtime.entity.DingOrgElement;
import me.ywork.org.realtime.entity.DingOrgUser;
import me.ywork.org.realtime.repository.DingOrgElementRepository;
import me.ywork.org.realtime.service.IDingOrgCorpService;
import me.ywork.org.realtime.service.IDingOrgUserService;

@Service(IDingOrgUserService.SERVICE_NAME)
public class DingOrgUserServiceImpl extends DingOrgElementServiceImpl implements IDingOrgUserService{

	
	private static Logger logger = LoggerFactory
			.getLogger(DingOrgUserServiceImpl.class);
	
	@Autowired
	private IDingOrgCorpService dingOrgCorpService;
	
	
	@Override
	public DingOrgElement loadUser(String orgId, String userId) {
		return getRepository().loadUser(orgId, userId);
	}

	@Override
	public String updateUser(DingOrgUser orgUser) {
		if (orgUser.getId() == null) {
			String IMPORTKEY = "ding_user_" + orgUser.getFdUserid();
			String fdId = MD5Util.getMD5String(orgUser.getFdOrgid().concat(
					IMPORTKEY));

			orgUser.setId(fdId);
			orgUser.setFdOrgType(DingOrgConstant.ORG_TYPE_PERSON);
			orgUser.setFdCreateTime(new Date());
			orgUser.setFdUserid(orgUser.getFdUserid());
			orgUser.setFdImportInfo(IMPORTKEY);
			orgUser.setFdDingid(orgUser.getFdUserid());
			orgUser.setFdIsAbandon(false);
			orgUser.setFdIsAvailable(true);
			//orgUser.setFdIsBusiness(true);
			orgUser.setFdFlagDeleted(false);
			orgUser.setFdAlterTime(new Date());

			getRepository().insert(orgUser);
		} else {
			getRepository().updateUser(orgUser);
		}
		return orgUser.getId();
	}

	@Override
	public DingOrgUserVo findDingOrgUserById(String corpId, String userId) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalStateException(
					"findDingOrgUserById - parameter corpId is null or emtpy.");

		}

		if (StringUtils.isBlank(userId)) {
			throw new IllegalStateException(
					"findDingOrgUserById - parameter userId is null or emtpy.");

		}

		DingOrgUser dingOrgUser = this.getRepository()
				.findDingOrgUserByCorpIdUserId(corpId, userId);

		DingOrgUserVo dingOrgUserVo = null;
		if (dingOrgUser != null) {
			dingOrgUserVo = DingOrgUser.toVo(dingOrgUser);
		}

		return dingOrgUserVo;
	}

	@Override
	public Map<String, String> getPersonIdsByCorpIdUserIds(String corpId,
			List<String> userIds) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalStateException(
					"getPersonIdsByCorpIdUserIds - parameter corpId is null or emtpy.");

		}

		if (userIds == null || userIds.isEmpty()) {
			throw new IllegalStateException(
					"getPersonIdsByCorpIdUserIds - parameter userIds is null or emtpy.");

		}

		List<DingOrgUser> dingOrgUsers = this.getRepository()
				.getDingOrgUsersByCorpIdUserIds(corpId, userIds);

		Map<String, String> userIdPersonIdMap = new HashMap<String, String>();

		if (dingOrgUsers != null) {
			for (DingOrgUser user : dingOrgUsers) {
				if (user == null) {
					continue;
				}

				userIdPersonIdMap.put(user.getFdUserid(), user.getId());
			}
		}

		return userIdPersonIdMap;
	}

	@Override
	public List<String> getDingOrgUserVosByCompanyIdDeptId(String comapnyId,
			String deptId, int count) {
		if (StringUtils.isBlank(comapnyId)) {
			throw new IllegalArgumentException(
					"getDingOrgUserVosByCompanyIdDeptId - companyId is null or empty.");
		}

		if (StringUtils.isBlank(deptId)) {
			throw new IllegalArgumentException(
					"getDingOrgUserVosByCompanyIdDeptId - deptId is null or empty.");
		}

		return this.getRepository().getDingOrgUserVosByCompanyIdDeptId(comapnyId,
				deptId, count);
	}

	@Override
	public List<DingOrgActor> getActorsForUser(String companyId, String fdUserId) {
		if (StringUtils.isBlank(companyId)) {
			throw new IllegalArgumentException(
					"getActorsForUser - companyId is null or empty.");
		}

		if (StringUtils.isBlank(fdUserId)) {
			throw new IllegalArgumentException(
					"getDingOrgUserVosByCompanyIdDeptId - fdUserId is null or empty.");
		}
		return this.getRepository().loadActorsForUser(companyId, fdUserId);
	}
	
	@Override
	public String updateUser(String suiteId , String corpId, JSONObject userDetailInfoJson,
			Date alterTime,Boolean isAdmin) {
		DingOrgCorp corp = dingOrgCorpService.getCorpByAppKey(corpId);
		if(corp!=null){
			List<DingOrgElement> users = new ArrayList<DingOrgElement>();
			List<DingOrgElement> actors = new ArrayList<DingOrgElement>();
			
			DingOrgUser user =  new DingOrgUser();
			List<DingOrgActor> l = super.syncUserByJson(suiteId , corp, userDetailInfoJson, null,isAdmin,user);
			users.add(user);
			actors.addAll(l);
			dingOrgElementRepository.batchUpdateSelective(users,8);
			if(actors.size()>0){
				dingOrgElementRepository.batchUpdateSelective(actors,64);
				List<DingOrgElement> as = new ArrayList<DingOrgElement>();
				for(DingOrgElement e : actors){
					DingOrgActor a = (DingOrgActor)e;
					if(a.getFdUser()!=null){
						as.add(a);
					}
				}
				if(as.size()>0){
					dingOrgElementRepository.batchUpsertActors(as);
				}
			}
			return user.getId();
		}else{
			logger.warn("企业不存在:{}" , corpId);
//			DingSuiteThird third = dingSuiteThirdMapper.findDingSuiteThirdByCorpId(corpId, suiteId);
//			if (third != null) {
//				sysOrgManageService.syncDingOrg(DingSuiteThird.toVo(third));
//			}
			return null;
		}
	}
	
	@Autowired
	private DingOrgElementRepository dingOrgElementRepository;

	@Override
	public String removeUser(String suiteId, String corpId, String[] userIds) {
		//element表 is_availate = 0
		if(userIds==null || userIds.length == 0){
			return null;
		}
		dingOrgElementRepository.updateUnavailableByDingId(corpId, userIds);
		return null;
	}

	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
}