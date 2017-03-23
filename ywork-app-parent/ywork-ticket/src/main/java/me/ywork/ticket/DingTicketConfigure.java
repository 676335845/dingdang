package me.ywork.ticket;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DingTicketConfigure {
	private static Logger logger = LoggerFactory.getLogger(DingTicketConfigure.class);

	public final static String defaultToken;
	public final static String defaultSuiteKey;
	public final static String defaultAESKey;
	public final static boolean isTest;
	public final static String verificationCode;
	public final static  String MarketApiTestUrl;
	public final static  String MarketApiUrl;
	public final static String yworkSalaryBillingSuiteKey;

	static {
		logger.info("开始从资源文件dingticket.properties中加载参数配置。");

		InputStream is = ClassLoader.getSystemResourceAsStream("ticket.properties");
		if (is == null) {
			throw new RuntimeException("载资源文件dingticket.properties加载失败。");
		}

		Properties prop = new Properties();

		try {
			prop.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 

		defaultToken = prop.getProperty("ticket.dd.defaultToken");
		if (StringUtils.isEmpty(defaultToken)) {
			throw new RuntimeException("在资源文件dingticket.properties中未找到参数【ticket.dd.defaultToken】的配置值。");
		}

		defaultSuiteKey = prop.getProperty("ticket.dd.defaultSuiteKey");
		if (StringUtils.isEmpty(defaultSuiteKey)) {
			throw new RuntimeException("在资源文件dingticket.properties中未找到参数【ticket.dd.defaultSuiteKey】的配置值。");
		}

		defaultAESKey = prop.getProperty("ticket.dd.defaultAESKey");
		if (StringUtils.isEmpty(defaultAESKey)) {
			throw new RuntimeException("在资源文件dingticket.properties中未找到参数【ticket.dd.defaultAESKey】的配置值。");
		}

		verificationCode = prop.getProperty("ticket.dd.verificationcode");
		if (StringUtils.isEmpty(verificationCode)){
			throw new RuntimeException("在资源文件dingticket.properties中未找到参数【ticket.dd.verificationcode】的配置值。");
		}

		MarketApiTestUrl = prop.getProperty("ticket.dd.MarketApiTestUrl");
		if (StringUtils.isEmpty(MarketApiTestUrl)){
			throw new RuntimeException("在资源文件dingticket.properties中未找到参数【ticket.dd.MarketApiTestUrl】的配置值。");
		}

		MarketApiUrl = prop.getProperty("ticket.dd.MarketApiUrl");
		if (StringUtils.isEmpty(MarketApiUrl)){
			throw new RuntimeException("在资源文件dingticket.properties中未找到参数【ticket.dd.MarketApiUrl】的配置值。");
		}

		yworkSalaryBillingSuiteKey = prop.getProperty("ticket.dd.yworkSalaryBillingSuiteKey");
		if (StringUtils.isEmpty(yworkSalaryBillingSuiteKey)){
			throw new RuntimeException("在资源文件dingticket.properties中未找到参数【ticket.dd.yworkSalaryBillingSuiteKey】的配置值。");
		}

		String test = prop.getProperty("ticket.dd.test");
		isTest = StringUtils.isNotBlank(test) && test.equals("true");

		logger.info("资源文件dingticket.properties中的参数配置加载完成。");
	}
}
