package org.example.mappings.onetoone;

import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class OneToOne {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try(session){
           // insert(session);
            find(session);

        }
    }

    private static void find(Session session) {
        //this example is the unidirectional example we can find passport details with user but we cont get user details from passport
        //to make it bidirectional we need to add user field in passport and add  @OneToOne(mappedBy="passPort")
        User user = session.find(User.class, 2);
        System.out.println(user);
        PassPort passPort = session.find(PassPort.class, 1719730);
        System.out.println(passPort);
        //after making objects bidirectional
        System.out.println(passPort.getUser().getUserName());//this will come after adding user filed in passport
    }

    private static void insert(Session session) {
        PassPort p=new PassPort(1719731,"25-oct-2025");
        User user=new User("ramesh",p);
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
    }
}
