package me.ywork.salary.model;

import java.io.Serializable;
import java.util.List;

public class SalInfoExcelModel  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2318800553670918253L;
	/**
	 * 成功的数目 通过excel上传时上传成功的数目
	 */
	private Integer successCount;
	/**
	 * 失败的数目 通过excel上传失败的数目
	 */
	private Integer failCount;
	
	/**
	 * 缓存的key
	 */
	private String cacheKey;
	
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
	private List<SalStaffBaseInfoModel> salInfos;
	
	
	public SalInfoExcelModel() {
		super();
	}
	public List<String> getDetailTitle() {
		return detailTitle;
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
	public List<SalStaffBaseInfoModel> getSalInfos() {
		return salInfos;
	}
	public void setSalInfos(List<SalStaffBaseInfoModel> salInfos) {
		this.salInfos = salInfos;
	}
	public void setDetailTitle(List<String> detailTitle) {
		this.detailTitle = detailTitle;
	}
    

}
