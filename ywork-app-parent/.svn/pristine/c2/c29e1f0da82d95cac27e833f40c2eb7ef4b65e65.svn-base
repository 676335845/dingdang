package me.ywork.org.realtime.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.MD5Util;

import me.ywork.context.CallContext;
import me.ywork.org.realtime.constants.BaseTreeConstant;
import me.ywork.org.realtime.constants.DingOrgConstant;
import me.ywork.org.realtime.entity.DingOrgActor;
import me.ywork.org.realtime.entity.DingOrgCorp;
import me.ywork.org.realtime.entity.DingOrgDept;
import me.ywork.org.realtime.entity.DingOrgElement;
import me.ywork.org.realtime.entity.DingOrgUser;
import me.ywork.org.realtime.entity.DingSuiteThirdMain;
import me.ywork.org.realtime.repository.DingOrgCorpRepository;
import me.ywork.org.realtime.repository.DingOrgElementRepository;
import me.ywork.org.realtime.repository.DingSuiteThirdMainRepositroy;
import me.ywork.org.realtime.service.IDingApiService;
import me.ywork.org.realtime.service.IDingOrgCorpService;
import me.ywork.org.realtime.service.IDingOrgElementService;

@Service(IDingOrgElementService.SERVICE_NAME)
public class DingOrgElementServiceImpl implements IDingOrgElementService{
	
	private Logger logger = LoggerFactory.getLogger(DingOrgElementServiceImpl.class);
	
	@Autowired
	private DingOrgElementRepository dingOrgElementRepository;
	
	public DingOrgElementRepository getRepository() {
		return dingOrgElementRepository;
	}
	
	
	@Override
	public void deleteElementById(String orgId, String deptId) {
		dingOrgElementRepository.deleteElementById(orgId, deptId);
	}
	@Override
	public void deleteElement(DingOrgElement element) {
		dingOrgElementRepository.deleteElement(element);
	}

	@Override
	public DingOrgElement loadElement(String orgId, String eleId) {
		return dingOrgElementRepository.loadElement(orgId, eleId);
	}

	@Override
	public DingOrgElement loadElementByImportInfo(String orgId,
			String importInfo) {
		return dingOrgElementRepository.loadElementByImportInfo(orgId, importInfo);
	}

	@Override
	public void updateSelective(DingOrgElement element) {
		dingOrgElementRepository.updateSelective(element);
	}

	@Override
	public void updateHierarchyId(String orgId, String newHierarchyId,
			String oldHierarchyId) {
		dingOrgElementRepository.updateHierarchyId(orgId, newHierarchyId, oldHierarchyId.length() + 1, oldHierarchyId.length(), oldHierarchyId);
	}

	@Override
	public void upsert(DingOrgElement element) {
		dingOrgElementRepository.upsert(element);
	}

	@Override
	public void updateAlterTime(DingOrgElement element) {
		dingOrgElementRepository.updateAlterTime(element);
	}

	@Override
	public List<String> loadUnavailableByOrgId(String orgId,Date lastAlterTime) {
		return dingOrgElementRepository.loadUnavailableByOrgId(orgId , lastAlterTime);
	}

	@Override
	public void updateUnavailableByOrgId(String orgId,Date lastAlterTime) {
		dingOrgElementRepository.updateUnavailableByOrgId(orgId , lastAlterTime);
	}

	@Override
	public void updateActorsHierarchyId(String orgId, DingOrgDept dept,
			String hierarchyId) {
		dingOrgElementRepository.updateActorsHierarchyId(orgId , dept.getId() , hierarchyId);
		
	}
	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	
/*******************华丽分割线****************************/
	private final DeptVoIdComparator deptVoIdComparator = new DeptVoIdComparator();
	
	static class DeptVoIdComparator implements Comparator<DeptVo> {
		@Override
		public int compare(DeptVo o1, DeptVo o2) {
			return o1.id.compareTo(o2.id);
		}
	}
	
