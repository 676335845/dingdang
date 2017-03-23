package com.heyi.framework.orm.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.heyi.framework.orm.helper.EntityHelperable;
import com.heyi.framework.orm.helper.EntityModifiedField;

/**
 * 带有状态控制的实体抽象类
 */
public abstract class StatusableEntity<PK> extends AbstractEntity<PK>
		implements EntityHelperable<PK> {
	private static final long serialVersionUID = -7628368516501622786L;

    /**
     * 实体状态
     */
    private EntityStatus entityStatus;

    /**
     * 被修改的字段
     */
    private Map<String, EntityModifiedField> modifiedFieldsMap;

    /**
     * 当字段由修改状态变成未修改状态时，
     * 将对应字段信息从{@link #modifiedFieldsMap}移入此列表中，以避免对同一字段重复创建字段变更对象
     */
    private Map<String, EntityModifiedField> unModifyFieldsMap;

    public StatusableEntity() {
        super();
        this.setEntityStatus(EntityStatus.New);
    }

    /**
     * 实体字段值变更事件
     * @param fieldName   字段名称
     * @param originValue 字段原始值
     * @param value       字段当前值
     */
    protected void fieldValueChanged(String fieldName, Object originValue, Object value) {
        this.fieldColumnValueChanged(fieldName, fieldName, originValue, value);
    }

    /**
     * 实体字段值变更事件
     * @param fieldName   字段名称
     * @param columnName  字段对应数据库中的列名
     * @param originValue 字段原始值
     * @param value       字段当前值
     */
    protected void fieldColumnValueChanged(String fieldName, String columnName, Object originValue, Object value) {
        // 不处理新增实体
        if (EntityStatus.New == this.getEntityStatus()) {
            return;
        }

        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("fieldValueChanged - fieldName is null or empty.");
        }

        if (StringUtils.isBlank(columnName)) {
            columnName = fieldName;
        }

        EntityModifiedField entityModifiedField = this.getModifiedFieldsMap().get(fieldName);
        if (entityModifiedField == null) { // 当前字段未被修改过
            // 如果原始值与新值相同则不标记为被修改过
            if (!ObjectUtils.equals(originValue, value)) {
                // 先从未变更列表中查找
                entityModifiedField = this.getUnModifyFieldsMap().get(fieldName);
                if (entityModifiedField == null) {
                    entityModifiedField = new EntityModifiedField(fieldName, columnName, originValue, value);
                } else {
                    // 与原始值进行比较
                    if (ObjectUtils.equals(value, entityModifiedField.getOriginalValue())) {
                        // 值未改变,不需要标记为已更新
                        entityModifiedField = null;
                    } else {
                        // 从未修改列表中删除字段
                        this.getUnModifyFieldsMap().remove(fieldName);
                        entityModifiedField.setValue(value);
                    }
                }

                if (entityModifiedField != null) {
                    this.getModifiedFieldsMap().put(fieldName, entityModifiedField);
                }
            }
        } else { // 当前字段已经被修改
            // 使用新值跟原始值进行比较，如果相同则表示未发生变更，需要从修改列表中移除该值
            if (ObjectUtils.equals(value, entityModifiedField.getOriginalValue())) {
                // 值未改变,或已还原为原始值，将字段传记为未修改
                entityModifiedField.setValue(value);
                this.getModifiedFieldsMap().remove(fieldName);
                this.getUnModifyFieldsMap().put(fieldName, entityModifiedField);
            } else {
                // 已改变直接修改
                entityModifiedField.setValue(value);
            }
        }

        // 标记为被修改状态
        if (this.getModifiedFieldsMap().isEmpty()) {
            this.setEntityStatus(EntityStatus.UnModify);
        } else {
            this.setEntityStatus(EntityStatus.Modified);
        }
    }

    /**
     * 实体字段值变更事件
     * @param fieldName   字段名称
     * @param originValue 字段原始值
     * @param value       字段当前值
     * @param  ignoreFields  忽略修改的字段列表
     *  <br/><b><i>为了兼容以前的实体类，增加忽略字段参数；该参数一般为子实体根据具体业务设置静态排除字段列表</i></b>
     */
    protected void fieldValueChanged(String fieldName, Object originValue, Object value,
                                     Set<String> ignoreFields) {
        this.fieldValueChanged(fieldName, fieldName, originValue, value, ignoreFields);
    }

    /**
     * 实体字段值变更事件
     * @param fieldName   字段名称
     * @param columnName  字段对应数据库中的列名
     * @param originValue 字段原始值
     * @param value       字段当前值
     * @param  ignoreFields  忽略修改的字段列表
     *  <br/><b><i>为了兼容以前的实体类，增加忽略字段参数；该参数一般为子实体根据具体业务设置静态排除字段列表</i></b>
     */
    protected void fieldValueChanged(String fieldName, String columnName, Object originValue, Object value,
                                     Set<String> ignoreFields) {
        // 不处理新增实体
        if (EntityStatus.New == this.getEntityStatus()) {
            return;
        }

        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("fieldValueChanged - fieldName is null or empty.");
        }

        if (ignoreFields != null && !ignoreFields.isEmpty()) {
            if (ignoreFields.contains(fieldName)) {
                // 忽略字段不需要记录状态
                return;
            }
        }

        this.fieldColumnValueChanged(fieldName, columnName, originValue, value);
    }

    /**
     * 强制标记一个字段为被修改或未修改状态<br/>主要应对部分特殊字段的状态判断处理
     * @param fieldName     字段名称
     * @param originValue   原始值
     * @param value         新值
     * @param chenaged     true标记为被修改，false标记为未被修改
     */
    protected void forceMarkFieldChanged(String fieldName, Object originValue, Object value, boolean chenaged) {
        // 不处理新增实体
        if (EntityStatus.New == this.getEntityStatus()) {
            return;
        }

        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("fieldValueChanged - fieldName is null or empty.");
        }

        if (!chenaged) {
            // 标记为未改变
            if (this.getModifiedFieldsMap().containsKey(fieldName)) {
                this.getUnModifyFieldsMap().put(fieldName, this.getModifiedFieldsMap().get(fieldName));
                this.getModifiedFieldsMap().remove(fieldName);
                return;
            }
        }

        EntityModifiedField entityModifiedField = this.getModifiedFieldsMap().get(fieldName);
        if (entityModifiedField == null) { // 当前字段未被修改过
            // 先从未变更列表中查找
            entityModifiedField = this.getUnModifyFieldsMap().get(fieldName);
            if (entityModifiedField == null) {
                entityModifiedField = new EntityModifiedField(fieldName, originValue, value);
            } else {
                // 从未修改列表中删除字段
                this.getUnModifyFieldsMap().remove(fieldName);
                entityModifiedField.setValue(value);
            }

            if (entityModifiedField != null) {
                this.getModifiedFieldsMap().put(fieldName, entityModifiedField);
            }

        } else { // 当前字段已经被修改
            entityModifiedField.setValue(value);
            this.getModifiedFieldsMap().remove(fieldName);
            this.getUnModifyFieldsMap().put(fieldName, entityModifiedField);
        }

        // 标记为被修改状态
        if (this.getModifiedFieldsMap().isEmpty()) {
            this.setEntityStatus(EntityStatus.UnModify);
        } else {
            this.setEntityStatus(EntityStatus.Modified);
        }
    }

    public Map<String, EntityModifiedField> getUnModifyFieldsMap() {
        if (unModifyFieldsMap == null) {
            unModifyFieldsMap = new HashMap<>();
        }

        return unModifyFieldsMap;
    }

    @Override
    public EntityStatus getEntityStatus() {
        return entityStatus;
    }

    @Override
    public void setEntityStatus(EntityStatus entityStatus) {
        if (entityStatus == null) {
            throw new NullPointerException("setEntityStatus -  param entityStatus is null.");
        }

        this.entityStatus = entityStatus;
    }

    @Override
    public Map<String, EntityModifiedField> getModifiedFieldsMap() {
        if (modifiedFieldsMap == null) {
            modifiedFieldsMap = new HashMap<>();
        }

        return modifiedFieldsMap;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.fieldColumnValueChanged(CREATEDATE, COL_CREATEDATE, this.getCreateDate(), createDate);
        super.setCreateDate(createDate);
    }

    @Override
    public void setModifiedDate(Date modifiedDate) {
        this.fieldColumnValueChanged(MODIFIEDDATE, COL_MODIFIEDDATE, this.getModifiedDate(), modifiedDate);
        super.setModifiedDate(modifiedDate);
    }



}
