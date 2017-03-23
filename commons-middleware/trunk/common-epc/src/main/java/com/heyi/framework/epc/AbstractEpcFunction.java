package com.heyi.framework.epc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractEpcFunction<E extends EpcEvent> implements EpcFunction<E>{
	
	public static Class<?> getGenericType(Class<?> clazz) {
		if (clazz == Object.class) {
			return null;
		}
		Type type = clazz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType ptype = ((ParameterizedType) type);
			Type[] args = ptype.getActualTypeArguments();
			return (Class<?>) args[0];
		}
		return getGenericType(clazz.getSuperclass());
	}
	
	@Override
	public boolean supports(E e) {
		Class<?> type = getGenericType(getClass());
		return e != null
				&& type.isAssignableFrom(e.getClass())
				&& supports0(e);
	}
	
	public boolean supports0(E e) {
		return true;
	}
	
}
