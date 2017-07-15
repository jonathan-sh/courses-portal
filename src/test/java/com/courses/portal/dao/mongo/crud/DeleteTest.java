package com.courses.portal.dao.mongo.crud;

import com.courses.portal.dao.mongo.MockData;
import com.courses.portal.dao.mongoDB.MongoCrud;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by jonathan on 7/14/17.
 */
public class DeleteTest {
    @Test
    public void updateTest(){
        MongoCrud mongoCrud = new MongoCrud();
        Boolean status =  mongoCrud.deleteOne(MockData.COLLECTION,ReadTest.mockData._id);
        assertEquals(true, status.booleanValue());
    }
}
