package me.ywork.base.repository;

import java.io.Serializable;

import org.apache.ibatis.annotations.Param;

import me.ywork.base.entity.AbstractEntity;


/**
 * 使用MyBatis框架支持基本数据操作的DAO层接口基类
 * 
 * @author TangGang 2016年7月8日
 * 
 * @param <T>
 *            主数据模型
 */
public interface IBatisCrudRepository<PK extends Serializable, T extends AbstractEntity<PK>> extends IBatisRepository {
	/**
	 * 插入记录
	 * 
	 * @param entity
	 *            记录对象实体
	 * @return 插入行数
	 */
	public <S extends T> Integer insert(S entity);

	/**
	 * 根据主键更新实体
	 * 
	 * @param entity
	 *            记录对象实体
	 * @return 更新行数
	 */
	public <S extends T> Integer update(S entity);
	
	

	/**
	 * 根据主键查找实体
	 * 
	 * @param companyId
	 *            投票发起人所在企业的蓝凌内部ID，主要用于分库键
	 * @param id
	 *            主键
	 * @return
	 */
	public T findById(@Param(value = "corpId") String corpId,
					  @Param(value = "id") String id);

	/**
	 * 根据主键判断实体是否存在 ，多用于关联检查
	 * 
	 * @param companyId
	 *            投票发起人所在企业的蓝凌内部ID，主要用于分库键
	 * @param id
	 *            主键
	 * @return true存在，false不存在
	 */
	public Boolean exists(@Param(value = "corpId") String corpId,
						  @Param(value = "id") String id);

	/**
	 * 根据主键删除记录
	 * 
	 * @param companyId
	 *            投票发起人所在企业的蓝凌内部ID，主要用于分库键
	 * @param id
	 *            主键
	 */
	public Boolean deleteById(@Param(value = "corpId") String corpId,
							  @Param(value = "id") String id);
}
