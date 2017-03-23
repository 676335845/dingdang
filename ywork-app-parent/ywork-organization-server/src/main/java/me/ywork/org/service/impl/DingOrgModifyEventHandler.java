package me.ywork.org.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heyi.framework.messagebus.message.DefaultMessage;
import com.heyi.framework.messagebus.message.MessageHandler;

import me.ywork.org.service.IDingApiService;
import me.ywork.org.service.IDingOrgUserService;

@Service
public class DingOrgModifyEventHandler implements MessageHandler<DefaultMessage> {
	
	private static Logger logger = LoggerFactory.getLogger(DingOrgModifyEventHandler.class);

	@Autowired
	private IDingOrgUserService dingOrgUserService;
	
//	@Autowired
//	private DingSuiteThirdMapper dingSuiteThirdMapper;
//
	
	@Autowired
	private IDingApiService dingApiService;
	 
	
	/**
	 * 
	 * 
		user_add_org : 通讯录用户增加
		user_modify_org : 通讯录用户更改
		user_leave_org : 通讯录用户离职
		org_admin_add ：通讯录用户被设为管理员
		org_admin_remove ：通讯录用户被取消设置管理员
		org_dept_create ： 通讯录企业部门创建
		org_dept_modify ： 通讯录企业部门修改
		org_dept_remove ： 通讯录企业部门删除
	 */
	@Override
	public void onMessage(DefaultMessage object) {
//		if (object instanceof DingOrgModifyMessage) {
//			DingOrgModifyMessage dingOrgModifyMessage = (DingOrgModifyMessage) object;
//
//			if(dingOrgModifyMessage.getEventType().equalsIgnoreCase("user_leave_org")){
//				return;
//			}
//
//			DingSuiteThirdForSync third = new DingSuiteThirdForSync();
//			third.setFdCorpId(dingOrgModifyMessage.getCorpId());
//			third.setFdCorpName("");
//			third.setFdSuiteId(dingOrgModifyMessage.getSuiteId());
//			third.setFdPermanentCode("");
//			//DingSuiteThird suitethird = dingSuiteThirdMapper.findDingSuiteThirdByCorpId(dingOrgModifyMessage.getCorpId(), dingOrgModifyMessage.getSuiteId());
//
//			for (String userId : dingOrgModifyMessage.getUserId()) {
//				try {
//					JSONObject obj = dingApiService.getDingUser(third, userId);
//					if(logger.isDebugEnabled()){
//						logger.debug("人员修改事件:{}" , obj.toJSONString());
//					}
//					String userid = obj.getString("userid");
//					if (StringUtils.isEmpty(userid)) {
//						userid = obj.getString("userId");
//					}
//					if(StringUtils.isNotEmpty(userid)){
//						dingOrgUserService.updateUser(dingOrgModifyMessage.getSuiteId() , dingOrgModifyMessage.getCorpId() , obj, new Date(), false);
//					}
//				} catch (Exception e) {
//					logger.error("发生错误:"+dingOrgModifyMessage.getCorpId() + "," + userId,e);
//				}
//			}
//
//		}
	}

	@Override
	public boolean supports(Class<?> classOfMsg) {
//		if(DingOrgModifyMessage.class.isAssignableFrom(classOfMsg)){
//			return true;
//		}
		return false;
	}
}
