package com.tourismmanagement.Model;

import com.tourismmanagement.Helper.DBConnector;
import com.tourismmanagement.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Period {
    private int periodId;
    private int hotelId;
    private Date startDate;
    private Date endDate;
    private String periodName;

    public Period() {
    }

    public Period(int periodId, int hotelId, Date startDate, Date endDate, String periodName) {
        this.periodId = periodId;
        this.hotelId = hotelId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodName = periodName;
    }



    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String startEndDateToString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);
        return "Başlangıç: " + formattedStartDate + " - Bitiş: " + formattedEndDate;
    }

    public static int getPeriodIdByDateRange(int hotelId, Date[] dates) {
        String query = "SELECT period_id FROM periods WHERE hotel_id = ? AND start_date = ? AND end_date = ?";

        try (PreparedStatement pst = DBConnector.getInstance().prepareStatement(query)) {
            pst.setInt(1, hotelId);
            pst.setDate(2, new java.sql.Date(dates[0].getTime()));
            pst.setDate(3, new java.sql.Date(dates[1].getTime()));

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("period_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Belirtilen tarihe uygun bir dönem bulunamazsa -1 veya başka bir belirleyici değeri döndürebilirsiniz.
        return -1;

    }

    public static String getPeriodNameById(int periodId) {
        String query = "SELECT period_name FROM periods WHERE period_id = ?";

        try (PreparedStatement pst = DBConnector.getInstance().prepareStatement(query)) {
            pst.setInt(1, periodId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("period_name");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Belirtilen tarihe uygun bir dönem bulunamazsa -1 veya başka bir belirleyici değeri döndürebilirsiniz.
        return "";
    }

    public static int getPeriodIdByDate(String dateString, int hotelId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(dateString);

            String query = "SELECT period_id FROM periods WHERE hotel_id = ? AND ? BETWEEN start_date AND end_date";

            try (PreparedStatement pst = DBConnector.getInstance().prepareStatement(query)) {
                pst.setInt(1, hotelId);
                pst.setDate(2, new java.sql.Date(date.getTime()));

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("period_id");
                    }
                }
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }

        // Belirtilen tarihe uygun bir dönem bulunamazsa -1 veya başka bir belirleyici değeri döndürebilirsiniz.
        return -1;
    }

    public static ArrayList<Period> getList() {
        ArrayList<Period> periodList = new ArrayList<>();
        String query = "SELECT * FROM periods";
        Period period;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                period = new Period();
                period.setPeriodId(rs.getInt("period_id"));
                period.setPeriodName(rs.getString("period_name"));
                period.setHotelId(rs.getInt("hotel_id"));
                period.setStartDate(rs.getDate("start_date"));
                period.setEndDate(rs.getDate("end_date"));
                periodList.add(period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return periodList;
    }

    public static ArrayList<Period> getPeriodsForHotel(String id) {
        ArrayList<Period> periodList = new ArrayList<>();
        String query = "SELECT * FROM periods WHERE hotel_id = ?";
        Period period;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, id);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                period = new Period();
                period.setPeriodId(rs.getInt("period_id"));
                period.setPeriodName(rs.getString("period_name"));
                period.setStartDate(rs.getDate("start_date"));
                period.setEndDate(rs.getDate("end_date"));
                periodList.add(period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return periodList;
    }

    public static List<String> getDonemlerFromDatabase(int otel, int oda) {
        List<String> donemler = new ArrayList<>();
        String query = "SELECT start_date, end_date FROM periods WHERE hotel_id = ? AND period_id IN (SELECT DISTINCT period_id FROM pricelist WHERE room_id = ?)";

        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, otel);
            pst.setInt(2, oda);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String start_date = rs.getString("start_date");
                String end_date = rs.getString("end_date");
                String donem = start_date + " - " + end_date;
                donemler.add(donem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return donemler;
    }

    public static boolean addPeriodsForHotel(int hotelId, Date startDate, Date endDate, String periodName) {
        String query = "INSERT INTO periods (hotel_id, start_date, end_date, period_name) VALUES (?, ?, ?, ?)";
        List<Period> periods = getAllPeriodsForHotel(hotelId);

        for (Period existingPeriod : periods) {
            if (!(existingPeriod.getEndDate().before(startDate) || existingPeriod.getStartDate().after(endDate))) {
                Helper.showMsg("Bu dönem zaten mevcut.");
                return false;
            }
        }

        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotelId);
            pst.setDate(2, new java.sql.Date(startDate.getTime()));
            pst.setDate(3, new java.sql.Date(endDate.getTime()));
            pst.setString(4, periodName);
            int response = pst.executeUpdate();

            if (response == -1) {
                Helper.showMsg("Ekleme işlemi başarısız oldu.");
            }
            return response != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Period> getAllPeriodsForHotel(int hotelId) {
        List<Period> periods = new ArrayList<>();
        String query = "SELECT * FROM periods WHERE hotel_id = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotelId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Period period = new Period();
                period.setPeriodId(rs.getInt("period_id"));
                period.setHotelId(rs.getInt("hotel_id"));
                period.setStartDate(rs.getDate("start_date"));
                period.setEndDate(rs.getDate("end_date"));
                period.setPeriodName(rs.getString("period_name"));
                periods.add(period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return periods;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM periods WHERE period_id = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, id);
            int response = pst.executeUpdate();
            return response != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deletePeriodsForHotel(int id) {
        String query = "DELETE FROM periods WHERE hotel_id = ?";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, id);
            int response = pst.executeUpdate();
            return response != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
