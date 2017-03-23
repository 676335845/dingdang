package me.ywork.suite.service;

/**
 * 套件、企业号自定义应用相关服务接口
 */
public interface DingCorpTokenService {

    String getToken(String corpId, String suiteId);
    
    String getPcToken(String corpId,String sercet);
}
