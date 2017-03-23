package me.ywork.salarybill.model;

import java.io.Serializable;

/**
 * 
 * @author kezm
 *
 * 2016年1月20日
 */
public class OrgPcSelectedStatusModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2416538429307435025L;
	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
