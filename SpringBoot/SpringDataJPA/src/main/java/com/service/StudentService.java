package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dao.StudentRepository;
import com.model.Student;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepository;
	
	public List<Student> findAllStudents() {
	      return studentRepository .findAll();
	}

    public Student findStudentByName(String name) {
        return studentRepository.findByStudentName(name);
    }
    
    public String saveStudent(Student student) {
    	Student s= studentRepository.save(student);
		return s.getStudentName();
    }
    
    public Page<Student> getPageDat(int pageId,int size) {
    	Sort descending=Sort.by("studentAge").descending();
    	PageRequest p=PageRequest.of(pageId, size,descending);
    	return studentRepository.findAll(p);
    }

}
