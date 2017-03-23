package com.heyi.scheduler;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.heyi.framework.cassandra.codc.JacksonCodec;
import com.heyi.framework.messagebus.kafka.KafkaProducer;
import com.heyi.framework.messagebus.ons.codec.JsonJacksonCodec;
import com.heyi.message.timedtask.TimedTaskArrangement;


/**
 * 延迟任务安排器
 * @author sulta
 *
 */
public abstract class DefaultJobArranger {
	private static final Logger log = LoggerFactory.getLogger(DefaultJobArranger.class);
	private static final String CHANNELNAME = "pub_timedtask_receiver";
	private DefaultJobArranger() {
	}
	
	
	/**
	 * 安排一次性任务
	 * @param task
	 * @param taskParam 任务运行时参数
	 * @param delay 延迟多长时间运行
	 * @param unit 延迟多长时间运行
	 */
	public static void arrangeOneTimeTask(ITimedTaskKey task , Object taskParam , long delay, TimeUnit unit) {
		Date triggerTime = new Date(System.currentTimeMillis() + unit.toMillis(delay));
		arrangeOneTimeTask(task, taskParam, triggerTime);
	}
	
	/**
	 * 安排一次性任务
	 * @param task 
	 * @param taskParam 任务运行时参数
	 * @param triggerTime 任务运行时间
	 */
	public static void arrangeOneTimeTask(ITimedTaskKey task , Object taskParam , Date triggerTime) {
		Assert.notNull(triggerTime);
		Assert.notNull(task);
		Assert.notNull(task.getFdSystemName());
		Assert.notNull(task.getFdTaskCategory());
		Assert.notNull(task.getFdTaskKey());
		TimedTaskArrangement arrangement = new TimedTaskArrangement();
	
		arrangement.setFdSystemName(task.getFdSystemName());
		arrangement.setFdTaskCategory(task.getFdTaskCategory());
		arrangement.setFdTaskKey(task.getFdTaskKey());
		
		arrangement.setFdCreateTime(new Date());
		arrangement.setFdIsRepeat(false);
		arrangement.setFdName(task.getFdName());
		arrangement.setFdTaskChannel(task.getFdTaskChannel());
		arrangement.setFdTriggerTime(triggerTime);
		
		if (taskParam != null) {
			arrangement.setFdTaskParam(JsonJacksonCodec.getInstance().encodeValue(taskParam));
		}
		
		KafkaProducer.getInstance().sendMessage(CHANNELNAME, arrangement);
	}
	
	/**
	 * 安排一个周期性任务
	 * @param task
	 * @param initialDelay 首次延迟多长时间运行
	 * @param period  任务首次触发之后，每隔多久触发一次(时间)
	 * @param unit 任务首次触发之后，每隔多久触发一次(时间参数)
	 * @param repeatTimes 任务首次触发之后，重复执行多少次。
	 * @param taskParam 任务运行时参数
	 */
	public static void arrangeFixedRateTimeTask(ITimedTaskKey task ,long initialDelay , long period,TimeUnit unit, int repeatTimes, Object taskParam) {
		Date triggerTime = new Date(System.currentTimeMillis() + unit.toMillis(initialDelay));
		arrangeFixedRateTimeTask(task, triggerTime, period, unit, repeatTimes, taskParam);
	}
	
	/**
	 * 安排一个周期性任务
	 * @param task 
	 * @param initialTriggerTime 首次触发时间
	 * @param period	任务首次触发之后，每隔多久触发一次(时间)
	 * @param unit		任务首次触发之后，每隔多久触发一次(时间参数)
	 * @param repeatTimes 任务首次触发之后，重复执行多少次。
	 * @param taskParam 任务运行时参数
	 */
	public static void arrangeFixedRateTimeTask(ITimedTaskKey task ,Date initialTriggerTime ,long period ,TimeUnit unit ,int repeatTimes, Object taskParam) {
		Assert.notNull(initialTriggerTime);

		if (System.currentTimeMillis() > initialTriggerTime.getTime()) {
            throw new IllegalArgumentException("initialTriggerTime must be after current time");
        }
		
		Assert.notNull(task);
		Assert.notNull(task.getFdSystemName());
		Assert.notNull(task.getFdTaskCategory());
		Assert.notNull(task.getFdTaskKey());		
		
		TimedTaskArrangement arrangement = new TimedTaskArrangement();
		
		arrangement.setFdSystemName(task.getFdSystemName());
		arrangement.setFdTaskCategory(task.getFdTaskCategory());
		arrangement.setFdTaskKey(task.getFdTaskKey());
		
		arrangement.setFdCreateTime(new Date());
		arrangement.setFdName(task.getFdName());
		arrangement.setFdTaskChannel(task.getFdTaskChannel());
		arrangement.setFdTriggerTime(initialTriggerTime);
		
		arrangement.setFdIsRepeat(true);
		arrangement.setFdPeriod(new Long(unit.toMillis(period)).intValue());
		arrangement.setFdRepeatTimes(repeatTimes);
		
		if(taskParam!=null){
			arrangement.setFdTaskParam(JacksonCodec.getInstance().encodeAsByteArray(taskParam));
		}
		
		KafkaProducer.getInstance().sendMessage(CHANNELNAME, arrangement);
	}
	
	/**
	 * 取消某个已安排的任务
	 * @param task
	 */
	public static void cancelTimedTask(ITimedTaskKey task) {
		Assert.notNull(task);
		Assert.notNull(task.getFdSystemName());
		Assert.notNull(task.getFdTaskCategory());
		Assert.notNull(task.getFdTaskKey());

		TimedTaskArrangement arrangement = new TimedTaskArrangement();
		arrangement.setFdSystemName(task.getFdSystemName());
		arrangement.setFdTaskCategory(task.getFdTaskCategory());
		arrangement.setFdTaskKey(task.getFdTaskKey());
		arrangement.setFdIsCanceled(true);
		KafkaProducer.getInstance().sendMessage(CHANNELNAME, arrangement);
	}
	
	public static void main(String[] args) {
		//样例:
		DefaultJobArranger.arrangeFixedRateTimeTask(new ITimedTaskKey() {
			public String getFdTaskKey() {
				return "xxx";
			}
			
			@Override
			public String getFdTaskChannel() {
				return null;
			}
			
			@Override
			public String getFdTaskCategory() {
				return "xxx";
			}
			
			@Override
			public String getFdSystemName() {
				return "xxx";
			}
			
			@Override
			public String getFdName() {
				return "xxx任务";
			}
		}, 
			new Date(new Date().getTime()+ 60000), //首次触发时间
			1, TimeUnit.DAYS,					//触发间隔
			99999,								//重复次数
			new Object()					//任务运行时参数
		);
	}
}
