package com.tourismmanagement.View;

import com.tourismmanagement.Helper.Config;
import com.tourismmanagement.Helper.DBConnector;
import com.tourismmanagement.Helper.Helper;
import com.tourismmanagement.Model.Operator;
import com.tourismmanagement.Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginGUI extends JFrame {

    private JPanel wrapper;
    private JPanel pnl_top;
    private JLabel lbl_welcome;
    private JButton btn_exit;
    private JPanel pnl_login;
    private JTextField fld_user_name;
    private JLabel lbl_username;
    private JButton btn_login;
    private JPasswordField fld_password;
    private JLabel lbl_pass;
    private User user;
    private Operator operator;

    public LoginGUI() {
        this.user = new User();
        this.operator = new Operator();

        HomePageGUI.FrameDragListener frameDragListener = new HomePageGUI.FrameDragListener(this);
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
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);

        btn_login.addActionListener(e -> {

            String username = fld_user_name.getText();
            String password = String.valueOf(fld_password.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                Helper.showMsg("Kullanıcı adı ve şifre boş bırakılamaz!");
                return;
            }

            String query = "SELECT * FROM `user` WHERE uname=? AND pass=?";
            try {
                PreparedStatement preparedStatement = DBConnector.getInstance().prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet rs = preparedStatement.executeQuery();


                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String uname = rs.getString("uname");
                    String pass = rs.getString("pass");
                    String type = rs.getString("type");
                    if (username.equals(uname) && (password.equals(pass))) {
                        if (type.equals("Admin")) {
                            user.setId(id);
                            user.setName(name);
                            user.setUsername(uname);
                            user.setPassword(pass);
                            user.setType(type);
                            dispose();
                            OperatorGUI hm = new OperatorGUI(user);
                            hm.setVisible(true);
                            return;
                        } else if (type.equals("User")) {
                            dispose();
                            user.setId(id);
                            user.setName(name);
                            user.setUsername(uname);
                            user.setPassword(pass);
                            user.setType(type);
                            HomePageGUI hm = new HomePageGUI(user);
                            hm.setVisible(true);
                            return;
                        }
                    }
                }

                Helper.showMsg("Bilgilerinizi kontrol ediniz!");
                fld_password.setText("");
                fld_user_name.setText("");


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btn_exit.addActionListener(e -> dispose());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
        login.setVisible(true);
    }
}

