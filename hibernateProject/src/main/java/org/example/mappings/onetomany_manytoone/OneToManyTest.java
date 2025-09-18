package org.example.mappings.onetomany_manytoone;

import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.List;

public class OneToManyTest {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //insert(session);

        find(session);


    }

    private static void find(Session session) {
        Person p2 = session.find(Person.class, 2);
        System.out.println(p2);
        System.out.println(p2.getOrders());
    }

    private static void insert(Session session) {
        Person p1=new Person("suresh");
        Person p2= new Person("ramesh");

        Orders o1=new Orders("mobile",1);
        Orders o2=new Orders("laptop",3);
        Orders o3=new Orders("clg bag",5);


        o1.setPerson(p1);
        o2.setPerson(p1);
        List<Orders> orderList1= Arrays.asList(o1,o2);
        p1.setOrders(orderList1);


        try (session){
            session.beginTransaction();
            session.persist(p1);
            session.getTransaction().commit();
        }
    }
}
