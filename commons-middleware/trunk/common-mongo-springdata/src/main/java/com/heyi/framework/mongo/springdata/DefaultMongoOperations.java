package com.heyi.framework.mongo.springdata;

import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Collection;
import java.util.List;

/**
 * 扩展spring data中的{@link MongoOperations}接口
 */
public interface DefaultMongoOperations extends MongoOperations {

    /**
     * 获取实体对应的MongoDB集合{@link DBCollection}
     * @param entityClass  实体类型对象
     * @return
     */
    DBCollection getDBCollection(Class<?> entityClass);

    /**
     * 生成跟指定实体相关的批处理对象
     * @param ordered  是否对批处理指令进行排序
     *                    <br/>根据insert,update,remove排序分组,除非对执行顺序有严格要求否则不应进行排序
     * @param entityClass 实体类型对象
     * @return  批处理对象
     */
    BulkWriteOperation buildBulkWriteOperation(final boolean ordered, Class<?> entityClass);

    /**
     * 将对象转换成MongoDB文档对象
     * @param entities   实体对象列表
     * @param <T>  对象类型
     * @return  MongoDB文档对象列表
     */
    <T> List<DBObject> generateDocument(Collection<T> entities);

    /**
     * 将对象转换成MongoDB文档对象
     * @param entity   需要转换成MongoDB文档的对象
     * @param <T>  对象类型
     * @return  MongoDB文档对象
     */
    <T> DBObject generateDocument(T entity);

    /**
     * 将实例列表追加到批次处理对象中
     * @param bulkWriteOperation   批次处理对象
     * @param entities  需要插入的实体列表
     * @param <T>  实体类型
     * @return 插入批次处理的数量
     */
    <T> int bulkInsertDocument(BulkWriteOperation bulkWriteOperation, Collection<? extends T> entities);

    /**
     * 标记只从主节点查询
     * <br/>在某些场景下，由于MongoDB从节点从主节点同步数据存在时间差，可能导致无法从从节点读取到最新的数据,
     * <br/>用此标记可以强制从主节点读取数据
     */
    void queryFromPrimaryOnly();
}
