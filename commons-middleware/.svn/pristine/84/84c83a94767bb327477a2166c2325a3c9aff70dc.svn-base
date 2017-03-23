package com.heyi.framework.messagebus.consumer;


import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.kafka.ExpendableQueue;
import org.kafka.KafkaMessageKey;
import org.kafka.KafkaMessageObject;
import org.kafka.consumer.AbstractConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heyi.framework.messagebus.event.QueueEventHandler;
import com.heyi.framework.messagebus.message.DefaultMessage;

/**
 * 
 * @author sulta
 *
 */
public class DefaultMessageConsumer extends AbstractConsumer<KafkaMessageKey, DefaultMessage> implements ExpendableQueue<KafkaMessageObject<KafkaMessageKey, DefaultMessage>> {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultMessageConsumer.class);	
	
	private boolean warnningOnLowSpeedProcessing = true;

	private final long bootTime = System.currentTimeMillis(); 
	
	/**
	 * 暂存的队列
	 */
	private BlockingQueue<KafkaMessageObject<KafkaMessageKey, DefaultMessage>> fetched;
	
	@Override
	public void onInitialize() throws ExceptionInInitializerError {
		fetched = new ArrayBlockingQueue<KafkaMessageObject<KafkaMessageKey, DefaultMessage>>(1);		
	}
	
	@Override
	public void onNewMessageArrived(
			KafkaMessageObject<KafkaMessageKey, DefaultMessage> message)
			throws InterruptedException {
		
		if (null != message) {
			if(log.isTraceEnabled()){
				KafkaMessageKey key = message.getKey();
				if(key.getCreateTimeStamp()!=null){
					log.trace("@@ 已接收消息:{} , 耗时: {}ms", new Object[]{
							message,
							key.getReceiveTimeStamp() - key.getCreateTimeStamp()
					});
				}else{
					log.trace("@@ 已接收消息:{} ", new Object[]{
							message
					});
				}
			}
			
			try {
				fetched.put(message);
			} catch (InterruptedException ie) {
				log.warn("DefaultMessageConsumer 线程中断,消息已丢失:",new Object[]{
						message
				});
				throw ie;
			}
			if(warnningOnLowSpeedProcessing){
				KafkaMessageKey key = message.getKey();
				if(key!=null
					&& key.getCreateTimeStamp() != null
					&& bootTime < key.getCreateTimeStamp()
					&& key.getReceiveTimeStamp()
								- key.getCreateTimeStamp() > 3000){
					log.warn("处理消息的速度过慢({}),请检查原因!!!",new Object[]{
							message.getKey().getReceiveTimeStamp()
							- message.getKey().getCreateTimeStamp()
					});
					warnningOnLowSpeedProcessing = false;
				}
			}
			QueueEventHandler.getInstance().onMessageArrive(this);
		}
	}
	
	@Override
	public KafkaMessageObject<KafkaMessageKey, DefaultMessage> poll() {
		return fetched.poll();
	}

	@Override
	public int drainTo(
			Collection<? super KafkaMessageObject<KafkaMessageKey, DefaultMessage>> c) {
		return fetched.drainTo(c);
	}	
}
