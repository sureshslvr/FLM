package com.dao;

import com.model.Employee;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Transactional
public class EmployeeDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Employee getEmployeeById(int id){
        return getSession().find(Employee.class,id);
    };

    public void updateEmployeeEmail(String email,int empId){
        Session session = getSession();
        Employee employee = getEmployeeById(empId);
        employee.setEmail(email);
        getSession().persist(employee);

    }
    public void insertIntoEmployees(Employee e){
        getSession().persist(e);
    }

    public SelectionQuery<Employee> findAll(){
        return getSession().createSelectionQuery("From Employee",Employee.class);
    }
    public Session getSession(){
        return sessionFactory.getCurrentSession();
    }
}
