package com.courses.portal.dao;

import com.courses.portal.dao.mongoDB.MongoCrud;

/**
 * Created by jonathan on 7/15/17.
 */
public class CourseRepository extends MongoCrud {
    public CourseRepository(String collection, Class clazz) {
        super(collection, clazz);
    }
}
