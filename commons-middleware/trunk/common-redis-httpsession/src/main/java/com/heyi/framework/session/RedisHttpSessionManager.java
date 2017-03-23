package com.heyi.framework.session;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.kafka.http.session.SessionManager;
import org.redisson.Redisson;
import org.redisson.core.RMap;
import org.springframework.util.Assert;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * redis http session共享
 * @author sulta
 *
 */
public class RedisHttpSessionManager implements SessionManager{
	
	
	private org.redisson.Redisson redisson;
	
	/**
	 * Session缺省存放时间
	 */
	private int sessionTimeout = 30;
	
	public int getSessionTimeout() {
		return sessionTimeout;
	}
	
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	
	public RedisHttpSessionManager() {		
	}
	
	
	public RMap<String, Object> getSessionAttributes(String sessionId) {
		RMap<String, Object> map = redisson.getMap(Config.REDISSESSION_PREX.concat(sessionId));
		return map;
	}

	public void setRedissonConfig(org.redisson.Config config) {
		this.redisson = Redisson.create(config);
	}

	@Override
	public HttpSession getSession(String sessionId) {
		Assert.notNull(sessionId);
		try {
			return __RedisSession_Cache.get(sessionId);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected RMap<String, Object> getSessionAttributes0(String sessionId) {
		RMap<String, Object> map = redisson.getMap(Config.REDISSESSION_PREX.concat(sessionId));
		map.expire(Config.EXPIRATION, TimeUnit.MINUTES);
		return map;
	}
	
	/**
	 * 缓存
	 */
	private final LoadingCache<String, RedisHttpSession> __RedisSession_Cache = CacheBuilder
			.newBuilder()
			.maximumSize(1024)
			.build(new CacheLoader<String, RedisHttpSession>() {
				@Override
				public RedisHttpSession load(String sessionId) throws Exception {
					RedisHttpSession session = new RedisHttpSession(null ,sessionId ,RedisHttpSessionManager.this);
					return session;
				}
			});
	
}
