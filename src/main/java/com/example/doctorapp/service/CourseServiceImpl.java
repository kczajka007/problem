package com.example.doctorapp.service;

import com.example.doctorapp.dto.CourseDto;
import com.example.doctorapp.entity.Course;
import com.example.doctorapp.entity.User;
import com.example.doctorapp.mapper.CourseMapper;
import com.example.doctorapp.repository.CourseRepository;
import com.example.doctorapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Course> getCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses;
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setCourse_id(courseDto.getCourse_id());
        course.setCourseName(courseDto.getCourseName());
        course.setCourseDescription(courseDto.getCourseDescription());
        course.setEnrolledUsers(courseDto.getEnrolledUsers());

        Course savedCourse = courseRepository.save(course);

        return courseMapper.mapToCourseDto(savedCourse);
    }

    @Override
    public boolean deleteCourse(Long courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Course> getCoursesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Użytkownik nie znaleziony")
        );
        return new ArrayList<>(user.getCourses());
    }

    public boolean enrollUserToCourse(Long userId, Long courseId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isPresent() && courseOpt.isPresent()) {
            User user = userOpt.get();
            Course course = courseOpt.get();

            // Sprawdzenie, czy użytkownik jest już zapisany na kurs
            if (!course.getEnrolledUsers().contains(user)) {
                course.getEnrolledUsers().add(user);
                courseRepository.save(course);
                return true; // Zapisano na kurs
            }
        }
        return false; // Nie udało się zapisać na kurs
    }
}
