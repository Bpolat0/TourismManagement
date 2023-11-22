package com.tourismmanagement.View;

import com.toedter.calendar.JDateChooser;
import com.tourismmanagement.Helper.Helper;
import com.tourismmanagement.Helper.Item;
import com.tourismmanagement.Model.BoardType;
import com.tourismmanagement.Model.Hotel;
import com.tourismmanagement.Model.Period;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import static com.tourismmanagement.Model.BoardType.addBoardTypeByHotelID;
import static com.tourismmanagement.Model.BoardType.deleteBoardTypes;

public class CardHotelPanel extends JPanel {
    private JPanel pnl_add_hotel;
    private JPanel pnl_hotel_table;
    private JPanel pnl_hotel;
    private JScrollPane scrl_add_hotel;
    private JTable tbl_hotel_list;
    private JTextField fld_hotel_name;
    private JTextField fld_hotel_country;
    private JTextField fld_hotel_region;
    private JTextField fld_hotel_address;
    private JTextField fld_hotel_email;
    private JTextField fld_hotel_phone;
    private JCheckBox cbx_free_parking;
    private JCheckBox cbx_free_wifi;
    private JCheckBox cbx_swimming_pool;
    private JCheckBox cbx_fitness_center;
    private JCheckBox cbx_hotel_concierge;
    private JCheckBox cbx_spa;
    private JCheckBox cbx_room_service;
    private JCheckBox cbx_ultra_all_inclusive;
    private JCheckBox cbx_alI_inclusive;
    private JCheckBox cbx_room_breakfast;
    private JCheckBox cbx_full_board;
    private JCheckBox cbx_half_board;
    private JCheckBox cbx_room_only;
    private JCheckBox cbx_alcohol_free;
    private JPanel pnl_hotel_star;
    private JRadioButton rbutton_7;
    private JRadioButton rbutton_6;
    private JRadioButton rbutton_5;
    private JRadioButton rbutton_4;
    private JRadioButton rbutton_3;
    private JRadioButton rbutton_2;
    private JRadioButton rbutton_1;
    private JPanel pnl_hotel_control_button;
    private ButtonGroup btn_group_star = new ButtonGroup();
    private DefaultTableModel mdl_hotel_list;
    private DefaultTableModel mdl_board_list;
    private DefaultTableModel mdl_period_list;
    private Object[] row_hotel_list;
    private Object[] row_board_list;
    private Object[] row_period_list;
    private JButton btn_hotel_add;
    private JButton btn_hotel_update;
    private JButton btn_hotel_delete;
    private JButton btn_hotel_panel_clear;
    private JPanel wrapper;
    private JPanel pnl_board_season;
    private JPanel pnl_seasons;
    private JTable tbl_board_type;
    private JTable tbl_period;
    private JScrollPane scrl_board_types;
    private JScrollPane scrl_seasons;
    private JPanel pnl_add_period;
    private JTextField fld_period_name;
    private JPanel pnl_date_start;
    private JPanel pnl_date_end;
    private JComboBox chbx_hotel_list;
    private JPanel pnl_period_control;
    private JButton btn_period_delete_with_combobox;
    private JButton btn_period_add;
    private JTextField fld_hotel_period;
    private JPanel pnl_boardAndPeriod_control;
    private JComboBox chbx_period_list;
    private JButton btn_add_hotel_board;
    private JButton btn_delete_hotel_board;
    private JDateChooser JDateChooser1;
    private JDateChooser JDateChooser2;

