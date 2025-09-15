package org.example;

import org.example.Employees;
import org.example.HibernateUtil;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Employees emp=new Employees("Suresh","dev",23445);
        session.beginTransaction();
        session.persist(emp);
        session.getTransaction().commit();
        session.close();
    }
}