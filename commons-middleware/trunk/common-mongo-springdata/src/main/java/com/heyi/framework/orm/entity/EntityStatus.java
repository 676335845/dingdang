package com.heyi.framework.orm.entity;

/**
 * Mongo实体对象的状态
 */
public enum EntityStatus {
    /**
     * 新增实体
     */
    New,
    /**
     * 未修改
     */
    UnModify,
    /**
     * 已修改
     */
    Modified;
}
