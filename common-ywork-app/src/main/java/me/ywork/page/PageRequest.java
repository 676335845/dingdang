package me.ywork.page;

import java.io.Serializable;

public class PageRequest implements Pageable, Serializable {

	private static final long serialVersionUID = -5390518339377444510L;

	private int pageNo;
	private int pageSize;
	private long totalCount;

	public PageRequest() {

	}

	public PageRequest(int pageNo, int pageSize, long totalCount) {
		if (0 > pageNo) {
			throw new IllegalArgumentException(
					"Page index must not be less than zero!");
		}

		if (0 >= pageSize) {
			throw new IllegalArgumentException(
					"Page size must not be less than or equal to zero!");
		}
		
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
	}


	public int getPageSize() {

		return pageSize;
	}

	@Override
	public int getPageNo() {

		return pageNo;
	}

/*	public int getOffset() {
		if (pageNo == 1) {
			return 0;
		}

		return (pageNo - 1) * pageSize;
	}*/

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageRequest)) {
			return false;
		}

		PageRequest that = (PageRequest) obj;

		boolean pageEqual = this.pageNo == that.pageNo;
		boolean sizeEqual = this.pageSize == that.pageSize;

		return pageEqual && sizeEqual;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;

		result = 31 * result + pageNo;
		result = 31 * result + pageSize;

		return result;
	}


	public long getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

}
