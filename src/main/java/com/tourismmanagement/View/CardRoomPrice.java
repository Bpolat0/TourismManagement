package com.tourismmanagement.View;

import com.tourismmanagement.Helper.Helper;
import com.tourismmanagement.Helper.Item;
import com.tourismmanagement.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.tourismmanagement.Model.BoardType.getBoardTypeIdByName;
import static com.tourismmanagement.Model.Period.getPeriodNameById;

public class CardRoomPrice extends JPanel {
    private JPanel wrapper; // wrapper değişkenini tanımladık
    private JPanel pnl_room;
    private JPanel pnl_room_table;
    private JTable tbl_room_price;
    private JPanel pnl_room_price;
    private JPanel pnl_hotel_control_button;
    private JButton btn_add;
    private JButton btn_room_update;
    private JButton btn_room_delete;
    private JButton btn_room_panel_clear;
    private JComboBox chbx_hotel_list; // chbx_hotel_list değişkenini tanımladık
    private JComboBox chbx_room_list; // chbx_room_list değişkenini tanımladık
    private JComboBox chbx_board_type_list;
    private JComboBox chbx_period_list;
    private JTextField fld_adult_price;
    private JTextField fld_child_price;
    private JTextField fld_period_name;
    private JLabel lbl_period_name;
    private JPanel dinamikPanel;
    private int hotel_id;
    private String board_type_name;
    private DefaultTableModel mdl_room_price;
    private Object[] row_price_list;

