package com.heyi.utils;

import java.util.*;

/**
 * 所有集合工具方法类
 * @author TangGang  2015年7月30日
 *
 */
public class CollectionUtils {
	/**
	 * 将列表根据指定键值进行分组
	 * @param objects  列表数据对象
	 * @return  分组map对象
	 */
	public static <K, E extends IGroupable<K>> 
			Map<K, List<E>> groupCollection(List<E> objects) {
		
		if (objects == null) {
			throw new NullPointerException(
					"groupCollection - parameter objects is null.");
		}
		
		Map<K, List<E>> groupMap = new HashMap<K, List<E>>();
		List<E> tempList = null;
		
		for (E object : objects) {
			if (object == null) {
				continue;
			}
			
			// 获取当前组
			tempList = groupMap.get(object.getGroupKey());
			
			// 如果为空则创建分组列表
			if (tempList == null) {
				tempList = new ArrayList<E>();
				
				groupMap.put(object.getGroupKey(), tempList);
			}
			
			// 加入分组对象
			tempList.add(object);
		}
		
		return groupMap;
	}

	/**
	 * 将列表根据指定键值进行分组并根据key值排序
	 * @param objects  列表数据对象
	 * @param desc     true降序，false升序
	 * @return  分组map对象
	 */
	public static <K, E extends IGroupable<K>>
	Map<K, List<E>> groupCollectionSortByKey(List<E> objects, boolean desc) {

		if (objects == null) {
			throw new NullPointerException(
					"groupCollection - parameter objects is null.");
		}

		TreeMap<K, List<E>> groupMap = new TreeMap<K, List<E>>();
		List<E> tempList = null;

		for (E object : objects) {
			if (object == null) {
				continue;
			}

			// 获取当前组
			tempList = null;
			if (!groupMap.isEmpty()) {
				tempList = groupMap.get(object.getGroupKey());
			}

			// 如果为空则创建分组列表
			if (tempList == null) {
				tempList = new ArrayList<E>();

				groupMap.put(object.getGroupKey(), tempList);
			}

			// 加入分组对象
			tempList.add(object);
		}

		// 倒序输出
		if (desc) {
			return groupMap.descendingMap();
		}

		return groupMap;
	}
	
	/**
	 * 将list集合使用指定的分隔符拼接成字符串
	 * @param items    来源列表
	 * @param separator  分隔符
	 * @return  使用分隔符拼接的字符串
	 */
	public static <E> String listToString(List<E> items, char separator) {
		if (items == null) {
			throw new NullPointerException(
					"listToString - parameter items is null.");
		}
		
		StringBuilder sb = new StringBuilder();
		boolean hasElement = false;
		for (E item : items) {
			if (item == null) {
				continue;
			}
			
			if (hasElement) {
				sb.append(separator);
			}
			
			sb.append(item);
			
			hasElement = true;
		}
		
		return sb.toString();		
	}
	
	/**
	 * 将多个list中的数据放在set中，以便压缩掉重复值
	 * @param lists   多个list对象
	 * @return  压缩后的set对象
	 */
	public static <E> Set<E> CompressionRepetition(List<E>...lists) {
		if (lists == null) {
			throw new NullPointerException(
					"CompressionRepetition - parameter lists is null.");
		}
		
		Set<E> datas = new HashSet<E>();
		for (List<E> tempList : lists) {
			if (tempList == null) {
				continue;
			}
			
			for (E e : tempList) {
				datas.add(e);
			}
		}
		
		return datas;
	}
	
	/**
	 * 压缩List对象中的重复数据
	 * @param lists  一个或多个List对象
	 * @return  压缩后的List对象
	 */
	public static <E> List<E> CompressionList(List<E>...lists) {
		Set<E> datas = CompressionRepetition(lists);
		
		return new ArrayList<E>(datas);
	}
	
	/**
	 * 将list转换成Map
	 * @param list  源列表
	 * @return  根据指定Key生成Map
	 */
	public static <K, V extends IListToMapable<K>> Map<K, V> listToMap(
			List<V> list) {
		if (list == null) {
			throw new NullPointerException(
					"listToMap - parameter list is null.");
		}
		
		Map<K, V> desMap = new HashMap<K, V>();
		
		for (V v : list) {
			desMap.put(v.getMapkey(), v);
		}
		
		return desMap;		
	}
}
