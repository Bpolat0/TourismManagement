package com.tourismmanagement.Model;

import com.tourismmanagement.Helper.DBConnector;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BoardType {
    private int id;
    private int hotel_id;
    private int board_type_id;
    private String hotel_board_type;
    private String board_type_name;

    public BoardType(int id, int hotel_id, int board_type_id) {
        this.id = id;
        this.hotel_id = hotel_id;
        this.board_type_id = board_type_id;
    }

    public BoardType() {

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

    public int getBoard_type_id() {
        return board_type_id;
    }

    public void setBoard_type_id(int board_type_id) {
        this.board_type_id = board_type_id;
    }

    public String getHotel_board_type() {
        return hotel_board_type;
    }

    public void setHotel_board_type(String hotel_board_type) {
        this.hotel_board_type = hotel_board_type;
    }

    public String getBoard_type_name() {
        return board_type_name;
    }

    public void setBoard_type_name(String board_type_name) {
        this.board_type_name = board_type_name;
    }

    public static boolean addBoardTypeByHotelID(int hotel_id, int board_type_id){
        //same hotel board type check
        BoardType.findBoardTypeByHotelID(hotel_id, board_type_id);

        if (BoardType.findBoardTypeByHotelID(hotel_id, board_type_id) == true) {
            JOptionPane.showMessageDialog(null, "Ayı aynı pansiyon tipi ekleyemezsiniz!");
            return false;
        }

        String query = "INSERT INTO hotel_boardtypes (hotel_id, board_type_id) VALUES (?, ?)";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotel_id);
            pst.setInt(2, board_type_id);
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean findBoardTypeByHotelID(int hotelId, int boardTypeId) {
        String query = "SELECT * FROM hotel_boardtypes WHERE hotel_id = ? AND board_type_id = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotelId);
            pst.setInt(2, boardTypeId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean deleteBoardTypes(int hotelID, int boardTypeID) {
        String query = "DELETE FROM hotel_boardtypes WHERE hotel_id = ? AND board_type_id = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotelID);
            pst.setInt(2, boardTypeID);
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ArrayList<BoardType> getOnlyBoardTypeList(){
        ArrayList<BoardType> boardTypeList = new ArrayList<>();
        String query = "SELECT * FROM board_types";
        BoardType obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                obj = new BoardType();
                obj.setId(rs.getInt("id"));
                obj.setBoard_type_name(rs.getString("board_type_name"));
                boardTypeList.add(obj);

            }
            //close connection
            rs.close();
            st.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boardTypeList;
    }

    public static int getBoardTypeIdByName(String string) {
        int board_type_id = 0;
        String query = "SELECT id FROM board_types WHERE board_type_name = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setString(1, string);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                board_type_id = rs.getInt("id");
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return board_type_id;
    }

    public static ArrayList<BoardType> getHotelTypeNameByID(String id) {
        ArrayList<BoardType> boardTypeList = new ArrayList<>();
        String query = "SELECT board_types.board_type_name FROM hotel_boardtypes LEFT JOIN board_types ON board_type_id = board_types.id WHERE hotel_boardtypes.hotel_id=?";
        BoardType obj;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, id);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                obj = new BoardType();
                obj.hotel_board_type = rs.getString("board_type_name");
                boardTypeList.add(obj);

            }
            //close connection
            rs.close();
            pr.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boardTypeList;
    }

    public static ArrayList<BoardType> getList() {
        ArrayList<BoardType> boardTypeList = new ArrayList<>();
        String query = "SELECT * FROM `hotel_boardtypes`";
        BoardType obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                obj = new BoardType();
                obj.setId(rs.getInt("id"));
                obj.setHotel_id(rs.getInt("hotel_id"));
                obj.setBoard_type_id(rs.getInt("board_type_id"));
                boardTypeList.add(obj);
            }
            //close connection
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boardTypeList;
    }


    public static List<String> getPansiyonTipleriFromDatabase(int otel, int oda, String donem) {
        List<String> pansiyonTipleri = new ArrayList<>();
        String query = "SELECT DISTINCT b.board_type_name " +
                "FROM hotel_boardtypes hb " +
                "JOIN board_types b ON hb.board_type_id = b.id " +
                "JOIN pricelist p ON hb.id = p.board_type_id " +
                "WHERE hb.hotel_id = ? AND p.room_id = ? AND p.period_id IN (SELECT period_id FROM periods WHERE hotel_id = ? AND start_date <= ? AND end_date >= ?)";

        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, otel);
            pst.setInt(2, oda);
            pst.setInt(3, otel);
            pst.setString(4, donem.split(" - ")[0]); // start_date
            pst.setString(5, donem.split(" - ")[1]); // end_date
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String pansiyonTipi = rs.getString("board_type_name");
                pansiyonTipleri.add(pansiyonTipi);
            }

            //close connection
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pansiyonTipleri;
    }

    public static boolean deleteBoardTypesForHotel(int id) {
        String query = "DELETE FROM hotel_boardtypes WHERE hotel_id = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
