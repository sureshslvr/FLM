package com.xmlwithautowire;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StudentTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext= new ClassPathXmlApplicationContext("appconfigwithjavaannotations.xml");
        Student student = applicationContext.getBean("student", Student.class);
        System.out.println(student);
    }
}
