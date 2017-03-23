package com.heyi.framework.orm.helper;

import java.util.List;
import java.util.Set;

/**
 * 供实体管理器加载实体的接口
 * @param <PK>   实体主键类型
 * @param <PT>    实现了{@link EntityHelperable}接口的实体类型
 */
public interface EntityHelperRepository<PK, PT extends EntityHelperable<PK>> {

    /**
     * 通过主键加载对应实体
     * <br/>从数据库加载出来的实体需要将状态设置为{@link me.ywork.orm.entity.EntityStatus#UnModify}
     * @param ids  主键列表
     * @return  主键对应的实体列表
     */
     List<PT> loadEntitiesByIds(Set<PK> ids);

    /**
     * 通过主键加载对应实体
     * <br/>从数据库加载出来的实体需要将状态设置为{@link me.ywork.orm.entity.EntityStatus#UnModify}
     * @param corpId 与平台相关的企业号
     * @param ids  主键列表
     * @return  主键对应的实体列表
     */
    List<PT> loadEntitiesByIds(String corpId, Set<PK> ids);

    /**
     * 将列表中的实体保存到数据库
     * @param newEntities    新增的实体列表
     * @param updateEntities 被更新的实体列表
     * @return  保存成功的实体数量
     */
    int flush(List<PT> newEntities, List<PT> updateEntities);

    /**
     * 将列表中的实体保存到数据库
     * @param corpId         与平台相关的企业号
     * @param newEntities    新增的实体列表
     * @param updateEntities 被更新的实体列表
     * @return  保存成功的实体数量
     */
    int flush(String corpId, List<PT> newEntities, List<PT> updateEntities);
}