	/**
	 * 企业授权通讯录实时同步
	 */
	@Override
	public String syncElement(String suiteId, String corpId) {
		
		DingSuiteThirdMain suiteThirdMain = dingSuiteThirdMainRepositroy.findDingSuiteThirdByCorpId(corpId, suiteId);
		
		DingOrgCorp corp = getDingorgCorp(suiteThirdMain);
		
		if(corp != null){
			corp.setFdLastSyncTime(new Date());
			
			//部门及人员
			Map<String, DingOrgDept> deptMaps = tranDept(suiteThirdMain, corp,true);
			
			if(deptMaps.isEmpty()){
				return null;
			}
			
			dingOrgCorpService.updateLastSyncTime(corp);
			
			//更新离职员工
			updateUnavailable(suiteThirdMain, corp);
		}
		
		return null;
	}
	
	
	
	//部门同步
	private Map<String, DingOrgDept> tranDept(DingSuiteThirdMain third ,DingOrgCorp dingOrg,boolean sysPerson) {
		// 获取钉钉部门列表
		Map<String, DingOrgDept> deptMap = new HashMap<>();
		
		List<DeptVo> deptList = listDept(third ,dingOrg);
		if (deptList == null || deptList.size() == 0) {
			return deptMap;
		}

		Collections.sort(deptList ,deptVoIdComparator);
		
		//部门导入
		List<DingOrgElement> depts = new ArrayList<DingOrgElement>();
		DingOrgDept dept = null;
		for (DeptVo qyDept : deptList) {
			dept = saveDept(qyDept,dingOrg);
			if(dept!=null){
				deptMap.put(String.valueOf(qyDept.id), dept);
			}
		}
		
		//更新父部门
		for (DeptVo qyDept : deptList) {
			dept = updateParent(qyDept ,dingOrg , deptMap);
			deptMap.put(dept.getId(), dept);
		}
		
		//更新层级ID
		for (DeptVo qyDept : deptList) {
			dept = updateHierarchy(qyDept ,dingOrg , deptMap);
			depts.add(dept);
		}
		
		if(depts.size()>0){
			dingOrgElementRepository.batchUpdateSelective(depts,2);
		}
		
		//更新员工信息
		if(sysPerson){
			JSONObject userDetailJson = null;
			for (DeptVo qyDept : deptList) {
				userDetailJson = getDetailListDeptUsers(third, String.valueOf(qyDept.id));
				tranPerson(third , dingOrg ,userDetailJson, deptMap);
			}
		}
		
		//更新主管理员
		updateCorpAdmin(third.getSuiteId(),third.getCorpId());
		
		return deptMap;
	}
	
	private void updateCorpAdmin(String suiteid,String corpid){
		JSONObject corpAdminsJson = dingApiService.getCorpAdmins(suiteid, corpid);
		if(corpAdminsJson==null){
			logger.error("get corp admin info error,return null,corpid:"+corpid+",suiteid:"+suiteid);
			return;
		}else if(corpAdminsJson.getInteger("errcode")!=0){
			logger.error("get corp admin info error,return data:"+corpAdminsJson.toJSONString()+",corpid:"+corpid+",suiteid:"+suiteid);
			return;
		}
		JSONObject jsonObj = null;
		JSONArray adminlist = corpAdminsJson.getJSONArray("adminList");
		String supAdmin = "";
		for (int i = 0; i < adminlist.size(); i++) {
			jsonObj = adminlist.getJSONObject(i);
			int level = jsonObj.getInteger("sys_level");
			if(1==level){
				supAdmin = jsonObj.getString("userid");
				break;
			}
		}
		//save admin
		if(StringUtils.isNotBlank(supAdmin)){
			dingOrgElementRepository.updateCorpSupAdmin(corpid, supAdmin);
		}
	}
	
