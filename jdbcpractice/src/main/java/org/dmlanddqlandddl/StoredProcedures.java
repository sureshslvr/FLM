package org.dmlanddqlandddl;

import org.util.JdbcUtil;

import java.sql.*;

public class StoredProcedures {
    public static void main(String[] args) throws SQLException {

        JdbcUtil j=new JdbcUtil();
        Connection connection = j.getConnection();
       // CallableStatement statement = connection.prepareCall("{CALL getALLEmployees()}");
        //In param
        CallableStatement statement = connection.prepareCall("{CALL getALLEmployeeById(?)}");
        statement.setInt(1,4);
        ResultSet r = statement.executeQuery();
        while (r.next()) {
            System.out.println(r.getInt(1) + "\t" + r.getString(2) +
                    "\t" + r.getString(3) + "\t" + r.getDouble(4));
        }

        //out param
        CallableStatement statement1 = connection.prepareCall("{CALL getSalaryById(?,?)}");
        statement1.setInt(1,4);
        statement1.registerOutParameter(2,Types.DOUBLE);
        statement1.execute();
        double aDouble = statement1.getDouble(2);
        System.out.println("salary "+ aDouble);


        // stored procedure creation
        PreparedStatement statement2;
        statement2=connection.prepareStatement("DROP PROCEDURE IF EXISTS increaseSalary;");
        statement2.execute();
        statement2= connection.prepareStatement(
                    "create procedure increaseSalary(IN empId int,IN incrementAmount int , INOUT currentSal Double) \n" +
                    "Begin \n" +
                    " select salary into currentSal from employees where id=empID;\n" +
                    " set currentSal=currentSal+incrementAmount;\n" +
                    "update employees set salary=currentSal where id=empId;\n" +
                    "End ");

        statement2.execute();
        System.out.println("procedure created");
        CallableStatement increaseSal = connection.prepareCall("{CALL increaseSalary(?,?,?)}");
        increaseSal.setInt(1,4);
        increaseSal.setInt(2,5000);
        increaseSal.registerOutParameter(3,Types.DOUBLE);

        increaseSal.execute();
        double increasedSal = increaseSal.getDouble(3);
        System.out.println("new sal "+ increasedSal);

    }
}

