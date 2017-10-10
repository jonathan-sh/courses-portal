package com.courses.portal.dao.mongoDB;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 1/26/17.
 */
public class MongoCrud {

    private final String COLLECTION;
    private final Class CLAZZ;
    private static Logger logger = LoggerFactory.getLogger(MongoCrud.class);

    public MongoCrud(String collection, Class clazz) {
        this.COLLECTION = collection;
        this.CLAZZ = clazz;
    }

    private MongoConnection mongoConnection;

    private MongoCollection getCollection() {
        try
        {
            MongoConnectionFactory mongoConnectionFactory = new MongoConnectionFactory();
            mongoConnection = mongoConnectionFactory.getConetion();
            MongoCollection collection = mongoConnection.database.getCollection(this.COLLECTION);
            return collection;
        }
        catch (Exception e)
        {
            logger.error("Error in getCollection()");
            logger.error("Possible cause: " + e.getCause());
            return null;
        }
    }

    public Boolean create(Object item) {
        Boolean status;
        try
        {
            MongoCollection c = getCollection();
            c.insertOne(toDocument(item));
            status = true;
        }
        catch (Exception e)
        {
            logger.error("Error during create()");
            logger.error("Possible cause: " + e.getCause());
            status = false;
        }
        mongoConnection.client.close();
        return status;

    }

    public List read(Document queryFind, Document querySort, Integer limit) {

        List list = new ArrayList();
        try
        {
            MongoCollection c = getCollection();

            FindIterable<Document> searcheResult = c.find(queryFind).sort(querySort).limit(limit);
            Gson gson = getGson();
            for (Document document : searcheResult)
            {
                Object item = gson.fromJson(document.toJson(), CLAZZ);
                list.add(item);
            }

        }
        catch (Exception e)
        {
            logger.error("Error during read()");
            logger.error("Possible cause: " + e.getCause());
        }

        mongoConnection.client.close();
        return list;
    }

    public Object readOne(Object id) {

        Object result = new Object();
        try
        {
            MongoCollection c = getCollection();
            FindIterable<Document> searcheResult = c.find(getDocumentID(id));
            Gson gson = getGson();
            for (Document document : searcheResult)
            {
                result = gson.fromJson(document.toJson(), CLAZZ);

            }
        }
        catch (Exception e)
        {
            logger.error("Error during readOne()");
            logger.error("Possible cause: " + e.getCause());
        }

        mongoConnection.client.close();
        return result;
    }

    public List readAll() {

        List list = new ArrayList();
        try
        {
            MongoCollection c = getCollection();

            FindIterable<Document> searcheResult = c.find();
            Gson gson = getGson();
            for (Document document : searcheResult)
            {
                Object item = gson.fromJson(document.toJson(), CLAZZ);
                list.add(item);
            }

        }
        catch (Exception e)
        {
            logger.error("Error during readAll()");
            logger.error("Possible cause: " + e.getCause());
        }

        mongoConnection.client.close();
        return list;
    }


    public Boolean update(Object id, Object objectToUpdate) {
        try
        {
            MongoCollection c = getCollection();
            UpdateOptions updateOptions = new UpdateOptions();
            updateOptions.upsert(false);
            UpdateResult updateResult = c.updateOne(getDocumentID(id), toDocumentToUpdate(objectToUpdate), updateOptions);
            mongoConnection.client.close();
            return updateResult.isModifiedCountAvailable();
        }
        catch (Exception e)
        {
            logger.error("Error during update()");
            logger.error("Possible cause: " + e.getCause());
            mongoConnection.client.close();
            return false;
        }

    }

    public Long delete(Document queryFind) {
        try
        {
            MongoCollection c = getCollection();
            DeleteResult deleteResult = c.deleteOne(queryFind);
            mongoConnection.client.close();
            return deleteResult.getDeletedCount();
        }
        catch (Exception e)
        {
            logger.error("Error during delete()");
            logger.error("Possible cause: " + e.getCause());
            mongoConnection.client.close();
            return 0L;
        }

    }

    public Boolean deleteOne(Object id) {
        try
        {
            MongoCollection c = getCollection();
            DeleteResult deleteResult = c.deleteOne(getDocumentID(id));
            mongoConnection.client.close();
            return deleteResult.getDeletedCount() == 1;
        }
        catch (Exception e)
        {
            logger.error("Error during deleteOne()");
            logger.error("Possible cause: " + e.getCause());
            mongoConnection.client.close();
            return false;
        }

    }

    private Document getDocumentID(Object id) {
        Document objectID = new Document();
        try
        {
            ObjectId objectId = new ObjectId(id.toString().replace("{$oid=", "").replace("}", ""));

            objectID.put("_id", objectId);
        }
        catch (Exception e)
        {
            logger.error("Error during deleteOne()");
            logger.error("Possible cause: " + e.getCause());
        }
        return objectID;
    }

    private Document toDocument(Object object) {
        try
        {
            Gson gson = getGson();
            return Document.parse(gson.toJsonTree(object).toString());
        }
        catch (Exception e)
        {
            logger.error("Error during toDocument()");
            logger.error("Possible cause: " + e.getCause());
            return null;
        }
    }

    private Gson getGson() {
        try
        {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            return gson;
        }
        catch (Exception e)
        {
            logger.error("Error during getGson()");
            logger.error("Possible cause: " + e.getCause());
            return null;
        }
    }

    private Document toDocumentToUpdate(Object object) {
        Document documentToUpdate = new Document();
        try
        {
            Document newData = toDocument(object);
            newData.remove("_id");
            documentToUpdate.put("$set", newData);
        }
        catch (Exception e)
        {
            logger.error("Error during toDocumentToUpdate()");
            logger.error("Possible cause: " + e.getCause());
        }
        return documentToUpdate;
    }


}
