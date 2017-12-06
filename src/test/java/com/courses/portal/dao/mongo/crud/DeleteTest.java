package com.courses.portal.dao.mongo.crud;

import com.courses.portal.dao.mongo.MockData;
import com.courses.portal.dao.mongoDB.MongoCrud;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by jonathan on 7/14/17.
 */
public class DeleteTest {

    public void updateTest() {
        MongoCrud mongoCrud = new MongoCrud(MockData.COLLECTION, MockData.class);
        ReadTest readTest = new ReadTest();
        readTest.readTest();
        Boolean status = mongoCrud.deleteOne(readTest.mockData._id);
        assertEquals(true, status.booleanValue());
    }
}
