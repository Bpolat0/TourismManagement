package com.tourismmanagement.View;

import com.tourismmanagement.Helper.Helper;
import com.tourismmanagement.Helper.Item;
import com.tourismmanagement.Helper.JGraph;
import com.tourismmanagement.Model.Hotel;
import com.tourismmanagement.Model.Reservation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CardHomePanel extends JPanel {
    private JPanel wrapper;
    private JPanel pnl_room;
    private JPanel pnl_reservation;
    private JTable tbl_reservation_list;
    private JPanel pnl_dashboard;
    private JPanel pnl_hotel_control_button;
    private JButton btn_update_reservation;
    private JButton btn_delete_reservation;
    private JComboBox chbx_hotel_list;
    private JPanel pnl_graph;
    private JButton btn_graph;
    private JButton btn_show_unshow_graph;
    private DefaultTableModel mdl_reservation_list;
    private Object[] row_reservation_list;

    public CardHomePanel() {
        setLayout(new BorderLayout());
        add(wrapper);
        setSize(600, 400);
        pnl_graph.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow]"));

        loadHotelCombo();

        //ModelRoomList
        mdl_reservation_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tüm hücrelerin düzenlenmesini engeller
            }
        };
        Object[] col_reservation_list = {"İD", "Oda Tipi", "Müşteri Adı", "Müşteri Mail", "Müşteri TC", "Giriş Tarihi", "Çıkış Tarihi", "Ödenen/Ödenecek Toplam"};
        mdl_reservation_list.setColumnIdentifiers(col_reservation_list);
        row_reservation_list = new Object[col_reservation_list.length];
        loadReservationModel();
        tbl_reservation_list.setModel(mdl_reservation_list);
        tbl_reservation_list.getColumnModel().getColumn(0).setPreferredWidth(1);
        tbl_reservation_list.getTableHeader().setReorderingAllowed(false);
        tbl_reservation_list.getTableHeader().setResizingAllowed(false);


        //#ModelRoomList

        JPanel graph_1 = new JPanel();
        JPanel graph_2 = new JPanel();
        JPanel graph_3 = new JPanel();

        pnl_graph.add(graph_1, "cell 0 0, grow");
        pnl_graph.add(graph_2, "cell 1 0, grow");
        pnl_graph.add(graph_3, "cell 2 0, grow");

        btn_graph.addActionListener(e -> {
            pnl_graph.removeAll();
            pnl_graph.revalidate();
            pnl_graph.repaint();

            createAllGraph();
        });

        btn_show_unshow_graph.addActionListener(e -> {
            if (pnl_graph.isVisible()) {
                pnl_graph.setVisible(false);
            } else {
                pnl_graph.setVisible(true);
            }
        });

        btn_delete_reservation.addActionListener(e -> {
            int selectedRow = tbl_reservation_list.getSelectedRow();
            if (selectedRow == -1) {
                Helper.showMsg("Lütfen silmek istediğiniz rezervasyonu seçiniz!");
                return;
            }
            int id = (int) tbl_reservation_list.getValueAt(selectedRow, 0);
            if (Reservation.delete(id)) {
                Helper.showMsg("Rezervasyon silindi!");
                loadReservationModel();
            } else {
                Helper.showMsg("Rezervasyon silinemedi!");
            }
            pnl_graph.removeAll();
            pnl_graph.revalidate();
            pnl_graph.repaint();
            createAllGraph();
        });
    }

    private void loadReservationModel() {
        mdl_reservation_list.setRowCount(0);
        for (Reservation obj : Reservation.getSpecialReservationList()) {
            row_reservation_list[0] = obj.getId();
            row_reservation_list[1] = obj.getRoom_type();
            row_reservation_list[2] = obj.getCustomer_name();
            row_reservation_list[3] = obj.getCustomer_email();
            row_reservation_list[4] = obj.getCustomer_tc();
            row_reservation_list[5] = obj.getStart_date();
            row_reservation_list[6] = obj.getEnd_date();
            row_reservation_list[7] = obj.getTotal_price();
            mdl_reservation_list.addRow(row_reservation_list);
        }
    }

    public void loadHotelCombo() {
        chbx_hotel_list.removeAllItems();
        for (Hotel obj : Hotel.getList()) {
            chbx_hotel_list.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void createAllGraph(){
        JPanel roomTypeStockChart1 = JGraph.createRoomTypeStockChart(((Item) chbx_hotel_list.getSelectedItem()).getKey());
        pnl_graph.add(roomTypeStockChart1, "cell 0 0,grow");

        JPanel reservationCountChart1 = JGraph.createReservationCountChart(((Item) chbx_hotel_list.getSelectedItem()).getKey());
        pnl_graph.add(reservationCountChart1, "cell 1 0,grow");

        JPanel totalIncomeChart1 = JGraph.createHotelTotalIncomeChart(((Item) chbx_hotel_list.getSelectedItem()).getKey());
        pnl_graph.add(totalIncomeChart1, "cell 2 0,grow");
    }

}
