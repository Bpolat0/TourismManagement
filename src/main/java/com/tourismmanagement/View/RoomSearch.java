package com.tourismmanagement.View;

import com.tourismmanagement.Helper.DBConnector;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomSearch {
    public static void searchRooms(String city, String startDate, String endDate, JPanel roomListPanel, int adultCount, int childCount, String customerName, String customerEmail) {
        Connection connection = DBConnector.getInstance();

        // Query to retrieve available rooms based on the city and date range
        String query = "SELECT hotels.name, rooms.id AS room_id, rooms.room_number, rooms.room_type, board_types.board_type_name " +
                "FROM hotels " +
                "JOIN rooms ON rooms.hotel_id = hotels.id " +
                "LEFT JOIN reservations ON reservations.room_id = rooms.id " +
                "AND NOT (reservations.end_date < ? OR reservations.start_date > ?) " +
                "LEFT JOIN hotel_boardtypes ON hotel_boardtypes.hotel_id = hotels.id " +
                "LEFT JOIN board_types ON hotel_boardtypes.board_type_id = board_types.id " +
                "WHERE hotels.city = ? " +
                "AND (reservations.id IS NULL OR reservations.id = '') " +
                "GROUP BY room_id, hotels.name, rooms.room_number, rooms.room_type, board_types.board_type_name";


        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            preparedStatement.setString(3, city);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Display the results in the roomListPanel
            Map<String, List<Map<String, String>>> roomMap = new HashMap<>();

            while (resultSet.next()) {
                String roomId = resultSet.getString("room_id"); // Use the unique identifier for the room

                Map<String, String> roomInfo = new HashMap<>();
                roomInfo.put("Otel İsmi", resultSet.getString("name"));
                roomInfo.put("Oda ID", resultSet.getString("room_id"));
                roomInfo.put("Oda Numarası", resultSet.getString("room_number"));
                roomInfo.put("Oda Tipi", resultSet.getString("room_type"));
                roomInfo.put("Pansiyon Tipi", resultSet.getString("board_type_name"));
                roomInfo.put("Oda Fiyatı", calculateTotalPrice(Integer.parseInt(roomId), 1, 1, adultCount, childCount, startDate, endDate).toString());

                roomMap.computeIfAbsent(roomId, k -> new ArrayList<>()).add(roomInfo);
            }

            for (List<Map<String, String>> roomList : roomMap.values()) {
                JPanel roomPanel = new JPanel();
                roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));

                for (Map<String, String> roomInfo : roomList) {
                    roomPanel.add(createLabelWithMargin(roomInfo.get("Otel İsmi")));
                    roomPanel.add(createLabelWithMargin("Oda ID: " + roomInfo.get("Oda ID")));
                    roomPanel.add(createLabelWithMargin("Oda Numarası: " + roomInfo.get("Oda Numarası")));
                    roomPanel.add(createLabelWithMargin("Oda Tipi: " + roomInfo.get("Oda Tipi")));
                    roomPanel.add(createLabelWithMargin("Pansiyon Tipi: " + roomInfo.get("Pansiyon Tipi")));
                    roomPanel.add(createLabelWithMargin("Oda Fiyatı: " + calculateTotalPrice(Integer.parseInt(roomInfo.get("Oda ID")), 1, 1, adultCount, childCount, startDate, endDate)));

                    JButton reserveButton = new JButton("Rezerve Et");
                    reserveButton.addActionListener(e -> {
                        reserveRoom(roomInfo.get("Oda Numarası"), startDate, endDate, roomListPanel, adultCount, childCount, customerName, customerEmail);
                        JOptionPane.showMessageDialog(roomListPanel, "Rezerve Edildi!\nOda ID: " + roomInfo.get("Oda Numarası") + "\nGiriş : " + startDate + "\nÇıkış : " + endDate);
                        roomPanel.removeAll();
                        roomPanel.revalidate();
                        roomPanel.repaint();
                    });

                    roomPanel.add(reserveButton);
                }

                roomListPanel.add(roomPanel);
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        roomListPanel.revalidate();
    }

    private static JLabel createLabelWithMargin(String text) {
        if (text.equals("board_type_name")) {
            text = "Board Type";
        }
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    private static void reserveRoom(String roomNumber, String startDate, String endDate, JPanel roomListPanel, int adultCount, int childCount, String customerName, String customerEmail) {
        Connection connection = DBConnector.getInstance();


        String getIdsQuery = "SELECT r.hotel_id, r.id AS room_id, p.board_type_id, p.period_id " +
                "FROM rooms r " +
                "JOIN pricelist p ON r.id = p.room_id " +
                "WHERE r.room_number = ?";

        try {
            PreparedStatement getIdsStatement = connection.prepareStatement(getIdsQuery);
            getIdsStatement.setString(1, roomNumber);
            ResultSet idsResultSet = getIdsStatement.executeQuery();

            if (idsResultSet.next()) {
                int hotelId = idsResultSet.getInt("hotel_id");
                int roomId = idsResultSet.getInt("room_id");
                int boardTypeId = idsResultSet.getInt("board_type_id");
                int periodId = idsResultSet.getInt("period_id");


                String reserveQuery = "INSERT INTO reservations (hotel_id, room_id, start_date, end_date, customer_name, customer_email, total_price) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                BigDecimal totalPrice = calculateTotalPrice(roomId, boardTypeId, periodId, adultCount, childCount, startDate, endDate);

                PreparedStatement reserveStatement = connection.prepareStatement(reserveQuery);
                reserveStatement.setInt(1, hotelId);
                reserveStatement.setInt(2, roomId);
                reserveStatement.setString(3, startDate);
                reserveStatement.setString(4, endDate);
                reserveStatement.setString(5, customerName);
                reserveStatement.setString(6, customerEmail);
                reserveStatement.setBigDecimal(7, totalPrice);

                int rowsAffected = reserveStatement.executeUpdate();

                reserveStatement.close();
            } else {
                JOptionPane.showMessageDialog(roomListPanel, "İlgili oda numarası için database de bilgi bulunamadı: " + roomNumber);
            }

            getIdsStatement.close();
            idsResultSet.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static BigDecimal calculateTotalPrice(int roomId, int boardTypeId, int periodId, int adultCount, int childCount, String startDate, String endDate) {
        Connection connection = DBConnector.getInstance();

        // Calculate the number of nights between start and end dates
        long numberOfNights = calculateNumberOfNights(startDate, endDate);

        // Query to get the price details from pricelist
        String priceQuery = "SELECT adult_price, child_price " +
                "FROM pricelist " +
                "WHERE room_id = ? " +
                "AND board_type_id = ? " +
                "AND period_id = ?";

        try {
            PreparedStatement priceStatement = connection.prepareStatement(priceQuery);
            priceStatement.setInt(1, roomId);
            priceStatement.setInt(2, boardTypeId);
            priceStatement.setInt(3, periodId);

            ResultSet priceResultSet = priceStatement.executeQuery();

            if (priceResultSet.next()) {
                BigDecimal adultPrice = priceResultSet.getBigDecimal("adult_price");
                BigDecimal childPrice = priceResultSet.getBigDecimal("child_price");

                // Calculate total price based on adult and child counts and the number of nights
                BigDecimal totalAdultPrice = adultPrice.multiply(BigDecimal.valueOf(adultCount)).multiply(BigDecimal.valueOf(numberOfNights));
                BigDecimal totalChildPrice = childPrice.multiply(BigDecimal.valueOf(childCount)).multiply(BigDecimal.valueOf(numberOfNights));

                return totalAdultPrice.add(totalChildPrice);
            } else {
                System.out.println("Fiyat bulunamadı");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return BigDecimal.ZERO; // Default to zero if no price is found
    }

    private static long calculateNumberOfNights(String startDate, String endDate) {
        // Convert startDate and endDate to java.util.Date objects
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date start = sdf.parse(startDate);
            java.util.Date end = sdf.parse(endDate);

            // Calculate the difference in milliseconds
            long differenceInMillis = end.getTime() - start.getTime();

            // Convert the difference to days
            return differenceInMillis / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0; // Return 0 in case of an error
    }


}
