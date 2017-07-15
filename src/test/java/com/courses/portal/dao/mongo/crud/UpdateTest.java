package com.courses.portal.dao.mongo.crud;

import com.courses.portal.dao.mongo.MockData;
import com.courses.portal.dao.mongoDB.MongoCrud;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by jonathan on 7/14/17.
 */
public class UpdateTest {
    @Test
    public void updateTest(){
      MongoCrud mongoCrud = new MongoCrud();
      Boolean status =  mongoCrud.update(MockData.COLLECTION,ReadTest.mockData._id,new MockData().fillUpdateData());
      if (status.booleanValue())
      {
         MockData mockData = (MockData) mongoCrud.readOne(MockData.COLLECTION,ReadTest.mockData._id,MockData.class);
          assertEquals(MockData.NEW_NAME, mockData.name);
      }
      assertEquals(true, status.booleanValue());
  }
}
