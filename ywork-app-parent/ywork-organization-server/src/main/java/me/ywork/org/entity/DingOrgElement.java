package me.ywork.org.entity;

import java.util.Date;

import me.ywork.base.entity.Entity;
import me.ywork.org.constants.DingOrgConstant;

/**
 * 钉钉组织架构元素
 * 
 * @author
 * @version 1.0 2015-07-27
 */
public class DingOrgElement extends Entity implements DingOrgConstant{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8363618286263758204L;

	/**
	 * 名称
	 */
	protected String fdName;
	
	/**
	 * 排序号
	 */
	protected Integer fdOrder;
	
	/**
	 * 企业id
	 */
	protected String fdOrgid;
	
	protected String fdDingid;
	
	/**
	 * 组织架构类型
	 */
	protected Integer fdOrgType;
	
	/**
	 * 编号 jobNo
	 */
	protected String fdNo;
	
	
	/**
	 * 是否可用激活状态
	 */
	protected Boolean fdIsAvailable;
	
	/**
	 * 是否当此同步
	 */
	protected Boolean fdIsSync;
	
	/**
	 * 外部导入关键字
	 */
	protected String fdImportInfo;
	
	/**
	 * 是否废弃
	 */
	protected Boolean fdIsAbandon = new Boolean(false);
	
	/**
	 * 标记为删除
	 */
	protected Boolean fdFlagDeleted;
	
	/**
	 * 备注
	 */
	protected String fdMemo;
	

	/**
	 * 层级ID
	 */
	protected String fdHierarchyId;
	
	/**
	 * 创建时间
	 */
	protected Date fdCreateTime = new Date();
	
	/**
	 * 修改时间
	 */
	protected Date fdAlterTime = new Date();
	
	/**
	 * 父级
	 */
	protected String fdParentid;
	
	
	/**
	 * @return 是否可用
	 */
	public Boolean getFdIsAvailable() {
		if (fdIsAvailable == null)
			fdIsAvailable = Boolean.TRUE;
		return fdIsAvailable;
	}
	/**
	 * @param fdIsAvailable 是否可用
	 */
	public void setFdIsAvailable(Boolean fdIsAvailable) {
		this.fdIsAvailable = fdIsAvailable;
	}
	
	
	public Boolean getFdIsSync() {
		return fdIsSync;
	}
	public void setFdIsSync(Boolean fdIsSync) {
		this.fdIsSync = fdIsSync;
	}
	
	/**
	 * @return 外部导入关键字
	 */
	public String getFdImportInfo() {
		return fdImportInfo;
	}
	
	/**
	 * @param fdImportInfo 外部导入关键字
	 */
	public void setFdImportInfo(String fdImportInfo) {
		this.fdImportInfo = fdImportInfo;
	}
	
	
	
	/**
	 * @return 标记为删除
	 */
	public Boolean getFdFlagDeleted() {
		return fdFlagDeleted;
	}
	
	/**
	 * @param fdFlagDeleted 标记为删除
	 */
	public void setFdFlagDeleted(Boolean fdFlagDeleted) {
		this.fdFlagDeleted = fdFlagDeleted;
	}
	
	
	
	/**
	 * @return 备注
	 */
	public String getFdMemo() {
		return fdMemo;
	}
	
	/**
	 * @param fdMemo 备注
	 */
	public void setFdMemo(String fdMemo) {
		this.fdMemo = fdMemo;
	}
	
	
	
	/**
	 * @return 层级ID
	 */
	public String getFdHierarchyId() {
		return fdHierarchyId;
	}
	
	/**
	 * @param fdHierarchyId 层级ID
	 */
	public void setFdHierarchyId(String fdHierarchyId) {
		this.fdHierarchyId = fdHierarchyId;
	}
	
	
	
	/**
	 * @return 创建时间
	 */
	public Date getFdCreateTime() {
		return fdCreateTime;
	}
	
	/**
	 * @param fdCreateTime 创建时间
	 */
	public void setFdCreateTime(Date fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}
	
	
	
	/**
	 * @return 修改时间
	 */
	public Date getFdAlterTime() {
		return fdAlterTime;
	}
	
	/**
	 * @param fdAlterTime 修改时间
	 */
	public void setFdAlterTime(Date fdAlterTime) {
		this.fdAlterTime = fdAlterTime;
	}
	
	
	
	
	
	
	/**
	 * @return 父级
	 */
	public String getFdParentid() {
		return fdParentid;
	}
	
	/**
	 * @param fdParentid 父级
	 */
	public void setFdParentid(String fdParentid) {
		this.fdParentid = fdParentid;
	}
	

	


	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public Integer getFdOrder() {
		return fdOrder;
	}

	public void setFdOrder(Integer fdOrder) {
		this.fdOrder = fdOrder;
	}

	public String getFdOrgid() {
		return fdOrgid;
	}

	public void setFdOrgid(String fdOrgid) {
		this.fdOrgid = fdOrgid;
	}

	public String getFdDingid() {
		return fdDingid;
	}

	public void setFdDingid(String fdDingid) {
		this.fdDingid = fdDingid;
	}

	public Integer getFdOrgType() {
		return fdOrgType;
	}

	public void setFdOrgType(Integer fdOrgType) {
		this.fdOrgType = fdOrgType;
	}

	public String getFdNo() {
		return fdNo;
	}

	public void setFdNo(String fdNo) {
		this.fdNo = fdNo;
	}

	public Boolean getFdIsAbandon() {
		return fdIsAbandon;
	}

	public void setFdIsAbandon(Boolean fdIsAbandon) {
		this.fdIsAbandon = fdIsAbandon;
	}


}
