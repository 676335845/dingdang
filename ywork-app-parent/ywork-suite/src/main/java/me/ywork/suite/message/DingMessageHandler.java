package me.ywork.suite.message;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.heyi.framework.messagebus.message.MessageHandler;
import com.heyi.framework.spring.context.AppContext;

import me.ywork.kfkmessage.DingHttpPostMessage;
import me.ywork.suite.constants.DingAPIConstants;
import me.ywork.suite.util.DingAPIHttpUtil;
import me.ywork.ticket.api.rpc.SuiteTokenRpcService;


/**
 * 钉钉异步发送消息，水位控制
 * @author sulta
 *
 */
@Service
public class DingMessageHandler implements MessageHandler<DingHttpPostMessage> {
	
	private  static Logger logger = LoggerFactory.getLogger(DingMessageHandler.class);
	
	protected volatile boolean isShuttingDown = false;
	
	/**
	 * 水位控制 一分钟最多发10000条 (最终控制结果是每隔?毫秒发一条)
	 */
	private final static int MAX_MESSAGES_CONTROL = 1000  /  ( 10000 / 60);
	
	
	private SuiteTokenRpcService suiteTokenRpcService;
	
	public SuiteTokenRpcService getSuiteTokenRpcService() {
		if(suiteTokenRpcService==null)
			suiteTokenRpcService = (SuiteTokenRpcService) AppContext.getBean("suiteTokenRpcService");
		return suiteTokenRpcService;
	}

	
	/**
	 * TODO 分布式下则将这个保存到redis中去
	 */
	private final static AtomicReference<Long> lastMessageTime = new AtomicReference<Long>(0L);
	
	@Override
	public void onMessage(DingHttpPostMessage message) {
		
		if (message == null) {
			logger.warn("Type of DingHttpPostMessage Message is null.");
			return;
		}
		
		String token = getSuiteTokenRpcService().getToken(message.getCorpId(), message.getSuiteId());
		String url = DingAPIConstants.SEND_MESSAGE;
		url = url.replace("ACCESS_TOKEN", token);
		
		Long lastTime = lastMessageTime.get();
		if(lastTime> 0){
			long distance = System.currentTimeMillis() - lastTime;
			if(distance < MAX_MESSAGES_CONTROL){
				try {
					Thread.sleep(MAX_MESSAGES_CONTROL - distance);
				} catch (InterruptedException e) {
				}
			}
		}
		try {
			String rtnStr = DingAPIHttpUtil.httpPost(url, message.getJsonObject().toJSONString().replace("<br/>", "\r\n"), "sendMsgResult");
			
			logger.info(rtnStr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("调用钉钉远程服务时发生错误",e);
		}
	}

	@Override
	public boolean supports(Class<?> classOfMsg) {
		if(DingHttpPostMessage.class.isAssignableFrom(classOfMsg)){
			return true;
		}
		return false;
	}
	
}
