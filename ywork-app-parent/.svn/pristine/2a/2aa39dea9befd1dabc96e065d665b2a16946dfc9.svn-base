package me.ywork.salarybill.model;


import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 缓存上传的手机信息
 */
public class CacheSalaryMobileModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1024478218095572100L;

	//缓存key
	private String cacheKey;
	
	private String fileKey;
	
	//通知
	private String tips ;
	
	//成功数据
	private List<SalaryBillMobileModel> successSalaryMobiles;
	//错误数据
	private List<SalaryBillMobileModel> errorSalaryMobiles;
	
	//分页显示
	private List<SalaryBillMobileModel> salaryMobiles;
	
	private Boolean hasNext;
	
	private Integer successCount;
	
	private Integer errorCount;
	
	private Map<Short,String> templateType;

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public List<SalaryBillMobileModel> getSuccessSalaryMobiles() {
		return successSalaryMobiles;
	}

	public void setSuccessSalaryMobiles(List<SalaryBillMobileModel> successSalaryMobiles) {
		this.successSalaryMobiles = successSalaryMobiles;
	}

	public List<SalaryBillMobileModel> getErrorSalaryMobiles() {
		return errorSalaryMobiles;
	}

	public void setErrorSalaryMobiles(List<SalaryBillMobileModel> errorSalaryMobiles) {
		this.errorSalaryMobiles = errorSalaryMobiles;
	}

	public List<SalaryBillMobileModel> getSalaryMobiles() {
		return salaryMobiles;
	}

	public void setSalaryMobiles(List<SalaryBillMobileModel> salaryMobiles) {
		this.salaryMobiles = salaryMobiles;
	}

	public Boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Map<Short, String> getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Map<Short, String> templateType) {
		this.templateType = templateType;
	}

}
