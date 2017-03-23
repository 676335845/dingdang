package com.heyi.framework.orm.helper;

import com.heyi.framework.orm.entity.EntityStatus;

/**
 * 实体代理接口，主要返回实体变更信息
 */
public interface EntityStatusable {
    /**
     * 获取实体对象的状态
     * @return  {@link EntityStatus}枚举值
     */
    EntityStatus getEntityStatus();

    /**
     * 设置实体对象的状态
     * @param entityStatus  实体对象的状态，对应{@link EntityStatus}枚举值
     */
    void setEntityStatus(EntityStatus entityStatus);

}
