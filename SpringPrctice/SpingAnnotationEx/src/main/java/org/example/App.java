package org.example;

import com.Config;
import com.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        ApplicationContext context=new AnnotationConfigApplicationContext(Config.class);
        Student s=   context.getBean("student", Student.class);
        System.out.println(s);

    }
}
