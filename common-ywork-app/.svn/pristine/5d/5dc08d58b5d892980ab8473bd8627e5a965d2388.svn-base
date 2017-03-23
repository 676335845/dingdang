package me.ywork.result;

import java.io.Serializable;


/**
 * pc端调用返回管理员
 * 
 * @author TangGang   2015-6-17
 *
 */
public class PcAdmin implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -664179579843861148L;

	private Integer errcode;	
	
	private String errmsg;
	
	private String email;
	
	private String name;
	
	private String userid;
	
	private String avatar;
	
	private String corpid;
	
	private String corpName;
	
	

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public PcAdmin() {
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	
	public void setErrcode(ResultStatus resultStatus) {
		if (resultStatus == null) {
			throw new NullPointerException("setErrcode - parameter resultStatus is null.");
		}
		
		this.setErrcode(resultStatus.getCode());
		this.setErrmsg(resultStatus.getDefaultLabel());
	}
}
