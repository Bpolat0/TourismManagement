package com.tourismmanagement.View;

import com.tourismmanagement.Helper.DBConnector;
import com.tourismmanagement.Helper.Helper;
import com.tourismmanagement.Helper.Item;
import com.tourismmanagement.Model.Hotel;
import com.tourismmanagement.Model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CardRoomPanel extends JPanel {
    private JPanel wrapper;
    private JPanel pnl_room;
    private JPanel pnl_room_table;
    private JTable tbl_room_list;
    private JPanel pnl_add_hotel;
    private JTextField fld_room_stock;
    private JTextField fld_room_bed;
    private JCheckBox cbx_television;
    private JCheckBox cbx_minibar;
    private JCheckBox cbx_game_console;
    private JCheckBox cbx_safe;
    private JCheckBox cbx_projection;
    private JPanel pnl_hotel_control_button;
    private JButton btn_rooml_add;
    private JButton btn_hotel_update;
    private JButton btn_hotel_delete;
    private JButton btn_hotel_panel_clear;
    private JComboBox chbx_hotel_list;
    private JComboBox chbx_room_type;
    private JTextField fld_room_number;
    private JTextField fld_room_msq;
    private JTextField fld_room_type;
    private DefaultTableModel mdl_room_list;
    private Object[] row_room_list;

    public CardRoomPanel() {
        setLayout(new BorderLayout());
        Helper.setLayout();
        add(wrapper);
        setSize(600, 400);

        loadHotelCombo();

        //ModelRoomList
        mdl_room_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_room_list = {"İD", "Oda Sayısı", "Oda Tipi", "Stok Sayısı", "Yatak Sayısı", "Metre Kare", "Televizyon", "Minibar", "Oyun Konsolu", "Kasa", "Projeksiyon"};
        mdl_room_list.setColumnIdentifiers(col_room_list);

        row_room_list = new Object[col_room_list.length];
        loadRoomModel();
        tbl_room_list.setModel(mdl_room_list);
        tbl_room_list.getTableHeader().setReorderingAllowed(false);


        btn_rooml_add.addActionListener(e -> {
            int hotel_id = ((Item) chbx_hotel_list.getSelectedItem()).getKey();
            String room_number = fld_room_number.getText();
            String room_type = fld_room_type.getText();
            String stock_quantity = fld_room_stock.getText();
            String bed_quantity = fld_room_bed.getText();
            String meter_square = fld_room_msq.getText();
            boolean television = cbx_television.isSelected();
            boolean minibar = cbx_minibar.isSelected();
            boolean game_console = cbx_game_console.isSelected();
            boolean safe = cbx_safe.isSelected();
            boolean projection = cbx_projection.isSelected();

            if (Helper.isFieldEmpty(fld_room_number) || Helper.isFieldEmpty(fld_room_stock) || Helper.isFieldEmpty(fld_room_bed) || Helper.isFieldEmpty(fld_room_msq)) {
                Helper.showMsg("fill");
            } else {
                addRoom(hotel_id, Integer.parseInt(room_number), room_type, Integer.parseInt(stock_quantity), Integer.parseInt(bed_quantity), Integer.parseInt(meter_square), television, minibar, game_console, safe, projection);
                loadRoomModel();
                clearForm();
                Helper.showMsg("done");
            }

        });

        ListSelectionModel selectionModel = tbl_room_list.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            fillFormWithSelectedRoom();
        });
    }

    public void fillFormWithSelectedRoom() {
        int selectedRow = tbl_room_list.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tbl_room_list.getValueAt(selectedRow, 0);
            int hotel_id = Integer.parseInt(selectedRoomHotelId(id));
            chbx_hotel_list.removeAllItems();
            cbx_television.setSelected(false);
            cbx_minibar.setSelected(false);
            cbx_game_console.setSelected(false);
            cbx_safe.setSelected(false);
            cbx_projection.setSelected(false);
            for (Room obj : Room.getRoomList()) {
                if (obj.getId() == id){
                    obj.setId(id);
                    boolean a = obj.isTelevision();
                    boolean b = obj.isMinibar();
                    boolean c = obj.isGame_console();
                    boolean d = obj.isSafe();
                    boolean e = obj.isProjection();
                    cbx_television.setSelected(a);
                    cbx_minibar.setSelected(b);
                    cbx_game_console.setSelected(c);
                    cbx_safe.setSelected(d);
                    cbx_projection.setSelected(e);
                }
            }
            int room_number = (int) tbl_room_list.getValueAt(selectedRow, 1);
            String room_type = (String) tbl_room_list.getValueAt(selectedRow, 2);
            int stock_quantity = (int) tbl_room_list.getValueAt(selectedRow, 3);
            int bed_quantity = (int) tbl_room_list.getValueAt(selectedRow, 4);
            int meter_square = (int) tbl_room_list.getValueAt(selectedRow, 5);
            loadRoomHotelCombo(hotel_id);
            chbx_hotel_list.addItem(new Item(hotel_id, loadRoomHotelCombo(hotel_id)));
            fld_room_number.setText(String.valueOf(room_number));
            fld_room_type.setText(room_type);
            fld_room_stock.setText(String.valueOf(stock_quantity));
            fld_room_bed.setText(String.valueOf(bed_quantity));
            fld_room_msq.setText(String.valueOf(meter_square));

        }
    }


    public static void addRoom(int hotel_id, int room_number, String room_type, int stock_quantity, int bed_quantity, int meter_square, boolean television, boolean minibar, boolean game_console, boolean safe, boolean projection) {
        String query = "INSERT INTO rooms (hotel_id, room_number, room_type, stock_quantity, bed_quantity, meter_square, television, minibar, game_console, safe, projection) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotel_id);
            pst.setInt(2, room_number);
            pst.setString(3, room_type);
            pst.setInt(4, stock_quantity);
            pst.setInt(5, bed_quantity);
            pst.setInt(6, meter_square);
            pst.setBoolean(7, television);
            pst.setBoolean(8, minibar);
            pst.setBoolean(9, game_console);
            pst.setBoolean(10, safe);
            pst.setBoolean(11, projection);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadRoomModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_room_list.getModel();
        clearModel.setRowCount(0);

        for (Room obj : Room.getRoomList()) {
            int i = 0;
            row_room_list[i++] = obj.getId();
            row_room_list[i++] = obj.getRoom_number();
            row_room_list[i++] = obj.getRoom_type();
            row_room_list[i++] = obj.getStock_quantity();
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

    public void loadHotelCombo() {
        chbx_hotel_list.removeAllItems();
        for (Hotel obj : Hotel.getList()) {
            chbx_hotel_list.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public String loadRoomHotelCombo(int hotel_id) {
        String hotel_name = "";
        for (Hotel obj : Hotel.getList()) {
            if (obj.getId() == hotel_id) {
                hotel_name = obj.getName();
            }
        }
        return hotel_name;
    }

    public String selectedRoomHotelId(int selectedRow) {
        int hotel_id = 0;
        for (Room obj : Room.getRoomList()) {
            if (obj.getId() == selectedRow) {
                hotel_id = obj.getHotel_id();
            }
        }
        return String.valueOf(hotel_id);
    }

    public void clearForm() {
        fld_room_number.setText("");
        fld_room_type.setText("");
        fld_room_stock.setText("");
        fld_room_bed.setText("");
        fld_room_msq.setText("");
        cbx_television.setSelected(false);
        cbx_minibar.setSelected(false);
        cbx_game_console.setSelected(false);
        cbx_safe.setSelected(false);
        cbx_projection.setSelected(false);
    }
}
