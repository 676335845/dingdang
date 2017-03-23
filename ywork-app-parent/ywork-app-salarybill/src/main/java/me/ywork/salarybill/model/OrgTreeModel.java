package me.ywork.salarybill.model;

import java.io.Serializable;
import java.util.List;

/**
 * 该部门下的子部门或成员信息
 * @author Think
 *
 */
public class OrgTreeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4853105173707471263L;

	private String id;
	
	private String name;
	
	private int count;
	
	private String type ;
	
	private String avatar;
	
	private List<OrgTreeModel> datas; //子部门或该部门下的人员(不包含子部门的成员)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<OrgTreeModel> getDatas() {
		return datas;
	}

	public void setDatas(List<OrgTreeModel> datas) {
		this.datas = datas;
	}

	
}
