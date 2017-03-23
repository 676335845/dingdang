package me.ywork.org.api.rpc;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import me.ywork.org.api.model.DingOrgCorpVo;
import me.ywork.org.api.model.DingOrgDeptVo;
import me.ywork.org.api.model.DingOrgUserVo;


/**
 * 组织架构管理接口
 * @author V
 *
 */
public interface ISysOrgManageService {
	

	/**
	 * 创建企业
	 * @param corp
	 * @return
	 */
	public String createDingOrgCorp(DingOrgCorpVo corp);
	
	
	/**
	 * 更新企业
	 * @param corp
	 */
	public void updateDingOrgCorp(DingOrgCorpVo corp);
	
	/**
	 * 创建更新用户
	 * @param user
	 * @return
	 */
	public String updateUser(JSONObject obj,String suiteId ,String corpId,Boolean isAdmin);
	
	
	/**
	 *  根据企业id查找企业
	 * @param corpId
	 * @return
	 */
	public DingOrgCorpVo findDingOrgCorpById(String corpId);
	
	/**
	 * 根据企业id和部门id找到部门
	 * @param corpId
	 * @param deptId
	 * @return
	 */
	public DingOrgDeptVo findDingOrgDeptByCorpIdAndDeptId(String corpId,String deptId);
	
	/**
	 * 根据企业id和员工id找到用户
	 * @param corpId
	 * @param userId
	 * @return
	 */
	public DingOrgUserVo findDingOrgUserById(String corpId, String userId);
		
	/**
	 * 用于获取指定企业部门下面的所有人员Id列表
	 * @param corpId  企业ID
	 * @param deptId  部门ID
	 * @param isFetchAll  是否获取所有子级部门人员
	 * @return 该部门下面所有的人员列表
	 */
	public List<String> getDingOrgUserIdsByCompanyIdDeptId(
			String corpId, String deptId, Boolean isFetchAll);
	
	
	/**
	 * 根据人员ID获取其所在直接部门及其上级部门列表
	 * @param corpId
	 * @param userId
	 * @return
	 */
	public Set<String> getDingOrgUserParentIdList(String corpId , String userId);
}
