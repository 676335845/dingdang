package com.heyi.framework.cassandra.repository;

import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolOptions.Compression;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.heyi.framework.cassandra.exception.DBInitException;

/**
 * cassandra db 统一连接 abstract类
 * @author sulta
 *
 */
public abstract class AbstractCassandraRepository {
	private static final Logger log = LoggerFactory.getLogger(AbstractCassandraRepository.class);
	
	public static class LightWeightTransactionException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public LightWeightTransactionException(String message, Throwable cause) {
			super(message, cause);
		}

		public LightWeightTransactionException(String message) {
			super(message);
		}
	}
	
	private Cluster cluster;
	
	protected abstract Session getSession();
		
	/**
	 * 必须在init的时候就call getSession，而不是用的时候Call
	 * @param hosts
	 * @param keyspace
	 * @param userName
	 * @param pwd
	 * @param replication_factor
	 * @return
	 * @throws DBInitException
	 */
	public Session getSession(String[] hosts , String keyspace , String userName, String pwd , int replication_factor) throws DBInitException {
		Session session = null;
		synchronized (this) {
			if(getSession()==null){
				try {					
					Cluster.Builder builder = Cluster.builder();
					for (int i = 0; i < hosts.length; i++) {
						builder.addContactPoint(hosts[i]);
					}
					//builder.addContactPoint(host2);
					SocketOptions so = new SocketOptions();
			        so.setReadTimeoutMillis(60 * 60 * 1000);
			        so.setConnectTimeoutMillis(60 * 60 * 1000);
			        builder.withSocketOptions(so);
					
			        if(userName != null){
			        	builder.withCredentials(userName, pwd);
			        }
			        
			        PoolingOptions po = new PoolingOptions();
			        po.setCoreConnectionsPerHost(HostDistance.LOCAL, 1);
			        po.setCoreConnectionsPerHost(HostDistance.REMOTE, 1);
			        builder.withPoolingOptions(po);

			        QueryOptions queryOptions = new QueryOptions();
			        
			        //Ensure that the write has been written to at least 1 node's commit log and memory table before responding to the client.
			        queryOptions.setConsistencyLevel(ConsistencyLevel.ONE); 
			        builder.withQueryOptions(queryOptions);

			        builder.withCompression(Compression.LZ4);
			        builder.withReconnectionPolicy(new ConstantReconnectionPolicy(1000L));
			        builder.withLoadBalancingPolicy(new TokenAwarePolicy(new RoundRobinPolicy()));
			        cluster = builder.build();
			        cluster.init();
			        
			        session = cluster.connect();
			        
			        if(replication_factor==0) replication_factor = 1;
			        
			        String cql = "CREATE KEYSPACE IF NOT EXISTS " + keyspace + " WITH replication "
			                + "= {'class':'SimpleStrategy', 'replication_factor':"+replication_factor+"};";
			        System.out.println(cql);
			        session.execute(cql);
			        
			        /**
			         * 
	SimpleStrategy针对是一个data center中的多个存储节点(node)的存储，strategy_options表示数据存储所有存储节点(node)的复本数量，选择node的规则是在data center中按照顺时针的方向进行选择；
	NetworkTopologyStrategy是针对多个data center的情况进行处理，这个是以防同一个data center中的所以节点同时出现问题，如掉电；

			         */
			        //        session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE_NAME + " WITH replication "
			        //                + "= {'class':'NetworkTopologyStrategy', 'DC1':2, 'DC2':1};");
			        session.execute("USE " + keyspace);
				} catch (Exception e) {
					throw new DBInitException("Could not connect to Cassandra server", e);
				}
			}else{
				session = getSession();
			}
		}
		
		return session;
	}
	
	protected ResultSet execute(Statement stmt) {
		if(log.isDebugEnabled()){
			if (stmt instanceof BatchStatement) {
				BatchStatement batch = (BatchStatement) stmt;
				for (Statement st : batch.getStatements()) {
					log.debug("executing cql: {}" , new Object[]{
							st.toString()
						});
				}
			}else{
				log.debug("executing cql: {}" , new Object[]{
						stmt.toString()
					});
			}
		}
		return getSession().execute(stmt);
	}
	
	protected void execute(String cql){
		System.out.println(cql);
		getSession().execute(cql);
	}
	
	public  synchronized void destroy() {
		getSession().close();
	}
	
	public static String generateID() {
		return generateID(System.currentTimeMillis());
	}
	
	protected static String generateID(long time) {
		String rtnVal = Long.toHexString(time);
		rtnVal += UUID.randomUUID();
		rtnVal = rtnVal.replaceAll("-", "");
		return rtnVal.substring(0, 32);
	}
	
	protected static String readValue(String key) {
		Properties props = new Properties();
		String value = "";
		try {
			InputStream in = AbstractCassandraRepository.class.getClassLoader().getResourceAsStream("cassandra.properties");
			props.load(in);
			value = props.getProperty(key);
			in.close();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
