package me.ywork.salarybill;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 投票参数配置静态类
 * 
 * @author TangGang 2015年8月11日
 * 
 */
public final class SalaryBillConfigure {
	private static Logger logger = LoggerFactory.getLogger(SalaryBillConfigure.class);
	
	/**
	 * 平台绑定的域名
	 */
	public final static String domainName;
	
	
	/**
	 * Netty中配置的URL
	 */
	public final static String nettyUrl;
	/**
	 * Nginx中配置的静态文件URL
	 */
	public final static String nginxStaticUrl;
	/**
	 * 投票对应的主页
	 */
	public final static String homePage;
	
	/**
	 * 是否为测试模式
	 */
	public final static boolean isTest;
	
	/**
	 * 套件id
	 */
	public final static String suiteId;
	
	/**
	 * 套件id
	 */
	public final static String appId;
	
	/**
	 * 移动客户端版本号
	 */
	public final static String mobileVersion;

	/**
	 * 服务器端版本号
	 */
	public final static String serverVersion;
	
	
	static {
		
		
		logger.info("开始从资源文件vote.properties中加载参数配置。");
		
		InputStream is = ClassLoader.getSystemResourceAsStream("salarybill.properties");
		if (is == null) {
			throw new RuntimeException("载资源文件salarybill.properties加载失败。");
		}
		
		Properties prop = new Properties();
		
		try {
			prop.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		domainName = prop.getProperty("Application.DomainName");
		
		nettyUrl = prop.getProperty("SalaryBill.Netty.url");
		if (nettyUrl == null || nettyUrl.trim().isEmpty()) {
			logger.warn("在资源文件salarybill.properties中未找到参数【Vote.Netty.url】的配置值。");
		}
		
		nginxStaticUrl = prop.getProperty("SalaryBill.Nginx.Static.url");
		if (nginxStaticUrl == null || nginxStaticUrl.trim().isEmpty()) {
			logger.warn("在资源文件salarybill.properties中未找到参数【Vote.Nginx.Static.url】的配置值。");
		}
		
		homePage = prop.getProperty("SalaryBill.homePage");
		if (nettyUrl == null || nettyUrl.trim().isEmpty()) {
			throw new RuntimeException("在资源文件salarybill.properties中未找到参数【Vote.homePage】的配置值。");
		}
		
		String test = prop.getProperty("SalaryBill.test");
		isTest = StringUtils.isNotBlank(test) && test.equals("true");
		
		suiteId = prop.getProperty("SalaryBill.suiteId");
		appId = prop.getProperty("SalaryBill.appId");
		
		mobileVersion = prop.getProperty("SalaryBill.Version.Mobile",
				StringUtils.EMPTY);
		serverVersion = prop.getProperty("SalaryBill.Version.Server",
				StringUtils.EMPTY);
		
		
		logger.info("资源文件salarybill.properties中的参数配置加载完成。");
	}
}
