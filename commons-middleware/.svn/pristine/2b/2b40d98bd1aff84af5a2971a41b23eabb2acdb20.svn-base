package com.heyi.framework.messagebus.message.responsible;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import org.kafka.KafkaMessageKey;
import org.kafka.KafkaProducerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.heyi.framework.messagebus.event.QueueEventHandler;
import com.heyi.framework.messagebus.message.responsible.async.AsyncOperation;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;


/**
 * 
 * @author sulta
 *
 */
public class DefaultResponsibleService {

	private static final Logger logger = LoggerFactory.getLogger(DefaultResponsibleService.class);

	private final Producer<KafkaMessageKey, Object> producer;
	
	/**服务编码*/
	private String serviceCode;
	
	/**发送时使用的频道名*/
	private String sendTopic;
	
	/**接收回应使用的频道名*/
	private String responseTopic;
	
	/**接收回应使用的分区*/
	private Integer responsePartiton; //TODO 解决分布式问题
	
	public DefaultResponsibleService() {
		this.producer = KafkaProducerManager.getProducer();
	}
	
	/**定时器*/
	private final static HashedWheelTimer timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 100, TimeUnit.MILLISECONDS);
	
	/***产生一个不会重复的id*/
	private final static AtomicLong messageIdAL = new AtomicLong(System.nanoTime());
		
	/**
	 * 以异步方式获取对方处理结果
	 * @param message
	 * @return
	 */
	public <V> AsyncOperation<V> getAsync(Object obj , long maxWait , TimeUnit timeunit) {
		Assert.notNull(obj);
		Assert.notNull(responseTopic);
		Assert.notNull(responsePartiton);
		Assert.hasLength(sendTopic);
		
		DefaultTopicMessage message = new DefaultTopicMessage(obj);
		
		final AsyncOperation<V> asyncOperation = new AsyncOperation<V>(GlobalEventExecutor.INSTANCE);			
		
		final ResponsibleMessageKey key = new ResponsibleMessageKey();
		key.setId(messageIdAL.getAndIncrement());
		
		key.setCreateTimeStamp(System.currentTimeMillis());
		key.setTopic(sendTopic);
		key.setResponseTopic(responseTopic);
		key.setResponsePartiton(responsePartiton);
		
		producer.send(new KeyedMessage<KafkaMessageKey, Object>( sendTopic, key, message));
		
		final AsyncOperation<DefaultResponseMessage> promise = new AsyncOperation<DefaultResponseMessage>(GlobalEventExecutor.INSTANCE);		
		
		QueueEventHandler.ASYNCOPERATIONCACHE.put(key.getId(), promise);
		
		if(logger.isDebugEnabled()){
			logger.debug("@@getAsync -- new Promise by {}, id:" + key.getId(), sendTopic);
		}
		
		TimerTask timerTask = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                try {
					if (promise.isDone()) {
					    return;
					}
					promise.setFailure(new TimeoutException());
					if(logger.isDebugEnabled()){
						logger.debug("@@getAsync -- promise timeout, id:" + key.getId());
					}
				} finally{
					QueueEventHandler.ASYNCOPERATIONCACHE.invalidate(key.getId());
				}
            }
        };
        
        timer.newTimeout(timerTask, maxWait , timeunit);
        
		promise.addListener(new GenericFutureListener<Future<DefaultResponseMessage>>() {

			@SuppressWarnings("unchecked")
			@Override
			public void operationComplete(
					Future<DefaultResponseMessage> future) throws Exception {
				try {
					if (!future.isSuccess()) {
						asyncOperation.setFailure(future.cause());
					}else{
						DefaultResponseMessage responseMessage = future.getNow();
						asyncOperation.setSuccess((V) responseMessage.getMessage());
					}
				} finally {
					QueueEventHandler.ASYNCOPERATIONCACHE.invalidate(key.getId());
				}
			}
		});
		return asyncOperation;
	}
	
	public String getSendTopic() {
		return sendTopic;
	}

	public void setSendTopic(String sendTopic) {
		this.sendTopic = sendTopic;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getResponseTopic() {
		return responseTopic;
	}

	public void setResponseTopic(String responseTopic) {
		this.responseTopic = responseTopic;
	}

	public Integer getResponsePartiton() {
		return responsePartiton;
	}

	public void setResponsePartiton(Integer responsePartiton) {
		this.responsePartiton = responsePartiton;
	}
	
}
