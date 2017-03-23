package me.ywork.salary.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.model.SalCorpInfoModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;

/**
 * 
 * 企业密码锁数据返回层的管理器
 * 
 * @author xiaobai
 */
@Repository
public interface SalPassRepository extends IRepository<SalStaffBaseInfoEntity> {

	/**
	 * 得到企业密码锁开启的状态
	 * 
	 * @param corpId   钉钉企业号
	 * @return   密码开启的状态
	 */
	Short getCorpPasswordOpenStatus(@Param("corpId") String corpId);

	/**
	 * 根据员工的信息去模糊查询员工的信息
	 * 
	 * @param corpId  钉钉企业号
	 * @param keyword  查询的关键字
	 * @return   查询的结果
	 */
	List<SalStaffBaseInfoModel> getStaffInfoByKeyword(@Param("corpId") String corpId, @Param("keyword") String keyword);
	
	/**
	 * 更新企业的密码锁
	 * 
	 * @param salCorpInfoModel 存储要更新企业密码锁的企业信息
	 * @return 更新密码锁的结果
	 */
	 Integer updateCorpPassStatus(SalCorpInfoModel salCorpInfoModel);

	/**
	 * 对员工的密码进行重置
	  * 
	  * @param corpId  钉钉企业ID
	  * @param salStaffBaseInfoModel 更新企业密码锁的员工信息的数据包
	  * @param modifiedDate 更改的时间
	  * @return  重置的结果
	  */
	Integer resetStaffPassword(@Param("corpId") String corpId, @Param("sal")SalStaffBaseInfoModel salStaffBaseInfoModel,@Param("modifiedDate")Date modifiedDate);
	
	/**
	 * 对部门下的人员的密码进行重置
	 * 
	 * @param corpId  钉钉企业ID
	 * @param deptId 钉钉部门ID
	 * @param modifiedDate 更改的时间
	 * @return  重置的结果
	 */
	Integer resetDeptPassword(@Param("corpId") String corpId, @Param("deptId")String deptId,@Param("modifiedDate")Date modifiedDate);
	
	 /**
	  *  得到员工的密码
	  *  
	 * @param corpId 钉钉企业ID
	 * @param dingStaffId 钉钉员工ID
	 * @return 员工的密码
	 */
	 String getUserPwd(@Param("corpId")String corpId , @Param("dingStaffId")String dingStaffId);
	 
	 /**
	  * 设置员工密码
	  * 
	  * @param corpId 钉钉企业ID
	  * @param dingStaffId 钉钉员工ID
	  * @param password 员工的密码 
	  * @return 设置密码的结果
	  */
	Integer  setUserPwd(@Param("corpId")String corpId , @Param("dingStaffId")String dingStaffId,@Param("staffPass")String password);
	
	/**
	 * 开启或关闭密码查看
	 * 
	 * @param corpId  钉钉企业ID
	 * @param dingStaffId 钉钉员工ID
	 * @param passState 开启密码锁的状态
	 * @return 设置的结果
	 */
	Integer managerUserPwdState(@Param("corpId")String corpId , @Param("dingStaffId")String dingStaffId,@Param("passState")Short passState);
	 
}
