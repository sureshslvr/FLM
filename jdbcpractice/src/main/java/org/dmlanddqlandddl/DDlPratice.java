package org.dmlanddqlandddl;

import org.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DDlPratice {
    public static void main(String[] args) throws SQLException {
        JdbcUtil jdbcUtil=new JdbcUtil();
        Connection connection=jdbcUtil.getConnection();
        String createTable="create table users(username varchar(20),password varchar(20))";
        PreparedStatement statement = connection.prepareStatement(createTable);
        statement.executeUpdate();
        System.out.println("table created");
    }
}
