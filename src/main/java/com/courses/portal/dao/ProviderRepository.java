package com.courses.portal.dao;

import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.Provider;
import org.bson.Document;

import java.util.List;

/**
 * Created by jonathan on 7/14/17.
 */
public class ProviderRepository extends MongoCrud{

    public ProviderRepository(String collection, Class clazz) {
        super(collection, clazz);
    }

    public Provider findByEmail(String email) {
        Document query = new Document();
        query.append("email",email);
        Provider provider = null;
        List<Provider> providers = super.read(query,new Document(),0);
        try
        {
            provider = providers.get(0);
        }
        catch (Exception e)
        {
            //log de erro
        }
        return provider;
    }
}
