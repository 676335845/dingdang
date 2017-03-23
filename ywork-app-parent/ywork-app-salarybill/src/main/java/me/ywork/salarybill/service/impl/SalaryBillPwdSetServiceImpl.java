package me.ywork.salarybill.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.heyi.framework.spring.context.AppContext;
import com.heyi.utils.DateUtils;
import com.heyi.utils.IdGenerator;

import me.ywork.context.CallContext;
import me.ywork.message.DingOAMessage;
import me.ywork.message.DingOAMessage.DingOABody;
import me.ywork.message.base.DingCorpMessageHeader;
import me.ywork.message.base.DingCorpMessageResult;
import me.ywork.message.base.DingMessageType;
import me.ywork.salarybill.ListUtils;
import me.ywork.salarybill.SalaryBillConfigure;
import me.ywork.salarybill.base.SalaryBillConstant;
import me.ywork.salarybill.base.SalaryPwdType;
import me.ywork.salarybill.entity.SalaryBillNoPwdSelectedItem;
import me.ywork.salarybill.entity.SalaryBillPwdSet;
import me.ywork.salarybill.model.OrgDataModel;
import me.ywork.salarybill.model.OrgDeptModel;
import me.ywork.salarybill.model.OrgItemResult;
import me.ywork.salarybill.model.PwdCheckResult;
import me.ywork.salarybill.model.SalaryBillNoPwdSelectedItemViewMode;
import me.ywork.salarybill.model.SalaryBillPwdSetModel;
import me.ywork.salarybill.model.SalaryBillPwdTempModel;
import me.ywork.salarybill.model.UserModel;
import me.ywork.salarybill.repository.SalaryBillPwdSetRepository;
import me.ywork.salarybill.repository.SalaryBillRepository;
import me.ywork.salarybill.service.SalaryBillException;
import me.ywork.salarybill.service.SalaryBillPwdSetService;
import me.ywork.salarybill.service.impl.util.SalaryServiceImplUtils;
import me.ywork.suite.api.rpc.IDingAPIRpcService;
import me.ywork.util.AESUtil;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalaryBillPwdSetServiceImpl implements SalaryBillPwdSetService {

	private static Logger logger = LoggerFactory.getLogger(SalaryBillPwdSetServiceImpl.class);
	
	@Autowired
	private SalaryBillPwdSetRepository salaryBillPwdSetRepository;
	
	@Autowired
	protected SalaryBillRepository salaryBillRepository;
	
	// 钉钉接口
	private IDingAPIRpcService dingAPIRpcService;
	private IDingAPIRpcService getDingAPIRpcService() {
		if (dingAPIRpcService == null) {
			dingAPIRpcService = (IDingAPIRpcService) AppContext.getBean("dingAPIRpcService");
		}
		return dingAPIRpcService;
	}
	
	@Override
	public boolean deleteById(CallContext callContext, String id)  {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public Boolean evensetpwd(String companyId,String personId, Short pwdType) throws SalaryBillException {
		
		SalaryBillPwdSet salaryBillPwdSet =  salaryBillPwdSetRepository.existsUserPwd(companyId,personId,pwdType);
		//PC端管理员类型
		if(pwdType == SalaryPwdType.Manager.getCode()){
			if(salaryBillPwdSet!=null && !StringUtils.isBlank(salaryBillPwdSet.getPassword())){
				return true;
			}else{
				return false;
			}
		}else{
			//移动端普通用户
			boolean flag=true;
			if(salaryBillPwdSet!=null){
				boolean isNeedPasswd =salaryBillPwdSet.getIsNeedPasswd();
				//说明自己已经修改成需要密码
				if(isNeedPasswd == true){
					return false;
				}else if(isNeedPasswd == false){
					//无需密码
					return true;
				}else{
					//没有设置，则检查是否被设置在无需密码的人员或部门中
					flag=false;
				}
			}else{
				flag=false;
			}
			
			boolean isNeedPwd=false;
			
			if(!flag){
				//查是否管理员设置过此人
				List<String> list = salaryBillPwdSetRepository.needPwd(companyId,personId);//如果这里存在表示不需要输入密码
				if(list!=null && list.size()>0){
					isNeedPwd = true;
				}
			}
			return isNeedPwd;
		}
	}
	


	@Override
	public List<UserModel> findSalaryPwdModelBySerach(String companyId, String searchKey) {
		// TODO Auto-generated method stub
		
		//获取员工信息
		List<UserModel> userList = salaryBillPwdSetRepository.findSalaryBillPwdBySerachKey(companyId, searchKey);
		//list 排序
		String [] sortNameArr = {"parentId","userId"};
		boolean [] isAscArr = {true,true};
		ListUtils.sort(userList,sortNameArr,isAscArr);
		
		//获取公司所有部门
		List<OrgDeptModel> deptList = salaryBillRepository.getDeptInfoByCompany(companyId);
		//转map
		Map<String,String> deptMap = new HashMap<String,String>();
		for(OrgDeptModel dept : deptList){
			deptMap.put(dept.getId(), dept.getDeptName());
		}
		
		Map<String,UserModel> f = new LinkedHashMap<String,UserModel>();
		//处理部门
		for(UserModel user : userList){
			String hierarchy = user.getHierarchy();
			String[] hierarchys = hierarchy.split("x");
			String deptName = "";
			for(String h : hierarchys){
				if(deptMap.get(h)!=null){
					deptName += deptMap.get(h)+"/";
				}
			}
			if(deptName.length()>1){
				deptName = deptName.substring(0, deptName.length()-1);
			}
			if(f.get(user.getUserId())!=null){
				//标明多部门
				user = f.get(user.getUserId());
				user.setDeptName(user.getDeptName()+"-"+deptName);
			}else{
				user.setDeptName(deptName);
			}
			f.put(user.getUserId(), user);
		}
		//map转list
		List<UserModel> ulist = new ArrayList<UserModel>();
		for(String key : f.keySet()){
			ulist.add(f.get(key));
		}
		return ulist;
	}


	@Override
	public Boolean setUserPwd(String companyId,String personId, Short type ,SalaryBillPwdSetModel salaryBillPwdSetModel)
			throws SalaryBillException {
		
		SalaryBillPwdSet salaryBillPwdSet = salaryBillPwdSetRepository.findSalaryBillPwdSetByUser(companyId,personId,type);
		
		boolean flag = false;
		
		if(salaryBillPwdSet==null){
			salaryBillPwdSetModel.setCompanyId(companyId);
			salaryBillPwdSetModel.setCreateUserId(personId);
			salaryBillPwdSetModel.setUpdateUserId(personId);
			salaryBillPwdSetModel.setUserId(personId);
			salaryBillPwdSetModel.setNeedReset(false);
			salaryBillPwdSetModel.setPasswordType(type);
			salaryBillPwdSetModel.setId(IdGenerator.newId());
			salaryBillPwdSet = SalaryServiceImplUtils.convertToSalaryBillPwdSet(salaryBillPwdSetModel);
			if("-1".equals(salaryBillPwdSet.getPassword())){
				return false;
			}
			salaryBillPwdSet.setIsNeedPasswd(true);
			int count = salaryBillPwdSetRepository.insert(salaryBillPwdSet);
			if(count>0){
				flag = true;
			}
		}else{
			try {
				String pass = AESUtil.encrypt(salaryBillPwdSetModel.getPassword(),salaryBillPwdSet.getId());
				flag = salaryBillPwdSetRepository.updateUserPwd(companyId,salaryBillPwdSet.getId(),personId,pass,type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!flag) {
			logger.error("设置密码失败!callContext:{}, commitResult:{}",
					companyId+":"+personId, salaryBillPwdSetModel);
			throw new SalaryBillException("设置密码失败!");
		}
		
		return flag;
	}

	
	
	
	@Override
	public String resetUserPwd(String companyId, String personId, Short type,
			SalaryBillPwdSetModel salaryBillPwdSetModel) throws SalaryBillException {
		// TODO Auto-generated method stub
		SalaryBillPwdSet salaryBillPwdSet = salaryBillPwdSetRepository.findSalaryBillPwdSetByUser(companyId,personId,type);
		
		if(salaryBillPwdSet != null ){
			//String pass = salaryBillPwdSet.getPassword();
			try {
				//String decryptPass = AESUtil.decrypt(pass, salaryBillPwdSet.getId());
				//if(decryptPass.equals(salaryBillPwdSetModel.getOldpass())){
					//设置密码
					try {
						String newpass = AESUtil.encrypt(salaryBillPwdSetModel.getPassword(),salaryBillPwdSet.getId());
						Boolean f = salaryBillPwdSetRepository.updateUserPwd(companyId, salaryBillPwdSet.getId(), personId, newpass,type);
						if(f==null || !f){
							return "密码更新异常";
						}
						return "";
					} catch (Exception e) {
						e.printStackTrace();
					}
				//}
//				else{
//					//旧密码不对
//					return "旧密码输入有误";
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	
	@Override
	public Boolean clearUserPwd(String domainName, String companyId, String personId, Short type) throws SalaryBillException {
		
		SalaryBillPwdSet salaryBillPwdSet =  salaryBillPwdSetRepository.existsUserPwd(companyId,personId,SalaryPwdType.User.getCode());
		
		if(salaryBillPwdSet == null){
			return true;
		}else{
			//清空密码
			Boolean flag = salaryBillPwdSetRepository.updateUserPwd(companyId, salaryBillPwdSet.getId(), personId, "", SalaryPwdType.User.getCode());
			if(flag!= null && flag){
				//发消息
				String text = "您的悦通知应用密码已被管理员重置，请尽快点击查看详细进入，设置您的新密码。";
				int code = sendDingOAMessage(domainName, companyId,salaryBillPwdSet.getUserId(),text);
				if(code == 0){
					return true;
				}else{
					logger.error("salary clearUserPwd error:"+code);
				}
			}
		}
		
		return false;
	}


	@Override
	public PwdCheckResult comparePwd(String companyId,String personId, SalaryBillPwdSetModel salaryBillPwdSetModel)
			throws SalaryBillException {
		PwdCheckResult checkResult = new PwdCheckResult();
		SalaryBillPwdSet salaryBillPwdSet = salaryBillPwdSetRepository.findSalaryBillPwdSetByUser(companyId,personId,salaryBillPwdSetModel.getPasswordType());
		
		if(salaryBillPwdSet != null ){
			Date releaseTime = salaryBillPwdSet.getReleaseTime();
			
			//是否被禁用的
			long diff = releaseTime == null ? 0 : releaseTime.getTime() - new Date().getTime();
			if(diff>0){
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				checkResult.setIsSuccess(false);
				checkResult.setCheckInfo("由于您输入5次错误密码，禁用至："+df.format(releaseTime));
				return checkResult;
			}
			
			
			String pass = salaryBillPwdSet.getPassword();
			try {
				String decryptPass = AESUtil.decrypt(pass, salaryBillPwdSet.getId());
				if(decryptPass.equals(salaryBillPwdSetModel.getPassword())){
					//清空密码错误清空
					if(salaryBillPwdSet.getErrPwdCount()!= null && salaryBillPwdSet.getErrPwdCount()!=0){
						salaryBillPwdSet.setErrPwdCount(0);
						salaryBillPwdSet.setReleaseTime(null);
						salaryBillPwdSetRepository.update(salaryBillPwdSet);
					}
					checkResult.setIsSuccess(true);
					checkResult.setCheckInfo("check success");
				}else{
					checkResult.setIsSuccess(false);
					
					//密码输入错误 更新错误次数
					salaryBillPwdSet.setErrPwdCount(salaryBillPwdSet.getErrPwdCount()==null?1:salaryBillPwdSet.getErrPwdCount()+1);
					
					if(salaryBillPwdSet.getErrPwdCount()>4){
						salaryBillPwdSet.setErrPwdCount(0);//5次清空 重新计算
						salaryBillPwdSet.setReleaseTime(DateUtils.addMinutes(new Date(),20));
						checkResult.setCheckInfo("您已经输错5次！忘记密码请联系管理员或20分钟后重试。");
					}else if(salaryBillPwdSet.getErrPwdCount()==4){
						checkResult.setCheckInfo("您输错密码4次啦！再错一次会被禁用20分钟哦。");
					}else{
						checkResult.setCheckInfo("您输入的密码错误!");
					}
					salaryBillPwdSetRepository.update(salaryBillPwdSet);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return checkResult;
	}
	
	
	@Override
	public String forgetPwd(String domainName, String companyId, String userId,Short type) {
		
		SalaryBillPwdSet salaryBillPwdSet = salaryBillPwdSetRepository.findSalaryBillPwdSetByUser(companyId,userId,type);
		
		if(salaryBillPwdSet == null){
			return "";
		}
		String createUser = salaryBillPwdSet.getCreateUserId();
		//校验是否已经离职
		String uName = salaryBillPwdSetRepository.isAvailable(companyId, createUser);
		if(StringUtils.isBlank(uName)){
			//离职查询该公司管理员--暂时没有接口
			createUser = "";//salaryBillPwdSetRepository.findManager(companyId);
		}
		
		if(StringUtils.isBlank(uName)){
			return "";
		}
		
		int random = random();
		//存随机码
		salaryBillPwdSet.setDingRandom(random);
		salaryBillPwdSet.setUpdateTime(new Date());
		salaryBillPwdSet.setUpdateUserId(userId);
		salaryBillPwdSetRepository.update(salaryBillPwdSet);
		
		//发随机码 oa消息
		String text =  "私信后台登陆验证码："+random;
		int code = sendDingOAMessage(domainName, companyId,createUser,text);
		if(code != 0 ){
			logger.error("salary send randomcode error:"+code);
			return "";
		}
		return uName;
	}
	
	
	
	@Override
	public Boolean checkRandom(String companyId, String randomCode) {
		SalaryBillPwdSet salaryBillPwdSet = salaryBillPwdSetRepository.findSalaryBillPwdSetByUser(companyId,"",SalaryPwdType.Manager.getCode());
		if(salaryBillPwdSet == null){
			return false;
		}
		int random = salaryBillPwdSet.getDingRandom();
		if(randomCode.equals(random+"")){
			return true;
		}
		return false;
	}


	private int sendDingOAMessage(String domainName, String companyId, String createUser,String text) {
		DingCorpMessageHeader messageHeader = null;
		DingOAMessage dingOAMessage = null;
		DingCorpMessageResult result = null;
		messageHeader = new DingCorpMessageHeader();
		//oa消息类型
		messageHeader.setMsgtype(DingMessageType.oa);
		List<String> userIds = new ArrayList<String>();
		userIds.add(createUser);
		messageHeader.appendReceiveUserIds(userIds);
		dingOAMessage = new DingOAMessage();
		dingOAMessage.setHead("FF3B81F5", "悦通知");
		DingOABody body = new DingOABody();
		body.setContent(text); //"私信后台登陆验证码："+random
		dingOAMessage.setBody(body);
		
		String url = domainName; // SalaryBillConfigure.isTest?ApplicationConfigure.domainNameTest: ApplicationConfigure.domainName;
		url += "/alid/app/salarybill/salarybillbiz/autologin?corpid="+companyId +
				"&appid=" + SalaryBillConfigure.appId +
				"&suiteid=" + SalaryBillConfigure.suiteId +
				"&dd_nav_bgcolor=FF3B81F5&dd_share=false";
		
		dingOAMessage.setMessage_url(url);
		result = getDingAPIRpcService().sendDingCorpMessage(companyId,
				SalaryBillConfigure.suiteId, SalaryBillConfigure.appId, messageHeader, dingOAMessage);
		logger.debug("send vote message，result:{}", JSON.toJSONString(result));
		
		return result.getErrcode();
	}
	
	public static int random() {
		int max=9999;
        int min=1000;
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}

	@Override
	public OrgItemResult saveNoneedPwdItems(List<OrgDataModel> orgDataModels,String companyId,String createUserId) {
		OrgItemResult orgItemResult = new OrgItemResult();
		
		if(orgDataModels!=null){
			
			List<SalaryBillNoPwdSelectedItem> noPwdItemList = new ArrayList<SalaryBillNoPwdSelectedItem>();
			
			List<String> personList = new ArrayList<String>();
			List<String> deptList = new ArrayList<String>();
			
			for(OrgDataModel orgTempModel:orgDataModels){
				
				String id =orgTempModel.getId();
				String type=orgTempModel.getType();
				String name = orgTempModel.getName();
				
				SalaryBillNoPwdSelectedItem salaryBillNoPwdSelectedItem = new SalaryBillNoPwdSelectedItem();
				salaryBillNoPwdSelectedItem.setCompanyId(companyId);
				salaryBillNoPwdSelectedItem.setName(name);
				salaryBillNoPwdSelectedItem.setEid(id);
				salaryBillNoPwdSelectedItem.setType(type);
				salaryBillNoPwdSelectedItem.setCreateDate(new Date());
				salaryBillNoPwdSelectedItem.setCreateUserId(createUserId);

				if(type.equals(String.valueOf(SalaryBillConstant.ORG_TYPE_PERSON))){
					personList.add(id);
				}else if(type.equals(String.valueOf(SalaryBillConstant.ORG_TYPE_DEPT))){
					deptList.add(id);
				}
				salaryBillNoPwdSelectedItem.setId(IdGenerator.newId());
				noPwdItemList.add(salaryBillNoPwdSelectedItem);
			}
			
			salaryBillPwdSetRepository.saveNoneedPwdItems(companyId, noPwdItemList);
			
			if(personList!=null && personList.size()>0){
				salaryBillPwdSetRepository.updateUserPwdOpenStatus(companyId,personList,SalaryPwdType.User.getCode(),false);
			}
			
			if(deptList!=null && deptList.size()>0){
			   List<String> userList= salaryBillPwdSetRepository.findChildPerson(companyId, deptList);
			   if(userList!=null && userList.size()>0){
				   salaryBillPwdSetRepository.updateUserPwdOpenStatus(companyId,userList,SalaryPwdType.User.getCode(),false);
			   }
			  }
			
			orgItemResult.setIsSuccess(true);
			orgItemResult.setResultInfo("success");;
			
		}else{
			orgItemResult.setIsSuccess(false);
			orgItemResult.setResultInfo("fail");;
		}
		return orgItemResult;
	}


	@Override
	public List<SalaryBillNoPwdSelectedItemViewMode> findNoneedPwdItems(String companyId) {
		return salaryBillPwdSetRepository.findNoneedPwdItems(companyId);
	}


	@Override
	public OrgItemResult doReset(String companyId,String id,String type) {
			
		if(StringUtils.isNotBlank(id)){
			if(type.equals(String.valueOf(SalaryBillConstant.ORG_TYPE_PERSON))){
				SalaryBillPwdSet salaryBillPwdSet = salaryBillPwdSetRepository.findSalaryBillPwdSetByUser(companyId,id,SalaryPwdType.User.getCode());
				if(salaryBillPwdSet != null ){
					salaryBillPwdSet.setIsNeedPasswd(true);
					salaryBillPwdSet.setErrPwdCount(0);
					salaryBillPwdSet.setNeedReset(false);
					salaryBillPwdSet.setPassword("");
					salaryBillPwdSet.setReleaseTime(null);
					salaryBillPwdSetRepository.update(salaryBillPwdSet);
				}
			}else if(type.equals(String.valueOf(SalaryBillConstant.ORG_TYPE_DEPT))){
				   List<String> d = new ArrayList<String>();
				   d.add(id);
				   List<String> userList= salaryBillPwdSetRepository.findChildPerson(companyId,d );
				   Boolean isNeedPwd=true;
				   if(userList!=null &&  userList.size()>0)
					   salaryBillPwdSetRepository.updateUserPwdOpenStatus(companyId,userList,SalaryPwdType.User.getCode(),isNeedPwd);
			}
		}
		salaryBillPwdSetRepository.doReset(companyId,id);
		OrgItemResult orgItemResult = new OrgItemResult();
		orgItemResult.setIsSuccess(true);
		orgItemResult.setResultInfo("delete success");
		return orgItemResult;
	}


	@Override
	public PwdCheckResult setPasswdOnOf(String companyId, String personId,
			SalaryBillPwdTempModel salaryBillPwdTempModel) {
		
		PwdCheckResult checkResult = new PwdCheckResult();
		boolean isNeedPasswdFlag =salaryBillPwdTempModel.getIsNeedPasswd();
		SalaryBillPwdSet salaryBillPwdSet = null;
		try{
			salaryBillPwdSet = salaryBillPwdSetRepository.findSalaryBillPwdSetByUser(companyId,personId,salaryBillPwdTempModel.getPasswordType());
		}catch(Exception e){
			logger.error("setPasswdOnOf error,query param companyid:"+companyId+",personid:"+personId+",type:"+salaryBillPwdTempModel.getPasswordType(),e);
			throw e;
		}
		if(salaryBillPwdSet != null ){
			
			// isNeedPasswdFlag=true 需要密码    isNeedPasswdFlag=false 不需要密码
			salaryBillPwdSet.setIsNeedPasswd(isNeedPasswdFlag);
			salaryBillPwdSet.setErrPwdCount(0);
			salaryBillPwdSet.setNeedReset(false);
			salaryBillPwdSet.setPassword("");
//			salaryBillPwdSet.setPasswordType(null);
			salaryBillPwdSet.setReleaseTime(null);
			//salaryBillPwdSet.setDingRandom(dingRandom);
			salaryBillPwdSetRepository.update(salaryBillPwdSet);
		}else {
			 salaryBillPwdSet = new SalaryBillPwdSet();
			 
			 salaryBillPwdSet.setId(IdGenerator.newId());
			 salaryBillPwdSet.setCompanyId(companyId);
			 salaryBillPwdSet.setUserId(personId);
			 salaryBillPwdSet.setPasswordType(SalaryPwdType.User.getCode());
			 salaryBillPwdSet.setPassword("");
			 salaryBillPwdSet.setNeedReset(false);
			 salaryBillPwdSet.setCreateUserId(personId);
			 salaryBillPwdSet.setCreateTime(new Date());
			 salaryBillPwdSet.setUpdateUserId(personId);
			 salaryBillPwdSet.setIsNeedPasswd(isNeedPasswdFlag);
			 salaryBillPwdSet.setUpdateTime(new Date());
			 salaryBillPwdSet.setErrPwdCount(0);
			 salaryBillPwdSetRepository.insert(salaryBillPwdSet);
		}
		
		if(isNeedPasswdFlag){
			salaryBillRepository.deleteSalaryBillNoPwdSelectedItem(companyId,personId);
		}
		
		checkResult.setIsSuccess(true);
		checkResult.setCheckInfo("check success");
		return checkResult;
	}
	
	@Override
	public Boolean  checkHaveSetPasswd(String companyId, String personId,Short pwdType){
		Boolean flag=false;
		SalaryBillPwdSet salaryBillPwdSet =salaryBillPwdSetRepository.existsUserPwd(companyId, personId,pwdType);
		if(salaryBillPwdSet!=null){
			String pwd =salaryBillPwdSet.getPassword();
			if(StringUtils.isNotEmpty(pwd) && !"".equals(pwd)){
				flag= true;
			}
		}
		return flag;
	}
	
	@Override
	public Boolean getPWdStatus(String companyId, String personId,Short pwdType)throws SalaryBillException{
		//pwdOpenStatus = true 表示需要密码状态打开     pwdOpenStatus =false表示需要密码状态关闭
		Boolean pwdOpenStatus=true;
		SalaryBillPwdSet salaryBillPwdSet =salaryBillPwdSetRepository.existsUserPwd(companyId, personId,pwdType);

		//移动端普通用户
		boolean flag=false;
		if(salaryBillPwdSet!=null){
			boolean isNeedPasswd =salaryBillPwdSet.getIsNeedPasswd();
			//说明自己已经修改成需要密码
			if(isNeedPasswd == true){
				pwdOpenStatus=true;
			}else if(isNeedPasswd == false){
				//无需密码
				pwdOpenStatus=false;
			}else{
				//没有设置，则检查是否被设置在无需密码的人员或部门中
				flag=true;
			}
		}else{
			flag=true;
		}
		
		if(flag){
			//查是否管理员设置过此人
			List<String> list = salaryBillPwdSetRepository.needPwd(companyId,personId);//如果这里存在表示不需要输入密码
			if(list!=null && list.size()>0){
				pwdOpenStatus = false;
			}
		}
		
		return pwdOpenStatus;
	}
}
