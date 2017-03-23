package com.heyi.framework.orm.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heyi.framework.model.PrimaryKey;
import com.heyi.framework.orm.entity.EntityStatus;

/**
 * 实体管理器
 * @param <PK>  实体主键类型
 * @param <T>   实现了{@link EntityHelperable}接口的实体
 */
public abstract class EntityHelper<PK, T extends EntityHelperable<PK>> {
    private static Logger logger = LoggerFactory.getLogger(EntityHelper.class);

    /**
     * 每次从数据库加载实体的笔数，默认为500
     */
    private int rowsPerBatchQuery;
    /**
     * 需要处理的实体列表
     */
    private List<T> entities = null;
    private Map<PK, T> entitiesMap = null;
    
    /**
     * 需要更新的实体列表
     */
    private List<T> updateEntities = null;
    
    /**
     * 需要新增的实体列表
     */
    private List<T> insertEntities = null;
    
    /**
     * 忽略处理的实体列表
     */
    private List<T> ignoreEntities = null;

    /**
     * 是否由数据库生成主键
     */
    private boolean generatePrimaryKeyByDB;

    private EntityHelperRepository<PK, T> entityHelperRepository = null;
    /**
     * 与平台相关的企业号，如悦工作、微信钉钉等企业号
     */
    private String corpId = null;
    /**
     * 是否在整个处理过程中使用平台相关企业号
     */
    private boolean dealWithCorpId;

    protected EntityHelper() {
        super();

        this.rowsPerBatchQuery = 500;
        this.entities = new LinkedList<>();
        this.updateEntities = new LinkedList<>();
        this.insertEntities = new LinkedList<>();
        this.ignoreEntities = new LinkedList<>();
        this.entitiesMap = new LinkedHashMap<>();
        this.generatePrimaryKeyByDB = false;
        this.corpId = null;
        this.dealWithCorpId = false;
    }

    public EntityHelper(boolean generatePrimaryKeyByDB, EntityHelperRepository<PK, T> entityHelperRepository) {
        this();

        if (entityHelperRepository == null) {
            throw new NullPointerException("entityHelperRepository is null.");
        }

        this.generatePrimaryKeyByDB = generatePrimaryKeyByDB;
        this.entityHelperRepository = entityHelperRepository;

    }
    
    
    public List<T> getIgnoreEntities() {
    	return this.ignoreEntities;
    }

    /**
     *
     * @return 需要更新的实体列表
     */
    public List<T> getUpdateEntities() {
//        List<T> updateEntities = new LinkedList<>();
//
//        if (this.updateCount == 0) {
//            return updateEntities;
//        }
//
//        for (T t : entities) {
//            if (EntityStatus.Modified == ((EntityStatusable) t).getEntityStatus()) {
//                updateEntities.add(t);
//            }
//        }

        return this.updateEntities;
    }

    /**
     *
     * @return 新增实体列表
     */
    public List<T> getNewEntities() {
//        List<T> newEntities = new LinkedList<>();
//
//        if (this.insertCount == 0) {
//            return newEntities;
//        }
//
//        for (T t : entities) {
//            if (EntityStatus.New == ((EntityStatusable) t).getEntityStatus()) {
//                newEntities.add(t);
//            }
//        }

        return this.insertEntities;
    }

