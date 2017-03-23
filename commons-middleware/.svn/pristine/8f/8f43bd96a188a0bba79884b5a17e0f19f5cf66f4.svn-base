package com.heyi.framework.messagebus.ons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class OnsConfig {

	private final static String accessKey;
	private final static String secretKey;
	
	static {
		accessKey	= readValue("ons.accesskey");
		secretKey	= readValue("ons.secretkey");
	}

	public static String getAccesskey() {
		return accessKey;
	}


	public static String getSecretkey() {
		return secretKey;
	}


	public static String readValue(String key) {
		Properties props = new Properties();
		String value = "";
		InputStream in = null;
		try {
			File file = new File("config/ons-config.properties");
			if(file.exists()){
				in = new FileInputStream(file);
			}else{
				in = OnsConfig.class.getClassLoader().getResourceAsStream("ons-config.properties");
			}
			props.load(in);
			value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return value;
	}
}
