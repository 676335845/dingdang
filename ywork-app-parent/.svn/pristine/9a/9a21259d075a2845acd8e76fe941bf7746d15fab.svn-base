package me.ywork.org.realtime.entity;

/**
 * 
 * 分身，员工在部门的信息
 * @author
 * @version 1.0 2015-07-27
 */
public class DingOrgActor extends DingOrgElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4724361551298550669L;

	public DingOrgActor() {
		super();
		setFdOrgType(new Integer(ORG_TYPE_ACTOR));
	}
	
	private DingOrgUser fdUser;

	public DingOrgUser getFdUser() {
		return fdUser;
	}

	public void setFdUser(DingOrgUser fdUser) {
		this.fdUser = fdUser;
	}
	
//	public static DingOrgActorVo toVo(DingOrgActor model){
//		if(model==null) return null;
//		DingOrgActorVo vo = new DingOrgActorVo();
//		vo.setId(model.getId());
//		vo.setFdDeptId(model.getFdParentid());
//		vo.setFdDeptName("");
//		vo.setFdUserId(model.getFdUser().getId());
//		vo.setFdUserName(model.getFdUser().getFdName());
//		return vo;
//	}
}
