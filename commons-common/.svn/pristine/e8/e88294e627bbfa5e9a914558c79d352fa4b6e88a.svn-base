package com.heyi.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class PropertityUtil {

	/**
	 * 通过key读取对应的value
	 * 
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String readValue(String filePath, String key) {
		Properties props = new Properties();
		String value = "";
		try {
			InputStream in = PropertityUtil.class.getClassLoader().getResourceAsStream(filePath);//new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 读取properties中的所有配置信息
	 * 
	 * @param filePath
	 * @return
	 */
	public static Map<String, String> readProperties(String filePath) {
		Properties props = new Properties();
		Map<String, String> defaultMap = new HashMap<String, String>();
		try {
			//String webContentPath = ConfigProgramXmlBuilder.getWebContentPath();
			//filePath = webContentPath + "/" + filePath;
			InputStream in = PropertityUtil.class.getClassLoader().getResourceAsStream(filePath);//new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String value = props.getProperty(key);
				key = key.trim();
				value = value.trim();
				defaultMap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultMap;
	}

	// 写入properties信息
	public static void writeProperties(String filePath, Map<String, String> map) {
		Properties prop = new Properties();

		try {
			//注意，这里写数据时，不能用class下的文件路径，而是源文件路径
			//String webContentPath = ConfigProgramXmlBuilder.getWebContentPath();
			//filePath = webContentPath + "/" + filePath;
			InputStream fis =PropertityUtil.class.getClassLoader().getResourceAsStream(filePath); //new FileInputStream(filePath);
			prop.load(fis);
			OutputStream outputStream = new FileOutputStream(filePath);
			if (map != null) {
				Iterator<String> iterator = map.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					if (StringUtils.isNotEmpty(key)) {
						String value = map.get(key);
						prop.setProperty(key, value);
					}
				}
			}
			// 以适合使用 load 方法加载到 Properties 表中的格式，将此 Properties
			// 表中的属性列表（键和元素对）写入输出流
			prop.store(outputStream, "information");
			outputStream.close();
		} catch (IOException e) {
			System.err.println("update error");
		}
	}

}
