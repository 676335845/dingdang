package com.heyi.framework.orm.helper;

import org.apache.commons.lang.StringUtils;

/**
 * 实体字段变更信息
 */
public class EntityModifiedField {
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 数据库中的列名称
     */
    private String columnName;
    /**
     * 原始值
     */
    private Object originalValue;
    /**
     * 当前值
     */
    private Object value;

    public EntityModifiedField(String fieldName, Object originalValue, Object value) {
        if (fieldName == null || fieldName.isEmpty()) {
            throw new IllegalArgumentException("fieldName is null or empty.");
        }

        this.fieldName = fieldName;
        this.columnName = fieldName;
        this.originalValue = originalValue;
        this.value = value;
    }

    public EntityModifiedField(String fieldName, String columnName, Object originalValue, Object value) {
        this(fieldName, originalValue, value);

        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getOriginalValue() {
        return originalValue;
    }

    public Object getValue() {
        return value;
    }

    public void setFieldName(String fieldName) {
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("setFieldName - param fieldName is null or empty.");
        }

        this.fieldName = fieldName;
    }

    public void setOriginalValue(Object originalValue) {
        this.originalValue = originalValue;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
