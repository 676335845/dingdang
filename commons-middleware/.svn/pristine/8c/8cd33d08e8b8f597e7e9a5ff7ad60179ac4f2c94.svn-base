package com.heyi.framework.orm.helper;

import java.util.Map;

import com.heyi.framework.model.PrimaryKey;

/**
 * 可用于管理的实体必须实现该接口
 * @param <PK>  实体主键类型
 */
public interface EntityHelperable<PK> extends PrimaryKey<PK>, EntityStatusable {
    /**
     *
     * @return 返回实体被更新的字段列表，其中包含原始和更新后的内容
     */
    Map<String, EntityModifiedField> getModifiedFieldsMap();

}