    /**
     * 根据主键列表分批从数据库里加载数据实体
     */
    protected void loadEntities(List<? extends PrimaryKey<PK>> primaryKeys) {

        if (primaryKeys.isEmpty()) {
            return;
        }

        if (this.dealWithCorpId && StringUtils.isBlank(corpId)) {
            throw new IllegalStateException("当dealWithCorpId设置为true时，必须指定与平台相关的企业号。");
        }

        // 生成需要从数据库中加载实体的ID列表
        Set<PK> ids = new LinkedHashSet<>();
        PrimaryKey<PK> primaryKeyObject = null;
        PK primaryKey = null;
        for (int i = 0; i < primaryKeys.size(); i++) {
            primaryKeyObject = primaryKeys.get(i);
            if (primaryKeyObject == null) {
                throw new NullPointerException("索引为" + i + "的主键对象为null.");
            }

            primaryKey = primaryKeyObject.getId();
            if (primaryKey == null) {
                //略过新增对象
                continue;
            }
            // 将ID加入到列表中
            ids.add(primaryKey);
        }

        // 分批加载数据实体
        if (!ids.isEmpty()) {
            Set<PK> queryIds = new LinkedHashSet<>();
            for (PK pk : ids) {
                queryIds.add(pk);
                // 如果ID数量达到指定的值则触发一次查询
                if (queryIds.size() == rowsPerBatchQuery) {
                    if (this.dealWithCorpId) {
                        this.entities.addAll(this.entityHelperRepository.loadEntitiesByIds(this.corpId, queryIds));
                    } else {
                        this.entities.addAll(this.entityHelperRepository.loadEntitiesByIds(queryIds));
                    }

                    queryIds.clear();// 清除已经使用的
                }
            }

            // 如果查询ID列表不为空则再执行一次加载
            if (!queryIds.isEmpty()) {
                if (this.dealWithCorpId) {
                    this.entities.addAll(this.entityHelperRepository.loadEntitiesByIds(corpId, queryIds));
                } else {
                    this.entities.addAll(this.entityHelperRepository.loadEntitiesByIds(queryIds));
                }
                queryIds.clear();// 清除已经使用的
            }
        }

        // 将实体根据ID索引到map中
        for (T t : this.entities) {
            //将状态设置未修改
            t.setEntityStatus(EntityStatus.UnModify);

            this.entitiesMap.put(t.getId(), t);
        }
    }

    public int getRowsPerBatchQuery() {
        return rowsPerBatchQuery;
    }

    public void setRowsPerBatchQuery(int rowsPerBatchQuery) {
        this.rowsPerBatchQuery = rowsPerBatchQuery;
    }

    /**
     * 根据主键查找实体对象
     * @param primaryKey  主键
     * @return  实体对象
     */
    public T findByPrimaryKey(PK primaryKey) {
        if (primaryKey == null) {
            throw new NullPointerException("primaryKey is null.");
        }

        return this.entitiesMap.get(primaryKey);
    }

    /**
     * 将模型数据与实体对象的数据进行合并
     * @param model    来源数据模型对象
     * @param entityBuilder   实体构造器
     * @param <M>  数据来源类型
     */
    public <M extends PrimaryKey<PK>>  void merge(M model, BuildEntityable<PK, T, M> entityBuilder, boolean isInit) {
        if (model == null) {
            throw new NullPointerException("merge - param model is null.");
        }

        if (entityBuilder == null) {
            throw new NullPointerException("merge - param entityBuilder is null.");
        }

        List<M> models = new ArrayList<>(1);
        models.add(model);

        this.merge(models, entityBuilder, isInit);
    }
    
    /**
     * 将模型数据与实体对象的数据进行合并
     * @param models	来源数据模型对象列表
     * @param entityBuilder	实体构造器
     * @param <M> 数据来源类型
     */
    public <M extends PrimaryKey<PK>>  void merge(List<M> models, BuildEntityable<PK, T, M> entityBuilder) {
    	this.merge(models, entityBuilder, false);
    }

