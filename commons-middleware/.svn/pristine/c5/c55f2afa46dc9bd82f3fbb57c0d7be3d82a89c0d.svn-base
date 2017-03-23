package com.heyi.framework.cassandra.cache;

import java.nio.ByteBuffer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.heyi.framework.cassandra.codc.JacksonCodec;
import com.heyi.framework.cassandra.exception.DBException;
import com.heyi.framework.cassandra.exception.DBInitException;
import com.heyi.framework.cassandra.repository.AbstractCassandraRepository;


/**
 * 
 * @author sulta
 *
 */
public class CassandraCacheRepository extends AbstractCassandraRepository implements InitializingBean {
	
	private static final String SYSCACHE_TABLE = "sys_cache_table";
	private static final String SYSCACHE_TABLE_EX = "sys_cacheex_table";
	
	private static final String SYSSEQTABLE = "sys_sequence_table";
	
	private static final String GEN_SEQUENCE_PS			= "UPDATE "+SYSSEQTABLE+" SET sequence = sequence + 1 WHERE row_id = ? ;";
	private static final String GET_SEQUENCE_PS 		= "SELECT sequence from "+SYSSEQTABLE+" WHERE row_id = ? ;";
	
	
	private static final Logger log = LoggerFactory.getLogger(CassandraCacheRepository.class);
	
	private Session session;
	
	/**
	 * APPid
	 */
	private static final String APP_ID = "app_id";	
	
	/**
	 * 条目id
	 */
	private static final String ENTRY_ID = "entry_id";	
	/**
	 * 条目资料
	 */
	private static final String ENTRY_DATA = "entry_data";
	
	/**
	 * 生成顺序号
	 */
	private PreparedStatement generateSequenceUpdatePs = null;
	/**
	 * 查看顺序号
	 */
	private PreparedStatement generateSequenceSelectPs = null;
	
	public void initRepository(String[] hosts , String keyspace , String userName, String pwd , int factor)
			throws DBInitException { 
		this.session = getSession(hosts, keyspace, userName, pwd ,factor);
		System.out.println("===========================init cassandradb Repository====================================");
				
		String cql = "CREATE TABLE IF NOT EXISTS " + SYSCACHE_TABLE 
		+ " ("+ENTRY_ID+" varchar, " +
				ENTRY_DATA + " blob, " +
				"PRIMARY KEY("+ENTRY_ID+"));";
		execute(cql);
		
		cql = "CREATE TABLE IF NOT EXISTS " + SYSCACHE_TABLE_EX 
				+ " ("+
				APP_ID+" varchar, " +
				ENTRY_ID+" varchar, " +
				ENTRY_DATA + " blob, " +
				"PRIMARY KEY("+APP_ID+","+ENTRY_ID+"));";
		execute(cql);
				
		cql = "CREATE TABLE IF NOT EXISTS "+SYSSEQTABLE+" (row_id varchar, sequence counter, PRIMARY KEY(row_id));";
		execute(cql);
		

		generateSequenceUpdatePs = session.prepare(GEN_SEQUENCE_PS);
		generateSequenceSelectPs = session.prepare(GET_SEQUENCE_PS);
	}
	
	
	/**
	 * 生成顺序号
	 * @param keys
	 * @return
	 */
	public long generateSequence(String... keys){
		String rowId= getRowId(keys);
		long oldSeq = 0;
		BoundStatement bs = generateSequenceSelectPs.bind(rowId);
		ResultSet rs = session.execute(bs);
		if(! rs.isExhausted()){
			Row row = rs.one();
			oldSeq = row.getLong(0);
		}
		
		bs = generateSequenceUpdatePs.bind(rowId);
		session.execute(bs); //认为一定会成功
		
		return (oldSeq+1);
	}
	
	/**
	 * 查询顺序号
	 * @param keys
	 * @return
	 */
	public long getSequence(String... keys){
		String rowId= getRowId(keys);
		BoundStatement bs = generateSequenceSelectPs.bind(rowId);
		ResultSet rs = session.execute(bs);
		Row row = rs.one();
		if(row!=null)
			return row.getLong(0);
		return 0;
	}
	
	/**
	 * 设置缓存
	 * @deprecated
	 * @param keys
	 * @param value
	 * @param ttlSeconds
	 * @throws DBException
	 */
	public void putInCache(String[] keys, Object value, int ttlSeconds) {
		String rowId= getRowId(keys);
		try {
			Statement insert = QueryBuilder.insertInto(SYSCACHE_TABLE)
					.value(ENTRY_ID, rowId)
					.value(ENTRY_DATA, JacksonCodec.getInstance().encode(value)).using(QueryBuilder.ttl(ttlSeconds));
			//insert.setConsistencyLevel(ConsistencyLevel.QUORUM);
			insert.setConsistencyLevel(ConsistencyLevel.ONE);
			execute(insert);
		} catch (Exception e) {
			throw new RuntimeException("缓存数据时出错", e);
		}
	}
	
