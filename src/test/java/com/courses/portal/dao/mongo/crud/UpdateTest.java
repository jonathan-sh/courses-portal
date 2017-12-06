package com.courses.portal.dao.mongo.crud;

import com.courses.portal.dao.mongo.MockData;
import com.courses.portal.dao.mongoDB.MongoCrud;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by jonathan on 7/14/17.
 */
public class UpdateTest {

    public void updateTest() {
        MongoCrud mongoCrud = new MongoCrud(MockData.COLLECTION, MockData.class);
        ReadTest readTest = new ReadTest();
        readTest.readTest();
        Boolean status = mongoCrud.update(readTest.mockData._id, new MockData().fillUpdateData());
        if (status.booleanValue())
        {
            MockData mockData = (MockData) mongoCrud.readOne(ReadTest.mockData._id);
            assertEquals(MockData.NEW_NAME, mockData.name);
        }
        assertEquals(true, status.booleanValue());
    }
}
