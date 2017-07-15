package com.courses.portal.dao.mongo.crud;

import com.courses.portal.dao.mongo.MockData;
import com.courses.portal.dao.mongoDB.MongoCrud;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by jonathan on 7/14/17.
 */
public class ReadTest {

    public static MockData mockData;

    @Test
    public void readTest(){
        List<MockData> result = new MongoCrud().readAll(MockData.COLLECTION,MockData.class);
        Boolean status = result.size() >0;
        mockData = result.get(0).treatsId();
        assertEquals(true,status.booleanValue());

    }
}
