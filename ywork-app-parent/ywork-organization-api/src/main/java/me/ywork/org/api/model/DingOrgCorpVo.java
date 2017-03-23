package me.ywork.org.api.model;

import java.io.Serializable;

public class DingOrgCorpVo implements Serializable {
	
	public DingOrgCorpVo() {
	}
	
	protected String id;

	/**
	 * 企业名称
	 */
	protected String fdCorpName;
	
	/**
	 * 企业AppKey
	 */
	protected String fdAppkey;
	
	/**
	 * 图片ID
	 */
	protected String fdPicurl;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFdCorpName() {
		return fdCorpName;
	}

	public void setFdCorpName(String fdCorpName) {
		this.fdCorpName = fdCorpName;
	}

	public String getFdAppkey() {
		return fdAppkey;
	}

	public void setFdAppkey(String fdAppkey) {
		this.fdAppkey = fdAppkey;
	}

	public String getFdPicurl() {
		return fdPicurl;
	}

	public void setFdPicurl(String fdPicurl) {
		this.fdPicurl = fdPicurl;
	}
	
}
