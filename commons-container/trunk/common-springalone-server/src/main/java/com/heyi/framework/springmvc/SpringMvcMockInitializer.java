package com.heyi.framework.springmvc;

import java.util.List;

import javax.servlet.Filter;

import org.kafka.context.KafkaConsumerManager;
import org.kafka.http.HttpRequestConsumer;
import org.kafka.http.dispatcher.RequestDispatchers;
import org.kafka.http.dispatcher.springmvc.SpringMvcMockDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.heyi.framework.spring.context.AppContext;

/**
 * 
 * @author sulta
 *
 */
public class SpringMvcMockInitializer implements InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringMvcMockInitializer.class);
	
	public void afterPropertiesSet() throws Exception {
		
		Object[] controllers = AppContext.getApplicationContext().getBeansWithAnnotation(RequestMapping.class).values()
				.toArray(new Object[] {});
		
		StandaloneMockMvcBuilder builder = MockMvcBuilders.standaloneSetup(controllers);
		
		List<Filter> filters = AppContext.getBeansForType(Filter.class);
		
		if (!filters.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("add filters : {}" , filters);
			}
			builder.addFilters(filters.toArray(new Filter[] {}));
		}
		
		MockMvc mock = builder.build();
		
		SpringMvcMockDispatcher dispatcher = new SpringMvcMockDispatcher();
		dispatcher.setMockMvc(mock);
		
		if(!dispatcher.isReady()) {
			dispatcher.build();
			RequestDispatchers.setDispatcher(dispatcher);
		}
		
		KafkaConsumerManager.getInstance().startControllersByConsumerclass(HttpRequestConsumer.class.getName());
	}
}
