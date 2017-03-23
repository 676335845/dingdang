package me.ywork.suite;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class SuiteConfigure {
	private static Logger logger = LoggerFactory.getLogger(SuiteConfigure.class);
	
	/**
	 * Netty中配置的URL
	 */
	public final static String nettyUrl;
	/**
	 * Nginx中配置的静态文件URL
	 */
	//public final static String nginxStaticUrl;
	/**
	 * 对应的主页
	 */
	public final static String homePage;
	
	/**
	 * 是否为测试模式
	 */
	public final static boolean isTest;
	
	/**
	 * EKP创建WS账号URL
	 */
	public final static String ekpWsUrl;
	
	/*
	 * EKP消息加解密KEY
	 */
	public final static String ekpNotifyAESKey;
	
	/*
	 * EKP消息地址
	 */
	public final static String ekpNotifyUrl;
	
	/**
	 * 日志是否输出到控制台
	 */
	public final static boolean isConsole;

	/**
	 * 获取ekp的对接版本号
	 */
	public final static String ekpYworkVersion;

    /**
     * 会话套件ID
     */
    public final static String chatSuiteId;

	public final static  String cookieVersion;

	public final static String overduePageUrl;
	
	public final static String weiyunAppid;

	/**
	 * v2.4新增，企业号EKP标准套件ID
	 */
	public final static String qyEkpSuiteId = "suiteEkp011";

	/**
	 * v2.4新增，企业号KMS标准套件ID
	 */
	public final static String qyKmsSuiteId = "suiteKms011";

	/**
	 * v2.4新增，钉钉EKP标准套件ID
	 */
	public final static String dingEkpSuiteId = "ddsuite01";

	/**
	 * v2.4新增，钉钉KMS标准套件ID
	 */
	public final static String dingKmsSuiteId = "ddkmssuite01";

	static {
		
		InputStream is = ClassLoader.getSystemResourceAsStream("suite.properties");
		if (is == null) {
			throw new RuntimeException("载资源文件suite.properties加载失败。");
		}
		
		Properties prop = new Properties();
		
		try {
			prop.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		nettyUrl = prop.getProperty("Suite.Netty.url");
		if (nettyUrl == null || nettyUrl.trim().isEmpty()) {
			logger.warn("在资源文件suite.properties中未找到参数【Suite.Netty.url】的配置值。");
		}
		
		/*nginxStaticUrl = prop.getProperty("Suite.Nginx.Static.url");
		if (nginxStaticUrl == null || nginxStaticUrl.trim().isEmpty()) {
			logger.warn("在资源文件suite.properties中未找到参数【Suite.Nginx.Static.url】的配置值。");
		}*/
		
		homePage = prop.getProperty("Suite.homePage");
		if (nettyUrl == null || nettyUrl.trim().isEmpty()) {
			throw new RuntimeException("在资源文件suite.properties中未找到参数【Suite.homePage】的配置值。");
		}
		
		String test = prop.getProperty("Suite.test");
		isTest = StringUtils.isNotBlank(test) && test.equals("true");
		
		String console = prop.getProperty("Suite.isConsole");
		isConsole = StringUtils.isNotBlank(console) && console.equals("true");
		
		ekpWsUrl = prop.getProperty("Suite.ekpWsUrl");
		if (ekpWsUrl == null || ekpWsUrl.trim().isEmpty()) {
			throw new RuntimeException("在资源文件suite.properties中未找到参数【Suite.ekpWsUrl】的配置值。");
		}
		
		ekpNotifyAESKey = prop.getProperty("Suite.ekpNotify.AESKey");
		if (StringUtils.isBlank(ekpNotifyAESKey)){
			throw new RuntimeException("在资源文件suite.properties中未找到参数【Suite.ekpNotify.AESKey】的配置值。");
		}
		
		ekpNotifyUrl = prop.getProperty("Suite.ekpNotify.Url");
		if (StringUtils.isBlank(ekpNotifyUrl)){
			throw new RuntimeException("在资源文件suite.properties中未找到参数【Suite.ekpNotify.Url】的配置值。");
		}

		ekpYworkVersion =prop.getProperty("Suite.ekpYworkVersion.Url");
		if (StringUtils.isBlank(ekpYworkVersion)){
			throw new RuntimeException("在资源文件suite.properties中未找到参数【Suite.ekpYworkVersion.Url】的配置值。");
		}

        chatSuiteId = prop.getProperty("Suite.chatSuiteId");
        if (StringUtils.isBlank(chatSuiteId)){
            throw new RuntimeException("在资源文件suite.properties中未找到参数【Suite.chatSuiteId】的配置值。");
        }

		cookieVersion = prop.getProperty("Suite.cookieVersion");
		if (StringUtils.isBlank(cookieVersion)){
			throw new RuntimeException("在资源文件suite.properties中未找到参数【Suite.cookieVersion】的配置值。");
		}

		overduePageUrl = prop.getProperty("Suite.overduePage.Url");
		if (StringUtils.isBlank(overduePageUrl)){
			throw new RuntimeException("在资源文件suite.properties中未找到参数【Suite.overduePage.Url】的配置值。");
		}
		
		weiyunAppid = prop.getProperty("Suite.weiyun.appid");

		logger.info("资源文件suite.properties中的参数配置加载完成。");
	}
}
