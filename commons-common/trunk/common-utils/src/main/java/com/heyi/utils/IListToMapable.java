package com.heyi.utils;

/**
 * 将列表根据指定Key类型转换成Map
 * @author TangGang  2015年8月27日
 *
 * @param <K>  Map中的Key
 */
public interface IListToMapable<K> {
	public K getMapkey();
}
