package com.heyi.framework.springmvc;

import java.io.File;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;

import org.apache.catalina.startup.Tomcat;

public class WebServer{
	private static final String CONTEXT_PATH = "/";
	
	private static final String LOG_PATH = "logs/access/yyyy_mm_dd.request.log";
	
	private static final String WEB_XML = "META-INF/webapp/WEB-INF/web.xml";
	
	private static final String CLASS_ONLY_AVAILABLE_IN_IDE = "com.heyi.IDE";
	
	private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/main/java/META-INF/webapp";

	public static interface WebContext {
		public File getWarPath();

		public String getContextPath();
	}

	private Tomcat server;
	private int port;
	private String bindInterface = "127.0.0.1";

	public WebServer(int aPort) {
		this(aPort, null);
	}

	public WebServer(int aPort, String aBindInterface) {
		port = aPort;
		bindInterface = aBindInterface;
	}

	public void start() throws Exception {
		server = new Tomcat();
		
		int port = this.port;
		try {
			while(true){
				new Socket(this.bindInterface, port).close();
				port++;
			}
		} catch (ConnectException e) {
		} catch (Exception e) {
		}
		
		server.setPort(port);
		 
		server.setBaseDir(new File("work").getAbsolutePath());
		if (isRunningInShadedJar()) {
			server.addWebapp(CONTEXT_PATH, getShadedWarUrl());
		} else {
			server.addWebapp(CONTEXT_PATH, PROJECT_RELATIVE_PATH_TO_WEBAPP);
		}
		
		server.start();	
	}

	public void join() throws InterruptedException {
		server.getServer().await();
	}

	public void stop() throws Exception {
		try {
			server.stop();
        } finally {
        	server.destroy();
        }
	}

	private boolean isRunningInShadedJar() {
		try {
			Class.forName(CLASS_ONLY_AVAILABLE_IN_IDE);
			return false;
		} catch (ClassNotFoundException anExc) {
			return true;
		}
	}

	private URL getResource(String aResource) {
		return Thread.currentThread().getContextClassLoader()
				.getResource(aResource);
	}

	private String getShadedWarUrl() {
		String _urlStr = getResource(WEB_XML).toString();
		return _urlStr.substring(0, _urlStr.length() - 15);
	}
}
