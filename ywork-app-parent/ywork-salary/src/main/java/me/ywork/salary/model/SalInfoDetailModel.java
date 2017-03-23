package me.ywork.salary.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaobai on 2017/1/11.
 */
public class SalInfoDetailModel extends SalStaffBaseInfoModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8332833301286052901L;
	/**
	 * 员工薪资信息的标识
	 */
	private String id;
	/**
	 * 管理员对该员工自定义的字段模型列表
	 */
	private List<SalSysFieldItemModel> itemModels;

	public SalInfoDetailModel() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SalSysFieldItemModel> getItemModels() {
		return itemModels;
	}

	public void setItemModels(List<SalSysFieldItemModel> itemModels) {
		this.itemModels = itemModels;
	}

}
