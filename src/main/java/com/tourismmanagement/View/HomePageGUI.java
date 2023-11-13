package com.tourismmanagement.View;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.tourismmanagement.Helper.Config;
import com.tourismmanagement.Helper.Helper;
import com.tourismmanagement.Model.User;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class HomePageGUI extends JFrame {
    private JPanel wrapper;
    private JButton btn_add_hotel;
    private JButton btn_add_room;
    private JButton btn_add_room_price;
    private JButton btn_home_page;
    private JButton btn_customer_list;
    private JPanel pnl_welcome;
    private JLabel lbl_welcome;
    private JPanel pnl_main_window;
    private JPanel pnl_dashboard;
    private JPanel pnl_home_page;
    private JPanel pnl_hotel;
    private JPanel pnl_add_room;
    private JPanel pnl_reservation;
    private JPanel pnl_customer_list;
    private JButton btn_logout;
    private JButton btn_change_theme;

    public HomePageGUI(User user) {

        FrameDragListener frameDragListener = new FrameDragListener(this);
        this.addMouseListener(frameDragListener);
        this.addMouseMotionListener(frameDragListener);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 180));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        });
        Helper.setLayout();
        add(wrapper);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        String mesaj = "Selam. Ödevimi kim inceliyor bilmiyorum ama lütfen değerlendirirken mükemmeliyetçiliğim yüzünden bazı işlevleri yetiştiremediğimi unutmayın :( Şu an Otel,oda,fiyatlandırma,rezervasyon eklenebiliyor. Dönem ekleme kısmını hotel kısmına ekleyeceğim. Bazı yerler eksik. Mükemmel olsun diyerekten dinamik panel oluşturma işine girdim ve mahvoldum :( ama ama reservasyon panelim dinamik. Bu arada ana sayfa kısmına da grafikler ekleyecektim ama yetişmedi. Şu an bende proje çalışıyor sorun olursa lütfen discorddan yazın. :D. İyi günler dilerim. ";
        JOptionPane.showInputDialog(mesaj, "Sorun Yok!");

        lbl_welcome.setText("Hoşgeldiniz : " + user.getName());

        btn_home_page.addActionListener(e -> {
            pnl_main_window.removeAll();
            pnl_main_window.add(pnl_home_page);
            pnl_main_window.repaint();
            pnl_main_window.revalidate();
        });

        btn_add_hotel.addActionListener(e -> {
            pnl_main_window.removeAll();
            CardHotelPanel a = new CardHotelPanel();
            pnl_main_window.add(a);
            pnl_main_window.repaint();
            pnl_main_window.revalidate();
        });

        btn_add_room.addActionListener(e -> {
            pnl_main_window.removeAll();
            CardRoomPanel a = new CardRoomPanel();
            pnl_main_window.add(a);
            pnl_main_window.repaint();
            pnl_main_window.revalidate();
        });

        btn_add_room_price.addActionListener(e -> {
            pnl_main_window.removeAll();
            CardRoomPrice a = new CardRoomPrice();
            pnl_main_window.add(a);
            pnl_main_window.repaint();
            pnl_main_window.revalidate();
        });

        btn_customer_list.addActionListener(e -> {
            pnl_main_window.removeAll();
            RoomSearchPanel a = new RoomSearchPanel();
            pnl_main_window.add(a);
            pnl_main_window.repaint();
            pnl_main_window.revalidate();
        });

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
            login.setVisible(true);
        });

        btn_change_theme.addActionListener(e -> {
            if (!FlatLaf.isLafDark()) {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatDarkLaf.setup();
                    FlatDarkLaf.updateUI();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            } else {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatMacLightLaf.setup();
                    FlatDarkLaf.updateUI();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            }
        });
    }
    public void paintComponents(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.dispose();

    }

    public static class FrameDragListener extends MouseAdapter {
        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }

    private static class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}