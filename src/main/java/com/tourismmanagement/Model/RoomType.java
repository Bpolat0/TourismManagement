package com.tourismmanagement.Model;

import com.tourismmanagement.Helper.DBConnector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RoomType {
    private int id;
    private String room_type_name;

    public RoomType(int id, String room_type_name) {
        this.id = id;
        this.room_type_name = room_type_name;
    }

    public RoomType() {

    }

    public int getId() {
        return id;
    }

    public String getRoom_type_name() {
        return room_type_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoom_type_name(String room_type_name) {
        this.room_type_name = room_type_name;
    }

    public static ArrayList<RoomType> getOnlyRoomTypeList(){
        ArrayList<RoomType> roomTypeList = new ArrayList<>();
        String query = "SELECT * FROM hotel_room_types";
        RoomType obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                obj = new RoomType();
                obj.setId(rs.getInt("id"));
                obj.setRoom_type_name(rs.getString("room_type_name"));
                roomTypeList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomTypeList;
    }

}