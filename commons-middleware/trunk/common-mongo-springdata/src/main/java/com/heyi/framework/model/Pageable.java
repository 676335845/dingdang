package com.heyi.framework.model;

public interface Pageable {

	/**
	 * @return  当前页次, 如果请求页次小于等于 第一页则需要重新计算页次
	 */
	public int getPageNo();

	/**
	 * @return 每页的数据量
	 */
	public int getPageSize();
	
	/**
	 * @return 如果请求的大于第一页的数据，则必须提供第一次请求的数据量
	 */
	public long getTotalCount();
	
	/**
	 * 设置满足条件的数据总量
	 * @param totalCount 数据行数
	 */
	public void setTotalCount(long totalCount);
}
