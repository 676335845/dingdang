package me.ywork.salary;

import com.heyi.framework.springalone.SpringStandaloneServer;

public class Main {
	public static void main(String... anArgs) throws Exception {
		SpringStandaloneServer server = new SpringStandaloneServer();
		server.start("classpath:META-INF/webapp/WEB-INF/spring.xml");
	}
}
