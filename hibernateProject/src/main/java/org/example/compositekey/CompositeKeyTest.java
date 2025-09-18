package org.example.compositekey;

import org.example.util.HibernateUtil;
import org.hibernate.Session;

public class CompositeKeyTest {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try(session){
            insert(session);
            //find(session);
        }
    }

    private static void find(Session session) {
        Car bajaj = session.find(Car.class, new CarId(1, "bajaj"));
        System.out.println(bajaj);
    }

    private static void insert(Session session) {
        Car c=new Car(new CarId(1,"suziki"),"maruthi");
        session.beginTransaction();
        session.persist(c);
        session.getTransaction().commit();
    }
}
