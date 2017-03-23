package me.ywork.org.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ding2OrgUtil {
	
	private static final Logger log = LoggerFactory.getLogger(Ding2OrgUtil.class);
	private static final String DING2ORGCONFIGFILENAME = "config.properties";
	private static Properties ding2orgProperties = null;
	
	private static void init() {
//		if(ding2orgProperties==null){
//			ding2orgProperties = new Properties();
//			InputStream is = null;
//			try {
//				File mq = new File(DING2ORGCONFIGFILENAME);
//				if(log.isTraceEnabled()) log.trace("try to load ding2org configuration from {}",mq.getAbsoluteFile());
//				if(mq.exists()){
//					is = new FileInputStream(mq);
//				}else{
//					if(log.isTraceEnabled()) log.trace("try to load ding2org configuration from classpath");
//					is = MQConfig.class.getClassLoader().getResourceAsStream(DING2ORGCONFIGFILENAME);
//					if(is==null){
//						is = MQConfig.class.getResourceAsStream("/" + DING2ORGCONFIGFILENAME);
//						if (is == null) {
//							log.warn("could not load ding2org configuration!!!");
//						}
//					}
//				}
//				ding2orgProperties.load(is);
//	        } catch(Exception ignored) {
//	        }finally{
//	        	try {
//	                is.close();
//	            }catch (Exception e) {
//	            }
//	        }
//		}
	}
	
	public static String getDing2OrgSuiteId(){
		init();
		if(ding2orgProperties!=null){
			return ding2orgProperties.getProperty("ding2org.suiteid");
		}
		return null;
	}
	
	//双11时禁用同步服务
	public static boolean todayIsDisabledSyncDay(Date today){
		Calendar cal =Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.MONTH, 10); //这是11月
		cal.set(Calendar.DAY_OF_MONTH, 11);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		
		long double11StartTime = cal.getTimeInMillis() - TimeUnit.HOURS.toMillis(2); //这个工作在10号晚上10点左右就开始做吧
		long double11endTime = cal.getTimeInMillis() + TimeUnit.DAYS.toMillis(1) + TimeUnit.HOURS.toMillis(1) ; //我们12号凌晨1点再启动
		
		long now = today==null?System.currentTimeMillis():today.getTime();
		return (now >= double11StartTime &&  now < double11endTime);
	}
	
	public static void main(String[] args) throws ParseException {
		
		System.out.println(todayIsDisabledSyncDay(null));		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		Date double11 =  df.parse("2015-11-11 3:0:0");
		System.out.println(todayIsDisabledSyncDay(double11));
		double11 =  df.parse("2015-11-11 23:10:10");
		System.out.println(todayIsDisabledSyncDay(double11));
	}
}
