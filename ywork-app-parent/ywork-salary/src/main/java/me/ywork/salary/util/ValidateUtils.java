package me.ywork.salary.util;

import org.apache.commons.lang.StringUtils;

import me.ywork.context.CallContext;

public class ValidateUtils {

	public static Boolean existCorpIdAndStaffId(CallContext callContext){
		Boolean rs=Boolean.TRUE;
		if(callContext==null){
			return Boolean.FALSE;
		}
		String corpId = callContext.getCorpId();
		String staffId = callContext.getUserId();
		if(StringUtils.isBlank(corpId)){
			rs=Boolean.FALSE;
		}
		if(StringUtils.isBlank(staffId)){
			rs=Boolean.FALSE;
		}
		return rs;
	}

}
