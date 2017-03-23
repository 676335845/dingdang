package me.ywork.org.realtime.entity;

import org.springframework.beans.BeanUtils;

import me.ywork.org.api.model.DingOrgUserVo;

/**
 * 钉钉用户
 *
 * @author
 * @version 1.0 2015-07-27
 */
public class DingOrgUser extends DingOrgElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2447286674230617716L;

	public DingOrgUser() {
		super();
		setFdOrgType(new Integer(ORG_TYPE_PERSON));
	}

	/**
	 * 是否管理员
	 */
	protected Boolean fdIsAdmin;

	/**
	 * 是否老板
	 */
	protected Boolean fdIsBoss;

	/**
	 * 是否部门主管
	 */
	protected Boolean fdIsLeader;

	/**
	 * 图像URL
	 */
	protected String fdAvatar;

	/**
	 * 员工UserID
	 */
	protected String fdUserid;

	/**
	 * 职位
	 */
	protected String fdPosition;

	/**
	 * 手机
	 */
	protected String fdMobile;

	/**
	 * Email
	 */
	protected String fdEmail;

	public Boolean getFdIsAdmin() {
		return fdIsAdmin;
	}

	public void setFdIsAdmin(Boolean fdIsAdmin) {
		this.fdIsAdmin = fdIsAdmin;
	}

	public Boolean getFdIsBoss() {
		return fdIsBoss;
	}

	public void setFdIsBoss(Boolean fdIsBoss) {
		this.fdIsBoss = fdIsBoss;
	}

	public Boolean getFdIsLeader() {
		return fdIsLeader;
	}

	public void setFdIsLeader(Boolean fdIsLeader) {
		this.fdIsLeader = fdIsLeader;
	}

	public String getFdAvatar() {
		return fdAvatar;
	}

	public void setFdAvatar(String fdAvatar) {
		this.fdAvatar = fdAvatar;
	}

	public String getFdUserid() {
		return fdUserid;
	}

	public void setFdUserid(String fdUserid) {
		this.fdUserid = fdUserid;
	}

	public String getFdPosition() {
		return fdPosition;
	}

	public void setFdPosition(String fdPosition) {
		this.fdPosition = fdPosition;
	}

	public String getFdMobile() {
		return fdMobile;
	}

	public void setFdMobile(String fdMobile) {
		this.fdMobile = fdMobile;
	}

	public String getFdEmail() {
		return fdEmail;
	}

	public void setFdEmail(String fdEmail) {
		this.fdEmail = fdEmail;
	}

	public static DingOrgUserVo toVo(DingOrgUser model) {
		if (model == null)
			return null;
		DingOrgUserVo vo = new DingOrgUserVo();
		BeanUtils.copyProperties(model, vo);
		return vo;
	}
}
