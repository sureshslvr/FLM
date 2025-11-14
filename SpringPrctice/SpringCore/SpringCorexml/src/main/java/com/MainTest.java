package com;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {
        ApplicationContext context= new ClassPathXmlApplicationContext("bookbean.xml");
        Library l=context.getBean("library",Library.class);
        System.out.println(l);
    }


}
