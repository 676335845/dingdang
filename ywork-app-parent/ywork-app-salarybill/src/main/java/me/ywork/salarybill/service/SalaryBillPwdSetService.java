package me.ywork.salarybill.service;

import java.util.List;

import me.ywork.base.service.BizService;
import me.ywork.salarybill.model.OrgDataModel;
import me.ywork.salarybill.model.OrgItemResult;
import me.ywork.salarybill.model.PwdCheckResult;
import me.ywork.salarybill.model.SalaryBillNoPwdSelectedItemViewMode;
import me.ywork.salarybill.model.SalaryBillPwdSetModel;
import me.ywork.salarybill.model.SalaryBillPwdTempModel;
import me.ywork.salarybill.model.UserModel;

public interface SalaryBillPwdSetService extends BizService {

	/**
	 * 用户是否设置过密码
	 * 
	 * @param companyId
	 * 
	 * @param personId
	 * 
	 * @param pwdType
	 *            密码类型
	 * @return true已设置，false没有
	 */
	public Boolean evensetpwd(String companyId, String personId, Short pwdType) throws SalaryBillException;

	/**
	 * 用户设置密码
	 * 
	 * @param companyId
	 * 
	 * @param personId
	 * 
	 * @param salaryBillPwdSetModel
	 *            设置信息
	 * @return true成功，false失败
	 */
	public Boolean setUserPwd(String companyId, String persionId, Short type,
			SalaryBillPwdSetModel salaryBillPwdSetModel) throws SalaryBillException;

	/**
	 * 用户重设置密码
	 * 
	 * @param companyId
	 * 
	 * @param personId
	 * 
	 * @param salaryBillPwdSetModel
	 *            设置信息
	 * @return true成功，false失败
	 */
	public String resetUserPwd(String companyId, String persionId, Short type,
			SalaryBillPwdSetModel salaryBillPwdSetModel) throws SalaryBillException;

	/**
	 * 密码比较
	 * 
	 * @param companyId
	 * 
	 * @param personId
	 * 
	 * @param salaryBillPwdSetModel
	 *            设置信息
	 * @return true成功，false失败
	 */
	public PwdCheckResult comparePwd(String companyId, String personId, SalaryBillPwdSetModel salaryBillPwdSetModel)
			throws SalaryBillException;

	public String forgetPwd(String domainName, String companyId, String userId, Short type);

	public Boolean checkRandom(String companyId, String randomCode);

	public List<UserModel> findSalaryPwdModelBySerach(String companyId, String searchKey);

	public Boolean clearUserPwd(String domainName, String companyId, String persionId, Short type)
			throws SalaryBillException;

	public OrgItemResult saveNoneedPwdItems(List<OrgDataModel> orgDataModels,String companyId, String createUserId);

	public List<SalaryBillNoPwdSelectedItemViewMode> findNoneedPwdItems(String companyId);

	public OrgItemResult doReset(String companyId, String id,String type);

	public PwdCheckResult setPasswdOnOf(String companyId, String personId,
			SalaryBillPwdTempModel salaryBillPwdTempModel);

	public Boolean checkHaveSetPasswd(String companyId, String personId, Short pwdType);

	public Boolean getPWdStatus(String companyId, String personId, Short pwdType) throws SalaryBillException;

}
