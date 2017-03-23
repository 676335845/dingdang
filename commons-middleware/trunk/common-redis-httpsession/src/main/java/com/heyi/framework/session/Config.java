package com.heyi.framework.session;

public class Config {
	public static final String REDISSESSION_PREX = "RedisHttpSessionManager_";
	/**
	 * 密钥
	 */
	public static final String SECRET = "OJ4PQYMMNCFeKbF0nqvpvHkBq5M=";
	/**
	 * 分钟 
	 */
	public static final int EXPIRATION = 60 *24 * 30; //30天
}