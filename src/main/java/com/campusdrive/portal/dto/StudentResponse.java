package com.campusdrive.portal.dto;

import com.campusdrive.portal.entity.Student;
import lombok.Getter;

@Getter
public class StudentResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final Double cgpa;
    private final String skills;
    private final Long collegeId;
    private final String collegeName;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.cgpa = student.getCgpa();
        this.skills = student.getSkills();
        this.collegeId = student.getCollege().getId();
        this.collegeName = student.getCollege().getName();
    }
}