    /**
     * 将模型数据与实体对象的数据进行合并
     * @param models  来源数据模型对象列表
     * @param entityBuilder 实体构造器
     * @param isInit  是否初始化同步
     * @param <M> 数据来源类型
     */
    public <M extends PrimaryKey<PK>>  void merge(List<M> models, BuildEntityable<PK, T, M> entityBuilder, boolean isInitSync) {
        if (models == null) {
            throw new NullPointerException("merge - param models is null.");
        }

        if (entityBuilder == null) {
            throw new NullPointerException("merge - param entityBuilder is null.");
        }

        // 清除本地缓存
        this.clearCache();

        // 从数据库加载需要合并的数据实体
        long start = System.currentTimeMillis();
        this.loadEntities(models);
        if (logger.isDebugEnabled()) {
            logger.debug("加载数据共耗时(ms):{}", System.currentTimeMillis() - start);
        }

        // 循环合并数据
        T entity = null;
        PK primaryKey = null;
        start = System.currentTimeMillis();
        for (M model : models) {
            if (models == null) {
                continue;
            }

            primaryKey = model.getId();
            if (primaryKey == null) {
                entity = null;
            } else {
                entity = this.findByPrimaryKey(primaryKey);
            }

            if (entity == null) {
                // 新增实体
            	if(isInitSync && !entityBuilder.beforeMergeEntity(entity, model)){//首次同步新增实体无效数据则跳过
            		this.ignoreEntities.add(entity);
            		continue;
            	}
                entity = entityBuilder.buildEntity(model);
                if (entity == null) {
                    throw new IllegalStateException("实体创建失败。");
                }
                // 将新增实体追加到列表中
                this.insertEntities.add(entity);
//                this.entities.add(entity);
                if (entity.getId() != null) {
                    this.entitiesMap.put(entity.getId(), entity);
                } else {
                    if (!this.isGeneratePrimaryKeyByDB()) {
                        throw new IllegalStateException("新建实体主键不允许数据库自动创建，请显示设置主键值。");
                    }
                }
            } else {
                // 合并实体
                entityBuilder.mergeEntity(entity, model);
                if(entity.getEntityStatus() == EntityStatus.Modified){
                	this.updateEntities.add(entity);
                }else {
                	this.ignoreEntities.add(entity);
                 }
            }
            
        }

        if (logger.isDebugEnabled()) {
            logger.debug("合并数据共耗时(ms):{}", System.currentTimeMillis() - start);
        }
    }

    /**
     * 清除所有本地缓存
     */
    protected void clearCache() {
        this.entities.clear();
        this.insertEntities.clear();
        this.updateEntities.clear();
        this.ignoreEntities.clear();
        this.entitiesMap.clear();
        this.corpId = null;
        this.dealWithCorpId = false;
    }

    public boolean isGeneratePrimaryKeyByDB() {
        return generatePrimaryKeyByDB;
    }

    public void setGeneratePrimaryKeyByDB(boolean generatePrimaryKeyByDB) {
        this.generatePrimaryKeyByDB = generatePrimaryKeyByDB;
    }


    /**
     * 将变更的实体保存到数据库中
     * @return 保存到数据库中的实体数量
     */
    public int flush() {
        // 新增实体列表
       //List<T> newEntities = this.getNewEntities();

        // 更新实体列表
       //List<T> updateEntities = this.getUpdateEntities();

        if (this.dealWithCorpId) {
            if (StringUtils.isBlank(corpId)) {
                throw new IllegalStateException("当dealWithCorpId设置为true时，必须指定与平台相关的企业号。");
            }

            return this.entityHelperRepository.flush(corpId, this.getNewEntities(), this.getUpdateEntities());
        } else {
            return this.entityHelperRepository.flush(this.getNewEntities(), this.getUpdateEntities());
        }
    }

    public int getInsertCount() {
        return this.getNewEntities().size();
    }

    public int getUpdateCount() {
        return this.getUpdateEntities().size();
    }
    
    public int getIgnoreCount() {
    	return this.getIgnoreEntities().size();
    }

    public void setDealWithCorpId(boolean dealWithCorpId) {
        this.dealWithCorpId = dealWithCorpId;
    }

    public void setCorpId(String corpId) {
        if (StringUtils.isBlank(corpId)) {
            throw new IllegalArgumentException("setCorpId - param corpId is null or empty.");
        }
        this.corpId = corpId;
    }
}
