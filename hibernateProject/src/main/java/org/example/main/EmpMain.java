package org.example.main;

import org.example.pojo.Employee;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

public class EmpMain {
    public static void main(String[] args) {
        Session session=HibernateUtil.getSessionFactory().openSession();
        try(session){
            insertData(session);
            //findData(session);
           // updateData(session);
            //deleteData(session);
            //statesEX(session);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            session.getSessionFactory().close();
        }


    }
    private static void updateData(Session session) {
        /*when update the data get the data from database first, don't create new object*/
        Employee emp = session.find(Employee.class, 9);
        emp.setName("dsah4wrg");
        session.beginTransaction();
        //session.merge(emp);
        session.getTransaction().commit();
        System.out.println(emp);
    }

    private static void deleteData(Session session) {
        Employee emp = session.find(Employee.class, 12);
        session.beginTransaction();
        session.remove(emp);
        session.getTransaction().commit();
        System.out.println(emp);
    }

    private static void findData(Session session) {
        Employee emp = session.find(Employee.class, 11);
        System.out.println(emp);
    }

    private static void insertData(Session session) {
        Employee emp=new Employee("Saghreh","dev",29445);//transient
        session.beginTransaction();
        /*to save or insert the data we need to do persist*/
        session.persist(emp);
        session.getTransaction().commit();
    }

    private static void statesEX(Session session) {
        Employee emp=new Employee("sfks","dev",25000);//transient
        session.beginTransaction();
        /*to save or insert the data we need to do persist*/
        session.persist(emp);//persist
        session.getTransaction().commit();

        session.close();
        emp.setSalary(30000);//detached
    }
}