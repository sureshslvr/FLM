package org.dmlanddqlandddl;

import org.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchProcessing {
    public static void main(String[] args) throws SQLException {
        JdbcUtil j=new JdbcUtil();
        Connection connection = j.getConnection();
        PreparedStatement statement = connection.prepareStatement("insert into employees values(?,?,?,?)");

        statement.setInt(1,6);
        statement.setString(2,"ramesh");
        statement.setString(3,"tester");
        statement.setDouble(4,42000.00);
        statement.addBatch();

        statement.setInt(1,7);
        statement.setString(2,"niranjan");
        statement.setString(3,"telecaller");
        statement.setDouble(4,22000.00);
        statement.addBatch();

        statement.setInt(1,8);
        statement.setString(2,"anil");
        statement.setString(3,"hr");
        statement.setDouble(4,72000.00);
        statement.addBatch();

        statement.executeBatch();
        System.out.println("data updated");
    }
}
