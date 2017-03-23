package com.heyi.framework.messagebus.event;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.kafka.ExpendableQueue;
import org.kafka.KafkaMessageKey;
import org.kafka.KafkaMessageObject;
import org.kafka.KafkaProducerManager;
import org.kafka.partition.SpecifiedPartitioner;
import org.kafka.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.heyi.framework.messagebus.error.ErrorHandler;
import com.heyi.framework.messagebus.message.DefaultMessage;
import com.heyi.framework.messagebus.message.MessageHandler;
import com.heyi.framework.messagebus.message.MessageHandlers;
import com.heyi.framework.messagebus.message.responsible.DefaultResponseFaultMessage;
import com.heyi.framework.messagebus.message.responsible.DefaultResponseMessage;
import com.heyi.framework.messagebus.message.responsible.DefaultTopicMessage;
import com.heyi.framework.messagebus.message.responsible.ResponsibleMessageHandler;
import com.heyi.framework.messagebus.message.responsible.ResponsibleMessageKey;
import com.heyi.framework.messagebus.message.responsible.async.AsyncOperation;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

/**
 * 消息总调度处理
 * @author sulta
 *
 */
public class QueueEventHandler{
	
	private static final Logger log = LoggerFactory.getLogger(QueueEventHandler.class);

	/**全局Promise缓存*/
	public final static Cache<Long, AsyncOperation<DefaultResponseMessage>> ASYNCOPERATIONCACHE  = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
	
	final Executor executor ;
		
	int threadCount;
	
	private final Producer<KafkaMessageKey, Object>  producer;
	
	private ErrorHandler errorHandler;
	
	private QueueEventHandler() {
		threadCount = Runtime.getRuntime().availableProcessors() * 8;
    	executor = Executors.newFixedThreadPool(threadCount,new NamedThreadFactory("default-message-consumer"));
    	this.producer = KafkaProducerManager.getProducer(SpecifiedPartitioner.class);
    	log.info("@QueueEventHandler: 已创建  FixedThreadPool("+threadCount+") 作为工作线程池" );
	}
	
	public static final QueueEventHandler getInstance() {
		return SingletonHolder.instance;
	}	
    
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	@SuppressWarnings("rawtypes")
	public void onMessageArrive(final ExpendableQueue queue) {
		
		Runnable runnable = new Runnable() {
            @SuppressWarnings("unchecked")
			public void run() {
            	final KafkaMessageObject<KafkaMessageKey, DefaultMessage> kafkaMessage = (KafkaMessageObject<KafkaMessageKey, DefaultMessage>) queue.poll();
                if (null == kafkaMessage) {
                    return;
                }

                DefaultMessage message = kafkaMessage.getMessage();
                if(log.isTraceEnabled()){
        			log.trace("开始处理消息 : {} " ,new Object[]{  message });
        		}
                
                KafkaMessageKey key = kafkaMessage.getKey();
                
                if (key instanceof ResponsibleMessageKey) {
                	ResponsibleMessageKey respKey = (ResponsibleMessageKey) key;
					if (message instanceof DefaultTopicMessage) {
						MessageHandlers<ResponsibleMessageHandler> handlers = MessageHandlers.RESPHANDLERS;
						if (handlers.isEmpty()) {
							log.warn("@@消息处理出错,没有加载任何ResponsibleMessageHandler");
						}
						DefaultTopicMessage topicMessage = (DefaultTopicMessage) message;
						
						Iterator<ResponsibleMessageHandler> iterator = handlers.iterator();
						for (; iterator.hasNext();) {
							ResponsibleMessageHandler handler = iterator.next();
							if (handler.supports(topicMessage.getMessage().getClass())) {
								DefaultResponseMessage responseMessage = null;
								try {
		    						if(log.isTraceEnabled()){
		    		        			log.trace("消息转至 : {} " ,new Object[]{  handler.getClass().getName() });
		    		        		}
		    						Object respond = handler.onResponsibleMessage(topicMessage.getMessage());
									if (respond != null) {
										responseMessage = new DefaultResponseMessage(respond);	
		    						}
		    					} catch (Throwable e) {
		    						log.warn("消息处理时出错", e);
		    						responseMessage = new DefaultResponseFaultMessage();
									((DefaultResponseFaultMessage) responseMessage).setCause(e);
									
		    					}finally{
		    						if(! respKey.getIsFinished()){											
										if(responseMessage == null){											
											responseMessage = new DefaultResponseMessage();
										}
										String responseTopic = respKey.getResponseTopic();											
										producer.send(new KeyedMessage<KafkaMessageKey, Object>( responseTopic, respKey, responseMessage));
									}
		    					}
								break;
							}							
						}
						
					}else if (message instanceof DefaultResponseMessage) {
						DefaultResponseMessage responseMessage = (DefaultResponseMessage) message;

						Long messageId = respKey.getId();
						if (messageId != null) {
							AsyncOperation<DefaultResponseMessage> promise = ASYNCOPERATIONCACHE.getIfPresent(messageId);
							
							if (promise != null) {
								if (!promise.isCancelled()) {
									if (message instanceof DefaultResponseFaultMessage) {
										promise.setFailure(((DefaultResponseFaultMessage) message)
												.getCause());
									} else {
										promise.setSuccess(responseMessage);
									}
								}else{
									if(log.isDebugEnabled()){
										log.debug("@@ Promise 已取消,id: {}" ,messageId );
									}
								}
							}else{
								if(log.isDebugEnabled()){
									log.debug("@@ Promise 已不存在,id: {}" ,messageId );
								}
							}
						}
					}
					return;
				}
                
                MessageHandlers<MessageHandler<DefaultMessage>> handlers = MessageHandlers.DEFAULTHANDLERS;
                
                if(handlers.isEmpty()){
                	log.warn("@@消息调度出错,没有加载任何MessageHandler");
                }
                
                Iterator<MessageHandler<DefaultMessage>> iterator = handlers.iterator();
                for ( ; iterator.hasNext();) {
                	MessageHandler<DefaultMessage> handler = iterator.next();
                	if(handler.supports(message.getClass())){
            			try {
    						if(log.isTraceEnabled()){
    		        			log.trace("消息转至 : {} " ,new Object[]{  handler.getClass().getName() });
    		        		}
    						handler.onMessage(message);
    					} catch (Throwable e) {
    						if(errorHandler != null){
    							try {
									errorHandler.onError(message, e);
								} catch (IOException e1) {
									log.error("", e);
								}
    						}
    						log.warn("消息处理时出错", e);
    					}finally{
    					}
            		}				
				}
            }
        };
        
        executor.execute(runnable);
	}	

	private static class SingletonHolder {
		protected static final QueueEventHandler instance = new QueueEventHandler();
	}
}
