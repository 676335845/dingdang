package com.heyi.framework.springmvc;

import java.io.File;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebServer {
	private static final String CONTEXT_PATH = "/";
	
	private static final String LOG_PATH = "logs/access/yyyy_mm_dd.request.log";
	
	private static final String WEB_XML = "META-INF/webapp/WEB-INF/web.xml";
	
	private static final String CLASS_ONLY_AVAILABLE_IN_IDE = "com.heyi.IDE";
	
	private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/main/java/META-INF/webapp";

	public static interface WebContext {
		public File getWarPath();

		public String getContextPath();
	}

	private Server server;
	private int port;
	private String bindInterface;

	public WebServer(int aPort) {
		this(aPort, null);
	}

	public WebServer(int aPort, String aBindInterface) {
		port = aPort;
		bindInterface = aBindInterface;
	}

	public void start() throws Exception {
		server = new Server(createThreadPool());
		server.addConnector(createConnector(server));
		server.setHandler(createHandlers());
		server.setStopAtShutdown(true);
		server.start();
	}

	public void join() throws InterruptedException {
		server.join();
	}

	public void stop() throws Exception {
		server.stop();
	}

	private ThreadPool createThreadPool() {
		QueuedThreadPool _threadPool = new QueuedThreadPool();
		_threadPool.setMinThreads(20);
		_threadPool.setMaxThreads(500);
		return _threadPool;
	}

	private ServerConnector createConnector(Server server) {
		ServerConnector _connector=new ServerConnector(server);
      
		int _port = port;
		try {
			while(true){
				new Socket("127.0.0.1", _port).close();
				_port++;
			}
		} catch (ConnectException e) {
		} catch (Exception e) {
		}
		
		_connector.setPort(_port);
		_connector.setHost(bindInterface);
		return _connector;
	}

	private HandlerCollection createHandlers() {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath(CONTEXT_PATH);

		if (isRunningInShadedJar()) {
			_ctx.setWar(getShadedWarUrl());
		} else {
			_ctx.setWar(PROJECT_RELATIVE_PATH_TO_WEBAPP);
		}

		List<Handler> _handlers = new ArrayList<Handler>();

		_handlers.add(_ctx);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers(_handlers.toArray(new Handler[0]));

		RequestLogHandler _log = new RequestLogHandler();
		_log.setRequestLog(createRequestLog());

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts, _log });

		return _result;
	}

	private RequestLog createRequestLog() {
		NCSARequestLog _log = new NCSARequestLog();

		File _logPath = new File(LOG_PATH);
		_logPath.getParentFile().mkdirs();

		_log.setFilename(_logPath.getPath());
		_log.setRetainDays(90);
		_log.setExtended(false);
		_log.setAppend(true);
		_log.setLogTimeZone("GMT+8");
		_log.setLogLatency(true);
		return _log;
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
