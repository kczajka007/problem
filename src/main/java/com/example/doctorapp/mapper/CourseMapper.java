package com.example.doctorapp.mapper;

import com.example.doctorapp.dto.CourseDto;
import com.example.doctorapp.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseDto mapToCourseDto(Course course){
        CourseDto courseDto = new CourseDto(
                course.getCourse_id(),
                course.getCourseName(),
                course.getCourseDescription(),
                course.getEnrolledUsers()
        );
        return courseDto;
    }

    public Course mapToCourse(CourseDto courseDto){
        Course course = new Course(
                courseDto.getCourse_id(),
                courseDto.getCourseName(),
                courseDto.getCourseDescription(),
                courseDto.getEnrolledUsers()
        );
        return course;
    }
}
