package org.example.mappings.manytomany;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseID;
    private String courseName;

    @ManyToMany(mappedBy = "courses")
    private List<Trainee> trainees;

    public Course(int courseID, String courseName, List<Trainee> trainees) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.trainees = trainees;
    }

    public Course(String courseName) {
        this.courseName = courseName;
    }

    public Course() {
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID=" + courseID +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