    public CardHotelPanel() {
        setLayout(new BorderLayout());

        pnl_add_hotel = new JPanel();
        // pnl_add_hotel'in içeriğini ayarlayın
        pnl_hotel_table = new JPanel();
        // pnl_hotel_table'ın içeriğini ayarlayın

        JScrollPane scrollPane = new JScrollPane(pnl_hotel_table);

        add(pnl_add_hotel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        add(wrapper);
        setSize(600, 400);
        pnl_date_start.setLayout(new MigLayout());
        pnl_date_end.setLayout(new MigLayout());
        JDateChooser1 = new JDateChooser();
        JDateChooser2 = new JDateChooser();
        pnl_date_start.add(JDateChooser1);
        pnl_date_end.add(JDateChooser2);
        pnl_add_hotel.setVisible(true);


        //ModelHotelList
        mdl_hotel_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tüm hücrelerin düzenlenmesini engeller
            }
        };
        Object[] col_hotel_list = {"ID", "Hotel Adı", "Şehir", "Bölge", "Adres", "E-mail", "Telefon", "Yıldız", "Otopark Durumu", "Wifi Hizmeti", "Yüzme Havuzu", "Fitness Center", "Hotel Concierge", "SPA", "Oda Servisi", "Ultra Herşey Dahil", "Herşey Dahil", "Oda Kahvaltı", "Tam Pansiyon", "Yarım Pansiyon", "Sadece Yatak", "Alkol Hariç Full credit"};
        mdl_hotel_list.setColumnIdentifiers(col_hotel_list);

        row_hotel_list = new Object[col_hotel_list.length];
        loadHotelModel();
        tbl_hotel_list.setModel(mdl_hotel_list);
        tbl_hotel_list.getTableHeader().setReorderingAllowed(false);
        tbl_hotel_list.getTableHeader().setResizingAllowed(false);

        //#ModelHotelList

