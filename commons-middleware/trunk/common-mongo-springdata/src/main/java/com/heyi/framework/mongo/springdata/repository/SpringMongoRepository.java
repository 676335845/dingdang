package com.heyi.framework.mongo.springdata.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.heyi.framework.model.PageData;
import com.heyi.framework.mongo.springdata.entity.SpringMongoEntity;
import com.mongodb.WriteResult;

/**
 * 统一repository基类，以便将来可以扩展
 */
@NoRepositoryBean
public interface SpringMongoRepository<T extends SpringMongoEntity<ID>, ID extends Serializable>
		extends MongoRepository<T, ID> {

	/**
	 * 根据主键查询批次查询实体列表
	 * 
	 * @param ids 主键列表
	 * @return
	 */
	List<T> findByIds(Set<ID> ids);

	/**
	 * 根据主键查询批次查询实体列表
	 *
	 * @param ids 主键列表
	 * @return
	 */
	PageData<T> findPageByIds(Set<ID> ids, com.heyi.framework.model.Pageable pageable);

	/**
	 * 根据条件查询实体列表
	 * 
	 * @param criterias 条件列表
	 * @return 实体列表
	 */
	List<T> find(Criteria... criterias);

	/**
	 * 根据条件查询实体列表
	 * 
	 * @param query 条件对象
	 * @return 实体列表
	 */
	List<T> find(Query query);

	// /**
	// * 根据条件查询对象列表
	// *
	// * @param query 条件对象
	// * @return 实体列表
	// */
	// <M> List<M> find(Query query,Class<M> modelClass);

	/**
	 * 以分页方式查询实体列表
	 * 
	 * @param page 以分页相关的参数对象
	 * @param fields 需要排除或包含的字段列表
	 * @param include true包含字段，false排除字段
	 * @param criterias 条件列表
	 * @return 指定页次的数据列表
	 */
	Page<T> findByParamFields(Pageable page, String[] fields, boolean include, Criteria... criterias);

	/**
	 * 以分页方式查询实体列表
	 *
	 * @param page 以分页相关的参数对象
	 * @param fields 需要排除或包含的字段列表
	 * @param include true包含字段，false排除字段
	 * @param criterias 条件列表
	 * @return 指定页次的数据列表
	 */
	PageData<T> findByParamFields(com.heyi.framework.model.Pageable page, String[] fields, boolean include,
			Criteria... criterias);

	/**
	 * 查询满足条件的实例列表
	 * 
	 * @param fields 需要排除或包含的字段列表
	 * @param include true包含字段，false排除字段
	 * @param criterias 条件列表
	 * @return 满足条件的实体列表
	 */
	List<T> findByParamFields(String[] fields, boolean include, Criteria... criterias);

	/**
	 * 根据条件查询实体对象
	 * 
	 * @param query 组装后的查询条件对象
	 * @return 满足条件的一个实体对象
	 */
	T findOne(Query query);

	/**
	 * 更新或插入字段
	 * 
	 * @param query 更新的查询条件对象
	 * @param update 需要更新的字段对象
	 * @return 执行结构
	 */
	WriteResult upsert(Query query, Update update);

	/**
	 * 更新满足条件的数据
	 * 
	 * @param query 查询条件
	 * @param update 更新内容
	 * @param multiUpdate true更新所有满足条件的数据，false只更新找到的第一笔数据
	 * @return 更新的数据影响行数
	 */
	int update(Query query, Update update, boolean multiUpdate);

	/**
	 * 更新满足条件的第一笔数据
	 * 
	 * @param query 查询条件
	 * @param update 更新内容
	 * @return 更新的数据影响行数
	 */
	int update(Query query, Update update);

	/**
	 * 根据字段更新状态更新实体
	 * 
	 * @param entity 被修改的实体
	 * @return 更新的行数
	 */
	int update(T entity);

	/**
	 * 批次更新数据到数据库中
	 * 
	 * @param newEntities 新增实体列表
	 * @param updateEntities 更新实体列表，需要根据字段状态生成更新语法
	 * @param deletedEntities 删除实体列表
	 * @return 操作成功的数据行数
	 */
	int bulkSave(List<T> newEntities, List<T> updateEntities, List<T> deletedEntities);

	/**
	 * 批次更新数据到数据库中
	 *
	 * @param newEntities 新增实体列表
	 * @param updateEntities 更新实体列表，需要根据字段状态生成更新语法
	 * @return 操作成功的数据行数
	 */
	int bulkSave(List<T> newEntities, List<T> updateEntities);

	/**
	 * 批次更新数据到数据库中
	 * 
	 * @param newEntities 新增实体列表
	 * @param updateEntities 更新实体列表，需要根据字段状态生成更新语法
	 * @param deletedIds 删除实体ID列表
	 * @return 操作成功的数据行数
	 */
	int bulkSaveWithIds(List<T> newEntities, List<T> updateEntities, Set<ID> deletedIds);

	/**
	 * 批次新增实体
	 * 
	 * @param newEntities 新增实体列表
	 * @return 新增数量
	 */
	int bulkInsert(List<T> newEntities);

	/**
	 * 批次更新实体
	 * 
	 * @param updateEntities 更新实体列表，需要根据字段状态生成更新语法
	 * @return 更新成功的笔数
	 */
	int bulkUpdate(List<T> updateEntities);

	/**
	 * 根据主键列表批次删除实体
	 * 
	 * @param primaryKeys 删除实体ID列表
	 * @return 返回删除成功的笔数
	 */
	int bulkDelete(Set<ID> primaryKeys);
}
