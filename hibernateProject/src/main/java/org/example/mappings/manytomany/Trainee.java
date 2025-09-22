package org.example.mappings.manytomany;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int traineeID;
    private String traineeName;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "trainee_courses",
            joinColumns = {@JoinColumn(name = "traineeID")},
            inverseJoinColumns = {@JoinColumn(name = "courseID")})
    private List<Course> courses;

    public Trainee(int traineeID, String traineeName, List<Course> courses) {
        this.traineeID = traineeID;
        this.traineeName = traineeName;
        this.courses = courses;
    }

    public Trainee(String traineeName) {
        this.traineeName = traineeName;
    }

    public Trainee() {
    }

    public int getTraineeID() {
        return traineeID;
    }

    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "traineeID=" + traineeID +
                ", traineeName='" + traineeName + '\'' +
                '}';
    }
}
