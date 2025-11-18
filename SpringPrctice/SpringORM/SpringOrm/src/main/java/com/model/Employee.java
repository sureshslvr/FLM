package com.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;
    private String name;
    private int age;
    private String email;
    private String phone_number;
    private double salary;

    public Employee(String name, int age, String email, String phone_number, double salary) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone_number = phone_number;
        this.salary = salary;
    }
}
