package me.ywork.org.service;

import java.util.Date;
import java.util.List;

import me.ywork.base.service.BizService;
import me.ywork.org.entity.DingOrgDept;
import me.ywork.org.entity.DingOrgElement;


public interface IDingOrgElementService extends BizService{
	
	String SERVICE_NAME = "dingOrgElementService";
	
	DingOrgElement loadElement(String orgId, String eleId);
	
	void deleteElementById(String orgId, String deptId);

	void deleteElement(DingOrgElement element);
	
	//DingOrgElement loadElementByImportInfo(String orgId, String importInfo);

	void updateSelective(DingOrgElement element);
	
	void upsert(DingOrgElement element);
	
	void batchUpsert(List<DingOrgElement> element,int fdOrgType);
	
	void updateHierarchyId(String orgId, String newHierarchyId ,String oldHierarchyId);

	void updateAlterTime(DingOrgElement element);
	
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
	 * 批量修改部门下面所有人员分身的Hid
	 * @param fdId
	 * @param dept
	 * @param hierarchyId
	 */
	void updateActorsHierarchyId(String fdId, DingOrgDept dept,
			String hierarchyId);
}
