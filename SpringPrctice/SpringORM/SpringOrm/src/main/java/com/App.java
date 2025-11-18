package com;

import com.config.Config;
import com.dao.EmployeeDao;
import com.model.Employee;
import org.hibernate.query.SelectionQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext ctx=new AnnotationConfigApplicationContext(Config.class);
        EmployeeDao employeeDao = ctx.getBean("employeeDao", EmployeeDao.class);
        System.out.println( employeeDao.getEmployeeById(1) );
        employeeDao.updateEmployeeEmail("suresh@gmail.com",1);
        System.out.println( employeeDao.getEmployeeById(1) );

        /*Employee e=new Employee("ramesh",22,"ramesh@gmail.com","14253667",43000);
        employeeDao.insertIntoEmployees(e);*/


       /* SelectionQuery<Employee> all = employeeDao.findAll();
        List<Employee> list = all.getResultList();
        System.out.println(list);*/
    }
}
