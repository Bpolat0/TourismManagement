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


