package com.tourismmanagement.Model;

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
    private String hotel_name;
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

    public Room() {
    }



    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
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

    public static ArrayList<Room> getSpecialRoomList(){
        ArrayList<Room> roomList = new ArrayList<>();
        String query = "SELECT h.name AS 'Otel İsmi', r.room_type AS 'Oda Tipi', r.bed_quantity AS 'Yatak Sayısı', r.meter_square AS 'Metre Kare', " +
                "r.television AS 'Televizyon', r.minibar AS 'Minibar', r.game_console AS 'Oyun Konsolu', r.safe AS 'Kasa', r.projection AS 'Projeksiyon' " +
                "FROM rooms r " +
                "JOIN hotels h ON r.hotel_id = h.id";
        Room obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new Room();
                obj.setHotel_name(rs.getString("Otel İsmi"));
                obj.setRoom_type(rs.getString("Oda Tipi"));
                obj.setBed_quantity(rs.getInt("Yatak Sayısı"));
                obj.setMeter_square(rs.getInt("Metre Kare"));
                obj.setTelevision(rs.getBoolean("Televizyon"));
                obj.setMinibar(rs.getBoolean("Minibar"));
                obj.setGame_console(rs.getBoolean("Oyun Konsolu"));
                obj.setSafe(rs.getBoolean("Kasa"));
                obj.setProjection(rs.getBoolean("Projeksiyon"));
                roomList.add(obj);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomList;
    }

    public static ArrayList<Room> roomSearchList(String city, Date startDate, Date endDate) {
        ArrayList<Room> roomList = new ArrayList<>();
        String query = "SELECT h.name AS 'Otel İsmi', r.room_type AS 'Oda Tipi', r.bed_quantity AS 'Yatak Sayısı', r.meter_square AS 'Metre Kare', " +
                "r.television AS 'Televizyon', r.minibar AS 'Minibar', r.game_console AS 'Oyun Konsolu', r.safe AS 'Kasa', r.projection AS 'Projeksiyon' " +
                "FROM rooms r " +
                "JOIN hotels h ON r.hotel_id = h.id " +
                "JOIN periods p ON p.hotel_id = h.id " +
                "WHERE h.city = ? " +
                "AND p.start_date <= ? " +
                "AND p.end_date >= ? " +
                "AND r.stock_quantity > 0";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, city);
            pr.setDate(2, new java.sql.Date(startDate.getTime()));
            pr.setDate(3, new java.sql.Date(endDate.getTime()));

            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setHotel_name(rs.getString("Otel İsmi"));
                room.setRoom_type(rs.getString("Oda Tipi"));
                room.setBed_quantity(rs.getInt("Yatak Sayısı"));
                room.setMeter_square(rs.getInt("Metre Kare"));
                room.setTelevision(rs.getBoolean("Televizyon"));
                room.setMinibar(rs.getBoolean("Minibar"));
                room.setGame_console(rs.getBoolean("Oyun Konsolu"));
                room.setSafe(rs.getBoolean("Kasa"));
                room.setProjection(rs.getBoolean("Projeksiyon"));
                roomList.add(room);
            }
            rs.close();
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomList;
    }

    public static boolean addRoom(int hotel_id, String room_type, int stock_quantity, int bed_quantity, int meter_square, boolean television, boolean minibar, boolean game_console, boolean safe, boolean projection) {
        String query = "INSERT INTO rooms (hotel_id, room_type, stock_quantity, bed_quantity, meter_square, television, minibar, game_console, safe, projection) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        if (stock_quantity == 0) {
            Helper.showMsg("Stok sayısı 0 olamaz!");
            return false;
        } else if (bed_quantity == 0) {
            Helper.showMsg("Yatak sayısı 0 olamaz!");
            return false;
        } else if (meter_square == 0) {
            Helper.showMsg("Metre kare 0 olamaz!");
            return false;
        } else if (room_type.equals("")) {
            Helper.showMsg("Oda tipi boş bırakılamaz!");
            return false;
        } else if (hotel_id == 0) {
            Helper.showMsg("Otel seçimi yapılmadı!");
            return false;
        } else if (stock_quantity < 0) {
            Helper.showMsg("Stok sayısı negatif olamaz!");
            return false;
        } else if (bed_quantity < 0) {
            Helper.showMsg("Yatak sayısı negatif olamaz!");
            return false;
        } else if (meter_square < 0) {
            Helper.showMsg("Metre kare negatif olamaz!");
            return false;
        }
        //aynı oda tipi aynı otelde olamaz
        if (roomTypeExist(hotel_id, room_type)) {
            Helper.showMsg("Bu oda tipi zaten mevcut!");
            return false;
        }
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotel_id);
            pst.setString(2, room_type);
            pst.setInt(3, stock_quantity);
            pst.setInt(4, bed_quantity);
            pst.setInt(5, meter_square);
            pst.setBoolean(6, television);
            pst.setBoolean(7, minibar);
            pst.setBoolean(8, game_console);
            pst.setBoolean(9, safe);
            pst.setBoolean(10, projection);
            int result = pst.executeUpdate();

            if (result == -1) {
                Helper.showMsg("error");
            } else {
                Helper.showMsg("done");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean updateRoom(int id, int hotelId, String roomType, int i, int i1, int i2, boolean television, boolean minibar, boolean gameConsole, boolean safe, boolean projection) {
        //update room
        String query = "UPDATE rooms SET hotel_id = ?, room_type = ?, stock_quantity = ?, bed_quantity = ?, meter_square = ?, television = ?, minibar = ?, game_console = ?, safe = ?, projection = ? WHERE id = ?";
        if (i == 0) {
            Helper.showMsg("Stok sayısı 0 olamaz!");
            return false;
        } else if (i1 == 0) {
            Helper.showMsg("Yatak sayısı 0 olamaz!");
            return false;
        } else if (i2 == 0) {
            Helper.showMsg("Metre kare 0 olamaz!");
            return false;
        } else if (roomType.equals("")) {
            Helper.showMsg("Oda tipi boş bırakılamaz!");
            return false;
        } else if (hotelId == 0) {
            Helper.showMsg("Otel seçimi yapılmadı!");
            return false;
        } else if (i < 0) {
            Helper.showMsg("Stok sayısı negatif olamaz!");
            return false;
        } else if (i1 < 0) {
            Helper.showMsg("Yatak sayısı negatif olamaz!");
            return false;
        } else if (i2 < 0) {
            Helper.showMsg("Metre kare negatif olamaz!");
            return false;
        }
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotelId);
            pst.setString(2, roomType);
            pst.setInt(3, i);
            pst.setInt(4, i1);
            pst.setInt(5, i2);
            pst.setBoolean(6, television);
            pst.setBoolean(7, minibar);
            pst.setBoolean(8, gameConsole);
            pst.setBoolean(9, safe);
            pst.setBoolean(10, projection);
            pst.setInt(11, id);
            int result = pst.executeUpdate();

            if (result == -1) {
                Helper.showMsg("error");
            } else {
                Helper.showMsg("done");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean deleteRoom(int id) {
        String query = "DELETE FROM rooms WHERE id = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, id);
            int result = pst.executeUpdate();

            if (result == -1) {
                Helper.showMsg("error");
            } else {
                Helper.showMsg("done");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean roomTypeExist(int hotelId, String roomType) {
        boolean exist = false;
        String query = "SELECT * FROM rooms WHERE hotel_id = ? AND room_type = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotelId);
            pst.setString(2, roomType);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                exist = true;
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exist;
    }

}