        //ModelBoardType
        mdl_board_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tüm hücrelerin düzenlenmesini engeller
            }
        };
        Object[] col_board_list = {"Pansiyon Tipi"};
        mdl_board_list.setColumnIdentifiers(col_board_list);
        row_board_list = new Object[col_board_list.length];
        tbl_board_type.setModel(mdl_board_list);
        tbl_board_type.getTableHeader().setReorderingAllowed(false);
        //#ModelBoardType

        //ModelPeriod
        mdl_period_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tüm hücrelerin düzenlenmesini engeller
            }
        };
        Object[] col_period_list = {"Sezon", "Sezon İsmi", "Sezon Başlangıç", "Sezon Bitiş"};
        mdl_period_list.setColumnIdentifiers(col_period_list);
        row_period_list = new Object[col_period_list.length];
        tbl_period.setModel(mdl_period_list);
        tbl_period.getTableHeader().setReorderingAllowed(false);

        //#ModelPeriod
        btn_group_star.add(rbutton_1);
        btn_group_star.add(rbutton_2);
        btn_group_star.add(rbutton_3);
        btn_group_star.add(rbutton_4);
        btn_group_star.add(rbutton_5);
        btn_group_star.add(rbutton_6);
        btn_group_star.add(rbutton_7);

        ListSelectionModel selectionModel = tbl_hotel_list.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            setTbl_hotel_list();
            if (!e.getValueIsAdjusting()) {
                // Seçilen otelin id'sini alın
                if (tbl_hotel_list.getSelectedRow() != -1) {
                    int selectedRow = tbl_hotel_list.getSelectedRow();
                    int id = (int) tbl_hotel_list.getValueAt(selectedRow, 0);
                    String.valueOf(id);
                    setFld_hotel_period(String.valueOf(id));
                    // loadBoardModel() metodunu id parametresi ile çağırın
                    loadBoardModel(String.valueOf(id));
                    loadPeriodModel(String.valueOf(id));
                    loadPeriodComboBox(id);
                }

            }
        });

        btn_hotel_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_hotel_name) || Helper.isFieldEmpty(fld_hotel_country) || Helper.isFieldEmpty(fld_hotel_region)
                    || Helper.isFieldEmpty(fld_hotel_address) || Helper.isFieldEmpty(fld_hotel_email) || Helper.isFieldEmpty(fld_hotel_phone)) {
                Helper.showMsg("fill");
            } else {
                String name = fld_hotel_name.getText();
                String city = fld_hotel_country.getText();
                String region = fld_hotel_region.getText();
                String address = fld_hotel_address.getText();
                String email = fld_hotel_email.getText();
                String phone = fld_hotel_phone.getText();
                String star = getSelectedRadioButtonText(btn_group_star);
                boolean freePark = cbx_free_parking.isSelected();
                boolean freeWifi = cbx_free_wifi.isSelected();
                boolean swimmingPool = cbx_swimming_pool.isSelected();
                boolean fitnessCenter = cbx_fitness_center.isSelected();
                boolean hotelConcierge = cbx_hotel_concierge.isSelected();
                boolean spa = cbx_spa.isSelected();
                boolean roomService = cbx_room_service.isSelected();
                boolean ultraAllInclusive = cbx_ultra_all_inclusive.isSelected();
                boolean allInclusive = cbx_alI_inclusive.isSelected();
                boolean bedAndBreakfast = cbx_room_breakfast.isSelected();
                boolean fullBoard = cbx_full_board.isSelected();
                boolean halfBoard = cbx_half_board.isSelected();
                boolean roomOnly = cbx_room_only.isSelected();
                boolean nonAlcoholFull = cbx_alcohol_free.isSelected();


                if (Hotel.add(name, city, region, address, email, phone, star, freePark, freeWifi, swimmingPool, fitnessCenter, hotelConcierge, spa, roomService, ultraAllInclusive, allInclusive, bedAndBreakfast, fullBoard, halfBoard, roomOnly, nonAlcoholFull)) {
                    Helper.showMsg("done");
                    clearHotelAddPanel();
                    loadHotelModel();
                } else {
                    Helper.showMsg("error");
                }

            }
        });

        btn_hotel_update.addActionListener(e -> {
            if (tbl_hotel_list.getSelectedRow() != -1) {
                // seçilen satırdaki değerleri alalım
                int id = (int) tbl_hotel_list.getValueAt(tbl_hotel_list.getSelectedRow(), 0);
                String name = fld_hotel_name.getText();
                String city = fld_hotel_country.getText();
                String region = fld_hotel_region.getText();
                String address = fld_hotel_address.getText();
                String email = fld_hotel_email.getText();
                String phone = fld_hotel_phone.getText();
                String star = getSelectedRadioButtonText(btn_group_star);
                boolean freePark = cbx_free_parking.isSelected();
                boolean freeWifi = cbx_free_wifi.isSelected();
                boolean swimmingPool = cbx_swimming_pool.isSelected();
                boolean fitnessCenter = cbx_fitness_center.isSelected();
                boolean hotelConcierge = cbx_hotel_concierge.isSelected();
                boolean spa = cbx_spa.isSelected();
                boolean roomService = cbx_room_service.isSelected();
                boolean ultraAllInclusive = cbx_ultra_all_inclusive.isSelected();
                boolean allInclusive = cbx_alI_inclusive.isSelected();
                boolean bedAndBreakfast = cbx_room_breakfast.isSelected();
                boolean fullBoard = cbx_full_board.isSelected();
                boolean halfBoard = cbx_half_board.isSelected();
                boolean roomOnly = cbx_room_only.isSelected();
                boolean nonAlcoholFull = cbx_alcohol_free.isSelected();

                // update metodunu çağıralım

                if (Hotel.update(id, name, city, region, address, email, phone, star, freePark, freeWifi, swimmingPool, fitnessCenter, hotelConcierge, spa, roomService, ultraAllInclusive, allInclusive, bedAndBreakfast, fullBoard, halfBoard, roomOnly, nonAlcoholFull)) {
                    Helper.showMsg("done");
                    clearHotelAddPanel();
                    loadHotelModel();
                } else {
                    Helper.showMsg("error");
                    loadHotelModel();
                }
            } else {
                // eğer hiçbir satır seçilmemişse
                Helper.showMsg("Lütfen güncellemek istediğiniz oteli seçiniz!");
            }
        });

        btn_hotel_delete.addActionListener(e -> {
            if (tbl_hotel_list.getSelectedRow() != -1) {
                // seçilen satırdaki değerleri alalım
                int id = (int) tbl_hotel_list.getValueAt(tbl_hotel_list.getSelectedRow(), 0);
                System.out.println(id);
                // delete metodunu çağıralım
                if (Hotel.delete(id)) {
                    Helper.showMsg("done");
                    clearHotelAddPanel();
                    loadHotelModel();
                } else {
                    Helper.showMsg("error");
                    loadHotelModel();
                }
            } else {
                // eğer hiçbir satır seçilmemişse
                Helper.showMsg("Lütfen silmek istediğiniz oteli seçiniz!");
            }
        });

        btn_hotel_panel_clear.addActionListener(e -> {
            clearHotelAddPanel();
        });

        btn_period_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_period_name) || Helper.isFieldEmpty(fld_hotel_period) || JDateChooser1.getDate() == null || JDateChooser2.getDate() == null) {
                Helper.showMsg("fill");

            } else {
                String periodName = fld_period_name.getText();
                String hotelId = fld_hotel_period.getText();
                java.sql.Date startDate = new java.sql.Date(JDateChooser1.getDate().getTime());
                java.sql.Date endDate = new java.sql.Date(JDateChooser2.getDate().getTime());
                if (Period.addPeriodsForHotel(Integer.parseInt(hotelId), startDate, endDate, periodName)) {
                    Helper.showMsg("done");
                    loadPeriodModel(fld_hotel_period.getText());
                    loadPeriodComboBox(Integer.parseInt(hotelId));
                } else {
                    Helper.showMsg("Bu dönem bilgileri sistemde kayıtlıdır!");

                }
            }
        });
        btn_period_delete_with_combobox.addActionListener(e -> {
            if (chbx_period_list.getSelectedIndex() != -1) {
                if (Helper.confirm("sure")) {
                    int id = ((Item) chbx_period_list.getSelectedItem()).getKey();
                    if (Period.delete(id)) {
                        Helper.showMsg("done");
                        loadPeriodModel(fld_hotel_period.getText());
                        loadPeriodComboBox(Integer.parseInt(fld_hotel_period.getText()));
                    } else {
                        Helper.showMsg("error");
                    }
                }
            } else {
                Helper.showMsg("Lütfen silmek istediğiniz dönemi seçiniz!");
            }
        });

        btn_add_hotel_board.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tbl_hotel_list.getSelectedRow() != -1) {
                    if (Helper.confirm("sure")) {
                        int hotelID = Integer.parseInt(fld_hotel_period.getText());
                        boolean ultraAllInclusive = cbx_ultra_all_inclusive.isSelected();
                        boolean allInclusive = cbx_alI_inclusive.isSelected();
                        boolean bedAndBreakfast = cbx_room_breakfast.isSelected();
                        boolean fullBoard = cbx_full_board.isSelected();
                        boolean halfBoard = cbx_half_board.isSelected();
                        boolean roomOnly = cbx_room_only.isSelected();
                        boolean nonAlcoholFull = cbx_alcohol_free.isSelected();
                        if (addBoardTypesByHotelID(hotelID, ultraAllInclusive, allInclusive, bedAndBreakfast, fullBoard, halfBoard, roomOnly, nonAlcoholFull)) {
                            Helper.showMsg("done");
                            loadBoardModel(fld_hotel_period.getText());
                        } else {
                            Helper.showMsg("error");
                        }
                    }
                }else {
                    Helper.showMsg("Lütfen pansiyon tipi eklemek istediğiniz oteli seçiniz!");
                }
            }
        });

        btn_delete_hotel_board.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //deleteBoardTypesByHotelID
                if (tbl_hotel_list.getSelectedRow() != -1) {
                    if (Helper.confirm("sure")) {
                        int hotelID = Integer.parseInt(fld_hotel_period.getText());
                        boolean ultraAllInclusive = cbx_ultra_all_inclusive.isSelected();
                        boolean allInclusive = cbx_alI_inclusive.isSelected();
                        boolean bedAndBreakfast = cbx_room_breakfast.isSelected();
                        boolean fullBoard = cbx_full_board.isSelected();
                        boolean halfBoard = cbx_half_board.isSelected();
                        boolean roomOnly = cbx_room_only.isSelected();
                        boolean nonAlcoholFull = cbx_alcohol_free.isSelected();
                        if (deleteBoardTypesByHotelID(hotelID, ultraAllInclusive, allInclusive, bedAndBreakfast, fullBoard, halfBoard, roomOnly, nonAlcoholFull)) {
                            Helper.showMsg("done");
                            loadBoardModel(fld_hotel_period.getText());
                        } else {
                            Helper.showMsg("error");
                        }
                    }
                } else {
                    Helper.showMsg("Lütfen pansiyon tipi silmek istediğiniz oteli seçiniz!");
                }
            }
        });
    }

    private void loadPeriodModel(String id) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_period.getModel();
        clearModel.setRowCount(0);

        for (Period obj : Period.getPeriodsForHotel(id)) {
            row_period_list[0] = obj.getPeriodId();
            row_period_list[1] = obj.getPeriodName();
            row_period_list[2] = obj.getStartDate();
            row_period_list[3] = obj.getEndDate();
            mdl_period_list.addRow(row_period_list);
        }
    }

    public void loadHotelModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_hotel_list.getModel();
        clearModel.setRowCount(0);
        for (Hotel obj : Hotel.getList()) {
            int i = 0;
            row_hotel_list[i++] = obj.getId();
            row_hotel_list[i++] = obj.getName();
            row_hotel_list[i++] = obj.getCity();
            row_hotel_list[i++] = obj.getRegion();
            row_hotel_list[i++] = obj.getAddress();
            row_hotel_list[i++] = obj.getEmail();
            row_hotel_list[i++] = obj.getPhone();
            row_hotel_list[i++] = obj.getStar();
            row_hotel_list[i++] = obj.isFreePark() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isFreeWifi() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isSwimmingPool() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isFitnessCenter() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isHotelConcierge() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isSpa() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isRoomService() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isUltraAllInclusive() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isAllInclusive() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isBedAndBreakfast() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isFullBoard() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isHalfBoard() ? "Var" : "Yok";
            row_hotel_list[i++] = obj.isRoomOnly() ? "Var" : "Yok";
            row_hotel_list[i] = obj.isNonAlcoholFull() ? "Var" : "Yok";
            mdl_hotel_list.addRow(row_hotel_list);
        }
    }

    public void loadBoardModel(String id) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_board_type.getModel();
        clearModel.setRowCount(0);

        for (BoardType obj : BoardType.getHotelTypeNameByID(id)) {
            row_board_list[0] = obj.getHotel_board_type(); // dizinin ilk elemanına pansiyon ismini ata
            mdl_board_list.addRow(row_board_list);
        }
    }


    public void setTbl_hotel_list() {
        // tablodan seçilen satırın indeksini alalım
        int selectedRow = tbl_hotel_list.getSelectedRow();
// eğer bir satır seçilmişse
        if (selectedRow != -1) {
            // seçilen satırdaki değerleri alalım
            int id = (int) tbl_hotel_list.getValueAt(selectedRow, 0);
            String name = (String) tbl_hotel_list.getValueAt(selectedRow, 1);
            String city = (String) tbl_hotel_list.getValueAt(selectedRow, 2);
            String region = (String) tbl_hotel_list.getValueAt(selectedRow, 3);
            String address = (String) tbl_hotel_list.getValueAt(selectedRow, 4);
            String email = (String) tbl_hotel_list.getValueAt(selectedRow, 5);
            String phone = (String) tbl_hotel_list.getValueAt(selectedRow, 6);
            String star = (String) tbl_hotel_list.getValueAt(tbl_hotel_list.getSelectedRow(), 7);
            // radio butonlarınızın her biri için
            for (int i = 1; i <= 7; i++) {
                // radio buton nesnesini alalım
                JRadioButton radioButton = null;
                try {
                    radioButton = (JRadioButton) this.getClass().getDeclaredField("rbutton_" + i).get(this);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchFieldException ex) {
                    throw new RuntimeException(ex);
                }
                // eğer bu radio butonun text değeri star değişkenine eşitse
                if (radioButton.getText().equals(star)) {
                    // bu radio butonu seçelim
                    btn_group_star.setSelected(radioButton.getModel(), true);
                    // döngüden çıkalım
                    break;
                }
            }
            boolean freePark = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean freeWifi = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean swimmingPool = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean fitnessCenter = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean hotelConcierge = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean spa = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean roomService = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean ultraAllInclusive = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean allInclusive = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean bedAndBreakfast = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean fullBoard = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean halfBoard = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean roomOnly = false; // boolean değeri almak için ayrı bir kod yazacağız
            boolean nonAlcoholFull = false; // boolean değeri almak için ayrı bir kod yazacağız

            for (int i = 8; i <= 21; i++) {
                // tablodan alınan değeri bir nesneye atayalım
                Object value = tbl_hotel_list.getValueAt(selectedRow, i);
                // eğer bu nesne String ise
                if (value instanceof String) {
                    // bu nesneyi String tipe dönüştürelim
                    String strValue = (String) value;
                    // bu değeri ilgili değişkene atayalım
                    switch (i) {
                        case 8:
                            freePark = strValue.equals("Var");
                            break;
                        case 9:
                            freeWifi = strValue.equals("Var");
                            break;
                        case 10:
                            swimmingPool = strValue.equals("Var");
                            break;
                        case 11:
                            fitnessCenter = strValue.equals("Var");
                            break;
                        case 12:
                            hotelConcierge = strValue.equals("Var");
                            break;
                        case 13:
                            spa = strValue.equals("Var");
                            break;
                        case 14:
                            roomService = strValue.equals("Var");
                            break;
                        case 15:
                            ultraAllInclusive = strValue.equals("Var");
                            break;
                        case 16:
                            allInclusive = strValue.equals("Var");
                            break;
                        case 17:
                            bedAndBreakfast = strValue.equals("Var");
                            break;
                        case 18:
                            fullBoard = strValue.equals("Var");
                            break;
                        case 19:
                            halfBoard = strValue.equals("Var");
                            break;
                        case 20:
                            roomOnly = strValue.equals("Var");
                            break;
                        case 21:
                            nonAlcoholFull = strValue.equals("Var");
                            break;
                    }
                } else {
                    // eğer bu nesne String değilse
                    // bir hata mesajı gösterelim
                    JOptionPane.showMessageDialog(null, "Tablodan alınan değer String türünde değil!", "Hata", JOptionPane.ERROR_MESSAGE);
                    // döngüden çıkalım
                    break;
                }
            }
            // bu değerleri otel ekleme kısmındaki alanlara dolduralım
            fld_hotel_name.setText(name);
            fld_hotel_country.setText(city);
            fld_hotel_region.setText(region);
            fld_hotel_address.setText(address);
            fld_hotel_email.setText(email);
            fld_hotel_phone.setText(phone);
            String selectedStar = getSelectedRadioButtonText(btn_group_star);
            cbx_free_parking.setSelected(freePark);
            cbx_free_wifi.setSelected(freeWifi);
            cbx_swimming_pool.setSelected(swimmingPool);
            cbx_fitness_center.setSelected(fitnessCenter);
            cbx_hotel_concierge.setSelected(hotelConcierge);
            cbx_spa.setSelected(spa);
            cbx_room_service.setSelected(roomService);
            cbx_ultra_all_inclusive.setSelected(ultraAllInclusive);
            cbx_alI_inclusive.setSelected(allInclusive);
            cbx_room_breakfast.setSelected(bedAndBreakfast);
            cbx_full_board.setSelected(fullBoard);
            cbx_half_board.setSelected(halfBoard);
            cbx_room_only.setSelected(roomOnly);
            cbx_alcohol_free.setSelected(nonAlcoholFull);
        }
    }

    public static String getSelectedRadioButtonText(ButtonGroup group) {
        // gruptaki tüm radio butonlarına bakalım
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements(); ) {
            // sıradaki radio butonu alalım
            AbstractButton button = buttons.nextElement();
            // eğer bu radio buton seçilmişse
            if (button.isSelected()) {
                // bu radio butonun text değerini döndürelim
                return button.getText();
            }
        }
        // eğer hiçbir radio buton seçilmemişse
        // boş bir string döndürelim
        return "";
    }

    public void clearHotelAddPanel() {
        fld_hotel_name.setText(null);
        fld_hotel_country.setText(null);
        fld_hotel_region.setText(null);
        fld_hotel_address.setText(null);
        fld_hotel_email.setText(null);
        fld_hotel_phone.setText(null);
        btn_group_star.clearSelection();
        cbx_free_parking.setSelected(false);
        cbx_free_wifi.setSelected(false);
        cbx_swimming_pool.setSelected(false);
        cbx_fitness_center.setSelected(false);
        cbx_hotel_concierge.setSelected(false);
        cbx_spa.setSelected(false);
        cbx_room_service.setSelected(false);
        cbx_ultra_all_inclusive.setSelected(false);
        cbx_alI_inclusive.setSelected(false);
        cbx_room_breakfast.setSelected(false);
        cbx_full_board.setSelected(false);
        cbx_half_board.setSelected(false);
        cbx_room_only.setSelected(false);
        cbx_alcohol_free.setSelected(false);
    }

    public void setFld_hotel_period(String fld_hotel_period) {
        this.fld_hotel_period.setText(fld_hotel_period);
    }

    public void loadPeriodComboBox(int id) {
        chbx_period_list.removeAllItems();
        for (Period obj : Period.getPeriodsForHotel(String.valueOf(id))) {
            chbx_period_list.addItem(new Item(obj.getPeriodId(), obj.getPeriodName()));
        }
    }

    public void revalidateAll() {
        revalidate();
        repaint();
    }

    public static boolean addBoardTypesByHotelID(int hotelID, boolean ultraAllInclusive, boolean allInclusive, boolean bedAndBreakfast, boolean fullBoard, boolean halfBoard, boolean roomOnly, boolean nonAlcoholFull) {
        try {
            if (ultraAllInclusive) {
                int ultraAllInclusiveID = 1;
                addBoardTypeByHotelID(hotelID, ultraAllInclusiveID);
            }

            if (allInclusive) {
                int allInclusiveID = 2;
                addBoardTypeByHotelID(hotelID, allInclusiveID);
            }

            if (bedAndBreakfast) {
                int bedAndBreakfastID = 3;
                addBoardTypeByHotelID(hotelID, bedAndBreakfastID);
            }

            if (fullBoard) {
                int fullBoardID = 4;
                addBoardTypeByHotelID(hotelID, fullBoardID);
            }

            if (halfBoard) {
                int halfBoardID = 5;
                addBoardTypeByHotelID(hotelID, halfBoardID);
            }

            if (roomOnly) {
                int roomOnlyID = 6;
                addBoardTypeByHotelID(hotelID, roomOnlyID);
            }

            if (nonAlcoholFull) {
                int nonAlcoholFullID = 7;
                addBoardTypeByHotelID(hotelID, nonAlcoholFullID);
            }

            return true; // Ekleme işlemi başarılıysa true döndürün.

        } catch (Exception e) {
            // Hata durumunda false döndürün veya hata işlemlerini gerçekleştirin.
            return false;
        }
    }

    public static boolean deleteBoardTypesByHotelID(int hotelID, boolean ultraAllInclusive, boolean allInclusive, boolean bedAndBreakfast, boolean fullBoard, boolean halfBoard, boolean roomOnly, boolean nonAlcoholFull) {
        try {
            if (ultraAllInclusive) {
                int ultraAllInclusiveID = 1;
                deleteBoardTypes(hotelID, ultraAllInclusiveID);
            }

            if (allInclusive) {
                int allInclusiveID = 2;
                deleteBoardTypes(hotelID, allInclusiveID);
            }

            if (bedAndBreakfast) {
                int bedAndBreakfastID = 3;
                deleteBoardTypes(hotelID, bedAndBreakfastID);
            }

            if (fullBoard) {
                int fullBoardID = 4;
                deleteBoardTypes(hotelID, fullBoardID);
            }

            if (halfBoard) {
                int halfBoardID = 5;
                deleteBoardTypes(hotelID, halfBoardID);
            }

            if (roomOnly) {
                int roomOnlyID = 6;
                deleteBoardTypes(hotelID, roomOnlyID);
            }

            if (nonAlcoholFull) {
                int nonAlcoholFullID = 7;
                deleteBoardTypes(hotelID, nonAlcoholFullID);
            }

            return true; // Ekleme işlemi başarılıysa true döndürün.

        } catch (Exception e) {
            // Hata durumunda false döndürün veya hata işlemlerini gerçekleştirin.
            return false;
        }
    }

}
