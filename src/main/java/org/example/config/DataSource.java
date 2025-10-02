package org.example.config;

import java.sql.*;

public class DataSource {

    private static String url = "jdbc:mysql://localhost:3306/hockeymanager";
    private static String user = "root";
    private static String pwd = "test1234";


    public static Connection getConnection(){
        Connection con = null;

        try{
            con = DriverManager.getConnection(url,user,pwd);

        }catch(SQLException e){
            e.printStackTrace();
        }
        return con;

    }


    public static void releaseConnection(Connection con, PreparedStatement ps){
        try{
            ps.close();
            con.close();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void releaseConnection(Connection con, PreparedStatement ps, ResultSet rs){
        try {
            rs.close();
            ps.close();
            con.close();
        }catch(SQLException e ){
            e.printStackTrace();
        }
    }




}
