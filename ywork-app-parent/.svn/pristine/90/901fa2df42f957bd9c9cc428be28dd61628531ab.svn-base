package me.ywork.org.realtime.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ywork.base.repository.IRepository;
import me.ywork.org.realtime.entity.DingOrgActor;
import me.ywork.org.realtime.entity.DingOrgElement;
import me.ywork.org.realtime.entity.DingOrgUser;

@Repository
public interface DingOrgElementRepository extends IRepository<DingOrgElement>{
	
	void upsert(DingOrgElement element);
	
	DingOrgElement loadElementByImportInfo(String orgId, String importInfo);
	
	DingOrgElement loadElement(String orgId, String eleId);
	
	DingOrgElement loadUser(String orgId, String userId);

	DingOrgElement loadDept(String orgId, String deptId);
	
	DingOrgElement getDeptByDingId(String orgId, String dingId);
	
	/**
	 * 获取人员在多个部门中的身份
	 * @param orgId
	 * @param userId
	 * @return
	 */
	List<DingOrgActor> loadActorsForUser(String orgId, String userId);
	
	List<DingOrgUser> loadPostPersons(String fdOrgid, String fdPostId);

	void deleteElementById(String orgId, String deptId);

	void deleteElement(DingOrgElement element);

	void updateUser(DingOrgUser user);
	
	void updateSelective(DingOrgElement element);
	
	void updateHierarchyId(String orgId, String newHierarchyId , int index1, int index2 , String oldHierarchyId);
	
	/**
	 * 更新管理员
	 * @param orgId
	 * @param userid
	 * @param isAdmin
	 */
	void updateAdmin(String orgId,String userid,boolean isAdmin);
	
	public DingOrgUser findDingOrgUserByCorpIdUserId(String corpId, String userId);
	
	/**
	 * 找出离职员工
	 * @param orgId
	 * @return
	 */
	List<String> loadUnavailableByOrgId(String orgId , Date lastAlterTime);
	
	/**
	 * 标记离职员工
	 * @param orgId
	 */
	void updateUnavailableByOrgId(String orgId , Date lastAlterTime);
	
	/**
	 * 标记离职员工
	 * @param orgId
	 * @param userIds
	 */
	void updateUnavailableByDingId(@Param("orgId") String orgId , @Param("userIds") String[] userIds);
	
	
	void updateAlterTime(DingOrgElement element);
	/**
	 * 根据钉钉返回的员工userId查询蓝凌人员列表
	 * @param corpId   钉钉商户ID
	 * @param userIds  钉钉员工ID列表
	 * @return 人员对象列表
	 */
	public List<DingOrgUser> getDingOrgUsersByCorpIdUserIds(String corpId,
			List<String> userIds);
	
	/**
	 * 用于压力测试，随机获取指定企业的部门ID
	 * @param corpId   企业ID
	 * @param random      用于随机获取部门的随机数
	 * @return  部门ID
	 */
	public String getRandomDeptId(String corpId, double random);
	
	/**
	 * 用于压力测试，获取指定企业部门下面的所有人员列表
	 * @param comapnyId  企业ID
	 * @param deptId  部门ID
	 * @param count   返回的最大人数量
	 * @return 该部门下面所有的人员列表
	 */
	public List<String> getDingOrgUserVosByCompanyIdDeptId(
			String comapnyId, String deptId, int count);
	
	/**
	 * 批量更新部门下的人员分身的hid
	 * @param orgId
	 * @param deptId
	 * @param hierarchyId
	 */
	void updateActorsHierarchyId(String orgId, String deptId, String hierarchyId);
	
	/**
	 * 更新corp user 主管理员
	 * @param corpId
	 * @param userIds
	 * @return
	 */
	void updateCorpSupAdmin(String corpId, String userId);
	
	void batchUpsertActors(@Param("elements") List<DingOrgElement> elements);
	
	void batchUpdateSelective(@Param("elements") List<DingOrgElement> elements,@Param("fdOrgType")int fdOrgType);
	
}
