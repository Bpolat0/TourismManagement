package com.tourismmanagement.Model;

import com.kitfox.svg.A;
import com.tourismmanagement.Helper.DBConnector;
import com.tourismmanagement.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Room {
    private int id;
    private int hotel_id;
    private int room_number;
    private String room_type;
    private int stock_quantity;
    private int bed_quantity;
    private int meter_square;
    private boolean television;
    private boolean minibar;
    private boolean game_console;
    private boolean safe;
    private boolean projection;

    public Room(int id, int hotel_id, int room_number, String room_type, int stock_quantity, int bed_quantity, int meter_square, boolean television, boolean minibar, boolean game_console, boolean safe, boolean projection) {
        this.id = id;
        this.hotel_id = hotel_id;
        this.room_number = room_number;
        this.room_type = room_type;
        this.stock_quantity = stock_quantity;
        this.bed_quantity = bed_quantity;
        this.meter_square = meter_square;
        this.television = television;
        this.minibar = minibar;
        this.game_console = game_console;
        this.safe = safe;
        this.projection = projection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public int getBed_quantity() {
        return bed_quantity;
    }

    public void setBed_quantity(int bed_quantity) {
        this.bed_quantity = bed_quantity;
    }

    public int getMeter_square() {
        return meter_square;
    }

    public void setMeter_square(int meter_square) {
        this.meter_square = meter_square;
    }

    public boolean isTelevision() {
        return television;
    }

    public void setTelevision(boolean television) {
        this.television = television;
    }

    public boolean isMinibar() {
        return minibar;
    }

    public void setMinibar(boolean minibar) {
        this.minibar = minibar;
    }

    public boolean isGame_console() {
        return game_console;
    }

    public void setGame_console(boolean game_console) {
        this.game_console = game_console;
    }

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public boolean isProjection() {
        return projection;
    }

    public void setProjection(boolean projection) {
        this.projection = projection;
    }

    public Room() {
    }

    public static int getRoomIdByNumber(int i) {
        int room_id = 0;
        String query = "SELECT id FROM rooms WHERE room_number = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, i);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                room_id = rs.getInt("id");
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return room_id;
    }
    public static ArrayList<Room> getRoomList() {
        ArrayList<Room> roomList = new ArrayList<>();
        String query = "SELECT * FROM rooms";
        Room obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new Room();
                obj.setId(rs.getInt("id"));
                obj.setHotel_id(rs.getInt("hotel_id"));
                obj.setRoom_number(rs.getInt("room_number"));
                obj.setRoom_type(rs.getString("room_type"));
                obj.setStock_quantity(rs.getInt("stock_quantity"));
                obj.setBed_quantity(rs.getInt("bed_quantity"));
                obj.setMeter_square(rs.getInt("meter_square"));
                obj.setTelevision(rs.getBoolean("television"));
                obj.setMinibar(rs.getBoolean("minibar"));
                obj.setGame_console(rs.getBoolean("game_console"));
                obj.setSafe(rs.getBoolean("safe"));
                obj.setProjection(rs.getBoolean("projection"));
                roomList.add(obj);
            }
            rs.close();
            st.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomList;
    }

    public static void addRoom(int hotel_id, int room_number, String room_type, int stock_quantity, int bed_quantity, int meter_square, boolean television, boolean minibar, boolean game_console, boolean safe, boolean projection) {
        String query = "INSERT INTO rooms (hotel_id, room_number, room_type, stock_quantity, bed_quantity, meter_square, television, minibar, game_console, safe, projection) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotel_id);
            pst.setInt(2, room_number);
            pst.setString(3, room_type);
            pst.setInt(4, stock_quantity);
            pst.setInt(5, bed_quantity);
            pst.setInt(6, meter_square);
            pst.setBoolean(7, television);
            pst.setBoolean(8, minibar);
            pst.setBoolean(9, game_console);
            pst.setBoolean(10, safe);
            pst.setBoolean(11, projection);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}




