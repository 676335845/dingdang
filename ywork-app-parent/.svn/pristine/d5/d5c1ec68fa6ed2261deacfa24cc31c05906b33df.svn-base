package me.ywork.org.entity;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import me.ywork.base.entity.Entity;
import me.ywork.org.api.model.DingOrgCorpVo;

/**
 * 钉钉公司
 * 
 * @author
 * @version 1.0 2015-07-27
 */
public class DingOrgCorp extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8531918248442360248L;

	/**
	 * 企业名称
	 */
	protected String fdCorpName;
	
	/**
	 * 企业AppKey
	 */
	protected String fdAppkey;
	
	/**
	 * 是否废弃
	 */
	protected Boolean fdIsAbandon = new Boolean(false);
	
	/**
	 * 图片ID
	 */
	protected String fdPicurl;
	
	/**
	 * 最后同步org时间
	 */
	protected Date fdLastSyncTime;

	
		
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

	public Boolean getFdIsAbandon() {
		return fdIsAbandon;
	}

	public void setFdIsAbandon(Boolean fdIsAbandon) {
		this.fdIsAbandon = fdIsAbandon;
	}

	public String getFdPicurl() {
		return fdPicurl;
	}

	public void setFdPicurl(String fdPicurl) {
		this.fdPicurl = fdPicurl;
	}

	public Date getFdLastSyncTime() {
		return fdLastSyncTime;
	}

	public void setFdLastSyncTime(Date fdLastSyncTime) {
		this.fdLastSyncTime = fdLastSyncTime;
	}

	public static DingOrgCorpVo toVo(DingOrgCorp model){
		if(model==null) return null;
		DingOrgCorpVo vo = new DingOrgCorpVo();
		BeanUtils.copyProperties(model, vo);
		return vo;
	}
}
