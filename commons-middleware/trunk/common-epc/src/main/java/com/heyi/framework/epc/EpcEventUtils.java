package com.heyi.framework.epc;

import java.util.Date;
import java.util.UUID;

import com.heyi.framework.messagebus.kafka.KafkaProducer;
import com.heyi.framework.messagebus.message.epc.EpcKafkaMessage;
import com.heyi.framework.messagebus.message.epc.EpcMessage;
import com.heyi.framework.messagebus.message.epc.EpcOnsMessage;
import com.heyi.framework.messagebus.ons.OnsProducer;
import com.heyi.framework.messagebus.ons.OnsTags;
import com.heyi.framework.messagebus.ons.OnsTopics;
import com.heyi.framework.messagebus.ons.codec.JsonJacksonCodec;
import com.heyi.scheduler.DefaultJobArranger;
import com.heyi.scheduler.DelayedEventTaskKey;

/**
 * 发布EPC事件工具类
 * @author sulta
 *
 */
public abstract class EpcEventUtils {
	
	private static JsonJacksonCodec codec = new JsonJacksonCodec();
	
	private static final boolean isTestEnv; 
	
	static {
		isTestEnv = OnsTopics.ONLINE_ONS_CHANNEL.getTopic().contains("test");
	}
	
	public static EpcEvent cloneEvent(EpcEvent event) {
		return (EpcEvent) codec.decodeValue(codec.encodeValue(event));
	}
	
	public static EpcEvent decodeEvent(EpcMessage message) {
		return (EpcEvent) codec.decodeValue(message.getEvent());
	}

	/**
	 * 
	 * @param event
	 * @param nextEpcEventNo
	 */
	public static void fireEvent(EpcEvent event, String nextEpcEventNo) {
		final EpcEvent newEvent = cloneEvent(event);
		newEvent.setEpcEventNo(nextEpcEventNo);	
		
		EpcKafkaMessage message = new EpcKafkaMessage();		
		message.setEvent(codec.encodeValue(newEvent));
		
		String kafkaTopic = "pub_epc_channel";		
		if (newEvent.getEventMessageKafkaTopic() != null) {
			kafkaTopic = newEvent.getEventMessageKafkaTopic();
		}
		
		if (newEvent.getStartDeliverTime() != null) {
			DefaultJobArranger.arrangeOneTimeTask(new DelayedEventTaskKey(kafkaTopic) {
				
				@Override
				public String getFdTaskKey() {
					return generateID();
				}

				@Override
				public String getFdTaskCategory() {
					return newEvent.getClass().getName();
				}
				
				@Override
				public String getFdName() {
					return null;
				}
				
			}, message, new Date(newEvent.getStartDeliverTime()));
		} else {
			KafkaProducer.getInstance().sendMessage(kafkaTopic, message);
		}
	}
	
	/**
	 * 
	 * @param event
	 * @param nextEpcEventNo
	 */
	public static void fireReliableEvent(EpcEvent event , String nextEpcEventNo) {	
		if (isTestEnv) {
			fireEvent(event, nextEpcEventNo);
			return;
		}
		
		EpcEvent newEvent = cloneEvent(event);
		newEvent.setEpcEventNo(nextEpcEventNo);
		
		EpcOnsMessage message = new EpcOnsMessage();
		message.setEvent(codec.encodeValue(newEvent));
		message.setMessageKey("EpcOnsMessage_".concat(nextEpcEventNo));
		
		if (newEvent.getEventMessageOnsTag() != null) {
			OnsProducer.getProducer(OnsTopics.ONLINE_ONS_CHANNEL).sendMessage(message, newEvent.getEventMessageOnsTag());
		} else {
			OnsProducer.getProducer(OnsTopics.ONLINE_ONS_CHANNEL).sendMessage(message, OnsTags.Tag_Epc);
		}		
	}
	
	private static String generateID() {
		String rtnVal = Long.toHexString(System.currentTimeMillis());
		rtnVal += UUID.randomUUID();
		rtnVal = rtnVal.replaceAll("-", "");
		return rtnVal.substring(0, 32);
	}
}
