package com.heyi.framework.spring.context;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContext implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		AppContext.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static Object getBean(String beanName) {
		if (null == beanName) {
			return null;
		}
		return applicationContext.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		if (null == requiredType) {
			return null;
		}
		return applicationContext.getBean(requiredType);
	}

	public static <T> List<T> getBeansForType(Class<T> type) {
		String[] beanNameArr = applicationContext.getBeanNamesForType(type);
		List<T> beanList = new ArrayList<T>();
		for (int i = 0; i < beanNameArr.length; i++) {
			beanList.add((T) applicationContext.getBean(beanNameArr[i]));
		}
		return beanList;
	}
}
