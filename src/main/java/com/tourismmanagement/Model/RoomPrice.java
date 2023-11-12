package com.tourismmanagement.Model;

import com.tourismmanagement.Helper.DBConnector;
import com.tourismmanagement.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomPrice {
    private int id;
    private int room_id;
    private int board_type_id;
    private int period_id;
    private double adult_price;
    private double child_price;
    private String hotel_name;
    private int room_number;
    private String board_type_name;
    private String season;


    public RoomPrice() {
    }

    public RoomPrice(String hotel_name, int room_number, String board_type_name, String season, double adult_price, double child_price) {
        this.hotel_name = hotel_name;
        this.room_number = room_number;
        this.board_type_name = board_type_name;
        this.season = season;
        this.adult_price = adult_price;
        this.child_price = child_price;
    }

    public RoomPrice(int id, int room_id, int board_type_id, int period_id, double adult_price, double child_price) {
        this.id = id;
        this.room_id = room_id;
        this.board_type_id = board_type_id;
        this.period_id = period_id;
        this.adult_price = adult_price;
        this.child_price = child_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getBoard_type_id() {
        return board_type_id;
    }

    public void setBoard_type_id(int board_type_id) {
        this.board_type_id = board_type_id;
    }

    public int getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(int period_id) {
        this.period_id = period_id;
    }

    public double getAdult_price() {
        return adult_price;
    }

    public void setAdult_price(double adult_price) {
        this.adult_price = adult_price;
    }

    public double getChild_price() {
        return child_price;
    }

    public void setChild_price(double child_price) {
        this.child_price = child_price;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public String getBoard_type_name() {
        return board_type_name;
    }

    public void setBoard_type_name(String board_type_name) {
        this.board_type_name = board_type_name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public static boolean add(int id, int room_id, int board_type_id, int period_id, double adult_price, double child_price) {
        String query = "INSERT INTO pricelist (room_id, board_type_id, period_id, adult_price, child_price) VALUES (?, ?, ?, ?, ?)";
        boolean findRoomPrice = RoomPrice.getFetch(room_id, period_id, board_type_id);
        if (findRoomPrice) {
            Helper.showMsg("Bu oda için fiyat ayarlanmıştır!");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, room_id);
            pr.setInt(2, board_type_id);
            pr.setInt(3, period_id);
            pr.setDouble(4, adult_price);
            pr.setDouble(5, child_price);
            int response = pr.executeUpdate();

            if (response == -1) {
                Helper.showMsg("error");
            }else {
                Helper.showMsg("done");
            }
            return response != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean getFetch(int roomId, int periodId, int boardTypeId) {
        String query = "SELECT COUNT(*) FROM pricelist WHERE room_id = ? AND period_id = ? AND board_type_id = ?";

        try (PreparedStatement pst = DBConnector.getInstance().prepareStatement(query)) {
            pst.setInt(1, roomId);
            pst.setInt(2, periodId);
            pst.setInt(3, boardTypeId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ArrayList<RoomPrice> getList() {
        ArrayList<RoomPrice> roomPriceList = new ArrayList<>();
        String query = "SELECT * FROM pricelist";
        RoomPrice roomPrice;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                roomPrice = new RoomPrice();
                roomPrice.setId(rs.getInt("id"));
                roomPrice.setRoom_id(rs.getInt("room_id"));
                roomPrice.setBoard_type_id(rs.getInt("board_type_id"));
                roomPrice.setPeriod_id(rs.getInt("period_id"));
                roomPrice.setAdult_price(rs.getDouble("adult_price"));
                roomPrice.setChild_price(rs.getDouble("child_price"));
                roomPriceList.add(roomPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomPriceList;
    }

    public static ArrayList<RoomPrice> getSpecialList() {
        ArrayList<RoomPrice> roomPriceList = new ArrayList<>();
        String query = "SELECT h.name AS hotel_name, r.room_number, bt.board_type_name, " +
                "CONCAT(p.start_date, ' - ', p.end_date) AS season, " +
                "pl.adult_price, pl.child_price " +
                "FROM hotels h " +
                "JOIN rooms r ON h.id = r.hotel_id " +
                "JOIN pricelist pl ON r.id = pl.room_id " +
                "JOIN hotel_boardtypes hb ON r.hotel_id = hb.hotel_id AND pl.board_type_id = hb.board_type_id " +
                "JOIN board_types bt ON hb.board_type_id = bt.id " +
                "JOIN periods p ON pl.period_id = p.period_id";
        RoomPrice roomPrice;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                roomPrice = new RoomPrice();
                roomPrice.setHotel_name(rs.getString("hotel_name"));
                roomPrice.setRoom_number(rs.getInt("room_number"));
                roomPrice.setBoard_type_name(rs.getString("board_type_name"));
                roomPrice.setSeason(rs.getString("season"));
                roomPrice.setAdult_price(rs.getDouble("adult_price"));
                roomPrice.setChild_price(rs.getDouble("child_price"));
                roomPriceList.add(roomPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomPriceList;
    }


}


