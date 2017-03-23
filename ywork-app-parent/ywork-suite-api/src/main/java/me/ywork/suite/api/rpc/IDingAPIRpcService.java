package me.ywork.suite.api.rpc;


import java.util.List;

import me.ywork.message.base.AbstractDingMessage;
import me.ywork.message.base.AbstractDingMessageHeader;
import me.ywork.message.base.AbstractDingMessageResult;
import me.ywork.message.base.DingCorpMessageHeader;
import me.ywork.message.base.DingCorpMessageResult;
import me.ywork.suite.api.model.CheckDataRpcModel;
import me.ywork.suite.api.model.CorpAdminRpcModel;

public interface IDingAPIRpcService {
	/**
	 * 通过平台接口发送群组钉消息(异步)
	 * @param corpid   钉钉企业号
	 * @param suiteid  套件ID
	 * @param messageHeader 消息头对象，必须为{@link AbstractDingMessageHeader}的子类
	 * @param message  消息体对象，必须为{@link AbstractDingMessage}的子类
	 * @return
	 */
	public <H extends AbstractDingMessageHeader, M extends AbstractDingMessage>
	      void sendDingGroupMessageAsync(String corpid, String suiteid,String appId,  H messageHeader, M message);
	
	
	/**
	 * 通过平台接口发送企业消息(异步)
	 * @param corpId   钉钉企业号
	 * @param suiteId  套件ID
	 * @param appId    应用ID
	 * @param messageHeader  企业消息头对象，该对象中还缺少agentId,需要通过corpId,suiteId和appId在套件服务中获取并更新
	 * @param mesage         企业消息体对象
	 * @return
	 */
	public <H extends DingCorpMessageHeader, M extends AbstractDingMessage>
			 void sendDingCorpMessageAsync( String corpId, String suiteId, String appId, H messageHeader,
						M mesage);
	
	/**
	 * 通过平台接口发送群组钉消息
	 * @param corpid   钉钉企业号
	 * @param suiteid  套件ID
	 * @param messageHeader 消息头对象，必须为{@link AbstractDingMessageHeader}的子类
	 * @param message  消息体对象，必须为{@link AbstractDingMessage}的子类
	 * @return 消息发送结果对象，必须为{@link AbstractDingMessageResult}的子类
	 */
	public <R extends AbstractDingMessageResult, H extends AbstractDingMessageHeader, M extends AbstractDingMessage>
	      R sendDingGroupMessage(String corpid, String suiteid,String appId,  H messageHeader, M message);
	
	
	/**
	 * 通过平台接口发送企业消息
	 * @param corpId   钉钉企业号
	 * @param suiteId  套件ID
	 * @param appId    应用ID
	 * @param messageHeader  企业消息头对象，该对象中还缺少agentId,需要通过corpId,suiteId和appId在套件服务中获取并更新
	 * @param mesage         企业消息体对象
	 * @return  接口返回的结果对象
	 */
	public <R extends DingCorpMessageResult, H extends DingCorpMessageHeader, M extends AbstractDingMessage>
			 R sendDingCorpMessage( String corpId, String suiteId, String appId, H messageHeader,
						M mesage);
	

	/**
	 * 获取管理员信息
	 * @param code
	 * @return
	 */
	public CorpAdminRpcModel getPcAdminInfo(String code,String suiteId);
	
	
	/**
	 * 获取企业钉钉考勤数据-最长只能时间区间为7天的查询
	 * @param corpid
	 * @param suiteid
	 * @param start
	 * @param end
	 * @return
	 */
	public List<CheckDataRpcModel> getCheckData(String corpid,String suiteid,Long startTime,Long endTime);
	
	/**
	 * 获取企业钉钉考勤数据-最长只能时间区间为7天的查询
	 * @param corpid
	 * @param suiteid
	 * @param start
	 * @param end
	 * @return
	 */
	public List<CheckDataRpcModel> getCheckData(String corpid,String suiteid,String userid,Long startTime,Long endTime);
	
	
}
