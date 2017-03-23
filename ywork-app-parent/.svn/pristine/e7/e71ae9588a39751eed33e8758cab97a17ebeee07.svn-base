package me.ywork.org.realtime.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.messagebus.message.DefaultMessage;
import com.heyi.framework.messagebus.message.MessageHandler;

import me.ywork.org.api.message.DingAdminLogonMessage;
import me.ywork.org.realtime.entity.DingSuiteThirdMain;
import me.ywork.org.realtime.repository.DingOrgCorpRepository;
import me.ywork.org.realtime.repository.DingOrgElementRepository;
import me.ywork.org.realtime.repository.DingSuiteThirdMainRepositroy;
import me.ywork.org.realtime.service.IDingApiService;
import me.ywork.org.realtime.service.IDingOrgElementService;
import me.ywork.org.realtime.service.IDingOrgUserService;
import me.ywork.ticket.message.DingOrgModifyMessage;

@Service
public class DingOrgModifyEventHandler implements MessageHandler<DefaultMessage> {
	
	private static Logger logger = LoggerFactory.getLogger(DingOrgModifyEventHandler.class);

	@Autowired
	private IDingOrgUserService dingOrgUserService;
	
	@Autowired
	private IDingOrgElementService dingOrgElementService;
	
	@Autowired
	private DingSuiteThirdMainRepositroy dingSuiteThirdMainRepositroy;
	
	
	@Autowired
	private IDingApiService dingApiService;
	
	@Autowired
	private DingOrgCorpRepository dingOrgCorpRepository;
	
	/**
	 * 
	 * 
	    sync_org:非钉钉，自定义类型 ，全量同步
		user_add_org : 通讯录用户增加
		user_modify_org : 通讯录用户更改
		user_leave_org : 通讯录用户离职
		org_admin_add ：通讯录用户被设为管理员
		org_admin_remove ：通讯录用户被取消设置管理员
		org_dept_create ： 通讯录企业部门创建
		org_dept_modify ： 通讯录企业部门修改
		org_dept_remove ： 通讯录企业部门删除
		org_remove ： 企业被解散
	 */
	@Override
	public void onMessage(DefaultMessage object) {
		if (object instanceof DingOrgModifyMessage) {
			DingOrgModifyMessage dingOrgModifyMessage = (DingOrgModifyMessage) object;
			
			if(dingOrgModifyMessage.getEventType().equalsIgnoreCase("sync_org")){
				//自定义全量同步标志
				logger.info("实时全量："+dingOrgModifyMessage.getCorpId());
				dingOrgElementService.syncElement(dingOrgModifyMessage.getSuiteId(), dingOrgModifyMessage.getCorpId());
				return;
			}
			else if(dingOrgModifyMessage.getEventType().equalsIgnoreCase("user_leave_org")){
				//logger.info("删除用户："+JSONObject.toJSONString(dingOrgModifyMessage));
				dingOrgUserService.removeUser(dingOrgModifyMessage.getSuiteId(), dingOrgModifyMessage.getCorpId(), dingOrgModifyMessage.getUserId());
				return;
			}else if(dingOrgModifyMessage.getEventType().equalsIgnoreCase("org_dept_remove")){
				
				return;
			}else if(dingOrgModifyMessage.getEventType().equalsIgnoreCase("org_remove")){
				logger.info("删除企业："+dingOrgModifyMessage.getCorpId());
				dingOrgCorpRepository.updateAbandoned(dingOrgModifyMessage.getCorpId());
				return;
			}
			
			DingSuiteThirdMain suitethird = dingSuiteThirdMainRepositroy.findDingSuiteThirdByCorpId(dingOrgModifyMessage.getCorpId(), dingOrgModifyMessage.getSuiteId());
			
			if(suitethird != null ){
				if(dingOrgModifyMessage.getUserId()!=null && dingOrgModifyMessage.getUserId().length>0){
					//logger.info("更新用户："+dingOrgModifyMessage.getCorpId()+",size:"+dingOrgModifyMessage.getUserId().length);
					JSONObject obj = null;
					String userid = "";
					for (String userId : dingOrgModifyMessage.getUserId()) {
						try {
							obj = dingApiService.getDingUser(suitethird, userId);
							userid = obj.getString("userid");
							if (StringUtils.isEmpty(userid)) {
								userid = obj.getString("userId");
							}
							if(StringUtils.isNotEmpty(userid)){
								dingOrgUserService.updateUser(dingOrgModifyMessage.getSuiteId() , dingOrgModifyMessage.getCorpId() , obj, new Date(), false);
							}
						} catch (Exception e) {
							logger.error("发生人员错误:"+dingOrgModifyMessage.getCorpId() + "," + userId,e);
						}
					}
				}
				
//				for(String deptId : dingOrgModifyMessage.getDeptId()){
//					try {
//						JSONObject obj = dingApiService.getDingDeptDetail(suitethird, deptId);
//						String userid = obj.getString("userid");
//						if (StringUtils.isEmpty(userid)) {
//							userid = obj.getString("userId");
//						}
//						if(StringUtils.isNotEmpty(userid)){
//							dingOrgDeptService.updateDept(dingOrgModifyMessage.getSuiteId() , dingOrgModifyMessage.getCorpId() , obj);
//						}
//					} catch (Exception e) {
//						logger.error("推送部门发生错误:"+dingOrgModifyMessage.getCorpId() + "," + deptId,e);
//					}
//				}
				
			}else{
				logger.error("该公司第三方授公信息未录入,coprid:"+dingOrgModifyMessage.getCorpId() + ",suiteId : " + dingOrgModifyMessage.getSuiteId());
			}
		}
		
		else if(object instanceof DingAdminLogonMessage){
			DingAdminLogonMessage dingAdminLogon = (DingAdminLogonMessage) object;
			logger.info("DingAdminLogonMessage："+dingAdminLogon.getCorpId()+",user:"+dingAdminLogon.getUserId()+",isadmin:"+dingAdminLogon.isAdmin());
			dingOrgElementRepository.updateAdmin(dingAdminLogon.getCorpId(),  dingAdminLogon.getUserId(),dingAdminLogon.isAdmin());
		}
	}
	
	@Autowired
	private DingOrgElementRepository dingOrgElementRepository;

	@Override
	public boolean supports(Class<?> classOfMsg) {
		if(DingOrgModifyMessage.class.isAssignableFrom(classOfMsg) || DingAdminLogonMessage.class.isAssignableFrom(classOfMsg)){
			return true;
		}
		return false;
	}
}
