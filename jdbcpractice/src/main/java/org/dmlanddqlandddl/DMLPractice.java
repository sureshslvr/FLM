package org.dmlanddqlandddl;

import org.util.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DMLPractice {
    public static void main(String[] args) throws  SQLException {
        try {
            JdbcUtil jdbcUtil=new JdbcUtil();
           Connection connection= jdbcUtil.getConnection();
           Statement statement = connection.createStatement();

           String query="insert into employees value(4,'suresh','junior dev',30000)";

            //step5:execute
            //statement.executeUpdate(query);
            System.out.println("data added");


            String updateQuery="update employees set position='developer' where id=4";
            statement.executeUpdate(updateQuery);
            System.out.println("data updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
