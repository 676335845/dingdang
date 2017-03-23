package com.heyi.framework.messagebus.ons;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.heyi.framework.messagebus.message.OnsMessage;
import com.heyi.framework.messagebus.ons.codec.JsonJacksonCodec;

/**
 * Ons消息发送
 * @author sulta
 *
 */
public class OnsProducer {
	private static final Logger log = LoggerFactory.getLogger(OnsProducer.class);	
	private static final JsonJacksonCodec codec = new JsonJacksonCodec();
	
	private static final Map<String, OnsProducer> producers = new HashMap<>();
	
	private final Producer producer;
	private final String topic;
	
	/**
	 * 
	 * @param topic
	 * @param producerId
	 */
	private OnsProducer(String topic , String producerId) {
		this.topic = topic;
		Properties properties = new Properties();	
		properties.put(PropertyKeyConst.ProducerId, producerId);
		properties.put(PropertyKeyConst.AccessKey, OnsConfig.getAccesskey());
		properties.put(PropertyKeyConst.SecretKey, OnsConfig.getSecretkey());
		this.producer = ONSFactory.createProducer(properties);
		this.producer.start();
	}
	
	/**
	 * 
	 * @param topic
	 * @return
	 */
	public static OnsProducer getProducer(OnsTopics topic) {
		String key = topic.getTopic().concat(topic.getDefaultProducer());
		OnsProducer producer = producers.get(key);
		if (producer == null) {
			synchronized (producers) {
				producer = producers.get(key);
				if (producer == null) {
					producer = new OnsProducer(topic.getTopic(), topic.getDefaultProducer());
					producers.put(key, producer);
				}
			}
		}
		return producer;
	}
	
	/**
	 * 
	 * @param message
	 * @param tag
	 * @return
	 */
	public String sendMessage(OnsMessage message , String tag){
		byte[] bodyBytes = codec.encodeValue(message);
		Message msg = new Message(
				topic,
				tag,
				bodyBytes);
		
		if (message.getStartDeliverTime() != null && message.getStartDeliverTime() > 0) {
			msg.setStartDeliverTime(message.getStartDeliverTime());
		}	    
	    msg.setKey(message.getMessageKey());
		SendResult sendResult = producer.send(msg);
		
		if(log.isTraceEnabled()){
			log.trace("发送ONS消息:{}" , message);
		}
		return sendResult.getMessageId();
	}
}
