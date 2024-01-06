package com.example.doctorapp.controller;

import com.example.doctorapp.dto.CourseDto;
import com.example.doctorapp.entity.Course;
import com.example.doctorapp.mapper.CourseMapper;
import com.example.doctorapp.service.CourseService;
import com.example.doctorapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/all-courses")
    public ResponseEntity<List<CourseDto>> getAllCourses(){
        List<Course> courses = courseService.getCourses();
        List<CourseDto> courseDtos = courses.stream()
                .map(courseMapper::mapToCourseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseDtos);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourseDto>> getCoursesByUser(@PathVariable Long userId) {
        List<Course> userCourses = courseService.getCoursesByUser(userId);
        List<CourseDto> courseDtos = userCourses.stream()
                .map(courseMapper::mapToCourseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseDtos);
    }

    @PostMapping("/add-course")
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        CourseDto newCourse = courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollUserToCourse(@RequestParam Long userId, @RequestParam Long courseId) {
        boolean enrolled = courseService.enrollUserToCourse(userId, courseId);
        if (enrolled) {
            return ResponseEntity.ok().body("Użytkownik został zapisany do kursu.");
        } else {
            return ResponseEntity.badRequest().body("Nie udało się zapisać użytkownika na kurs.");
        }
    }

    @DeleteMapping("/delete-course/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        boolean isDeleted = courseService.deleteCourse(courseId);
        if (isDeleted) {
            return ResponseEntity.ok().body("Kurs został usunięty.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kurs nie został znaleziony.");
        }
    }
}
