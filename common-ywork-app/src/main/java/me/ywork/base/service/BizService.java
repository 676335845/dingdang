package me.ywork.base.service;

import me.ywork.context.CallContext;

/**
 * 业务接口基类，封装通用接口
 * 
 * @author TangGang 2015年7月16日
 * 
 */
public interface BizService {

	// public <S extends T> S save(CallContext callContext, S model);

	// public T findById(CallContext callContext, String id);

	public boolean deleteById(CallContext callContext, String id);

}
