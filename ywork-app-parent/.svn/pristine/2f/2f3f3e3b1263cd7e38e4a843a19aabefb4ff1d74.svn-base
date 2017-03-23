package me.ywork.org.realtime.model;

import org.apache.commons.lang.builder.HashCodeBuilder;

import me.ywork.org.realtime.util.ModelUtil;

public class DingSuiteThirdForSync {
	
	public DingSuiteThirdForSync() {
	}
	
	private String corpId;
	
	private String corpName;
	
	private String suiteId;
	
	private String permanentCode;
	
	private String logoUrl;
	

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(String suiteId) {
		this.suiteId = suiteId;
	}

	public String getPermanentCode() {
		return permanentCode;
	}

	public void setPermanentCode(String permanentCode) {
		this.permanentCode = permanentCode;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		
		if(this == obj)
			return true;
		
		if (obj instanceof DingSuiteThirdForSync) {
			DingSuiteThirdForSync anotherObj = (DingSuiteThirdForSync) obj;
			if(anotherObj.getCorpId().equals(this.getCorpId())){
				if(anotherObj.getSuiteId().equals(this.getSuiteId())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 覆盖hashCode方法，通过模型中类名和ID构建哈希值
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder rtnVal = new HashCodeBuilder(-426830461, 631494429);
		rtnVal.append(ModelUtil.getModelClassName(this));
		rtnVal.append(getCorpId());
		rtnVal.append(getSuiteId());
		return rtnVal.toHashCode();
	}
}
