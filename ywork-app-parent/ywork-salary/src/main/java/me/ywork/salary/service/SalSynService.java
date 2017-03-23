package me.ywork.salary.service;

import java.util.List;

import me.ywork.base.service.BizService;

public interface SalSynService extends BizService   {
	
	
	void synchCopInfo(List<String> corpList);

}
