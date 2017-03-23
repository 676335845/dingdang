package me.ywork.salarybill.model;


import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 缓存上传的通知信息
 */
public class CacheSalaryModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1024478218095572100L;

	//缓存key
	private String cacheKey;
	
	private String fileKey;
	
	private String salaryMonth;
	private String salrayType;
	
	//通知
	private String tips ;
	//主title
	private List<String> mainTitles;
	//明细title
	private List<String> detailTitles;
	//成功数据
	private List<SalaryBillModel> successSalaryBills;
	//错误数据
	private List<SalaryBillModel> errorSalaryBills;
	
	//分页显示
	private List<SalaryBillModel> salaryBills;
	
	private Boolean hasNext;
	
	private Integer successCount;
	
	private Integer errorCount;
	
	private Map<Short,String> templateType;
	
	
	public Map<Short, String> getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Map<Short, String> templateType) {
		this.templateType = templateType;
	}

	public List<SalaryBillModel> getSalaryBills() {
		return salaryBills;
	}

	public void setSalaryBills(List<SalaryBillModel> salaryBills) {
		this.salaryBills = salaryBills;
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

	public Boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public String getSalaryMonth() {
		return salaryMonth;
	}

	public void setSalaryMonth(String salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

	public String getSalrayType() {
		return salrayType;
	}

	public void setSalrayType(String salrayType) {
		this.salrayType = salrayType;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public List<String> getMainTitles() {
		return mainTitles;
	}

	public void setMainTitles(List<String> mainTitles) {
		this.mainTitles = mainTitles;
	}

	public List<String> getDetailTitles() {
		return detailTitles;
	}

	public void setDetailTitles(List<String> detailTitles) {
		this.detailTitles = detailTitles;
	}

	public List<SalaryBillModel> getSuccessSalaryBills() {
		return successSalaryBills;
	}

	public void setSuccessSalaryBills(List<SalaryBillModel> successSalaryBills) {
		this.successSalaryBills = successSalaryBills;
	}

	public List<SalaryBillModel> getErrorSalaryBills() {
		return errorSalaryBills;
	}

	public void setErrorSalaryBills(List<SalaryBillModel> errorSalaryBills) {
		this.errorSalaryBills = errorSalaryBills;
	}
	
	public CacheSalaryModel(){
		
	}

}
