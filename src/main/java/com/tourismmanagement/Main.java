package com.tourismmanagement;

import com.tourismmanagement.Helper.DBConnector;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
// Veritabanına bağlan
        DBConnector.getInstance();

        String query = "SELECT * FROM hotelmanagement";
        ResultSet rs;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                if (!rs.next()) break;
                System.out.println(rs.getString(1) + " " + rs.getString(2)+ rs.getString(3)+ rs.getString(2));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

