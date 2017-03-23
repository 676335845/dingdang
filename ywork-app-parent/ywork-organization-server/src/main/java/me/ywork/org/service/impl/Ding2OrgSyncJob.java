package me.ywork.org.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.MD5Util;

import me.ywork.org.constants.BaseTreeConstant;
import me.ywork.org.constants.DingOrgConstant;
import me.ywork.org.entity.DingOrgActor;
import me.ywork.org.entity.DingOrgCorp;
import me.ywork.org.entity.DingOrgDept;
import me.ywork.org.entity.DingOrgElement;
import me.ywork.org.entity.DingOrgUser;
import me.ywork.org.model.DingSuiteThirdForSync;
import me.ywork.org.repository.DingOrgCorpRepository;
import me.ywork.org.repository.DingOrgElementRepository;
import me.ywork.org.repository.DingSuiteThirdMainRepositroy;
import me.ywork.org.service.IDingApiService;
import me.ywork.org.service.IDingOrgCorpService;
import me.ywork.org.service.IDingOrgElementService;

@Service("ding2OrgSyncJob")
public class Ding2OrgSyncJob implements InitializingBean, Runnable{
	private static Logger logger = LoggerFactory.getLogger(Ding2OrgSyncJob.class);
	
	@Autowired
	private IDingApiService dingApiService;
		
	@Autowired
	private IDingOrgCorpService dingOrgCorpService;
	
	@Autowired
	private DingOrgCorpRepository dingOrgCorpRepository;
	
	@Resource(name = "dingOrgElementService")
	private IDingOrgElementService dingOrgElementService;
	
	@Autowired
	private DingOrgElementRepository dingOrgElementRepository;
	
	@Autowired
	private DingSuiteThirdMainRepositroy dingSuiteThirdMainRepositroy;
	
	/**
	 * 线程数
	 */
	private final static int cores = 16;//4 * Runtime.getRuntime().availableProcessors();
	
	private final ArrayBlockingQueue<DingSuiteThirdForSync> queue = new ArrayBlockingQueue<>(cores);
	
	private final Executor executor = Executors.newFixedThreadPool(cores);
	
	private final DeptVoIdComparator deptVoIdComparator = new DeptVoIdComparator();
	
	protected volatile boolean isShuttingDown = false;
	
	private final Thread workThread;
	
	public Ding2OrgSyncJob() {
		workThread = new Thread(this);
		workThread.setDaemon(true);
		workThread.setName("org-sync-thread");
	}
	
	@Override
	public void run() {
		try {
			logger.info("正在等待同步组织架构....");
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e1) {
			
		}
		
		while (!this.isShuttingDown) {
			try {
				long start = System.currentTimeMillis();
				if(Thread.currentThread().isInterrupted()){
					break;
				}
				Date today = new Date();
				//双11时不跑同步任务
				//if(!Ding2OrgUtil.todayIsDisabledSyncDay(null)){
					//按套件区分 企业总量
					long count = dingSuiteThirdMainRepositroy.countAll();
					List<DingSuiteThirdForSync> dingSuiteThirdList = null;
					for (int i = 0; i < count; i = i + cores) {
						if(isShuttingDown) break;
						dingSuiteThirdList = dingSuiteThirdMainRepositroy.findByPage(i, cores);
						for (DingSuiteThirdForSync dingSuiteThird : dingSuiteThirdList) {
							queue.put(dingSuiteThird);
							doSync(today);
						}
					}
				//}
				long end = System.currentTimeMillis();
				long s = (end-start);
				logger.info("本轮已结束，共耗时："+s+"毫秒，正在等待下一轮同步组织架构..");
				
				Thread.sleep(1000 * 60 * 60);
			} catch (InterruptedException e) {
				break;
			} catch (Throwable e) {
				logger.error("", e);
			}
		}
		this.close();
	}
	
