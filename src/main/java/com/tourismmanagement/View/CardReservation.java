package com.tourismmanagement.View;

import com.toedter.calendar.JDateChooser;
import com.tourismmanagement.Helper.DBConnector;
import com.tourismmanagement.Helper.Helper;
import com.tourismmanagement.Helper.Item;
import com.tourismmanagement.Model.Reservation;
import com.tourismmanagement.Model.Room;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class CardReservation extends JPanel {
    private JPanel wrapper;
    private JTextField textField1;
    private JPanel pnl_date_start;
    private JPanel pnl_date_end;
    private JPanel pnl_customer_info;
    private JLabel lbl_customer_name;
    private JPanel pnl_room_list;
    private JTable tbl_room_list;
    private JScrollPane scrl_room_list;
    private JPanel pnl_search;
    private JPanel pnl_search_wrapper;
    private JSpinner spn_adult;
    private JSpinner spn_child;
    private JPanel pnl_customer_number;
    private JButton btn_search;
    private JTextField fld_customer_name;
    private JLabel TC;
    private JTextField fld_customer_id;
    private JTextField fld_mail;
    private JTextField fld_number_of_night;
    private JTextField fld_total;
    private JButton btn_reserve;
    private JComboBox cbx_board_type;
    private JComboBox cbx_room;
    private JButton btn_show_all_room;
    private DefaultTableModel mdl_room_list;
    private Object[] row_room_list;
    private JDateChooser JDateChooser1;
    private JDateChooser JDateChooser2;
    private ArrayList<Room> roomList = new ArrayList<>();

    public CardReservation() {
        setLayout(new BorderLayout());
        add(wrapper);
        setSize(600, 400);

        pnl_date_start.setLayout(new MigLayout());
        pnl_date_end.setLayout(new MigLayout());


        JDateChooser1 = new JDateChooser();
        JDateChooser2 = new JDateChooser();

        pnl_date_start.add(JDateChooser1);
        pnl_date_end.add(JDateChooser2);

        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        SpinnerModel spinnerModel1 = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        spn_adult.setModel(spinnerModel);
        spn_child.setModel(spinnerModel1);

        //ModelRoomList
        mdl_room_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_room_list = {"Otel İsmi", "Oda Tipi", "Yatak Sayısı", "Metre Kare", "Televizyon", "Minibar", "Oyun Konsolu", "Kasa", "Projeksiyon"};

        mdl_room_list.setColumnIdentifiers(col_room_list);
        row_room_list = new Object[col_room_list.length];

        tbl_room_list.setModel(mdl_room_list);
        tbl_room_list.getTableHeader().setReorderingAllowed(false);
        tbl_room_list.getTableHeader().setResizingAllowed(false);
        //#ModelRoomList

        cbx_board_type.addActionListener(e -> {
            if (cbx_board_type.getSelectedItem() != null) {
                int roomID = ((Item) cbx_room.getSelectedItem()).getKey();
                int boardTypeID = ((Item) cbx_board_type.getSelectedItem()).getKey();
                int periodID = findPeriodID(JDateChooser1.getDate(), JDateChooser2.getDate(), findhotelIdWithRoomId(roomID));
                int adultCount = (int) spn_adult.getValue();
                int childCount = (int) spn_child.getValue();
                int nightCount = calculateNightCount(JDateChooser1.getDate(), JDateChooser2.getDate());
                BigDecimal totalFiyat = calculateToplamFiyat(roomID, boardTypeID, periodID, adultCount, childCount, nightCount);
                fld_total.setText(String.valueOf(totalFiyat));
            }
        });

        cbx_room.addActionListener(e -> {
            cbx_board_type.removeAllItems();
            if (cbx_room.getSelectedItem() != null) {
                loadPensionTypesComboWithIDsByRoomID(((Item) cbx_room.getSelectedItem()).getKey());
            }
        });

        btn_search.addActionListener(e -> {
            cbx_board_type.removeAllItems();
            DefaultTableModel clearModel = (DefaultTableModel) tbl_room_list.getModel();
            clearModel.setRowCount(0);
            performRoomSearch();
            fld_number_of_night.setText(String.valueOf(calculateNightCount(JDateChooser1.getDate(), JDateChooser2.getDate())));
            cbx_room.removeAllItems();
            loadSelectedRoomIdCombo();
            loadPensionTypesComboWithIDsByRoomID(((Item) cbx_room.getSelectedItem()).getKey());
        });

        btn_reserve.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_customer_name) || Helper.isFieldEmpty(fld_customer_id) || Helper.isFieldEmpty(fld_mail) || Helper.isFieldEmpty(fld_total) || JDateChooser1.getDate() == null || JDateChooser2.getDate() == null || cbx_room.getSelectedItem() == null || cbx_board_type.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Hata", JOptionPane.ERROR_MESSAGE);
            } else {
                String customerName = fld_customer_name.getText();
                String customerTc = fld_customer_id.getText();
                String customerEmail = fld_mail.getText();
                String hotelID = String.valueOf(findhotelIdWithRoomId(((Item) cbx_room.getSelectedItem()).getKey()));
                int roomID = ((Item) cbx_room.getSelectedItem()).getKey();
                int boardTypeID = ((Item) cbx_board_type.getSelectedItem()).getKey();
                int periodID = findPeriodID(JDateChooser1.getDate(), JDateChooser2.getDate(), findhotelIdWithRoomId(roomID));
                int adultCount = (int) spn_adult.getValue();
                int childCount = (int) spn_child.getValue();
                int nightCount = calculateNightCount(JDateChooser1.getDate(), JDateChooser2.getDate());
                java.util.Date utilDate = JDateChooser1.getDate();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                java.util.Date utilDate2 = JDateChooser2.getDate();
                java.sql.Date sqlDate2 = new java.sql.Date(utilDate2.getTime());
                BigDecimal totalFiyat = calculateToplamFiyat(roomID, boardTypeID, periodID, adultCount, childCount, nightCount);
                Reservation.add(Integer.parseInt(hotelID), roomID, sqlDate, sqlDate2, customerName, Integer.parseInt(customerTc), customerEmail, totalFiyat.doubleValue());
                JOptionPane.showMessageDialog(this, "Rezervasyon başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            }


        });

        btn_show_all_room.addActionListener(e -> {
            loadroommodel();
            Helper.showMsg("Sistem de kayıtlı olan tüm odalar listelendi. Rezervasyon yapmak için lütfen özel arama seçeneğini kullanınız. Bu buton sadece sistemde kayıtlı olan tüm odaları listelemek için kullanılır.");
        });
    }

    public int findPeriodID(Date startDate, Date endDate, int hotelID) {
        int periodID = -1; // Eğer bulunamazsa -1 dönecek
        java.util.Date utilStartDate = new java.util.Date(startDate.getTime());
        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());

        java.util.Date utilEndDate = new java.util.Date(endDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(utilEndDate.getTime());
        String sql = "SELECT period_id FROM periods WHERE hotel_id = ? AND start_date <= ? AND end_date >= ?";
        PreparedStatement statement = null;
        try {
            statement = DBConnector.getInstance().prepareStatement(sql);
            statement.setInt(1, hotelID);
            statement.setDate(2, sqlStartDate);
            statement.setDate(3, sqlEndDate);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                periodID = resultSet.getInt("period_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return periodID;
    }

    private int calculateNightCount(Date startDate, Date endDate) {
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();

        long dayCount = (endMillis - startMillis) / (1000 * 60 * 60 * 24);

        int nightCount = (int) dayCount;

        return nightCount;
    }

    private BigDecimal calculateToplamFiyat(int roomID, int boardTypeID, int periodID, int adultCount, int childCount, int nightCount) {
        int hotelID = findhotelIdWithRoomId(((Item) cbx_room.getSelectedItem()).getKey());// Otel ID'yi bul

        BigDecimal totalFiyat = BigDecimal.ZERO;
        String sql = "SELECT adult_price, child_price " +
                "FROM room_prices " +
                "WHERE room_id = ? AND period_id = ? AND board_types_id = ?";
        PreparedStatement statement = null;
        try {
            statement = DBConnector.getInstance().prepareStatement(sql);
            statement.setInt(1, roomID);
            statement.setInt(2, periodID); // Burada metot parametresinden gelen periodID kullanılıyor
            statement.setInt(3, boardTypeID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BigDecimal adultPrice = resultSet.getBigDecimal("adult_price");
                BigDecimal childPrice = resultSet.getBigDecimal("child_price");
                totalFiyat = adultPrice.multiply(BigDecimal.valueOf(adultCount))
                        .add(childPrice.multiply(BigDecimal.valueOf(childCount)))
                        .multiply(BigDecimal.valueOf(nightCount));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalFiyat;
    }

    public static Map<Integer, String> getPensionTypesWithIDsByRoomID(int roomID) {
        Map<Integer, String> pensionTypes = new LinkedHashMap<>();
        String query = "SELECT bt.id, bt.board_type_name " +
                "FROM hotel_boardtypes hb " +
                "INNER JOIN board_types bt ON hb.board_type_id = bt.id " +
                "WHERE hb.hotel_id = (SELECT hotel_id FROM rooms WHERE id = ?)";
        PreparedStatement statement = null;
        try {
            statement = DBConnector.getInstance().prepareStatement(query);
            statement.setInt(1, roomID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int typeId = resultSet.getInt("id");
                String typeName = resultSet.getString("board_type_name");
                pensionTypes.put(typeId, typeName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pensionTypes;
    }


    private void loadPensionTypesComboWithIDsByRoomID(int roomID) {
        cbx_board_type.removeAllItems(); // Combobox içeriğini temizle
        Map<Integer, String> pensionTypes = getPensionTypesWithIDsByRoomID(roomID); // Seçilen odaya ait pansiyon tiplerini al
        if (pensionTypes != null) {
            for (Map.Entry<Integer, String> entry : pensionTypes.entrySet()) {
                int typeId = entry.getKey();
                String typeName = entry.getValue();
                cbx_board_type.addItem(new Item(typeId, typeId + " " + typeName)); // ID ve ismi birlikte ekle
            }
        }
    }

    public static int findhotelID(String hotelName) {
        int hotelID = -1;
        String query = "SELECT id FROM hotels WHERE name = ?";
        PreparedStatement statement = null;
        try {
            statement = DBConnector.getInstance().prepareStatement(query);
            statement.setString(1, hotelName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                hotelID = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hotelID;
    }

    public static int findhotelIdWithRoomId(int roomID) {
        int hotelID = -1;
        String query = "SELECT hotel_id FROM rooms WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = DBConnector.getInstance().prepareStatement(query);
            statement.setInt(1, roomID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                hotelID = resultSet.getInt("hotel_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hotelID;
    }


    public static int findRoomID(String hotelName, String roomType) {
        int roomID = -1;
        // Otel adına karşılık gelen hotel_id'yi bulma
        String hotelQuery = "SELECT id FROM hotels WHERE name = ?";
        PreparedStatement hotelStatement = null;
        int hotelID = -1;
        try {
            hotelStatement = DBConnector.getInstance().prepareStatement(hotelQuery);
            hotelStatement.setString(1, hotelName);
            ResultSet hotelResultSet = hotelStatement.executeQuery();
            if (hotelResultSet.next()) {
                hotelID = hotelResultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Otel ID'sini kullanarak oda ID'sini bulma
        String roomQuery = "SELECT id FROM rooms WHERE hotel_id = ? AND room_type = ?";
        PreparedStatement roomStatement = null;
        try {
            roomStatement = DBConnector.getInstance().prepareStatement(roomQuery);
            roomStatement.setInt(1, hotelID);
            roomStatement.setString(2, roomType);
            ResultSet roomResultSet = roomStatement.executeQuery();
            if (roomResultSet.next()) {
                roomID = roomResultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return roomID;
    }

    private void loadroommodel() {

        DefaultTableModel clearModel = (DefaultTableModel) tbl_room_list.getModel();
        clearModel.setRowCount(0);

        for (Room obj : Room.getSpecialRoomList()) {
            int i = 0;
            row_room_list[i++] = obj.getHotel_name();
            row_room_list[i++] = obj.getRoom_type();
            row_room_list[i++] = obj.getBed_quantity();
            row_room_list[i++] = obj.getMeter_square();
            row_room_list[i++] = obj.isTelevision() ? "Var" : "Yok";
            row_room_list[i++] = obj.isMinibar() ? "Var" : "Yok";
            row_room_list[i++] = obj.isGame_console() ? "Var" : "Yok";
            row_room_list[i++] = obj.isSafe() ? "Var" : "Yok";
            row_room_list[i++] = obj.isProjection() ? "Var" : "Yok";
            mdl_room_list.addRow(row_room_list);
        }
    }

    private void performRoomSearch() {


        String city = textField1.getText();
        Date startDate = JDateChooser1.getDate();
        Date endDate = JDateChooser2.getDate();
        int adultCount = (int) spn_adult.getValue();
        int childCount = (int) spn_child.getValue();

        if (city.isEmpty() || startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        roomList.clear();
        loadRoomSearchModel(city, startDate, endDate, adultCount, childCount);
    }



    private void loadRoomSearchModel(String city, Date startDate, Date endDate, int adultCount, int childCount) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_room_list.getModel();
        clearModel.setRowCount(0);

        ArrayList<Room> searchedRooms = Room.roomSearchList(city, startDate, endDate);
        int totalPersonCount = adultCount + childCount;

        for (Room obj : searchedRooms) {
            if (totalPersonCount == 1 && obj.getBed_quantity() != 1) {
                continue;
            } else if (totalPersonCount == 2 && obj.getBed_quantity() != 2) {
                continue;
            } else if (totalPersonCount > 2 && obj.getBed_quantity() < 3) {
                continue;
            }

            Object[] row_room_list = new Object[9];
            row_room_list[0] = obj.getHotel_name();
            row_room_list[1] = obj.getRoom_type();
            row_room_list[2] = obj.getBed_quantity();
            row_room_list[3] = obj.getMeter_square();
            row_room_list[4] = obj.isTelevision() ? "Var" : "Yok";
            row_room_list[5] = obj.isMinibar() ? "Var" : "Yok";
            row_room_list[6] = obj.isGame_console() ? "Var" : "Yok";
            row_room_list[7] = obj.isSafe() ? "Var" : "Yok";
            row_room_list[8] = obj.isProjection() ? "Var" : "Yok";

            DefaultTableModel model = (DefaultTableModel) tbl_room_list.getModel();
            model.addRow(row_room_list);

            if (!roomList.contains(obj)) {
                roomList.add(obj);
            }
        }
    }


    private void loadSelectedRoomIdCombo() {
        cbx_room.removeAllItems(); // Combobox içeriğini temizle
        for (Room room : roomList) {
            int roomId = findRoomID(room.getHotel_name(), room.getRoom_type()); // Oda ID'sini bul
            String roomInfo = roomId + " " + room.getRoom_type(); // ID ve oda tipini birleştir
            cbx_room.addItem(new Item(roomId, roomInfo)); // Yeni Item oluşturup comboboxa ekle
        }
    }

}
