package me.ywork.ticket.suite.service;

import com.alibaba.fastjson.JSONObject;

import me.ywork.ticket.suite.entity.DingSuiteMain;
import me.ywork.ticket.suite.entity.DingSuiteThirdMain;
import me.ywork.ticket.suite.model.GetUnactiveCorpReqModel;
import me.ywork.ticket.suite.model.GetUnactiveCorpResModel;
import me.ywork.ticket.suite.model.QySuiteAuthApp;
import me.ywork.ticket.suite.model.QySuitePermanentCode;
import me.ywork.ticket.suite.model.ReAuthCorpReqModel;

public interface IDingApiService {

	public JSONObject getDingData(String url);

	public JSONObject postDingData(String url, String jsonStr);

	// 获取永久授权码
	public QySuitePermanentCode getPermanentCode(DingSuiteMain ws, String authCode, String suiteTicket)
			throws Exception;
//
	// 获取企业号的授权信息
	public QySuitePermanentCode getAuthInfo(DingSuiteMain ws, String authCorpid, String permanentCode,
			String suiteTicket) throws Exception;

	// 获取企业号应用
	public QySuiteAuthApp getAgent(DingSuiteMain ws, String authCorpid, String permanentCode, String agentid,
			String suiteTicket) throws Exception;

	// 获取企业号access_token
	public JSONObject getCorpToken(String authCorpid, String permanentCode, String suiteToken);

	// 激活授权套件
	public int activateSuite(String authCorpid, String suiteId, String permanentCode, String suiteToken)
			throws Exception;

	// 注册回调
	public int registerCallBack(JSONObject registerDataJson, DingSuiteThirdMain vo);

	// 注册回调
	public int updateCallBack(JSONObject registerDataJson, DingSuiteThirdMain vo);

	// 获取应用未激活的企业列表
	public GetUnactiveCorpResModel getUnactiveCorp(String suiteId, GetUnactiveCorpReqModel getUnactiveCorpReqModel);

	// 重新授权未激活应用的企业
	public int ReAuthCorp(String suiteId, ReAuthCorpReqModel reAuthCorpReqModel);
}
