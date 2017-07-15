package com.courses.portal.dao;

import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.dto.Provider;
import org.bson.Document;

import java.util.List;

/**
 * Created by jonathan on 7/14/17.
 */
public class ProviderRepository extends MongoCrud{

    public Provider findByEmail(String email) {
        Document query = new Document();
        query.append("email",email);
        List<Provider> providers = super.read(Provider.COLLECTION,Provider.class,query,new Document(),0);
        return providers.get(0);
    }
}
