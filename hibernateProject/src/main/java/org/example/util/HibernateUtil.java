package org.example.util;


import org.example.compositekey.Car;
import org.example.mappings.onetoone.PassPort;
import org.example.mappings.onetoone.User;
import org.example.pojo.Employee;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory=null;
    public static SessionFactory getSessionFactory(){
        if(sessionFactory==null){
            Configuration configuration=new Configuration();
            /*configure method automatically look for hibernate.cfg.xml without giving also
            * if you change name like hibernate2.cfg.xml you will get error and need to tell file name to configure method */
            //configuration.configure("hibernate.cfg.xml");
            configuration.configure();
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(PassPort.class);
            configuration.addAnnotatedClass(Car.class);
            sessionFactory=configuration.buildSessionFactory();
        }
        return sessionFactory;
    }



}
