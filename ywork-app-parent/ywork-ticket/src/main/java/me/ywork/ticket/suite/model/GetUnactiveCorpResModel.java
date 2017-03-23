package me.ywork.ticket.suite.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Rocker on 2016-11-28.
 */
public class GetUnactiveCorpResModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3830632423598201814L;


	@JSONField(name = "errcode")
    private Integer errCode;

    @JSONField(name = "errmsg")
    private String errMsg;

    @JSONField(name = "app_id")
    private Long appId;

    @JSONField(name = "has_more")
    private Boolean hasMore;

    @JSONField(name = "corp_list")
    private String[] corpList;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String[] getCorpList() {
        return corpList;
    }

    public void setCorpList(String[] corpList) {
        this.corpList = corpList;
    }
}
