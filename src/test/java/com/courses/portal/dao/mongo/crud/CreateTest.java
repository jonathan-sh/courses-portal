package com.courses.portal.dao.mongo.crud;

import com.courses.portal.dao.mongo.MockData;
import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.dto.Provider;
import com.courses.portal.security.Encryption;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by jonathan on 7/14/17.
 */
public class CreateTest {
    @Test
    public void createTest(){
        MongoCrud mongoCrud = new MongoCrud();
       // Boolean status = mongoCrud.create(MockData.COLLECTION,new MockData().fillCreateData());
        Provider provider = new Provider();
        provider.name="alan";
        provider.email="alan@turing.com";
        provider.password= Encryption.generateHash("123456");
        Boolean status = mongoCrud.create(Provider.COLLECTION,provider);

        assertEquals(true,status.booleanValue());
    }


}
