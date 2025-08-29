package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        //spring by default follows single ton design pattern internal,
        // we will get same object if we get object more than once also
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("beans.xml");
        System.out.println("context loaded");
        Student student=applicationContext.getBean("st",Student.class);
        System.out.println(student);
        Student student2=applicationContext.getBean("st",Student.class);
        System.out.println(student2.hashCode());
        System.out.println(student==student2);

        //using constructor args
        Student student3=applicationContext.getBean("st2",Student.class);
        System.out.println(student3);

    }
}
