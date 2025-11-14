package com.address;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        /*ApplicationContext applicationContext=new ClassPathXmlApplicationContext("appconfig.xml");
        Employee emp = applicationContext.getBean("emp", Employee.class);
        System.out.println(emp);*/

        /*//scopes example
        ApplicationContext scopeContext=new ClassPathXmlApplicationContext("appconfigscopeexample.xml");
        Employee emp1 = scopeContext.getBean("emp", Employee.class);
        Employee emp2 = scopeContext.getBean("emp", Employee.class);
        System.out.println("emp1: "+emp1+"\n"+"emp2: "+ emp2);
        System.out.println(emp1==emp2);*/

        //auto wiring example byType and byName
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("appconfigautowireexample.xml");
        /*Employee emp = applicationContext.getBean("emp", Employee.class);
        System.out.println(emp);*/
        //by constructor
        Employee emp1 = applicationContext.getBean("emp1", Employee.class);
        System.out.println(emp1);
    }
}
