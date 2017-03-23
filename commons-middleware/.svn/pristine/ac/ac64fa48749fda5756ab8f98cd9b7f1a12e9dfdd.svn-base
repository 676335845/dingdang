package com.heyi.framework.messagebus.service;

import java.util.ArrayList;
import java.util.List;

import org.kafka.context.KafkaConsumerManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.heyi.framework.messagebus.consumer.DefaultMessageConsumer;
import com.heyi.framework.messagebus.message.IMessageHandler;
import com.heyi.framework.messagebus.message.MessageHandlers;

/**
 * 消费者初始化
 * @author sulta
 *
 */
public class ConsumerInitializer implements InitializingBean , ApplicationContextAware {
	
	public void afterPropertiesSet() throws Exception {
		// 初始化所有消息处理器
		List<IMessageHandler> messageHandlers = getBeansForType(IMessageHandler.class);
		for (IMessageHandler messageHandler : messageHandlers) {
			MessageHandlers.addHandler(messageHandler);
		}
		// 初始化所有kafka消息者
		KafkaConsumerManager.getInstance().startControllersByConsumerclass(DefaultMessageConsumer.class.getName());
		
		applicationContext.publishEvent(new ConsumerRefreshedEvent(this));
	}
	
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ConsumerInitializer.applicationContext = applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getBeansForType(Class<T> type) {
		String[] beanNameArr = applicationContext.getBeanNamesForType(type);
		List<T> beanList = new ArrayList<T>();
		for (int i = 0; i < beanNameArr.length; i++) {
			beanList.add((T) applicationContext.getBean(beanNameArr[i]));
		}
		return beanList;
	}
}
