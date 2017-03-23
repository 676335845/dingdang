package com.heyi.framework.springalone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.StringUtils;

public class SpringStandaloneServer {

	private static final Logger logger = LoggerFactory.getLogger(SpringStandaloneServer.class);

	public void start(String springXmlLocation) throws Exception {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.getEnvironment().setActiveProfiles("standalone");

		if (!StringUtils.hasLength(springXmlLocation))
			springXmlLocation = "classpath:spring.xml";

		context.load(springXmlLocation);
		context.refresh();
		logger.info("Spring standalone Server started in {} ms .............",
				System.currentTimeMillis() - context.getStartupDate());

	}
}