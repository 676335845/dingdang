package com.heyi.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class HtmlEscaper {


	public static final String escapeHTML(String s) {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(s)) {
			int n = s.length();
			for (int i = 0; i < n; i++) {
				char c = s.charAt(i);
				switch (c) {
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '\'':
					sb.append("&#39;");
					break;
				case '(':
					sb.append("&#40;");
					break;
				case ')':
					sb.append("&#41;");
					break;
				case ' ':
					sb.append("&nbsp;");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}

	public static final String escapeHTML2(String s) {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(s)) {
			sb.append(s.replaceAll("&#39;", "\\\\'").replaceAll("\\s",""));
		}
		return sb.toString();
	}
	
	/**
	 * 替换对象中的特殊字符
	 * @param model 替换处理的对象
	 */
	public static <E extends IHtmlEscaperable> void escapeHTML(E model) {
		if (model == null) {
			throw new NullPointerException("escapeHTML - parameter model is null.");
		}
		
		model.setDealtString(escapeHTML(model.getOriginalString()));
	}
	
	/**
	 * 替换一批对象中的特殊字符
	 * @param models  需要处理的对象列表
	 */
	public static <E extends IHtmlEscaperable> void escapeHTML(List<E> models) {
		if (models == null) {
			throw new NullPointerException("escapeHTML - parameter models is null.");
		}
		
		for (E model : models) {
			if (model == null) {
				continue;
			}
			
			escapeHTML(model);
		}
	}
	
	/**
	 * 替换POJO对象中多个字段值的特殊字符
	 * @param model  需要处理的对象
	 */
	public  static <E extends IHtmlEscaperBatchable> void escapeHTML(E model) {
		if (model == null) {
			throw new NullPointerException("escapeHTML - parameter model is null.");
		}
		
		Map<String, String> stringMap = model.getOriginalStringMap();
		if (stringMap != null) {
			for (Entry<String, String> entry : stringMap.entrySet()) {
				if (entry == null) {
					continue;
				}
				
				entry.setValue(escapeHTML(entry.getValue()));
			}
			
			// 设置替换后的字符串
			model.setDealtStringMap(stringMap);
		}
	}
	
	/**
	 * 替换一批对象中的特殊字符
	 * @param models  需要处理的对象列表
	 */
	public static <E extends IHtmlEscaperBatchable> void batchEscapeHTML(List<E> models) {
		if (models == null) {
			throw new NullPointerException("escapeHTML - parameter models is null.");
		}
		
		for (E model : models) {
			if (model == null) {
				continue;
			}
			
			escapeHTML(model);
		}
	}
}
