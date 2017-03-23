package me.ywork.ticket.suite.model;

import java.io.Serializable;

/**
 * Created by Rocker on 2016-9-5.
 */
public class AuthResult implements Serializable {
    private static final long serialVersionUID = 8056960050725020600L;

    private int errCode;
    private String errMessage;
    private String authDomain;
    private String corpId;

    public int getErrCode() {
        return errCode;
    }

    public AuthResult setErrCode(int errCode) {
        this.errCode = errCode;
        return this;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public AuthResult setErrMessage(String errMessage) {
        this.errMessage = errMessage;
        return this;
    }

    public String getAuthDomain() {
        return authDomain;
    }

    public AuthResult setAuthDomain(String authDomain) {
        this.authDomain = authDomain;
        return this;
    }

    public String getCorpId() {
        return corpId;
    }

    public AuthResult setCorpId(String corpId) {
        this.corpId = corpId;
        return this;
    }
}
