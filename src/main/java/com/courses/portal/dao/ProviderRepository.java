package com.courses.portal.dao;

import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.Provider;
import com.courses.portal.useful.mongo.MongoHelper;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by jonathan on 7/14/17.
 */
public class ProviderRepository extends MongoCrud {

    public ProviderRepository(String collection, Class clazz) {
        super(collection, clazz);
    }
    private static Logger logger = LoggerFactory.getLogger(ProviderRepository.class);

    public Provider findByEmail(String email) {
        Document query = new Document();
        query.append("email", email);
        return findOne(query, new Document());
    }

    public Provider findById(String id) {
        Document query = new Document();
        ObjectId objectId = new ObjectId(MongoHelper.treatsId(id));
        query.append("_id", objectId);
        return findOne(query, new Document());
    }

    private Provider findOne(Document query, Document sort){
        Provider provider = null;
        List<Provider> providers = super.read(query, sort, 0);
        try
        {
            provider = providers.get(0);
        }
        catch (Exception e)
        {
            logger.error("Error to find the findOne");
            logger.error("Possible cause: " + e.getCause());
        }
        return provider;

    }
}
