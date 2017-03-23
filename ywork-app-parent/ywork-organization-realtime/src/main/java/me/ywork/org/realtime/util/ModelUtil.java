package me.ywork.org.realtime.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class ModelUtil {
	
	/**
	 * 判断一个对象是否被Hibernate的延时加载机制给修改，并返回正确的类名
	 * 
	 * @param mainModel
	 * @return
	 */
	public static String getModelClassName(Object mainModel) {
		String rtnVal = mainModel.getClass().getName();
		int i = rtnVal.indexOf('$');
		if (i > -1)
			rtnVal = rtnVal.substring(0, i);
		return rtnVal;
	}

	/**
	 * 返回HQL语句中用于查询使用的表名，如：sysOrgElement
	 * 
	 * @param mainModel
	 * @return
	 */
	public static String getModelTableName(Object mainModel) {
		String rtnVal = mainModel instanceof String ? (String) mainModel
				: getModelClassName(mainModel);
		int i = rtnVal.lastIndexOf('.');
		if (i > -1)
			rtnVal = rtnVal.substring(i + 1);
		return rtnVal.substring(0, 1).toLowerCase() + rtnVal.substring(1);
	}

	public static void checkTreeCycle(Object treeModel, Object parent,
			String parentProperty)   {
		List parentList = new ArrayList();
		parentList.add(treeModel);
		try {
			for (Object curNode = parent; curNode != null; curNode = PropertyUtils
					.getProperty(curNode, parentProperty)) {
				if (parentList.contains(curNode)) {
					throw new RuntimeException();
				}
				parentList.add(curNode);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 对象克隆
	 * 
	 * @param object
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 */
	public static Object clone(Object object) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			InvocationTargetException {
		String clsName = getModelClassName(object);
		Object newObj = Class.forName(clsName).newInstance();
		BeanUtils.copyProperties(newObj, object);
		return newObj;
	}
}
