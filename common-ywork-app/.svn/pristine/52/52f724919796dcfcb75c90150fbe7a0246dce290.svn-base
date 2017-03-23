package me.ywork.util;


import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String,Date>{

	public Date convert(String source) {
		/*DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateTimeFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;*/
		
		if (StringUtils.isBlank(source)) {
			throw new IllegalArgumentException("convert - parameter soruce is null or empty.");
		}
		
		if (!StringUtils.isNumeric(source)) {
			throw new IllegalStateException("日期字符串必须为以毫秒为单位的数字。");
		}
		
		Long milliseconds = Long.valueOf(source);
		
		return new Date(milliseconds);
	}
}
