package me.ywork.ticket.suite.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Rocker on 2016-11-28.
 */
public class GetUnactiveCorpReqModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5932095504451361670L;
	@JSONField(name = "app_id")
    private Long appId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
