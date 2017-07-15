package com.courses.portal.dao.mongoDB;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 1/26/17.
 */
public class MongoCrud {

    private MongoConnection mongoConnection;

    private MongoCollection getCollection(String collectionName) {
        try
        {
            MongoConnectionFactory mongoConnectionFactory = new MongoConnectionFactory();
            mongoConnection = mongoConnectionFactory.getConetion();
            MongoCollection collection = mongoConnection.database.getCollection(collectionName);
            return  collection;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Boolean create(String collection, Object item) {
        Boolean status;
        try
        {
            MongoCollection c = getCollection(collection);
            c.insertOne(toDocument(item));
            status = true;
        }
        catch (Exception e)
        {
            //loga erro
            status = false;
        }
        mongoConnection.client.close();
        return status;

    }

    public List read(String collection, Class classOfObject, Document queryFind, Document querySort, Integer limit){

        List list = new ArrayList();
        try
        {
            MongoCollection c = getCollection(collection);

            FindIterable<Document> searcheResult = c.find(queryFind).sort(querySort).limit(limit);

            for (Document document : searcheResult)
            {
                Object item = new Gson().fromJson(document.toJson(), classOfObject);
                list.add(item);
            }

        }
        catch (Exception e)
        {
           //loga erro
        }

        mongoConnection.client.close();
        return list;
    }

    public Object readOne(String collection, Object id, Class classOfObject) {

        Object result = new Object();
        try
        {
            MongoCollection c = getCollection(collection);
            FindIterable<Document> searcheResult = c.find(getDocumentID(id));
            for (Document document : searcheResult)
            {
                result = new Gson().fromJson(document.toJson(), classOfObject);

            }
        }
        catch (Exception e)
        {
          //loga erro
        }

        mongoConnection.client.close();
        return result;
    }

    public List readAll(String collection, Class classOfObject){

        List list = new ArrayList();
        try
        {
            MongoCollection c = getCollection(collection);

            FindIterable<Document> searcheResult = c.find();

            for (Document document : searcheResult)
            {
                Object item = new Gson().fromJson(document.toJson(), classOfObject);
                list.add(item);
            }

        }
        catch (Exception e)
        {
            //loga erro
        }

        mongoConnection.client.close();
        return list;
    }


    public Boolean update(String collection, Object id, Object objectToUpdate){
        try
        {
            MongoCollection c =  getCollection(collection);
            UpdateOptions updateOptions = new UpdateOptions();
            updateOptions.upsert(false);
            UpdateResult updateResult = c.updateOne(getDocumentID(id), toDocumentToUpdate(objectToUpdate), updateOptions);
            mongoConnection.client.close();
            return updateResult.isModifiedCountAvailable();
        }
        catch (Exception e)
        {
          //loga erro
          mongoConnection.client.close();
          return  false;
        }

    }

    public Long delete(String collection, Document queryFind){
        try
        {
            MongoCollection c = getCollection(collection);
            DeleteResult deleteResult = c.deleteOne(queryFind);
            mongoConnection.client.close();
            return deleteResult.getDeletedCount();
        }
        catch (Exception e)
        {
            //loga erro
            mongoConnection.client.close();
            return 0L;
        }

    }

    public Boolean deleteOne(String collection, Object id){
        try
        {
            MongoCollection c = getCollection(collection);
            DeleteResult deleteResult = c.deleteOne(getDocumentID(id));
            mongoConnection.client.close();
            return deleteResult.getDeletedCount() ==1;
        }
        catch (Exception e)
        {
            //loga erro
            mongoConnection.client.close();
            return false;
        }

    }

    private Document getDocumentID(Object id){
        ObjectId objectId = new ObjectId(id.toString().replace("{$oid=","").replace("}",""));
        Document objectID = new Document();
        objectID.put("_id",objectId);
        return  objectID;
    }

    private Document toDocument(Object object)  {
        return Document.parse(new Gson().toJsonTree(object).toString());
    }

    private Document toDocumentToUpdate(Object object){
        Document documentToUpdate = new Document();
        Document newData = toDocument(object);
        newData.remove("_id");
        documentToUpdate.put("$set",newData);
        return documentToUpdate;
    }






}
