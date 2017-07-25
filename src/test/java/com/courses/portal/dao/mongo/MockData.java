package com.courses.portal.dao.mongo;

import com.courses.portal.useful.encryptions.EncryptionSHA;
import com.courses.portal.useful.mongo.MongoHelper;
import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 7/14/17.
 */
public class MockData {
    public static final String COLLECTION = "mock";
    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String password;

    public MockData fillCreateData() {
        this.name = "Alan";
        this.password = EncryptionSHA.generateHash("28064212");
        return this;
    }

    public static final String NEW_NAME = "Alan Turing";

    public MockData fillUpdateData() {
        this.name = NEW_NAME;
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

    public MockData treatsId() {
        this._id = MongoHelper.treatsId(this._id);
        return this;
    }
}