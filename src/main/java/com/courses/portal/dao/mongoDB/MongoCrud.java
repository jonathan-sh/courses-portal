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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 1/26/17.
 */
public class MongoCrud {

    private final String COLLECTION;
    private final Class CLAZZ;

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
            return  collection;
        }
        catch (Exception e)
        {
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
            //loga erro
            status = false;
        }
        mongoConnection.client.close();
        return status;

    }

    public List read(Document queryFind, Document querySort, Integer limit){

        List list = new ArrayList();
        try
        {
            MongoCollection c = getCollection();

            FindIterable<Document> searcheResult = c.find(queryFind).sort(querySort).limit(limit);
            Gson gson = getGson();
            for (Document document : searcheResult)
            {
                System.out.println(document.toJson());
                Object item = gson.fromJson(document.toJson(), CLAZZ);
                list.add(item);
            }

        }
        catch (Exception e)
        {
            System.out.println("s");
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
          //loga erro
        }

        mongoConnection.client.close();
        return result;
    }

    public List readAll(){

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
            //loga erro
        }

        mongoConnection.client.close();
        return list;
    }


    public Boolean update(Object id, Object objectToUpdate){
        try
        {
            MongoCollection c =  getCollection();
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

    public Long delete(Document queryFind){
        try
        {
            MongoCollection c = getCollection();
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

    public Boolean deleteOne(Object id){
        try
        {
            MongoCollection c = getCollection();
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
       try {
           Gson gson = getGson();
           return Document.parse(gson.toJsonTree(object).toString());
       }
       catch (Exception e)
       {
           return null;
       }
    }

    private Gson getGson() {
        try {
            Gson gson = new GsonBuilder()
                                .setPrettyPrinting()
                                .excludeFieldsWithoutExposeAnnotation()
                                .create();
            return gson;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private Document toDocumentToUpdate(Object object){
        Document documentToUpdate = new Document();
        Document newData = toDocument(object);
        newData.remove("_id");
        documentToUpdate.put("$set",newData);
        return documentToUpdate;
    }






}
