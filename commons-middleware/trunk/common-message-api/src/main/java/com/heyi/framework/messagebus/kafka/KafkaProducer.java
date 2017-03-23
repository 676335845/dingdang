package com.heyi.framework.messagebus.kafka;

import java.util.ArrayList;
import java.util.List;

import org.kafka.KafkaMessageKey;
import org.kafka.KafkaProducerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heyi.framework.messagebus.message.DefaultMessage;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;


/**
 * 消息发送器
 * @author sulta
 *
 */
public class KafkaProducer {
	private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
	
	private final Producer<KafkaMessageKey, Object>  producer;
	private KafkaProducer() {
		this.producer = KafkaProducerManager.getProducer();
	}
	
	public static final KafkaProducer getInstance() {
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder {
		protected static final KafkaProducer instance = new KafkaProducer();
	}
	
	public void send(KeyedMessage<KafkaMessageKey, Object> message) {
		producer.send(message);
	}
	
	public void sendMessage(String topic , DefaultMessage message){
		KafkaMessageKey key = new KafkaMessageKey();
		
		key.setCreateTimeStamp(System.currentTimeMillis());
		key.setTopic(topic);
		
		if(log.isTraceEnabled()){
			log.trace("@KafkaProducer 发布消息到" + topic, message);
		}
		
		producer.send(new KeyedMessage<KafkaMessageKey, Object>( topic, key, message));
	}
	
	public void sendMessage(String topic , List<? extends DefaultMessage> messages){
	
		if(log.isTraceEnabled()){
			log.trace("@KafkaProducer 发布多条消息到" + topic);
		}
		
		List<KeyedMessage<KafkaMessageKey, Object>> messageList = new ArrayList<>(messages.size());
		
		for (DefaultMessage message : messages) {
			KafkaMessageKey key = new KafkaMessageKey();
			key.setCreateTimeStamp(System.currentTimeMillis());
			key.setTopic(topic);
			
			messageList.add(new KeyedMessage<KafkaMessageKey, Object>(topic, key, message));
		}
		producer.send(messageList);
	}
}
