package me.ywork.ticket.suite.service;

import me.ywork.ticket.suite.entity.DingSuiteMain;
import me.ywork.ticket.suite.entity.DingSuiteThirdMain;

public interface IAccessTokenService {

//	/**
//	 * 获取jsapi_ticket
//	 * @param third
//	 * @return
//	 */
//	String getJSAPITicket(DingSuiteThirdMain third);
//
//	/**
//	 * 获取管理组JS-SDK凭据
//	 * @param third
//	 * @return
//     */
//	JSONObject getJSAPITicketForContact(DingSuiteThirdMain third);
//
//	String getJSAPISignature(String ticket, String nonceStr, long timeStamp, String url,String agentid);

//	/**
//	 * 获取管理组JS-SDK凭据签名
//	 * @param ticket
//	 * @param nonceStr
//	 * @param timeStamp
//	 * @param url
//     * @return
//     */
//	String getJSSdkAuthoritySignature(String ticket, String nonceStr, long timeStamp, String url);

	/**
	 * 获取token的方法--应用授权与非授权方式
	 * @param corp
	 * @param tm
	 * @return
	 */
	String getAccessToken(DingSuiteThirdMain third);
	
	/**
	 * 获取套件token
	 * @Description
	 * @param suite
	 * @param suiteTicket
	 * @return
	 * @return String
	 */
	String getSuiteToken(DingSuiteMain suite,String suiteTicket);
	
	/**
	 * 删应用token
	 * @param suiteId
	 * @param suiteSecret
	 * @param suiteTicket
	 */
	void delSuiteAccessToken(String suiteId,String corpId);
	
	/**
	 * 获取 pc端 access_token
	 * @Description
	 * @param corpid
	 * @param corpsecret
	 * @return
	 * @return WeChatToken
	 */
	//String getPCAccessToken(String corpid, String corpsecret);
}
