package com.tourismmanagement.Model;

import com.tourismmanagement.Helper.DBConnector;
import com.tourismmanagement.Helper.Helper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Hotel {
    private int id;
    private String name;
    private String city;
    private String region;
    private String address;
    private String email;
    private String phone;
    private String star;
    private boolean freePark;
    private boolean freeWifi;
    private boolean swimmingPool;
    private boolean fitnessCenter;
    private boolean hotelConcierge;
    private boolean spa;
    private boolean roomService;
    private boolean ultraAllInclusive;
    private boolean allInclusive;
    private boolean bedAndBreakfast;
    private boolean fullBoard;
    private boolean halfBoard;
    private boolean roomOnly;
    private boolean nonAlcoholFull;

    public Hotel() {
    }

    public Hotel(int id, String name, String city, String region, String address, String email, String phone, String star, boolean freePark, boolean freeWifi, boolean swimmingPool, boolean fitnessCenter, boolean hotelConcierge, boolean spa, boolean roomService, boolean ultraAllInclusive, boolean allInclusive, boolean bedAndBreakfast, boolean fullBoard, boolean halfBoard, boolean roomOnly, boolean nonAlcoholFull) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.region = region;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.star = star;
        this.freePark = freePark;
        this.freeWifi = freeWifi;
        this.swimmingPool = swimmingPool;
        this.fitnessCenter = fitnessCenter;
        this.hotelConcierge = hotelConcierge;
        this.spa = spa;
        this.roomService = roomService;
        this.ultraAllInclusive = ultraAllInclusive;
        this.allInclusive = allInclusive;
        this.bedAndBreakfast = bedAndBreakfast;
        this.fullBoard = fullBoard;
        this.halfBoard = halfBoard;
        this.roomOnly = roomOnly;
        this.nonAlcoholFull = nonAlcoholFull;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public boolean isFreePark() {
        return freePark;
    }

    public void setFreePark(boolean freePark) {
        this.freePark = freePark;
    }

    public boolean isFreeWifi() {
        return freeWifi;
    }

    public void setFreeWifi(boolean freeWifi) {
        this.freeWifi = freeWifi;
    }

    public boolean isSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(boolean swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public boolean isFitnessCenter() {
        return fitnessCenter;
    }

    public void setFitnessCenter(boolean fitnessCenter) {
        this.fitnessCenter = fitnessCenter;
    }

    public boolean isHotelConcierge() {
        return hotelConcierge;
    }

    public void setHotelConcierge(boolean hotelConcierge) {
        this.hotelConcierge = hotelConcierge;
    }

    public boolean isSpa() {
        return spa;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public boolean isRoomService() {
        return roomService;
    }

    public void setRoomService(boolean roomService) {
        this.roomService = roomService;
    }

    public boolean isUltraAllInclusive() {
        return ultraAllInclusive;
    }

    public void setUltraAllInclusive(boolean ultraAllInclusive) {
        this.ultraAllInclusive = ultraAllInclusive;
    }

    public boolean isAllInclusive() {
        return allInclusive;
    }

    public void setAllInclusive(boolean allInclusive) {
        this.allInclusive = allInclusive;
    }

    public boolean isBedAndBreakfast() {
        return bedAndBreakfast;
    }

    public void setBedAndBreakfast(boolean bedAndBreakfast) {
        this.bedAndBreakfast = bedAndBreakfast;
    }

    public boolean isFullBoard() {
        return fullBoard;
    }

    public void setFullBoard(boolean fullBoard) {
        this.fullBoard = fullBoard;
    }

    public boolean isHalfBoard() {
        return halfBoard;
    }

    public void setHalfBoard(boolean halfBoard) {
        this.halfBoard = halfBoard;
    }

    public boolean isRoomOnly() {
        return roomOnly;
    }

    public void setRoomOnly(boolean roomOnly) {
        this.roomOnly = roomOnly;
    }

    public boolean isNonAlcoholFull() {
        return nonAlcoholFull;
    }

    public void setNonAlcoholFull(boolean nonAlcoholFull) {
        this.nonAlcoholFull = nonAlcoholFull;
    }

    public static int getHotelIdByName(String string) {
        int hotel_id = 0;
        String query = "SELECT id FROM hotels WHERE name = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, string);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                hotel_id = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hotel_id;
    }

    public static ArrayList<Hotel> getList() {
        ArrayList<Hotel> hotelList = new ArrayList<>();
        String query = "SELECT * FROM hotels";
        Hotel obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new Hotel();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setCity(rs.getString("city"));
                obj.setRegion(rs.getString("region"));
                obj.setAddress(rs.getString("address"));
                obj.setEmail(rs.getString("email"));
                obj.setPhone(rs.getString("phone"));
                obj.setStar(rs.getString("star"));
                obj.setFreePark(rs.getBoolean("freePark"));
                obj.setFreeWifi(rs.getBoolean("freeWifi"));
                obj.setSwimmingPool(rs.getBoolean("swimmingPool"));
                obj.setFitnessCenter(rs.getBoolean("fitnessCenter"));
                obj.setHotelConcierge(rs.getBoolean("hotelConcierge"));
                obj.setSpa(rs.getBoolean("spa"));
                obj.setRoomService(rs.getBoolean("roomService"));
                obj.setUltraAllInclusive(rs.getBoolean("ultraAllInclusive"));
                obj.setAllInclusive(rs.getBoolean("allInclusive"));
                obj.setBedAndBreakfast(rs.getBoolean("bedAndBreakfast"));
                obj.setFullBoard(rs.getBoolean("fullBoard"));
                obj.setHalfBoard(rs.getBoolean("halfBoard"));
                obj.setRoomOnly(rs.getBoolean("roomOnly"));
                obj.setNonAlcoholFull(rs.getBoolean("nonAlcoholFull"));
                hotelList.add(obj);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hotelList;
    }


    public static boolean add(String name, String city, String region, String address, String email, String phone, String star, boolean freePark, boolean freeWifi, boolean swimmingPool, boolean fitnessCenter, boolean hotelConcierge, boolean spa, boolean roomService, boolean ultraAllInclusive, boolean allInclusive, boolean bedAndBreakfast, boolean fullBoard, boolean halfBoard, boolean roomOnly, boolean nonAlcoholFull) {
        String query = "INSERT INTO hotels (name, city, region, address, email, phone, star,freePark, freeWifi, swimmingPool, fitnessCenter, hotelConcierge, spa, roomService, ultraAllInclusive, allInclusive, bedAndBreakfast, fullBoard, halfBoard,roomOnly, nonAlcoholFull ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Hotel findHotel = Hotel.getFetch(name, email, phone);
        //, boolean ultraAllInclusive, boolean allInclusive, boolean bedAndBreakfast, boolean fullBoard, boolean halfBoard, boolean roomOnly, boolean nonAlcoholFull
        if (findHotel != null) {
            Helper.showMsg("Lütfen otelin isim, telefon ve e-mail bilgilerini değiştiriniz !");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, city);
            pr.setString(3, region);
            pr.setString(4, address);
            pr.setString(5, email);
            pr.setString(6, phone);
            pr.setString(7, star);
            pr.setBoolean(8, freePark);
            pr.setBoolean(9, freeWifi);
            pr.setBoolean(10, swimmingPool);
            pr.setBoolean(11, fitnessCenter);
            pr.setBoolean(12, hotelConcierge);
            pr.setBoolean(13, spa);
            pr.setBoolean(14, roomService);
            pr.setBoolean(15, ultraAllInclusive);
            pr.setBoolean(16, allInclusive);
            pr.setBoolean(17, bedAndBreakfast);
            pr.setBoolean(18, fullBoard);
            pr.setBoolean(19, halfBoard);
            pr.setBoolean(20, roomOnly);
            pr.setBoolean(21, nonAlcoholFull);
            int response = pr.executeUpdate();

            if (response == -1) {
                Helper.showMsg("error");
            }
            return response != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Hotel getFetch(String name, String email, String phone) {
        Hotel obj = null;
        String query = "SELECT * FROM hotels WHERE name = ? AND email = ? AND phone = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, email);
            pr.setString(3, phone);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new Hotel();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setCity(rs.getString("city"));
                obj.setRegion(rs.getString("region"));
                obj.setAddress(rs.getString("address"));
                obj.setEmail(rs.getString("email"));
                obj.setPhone(rs.getString("phone"));
                obj.setStar(rs.getString("star"));
                obj.setFreePark(rs.getBoolean("freePark"));
                obj.setFreeWifi(rs.getBoolean("freeWifi"));
                obj.setSwimmingPool(rs.getBoolean("swimmingPool"));
                obj.setFitnessCenter(rs.getBoolean("fitnessCenter"));
                obj.setHotelConcierge(rs.getBoolean("hotelConcierge"));
                obj.setSpa(rs.getBoolean("spa"));
                obj.setRoomService(rs.getBoolean("roomService"));


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static boolean update(int id, String name, String city, String region, String address, String email, String phone, String star, boolean freePark, boolean freeWifi, boolean swimmingPool, boolean fitnessCenter, boolean hotelConcierge, boolean spa, boolean roomService) {
        String query = "UPDATE hotels SET name = ?, city = ?, region = ?, address = ?, email = ?, phone = ?, star = ?, freePark = ?, freeWifi = ?, swimmingPool = ?, fitnessCenter = ?, hotelConcierge = ?, spa = ?, roomService = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, city);
            pr.setString(3, region);
            pr.setString(4, address);
            pr.setString(5, email);
            pr.setString(6, phone);
            pr.setString(7, star);
            pr.setBoolean(8, freePark);
            pr.setBoolean(9, freeWifi);
            pr.setBoolean(10, swimmingPool);
            pr.setBoolean(11, fitnessCenter);
            pr.setBoolean(12, hotelConcierge);
            pr.setBoolean(13, spa);
            pr.setBoolean(14, roomService);
            pr.setInt(15, id);

            int response = pr.executeUpdate();

            if (response == -1) {
                Helper.showMsg("error");
            } else {
                Helper.showMsg("Otel bilgileri güncellendi!");
            }
            return response != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM hotels WHERE id = ?";
        PreparedStatement pr = null;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            return false;
        }
    }
}
