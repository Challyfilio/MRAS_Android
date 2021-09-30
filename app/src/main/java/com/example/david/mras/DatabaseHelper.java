package com.example.david.mras;

import android.util.Log;

import java.sql.*;

public class DatabaseHelper {
    public static Connection openConnection() {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3307/mras?useUnicode=true&characterEncoding=UTF8", "Challyfilio", "123456");
            //conn = DriverManager.getConnection("jdbc:mysql://192.168.10.240:3307/mras?useUnicode=true&characterEncoding=UTF8", "Challyfilio", "123456");
            Log.d("DatabaseHelper", "linked " + conn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            conn = null;
        }
        return conn;
    }

    public static ResultSet getResult(Connection conn, String sql) {
        Statement stat;
        ResultSet rs;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            rs = null;
        }
        return rs;
    }

    public static Statement exeStat(Connection conn, String sql) {
        Statement stat;
        try {
            stat = conn.createStatement();
            stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            stat = null;
        }
        return stat;
    }
}
