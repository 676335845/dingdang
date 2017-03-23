package me.ywork.ticket.suite.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Rocker on 2016-11-28.
 */
public class ReAuthCorpReqModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6080749757444705027L;
	@JSONField(name = "app_id")
    private Long appId;
    private String[] corpIdList;

    @JSONField(name = "corpid_list")
    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String[] getCorpIdList() {
        return corpIdList;
    }

    public void setCorpIdList(String[] corpIdList) {
        this.corpIdList = corpIdList;
    }
}
