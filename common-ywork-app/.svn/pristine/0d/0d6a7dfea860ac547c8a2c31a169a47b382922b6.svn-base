package me.ywork.base.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据实体映射抽象基类<br/>
 * 主要为了兼容从第三方加入的应用，其映射主键类型不确定
 * 
 * @author TangGang 2015年8月9日
 * 
 * @param <PK>
 *            主键类型，只是基础类型的封装类型
 */
public abstract class AbstractEntity<PK> implements Serializable {
	private static final long serialVersionUID = 402635528276100911L;

	/**
	 * 数据主键
	 */
	private PK id;

	/**
	 * 实体创建时间
	 */
	private Date createDate;
	public static final String CREATEDATE = "createDate";
	public static final String COL_CREATEDATE = "create_time";
	/**
	 * 实体最后被修改的时间
	 */
	private Date modifiedDate;
	public static final String MODIFIEDDATE = "modifiedDate";
	public static final String COL_MODIFIEDDATE = "modified_time";
	
	/**
	 * 只用于更新操作
	 */
	private boolean updateOnly;

	public AbstractEntity() {
		Date now = new Date();
		this.createDate = now;
		this.modifiedDate = now;
	}

	public PK getId() {
		return id;
	}

	public void setId(PK id) {
		this.id = id;
	}

	public boolean isNew() {
		return id == null;
	}

	public boolean isUpdateOnly() {
		return updateOnly;
	}

	public void setUpdateOnly(boolean updateOnly) {
		this.updateOnly = updateOnly;
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
}