	private void doSync(final Date date) throws InterruptedException {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					DingSuiteThirdForSync third = queue.take();
										
					if(logger.isTraceEnabled()){
						logger.trace(Thread.currentThread().getName()+": 处理: " + third.getCorpName() +"("+third.getCorpId()+")");
					}
					DingOrgCorp corp = Ding2OrgSyncJob.this.getDingorgCorp(third);
					if(corp != null){
						if(corp.getFdLastSyncTime()!=null && isSameDate(corp.getFdLastSyncTime(),date)){
							//logger.info("今天已同步 , corp name:"+corp.getFdCorpName());
							//return;
						}
						corp.setFdLastSyncTime(new Date());
						
						//同步
						Map<String, DingOrgDept> deptMaps = Ding2OrgSyncJob.this.tranDept(third, corp);
						
						if(deptMaps.isEmpty()){
							return;
						}
						
						deptMaps = null;//标记对象回收
						
						dingOrgCorpService.updateLastSyncTime(corp);
						
						//更新离职员工
						updateUnavailable(third, corp);
					}
				}catch (InterruptedException e) {
					isShuttingDown = true;
				} catch (Exception e) {
					logger.error("error", e);
				}
			}
		});
	}
	
	private static boolean isSameDate(Date date1, Date date2) {
	       Calendar cal1 = Calendar.getInstance();
	       cal1.setTime(date1);

	       Calendar cal2 = Calendar.getInstance();
	       cal2.setTime(date2);

	       boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
	               .get(Calendar.YEAR);
	       boolean isSameMonth = isSameYear
	               && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
	       boolean isSameDate = isSameMonth
	               && cal1.get(Calendar.DAY_OF_MONTH) == cal2
	                       .get(Calendar.DAY_OF_MONTH);

	       return isSameDate;
	   }
	
	
	protected void close() {
		if (this.isShuttingDown)
			return;
		this.isShuttingDown = true;
		
		try{
			workThread.interrupt();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private DingOrgCorp getDingorgCorp(DingSuiteThirdForSync sync) {
		
		DingOrgCorp corp = dingOrgCorpService.getCorpByAppKey(sync.getCorpId());
		
		if(corp!=null && corp.getFdIsAbandon()!=null && corp.getFdIsAbandon().booleanValue()){
			if(logger.isDebugEnabled()) {
				logger.debug("企业已解散,不同步此企业的数据：" + corp.getFdCorpName());
			}
			return null;
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


	//部门同步
	private Map<String, DingOrgDept> tranDept(DingSuiteThirdForSync third ,DingOrgCorp dingOrg) {
		Map<String, DingOrgDept> deptMap = new HashMap<>();
		// 获取钉钉部门列表
		List<DeptVo> deptList = listDept(third ,dingOrg);
		if (deptList == null || deptList.size() == 0) {
			return deptMap;
		}
		Collections.sort(deptList ,deptVoIdComparator);
		
		//部门导入
		DingOrgDept dept = null;
		List<DingOrgElement> depts = new ArrayList<DingOrgElement>();
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
			dingOrgElementService.batchUpsert(depts,2);
		}
		
		//更新员工信息
		JSONObject userDetailJson = null;
		for (DeptVo qyDept : deptList) {
			userDetailJson = getDetailListDeptUsers(third, String.valueOf(qyDept.id));
			Ding2OrgSyncJob.this.tranPerson(third , dingOrg ,userDetailJson, deptMap);
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
	
	// 获取钉钉组织架构部门列表
	private List<DeptVo> listDept(DingSuiteThirdForSync third ,DingOrgCorp corp) {
		List<DeptVo> depts = new ArrayList<DeptVo>();
		JSONObject deptJson = null;
		try {
			deptJson = getDingDeptList(third);
			if(deptJson==null){
				logger.warn("钉钉接口返回部门数据为空,不同步" + third.getCorpName());
				return null;
			}
			int errcode = deptJson.getIntValue("errcode");
			if(errcode!=0){
				if(errcode==70001){ //企业不存在或者已经被解散
					updateDingOrgCorpAbandon(corp);
					updateDingSuiteThirdMainAbandon(third);
					return null;
				}
				logger.error("获取部门失败:"+deptJson);
				return null;
			}
			JSONArray detpArray = deptJson.getJSONArray("department");
			DeptVo dept = null;
			JSONObject jsonObj = null;
			int id = 0 ;
			String name = "";
			Integer parentid = null;
			for (int i = 0; i < detpArray.size(); i++) {
				jsonObj = detpArray.getJSONObject(i);
				id = jsonObj.getInteger("id");
				name = jsonObj.getString("name");
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
	
	/**
	 * 企业解散
	 * @param corp
	 */
	private void updateDingOrgCorpAbandon(DingOrgCorp corp) {
		dingOrgCorpService.updateAbandoned(corp);
	}
	
	/**
	 * 第三方enable置为0
	 * @param corp
	 */
	private void updateDingSuiteThirdMainAbandon(DingSuiteThirdForSync thirdVo) {
		dingSuiteThirdMainRepositroy.updateEnable(thirdVo.getCorpId(),thirdVo.getSuiteId());
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
//			dingOrgElementService.upsert(orgDept);
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
				//dingOrgElementService.updateSelective(dept);
				if(logger.isTraceEnabled()){
					logger.trace("--updateSelective DingOrgDept parent -end-:{}", dept.getId());
				}
			} else {
				if (qyDept.parentid.equals(1)) {
				} else {
					logger.error("cant find parent:{},{}", new Object[] {dingOrg.getId(), qyDept.parentid });
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
		dept.setFdHierarchyId(hierarchyId);
		return dept;
//		if (logger.isTraceEnabled()) {
//			logger.trace("--updateHierarchyId -start-:{}", dept.getId());
//		}
//		dingOrgElementService.updateSelective(dept);
//		if (logger.isTraceEnabled()) {
//			logger.trace("--updateHierarchyId -end-:{}", dept.getId());
//		}
//		if (oldHierarchyId!=null && !hierarchyId.equals(oldHierarchyId)) {
//			if(logger.isTraceEnabled()){
//				logger.trace("--update Hierarchy -start-:{}", dept.getId());
//			}
//			dingOrgElementService.updateHierarchyId(dingOrg.getId(), hierarchyId, oldHierarchyId);
//			if(logger.isTraceEnabled()){
//				logger.trace("--update Hierarchy -end-:{}", dept.getId());
//			}
//		}else{
//			if(StringUtils.isBlank(oldHierarchyId)){
//				dingOrgElementService.updateActorsHierarchyId(dingOrg.getId(), dept , hierarchyId);
//			}
//		}
	}

	private static String getHierarchyId(DingOrgElement ele,String orgId,Map<String, DingOrgDept> deptMap, DingOrgElementRepository dingOrgElementRepository){
		
		DingOrgDept parentDept = null;
		if (deptMap != null) {
			parentDept = deptMap.get(ele.getFdParentid());
		}else{
			//这里是人员的层级id计算。 此时部门已经导入了 就只需要获取到所属部门的层级id后面追加自己的id， 而不是再递归获取所有层级部门
			parentDept = (DingOrgDept) dingOrgElementRepository.loadDept(orgId, ele.getFdParentid());
			if(StringUtils.isNotBlank(parentDept.getFdHierarchyId())){
				String phierarchyId = parentDept.getFdHierarchyId()+ele.getId()+BaseTreeConstant.HIERARCHY_ID_SPLIT;
				return phierarchyId;
			}
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
	
	private void updateUnavailable(DingSuiteThirdForSync third ,DingOrgCorp corp) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(corp.getFdLastSyncTime());
	    cal.add(Calendar.DAY_OF_YEAR, -1);
	    
		Date unavailableTime = cal.getTime();
		//List<String> importInfoids = dingOrgElementService.loadUnavailableByOrgId(corp.getId() , unavailableTime);
		dingOrgElementService.updateUnavailableByOrgId(corp.getId() , unavailableTime);
	}
	
	private void tranPerson(DingSuiteThirdForSync third ,DingOrgCorp corp ,JSONObject userDetailJson,Map<String, DingOrgDept> deptMaps) {
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
				List<DingOrgActor> l = syncUserByJson(third.getSuiteId() , corp,
						userDetailInfoJson, deptMaps, dingOrgElementRepository , dingSuiteThirdMainRepositroy,null,user);
				users.add(user);
				actors.addAll(l);
			}
			//batch save
			if(users.size()>0){
				dingOrgElementService.batchUpsert(users,8);
			}
			if(actors.size()>0){
				List<DingOrgElement> as = new ArrayList<DingOrgElement>();
				dingOrgElementService.batchUpsert(actors,64);
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
	
	public static List<DingOrgActor> syncUserByJson(String suiteId, DingOrgCorp corp ,
			JSONObject userDetailInfoJson ,
			Map<String, DingOrgDept> deptMap,
			DingOrgElementRepository dingOrgElementRepository,
			DingSuiteThirdMainRepositroy dingSuiteThirdMainRepositroy,
			 Boolean isAdmin,DingOrgUser orgUser) {
		
		List<DingOrgActor> dingOrgActors = new ArrayList<DingOrgActor>();
		try{
			//每个员工的信息
			String userid = userDetailInfoJson.getString("userid");
			String name = userDetailInfoJson.getString("name");
			Boolean active = userDetailInfoJson.getBoolean("active"); //激活状态
			String jobnumber = userDetailInfoJson.getString("jobnumber");
			JSONArray deptArray = userDetailInfoJson.getJSONArray("department");
			Integer[] dept = new Integer[deptArray.size()];
			for(int j = 0 ; j < deptArray.size(); j++){
				dept[j] = deptArray.getInteger(j);
			}
			String position = userDetailInfoJson.getString("position");
			//String mobile=userDetailInfoJson.getString("mobile");
			//String email=userDetailInfoJson.getString("email");
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
			//orgUser = new DingOrgUser();
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
			//orgUser.setFdMobile(mobile);
			//orgUser.setFdEmail(email);
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
			for (int j = 0; j < dept.length; j++) {
				String ACTORIMPORTKEY = "ding_actor_" + dept[j] +"_" + userid;
				
				String fdActorId = MD5Util.getMD5String(corp.getId().concat(ACTORIMPORTKEY));
				nowActorIds.add(fdActorId);
				
				DingOrgActor orgActor = getActor(actors, fdActorId);
				if(orgActor == null){
					DingOrgDept orgPostDept = null;
					if (deptMap == null) {
						String DEPTIMPORTKEY = "ding_dept_" + String.valueOf(dept[j]);
						String deptFdId = MD5Util.getMD5String(corp.getId().concat(DEPTIMPORTKEY));
						orgPostDept  = (DingOrgDept) dingOrgElementRepository.loadDept(corp.getId(), deptFdId);
						if (orgPostDept == null) {
							logger.error("部门为空:{},{}" ,corp.getId(), DEPTIMPORTKEY);
						}
					}else{
						orgPostDept = deptMap.get(String.valueOf(dept[j]));
						if (orgPostDept == null) {
							logger.error("部门为空:{},{}" ,corp.getId(), dept[j]);
						}
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
					
					String hierarchyId = getHierarchyId(orgActor, corp.getId(), deptMap , dingOrgElementRepository);
					orgActor.setFdHierarchyId(hierarchyId);
					if(logger.isTraceEnabled()){
						logger.trace("--upsert DingOrgActor-start-:{}", ACTORIMPORTKEY);
					}
					//if(orgUser!=null){
					dingOrgActors.add(orgActor);
					//}
//					try {
//						dingOrgElementRepository.upsert(orgActor);
//					} catch (Exception e) {
//						logger.error("",e);
//					}
					if(logger.isTraceEnabled()){
						logger.trace("--insert DingOrgActor-end-:{}", ACTORIMPORTKEY);
					}
				}else{
					String hierarchyId = getHierarchyId(orgActor, corp.getId(), deptMap , dingOrgElementRepository);
					DingOrgDept orgPostDept = null;
					if (deptMap == null) {
						String DEPTIMPORTKEY = "ding_dept_" + String.valueOf(dept[j]);
						String deptFdId = MD5Util.getMD5String(corp.getId().concat(DEPTIMPORTKEY));
						orgPostDept  = (DingOrgDept) dingOrgElementRepository.loadDept(corp.getId(), deptFdId);
						if (orgPostDept == null) {
							logger.error("部门为空:{},{}" ,corp.getId(), DEPTIMPORTKEY);
						}
					}else{
						orgPostDept = deptMap.get(String.valueOf(dept[j]));
						if (orgPostDept == null) {
							logger.error("部门为空:{},{}" ,corp.getId(), dept[j]);
						}
					}
					if (orgPostDept != null) {
						orgActor.setFdParentid(orgPostDept.getId());
					}
					orgActor.setFdOrgid(corp.getId());
					orgActor.setFdHierarchyId(hierarchyId);
					orgActor.setFdName(name);
					orgActor.setFdIsAvailable(active);
					orgActor.setFdIsSync(true);
					orgActor.setFdAlterTime(corp.getFdLastSyncTime());
					orgActor.setFdNo(jobnumber);
					//if(orgActor.getFdUser()!=null){
					dingOrgActors.add(orgActor);
					//}
					//dingOrgElementRepository.upsert(orgActor);
				}
				if(StringUtils.isBlank(orgActor.getFdHierarchyId())){
					logger.warn("层级id为空:{} , {}" , corp.getId(), orgActor.getId());
				}
			}
			
			//待改
			for (DingOrgActor dingOrgActor : actors) { //删除无用的actor
				if(!nowActorIds.contains(dingOrgActor.getId())){
					dingOrgElementRepository.deleteElement(dingOrgActor);
					if(logger.isTraceEnabled()){
						logger.trace("--delete ActorsForUser DingOrgUser-end-:{}", dingOrgActor.getId());
					}
				}
			}
			return dingOrgActors;
			//return fdId;
		}catch(Exception e){
			logger.error("单个用户异常："+userDetailInfoJson, e);
			e.printStackTrace();
		}
		return dingOrgActors;
	}
	
	private static DingOrgActor getActor(List<DingOrgActor> actors  , String actorId){
		for (DingOrgActor dingOrgActor : actors) {
			if(dingOrgActor.getId().equals(actorId))
				return dingOrgActor;
		}
		return null;
	}
	
	private static final String DINGDEPTLIST = "getDingDeptList";
	
	private JSONObject getDingDeptList(DingSuiteThirdForSync third) {
		JSONObject json =  dingApiService.getDingDeptList(third);
		return json;
	}
	
	private static final String GETDETAILLISTDEPTUSERS = "getDetailListDeptUsers";
//	private JSONObject getDetailListDeptUsers(DingSuiteThirdForSync third ,String deptId , int fetchAll){
//		JSONObject json=null;
//		String key = GETDETAILLISTDEPTUSERS + third.getCorpId() + deptId + fetchAll;
//		json = (JSONObject) cacheableService.getRawObjectFromCache(key);
//
//		if (json == null) {
//			long startTime = System.currentTimeMillis();
//			//json = dingApiService.getDetailListDeptUsers(third, deptId, fetchAll);
//			if(json!=null){
//				if(logger.isTraceEnabled()) logger.trace("获取人员详细信息耗时:("+third.getCorpName()+") --" + ((System.currentTimeMillis() - startTime)/1000) + "秒");
//				cacheableService.setRawObjectInCache(key, json, 1, TimeUnit.DAYS); //1天
//			}
//		}
//		return json;
//	}
	
	private JSONObject getDetailListDeptUsers(DingSuiteThirdForSync third ,String deptId){
		JSONObject json= dingApiService.getDetailListDeptUsers(third, deptId);
		return json;
	}
	
	static class DeptVo {
		private Integer id;
		private String name;
		private Integer parentid;
	}
	
	static class DeptVoIdComparator implements Comparator<DeptVo> {
		@Override
		public int compare(DeptVo o1, DeptVo o2) {
			return o1.id.compareTo(o2.id);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (isRunningInShadedJar()) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					Ding2OrgSyncJob.this.close();
				}
			});
			workThread.start();
		}
	}
	
	private boolean isRunningInShadedJar() {
		try {
			Class.forName(CLASS_ONLY_AVAILABLE_IN_IDE);
			return false;
		} catch (ClassNotFoundException anExc) {
			return true;
		}
	}
	
	private static final String CLASS_ONLY_AVAILABLE_IN_IDE = "com.landray.IDE";
}



/**
 * 获取部门失败:{"errmsg":"企业不存在或者已经被解散","errcode":70001}
[ERROR] [19:44:53] com.landray.lanyun.org.service.impl.Ding2OrgSyncJob - ---------获取人员详细信息失败:{"errmsg":"部门不存在","errcode":60003}
 */
