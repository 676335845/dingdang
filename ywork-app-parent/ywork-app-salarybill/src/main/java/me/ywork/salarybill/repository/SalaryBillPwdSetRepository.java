package me.ywork.salarybill.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.repository.IRepository;
import me.ywork.salarybill.entity.SalaryBillNoPwdSelectedItem;
import me.ywork.salarybill.entity.SalaryBillPwdSet;
import me.ywork.salarybill.model.SalaryBillNoPwdSelectedItemViewMode;
import me.ywork.salarybill.model.UserModel;
import me.ywork.salarybill.model.UserTempModel;

public interface SalaryBillPwdSetRepository extends IRepository<SalaryBillPwdSet>{

	/**
	 * 查用户密码
	 * @param companyId
	 * @param userId
	 * @param passwordType 密码类型
	 * @return
	 */
	public SalaryBillPwdSet findSalaryBillPwdSetByUser(
			@Param(value = "companyId") String companyId,
			@Param(value = "userId") String userId,
			@Param(value = "passwordType") Short passwordType);
	
	
	public Boolean updateUserPwd(
			@Param(value = "companyId") String companyId,
			@Param(value = "id") String id,
			@Param(value = "updateUserId") String updateUserId ,
			@Param(value = "password") String password,
			@Param(value = "passwordType") Short passwordType);
	
	
	public SalaryBillPwdSet existsUserPwd(
			@Param(value = "companyId") String companyId,
			@Param(value = "userId") String userId,
			@Param(value = "passwordType") Short passwordType);
	
	
	/**
	 * 离职查询
	 * @param companyId
	 * @param userId
	 * @return
	 */
	public String isAvailable(
			@Param(value = "companyId") String companyId,
			@Param(value = "userId") String userId);
	
	/**
	 * 查管理员
	 * @param companyId
	 * @return
	 */
	public String findManager(
			@Param(value = "companyId") String companyId);
	
	
	/**
	 * 重置密码用户查询
	 * @param companyId
	 * @param userId
	 * @param passwordType 密码类型
	 * @return
	 */
	public List<UserModel> findSalaryBillPwdBySerachKey(
			@Param(value = "companyId") String companyId,
			@Param(value = "searchKey") String searchKey);
	
	 /**
	  * 保存选中的不需要设置密码的项
	  * @param itemList
	  */
	public void saveNoneedPwdItems(@Param(value = "companyId") String companyId,@Param("itemList") List<SalaryBillNoPwdSelectedItem> itemList);
	
//	public Integer updateNoneedPwdItem(@Param(value = "companyId") String companyId,@Param("selectedId") String selectedId,
//			@Param("hierarchy") String hierarchy,@Param("name") String name,@Param("type") String type);
//
	public List<SalaryBillNoPwdSelectedItemViewMode> findNoneedPwdItems(@Param("companyId") String companyId);
	
	public List<UserTempModel> findDingPersonByUserId(@Param("companyId") String companyId,@Param("userId") String userId);
	
	public List<String> needPwd(@Param("companyId") String companyId,@Param("eid") String eid);

    public  List<String> findChildPerson(@Param("companyId") String companyId,@Param("deptList") List<String> deptList);
    
	public void doReset(@Param("companyId") String companyId,@Param("eid") String eid);


	public void updateUserPwdOpenStatus(@Param("companyId") String companyId,
			@Param("userList") List<String> userList,
			@Param(value = "passwordType") Short passwordType,
			@Param(value = "isNeedPwd") Boolean isNeedPwd);

}
