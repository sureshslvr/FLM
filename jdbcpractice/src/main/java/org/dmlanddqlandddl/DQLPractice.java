package org.dmlanddqlandddl;

import org.util.JdbcUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DQLPractice {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("enter username");
        String username=sc.next();
        System.out.println("enter password");
        String password=sc.next();
        try {
            JdbcUtil jdbcUtil=new JdbcUtil();
            Connection connection=jdbcUtil.getConnection();
            Statement statement=connection.createStatement();
            String query="SELECT * FROM users WHERE username='"+username+"' AND password='"+password+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                System.out.println("logged in");
            }else {
                System.out.println("invalid credentials");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printAllUsers() {
        try {
            JdbcUtil jdbcUtil=new JdbcUtil();
            Connection connection=jdbcUtil.getConnection();
            Statement statement=connection.createStatement();
            String query= "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);
            //print all users
            System.out.println("name\tpass");
            while (resultSet.next()){
                String name=resultSet.getString(1);
                String pass=resultSet.getString(2);
                System.out.println(name+"\t"+pass);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