	private void tranPerson(DingSuiteThirdMain third ,DingOrgCorp corp ,JSONObject userDetailJson,Map<String, DingOrgDept> deptMaps) {
		try {
			//单个部门的数据
			if(userDetailJson==null){
				logger.warn("钉钉接口返回部门人员为空,不同步公司人员：" + third.getCorpName());
				return;
			}
			int errcode = userDetailJson.getIntValue("errcode");
			if(errcode!=0){
				logger.error("获取人员详细信息失败:"+userDetailJson);
				return ;
			}
			
			JSONArray userDetailArray = userDetailJson.getJSONArray("userlist");
			JSONObject userDetailInfoJson = null;
			List<DingOrgElement> users = new ArrayList<DingOrgElement>();
			List<DingOrgElement> actors = new ArrayList<DingOrgElement>();
			
			for (int i = 0; i < userDetailArray.size(); i++) {
				userDetailInfoJson = userDetailArray.getJSONObject(i);
				DingOrgUser user =  new DingOrgUser();
				List<DingOrgActor> l = syncUserByJson(third.getSuiteId() , corp, userDetailInfoJson, deptMaps,null,user);
				users.add(user);
				actors.addAll(l);
			}
			//batch save
			if(users.size()>0){
				dingOrgElementRepository.batchUpdateSelective(users,8);
			}
			if(actors.size()>0){
				List<DingOrgElement> as = new ArrayList<DingOrgElement>();
				dingOrgElementRepository.batchUpdateSelective(actors,64);
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return ;
	}
	
	private DingOrgDept saveDept(DeptVo jsonDingDept , DingOrgCorp corp) {
		int id = jsonDingDept.id;
		
		String IMPORTKEY = "ding_dept_" + String.valueOf(id);
		
		String fdId = MD5Util.getMD5String(corp.getId().concat(IMPORTKEY));
		DingOrgDept orgDept = new DingOrgDept();
		orgDept.setId(fdId);
		orgDept.setFdCreateTime(new Date());
		orgDept.setFdAlterTime(corp.getFdLastSyncTime());
		orgDept.setFdFlagDeleted(false);
		orgDept.setFdImportInfo(IMPORTKEY);
		orgDept.setFdDingid(String.valueOf(id));
		orgDept.setFdIsAbandon(false);
		orgDept.setFdIsAvailable(true);
		orgDept.setFdIsSync(true);
		orgDept.setFdOrgid(corp.getId());
		orgDept.setFdOrgType(DingOrgConstant.ORG_TYPE_DEPT);
		orgDept.setFdName(jsonDingDept.name);
		
//		try {
//			this.upsert(orgDept);
//		} catch (Exception e) {
//			logger.error("",e);
//		}
		return orgDept;
	}
	
	/**
	 * 更新部门层级
	 * @param qyDept
	 * @param dingOrg
	 * @param deptMap
	 */
	private DingOrgDept updateParent(DeptVo qyDept, DingOrgCorp dingOrg ,Map<String, DingOrgDept> deptMap) {
		DingOrgDept dept = deptMap.get(String.valueOf(qyDept.id));
		
		if (qyDept.parentid != null) {
			
			DingOrgDept parentDept = deptMap.get(String.valueOf(qyDept.parentid));
			if (parentDept != null) {
				dept.setFdParentid(parentDept.getId());
				if(logger.isTraceEnabled()){
					logger.trace("--updateSelective DingOrgDept parent -start-:{}", dept.getId());
				}
				//this.updateSelective(dept);
				if(logger.isTraceEnabled()){
					logger.trace("--updateSelective DingOrgDept parent -end-:{}", dept.getId());
				}
			} else {
				if (qyDept.parentid.equals(1)) {
				} else {
					logger.error("cant find parent:{},{}", new Object[] {
							dingOrg.getId(), qyDept.parentid });
				}
			}
		}
		return dept;
	}
	
	/**
	 * 更新部门层级
	 * @param qyDept
	 * @param dingOrg
	 * @param deptMap
	 */
	private DingOrgDept updateHierarchy(DeptVo qyDept, DingOrgCorp dingOrg ,Map<String, DingOrgDept> deptMap) {
		DingOrgDept dept = deptMap.get(String.valueOf(qyDept.id));
		String hierarchyId = getHierarchyId(dept, dingOrg.getId(), deptMap ,null);
		
		//String oldHierarchyId = dept.getFdHierarchyId();
		dept.setFdHierarchyId( hierarchyId);
		return dept;
//		if (logger.isTraceEnabled()) {
//			logger.trace("--updateHierarchyId -start-:{}", dept.getId());
//		}
//		//this.updateSelective(dept);
//		if (logger.isTraceEnabled()) {
//			logger.trace("--updateHierarchyId -end-:{}", dept.getId());
//		}
//		if (oldHierarchyId!=null && !hierarchyId.equals(oldHierarchyId)) {
//			if(logger.isTraceEnabled()){
//				logger.trace("--update Hierarchy -start-:{}", dept.getId());
//			}
//			//this.updateHierarchyId(dingOrg.getId(), hierarchyId, oldHierarchyId);
//			if(logger.isTraceEnabled()){
//				logger.trace("--update Hierarchy -end-:{}", dept.getId());
//			}
//		}else{
//			if(StringUtils.isBlank(oldHierarchyId)){
//				//this.updateActorsHierarchyId(dingOrg.getId(), dept , hierarchyId);
//			}
//		}
	}
	
	private static String getHierarchyId(DingOrgElement ele,String orgId,Map<String, DingOrgDept> deptMap, DingOrgElementRepository dingOrgElementRepository){
		
		DingOrgDept parentDept = null;
		if (deptMap != null) {
			parentDept = deptMap.get(ele.getFdParentid());
		}else{
			parentDept = (DingOrgDept) dingOrgElementRepository.loadDept(orgId, ele.getFdParentid());
		}
		
		if(parentDept!=null){
			String phierarchyId= getHierarchyId(parentDept,orgId,deptMap, dingOrgElementRepository);
			return phierarchyId + ele.getId() + BaseTreeConstant.HIERARCHY_ID_SPLIT;
		}else{
			String hierarchyId = BaseTreeConstant.HIERARCHY_ID_SPLIT
								+ orgId
								+BaseTreeConstant.HIERARCHY_ID_SPLIT
								+ele.getId()
								+BaseTreeConstant.HIERARCHY_ID_SPLIT;
			return hierarchyId;
		}
	}
	
	// 获取钉钉组织架构部门列表
	private List<DeptVo> listDept(DingSuiteThirdMain third ,DingOrgCorp corp) {
		List<DeptVo> depts = new ArrayList<DeptVo>();
		JSONObject deptJson = null;
		try {
			deptJson = getDingDeptList(third);
			if(deptJson==null){
				logger.warn("钉钉接口返回部门数据为空,不同步" + third.getCorpName());
				return null;
			}
			JSONArray detpArray = deptJson.getJSONArray("department");
			DeptVo dept = null;
			JSONObject jsonObj = null;
			int id = 0;
			String name = "";
			for (int i = 0; i < detpArray.size(); i++) {
				jsonObj = detpArray.getJSONObject(i);
				id = jsonObj.getInteger("id");
				name = jsonObj.getString("name");
				Integer parentid = null;
				try {
					parentid = jsonObj.getInteger("parentid");
				} catch (Exception e) {
				}
				dept = new DeptVo();
				dept.id = id;
				dept.name = name;
				if(parentid!=null) {
					dept.parentid = parentid;
				}
				depts.add(dept);
			}
		} catch (Exception e) {
			logger.error("====部门获取异常："+deptJson, e);
		}
		return depts;
	}
	
	
	private JSONObject getDetailListDeptUsers(DingSuiteThirdMain third ,String deptId){
		JSONObject json= dingApiService.getDetailListDeptUsers(third, deptId);
		return json;
	}
	
	private JSONObject getDingDeptList(DingSuiteThirdMain third) {
		JSONObject json=null;
		
		//if(json==null){
		long startTime = System.currentTimeMillis();
		json = dingApiService.getDingDeptList(third);
		if(json!=null){
			if(logger.isTraceEnabled()) {
				logger.trace("获取部门耗时:("+third.getCorpName()+") --" + ((System.currentTimeMillis() - startTime)/1000) + "秒");
			}
		}
		return json;
	}
	
	private void updateUnavailable(DingSuiteThirdMain third ,DingOrgCorp corp) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(corp.getFdLastSyncTime());
	    cal.add(Calendar.DAY_OF_YEAR, -1);
	    
		Date unavailableTime = cal.getTime();
		//List<String> importInfoids = dingOrgElementService.loadUnavailableByOrgId(corp.getId() , unavailableTime);
		updateUnavailableByOrgId(corp.getId() , unavailableTime);
	}
	
	//corp表处理
	private DingOrgCorp getDingorgCorp(DingSuiteThirdMain sync) {
		
		DingOrgCorp corp = dingOrgCorpService.getCorpByAppKey(sync.getCorpId());
		
		if(corp!=null && corp.getFdIsAbandon()!=null && corp.getFdIsAbandon().booleanValue()){
			//更新企业
			corp.setFdIsAbandon(false);
			dingOrgCorpRepository.update(corp);
		}
		if (corp == null) {
			//新建该企业
			corp = new DingOrgCorp();
			corp.setId(sync.getCorpId());
			corp.setFdAppkey(sync.getCorpId());
			corp.setFdCorpName(sync.getCorpName());
			corp.setFdPicurl(sync.getLogoUrl());
			dingOrgCorpRepository.insert(corp);
		}
		return corp;
	}
	
	
	public List<DingOrgActor> syncUserByJson(String suiteId, DingOrgCorp corp ,
			JSONObject userDetailInfoJson ,
			Map<String, DingOrgDept> deptMap,
			//DingOrgElementRepository dingOrgElementRepository,
			//DingSuiteThirdMainRepositroy dingSuiteThirdMainRepositroy,
			 Boolean isAdmin,DingOrgUser orgUser) {
		
		List<DingOrgActor> dingOrgActors = new ArrayList<DingOrgActor>();
		try{
			//每个员工的信息
			String userid = userDetailInfoJson.getString("userid");
			String name = userDetailInfoJson.getString("name");
			Boolean active = userDetailInfoJson.getBoolean("active");
			String jobnumber = userDetailInfoJson.getString("jobnumber");
			//
			JSONArray deptArray = userDetailInfoJson.getJSONArray("department");
			Integer[] dept = new Integer[deptArray.size()];
			for(int j = 0 ; j < deptArray.size(); j++){
				dept[j] = deptArray.getInteger(j);
			}
			
			String position = userDetailInfoJson.getString("position");
//			String mobile=userDetailInfoJson.getString("mobile");
//			String email=userDetailInfoJson.getString("email");
			String avatar=userDetailInfoJson.getString("avatar");
			if(avatar!=null){
				if(avatar.startsWith("http:")){
					avatar = avatar.substring(5);
				}else if(avatar.startsWith("https:")){
					avatar = avatar.substring(6);
				}
			}
			if (isAdmin == null) {
				isAdmin = userDetailInfoJson.getBoolean("isAdmin");
			}
			Boolean isBoss = userDetailInfoJson.getBoolean("isBoss");
			Boolean isLeader = userDetailInfoJson.getBoolean("isLeader");
			
			String IMPORTKEY = "ding_user_" + userid;
			
			String fdId = MD5Util.getMD5String(corp.getId().concat(IMPORTKEY));
			//DingOrgUser orgUser = new DingOrgUser();
			orgUser.setId(fdId);
			orgUser.setFdOrgType(DingOrgConstant.ORG_TYPE_PERSON);
			orgUser.setFdCreateTime(new Date());
			orgUser.setFdUserid(userid);
			orgUser.setFdImportInfo(IMPORTKEY);
			orgUser.setFdDingid(userid);
			orgUser.setFdOrgid(corp.getId());
			orgUser.setFdIsAbandon(false);
			orgUser.setFdIsAvailable(active);
			orgUser.setFdIsSync(true);
			orgUser.setFdFlagDeleted(false);
			orgUser.setFdAlterTime(corp.getFdLastSyncTime());
			orgUser.setFdName(name);
			orgUser.setFdPosition(position);
//			orgUser.setFdMobile(mobile);
//			orgUser.setFdEmail(email);
			orgUser.setFdAvatar(avatar);
			orgUser.setFdIsAdmin(isAdmin);
			orgUser.setFdIsBoss(isBoss);
			orgUser.setFdIsLeader(isLeader);
			orgUser.setFdNo(jobnumber);
			
//			try {
//				dingOrgElementRepository.upsert(orgUser);
//			} catch (Exception e) {
//				logger.error("",e);
//			}
			
			List<DingOrgActor> actors = dingOrgElementRepository.loadActorsForUser(corp.getId(), orgUser.getId());
						
			List<String> nowActorIds = new ArrayList<>(dept.length);
			//save actors

			String ACTORIMPORTKEY = "";
			String fdActorId = "";
			String DEPTIMPORTKEY = "";
			String deptFdId = "";
			String hierarchyId = "";
			DingOrgActor orgActor = null;
			for (int j = 0; j < dept.length; j++) {
				ACTORIMPORTKEY = "ding_actor_" + dept[j] +"_" + userid;
				
				fdActorId = MD5Util.getMD5String(corp.getId().concat(ACTORIMPORTKEY));
				nowActorIds.add(fdActorId);
				
				orgActor = getActor(actors, fdActorId);
				if(orgActor == null){
					DingOrgDept orgPostDept = null;
					if (deptMap == null) {
						DEPTIMPORTKEY = "ding_dept_" + String.valueOf(dept[j]);
						deptFdId = MD5Util.getMD5String(corp.getId().concat(DEPTIMPORTKEY));
						orgPostDept  = (DingOrgDept) dingOrgElementRepository.loadDept(corp.getId(), deptFdId);
						if (orgPostDept == null) {
							logger.warn("部门为空:{},{}" ,corp.getId(), DEPTIMPORTKEY);
							//同步全部门
							DingSuiteThirdMain suiteThirdMain = dingSuiteThirdMainRepositroy.findDingSuiteThirdByCorpId(corp.getId(), suiteId);
							deptMap = tranDept(suiteThirdMain,corp,false);
							//再次同步此员工
							if(deptMap!=null){
								orgPostDept = deptMap.get(String.valueOf(dept[j]));
								logger.info("同步了部门信息:{}" ,corp.getId());
							}
						}
					}else{
						orgPostDept = deptMap.get(String.valueOf(dept[j]));
					}
					
					orgActor = new DingOrgActor();
					orgActor.setId(fdActorId);
					orgActor.setFdOrgType(DingOrgConstant.ORG_TYPE_ACTOR);
					orgActor.setFdCreateTime(new Date());
					if (orgPostDept != null) {
						orgActor.setFdParentid(orgPostDept.getId());
					}
					
					orgActor.setFdImportInfo(ACTORIMPORTKEY);
					orgActor.setFdOrgid(corp.getId());
					orgActor.setFdAlterTime(corp.getFdLastSyncTime());
					orgActor.setFdIsAbandon(false);
					orgActor.setFdIsAvailable(active);
					orgActor.setFdIsSync(true);
					//orgActor.setFdIsBusiness(active);
					orgActor.setFdFlagDeleted(false);
					orgActor.setFdName(name);
					orgActor.setFdUser(orgUser);
					orgActor.setFdNo(jobnumber);
					orgActor.setFdDingid(userid);
					
					hierarchyId = getHierarchyId(orgActor, corp.getId(), deptMap , dingOrgElementRepository);
					orgActor.setFdHierarchyId(hierarchyId);
					if(logger.isTraceEnabled()){
						logger.trace("--upsert DingOrgActor-start-:{}", ACTORIMPORTKEY);
					}
					dingOrgActors.add(orgActor);
//					try {
//						dingOrgElementRepository.upsert(orgActor);
//					} catch (Exception e) {
//						logger.error("",e);
//					}
					if(logger.isTraceEnabled()){
						logger.trace("--insert DingOrgActor-end-:{}", ACTORIMPORTKEY);
					}
				}else{
					DingOrgDept orgPostDept = null;
					if (deptMap == null) {
						DEPTIMPORTKEY = "ding_dept_" + String.valueOf(dept[j]);
						deptFdId = MD5Util.getMD5String(corp.getId().concat(DEPTIMPORTKEY));
						orgPostDept  = (DingOrgDept) dingOrgElementRepository.loadDept(corp.getId(), deptFdId);
						if (orgPostDept == null) {
							logger.error("部门为空:{},{}" ,corp.getId(), DEPTIMPORTKEY);
							//同步全部门
							DingSuiteThirdMain suiteThirdMain = dingSuiteThirdMainRepositroy.findDingSuiteThirdByCorpId(corp.getId(), suiteId);
							deptMap = tranDept(suiteThirdMain,corp,false);
							//再次同步此员工
							if(deptMap!=null){
								orgPostDept = deptMap.get(String.valueOf(dept[j]));
								logger.info("同步了部门信息:{}" ,corp.getId());
							}
						}
					}else{
						orgPostDept = deptMap.get(String.valueOf(dept[j]));
					}
					orgActor = new DingOrgActor();
					orgActor.setId(fdActorId);
					orgActor.setFdOrgid(corp.getId());
					orgActor.setFdOrgType(DingOrgConstant.ORG_TYPE_ACTOR);
					orgActor.setFdCreateTime(new Date());
					if (orgPostDept != null) {
						orgActor.setFdParentid(orgPostDept.getId());
					}
					
					hierarchyId = getHierarchyId(orgActor, corp.getId(), deptMap , dingOrgElementRepository);
					
					orgActor.setFdHierarchyId(hierarchyId);
					orgActor.setFdName(name);
					orgActor.setFdIsAvailable(active);
					orgActor.setFdIsSync(true);
					orgActor.setFdNo(jobnumber);
					orgActor.setFdAlterTime(corp.getFdLastSyncTime());
					//dingOrgElementRepository.upsert(orgActor);
					dingOrgActors.add(orgActor);
				}
				if(StringUtils.isBlank(orgActor.getFdHierarchyId())){
					logger.warn("层级id为空:{} , {}" , corp.getId(), orgActor.getId());
				}
			}
			
			for (DingOrgActor dingOrgActor : actors) { //删除无用的actor
				if(! nowActorIds.contains(dingOrgActor.getId())){
					dingOrgElementRepository.deleteElement(dingOrgActor);
					if(logger.isTraceEnabled()){
						logger.trace("--delete ActorsForUser DingOrgUser-end-:{}", dingOrgActor.getId());
					}
				}
			}
			
			return dingOrgActors;
		}catch(Exception e){
			logger.error("单个用户异常："+userDetailInfoJson, e);
			e.printStackTrace();
		}
		return null;
	}
	
	private static DingOrgActor getActor(List<DingOrgActor> actors  , String actorId){
		for (DingOrgActor dingOrgActor : actors) {
			if(dingOrgActor.getId().equals(actorId))
				return dingOrgActor;
		}
		return null;
	}
	
	
	static class DeptVo {
		private Integer id;
		private String name;
		private Integer parentid;
	}
	
	
	@Autowired
	private IDingOrgCorpService dingOrgCorpService;
	
	@Autowired
	private DingOrgCorpRepository dingOrgCorpRepository;
	
	@Autowired
	private DingSuiteThirdMainRepositroy dingSuiteThirdMainRepositroy;
	
	@Autowired
	private IDingApiService dingApiService;
	
	
	
	
	
	
	
}
