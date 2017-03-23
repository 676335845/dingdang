package com.heyi.utils;

import java.util.Map;

/**
 * 批次替换字符串中的页面特殊字符，以防攻击
 * @author TangGang  2015年9月10日
 *
 */
public interface IHtmlEscaperBatchable {
	
	/**
	 * 如果在一个对象中存在多个字段需要处理<br/>可以将字段名称作为map的key,
	 * 其对应的值作为map的value
	 * @return  替换特殊字符后的字符串集合
	 */
	public Map<String, String> getOriginalStringMap();
	
	/**
	 * 设置处理过后的字符串<br/>
	 * 可根据字符名称获取其对应的替换特殊字符后的内容
	 * @param dealtMap  替换特殊字符后的字符串集合
	 */
	public void setDealtStringMap(Map<String, String> dealtMap);
}
