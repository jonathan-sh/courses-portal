package com.courses.portal.dao.mongo;

import com.courses.portal.dao.mongo.crud.CreateTest;
import com.courses.portal.dao.mongo.crud.DeleteTest;
import com.courses.portal.dao.mongo.crud.ReadTest;
import com.courses.portal.dao.mongo.crud.UpdateTest;
import org.junit.Test;

/**
 * Created by jonathan on 7/14/17.
 */

public class MongoCrudTest {

    @Test
    public void crud(){
      new CreateTest().createTest();
      new ReadTest().readTest();
      new UpdateTest().updateTest();
      new DeleteTest().updateTest();
    }
}
