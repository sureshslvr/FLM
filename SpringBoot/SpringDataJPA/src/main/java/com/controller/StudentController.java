package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model.Student;
import com.service.StudentService;

@RestController
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@GetMapping("/findall")
	public List<Student> getAllStudents() {
		
		 return studentService.findAllStudents();
	
		
	}

    @GetMapping("/findbyname")
    public Student findByName(@RequestParam(name = "name") String name) {
        return studentService.findStudentByName(name);

    }
    @GetMapping("/saveStudent")
    public String saveStudent(@RequestParam String name,@RequestParam int age, @RequestParam String address) {
    	Student s=new Student(name,age,address);
    	String savedstudentname=studentService.saveStudent(s);
        return savedstudentname+" saved in db";

    }
    
    @GetMapping("/findallbypage")
    public Page<Student> findbyPage(@RequestParam int pageId, @RequestParam int size) {
        return studentService.getPageDat(pageId, size);

    }

}
