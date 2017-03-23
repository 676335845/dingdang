package me.ywork.ticket.suite.model;

import java.io.Serializable;

/**
 * Created by Rocker on 2016-9-23.
 */
public class ModulePathModel implements Serializable {
    private static final long serialVersionUID = -3918424035153354095L;

    private String corpId;
    private String modulePath;
    private Long agentId;
    private String suiteId;
    private String logoUrl;


    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
