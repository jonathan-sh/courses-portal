package com.courses.portal.dao.mongo;


import com.courses.portal.dao.mongo.crud.CreateTest;
import com.courses.portal.dao.mongo.crud.DeleteTest;
import com.courses.portal.dao.mongo.crud.ReadTest;
import com.courses.portal.dao.mongo.crud.UpdateTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jonathan on 7/14/17.
 */
@RunWith(Suite.class)

@Suite.SuiteClasses({
        CreateTest.class,
        ReadTest.class,
        UpdateTest.class,
        DeleteTest.class
})
public class MongoCrudTest {

}
