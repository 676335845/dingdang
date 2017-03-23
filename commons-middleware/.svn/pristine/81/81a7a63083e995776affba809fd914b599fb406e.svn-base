package com.heyi.framework.mongo.springdata;

import com.mongodb.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoWriter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link MongoOperations}接口的扩展实现，继承了{@link MongoTemplate}的全部功能
 */
public class DefaultMongoOperationsImpl extends MongoTemplate implements DefaultMongoOperations {
	/**
	 * 常量空文档列表对象，避免重复生成空列表对象
	 */
	public final static List<DBObject> emptyDocuments = new ArrayList<>(0);

	public DefaultMongoOperationsImpl(Mongo mongo, String databaseName) {
		super(mongo, databaseName);
	}

	public DefaultMongoOperationsImpl(Mongo mongo, String databaseName, UserCredentials userCredentials) {
		super(mongo, databaseName, userCredentials);
	}

	public DefaultMongoOperationsImpl(MongoDbFactory mongoDbFactory) {
		super(mongoDbFactory);
	}

	public DefaultMongoOperationsImpl(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
		super(mongoDbFactory, mongoConverter);
	}

	public BulkWriteOperation buildBulkWriteOperation(final boolean ordered, Class<?> entityClass) {
		DBCollection dbCollection = this.getDBCollection(entityClass);

		BulkWriteOperation bulkWriteOperation = null;
		if (ordered) {
			bulkWriteOperation = dbCollection.initializeOrderedBulkOperation();
		} else {
			bulkWriteOperation = dbCollection.initializeUnorderedBulkOperation();
		}

		return bulkWriteOperation;
	}

	@Override
	public DBCollection getDBCollection(Class<?> entityClass) {
		if (entityClass == null) {
			throw new NullPointerException("getDBCollection - param entityClass is null.");
		}

		String collectionName = this.getCollectionName(entityClass);
		if (StringUtils.isBlank(collectionName)) {
			throw new IllegalStateException("未找到实体类[".concat(entityClass.getCanonicalName()).concat("]的集合名称。"));
		}

		DBCollection dbCollection = this.getCollection(collectionName);
		if (dbCollection == null) {
			throw new IllegalStateException("未找到名称为[".concat(collectionName).concat("]的集合对象。"));
		}

		return dbCollection;
	}

	@Override
	public <T> List<DBObject> generateDocument(Collection<T> entities) {
		if (entities == null) {
			throw new NullPointerException("generateDocument - param entities is null.");
		}

		if (entities.isEmpty()) {
			return emptyDocuments;
		}

		MongoWriter<T> writer = (MongoWriter<T>) this.getConverter();
		Assert.notNull(writer);

		List<DBObject> dbObjectList = new ArrayList<DBObject>();
		DBObject dbDoc = null;
		for (T entity : entities) {
			if (entity == null) {
				continue;
			}

			dbDoc = this.generateDocument(entity, writer);

			dbObjectList.add(dbDoc);
		}

		return dbObjectList;
	}

	@Override
	public <T> DBObject generateDocument(T entity) {
		if (entity == null) {
			throw new NullPointerException("generateDocument - param entity is null.");
		}
		MongoWriter<T> writer = (MongoWriter<T>) this.getConverter();
		Assert.notNull(writer);

		return this.generateDocument(entity, writer);
	}

	protected <T> DBObject generateDocument(T entity, MongoWriter writer) {
		if (entity == null) {
			throw new NullPointerException("generateDocument - param entity is null.");
		}

		if (writer == null) {
			throw new NullPointerException("generateDocument - param writer is null.");
		}

		BasicDBObject dbDoc = new BasicDBObject();

		writer.write(entity, dbDoc);

		return dbDoc;
	}

	@Override
	public <T> int bulkInsertDocument(BulkWriteOperation bulkWriteOperation, Collection<? extends T> entities) {
		if (bulkWriteOperation == null) {
			throw new NullPointerException("bulkInsertDocument - param bulkWriteOperation is null.");
		}

		if (entities == null) {
			throw new NullPointerException("bulkInsertDocument - param entities is null.");
		}

		MongoWriter<T> writer = (MongoWriter<T>) this.getConverter();
		Assert.notNull(writer);

		int insertCount = 0;
		DBObject dbObject = null;
		for (T entity : entities) {
			if (entity == null) {
				continue;
			}

			dbObject = this.generateDocument(entity, writer);
			if (dbObject == null) {
				continue;
			}

			bulkWriteOperation.insert(dbObject);
			insertCount ++;
		}

		return insertCount;
	}

	@Override
	public void queryFromPrimaryOnly() {
		this.setReadPreference(ReadPreference.primary());
	}
}
