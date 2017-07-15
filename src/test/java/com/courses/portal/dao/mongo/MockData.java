package com.courses.portal.dao.mongo;

import com.courses.portal.useful.MongoHelper;

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

}