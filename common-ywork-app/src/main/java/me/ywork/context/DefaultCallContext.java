package me.ywork.context;

/**
 * 前台调用后台上下文默认实现
 *
 * @author TangGang 2015-5-10
 */
public class DefaultCallContext implements CallContext {
	private static final long serialVersionUID = -2990484493590381996L;

	/**
	 * 第三方平台企业ID
	 */
	private String corpId;
	/**
	 * 用户在第三方平台中的ID
	 */
	private String userId;
	/**
	 * 用户在第三方平台的登录名
	 */
	private String userName;
	/**
	 * 是否为管理员
	 */
	private Boolean isAdmin;
	
	/**
	 * 用户头像URL
	 */
	private String avatar;

	/**
	 * 是否老板
	 */
	private Boolean isBoss;
	

	// 其它属性
	/**
	 * 当前请求的域名称
	 */
	private String domainName;

	/**
	 * 访问企业号的用户是否为企业内部员工
	 */
	private Boolean isQyUser;



	public DefaultCallContext() {
		super();
	}

	public DefaultCallContext(Long personId, String personName, String corpId) {
		super();
		this.corpId = corpId;
	}

	public Boolean getIsBoss() {
		return isBoss;
	}

	public void setIsBoss(Boolean isBoss) {
		this.isBoss = isBoss;
	}

	@Override
	public String toString() {
		return "DefaultCallContext{" +
				", corpId='" + corpId + '\'' +
				", userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", isAdmin=" + isAdmin +
				", avatar='" + avatar + '\'' +
				", domainName='" + domainName + '\'' +
				'}';
	}

	@Override
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public Boolean isAdmin() {
		return isAdmin;
	}

	@Override
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Override
	public String getCorpId() {
		return this.corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}


	@Override
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getAdmin() {
		return isAdmin;
	}

	public void setAdmin(Boolean admin) {
		isAdmin = admin;
	}


	@Override
	public Boolean isQyUser() {
		return isQyUser;
	}

	public Boolean getQyUser() {
		return isQyUser;
	}

	public void setQyUser(Boolean qyUser) {
		isQyUser = qyUser;
	}

	public Boolean getIsQyUser() {
		return isQyUser;
	}

	public void setIsQyUser(Boolean isQyUser) {
		this.isQyUser = isQyUser;
	}
	
	
}
