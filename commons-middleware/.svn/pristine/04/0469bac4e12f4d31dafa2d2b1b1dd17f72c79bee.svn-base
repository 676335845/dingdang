package com.heyi.framework.mongo.springdata.repository.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.util.Assert;

import com.heyi.framework.exception.UnImplementedException;
import com.heyi.framework.exception.UnSupportedException;
import com.heyi.framework.model.PageData;
import com.heyi.framework.model.PageDataImpl;
import com.heyi.framework.mongo.springdata.DefaultMongoOperations;
import com.heyi.framework.mongo.springdata.entity.SpringMongoEntity;
import com.heyi.framework.mongo.springdata.repository.SpringMongoRepository;
import com.heyi.framework.orm.entity.EntityStatus;
import com.heyi.framework.orm.helper.EntityModifiedField;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;

/**
 * 使用spring data操作mongodb DAO基础实现类 
 */
public abstract class SpringMongoRepositoryImpl<T extends SpringMongoEntity<ID>, ID extends Serializable> // extends
		// SimpleMongoRepository<T,
		// ID>
		implements SpringMongoRepository<T, ID> {
	private static final Logger logger = LoggerFactory.getLogger(SpringMongoRepositoryImpl.class);

	@Autowired
	protected DefaultMongoOperations mongoOperations;

	// @Autowired
	protected MongoEntityInformation<T, ID> entityInformation;

	public SpringMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
		// super(metadata, mongoOperations);

		this.entityInformation = metadata;
		// this.mongoOperations = mongoOperations;
	}

	public SpringMongoRepositoryImpl() {

	}

	protected abstract Class<T> getEntityClass();


	public <S extends T> S save(S entity) {

		Assert.notNull(entity, "Entity must not be null!");

		/*
		 * if (entityInformation.isNew(entity)) { mongoOperations.insert(entity,
		 * entityInformation.getCollectionName()); } else {
		 * mongoOperations.save(entity, entityInformation.getCollectionName());
		 * }
		 */
		mongoOperations.save(entity);

		this.resetEntityUnModify(entity);

		return entity;
	}

	public <S extends T> List<S> save(Iterable<S> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		List<S> result = convertIterableToList(entities);
		/*
		 * boolean allNew = true;
		 * 
		 * for (S entity : entities) { if (allNew &&
		 * !entityInformation.isNew(entity)) { allNew = false; } }
		 * 
		 * if (allNew) { mongoOperations.insertAll(result); } else {
		 * 
		 * for (S entity : result) { save(entity); } }
		 */
		for (S entity : result) {
			save(entity);
			this.resetEntityUnModify(entity);
		}

		return result;
	}

	public T findOne(ID id) {
		Assert.notNull(id, "The given id must not be null!");
		T entity = mongoOperations.findById(id, this.getEntityClass());
		this.resetEntityUnModify(entity);

		return entity;
		// return mongoOperations.findById(id, entityInformation.getJavaType(),
		// entityInformation.getCollectionName());
	}

	public boolean exists(ID id) {

		Assert.notNull(id, "The given id must not be null!");
		/*
		 * return mongoOperations.exists(getIdQuery(id),
		 * entityInformation.getJavaType(),
		 * entityInformation.getCollectionName());
		 */

		return false;
	}


	public long count() {
		// return
		// mongoOperations.getCollection(entityInformation.getCollectionName()).count();
		// return mongoOperations.getCollection(collectionName.substring(0,
		// 1).toLowerCase() + collectionName.substring(1)).count();
		return mongoOperations.count(new Query(), getEntityClass());
	}

	public long count(Query query) {
		return mongoOperations.count(query, getEntityClass());
	}


	public void delete(ID id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoOperations.remove(new Query().addCriteria(new Criteria("id").is(id)), getEntityClass());
		// mongoOperations.remove(getIdQuery(id),
		// entityInformation.getJavaType(),
		// entityInformation.getCollectionName());
	}

	public void delete(T entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		mongoOperations.remove(entity);
		// delete(entityInformation.getId(entity));
	}

	public void delete(Iterable<? extends T> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		Set<ID> ids = new HashSet<>();
		for (T entity : entities) {
			//delete(entity);
			ids.add(entity.getId());
		}

		// 批次删除
		this.bulkDelete(ids);
	}


	public void deleteAll() {
		// mongoOperations.remove(new Query(),
		// entityInformation.getCollectionName());
		throw new UnImplementedException("the method deleteAll UnImplements");
	}


	public List<T> findAll() {
		List<T> entities =  findAll(new Query());

		this.resetEntityUnModify(entities);

		return entities;

//		throw new UnSupportedException("不支持查询所有数据。");
	}


	public Iterable<T> findAll(Iterable<ID> ids) {

		Set<ID> parameters = new HashSet<ID>(tryDetermineRealSizeOrReturn(ids, 10));
		for (ID id : ids) {
			parameters.add(id);
		}

		// return findAll(new Query(new
		// Criteria(entityInformation.getIdAttribute()).in(parameters)));

		return null;
	}


	public Page<T> findAll(final Pageable pageable) {
		Long count = count();
		List<T> list = findAll(new Query().with(pageable));
		this.resetEntityUnModify(list);
		return new PageImpl<T>(list, pageable, count);
	}


	public List<T> findAll(Sort sort) {
		List<T> entities = findAll(new Query().with(sort));

		this.resetEntityUnModify(entities);

		return entities;
	}


	@Override
	public <S extends T> S insert(S entity) {

		Assert.notNull(entity, "Entity must not be null!");

		 /*mongoOperations.insert(entity,
		 entityInformation.getCollectionName());*/
		this.mongoOperations.insert(entity);

		this.resetEntityUnModify(entity);

		return entity;
	}

	/**
	 * 根据查询器查询
	 *
	 * @param page
	 * @param criterias
	 * @return
	 */
	public Page<T> findByParam(Pageable page, Criteria... criterias) {
		Query query = new Query();
		if (criterias != null && criterias.length != 0) {
			for (int i = 0; i < criterias.length; i++)
				query.addCriteria(criterias[i]);
		}
		long count = count(query);
		if (page!=null) query.with(page);
		List<T> list = mongoOperations.find(query, getEntityClass());

		this.resetEntityUnModify(list);

		return new PageImpl<T>(list, page, count);
	}

	@Override
	public Page<T> findByParamFields(Pageable page, String[] fields, boolean include, Criteria... criterias) {
		Query query = new Query();
		if (criterias != null && criterias.length != 0) {
			for (int i = 0; i < criterias.length; i++)
				query.addCriteria(criterias[i]);
		}
		this.buildQueryFields(fields, include, query);

		long count = count(query);
		if (page!=null) query.with(page);
		List<T> list = mongoOperations.find(query, getEntityClass());

		this.resetEntityUnModify(list);

		return new PageImpl<T>(list, page, count);
	}

	private void buildQueryFields(String[] fields, boolean include, Query query) {
		if (fields == null) {
			throw new NullPointerException("buildQueryFields - param buildQueryFields is null.");
		}

		if (fields.length == 0) {
			return;
		}

		if (query == null) {
			throw new NullPointerException("buildQueryFields - param query is null.");
		}

		if (fields != null && fields.length > 0) {
			for (int i = 0; i < fields.length; i++) {
				if (include) {
					query.fields().include(fields[i]);
				} else {
					query.fields().exclude(fields[i]);
				}
			}
		}
	}

	@Override
	public PageData<T> findByParamFields(com.heyi.framework.model.Pageable page, String[] fields, boolean include, Criteria... criterias) {

		Query query = new Query();
		if (criterias != null && criterias.length != 0) {
			for (int i = 0; i < criterias.length; i++)
				query.addCriteria(criterias[i]);
		}

		this.buildQueryFields(fields, include, query);

		long count = count(query);

		page.setTotalCount(count);

		if (page != null) {
			query.skip((page.getPageNo() - 1) * page.getPageSize());
			query.limit(page.getPageSize());
		}

		List<T> entities = this.mongoOperations.find(query, getEntityClass());

		this.resetEntityUnModify(entities);

		return new PageDataImpl(entities, page);
	}

	@Override
	public List<T> findByParamFields(String[] fields, boolean include, Criteria... criterias) {
		Query query = new Query();
		if (criterias != null && criterias.length != 0) {
			for (int i = 0; i < criterias.length; i++)
				query.addCriteria(criterias[i]);
		}

		this.buildQueryFields(fields, include, query);

		List<T> entities = this.mongoOperations.find(query, getEntityClass());

		this.resetEntityUnModify(entities);

		return entities;
	}

	@Override
	public List<T> findByIds(Set<ID> ids) {
		if (ids == null) {
			throw new NullPointerException("findByIds - param ids is null.");
		}

		if (ids.isEmpty()) {
			return new ArrayList<>();
		}

		Criteria criteria = new Criteria("_id").in(ids);
		List<T> entities = this.find(criteria);

		this.resetEntityUnModify(entities);

		return entities;
	}

	@Override
	public PageData<T> findPageByIds(Set<ID> ids, com.heyi.framework.model.Pageable pageable) {
			if (ids == null) {
			throw new NullPointerException("findByIds - param ids is null.");
		}

		if (ids.isEmpty()) {
			return new PageDataImpl<>(Collections.EMPTY_LIST,pageable);
		}

		Criteria criteria = new Criteria("_id").in(ids);
		Query query = new Query(criteria);
		if (pageable != null) {
			query.skip((pageable.getPageNo() - 1) * pageable.getPageSize());
			query.limit(pageable.getPageSize());
		}
		if (pageable.getTotalCount() <= 0) pageable.setTotalCount(this.count(query));

		List<T> entities = this.find(query);

		this.resetEntityUnModify(entities);

		return new PageDataImpl<>(entities,pageable);
	}

	@Override
	public List<T> find(Criteria... criterias) {
		if (criterias == null) {
			throw new NullPointerException("findByParam - param criterias is null.");
		}

		if (criterias.length == 0) {
			return new ArrayList<>();
		}

		Query query = new Query();
		if (criterias != null && criterias.length != 0) {
			for (int i = 0; i < criterias.length; i++)
				query.addCriteria(criterias[i]);
		}

		List<T> entities =  this.find(query);

		this.resetEntityUnModify(entities);

		return entities;
	}

	@Override
	public List<T> find(Query query) {
		if (query == null) {
			throw new NullPointerException("find - param query is null.");
		}

		List<T> entities = this.mongoOperations.find(query, this.getEntityClass());

		this.resetEntityUnModify(entities);

		return entities;
	}


	@Override
	public <S extends T> List<S> insert(Iterable<S> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		List<S> list = convertIterableToList(entities);

		if (list.isEmpty()) {
			return list;
		}

		mongoOperations.insertAll(list);

		this.resetEntityUnModify(list);

		return list;
	}

	private List<T> findAll(Query query) {

		if (query == null) {
			return Collections.emptyList();
		}

		// return mongoOperations.find(query, entityInformation.getJavaType(),
		// entityInformation.getCollectionName());
		List<T> entities = mongoOperations.find(query, this.getEntityClass());

		this.resetEntityUnModify(entities);

		return entities;
	}

	private static <T> List<T> convertIterableToList(Iterable<T> entities) {

		if (entities instanceof List) {
			return (List<T>) entities;
		}

		int capacity = tryDetermineRealSizeOrReturn(entities, 10);

		if (capacity == 0 || entities == null) {
			return Collections.<T> emptyList();
		}

		List<T> list = new ArrayList<T>(capacity);
		for (T entity : entities) {
			list.add(entity);
		}

		return list;
	}

	private static int tryDetermineRealSizeOrReturn(Iterable<?> iterable, int defaultSize) {
		return iterable == null ? 0
				: (iterable instanceof Collection) ? ((Collection<?>) iterable).size() : defaultSize;
	}

	@Override
	public T findOne(Query query) {
		if (query == null) {
			throw new NullPointerException("findOne - param query is null.");
		}

		query.limit(1);

		T entity = this.mongoOperations.findOne(query, this.getEntityClass());

		this.resetEntityUnModify(entity);

		return entity;
	}

	@Override
	public WriteResult upsert(Query query, Update update) {
		if (query == null) {
			throw new NullPointerException("upsert - param query is null.");
		}

		if (update == null) {
			throw new NullPointerException("upsert - param update is null.");
		}

		return this.mongoOperations.upsert(query, update, this.getEntityClass());
	}

	@Override
	public int update(Query query, Update update, boolean multiUpdate) {
		if (query == null) {
			throw new NullPointerException("update - param query is null.");
		}

		if (update == null) {
			throw new NullPointerException("update - param update is null.");
		}

		WriteResult writeResult = null;
		if (multiUpdate) {
			writeResult = this.mongoOperations.updateMulti(query, update, this.getEntityClass());
		} else {
			writeResult = this.mongoOperations.updateFirst(query, update, this.getEntityClass());
		}

		if (writeResult == null) {
			if (multiUpdate) {
				throw new IllegalStateException("call mongoOperations.updateMulti return null.");
			} else {
				throw new IllegalStateException("call mongoOperations.updateFirst return null.");
			}
		}

		return writeResult.getN();
	}

	@Override
	public int update(Query query, Update update) {
		if (query == null) {
			throw new NullPointerException("update - param query is null.");
		}

		if (update == null) {
			throw new NullPointerException("update - param update is null.");
		}

		return update(query, update, false);
	}

	@Override
	public int bulkSave(List<T> newEntities, List<T> updateEntities) {
		if (newEntities == null) {
			throw new NullPointerException("bulkSave - param newEntities is null.");
		}

		if (updateEntities == null) {
			throw new NullPointerException("bulkSave - param updateEntities is null.");
		}

		return this.bulkSaveWithIds(newEntities, updateEntities, new HashSet<ID>());
	}

	@Override
	public int bulkSave(List<T> newEntities, List<T> updateEntities, List<T> deletedEntities) {
		if (newEntities == null) {
			throw new NullPointerException("bulkSave - param newEntities is null.");
		}

		if (updateEntities == null) {
			throw new NullPointerException("bulkSave - param updateEntities is null.");
		}

		if (deletedEntities == null) {
			throw new NullPointerException("bulkSave - param deletedEntities is null.");
		}

		if (newEntities.isEmpty() && updateEntities.isEmpty() && deletedEntities.isEmpty()) {
			return 0;
		}

		Set<ID> ids = new HashSet<>(deletedEntities.size());
		for (T entity : deletedEntities) {
			if (entity == null) {
				ids.add(entity.getId());
			}
		}

		return this.bulkSaveWithIds(newEntities, updateEntities, ids);
	}

	@Override
	public int bulkSaveWithIds(List<T> newEntities, List<T> updateEntities, Set<ID> deletedIds) {
		if (newEntities == null) {
			throw new NullPointerException("bulkSaveWithIds - param newEntities is null.");
		}

		if (updateEntities == null) {
			throw new NullPointerException("bulkSaveWithIds - param updateEntities is null.");
		}

		if (deletedIds == null) {
			throw new NullPointerException("bulkSaveWithIds - param deletedIds is null.");
		}

		// 统计需要处理的列表参数个数
		final int listCount = (newEntities.isEmpty() ? 0 : 1)
				+ (updateEntities.isEmpty() ? 0 : 1);
		
		if (listCount == 0 && deletedIds.isEmpty()) {
			return 0;
		}
		
		// 生成批处理对象
		BulkWriteOperation bulkWriteOperation = null;
		if (listCount > 0) {
			// 需要执行批处理
			bulkWriteOperation = this.mongoOperations.buildBulkWriteOperation(false,
					this.getEntityClass());
		}

		// 分别生成批处理语法
		int insertCount = 0;
		if (!newEntities.isEmpty()) {
			if (bulkWriteOperation == null) {
				return this.bulkInsert(newEntities); // 其它列表为空，直接执行批次插入
			} else {
				insertCount = this.mongoOperations.bulkInsertDocument(bulkWriteOperation, newEntities);
			}
		}

		int updateCount = 0;
		if (!updateEntities.isEmpty()) {
			if (bulkWriteOperation == null) {
				return this.bulkUpdate(updateEntities); // 其它列表为空，直接执行批次更新
			} else {
				updateCount = this.buildBulkUpdateWriteRequest(bulkWriteOperation, updateEntities);
			}
		}

		int deleteCount = 0;
		if (!deletedIds.isEmpty()) {
			if (bulkWriteOperation == null) {
				return this.bulkDelete(deletedIds); // 其它列表为空，直接执行批次删除
			} else {
				Criteria criteria = new Criteria("_id").in(deletedIds);
				Query query = new Query(criteria);
				bulkWriteOperation.find(query.getQueryObject()).remove();
				deleteCount = deletedIds.size();
			}
		}

		if (insertCount + updateCount + deleteCount == 0) {
			return 0;
		}

		BulkWriteResult bulkWriteResult = bulkWriteOperation.execute();
		if (bulkWriteResult == null) {
			throw new IllegalStateException("执行批处理返回null.");
		}

		if (bulkWriteResult.getInsertedCount() != insertCount) {
			if (logger.isWarnEnabled()) {
				logger.warn("MongoDB数据可能出现不一致问题，批次插入的数量[{}]与传入的数量不匹配[{}]",
						bulkWriteResult.getInsertedCount(), insertCount);
			}
		}

		if (bulkWriteResult.getMatchedCount() != updateCount) {
			if (logger.isWarnEnabled()) {
				logger.warn("MongoDB数据可能出现不一致问题，批次更新的数量[{}]与传入的数量不匹配[{}]",
						bulkWriteResult.getMatchedCount(), updateCount);
			}
		}

		if (bulkWriteResult.getRemovedCount() != deleteCount) {
			if (logger.isWarnEnabled()) {
				logger.warn("MongoDB数据可能出现不一致问题，批次删除的数量[{}]与传入的数量不匹配[{}]",
						bulkWriteResult.getRemovedCount(), deleteCount);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("批次处理完成，处理结果为（成功数量/提交总量）：插入[{}/{}], 更新[{}/{}], 删除[{}/{}]",
					bulkWriteResult.getInsertedCount(), insertCount, bulkWriteResult.getMatchedCount(), updateCount,
					bulkWriteResult.getRemovedCount(), deleteCount);
		}
		
		return bulkWriteResult.getInsertedCount() + bulkWriteResult.getMatchedCount()
				+ bulkWriteResult.getRemovedCount();
	
	}

	@Override
	public int bulkInsert(List<T> newEntities) {
		if (newEntities == null) {
			throw new NullPointerException("bulkInsert - param newEntities is null.");
		}

		if (newEntities.isEmpty()) {
			return 0;
		}

		newEntities = this.insert(newEntities);

		return newEntities.size();
	}

	@Override
	public int bulkUpdate(List<T> updateEntities) {
		if (updateEntities == null) {
			throw new NullPointerException("bulkUpdate - param updateEntities is null.");
		}

		if (updateEntities.isEmpty()) {
			return 0;
		}
		// 如果只有一笔则直接更新，不需要使用批次更新
		if (updateEntities.size() == 1) {
			return this.update(updateEntities.get(0));
		}
		
		// 获取当前集合批处理对象
		BulkWriteOperation bulkWriteOperation = this.mongoOperations.buildBulkWriteOperation(false,
				this.getEntityClass());

		int needUpdateCount = this.buildBulkUpdateWriteRequest(bulkWriteOperation, updateEntities);
		if (needUpdateCount == 0) {
			return 0;
		}

		BulkWriteResult bulkWriteResult = bulkWriteOperation.execute();
		if (bulkWriteResult == null) {
			throw new IllegalStateException("执行批次更新操作返回null.");
		}

		return bulkWriteResult.getMatchedCount();
	}

	/**
	 * 生成批次更新对象
	 * @param bulkWriteOperation  执行批次操作的对象
	 * @param entities   需要更新的实体
     * @return  需要批次更新的笔数
     */
	protected int buildBulkUpdateWriteRequest(BulkWriteOperation bulkWriteOperation, List<T> entities) {
		if (bulkWriteOperation == null) {
			throw new NullPointerException("buildBulkUpdateWriteRequest - param bulkWriteOperation is null.");
		}

		if (entities == null) {
			throw new NullPointerException("buildBulkUpdateWriteRequest - param entities is null.");
		}

		if (entities.isEmpty()) {
			return 0;
		}
		int updateCount  = 0;
		Update update = null;
		Query query = null;
		for (T entity : entities) {
			if (entity == null) {
				continue;
			}

			update = this.buildUpdateByModifiedFields(entity);
			if (update == null) {
				// 无字段被修改，不需要更新
				continue;
			}

			query = this.buildQueryForUpdateById(entity);

			bulkWriteOperation.find(query.getQueryObject()).updateOne(update.getUpdateObject());
			updateCount ++;
		}

		return updateCount;
	}

	@Override
	public int bulkDelete(Set<ID> primaryKeys) {
		if (primaryKeys == null) {
			throw new NullPointerException("bulkDelete - param primaryKeys is null.");
		}

		if (primaryKeys.isEmpty()) {
			return 0;
		}

		Criteria criteria = new Criteria("_id").in(primaryKeys);
		Query query = new Query(criteria);

		WriteResult writeResult = this.mongoOperations.remove(query, this.getEntityClass());
		if (writeResult == null) {
			throw new IllegalStateException("批次删除数据结果返回null");
		}

		return writeResult.getN();
	}

	@Override
	public int update(T entity) {
		if (entity == null) {
			throw new NullPointerException("update - param entity is null.");
		}

		if (entity.getId() == null) {
			throw new NullPointerException("update - param entity.getId() is null.");
		}

		if (entity.getEntityStatus() == null) {
			throw new NullPointerException("update - param entity.getEntityStatus() is null.");
		}

		switch (entity.getEntityStatus()) {
			case New:
				entity = this.insert(entity);
				if (entity == null) {
					return 0;
				} else {
					return 1;
				}
			case UnModify:
				return 0;
			case Modified:
				break;
			default:
				throw new UnSupportedException("未支持的实体状态：" + entity.getEntityStatus());
		}

		Update update = this.buildUpdateByModifiedFields(entity);
		if (update == null) {
			// 未生成更新对象，说明不需要更新实体
			return 0;
		}

		Query query = this.buildQueryForUpdateById(entity);

		return this.update(query, update);
	}

	private Query buildQueryForUpdateById(T entity) {
		if (entity == null) {
			throw new NullPointerException("buildQueryForUpdateById - param entity is null.");
		}

		if (entity.getId() == null) {
			throw new NullPointerException("buildQueryForUpdateById - param entity.getId() is null.");
		}

		Criteria criteria = new Criteria("_id").is(entity.getId());
		return new Query(criteria);
	}

	protected Update buildUpdateByModifiedFields(T entity) {
		if (entity == null) {
			throw new NullPointerException("buildUpdateByModifiedFields - param entity is null.");
		}

		if (entity.getEntityStatus() == null) {
			throw new NullPointerException("buildUpdateByModifiedFields - param entity.getEntityStatus() is null.");
		}

		if (entity.getEntityStatus() != EntityStatus.Modified) {
			return null;
		}

		// 无字段更新不需要保存
		if (entity.getModifiedFieldsMap().isEmpty()) {
			return null;
		}

		// 生成更新语法
		Update update = null;
		for (EntityModifiedField entityModifiedField : entity.getModifiedFieldsMap().values()) {
			if (entityModifiedField == null) {
				continue;
			}

			if (update == null) {
				update = new Update();
				// 更新时间
				update.set("modifiedDate", entity.getModifiedDate());
			}

			update.set(entityModifiedField.getFieldName(), entityModifiedField.getValue());
		}

		return update;
	}

	/**
	 * 将实体设置为非修改状态
	 * @param entities   实体列表
     */
	protected  <S extends T> void resetEntityUnModify(Collection<S> entities) {
		if(entities == null || entities.isEmpty())
			return;
		this.resetEntityStatus(entities, EntityStatus.UnModify);
	}

	/**
	 * 重置实体状态
	 * @param entities    实体列表
	 * @param entityStatus 实体状态
	 */
	protected  <S extends T> void resetEntityStatus(Collection<S> entities, EntityStatus entityStatus) {
		if (entities == null) {
			throw new NullPointerException("resetEntityStatus - param entities is null.");
		}

		if (entityStatus == null) {
			throw new NullPointerException("resetEntityStatus - param entityStatus is null.");
		}

		// 置为未改变
		for (T entity : entities) {
			this.resetEntityStatus(entity, entityStatus);
		}
	}

	/**
	 * 将实体设置为未修改状态
	 * @param entity   需要设置状态的实体对象
     */
	protected <S extends T> void resetEntityUnModify(S entity) {
		if(entity == null)
			return;
		this.resetEntityStatus(entity, EntityStatus.UnModify);
	}

	/**
	 * 重置实体状态
	 * @param entity    实体对象
	 * @param entityStatus      实体状态
	 */
	protected <S extends T> void resetEntityStatus(S entity, EntityStatus entityStatus) {
		if (entity == null) {
			throw new NullPointerException("resetEntityStatus - param entity is null.");
		}

		if (entityStatus == null) {
			throw new NullPointerException("resetEntityStatus - param entityStatus is null.");
		}

		entity.setEntityStatus(entityStatus);
	}
}
