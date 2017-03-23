package me.ywork.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具方法类
 * @author TangGang  2015年8月29日
 *
 */
public class DateUtils {
	// 日期格式化常量
	/**
	 * 将日期格式为时间分钟，如09：23
	 */
	public static final String DateFormat_HHMM_COLON = "HH:mm";

	// 保存日期格式化对象的Map
	private static Map<String, SimpleDateFormat> dateFormatMap = new ConcurrentHashMap<String, SimpleDateFormat>();

	static {
		// 创建各日期格式的转换器对象
		dateFormatMap.put(DateFormat_HHMM_COLON, new SimpleDateFormat(DateFormat_HHMM_COLON));
	}

	public static Calendar getCalendarInstance() {
		return Calendar.getInstance();
	}
	
	public static Calendar getCalendarInstance(Date date) {
		Calendar calendar = getCalendarInstance();
		
		calendar.setTime(date);
		
		return calendar;
	}
	
	/**
	 * 获取日期部的指定部分
	 * @param date   来源日期对象
	 * @param partType  日期中的部分, 对应Calendar中的静态常量
	 * @return
	 */
	public static int getPartDate(Date date, int partType) {
		if (date == null) {
			throw new NullPointerException(
					"getDepartDate - parameter date is null.");
		}
		
		Calendar calendar = getCalendarInstance();
		calendar.setTime(date);
		
		return calendar.get(partType);
	}

	/**
	 * 获取指定日期是星期几
	 * @param date 日期
	 * @return 0为星期日，1为星期一，以此类推
	 */
	public static int getWeekDay(Date date) {
		return getPartDate(date, Calendar.DAY_OF_WEEK) - 1;
	}
	
	public static int getYear(Date date) {
		return getPartDate(date, Calendar.YEAR);
	}
	
	public static int getMonth(Date date) {
		return getPartDate(date, Calendar.MONTH);
	}
	
	public static int getMonth() {
		return getCalendarInstance().get(Calendar.MONTH);
	}
	
	/**
	 * 获取指定日期中的月份
	 * @param date   日期
	 * @param naturalMonth  是否为自然月份，系统的月份值为0~11，自然月份为1~12
	 * @return
	 */
	public static int getMonth(Date date, boolean naturalMonth) {
		if (date == null) {
			throw new NullPointerException("getMonth - parameter date is null.");
		}
		
		int month = getPartDate(date, Calendar.MONTH);
		
		if (naturalMonth) {
			month += 1;
		}
		
		return month;
	}
	
	public static int getDay(Date date) {
		return getPartDate(date, Calendar.DAY_OF_MONTH);
	}
	
	public static int getHour(Date date) {
		return getPartDate(date, Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinute(Date date) {
		return getPartDate(date, Calendar.MINUTE);
	}
	
	public static int getSecond(Date date) {
		return getPartDate(date, Calendar.SECOND);
	}
	
	public static int getMillisecond(Date date) {
		return getPartDate(date, Calendar.MILLISECOND);
	}
	
	/**
	 * 清除日期中的指定时间部分
	 * @param date  来源日期
	 * @param partType  日期某一部分的类型，对应Calendar中的常量
	 */
	public static Date clearPartDate(Date date, int partType) {
		Calendar calendar = getCalendarInstance();
		
		calendar.setTime(date);
		
		calendar.clear(partType);

		return calendar.getTime();
	}
	
	/**
	 * 清除日期中的时间部分
	 * @param date 来源日期
	 */
	public static Date clearTime(Date date) {
		Calendar calendar = getCalendarInstance();
		
		calendar.setTime(date);
		
		//calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);

		return calendar.getTime();
	}

	/**
	 * 将日期对象的日期部分还原为1970.1.1,只保留时间部分
	 * @param date   日期来源对象
	 * @return
	 */
	public static Date clearDate(Date date) {
		Calendar calendar = getCalendarInstance();

		calendar.setTime(date);

		/*
		calendar.clear(Calendar.YEAR);
		calendar.clear(Calendar.MONTH);
		calendar.clear(Calendar.DAY_OF_YEAR);
        */
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_YEAR, 1);

		return calendar.getTime();
	}
	
	public static Date clearMillisecond(Date date) {
		return clearPartDate(date, Calendar.MILLISECOND);
	}
	
	public static Date generateTime(int hour, int minute, int seconds) {
		Calendar calendar = getCalendarInstance();
		
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, seconds);
		
		return calendar.getTime();
	}
	
	public static Date generateDate(int year, int month, int day) {
		Calendar calendar = getCalendarInstance();
		
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		
		return calendar.getTime();
	}
	
