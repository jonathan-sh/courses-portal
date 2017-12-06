package com.courses.portal.dao.mongo.crud;

import com.courses.portal.dao.mongo.MockData;
import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.Provider;
import com.courses.portal.useful.encryptions.EncryptionSHA;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by jonathan on 7/14/17.
 */
public class CreateTest {

    public void createTest() {
        MongoCrud mongoCrud = new MongoCrud(MockData.COLLECTION, MockData.class);
        Boolean status = mongoCrud.create(new MockData().fillCreateData());
        assertEquals(true, status.booleanValue());
    }


}
