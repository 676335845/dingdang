package me.ywork.context;

import java.io.Serializable;

/**
 * 前台调用Server层时的上下文接口
 *
 * @author TangGang 2015-5-10
 */
public interface CallContext extends Serializable {


	/**
	 * @return 当前登陆平台对应企业ID, 对于服务号则为appId
	 */
	String getCorpId();

	/**
	 * @return 用户在对应平台中的ID
	 */
	String getUserId();

	/**
	 * @return 用户在对应平台名称
	 * @author YiMing 2016-3-29
	 */
	String getUserName();


	/**
	 * @return 是否是管理员
	 * @author TangGang 2015-5-12
	 */
	Boolean isAdmin();

	/**
	 * @return 用户在对应平台的头像URL
	 */
	String getAvatar();


	/**
	 * @return 当前请求的域名称
	 */
	String getDomainName();

	/**
	 * 访问企业号的用户是否为企业内部员工
	 * @return
	 */
	public Boolean isQyUser();
}
