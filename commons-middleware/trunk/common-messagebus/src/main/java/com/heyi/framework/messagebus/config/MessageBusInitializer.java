package com.heyi.framework.messagebus.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.kafka.context.MessagebusContextLoader;
import org.springframework.web.WebApplicationInitializer;

/**
 * 
 * @author sulta
 *
 */
public class MessageBusInitializer implements WebApplicationInitializer{
	
	private void registerMessageBusContext(ServletContext aContext){
		new MessagebusContextLoader().initContext(aContext);
	}
	
	public void onStartup(ServletContext aServletContext)
			throws ServletException {
		registerMessageBusContext(aServletContext);
	}
}