	/**
	 * 在指定日期上的指定部分进行累加操作
	 * @param date   来源日期
	 * @param partType 需要累加的日期部分
	 * @param amount  累加值
	 */
	public static Date addDateByPartType(Date date, int partType, int amount) {
		if (date == null) {
			throw new NullPointerException(
					"addDateByPartType - parameter date is null.");
		}
		
		Calendar calendar = getCalendarInstance();
		
		calendar.setTime(date);
		calendar.add(partType, amount);

		return  calendar.getTime();
	}
	
	public static Date addMinutes(Date date, int minutes) {
		return addDateByPartType(date, Calendar.MINUTE, minutes);
	}
	
	public static Date addDays(Date date, int days) {
		return addDateByPartType(date, Calendar.DAY_OF_MONTH, days);
	}
	
	/**
	 * 判断指定日期是否为当天
	 * @param date  来源日期
	 * @return  true当天，false不是当天
	 */
	public static boolean isToday(Date date) {
		if (date == null) {
			throw new NullPointerException("isToday - parameter date is null.");
		}
		
		Calendar calendar = getCalendarInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		
		calendar.setTime(date);
		
		return year == calendar.get(Calendar.YEAR) &&
				month == calendar.get(Calendar.MONTH) &&
				day == calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 判断两个日期是否同一天
	 * @param date1   比较的第一个日期
	 * @param date2  比较的第二个日期
	 * @return true相同，false不相同
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null) {
			throw new NullPointerException("isToday - parameter date1 is null.");
		}
		
		if (date2 == null) {
			throw new NullPointerException("isToday - parameter date2 is null.");
		}
		
		Calendar calendar = getCalendarInstance();
		calendar.setTime(date1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		
		calendar.setTime(date2);
		
		return year == calendar.get(Calendar.YEAR) &&
				month == calendar.get(Calendar.MONTH) &&
				day == calendar.get(Calendar.DAY_OF_YEAR);
		
	}
	
	/**
	 * @return  当月的第一天的日期
	 */
	public static Date getFirstDateTimeOfCurrentMonth() {
		Calendar calendar = getCalendarInstance();
		
		// 将日期设置为第一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		return calendar.getTime();
	}
	
	public static Date getFirstDateOfCurrentMonth() {
		Date firstDate = getFirstDateTimeOfCurrentMonth();
		
		return clearTime(firstDate);
	}
	
	/**
	 * 获取指定月份的第一天
	 * @param month   月份，其格式为201509
	 * @return  当月的第一天日期
	 */
	public static Date getFirstDateOfMonth(Integer month) {
		if (month == null) {
			throw new NullPointerException("getFirstDateOfMonth - parameter month is null.");
		}
		
		int y = month / 100;
		int m = month % 100 - 1;
		if (m < 0 || m > 11) {
			throw new IllegalStateException("非法的月份：" + m);
		}
		
		Calendar calendar = getCalendarInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, y);
		calendar.set(Calendar.MONTH, m);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		return calendar.getTime();
	}
	
	public static Date getLastDateOfMonth(Integer month) {
		if (month == null) {
			throw new NullPointerException("getFirstDateOfMonth - parameter month is null.");
		}
		
		int y = month / 100;
		int m = month % 100 - 1;
		if (m < 0 || m > 11) {
			throw new IllegalStateException("非法的月份：" + m);
		}
		
		Calendar calendar = getCalendarInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, y);
		calendar.set(Calendar.MONTH, m);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		// 加一个月
		calendar.add(Calendar.MONTH, 1);
		
		// 再减1天即为上个月的最后一天
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		return calendar.getTime();
	}
	
	public static Date getLastDateOfMonth(Date date) {
		if (date == null) {
			throw new NullPointerException("getFirstDateOfMonth - parameter date is null.");
		}
		
		Calendar calendar = getCalendarInstance();
		calendar.setTime(date);
		calendar.clear();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		// 加一个月
		calendar.add(Calendar.MONTH, 1);
		
		// 再减1天即为上个月的最后一天
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		return calendar.getTime();
	}

	/**
	 * 将日期格式化成指定的格式字符串
	 * @param format   对应静态常量
	 * @param date     需要格式化的来源日期对象
	 * @return    格式化后的字符串
	 */
	public static String formatDate(String format, Date date) {
		if (StringUtils.isBlank(format)) {
			throw new IllegalArgumentException("formatDate - parameter format is null or empty.");
		}

		if (date == null) {
			throw new NullPointerException("formatDate - parameter date is null.");
		}

		SimpleDateFormat sdf = dateFormatMap.get(format);
		if (sdf == null) {
			throw new IllegalStateException("未找到指定格式【"+ format + "】对应的日期格式化对象。");
		}

		return sdf.format(date);
	}
	
	public static Date today() {
		return clearTime(new Date());
	}
}