	/**
	 * 设置缓存
	 * @deprecated
	 * @param keys
	 * @param value 如果value为null则执行delete操作
	 * @throws DBException
	 */
	public void putInCache(String[] keys, Object value) {
		String rowId= getRowId(keys);
		try {
			if(null == value){
				Delete del = QueryBuilder.delete().from(SYSCACHE_TABLE);
				del.where(QueryBuilder.eq(ENTRY_ID, rowId));
				execute(del);
			}else{
				Statement insert = QueryBuilder.insertInto(SYSCACHE_TABLE)
						.value(ENTRY_ID, rowId)
						.value(ENTRY_DATA, JacksonCodec.getInstance().encode(value));
				//insert.setConsistencyLevel(ConsistencyLevel.QUORUM);
				insert.setConsistencyLevel(ConsistencyLevel.ONE);
				execute(insert);
			}
		} catch (Exception e) {
			throw new RuntimeException("缓存数据时出错", e);
		}	
	}
		
	/**
	 * 获取缓存数据
	 * @deprecated
	 * @param findKey
	 * @return
	 * @throws DBException
	 */
	public Object getFromCache(String[] keys) {
		String rowId= getRowId(keys);
		try {
			Select select = QueryBuilder.select(ENTRY_DATA).from(SYSCACHE_TABLE);
			select.where(QueryBuilder.eq(ENTRY_ID, rowId));
			select.limit(1);
			select.setConsistencyLevel(ConsistencyLevel.ONE);
			ResultSet rs = execute(select);
			if( !rs.isExhausted()) {
				Row row = rs.one();
				ByteBuffer bb = row.getBytes(ENTRY_DATA);
				return JacksonCodec.getInstance().decodeObject(bb);
			}
		} catch (Exception e) {
			throw new RuntimeException("获取缓存数据时发生错误", e);
		}
		return null;
	}
	
	
	/**
	 * 设置缓存
	 * @param appId
	 * @param keys
	 * @param value
	 * @param ttlSeconds
	 * @throws DBException
	 */
	public void putInCache(String appId ,String[] keys, Object value, int ttlSeconds) {
		Assert.notNull(appId);
		Assert.notNull(keys);
		String rowId= getRowId(keys);
		try {
			Statement insert = QueryBuilder.insertInto(SYSCACHE_TABLE_EX)
					.value(APP_ID, appId)
					.value(ENTRY_ID, rowId)
					.value(ENTRY_DATA, JacksonCodec.getInstance().encode(value)).using(QueryBuilder.ttl(ttlSeconds));
			//insert.setConsistencyLevel(ConsistencyLevel.QUORUM);
			insert.setConsistencyLevel(ConsistencyLevel.ONE);
			execute(insert);
		} catch (Exception e) {
			throw new RuntimeException("缓存数据时出错", e);
		}
	}
	
	/**
	 * 设置缓存
	 * @param appId
	 * @param keys
	 * @param value 如果value为null则执行delete操作
	 * @throws DBException
	 */
	public void putInCache(String appId , String[] keys, Object value) {
		Assert.notNull(appId);
		Assert.notNull(keys);
		String rowId= getRowId(keys);		
		try {
			if(null == value){
				Delete del = QueryBuilder.delete().from(SYSCACHE_TABLE_EX);
				del.where(QueryBuilder.eq(APP_ID, appId)).and(QueryBuilder.eq(ENTRY_ID, rowId));
				execute(del);
			}else{
				Statement insert = QueryBuilder.insertInto(SYSCACHE_TABLE_EX)
						.value(APP_ID, appId)
						.value(ENTRY_ID, rowId)
						.value(ENTRY_DATA, JacksonCodec.getInstance().encode(value));
				//insert.setConsistencyLevel(ConsistencyLevel.QUORUM);
				insert.setConsistencyLevel(ConsistencyLevel.ONE);
				execute(insert);
			}
		} catch (Exception e) {
			throw new RuntimeException("缓存数据时出错", e);
		}	
	}
		
	/**
	 * 获取缓存数据
	 * @param appId
	 * @param findKey
	 * @return
	 * @throws DBException
	 */
	public Object getFromCache(String appId , String[] keys) {
		Assert.notNull(appId);
		Assert.notNull(keys);
		String rowId= getRowId(keys);
		try {
			Select select = QueryBuilder.select(ENTRY_DATA).from(SYSCACHE_TABLE_EX);
			select.where(QueryBuilder.eq(APP_ID, appId)).and(QueryBuilder.eq(ENTRY_ID, rowId));
			
			select.limit(1);
			select.setConsistencyLevel(ConsistencyLevel.ONE);
			ResultSet rs = execute(select);
			if( !rs.isExhausted()) {
				Row row = rs.one();
				ByteBuffer bb = row.getBytes(ENTRY_DATA);
				return JacksonCodec.getInstance().decodeObject(bb);
			}
		} catch (Exception e) {
			throw new RuntimeException("获取缓存数据时发生错误", e);
		}
		return null;
	}
	
	private String getRowId(String... keys){
		Assert.notNull(keys);
		return StringUtils.join(keys,'x');
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		String hosts = readValue("cassandra.hosts");
		String schema = readValue("cassandra.cache.db");
		Assert.notNull(schema,"schema 未设置");
		String replication_factor = readValue("cassandra.cache.replication_factor");
		int factor = 0;
		try {
			factor = Integer.parseInt(replication_factor);
		} catch (Exception e) {
		}
		initRepository(hosts.split(";"), schema, null , null,factor);
	}


	@Override
	protected Session getSession() {
		return session;
	}
}
