package com.example.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SqlDemo {
    public static void main(String[] args) throws Exception {

        String url  = "jdbc:mysql://localhost:3306/demo";
        String user = "root";
        String pass = "password";

        // CONNECT
        Connection conn = DriverManager.getConnection(url, user, pass);

        // READ
        String sql = "SELECT name, score FROM scores";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("name") + " -> " + rs.getInt("score"));
        }

        // INSERT
        String insert = "INSERT INTO scores (name, score) VALUES (?, ?)";
        PreparedStatement add = conn.prepareStatement(insert);
        add.setString(1, "Ivy");
        add.setInt(2, 15);
        add.executeUpdate();

        conn.close();
    }
}
