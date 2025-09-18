package org.example.mappings.manytomany;

import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class ManyToManyTest {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //insert(session);
        find(session);


    }

    private static void find(Session session) {
        Trainee ramesh = session.find(Trainee.class, 2);
        System.out.println(ramesh);
        System.out.println(ramesh.getCourses());
    }

    private static void insert(Session session) {
        Trainee prasanth=new Trainee("prasanth");
        Trainee ramesh=new Trainee("ramesh");
        Trainee ravinder=new Trainee("prasanth");
        Trainee akhil=new Trainee("akhil");

        Course JFS=new Course("JFS");
        Course testing= new Course("Testing");
        Course ai=new Course("Ai");

        List<Trainee> JFSTraineeList=new ArrayList<>();
        List<Trainee> testingTraineeList=new ArrayList<>();
        List<Trainee> aiTraineeList=new ArrayList<>();

        List<Course> prasanthCourses=new ArrayList<>();
        List<Course> rameshCourses=new ArrayList<>();
        List<Course> ravinderCourses=new ArrayList<>();
        List<Course> akhilCourses=new ArrayList<>();

        JFSTraineeList.add(prasanth);
        JFSTraineeList.add(ramesh);

        testingTraineeList.add(ramesh);
        testingTraineeList.add(prasanth);

        aiTraineeList.add(akhil);
        aiTraineeList.add(ravinder);

        prasanthCourses.add(JFS);
        prasanthCourses.add(testing);
        rameshCourses.add(JFS);
        rameshCourses.add(testing);
        ravinderCourses.add(ai);
        akhilCourses.add(ai);

        prasanth.setCourses(prasanthCourses);
        ramesh.setCourses(rameshCourses);
        ravinder.setCourses(ravinderCourses);
        akhil.setCourses(akhilCourses);

        JFS.setTrainees(JFSTraineeList);
        testing.setTrainees(testingTraineeList);
        ai.setTrainees(aiTraineeList);

        session.beginTransaction();
        session.persist(prasanth);
        session.persist(ramesh);
        session.persist(ravinder);
        session.persist(akhil);
        session.getTransaction().commit();
        session.close();
    }
}
