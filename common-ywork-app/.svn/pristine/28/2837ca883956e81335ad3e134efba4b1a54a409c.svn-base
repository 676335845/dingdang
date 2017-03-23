package me.ywork.result;

import java.io.Serializable;


/**
 * 移动端调用返回结果类
 * 
 * @author TangGang   2015-6-17
 *
 */
public class JsonResult<T> implements Serializable {
	private static final long serialVersionUID = -3458066472643731732L;

	/**
	 * 结果状态码
	 */
	private Integer errcode;	
	
	/**
	 * 相关消息
	 */
	private String errmsg;
	
	/**
	 * 返回到移动端的数据对象
	 */
	private T data;

	protected JsonResult() {
		super();
		
	}
	
	/**
	 * @return 返回成功的结果对象
	 */
	public static <E> JsonResult<E> getSuccessJsonResult(E data) {
		JsonResult<E> result = new JsonResult<E>();
		
		result.setErrcode(ResultStatus.SUCCESS);
		
		result.setData(data);
		
		return result;
	}
	
	/**
	 * @return 返回权限不足的结果对象
	 */
	public static <E> JsonResult<E> getPermissionDeniedJsonResult(E data) {
		JsonResult<E> result = new JsonResult<E>();
		
		result.setErrcode(ResultStatus.PermissionDenied);
		
		result.setData(data);
		
		return result;
	}
	
	/**
	 * @return 返回失败的结果对象
	 */
	public static <E> JsonResult<E> getFailJsonResult(E data) {
		JsonResult<E> result = new JsonResult<E>();
		
		result.setErrcode(ResultStatus.FAIL);
		
		result.setData(data);
		
		return result;
	}
	
	/**
	 * @return 返回依赖的结果对象
	 */
	public static <E> JsonResult<E> getDependJsonResult(E data) {
		JsonResult<E> result = new JsonResult<E>();
		
		result.setErrcode(ResultStatus.DEPENDCE);
		
		result.setData(data);
		
		return result;
	}


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	
	public void setErrcode(ResultStatus resultStatus) {
		if (resultStatus == null) {
			throw new NullPointerException("setErrcode - parameter resultStatus is null.");
		}
		
		this.setErrcode(resultStatus.getCode());
		this.setErrmsg(resultStatus.getDefaultLabel());
	}
}
