package org.dmlanddqlandddl;

import org.util.JdbcUtil;

import java.sql.*;

public class MetaData {

    public static void main(String[] args) throws SQLException {
        ResultSetMetaData metaData = getResultSetMetaData();
        System.out.println(metaData.getColumnCount());//to get the column count in table
        System.out.println(metaData.getColumnName(1));//to get the column name
        System.out.println(metaData.getColumnTypeName(1));//to get the column type
        System.out.println(metaData.getTableName(4));//to get the table name
        System.out.println(metaData.isNullable(1));//to check whether the column is nullable or not
        System.out.println(metaData.isAutoIncrement(1));
        System.out.println(metaData.isReadOnly(1));

    }

    private static ResultSetMetaData getResultSetMetaData() throws SQLException {
        JdbcUtil jdbcUtil=new JdbcUtil();
        Connection connection = jdbcUtil.getConnection();
        String query="select * from employees";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        /*using while loop and resultSet we can get all the values of the table like below*/
       /* while (resultSet.next()){
            System.out.println(resultSet.getInt(1)+"\t"+resultSet.getString(2)
            +"\t"+resultSet.getString(3)+"\t"+resultSet.getString(4));
        }*/

        //to get the metadata about table we have ResultSetMetaData
        return resultSet.getMetaData();
    }
}
