package me.ywork.salarybill.service.impl.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ywork.salarybill.entity.SalaryBillPwdSet;
import me.ywork.salarybill.model.SalaryBillPwdSetModel;
import me.ywork.util.AESUtil;

/**
 * 投票业务工具方法类
 * 
 * @author TangGang 2015年7月24日
 * 
 */
public class SalaryServiceImplUtils {
	private static Logger logger = LoggerFactory.getLogger(SalaryServiceImplUtils.class);


	/**
	 * 根据密码模型转换成实体对象
	 * 
	 * @param callContext
	 *            客户端上下文对象
	 * @param salaryBillPwdSetModel
	 *            密码模型
	 * @return 密码实体对象
	 */
	public static SalaryBillPwdSet convertToSalaryBillPwdSet(SalaryBillPwdSetModel salaryBillPwdSetModel) {
		if (salaryBillPwdSetModel == null) {
			throw new NullPointerException(
					"convertToSalaryBillPwdSet - parameter salaryBillPwdSetModel is null.");
		}
		SalaryBillPwdSet salaryBillPwdSet = new SalaryBillPwdSet();
		Date now = new Date();

		if (salaryBillPwdSetModel.getNeedReset() != null) {
			salaryBillPwdSet.setNeedReset(salaryBillPwdSetModel.getNeedReset());
		}
		salaryBillPwdSet.setCompanyId(salaryBillPwdSetModel.getCompanyId());
		salaryBillPwdSet.setCreateTime(now);
		salaryBillPwdSet.setCreateUserId(salaryBillPwdSetModel.getCreateUserId());
		try {
			salaryBillPwdSet.setPassword(AESUtil.encrypt(salaryBillPwdSetModel.getPassword(),salaryBillPwdSetModel.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			salaryBillPwdSet.setPassword("-1");
		}
		salaryBillPwdSet.setPasswordType(salaryBillPwdSetModel.getPasswordType());
		salaryBillPwdSet.setUpdateTime(now);
		salaryBillPwdSet.setUpdateUserId(salaryBillPwdSetModel.getUpdateUserId());
		salaryBillPwdSet.setUserId(salaryBillPwdSetModel.getUserId());
		salaryBillPwdSet.setId(salaryBillPwdSetModel.getId());
		
		salaryBillPwdSet.setErrPwdCount(0);
		salaryBillPwdSet.setReleaseTime(new Date());
		return salaryBillPwdSet;
	}
}
