package me.ywork.ticket.suite.model;

import java.io.Serializable;
import java.util.List;

public class QySuiteAuthAppAllowUserInfo implements Serializable{
	public QySuiteAuthAppAllowUserInfo() {
	}
	
	private List<QySuiteAuthAppAllowUser> users;

	public List<QySuiteAuthAppAllowUser> getUsers() {
		return users;
	}

	public void setUsers(List<QySuiteAuthAppAllowUser> users) {
		this.users = users;
	}
	
}
