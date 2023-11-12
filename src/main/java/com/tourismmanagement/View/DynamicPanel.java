package com.tourismmanagement.View;

import com.tourismmanagement.Model.BoardType;
import com.tourismmanagement.Model.Period;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DynamicPanel extends JFrame {
    public DynamicPanel(int secilenOtel, int secilenOda) {
        setLayout(new BorderLayout());
        JPanel dinamikPanel = new JPanel();
        dinamikPanel.setLayout(new BoxLayout(dinamikPanel, BoxLayout.Y_AXIS));

        List<String> donemler = Period.getDonemlerFromDatabase(secilenOtel, secilenOda);

        for (String donem : donemler) {
            // Oda İsmi ve Dönem Tarih Aralığı başlıkları
            JPanel baslikPanel = new JPanel();
            baslikPanel.setLayout(new GridLayout(1, 2));
            baslikPanel.add(new JLabel("Oda İsmi"));
            baslikPanel.add(new JLabel("Dönem Tarih Aralığı: " + donem));
            dinamikPanel.add(baslikPanel);

            // Pansiyon başlıkları
            JPanel pansiyonBaslikPanel = new JPanel();
            pansiyonBaslikPanel.setLayout(new GridLayout(1, 5));
            pansiyonBaslikPanel.add(new JLabel("Pansiyon Tipi"));
            pansiyonBaslikPanel.add(new JLabel("Yetişkin 12+"));
            pansiyonBaslikPanel.add(new JLabel("Çocuk 4-11"));
            dinamikPanel.add(pansiyonBaslikPanel);

            List<String> pansiyonTipleri = BoardType.getPansiyonTipleriFromDatabase(secilenOtel, secilenOda, donem);
            for (String pansiyon : pansiyonTipleri) {
                JPanel rowPanel = new JPanel();
                rowPanel.setLayout(new GridLayout(1, 5));

                rowPanel.add(new JLabel(pansiyon));
                rowPanel.add(new JTextField());
                rowPanel.add(new JTextField());
                dinamikPanel.add(rowPanel);
            }
        }

        // Kaydet butonu
        JButton kaydetButton = new JButton("Fiyat Ayarla");
        JPanel kaydetPanel = new JPanel();
        kaydetPanel.setLayout(new GridLayout(1, 1));
        kaydetPanel.add(kaydetButton);
        dinamikPanel.add(kaydetPanel);

        add(dinamikPanel, BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Fiyat Ayarlama");

        kaydetButton.addActionListener(e -> {
            dinamikPanel.removeAll();
            dinamikPanel.revalidate();
            dinamikPanel.repaint();
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DynamicPanel frame = new DynamicPanel(1, 1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
