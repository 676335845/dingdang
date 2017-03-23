package com.heyi.framework.orm.helper;

/**
 * 构建实体对象接口
 */
public interface BuildEntityable<PK, T extends EntityHelperable<PK>, M> {
    /**
     * 使用其它对象更新实体对象
     * @param entity   实体对象
     * @param model    用于更新实体对象的其它对象
     */
    void mergeEntity(T entity, M model);

    /**
     * 使用其它对象构建实体对象
     * @param model   数据来源对象
     * @return   新建的实体对象
     */
    T buildEntity(M model);
    
    /**
     * 合并数据之前触发事件
     * @param entity 实体对象
     * @param model  用于更新实体对象的其它对象
     * @return true 继续合并，false 取消合并
     */
    boolean beforeMergeEntity(T entity, M model);
}
