package com.tourismmanagement.View;

import com.toedter.calendar.JDateChooser;
import com.tourismmanagement.Helper.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class RoomSearchPanel extends JPanel {
    private JTextField adultCountTextField;
    private JTextField childCountTextField;
    private JTextField cityTextField;
    private JTextField startDateTextField;
    private JTextField endDateTextField;
    private JPanel searchPanel;
    private JPanel roomListPanel;
    private JTextField customerNameTextField;
    private JTextField customerEmailTextField;
    private JDateChooser startDateChooser; // Use JDateChooser for start date
    private JDateChooser endDateChooser; // Use JDateChooser for end date


    public JTextField getAdultCountTextField() {
        return adultCountTextField;
    }

    public void setAdultCountTextField(JTextField adultCountTextField) {
        this.adultCountTextField = adultCountTextField;
    }

    public JTextField getChildCountTextField() {
        return childCountTextField;
    }

    public void setChildCountTextField(JTextField childCountTextField) {
        this.childCountTextField = childCountTextField;
    }

    public RoomSearchPanel() {
        setLayout(new BorderLayout());

        // Top Panel for Search
        searchPanel = new JPanel(new GridBagLayout());

        // UI components for Search
        cityTextField = new JTextField(15);
        startDateChooser = new JDateChooser(); // Initialize JDateChooser
        endDateChooser = new JDateChooser(); // Initialize JDateChooser
        this.adultCountTextField = new JTextField(3);
        this.childCountTextField = new JTextField(3);

        JButton searchButton = new JButton("Search Rooms");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRoomSearch();
            }

        });

        customerNameTextField = new JTextField(15);
        customerEmailTextField = new JTextField(15);

        GridBagConstraints gbc = new GridBagConstraints();

        addToPanel(searchPanel, createLabelWithMargin("Müşteri İsmi: "), gbc, 0, 6);
        addToPanel(searchPanel, createTextFieldWithMargin(customerNameTextField), gbc, 1, 6);

        addToPanel(searchPanel, createLabelWithMargin("Müşteri Maili: "), gbc, 0, 7);
        addToPanel(searchPanel, createTextFieldWithMargin(customerEmailTextField), gbc, 1, 7);

        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to searchPanel with margins
        addToPanel(searchPanel, createLabelWithMargin("Şehir: "), gbc, 0, 0);
        addToPanel(searchPanel, createTextFieldWithMargin(cityTextField), gbc, 1, 0);

        addToPanel(searchPanel, createLabelWithMargin("Giriş Tarihi: "), gbc, 0, 1);
        addToPanel(searchPanel, startDateChooser, gbc, 1, 1); // Use startDateChooser

        addToPanel(searchPanel, createLabelWithMargin("Çıkış Tarihi: "), gbc, 0, 2);
        addToPanel(searchPanel, endDateChooser, gbc, 1, 2); // Use endDateChooser

        addToPanel(searchPanel, createLabelWithMargin("Yetişkin Sayısı: "), gbc, 0, 3);
        addToPanel(searchPanel, createTextFieldWithMargin(adultCountTextField), gbc, 1, 3);

        addToPanel(searchPanel, createLabelWithMargin("Çocuk Sayısı: "), gbc, 0, 4);
        addToPanel(searchPanel, createTextFieldWithMargin(childCountTextField), gbc, 1, 4);

        addToPanel(searchPanel, searchButton, gbc, 0, 5, 2, 1, GridBagConstraints.CENTER);

        // Add searchPanel to the top of BorderLayout with left alignment
        add(searchPanel, BorderLayout.NORTH);

        // Bottom Panel for Room List
        roomListPanel = new JPanel();
        roomListPanel.setLayout(new GridLayout(0, 3, 10, 10)); // GridLayout with 3 columns

        // Add roomListPanel to the center of BorderLayout
        add(new JScrollPane(roomListPanel), BorderLayout.CENTER);
    }

    private void performRoomSearch() {
        roomListPanel.removeAll(); // Clear previous room list

        String city = cityTextField.getText();
        // Use SimpleDateFormat to format the selected dates
        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateChooser.getDate());
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(endDateChooser.getDate());
        int adultCount = Integer.parseInt(adultCountTextField.getText());
        int childCount = Integer.parseInt(childCountTextField.getText());

        String customerName = customerNameTextField.getText();
        String customerEmail = customerEmailTextField.getText();

        if (city.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || customerName.isEmpty() || customerEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RoomSearch.searchRooms(city, startDate, endDate, roomListPanel, adultCount, childCount, customerName, customerEmail);
    }

    private JLabel createLabelWithMargin(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    private JTextField createTextFieldWithMargin(JTextField textField) {
        textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return textField;
    }

    private void addToPanel(JPanel panel, Component component, GridBagConstraints gbc, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(component, gbc);
    }

    private void addToPanel(JPanel panel, Component component, GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        panel.add(component, gbc);
    }
}

class RoomSearch {
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
                roomInfo.put("Hotel", resultSet.getString("name"));
                roomInfo.put("Room ID", roomId);
                roomInfo.put("Room Number", resultSet.getString("room_number"));
                roomInfo.put("Room Type", resultSet.getString("room_type"));
                roomInfo.put("Board Type", resultSet.getString("board_type_name"));

                roomMap.computeIfAbsent(roomId, k -> new ArrayList<>()).add(roomInfo);
            }

            for (List<Map<String, String>> roomList : roomMap.values()) {
                JPanel roomPanel = new JPanel();
                roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));

                for (Map<String, String> roomInfo : roomList) {
                    roomPanel.add(createLabelWithMargin(roomInfo.get("Hotel")));
                    roomPanel.add(createLabelWithMargin("Room ID: " + roomInfo.get("Room ID")));
                    roomPanel.add(createLabelWithMargin("Room Number: " + roomInfo.get("Room Number")));
                    roomPanel.add(createLabelWithMargin("Room Type: " + roomInfo.get("Room Type")));
                    roomPanel.add(createLabelWithMargin("Board Type: " + roomInfo.get("Board Type")));

                    JButton reserveButton = new JButton("Reserve Room");
                    reserveButton.addActionListener(e -> {
                        reserveRoom(roomInfo.get("Room Number"), startDate, endDate, roomListPanel, adultCount, childCount, customerName, customerEmail);
                        JOptionPane.showMessageDialog(roomListPanel, "Room Reserved!\nRoom ID: " + roomInfo.get("Room Number") + "\nStart Date: " + startDate + "\nEnd Date: " + endDate);
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


