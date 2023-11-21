package com.tourismmanagement.Model;

import com.tourismmanagement.Helper.DBConnector;
import com.tourismmanagement.Helper.Helper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Reservation {
    private int id;
    private int hotel_id;
    private int room_id;
    private String room_type;
    private Date start_date;
    private Date end_date;
    private String customer_name;
    private int customer_tc;
    private String customer_email;
    private double total_price;

    public Reservation() {
    }

    public Reservation(int id, int hotel_id, int room_id, Date start_date, Date end_date, String customer_name, int customer_tc, String customer_email, double total_price) {
        this.id = id;
        this.hotel_id = hotel_id;
        this.room_id = room_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.customer_name = customer_name;
        this.customer_tc = customer_tc;
        this.customer_email = customer_email;
        this.total_price = total_price;
    }

    public Reservation(int id, int hotel_id, int room_id, String room_type, Date start_date, Date end_date, String customer_name, int customer_tc, String customer_email, double total_price) {
        this.id = id;
        this.hotel_id = hotel_id;
        this.room_id = room_id;
        this.room_type = room_type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.customer_name = customer_name;
        this.customer_tc = customer_tc;
        this.customer_email = customer_email;
        this.total_price = total_price;
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

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getCustomer_tc() {
        return customer_tc;
    }

    public void setCustomer_tc(int customer_tc) {
        this.customer_tc = customer_tc;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public static ArrayList<Reservation> getList() {
        ArrayList<Reservation> reservationList = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        Reservation obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new Reservation();
                obj.setId(rs.getInt("id"));
                obj.setHotel_id(rs.getInt("hotel_id"));
                obj.setStart_date(rs.getDate("start_date"));
                obj.setEnd_date(rs.getDate("end_date"));
                obj.setCustomer_name(rs.getString("customer_name"));
                obj.setCustomer_tc(rs.getInt("customer_tc"));
                obj.setCustomer_email(rs.getString("customer_email"));
                obj.setTotal_price(rs.getDouble("total_price"));
                reservationList.add(obj);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservationList;
    }

    public static ArrayList<Reservation> getSpecialReservationList() {
        //Object[] col_reservation_list = {"İD", "Oda Tipi", "Müşteri Adı", "Müşteri Mail", "Müşteri TC", "Giriş Tarihi", "Çıkış Tarihi", "Ödenen/Ödenecek Toplam"};
        ArrayList<Reservation> reservationList = new ArrayList<>();
        String query = "SELECT " +
                "reservations.id AS 'İD', " +
                "rooms.room_type AS 'Oda Tipi', " +
                "reservations.customer_name AS 'Müşteri Adı', " +
                "reservations.customer_email AS 'Müşteri Mail', " +
                "reservations.customer_tc AS 'Müşteri TC', " +
                "reservations.start_date AS 'Giriş Tarihi', " +
                "reservations.end_date AS 'Çıkış Tarihi', " +
                "reservations.total_price AS 'Ödenen/Ödenecek Toplam' " +
                "FROM reservations " +
                "INNER JOIN rooms ON reservations.room_id = rooms.id";
        Reservation obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new Reservation();
                obj.setId(rs.getInt("İD"));
                obj.setRoom_type(rs.getString("Oda Tipi"));
                obj.setCustomer_name(rs.getString("Müşteri Adı"));
                obj.setCustomer_email(rs.getString("Müşteri Mail"));
                obj.setCustomer_tc(rs.getInt("Müşteri TC"));
                obj.setStart_date(rs.getDate("Giriş Tarihi"));
                obj.setEnd_date(rs.getDate("Çıkış Tarihi"));
                obj.setTotal_price(rs.getDouble("Ödenen/Ödenecek Toplam"));
                reservationList.add(obj);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservationList;
    }

    public static boolean add(int hotel_id, int room_id, Date start_date, Date end_date, String customer_name, int customer_tc, String customer_email, double total_price) {
        String reservationQuery = "INSERT INTO reservations (hotel_id, room_id, start_date, end_date, customer_name, customer_tc, customer_email, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String updateStockQuery = "UPDATE rooms SET stock_quantity = stock_quantity - 1 WHERE id = ?";

        try {
            // Rezervasyon ekleme
            PreparedStatement pstReservation = DBConnector.getInstance().prepareStatement(reservationQuery);
            pstReservation.setInt(1, hotel_id);
            pstReservation.setInt(2, room_id);
            pstReservation.setDate(3, (java.sql.Date) start_date);
            pstReservation.setDate(4, (java.sql.Date) end_date);
            pstReservation.setString(5, customer_name);
            pstReservation.setInt(6, customer_tc);
            pstReservation.setString(7, customer_email);
            pstReservation.setDouble(8, total_price);
            int responseReservation = pstReservation.executeUpdate();
            pstReservation.close();

            if (responseReservation == -1) {
                Helper.showMsg("Rezervasyon eklenirken bir hata oluştu");
                return false;
            }

            // Stok miktarını güncelleme
            PreparedStatement pstStockUpdate = DBConnector.getInstance().prepareStatement(updateStockQuery);
            pstStockUpdate.setInt(1, room_id);

            int responseStockUpdate = pstStockUpdate.executeUpdate();
            pstStockUpdate.close();

            if (responseStockUpdate == -1) {
                Helper.showMsg("Stok miktarı güncellenirken bir hata oluştu");
                return false;
            }

            Helper.showMsg("Rezervasyon başarıyla eklendi ve stok güncellendi");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(int id, int room_id) {
        String reservationQuery = "DELETE FROM reservations WHERE id = ?";
        String updateStockQuery = "UPDATE rooms SET stock_quantity = stock_quantity + 1 WHERE id = ?";
        try {
            // Rezervasyon silme
            PreparedStatement pstReservation = DBConnector.getInstance().prepareStatement(reservationQuery);
            pstReservation.setInt(1, id);
            int responseReservation = pstReservation.executeUpdate();
            pstReservation.close();

            if (responseReservation == -1) {
                Helper.showMsg("Rezervasyon silinirken bir hata oluştu");
                return false;
            }

            // Stok miktarını güncelleme
            PreparedStatement pstStockUpdate = DBConnector.getInstance().prepareStatement(updateStockQuery);
            pstStockUpdate.setInt(1, room_id);

            int responseStockUpdate = pstStockUpdate.executeUpdate();
            pstStockUpdate.close();

            if (responseStockUpdate == -1) {
                Helper.showMsg("Stok miktarı güncellenirken bir hata oluştu");
                return false;
            }

            Helper.showMsg("Rezervasyon başarıyla silindi ve stok güncellendi");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Reservation getReservationById(int reservationId) {
        String query = "SELECT * FROM reservations WHERE id = ?";
        Reservation obj;
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, reservationId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                obj = new Reservation();
                obj.setId(rs.getInt("id"));
                obj.setHotel_id(rs.getInt("hotel_id"));
                obj.setRoom_id(rs.getInt("room_id"));
                obj.setStart_date(rs.getDate("start_date"));
                obj.setEnd_date(rs.getDate("end_date"));
                obj.setCustomer_name(rs.getString("customer_name"));
                obj.setCustomer_tc(rs.getInt("customer_tc"));
                obj.setCustomer_email(rs.getString("customer_email"));
                obj.setTotal_price(rs.getDouble("total_price"));
                return obj;
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
