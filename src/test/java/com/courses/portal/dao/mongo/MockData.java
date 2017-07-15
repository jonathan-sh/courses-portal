package com.courses.portal.dao.mongo;

import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.Provider;
import com.courses.portal.useful.mongo.MongoHelper;
import org.junit.Test;

/**
 * Created by jonathan on 7/14/17.
 */
public class MockData{
    public static final String COLLECTION = "mock";
    public Object _id;
    public String name;
    public String password;

    public MockData fillCreateData(){
        this.name ="Alan";
        this.password="28064212";
        return this;
    }

    public static final String NEW_NAME="Alan Turing";
    public MockData fillUpdateData(){
        this.name =NEW_NAME;
        return this;
    }

    @Override
    public String toString() {
        return "MockData{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public MockData treatsId()
    {
        this._id = MongoHelper.treatsId(this._id);
        return this;
    }


    @Test
    public void mimimi(){
        Provider provider = new Provider();
        MongoCrud mongoCrud = new MongoCrud(Provider.COLLECTION, Provider.class);
        provider.name="Jonathan";
        mongoCrud.create(provider);
    }

}