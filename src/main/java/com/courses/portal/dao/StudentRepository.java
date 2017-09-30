package com.courses.portal.dao;

import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.Student;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by jonathan on 7/16/17.
 */
public class StudentRepository extends MongoCrud {
    public StudentRepository(String collection, Class clazz) {
        super(collection, clazz);
    }

    private static Logger logger = LoggerFactory.getLogger(StudentRepository.class);

    public List<Student> findAll() {
        List<Student> courses = super.readAll();
        courses.forEach(Student::treatmentForResponse);
        return courses;
    }

    public Student findByEmail(String email) {
        Document query = new Document();
        query.append("email", email);
        Student student = null;
        List<Student> students = super.read(query, new Document(), 0);
        try
        {
            student = students.get(0);
        }
        catch (Exception e)
        {
            logger.error("Error to find the student by email");
            logger.error("Possible cause: " + e.getCause());
        }
        return student;
    }
}
