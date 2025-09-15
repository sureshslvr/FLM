package org.example;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    static SessionFactory sessionFactory=null;
    public static SessionFactory getSessionFactory(){
        if(sessionFactory==null){
            Configuration configuration=new Configuration();
            configuration.configure("hibernate.cgf.xml");
            sessionFactory=configuration.buildSessionFactory();
        }
        return sessionFactory;
    }



}
