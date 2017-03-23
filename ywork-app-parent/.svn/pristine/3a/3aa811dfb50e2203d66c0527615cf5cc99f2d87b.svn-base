package me.ywork.suite.entity;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import me.ywork.base.entity.Entity;
import me.ywork.org.api.model.DingSuiteThirdVo;


/**
 * 第三方授权信息
 * 
 * @author
 * @version 1.0 2015-07-27
 */
public class DingSuiteThirdMain extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3430732612509470468L;
	
	/**
	 * 所属套件
	 */
	protected String suiteId;
	
	/**
	 * 授权方企业号id
	 */
	protected String corpId;
	
	/**
	 * 企业号永久授权码
	 */
	protected String permanentCode;
	
	/**
	 * 授权方企业号名称
	 */
	protected String corpName;
	
	/**
	 * 所属行业
	 */
	protected String industry;

	
	/**
	 * 邀请码
	 */
	protected String inviteCode;
	
	
	/**
	 * 序列号
	 */
	protected String licenseCode;
	
	/**
	 * 渠道码
	 */
	protected String authChannel;
	
	/**
	 * 企业是否认证
	 */
	protected String isAuthenticated;
	
	/**
	 * 企业认证等级
	 */
	protected String authLevel;
	
	/**
	 * 企业邀请链接
	 */
	protected String inviteUrl;
	
	/**
	 * 企业图像
	 */
	protected String logoUrl;

	/**
	 * 是否启用,授权标识。未安装或删除（0），已安装（1），停用（2)
	 */
	protected short enabled;

	public String getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(String suiteId) {
		this.suiteId = suiteId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getPermanentCode() {
		return permanentCode;
	}

	public void setPermanentCode(String permanentCode) {
		this.permanentCode = permanentCode;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	public String getAuthChannel() {
		return authChannel;
	}

	public void setAuthChannel(String authChannel) {
		this.authChannel = authChannel;
	}

	public String getIsAuthenticated() {
		return isAuthenticated;
	}

	public void setIsAuthenticated(String isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public String getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(String authLevel) {
		this.authLevel = authLevel;
	}

	public String getInviteUrl() {
		return inviteUrl;
	}

	public void setInviteUrl(String inviteUrl) {
		this.inviteUrl = inviteUrl;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public short getEnabled() {
		return enabled;
	}

	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}


	public static DingSuiteThirdVo toVo(DingSuiteThirdMain model){
		if(model==null) return null;
		DingSuiteThirdVo vo = new DingSuiteThirdVo();
		try {
			BeanUtils.copyProperties(model, vo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return vo;
	}

}
