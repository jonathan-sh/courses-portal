package com.courses.portal.model;

import com.courses.portal.dao.CourseRepository;
import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.model.dto.GradeCourse;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeInformation {


    private CourseRepository courseRepository;
    private ProviderRepository providerRepository;


    public HomeInformation() {
        this.courseRepository = new CourseRepository(Course.COLLECTION,Course.class);
        this.providerRepository = new ProviderRepository(Provider.COLLECTION,Provider.class);
    }

    @Expose
    public String welcome;
    @Expose
    public List<ProviderTopic> topics = new ArrayList<ProviderTopic>();
    @Expose
    public List<GradeCourse> grade = new ArrayList<GradeCourse>();



    public HomeInformation get(){

        List<Provider> providers = providerRepository.readAll();
        List<Course> courses = courseRepository.findAll();

        this.welcome = providers.get(0).welcome == null ? "A diferença do sucesso." :  providers.get(0).welcome;
        this.topics = providers.get(0).topics;

        providers.get(0).grades.forEach(item->{
            GradeCourse gradeCourse = new GradeCourse();
            gradeCourse.description = item.description;
            grade.add(gradeCourse);

            item.courses.forEach(id->{
                Course course = courseRepository.findById(id);
                if (course!=null && course.status)
                {
                    gradeCourse.courses.add(course);
                }
            });

        });


        this.grade = this.grade
                         .stream()
                         .filter(item->item.courses.size()>0)
                         .collect(Collectors.toList());



        return this;
    }
}
