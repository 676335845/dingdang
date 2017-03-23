package me.ywork.ticket.suite.model;

import java.io.Serializable;

public class QySuiteAuthAppAllowPartys implements Serializable {
	public QySuiteAuthAppAllowPartys() {
	}

	private int[] partyid;

	public int[] getPartyid() {
		return partyid;
	}

	public void setPartyid(int[] partyid) {
		this.partyid = partyid;
	}

}
