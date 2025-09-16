package org.example.main;

import org.example.pojo.Employee;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class HQL {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try(session){
            findByParameter(session);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            session.getSessionFactory().close();
        }


    }
    private static void findByParameter(Session session) {
        SelectionQuery<Employee> query = session.createSelectionQuery("select e from Employee e where empId=?1", Employee.class);
        query.setParameter(1,3);
        List<Employee> list = query.list();
        System.out.println(list);
        query=session.createSelectionQuery("from Employee where name=?1",Employee.class);
        query.setParameter(1,"sfks");
        list = query.list();
        System.out.println(list);

    }

    private static void findAllFromTable(Session session) {
        /*in select query you should give same names as mentioned in pojo or entity class
         * if you give employee/employees instead of Employee it will give error*/
        SelectionQuery<Employee> query = session.createSelectionQuery("select e from Employee e", Employee.class);
        List<Employee> list = query.list();
        System.out.println(list);
        SelectionQuery<Employee> query1 = session.createSelectionQuery("from Employee", Employee.class);
        List<Employee> list1 = query1.list();
        System.out.println(list1);

    }
}
