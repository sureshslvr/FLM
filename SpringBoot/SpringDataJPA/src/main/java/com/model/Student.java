package com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_Id")
	private int studentId;
	
	@Column(name = "student_Name")
	private String studentName;
	@Column(name = "student_Age")
	private int studentAge;
	@Column(name = "student_Address")
	private String studentAddress;
	
	public Student(String studentName, int studentAge, String studentAddress) {
		this.studentName = studentName;
		this.studentAge = studentAge;
		this.studentAddress = studentAddress;
	}
	

	
	
	

}
