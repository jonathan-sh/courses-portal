package com.courses.portal.model;

import com.courses.portal.dao.CourseRepository;
import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.model.dto.GradeCourse;
import com.courses.portal.model.dto.ProviderTopic;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeInformation {


    private CourseRepository courseRepository;
    private ProviderRepository providerRepository;
    private List<Course> courses;
    List<Provider> providers;


    public HomeInformation() {
        this.providerRepository = new ProviderRepository(Provider.COLLECTION, Provider.class);
        this.courseRepository = new CourseRepository(Course.COLLECTION, Course.class);
        this.courses = courseRepository.findAll();
        this.providers = providerRepository.readAll();
    }

    @Expose
    public String welcome;
    @Expose
    public List<ProviderTopic> topics = new ArrayList<ProviderTopic>();
    @Expose
    public List<GradeCourse> grade = new ArrayList<GradeCourse>();


    public HomeInformation get() {
      if (this.providers.size()>0)
      {
          fillWelcomeAndTopics();
          fillGrades();
      }
      return this;
    }

    private void fillWelcomeAndTopics() {

        this.welcome =  providers.get(0).welcome;
        this.topics = providers.get(0).topics;
    }


    private void fillGrades() {
        providers.get(0).grades.forEach(item ->
        {
            GradeCourse gradeCourse = new GradeCourse();
            gradeCourse.description = item.description;
            grade.add(gradeCourse);

            item.courses.forEach(id ->
            {
                Course course = getCourseById(id);
                if (course != null && course.status)
                {
                    gradeCourse.courses.add(course);
                }
            });

        });

        removeNotActiveCouses();

    }

    private Course getCourseById(String id) {
        try
        {
            return courses.stream()
                    .filter(item -> item._id.equals(id))
                    .collect(Collectors.toList())
                    .get(0);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void removeNotActiveCouses() {
        this.grade = this.grade
                         .stream()
                         .filter(item -> item.courses.size() > 0)
                         .collect(Collectors.toList());

    }

}
