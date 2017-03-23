package me.ywork.org.api.model;

import java.io.Serializable;


/**
 * 第三方授权信息
 *
 */
public class DingSuiteThirdVo implements Serializable  {
	public DingSuiteThirdVo() {
	}
	
	private String id ;

	/**
	 * 授权方企业号名称
	 */
	protected String fdCorpName;

	/**
	 * @return 授权方企业号名称
	 */
	public String getFdCorpName() {
		return fdCorpName;
	}

	/**
	 * @param fdCorpName 授权方企业号名称
	 */
	public void setFdCorpName(String fdCorpName) {
		this.fdCorpName = fdCorpName;
	}

	/**
	 * 授权方企业号id
	 */
	protected String fdCorpId;

	/**
	 * @return 授权方企业号id
	 */
	public String getFdCorpId() {
		return fdCorpId;
	}

	/**
	 * @param fdCorpId 授权方企业号id
	 */
	public void setFdCorpId(String fdCorpId) {
		this.fdCorpId = fdCorpId;
	}

	/**
	 * 企业号永久授权码
	 */
	protected String fdPermanentCode;

	/**
	 * @return 企业号永久授权码
	 */
	public String getFdPermanentCode() {
		return fdPermanentCode;
	}

	/**
	 * @param fdPermanentCode 企业号永久授权码
	 */
	public void setFdPermanentCode(String fdPermanentCode) {
		this.fdPermanentCode = fdPermanentCode;
	}

	/**
	 * 授权方企业号类型
	 */
	protected String fdCorpType;

	/**
	 * @return 授权方企业号类型
	 */
	public String getFdCorpType() {
		return fdCorpType;
	}

	/**
	 * @param fdCorpType 授权方企业号类型
	 */
	public void setFdCorpType(String fdCorpType) {
		this.fdCorpType = fdCorpType;
	}

	/**
	 * 授权方企业号方形头像
	 */
	protected String fdCorpSquareLogoUrl;

	/**
	 * @return 授权方企业号方形头像
	 */
	public String getFdCorpSquareLogoUrl() {
		return fdCorpSquareLogoUrl;
	}

	/**
	 * @param fdCorpSquareLogoUrl 授权方企业号方形头像
	 */
	public void setFdCorpSquareLogoUrl(String fdCorpSquareLogoUrl) {
		this.fdCorpSquareLogoUrl = fdCorpSquareLogoUrl;
	}

	/**
	 * 所属套件
	 */
	protected String fdSuiteId;

	public String getFdSuiteId() {
		return fdSuiteId;
	}

	public void setFdSuiteId(String fdSuiteId) {
		this.fdSuiteId = fdSuiteId;
	}

	/**
	 * 阿里云套件实例ID
	 */
	protected String fdInstanceId;

	public String getFdInstanceId() {
		return fdInstanceId;
	}

	public void setFdInstanceId(String fdInstanceId) {
		this.fdInstanceId = fdInstanceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}