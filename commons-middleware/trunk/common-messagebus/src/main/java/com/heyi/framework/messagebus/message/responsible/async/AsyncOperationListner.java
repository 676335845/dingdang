package com.heyi.framework.messagebus.message.responsible.async;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 
 * @author sulta
 *
 * @param <V>
 */
public abstract class AsyncOperationListner<V>  implements GenericFutureListener<Future<V>>{
	
	public abstract void onSuccess(V result) throws Exception;

	public abstract void onFailure(Throwable cause) throws Exception;

	@Override
	public void operationComplete(Future<V> future) throws Exception {
		if(! future.isSuccess()){
			onFailure(future.cause());
		}else{
			onSuccess(future.getNow());
		}
	}	
}
