package me.ywork.org.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ywork.context.CallContext;
import me.ywork.org.entity.DingOrgDept;
import me.ywork.org.entity.DingOrgElement;
import me.ywork.org.repository.DingOrgElementRepository;
import me.ywork.org.service.IDingOrgElementService;

@Service(IDingOrgElementService.SERVICE_NAME)
public class DingOrgElementServiceImpl implements IDingOrgElementService{
	
	@Autowired
	private DingOrgElementRepository dingOrgElementRepository;
	
	public DingOrgElementRepository getRepository() {
		return dingOrgElementRepository;
	}
	
	
	@Override
	public void deleteElementById(String orgId, String deptId) {
		dingOrgElementRepository.deleteElementById(orgId, deptId);
	}
	@Override
	public void deleteElement(DingOrgElement element) {
		dingOrgElementRepository.deleteElement(element);
	}

	@Override
	public DingOrgElement loadElement(String orgId, String eleId) {
		return dingOrgElementRepository.loadElement(orgId, eleId);
	}

//	@Override
//	public DingOrgElement loadElementByImportInfo(String orgId,
//			String importInfo) {
//		return dingOrgElementRepository.loadElementByImportInfo(orgId, importInfo);
//	}

	@Override
	public void updateSelective(DingOrgElement element) {
		dingOrgElementRepository.updateSelective(element);
	}
	
	

	@Override
	public void batchUpsert(List<DingOrgElement> elements,int fdOrgType) {
		dingOrgElementRepository.batchUpdateSelective(elements,fdOrgType);
	}


	@Override
	public void updateHierarchyId(String orgId, String newHierarchyId,
			String oldHierarchyId) {
		dingOrgElementRepository.updateHierarchyId(orgId, newHierarchyId, oldHierarchyId.length() + 1, oldHierarchyId.length(), oldHierarchyId);
	}

	@Override
	public void upsert(DingOrgElement element) {
		dingOrgElementRepository.upsert(element);
	}

	@Override
	public void updateAlterTime(DingOrgElement element) {
		dingOrgElementRepository.updateAlterTime(element);
	}

	@Override
	public List<String> loadUnavailableByOrgId(String orgId,Date lastAlterTime) {
		return dingOrgElementRepository.loadUnavailableByOrgId(orgId , lastAlterTime);
	}

	@Override
	public void updateUnavailableByOrgId(String orgId,Date lastAlterTime) {
		dingOrgElementRepository.updateUnavailableByOrgId(orgId , lastAlterTime);
	}

	@Override
	public void updateActorsHierarchyId(String orgId, DingOrgDept dept,
			String hierarchyId) {
		dingOrgElementRepository.updateActorsHierarchyId(orgId , dept.getId() , hierarchyId);
		
	}
	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
}
