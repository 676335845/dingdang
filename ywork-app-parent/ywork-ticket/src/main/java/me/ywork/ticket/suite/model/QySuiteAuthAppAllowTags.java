package me.ywork.ticket.suite.model;

import java.io.Serializable;

public class QySuiteAuthAppAllowTags implements Serializable {
	public QySuiteAuthAppAllowTags() {
	}

	private int[] tagid;

	public int[] getTagid() {
		return tagid;
	}

	public void setTagid(int[] tagid) {
		this.tagid = tagid;
	}

}
