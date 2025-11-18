package com;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Table(name = "employees")
public class Employee {
    private int empId;
    private String name;
    private int age;
    private String email;
    private String phone_number;
    private double salary;

}
