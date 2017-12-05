package com.courses.portal;

import com.courses.portal.useful.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CoursesPortalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursesPortalApiApplication.class, args);
    }
}
