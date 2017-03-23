package me.ywork.org.realtime.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.spring.context.AppContext;

import me.ywork.org.realtime.constants.DingApiConstants;
import me.ywork.org.realtime.entity.DingSuiteThirdMain;
import me.ywork.org.realtime.model.DingSuiteThirdForSync;
import me.ywork.org.realtime.service.IDingApiService;
import me.ywork.org.realtime.util.DingAPIHttpUtil;
import me.ywork.ticket.api.rpc.SuiteTokenRpcService;


@Service
public class DingApiServiceImpl  implements IDingApiService {
	private static Logger logger = LoggerFactory.getLogger(DingApiServiceImpl.class);
	
	private SuiteTokenRpcService suiteTokenRpcService;
	
	public SuiteTokenRpcService getSuiteTokenRpcService() {
		if(suiteTokenRpcService==null)
			suiteTokenRpcService = (SuiteTokenRpcService) AppContext.getBean("suiteTokenRpcService");
		return suiteTokenRpcService;
	}

	@Override
	public JSONObject getDingData(String url){
		JSONObject josonInfo = DingAPIHttpUtil.httpRequest(url, "GET", null);
		if(logger.isTraceEnabled()){
			logger.trace("getDingData:{}" , new Object[]{josonInfo.toJSONString()});
		}
		return josonInfo;
	}

	@Override
	public JSONObject postDingData(String url, String jsonStr){
		JSONObject josonInfo = DingAPIHttpUtil.httpRequest(url, "POST", jsonStr);
		if(logger.isTraceEnabled()){
			logger.trace("getDingData:{}" , new Object[]{josonInfo.toJSONString()});
		}
		return josonInfo;
	}

	//获取部门列表
	@Override
	public JSONObject getDingDeptList(DingSuiteThirdMain suitethird) {
		String token = getSuiteTokenRpcService().getToken(suitethird.getCorpId(), suitethird.getSuiteId());
		String url = DingApiConstants.GET_DEPT_LIST.replace("ACCESS_TOKEN", token);
		return getDingData(url);
	}
	
	
	//获取部门详情
	@Override
	public JSONObject getDingDeptDetail(DingSuiteThirdMain suitethird, String deptId) {
		String token = getSuiteTokenRpcService().getToken(suitethird.getCorpId(), suitethird.getSuiteId());
		String url = DingApiConstants.GET_DEPT_DETAIL.replace("ACCESS_TOKEN", token).replace("DEPTID", deptId);
		return getDingData(url);
	}

	// 获取成员
	@Override
	public JSONObject getDingUser(DingSuiteThirdMain suitethird, String userid) throws Exception {
		String token = getSuiteTokenRpcService().getToken(suitethird.getCorpId(), suitethird.getSuiteId());
		String url = DingApiConstants.GET_USER.replace("ACCESS_TOKEN", token).replace("USERID", userid);
		return getDingData(url);
	}

	// 获取部门成员
	public JSONObject getSimpleListDeptUsers(DingSuiteThirdForSync suitethird, String deptId,
			int fetchClild) throws Exception {
		String token = getSuiteTokenRpcService().getToken(suitethird.getCorpId(), suitethird.getSuiteId());
		
		String url = DingApiConstants.GET_DEPT_USER.replace("ACCESS_TOKEN", token)
				.replace("DEPTID", deptId).replace("FETCH", fetchClild + "");
		return getDingData(url);
	}

	// 获取部门成员详情
//	public JSONObject getDetailListDeptUsers(DingSuiteThirdForSync suitethird, String deptId,
//			int fetchClild) {
//		String token = suiteTokenRpcService.getToken(suitethird.getCorpId(), suitethird.getSuiteId());
//
//		String url = DingApiConstants.GET_DEPT_USER_DETAIL
//				.replace("ACCESS_TOKEN", token).replace("DEPTID", deptId)
//				.replace("FETCH", fetchClild + "");
//		return getDingData(url);
//	}
	
	// 获取部门成员详情
	@Override
	public JSONObject getDetailListDeptUsers(DingSuiteThirdMain suitethird, String deptId) {
		String token = getSuiteTokenRpcService().getToken(suitethird.getCorpId(), suitethird.getSuiteId());
		
		String url = DingApiConstants.GET_DEPT_USER_DETAIL_EX.replace("ACCESS_TOKEN", token).replace("DEPTID", deptId);
		
		return getDingData(url);
	}

	@Override
	public JSONObject getCorpToken(String suiteId, String authCorpid, String permanentCode, String suiteToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getCorpAdmins(String suiteId, String corpid) {
		String token = getSuiteTokenRpcService().getToken(corpid, suiteId);
		String httpUrl = DingApiConstants.GET_CORP_ADMIN.replace("ACCESS_TOKEN", token);
		return getDingData(httpUrl);
	}




}
