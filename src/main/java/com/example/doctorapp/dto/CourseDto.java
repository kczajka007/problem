package com.example.doctorapp.dto;

import com.example.doctorapp.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long course_id;
    private String courseName;
    private String courseDescription;
    private Set<User> enrolledUsers;
}
