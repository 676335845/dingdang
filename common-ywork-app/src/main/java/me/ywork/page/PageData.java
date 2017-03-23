package me.ywork.page;

import java.util.Iterator;
import java.util.List;

public interface PageData<T> extends Iterable<T> {

	/**
	 * @return  当前请求的页次
	 */
	int getPageNo();

	/**
	 * @return 每页请求的数据量
	 */
	int getPageSize();

	/**
	 * @return 总页数
	 */
	int getTotalPages();

	/**
	 * @return 当前请求返回的数据行数
	 */
	int getReturnCount();

	/**
	 * @return 总数据行数
	 */
	long getTotalCount();

	boolean hasPreviousPage();

	boolean isFirstPage();

	boolean hasNextPage();

	boolean isLastPage();

	Iterator<T> iterator();

	List<T> getDatas();

	boolean hasDatas();

	public Pageable getPageable();
}
