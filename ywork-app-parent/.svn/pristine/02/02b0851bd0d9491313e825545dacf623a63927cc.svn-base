package me.ywork.salary.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SalAttenExcelModel implements Serializable{
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/**
		 * 成功的数目 通过excel上传时上传成功的数目
		 */
		private Integer successCount;
		/**
		 * 失败的数目 通过excel上传失败的数目
		 */
		private Integer failCount;
		
		/**
		 * 若存在考勤报表，存储考勤报表的ID 
		 */
		private String reportId;
		/**
		 * 缓存的key
		 */
		private String cacheKey;
		
		/**
		 *  考勤报表的月份
		 */
		private Date monthTime;
		/**
		 * 是否存在该月的考勤数据的标志器
		 */
		private Boolean isExist;
		
		/**
		 * 总数
		 */
		private Integer totalCount;
		/**
		 * 分页数
		 */
		private Integer pageNo;
		/**
		 * 页面数量
		 */
		private Integer pageSize;
		/**
		 * 总页数
		 */
		private Integer totalPages;
		private List<String> detailTitle;;
		private 	List<SalStaffAttendanceModel> staffAttens;
		public List<String> getDetailTitle() {
			return detailTitle;
		}
		public void setDetailTitle(List<String> detailTitle) {
			this.detailTitle = detailTitle;
		}
		public List<SalStaffAttendanceModel> getStaffAttens() {
			return staffAttens;
		}
		public void setStaffAttens(List<SalStaffAttendanceModel> staffAttens) {
			this.staffAttens = staffAttens;
		}
		public Integer getSuccessCount() {
			return successCount;
		}
		public void setSuccessCount(Integer successCount) {
			this.successCount = successCount;
		}
		public Integer getFailCount() {
			return failCount;
		}
		public void setFailCount(Integer failCount) {
			this.failCount = failCount;
		}
         
        
		public String getCacheKey() {
			return cacheKey;
		}
		public void setCacheKey(String cacheKey) {
			this.cacheKey = cacheKey;
		}
		public Integer getTotalCount() {
			return totalCount;
		}
		public void setTotalCount(Integer totalCount) {
			this.totalCount = totalCount;
		}
		public Integer getPageNo() {
			return pageNo;
		}
		public void setPageNo(Integer pageNo) {
			this.pageNo = pageNo;
		}
		public Integer getPageSize() {
			return pageSize;
		}
		public void setPageSize(Integer pageSize) {
			this.pageSize = pageSize;
		}
		public Integer getTotalPages() {
			return totalPages;
		}
		public void setTotalPages(Integer totalPages) {
			this.totalPages = totalPages;
		}
		public Date getMonthTime() {
			return monthTime;
		}
		public void setMonthTime(Date monthTime) {
			this.monthTime = monthTime;
		}
		public Boolean getIsExist() {
			return isExist;
		}
		public void setIsExist(Boolean isExist) {
			this.isExist = isExist;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public String getReportId() {
			return reportId;
		}
		public void setReportId(String reportId) {
			this.reportId = reportId;
		}
		
}
