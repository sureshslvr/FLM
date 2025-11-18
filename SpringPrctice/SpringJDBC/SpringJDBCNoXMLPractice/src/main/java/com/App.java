package com;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext context= new AnnotationConfigApplicationContext(Config.class);
        JdbcTemplate template = context.getBean("template", JdbcTemplate.class);
        //template.update("insert into employees values(?,?,?,?,?,?)",1,"suresh",20,"suresh@gmail.com","9876543210",25000.00);
        template.update("update employees set email=? where empId=?","dummy@gmail.com",1);
        //template.update("delete from employees where empId=?",1);

        List<Employee> query = template.query("select * from employees", new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Employee(rs.getInt(1), rs.getString(2), rs.getInt(3),
                        rs.getString(4), rs.getString(5), rs.getDouble(6));
            }
        });
        System.out.println(query);

        List<Employee> emps = template.query("select * from employees",
                (rs, num) -> new Employee(rs.getInt(1), rs.getString(2), rs.getInt(3),
                        rs.getString(4), rs.getString(5), rs.getDouble(6)));
        System.out.println(emps);
    }
}