    public CardRoomPrice() {
        setLayout(new BorderLayout());
        add(wrapper);
        setSize(600, 400);

        loadHotelComboBox();
        loadRoomComboBox();
        loadBoardTypeComboBox();
        loadPeriodComboBox();

        //ModelRoomPrice
        mdl_room_price = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_room_price = {"Otel Adı", "Oda Tipi", "Pansiyon Tipi", "Sezon", "Yetişkin Fiyatı", "Çocuk Fiyatı"};
        mdl_room_price.setColumnIdentifiers(col_room_price);
        row_price_list = new Object[col_room_price.length];
        loadPriceTable();
        tbl_room_price.setModel(mdl_room_price);
        tbl_room_price.getTableHeader().setReorderingAllowed(false);
        tbl_room_price.getTableHeader().setResizingAllowed(false);

        //#ModelRoomPrice

        ListSelectionModel selectionModel = tbl_room_price.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            fillFormWithSelectedRow();
        });

        btn_add.addActionListener(e -> {
            int hotel_id = ((Item) chbx_hotel_list.getSelectedItem()).getKey();
            int room_id = ((Item) chbx_room_list.getSelectedItem()).getKey();
            chbx_period_list.getSelectedItem();
            String board_type_name = ((Item) chbx_board_type_list.getSelectedItem()).getValue();
            String dateRangeString = chbx_period_list.getSelectedItem().toString();
            int period_id = Period.getPeriodIdByDateRange(hotel_id, parseDateRange(dateRangeString));
            String adult_price = (fld_adult_price.getText());
            String child_price = (fld_child_price.getText());


            if (Helper.isFieldEmpty(fld_adult_price) || Helper.isFieldEmpty(fld_child_price)) {
                Helper.showMsg("fill");
            } else {
                RoomPrice.add(room_id, period_id, getBoardTypeIdByName(board_type_name), Double.parseDouble(adult_price), Double.parseDouble(child_price));
                loadPriceTable();
                clearForm();
            }
        });

        chbx_hotel_list.addActionListener(e -> {
            updateRoomComboBox();
            updateBoardTypeComboBox();
            updatePeriodComboBox();
        });

        chbx_period_list.addActionListener(e -> {
            if (chbx_period_list.getSelectedItem() != null) {
                String dateRangeString = chbx_period_list.getSelectedItem().toString();
                int period_id = Period.getPeriodIdByDateRange(getHotelId(), parseDateRange(dateRangeString));
                setFld_period_name_by_id(period_id);
            }else {
                fld_period_name.setText("Seçilen Otele Ait Dönem Bulunamadı");
            }
        });

        btn_room_update.addActionListener(e -> {
            int hotel_id = ((Item) chbx_hotel_list.getSelectedItem()).getKey();
            int room_id = ((Item) chbx_room_list.getSelectedItem()).getKey();
            chbx_period_list.getSelectedItem();
            String board_type_name = ((Item) chbx_board_type_list.getSelectedItem()).getValue();
            String dateRangeString = chbx_period_list.getSelectedItem().toString();
            int period_id = Period.getPeriodIdByDateRange(hotel_id, parseDateRange(dateRangeString));
            String adult_price = (fld_adult_price.getText());
            String child_price = (fld_child_price.getText());


            if (Helper.isFieldEmpty(fld_adult_price) || Helper.isFieldEmpty(fld_child_price)) {
                Helper.showMsg("fill");
            } else {
                RoomPrice.update(room_id, period_id, getBoardTypeIdByName(board_type_name), Double.parseDouble(adult_price), Double.parseDouble(child_price));
                loadPriceTable();
                clearForm();
            }
        });
        btn_room_delete.addActionListener(e -> {
            int selectedRow = tbl_room_price.getSelectedRow();
            if (selectedRow > -1) {
                int room_id = ((Item) chbx_room_list.getSelectedItem()).getKey();
                String dateRangeString = chbx_period_list.getSelectedItem().toString();
                int period_id = Period.getPeriodIdByDateRange(getHotelId(), parseDateRange(dateRangeString));
                int board_type_id = getBoardTypeIdByName(((Item) chbx_board_type_list.getSelectedItem()).getValue());
                RoomPrice.deletePrices(room_id, period_id, board_type_id);
                loadPriceTable();
                clearForm();
                Helper.showMsg("done");
            } else {
                Helper.showMsg("Lütfen silmek istediğiniz bilgileri giriniz");
            }
        });

        btn_room_panel_clear.addActionListener(e -> clearForm());
    }

    private void clearForm() {
        chbx_hotel_list.setSelectedIndex(0);
        chbx_room_list.setSelectedIndex(0);
        chbx_board_type_list.setSelectedIndex(0);
        chbx_period_list.setSelectedIndex(0);
        fld_adult_price.setText("");
        fld_child_price.setText("");
    }

    private void fillFormWithSelectedRow() {
        int selectedRow = tbl_room_price.getSelectedRow();
        if (selectedRow > -1) {
            String hotel_name = tbl_room_price.getValueAt(selectedRow, 0).toString();
            String room_number = tbl_room_price.getValueAt(selectedRow, 1).toString();
            String board_type_name = tbl_room_price.getValueAt(selectedRow, 2).toString();
            String season = tbl_room_price.getValueAt(selectedRow, 3).toString();
            double adult_price = Double.parseDouble(tbl_room_price.getValueAt(selectedRow, 4).toString());
            double child_price = Double.parseDouble(tbl_room_price.getValueAt(selectedRow, 5).toString());

            chbx_hotel_list.setSelectedItem(hotel_name);
            fld_adult_price.setText(String.valueOf(adult_price));
            fld_child_price.setText(String.valueOf(child_price));

        }
    }

    public int getHotelId() {
        return ((Item) chbx_hotel_list.getSelectedItem()).getKey();
    }

    public int getRoomId() {
        return ((Item) chbx_room_list.getSelectedItem()).getKey();
    }

    public void loadPriceTable() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_room_price.getModel();
        clearModel.setRowCount(0);

        for (RoomPrice obj : RoomPrice.getSpecialList()) {
            int i = 0;
            row_price_list[i++] = obj.getHotel_name();
            row_price_list[i++] = obj.getRoom_type();
            row_price_list[i++] = obj.getBoard_type_name();
            row_price_list[i++] = obj.getSeason();
            row_price_list[i++] = obj.getAdult_price();
            row_price_list[i++] = obj.getChild_price();
            mdl_room_price.addRow(row_price_list);
        }
    }

    public void loadHotelComboBox() {
        chbx_hotel_list.removeAllItems();
        for (Hotel obj : Hotel.getList()) {
            chbx_hotel_list.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void updateRoomComboBox() {
        chbx_room_list.removeAllItems();
        for (Room obj : Room.getRoomList()) {
            if (obj.getHotel_id() == ((Item) chbx_hotel_list.getSelectedItem()).getKey()) {
                String room_type = obj.getRoom_type();
                chbx_room_list.addItem(new Item(obj.getId(), room_type));
            }
        }
    }

    public void loadRoomComboBox() {
        chbx_room_list.removeAllItems();
        for (Room obj : Room.getRoomList()) {
            String room_type = obj.getRoom_type();
            chbx_room_list.addItem(new Item(obj.getId(), room_type));
        }
    }

    public void loadBoardTypeComboBox() {
        chbx_board_type_list.removeAllItems();
        for (BoardType obj : BoardType.getOnlyBoardTypeList()) {
            chbx_board_type_list.addItem(new Item(obj.getId(), obj.getBoard_type_name()));
        }
    }

    public void updateBoardTypeComboBox() {
        chbx_board_type_list.removeAllItems();
        for (BoardType obj : BoardType.getList()) {
            if (obj.getHotel_id() == ((Item) chbx_hotel_list.getSelectedItem()).getKey()) {
                chbx_board_type_list.addItem(new Item(obj.getId(), getBoardTypeNameByID(obj.getBoard_type_id())));
            }
        }
    }

    private void loadPeriodComboBox() {
        ArrayList<Period> periods = Period.getList();
        for (Period period : periods) {
            chbx_period_list.addItem(period.startEndDateToString());
        }
    }

    public void setFld_period_name_by_id(int period_id) {
        fld_period_name.setText(getPeriodNameById(period_id));
    }

    private void updatePeriodComboBox() {
        chbx_period_list.removeAllItems();
        ArrayList<Period> periods = Period.getList();
        for (Period period : periods) {
            if (period.getHotelId() == ((Item) chbx_hotel_list.getSelectedItem()).getKey()) {
                chbx_period_list.addItem(period.startEndDateToString());
            }
        }
    }

    public static Date[] parseDateRange(String dateRangeString) {
        try {
            String[] dateParts = dateRangeString.split(" - ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = dateFormat.parse(dateParts[0].replace( "Başlangıç: ", ""));
            Date endDate = dateFormat.parse(dateParts[1].replace("Bitiş: ", ""));

            return new Date[]{startDate, endDate};
        } catch (ParseException | ArrayIndexOutOfBoundsException e) {
            // ParseException veya ArrayIndexOutOfBoundsException durumunda uygun bir hata işleme stratejisi belirlenebilir
            e.printStackTrace();
            return null;
        }
    }

    public String getBoardTypeNameByID(int board_type_id) {
        switch (board_type_id) {
            case 1:
                board_type_name = "Ultra Herşey Dahil";
                break;
            case 2:
                board_type_name = "Herşey Dahil";
                break;
            case 3:
                board_type_name = "Oda Kahvaltı";
                break;
            case 4:
                board_type_name = "Tam Pansiyon";
                break;
            case 5:
                board_type_name = "Yarım Pansiyon";
                break;
            case 6:
                board_type_name = "Sadece Yatak";
                break;
            case 7:
                board_type_name = "Alkol Hariç Full credit";
                break;
        }
        return board_type_name;
    }

}
