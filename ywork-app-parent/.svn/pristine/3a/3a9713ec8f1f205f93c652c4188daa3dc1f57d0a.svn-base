package me.ywork.org.realtime.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.MD5Util;

import me.ywork.org.realtime.constants.DingOrgConstant;
import me.ywork.org.realtime.entity.DingOrgCorp;
import me.ywork.org.realtime.entity.DingOrgDept;
import me.ywork.org.realtime.entity.DingOrgElement;
import me.ywork.org.realtime.service.IDingOrgCorpService;
import me.ywork.org.realtime.service.IDingOrgDeptService;
import me.ywork.org.realtime.service.IDingOrgElementService;

@Service(IDingOrgDeptService.SERVICE_NAME)
public class DingOrgDeptServiceImpl extends DingOrgElementServiceImpl implements IDingOrgDeptService{

	private static Logger logger = LoggerFactory.getLogger(DingOrgUserServiceImpl.class);
	
	@Autowired
	private IDingOrgCorpService dingOrgCorpService;
	
	@Resource(name = "dingOrgElementService")
	private IDingOrgElementService dingOrgElementService;
	
	@Override
	public DingOrgDept loadDept(String orgId, String deptId) {
		DingOrgDept dingOrgDept = (DingOrgDept)getRepository().loadDept(orgId, deptId);
		return dingOrgDept;
	}
	
	

	@Override
	public DingOrgElement getDeptByDingId(String orgId, String dingId) {
		DingOrgDept dingOrgDept = (DingOrgDept)getRepository().getDeptByDingId(orgId, dingId);
		return dingOrgDept;
	}



	@Override
	public String getRandomDeptId(String companyId) {
		//循环随机取企业号ID，尝试5次
		String deptId = null;
		int count = 0;
		while (count < 5) {
			count ++;
			// 产生随机数
			double random = Math.random();
			deptId = getRepository().getRandomDeptId(companyId, random);
			if (StringUtils.isNotBlank(deptId)) {
				break;
			}
		}
		return deptId;
	}

	@Override
	public String updateDept(String suiteId, String corpId, JSONObject deptJson) {
		// TODO Auto-generated method stub
		DingOrgCorp corp = dingOrgCorpService.getCorpByAppKey(corpId);
		if(corp!=null){
			String id = deptJson.getString("id");
			
			String IMPORTKEY = "ding_dept_" + String.valueOf(id);
			String fdId = MD5Util.getMD5String(corp.getId().concat(IMPORTKEY));
			DingOrgDept orgDept = new DingOrgDept();
			orgDept.setId(fdId);
			orgDept.setFdCreateTime(new Date());
			orgDept.setFdAlterTime(corp.getFdLastSyncTime());
			orgDept.setFdFlagDeleted(false);
			orgDept.setFdImportInfo(IMPORTKEY);
			orgDept.setFdDingid(String.valueOf(id));
			orgDept.setFdIsAbandon(false);
			orgDept.setFdIsAvailable(true);
			//orgDept.setFdIsBusiness(true);
			orgDept.setFdOrgid(corp.getId());
			orgDept.setFdOrgType(DingOrgConstant.ORG_TYPE_DEPT);
			orgDept.setFdName(deptJson.getString("name"));
			dingOrgElementService.upsert(orgDept);
			
			if(!"1".equals(id)){
				String parentid = deptJson.getString("parentid");
				//根据钉钉部门id查找部门
				DingOrgElement e = getDeptByDingId(corpId,parentid);
				orgDept.setFdParentid(e.getId());
			}
			
			//处理该部门下的所有子部门
			
			
			return "";//super.syncUserByJson(suiteId , corp, deptJson);
		}else{
			logger.warn("企业不存在:{}" , corpId);
//			DingSuiteThird third = dingSuiteThirdMapper.findDingSuiteThirdByCorpId(corpId, suiteId);
//			if (third != null) {
//				sysOrgManageService.syncDingOrg(DingSuiteThird.toVo(third));
//			}
			return null;
		}
		//DingOrgDept dept = saveDept(deptJson,dingOrg);
		
	}
	
	
}
