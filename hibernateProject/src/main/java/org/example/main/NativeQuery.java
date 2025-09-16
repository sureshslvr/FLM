package org.example.main;

import org.example.pojo.Employee;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.MutationQuery;

import java.util.List;

public class NativeQuery {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try(session){
            //dqlOperations(session);
            dmlOperations(session);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.getSessionFactory().close();
        }

    }
    private static void dmlOperations(Session session){
        //insert(session);
        //update(session);
        delete(session);
    }
    private static void delete(Session session) {
        session.beginTransaction();
        MutationQuery query = session.createNativeMutationQuery("delete from employees where empId=?1");
        query.setParameter(1,16);
        query.executeUpdate();
        session.getTransaction().commit();

    }
    private static void update(Session session) {
        session.beginTransaction();
        MutationQuery query = session.createNativeMutationQuery("update employees set name=?1 where empId=?2");
        query.setParameter(1,"sujith kumar");
        query.setParameter(2,18);
        query.executeUpdate();
        session.getTransaction().commit();

    }

    private static void insert(Session session) {
        session.beginTransaction();
        MutationQuery query = session.createNativeMutationQuery("insert into employees(name,position,sal) values(?1,?2,?3)");
        query.setParameter(1,"niranjan");
        query.setParameter(2,"axis insurence");
        query.setParameter(3,33000);
        query.executeUpdate();
        session.getTransaction().commit();

    }

    private static void dqlOperations(Session session){
        findAll(session);
        findByID(session);
    }
    private static void findByID(Session session) {
        org.hibernate.query.NativeQuery<Employee> query = session.createNativeQuery("select * from employees where empId=?1", Employee.class);
        query.setParameter(1,10);
        List<Employee> list = query.list();
        System.out.println(list);
    }

    private static void findAll(Session session) {
        org.hibernate.query.NativeQuery<Employee> query = session.createNativeQuery("select * from employees", Employee.class);
        List<Employee> list = query.list();
        System.out.println(list);
    }
}
