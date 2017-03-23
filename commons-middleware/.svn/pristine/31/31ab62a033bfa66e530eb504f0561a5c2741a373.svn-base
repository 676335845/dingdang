package com.heyi.framework.mongo.springdata.entity;

import org.springframework.data.annotation.Version;

/**
 * 带版本并发控制的实体
 */
public class SpringVersionEntity<ID> extends SpringMongoEntity<ID> {
    private static final long serialVersionUID = 5290231335039496665L;
    /**
     * 用于数据更新的并发控制
     */
    @Version
    private Long version;

    public SpringVersionEntity() {
        super();
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
