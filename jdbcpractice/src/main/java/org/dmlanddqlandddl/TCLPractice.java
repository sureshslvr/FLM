package org.dmlanddqlandddl;

import org.util.JdbcUtil;

import java.sql.*;

public class TCLPractice {

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        PreparedStatement statement;
        try {
            JdbcUtil j = new JdbcUtil();
            connection = j.getConnection();

            connection.setAutoCommit(false);//to set auto commit false

            System.out.println("table before update");
            statement = connection.prepareStatement("select * from employees");
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                System.out.println(r.getInt(1) + "\t" + r.getString(2) +
                        "\t" + r.getString(3) + "\t" + r.getDouble(4));
            }
            statement = connection.prepareStatement("update employees set name =? where id=?");
            statement.setString(1, "nikki");
            statement.setInt(2, 2);
            statement.executeUpdate();
            System.out.println("data updated");

            //throw new RuntimeException();//to test rollback

            //since auto commit is turned off data won't reflect until commit was done
            connection.commit();//to commit the changes
            System.out.println("table after commit");
            statement = connection.prepareStatement("select * from employees");
            ResultSet r1 = statement.executeQuery();
            while (r1.next()) {
                System.out.println(r1.getInt(1) + "\t" + r1.getString(2) +
                        "\t" + r1.getString(3) + "\t" + r1.getDouble(4));
            }

        } catch (Exception e) {

            //this block is demonstrated to show if any exception come in try block the data is rolled back
            //like in realtime you are sending money to someone and money debited from your account but not credited to
            //other person account, so money should be returned back
            connection.rollback();
            System.out.println("table after rollback");
            statement = connection.prepareStatement("select * from employees");
            ResultSet r1 = statement.executeQuery();
            while (r1.next()) {
                System.out.println(r1.getInt(1) + "\t" + r1.getString(2) +
                        "\t" + r1.getString(3) + "\t" + r1.getDouble(4));
            }


        }

    }
}
