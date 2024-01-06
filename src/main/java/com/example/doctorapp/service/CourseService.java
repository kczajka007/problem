package com.example.doctorapp.service;

import com.example.doctorapp.dto.CourseDto;
import com.example.doctorapp.entity.Course;

import java.util.List;
import java.util.Set;

public interface CourseService {
    List<Course> getCourses();
    CourseDto createCourse(CourseDto courseDto);
    boolean deleteCourse(Long courseId);
    List<Course> getCoursesByUser(Long userId);
    boolean enrollUserToCourse(Long userId, Long courseId);
}
