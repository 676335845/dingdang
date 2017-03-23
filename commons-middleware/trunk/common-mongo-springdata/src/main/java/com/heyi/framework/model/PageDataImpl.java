package com.heyi.framework.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PageDataImpl<T> implements PageData<T>, Serializable {

	private static final long serialVersionUID = -8646782686576687305L;

	private final List<T> datas;
	private final Pageable pageable;

	public PageDataImpl(List<T> datas, Pageable pageable) {

		if (null == datas) {
			throw new IllegalArgumentException("Content must not be null!");
		}

		this.datas = datas;
		this.pageable = pageable;
	}

	public int getPageNo() {
		return pageable == null ? 0 : pageable.getPageNo();
	}

	@Override
	public int getPageSize() {
		return pageable == null ? 0 : pageable.getPageSize();
	}

	@Override
	public int getTotalPages() {
		return getPageSize() == 0 ? 0 : (int) Math.ceil((double) this.getTotalCount()
				/ (double) getPageSize());
	}

	@Override
	public int getReturnCount() {
		return datas.size();
	}

	@Override
	public long getTotalCount() {
		return pageable == null ? this.datas.size() : pageable.getTotalCount();
	}

	public boolean hasPreviousPage() {
		return getPageNo() > 0;
	}

	public boolean isFirstPage() {
		return !hasPreviousPage();
	}

	public boolean hasNextPage() {
		return (getPageNo() + 1) * getPageSize() < this.getTotalCount();
	}

	public boolean isLastPage() {
		return !hasNextPage();
	}

	public Iterator<T> iterator() {
		return datas.iterator();
	}

	public List<T> getDatas() {
		return Collections.unmodifiableList(datas);
	}

	public boolean hasDatas() {
		return !datas.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String contentType = "UNKNOWN";

		if (datas.size() > 0) {
			contentType = datas.get(0).getClass().getName();
		}

		return String.format("Page %s of %d containing %s instances",
				getPageNo(), getTotalPages(), contentType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageDataImpl<?>)) {
			return false;
		}

		PageDataImpl<?> that = (PageDataImpl<?>) obj;

		boolean totalEqual = this.getTotalCount() == that.getTotalCount();
		boolean contentEqual = this.datas.equals(that.datas);
		boolean pageableEqual = this.pageable == null ? that.pageable == null
				: this.pageable.equals(that.pageable);

		return totalEqual && contentEqual && pageableEqual;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;
		long total = this.getTotalCount();
		result = 31 * result + (int) (total ^ total >>> 32);
		result = 31 * result + (pageable == null ? 0 : pageable.hashCode());
		result = 31 * result + datas.hashCode();

		return result;
	}

	public Pageable getPageable() {
		return pageable;
	}
}
