package me.ywork.salary.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by xiaobai on 2017/1/11.
 */
public class SalStaffBaseInfoModel implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -507439951375652758L;

	/**
	 * 员工的id
	 */
	private String id;
	
	/**
	 *  员工的名字
	 */
	@JSONField(name = "name")
	private String staffName;
	
	/**
	 * 部门的ID
	 */
	private String deptId;
	
	/**
	 * 部门的名字
	 */
	@JSONField(name ="deptName")
	private String deptName;
	
	/**
	 * 员工所属企业的Id
	 */
	private String corpId;
	
	/**
	 *  员工薪资规则
	 */
	@JSONField(name = "cbRuleId")
	private String corpBaseRuleId;
	
	/**
	 *  员工钉钉ID
	 */
	@JSONField(name = "staffId")
	private String dingStaffId;
	
	/**
	 *  员工叮当薪资密码
	 */
	@JSONField(name = "password")
	private String staffPass;
	
	/**
	 *  社保公积金是否参与
	 */
	private Integer attenSocial;
	
	/**
	 * 个人所得税是否参与
	 */
	private Integer attenPersonalTax;
	
	/**
	 *  应发工资
	 */
	@JSONField(name = "allSal")
	private Double shouldPaySal;
	
	/**
      * 创建时间
	 */
	private Date createDate;
	
	/**
	 *   更新时间
	 */
    private Date modifiedDate;
    
    /**
     *  （删除和添加操作时）人员的薪资规则的类型：0是基本的薪资规则，1是参加社保的规则，2是缴纳个人所得税的规则
     */
    private  Short salRuleHandleType;
    
    /**
     * 员工钉钉号集合
     */
    private List<String> staffIds;
    
    /**
     * 员工头像地址
     */
    @JSONField(name = "avatar")
    private String headUrl;
    
    /**
     * 正算或反算
     */
    @JSONField(name = "salRule")
    private Short salRuleType;
    
    /**
     *  确定ID是部门或人员的分辨器
     */
    private Short type;
    
    /**
     *  员工密码开启的状态
     */
    @JSONField(name ="status")
    private Short passState;
    /*
     * 是否设置了密码
     */
   private Boolean havePwd;
   /**
    * 是否需要输入密码
    */
	private Boolean needPwd;
    
	/**
	 * 系统是否开启密码
	 */
	private Boolean sysPassState;
	
	/**
	 * 是否是管理员
	 */
	private Boolean isAdmin;
	
	/**
	 * 工号
	 */
	@JSONField(name="workId")
	private String jobNum;
	
	/**
	 * 上传失败的原因
	 */
	private String failReason;
	
	/**
	 * 父类型的ID:用于在更新选中人员的部门ID
	 */
	private String parentId;
	
	
	/**
	 * 员工薪资字段集合
	 */
	private List<SalSysFieldItemModel> salFields;
	
	/**
	 * 行号：在上传企业的薪资模板的时候用到
	 */
	private Integer rowNum;
	
	public SalStaffBaseInfoModel() {
		super();
	}

  

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	
	public String getDingStaffId() {
		return dingStaffId;
	}

	public void setDingStaffId(String dingStaffId) {
		this.dingStaffId = dingStaffId;
	}

	public String getStaffPass() {
		return staffPass;
	}

	public void setStaffPass(String staffPass) {
		this.staffPass = staffPass;
	}

	public Integer getAttenSocial() {
		return attenSocial;
	}

	public void setAttenSocial(Integer attenSocial) {
		this.attenSocial = attenSocial;
	}

	public Integer getAttenPersonalTax() {
		return attenPersonalTax;
	}

	public void setAttenPersonalTax(Integer attenPersonalTax) {
		this.attenPersonalTax = attenPersonalTax;
	}

	




	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<String> getStaffIds() {
		return staffIds;
	}

	public void setStaffIds(List<String> staffIds) {
		this.staffIds = staffIds;
	}

	public Short getSalRuleHandleType() {
		return salRuleHandleType;
	}

	public void setSalRuleHandleType(Short salRuleHandleType) {
		this.salRuleHandleType = salRuleHandleType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
  
   

	public String getCorpBaseRuleId() {
		return corpBaseRuleId;
	}



	public void setCorpBaseRuleId(String corpBaseRuleId) {
		this.corpBaseRuleId = corpBaseRuleId;
	}



	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}



	public Short getSalRuleType() {
		return salRuleType;
	}



	public void setSalRuleType(Short salRuleType) {
		this.salRuleType = salRuleType;
	}



	public String getDeptId() {
		return deptId;
	}



	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}



	public Short getType() {
		return type;
	}



	public void setType(Short type) {
		this.type = type;
	}



	public Short getPassState() {
		return passState;
	}

	public void setPassState(Short passState) {
		this.passState = passState;
	}



	public Boolean getHavePwd() {
		return havePwd;
	}



	public void setHavePwd(Boolean havePwd) {
		this.havePwd = havePwd;
	}



	public Boolean getNeedPwd() {
		return needPwd;
	}



	public void setNeedPwd(Boolean needPwd) {
		this.needPwd = needPwd;
	}



	public Boolean getSysPassState() {
		return sysPassState;
	}



	public void setSysPassState(Boolean sysPassState) {
		this.sysPassState = sysPassState;
	}



	public Boolean getIsAdmin() {
		return isAdmin;
	}



	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}



	public List<SalSysFieldItemModel> getSalFields() {
		return salFields;
	}



	public void setSalFields(List<SalSysFieldItemModel> salFields) {
		this.salFields = salFields;
	}



	public String getJobNum() {
		return jobNum;
	}



	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}



	public String getFailReason() {
		return failReason;
	}



	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}



	public Double getShouldPaySal() {
		return shouldPaySal;
	}



	public void setShouldPaySal(Double shouldPaySal) {
		this.shouldPaySal = shouldPaySal;
	}



	public String getParentId() {
		return parentId;
	}



	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	public Integer getRowNum() {
		return rowNum;
	}



	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}
	
	
	
	
	
	
}
