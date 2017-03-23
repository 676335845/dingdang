package me.ywork.org.constants;

public class DingApiConstants {
	//获取部门列表
	public static String GET_DEPT_LIST = "https://oapi.dingtalk.com/department/list?access_token=ACCESS_TOKEN";
	//获取成员
	public static String GET_USER = "https://oapi.dingtalk.com/user/get?access_token=ACCESS_TOKEN&userid=USERID";
	//获取部门成员
	public static String GET_DEPT_USER = "https://oapi.dingtalk.com/user/simplelist?access_token=ACCESS_TOKEN&department_id=DEPTID&fetch_child=FETCH";
	//获取部门成员详情-ex
	public static String GET_DEPT_USER_DETAIL_EX = "https://oapi.dingtalk.com/user/list?access_token=ACCESS_TOKEN&department_id=DEPTID";
	//获取管理员接口
	public static String GET_CORP_ADMIN = "https://oapi.dingtalk.com/user/get_admin?access_token=ACCESS_TOKEN";
		
}
