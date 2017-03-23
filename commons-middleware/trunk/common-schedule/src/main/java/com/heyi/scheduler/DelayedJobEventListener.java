package com.heyi.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.heyi.framework.cassandra.codc.JacksonCodec;
import com.heyi.framework.messagebus.message.MessageHandler;
import com.heyi.message.timedtask.TimedTaskAlarmClock;

/**
 * 此bean需要加入sping.xml。
 * 业务模块的某个service如果需要响应定时任务，则必须实现 DelayedJobEventListener.Delegate 类
 * @author sulta
 *
 */
public class DelayedJobEventListener implements MessageHandler<TimedTaskAlarmClock> , ApplicationContextAware{
	private static final Logger log = LoggerFactory.getLogger(DelayedJobEventListener.class);	
	private final List<Delegate> delegates = new ArrayList<>();
	
	@Override
	public void onMessage(TimedTaskAlarmClock message) {
		if(log.isTraceEnabled()){
			log.trace("@新的定时任务消息：" + message.getFdName());
		}
		try {
			for (Delegate delegate : delegates) {
				if(delegate.supportsCategory(message.getFdTaskCategory())){
					Object param = null;
					if(message.getFdTaskParam()!=null){
						param = JacksonCodec.getInstance().decodeObjectFromByteArray(message.getFdTaskParam());
					}
					delegate.onTimedTaskEvent(message, param);
				}
			}					
		} catch (Exception e) {
			e.printStackTrace();
			log.error("", e);
		}finally {			
		}
	}
	
	@Override
	public boolean supports(Class<?> classOfMsg) {
		if(TimedTaskAlarmClock.class.isAssignableFrom(classOfMsg)){
			return true;
		}
		return false;
	}
		
	private List getBeansForType(Class type) {
		String[] beanNameArr = applicationContext.getBeanNamesForType(type);
		List beanNameList = Arrays.asList(beanNameArr);
		List beanList = new ArrayList();
		for (int i = 0; i < beanNameArr.length; i++) {
			if (beanNameArr[i].endsWith("Target")) {
				String serviceBeanName = beanNameArr[i].substring(0,
						beanNameArr[i].length() - 6)
						+ "Service";
				if (!beanNameList.contains(serviceBeanName))
					beanList.add(applicationContext.getBean(beanNameArr[i]));
			} else {
				beanList.add(applicationContext.getBean(beanNameArr[i]));
			}
		}
		return beanList;
	}
		
	private ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
		// 初始化所有消息处理器
		List<Delegate> delegates = getBeansForType(Delegate.class);
		if (delegates.size() == 0) {
			log.warn("@@DelayedJobEventListener 没有任何DelayedJobEventListener.Delegate,可能导致无法处理定时任务事件");
		}
		for (Delegate d : delegates) {
			this.delegates.add(d);
		}
	}
}
