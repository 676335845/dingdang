package me.ywork.salary.service;

import java.util.List;
import me.ywork.base.service.BizService;
import me.ywork.context.CallContext;
import me.ywork.salary.model.SalCorpInfoModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;

/**
 * 企业密码业务管理
 * 
 */
public interface SalPassService extends BizService {
	/**
	 * 得到企业密码锁开启的状态
	 * 
	 * @param corpId 钉钉企业号
	 * @return 密码开启的状态
	 */
	Short getCorpPassOpenStatus(String corpId);

	/**
	 * 根据员工的信息去模糊查询员工的信息
	 * 
	 * @param corpId  钉钉企业号
	 * @param keyword 查询的关键字
	 * @return 查询的结果
	 */
	List<SalStaffBaseInfoModel> getStaffInfoByKeyword(String corpId, String keyword);

	/**
	 * 对员工的密码进行重置
	 * 
	 * @param corpId 钉钉企业号
	 * @param salStaffBaseInfoModel 封装要更新员工密码的员工信息的集合
	 * @return 更新的结果
	 */
	Boolean resetStaffPass(String corpId,List<SalStaffBaseInfoModel> salStaffBaseInfoModel);
	
	/**
	 * 更新企业的密码锁
	 * 
	 * @param salCorpInfoModel 存储企业信息的数据包
	 * @return 更新的结果
	 */
	 Boolean updateCorpPassStatus(SalCorpInfoModel salCorpInfoModel);
	 
	 /**
	  * 判断是否设置过密码 
	  * 
	  * @param callContext 存储管理员或企业的数据包
	  * @return 
	  */
	SalStaffBaseInfoModel everStaffsetpwd(CallContext  callContext);
	
	 /**
	  *  得到员工的密码
	 * 
	 * @param callContext 存储管理员或企业的数据包
	 * @return 员工的密码
	 */
	 String getUserPwd(CallContext callContext);
	 
	 /**
	  * 设置员工密码
	  * 
	  * @param callContext  存储管理员或企业的数据包
	  * @param password 员工的密码
	  * @return 设置密码的结果
	  */
	Boolean  setUserPwd(CallContext callContext , String password);
	
	/**
	 * 开启或关闭密码查看
	 * 
	 * @param callContext 存储管理员或企业的数据包
	 * @param passState 企业开启或关闭密码锁的状态
	 * @return 开启或关闭密码锁的结果
	 */
	Boolean managerUserPwdState(CallContext callContext , Short passState);
}
