package com.tourismmanagement.Helper;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Helper {

    public static int screenCenterPoint(String axis, Dimension size) {
        int point;
        switch (axis) {
            case "x":
                point = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
                break;
            case "y":
                point = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
                break;
            default:
                point = 0;
        }
        return point;
    }

    public static void setLayout() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            FlatDarkLaf.registerCustomDefaultsSource("GlassIcon");
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldEmpty(JComboBox field) {
        return field.getSelectedItem().toString().trim().isEmpty();
    }



    public static void showMsg(String str) {
        optionPageTR();
        String msg;
        String title;

        switch (str) {
            case "fill":
                msg = "Lütfen Tüm Alanları Doldurunuz !";
                title = "Hata!";
                break;
            case "done":
                msg = "İşlem başarılı !";
                title = "Başarılı!";
                break;
            case "error":
                msg = "Hatalı veri girdisi !";
                title = "Database Hatası!";
                break;
            default:
                msg = str;
                title = "Mesaj";
        }

        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str) {
        optionPageTR();
        String msg;
        switch (str) {
            case "sure":
                msg = "Bu işlemi gerçekleştirmek istediğinize emin misiniz?";
                break;
            default:
                msg = str;
        }
        return JOptionPane.showConfirmDialog(null, msg, "Son Kararın Mı?", JOptionPane.YES_NO_OPTION) == 0;
    }

    public static void optionPageTR() {
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }
}
