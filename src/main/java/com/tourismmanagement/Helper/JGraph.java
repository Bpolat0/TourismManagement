package com.tourismmanagement.Helper;

import java.awt.*;
import java.sql.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;

public class JGraph {
    public static DefaultPieDataset getRoomTypeStockData(int hotelId) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try {
            String queryRoomTypesStock = "SELECT room_type, stock_quantity FROM rooms WHERE hotel_id = ?";
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(queryRoomTypesStock);
            pst.setInt(1, hotelId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dataset.setValue(rs.getString("room_type"), rs.getInt("stock_quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public static JPanel createRoomTypeStockChart(int hotelId) {
        DefaultPieDataset dataset = getRoomTypeStockData(hotelId);
        JFreeChart chart = ChartFactory.createPieChart(
                "Oda Tipleri Ve Stoklar", // Grafiğin başlığı
                dataset,
                true,
                true,
                false
        );
        chart.getPlot().setForegroundAlpha(0.7f);
        ((PiePlot) chart.getPlot()).setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
        return new ChartPanel(chart);
    }

    public static DefaultPieDataset getReservationCount(int hotelId) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try {
            String query = "SELECT COUNT(*) FROM reservations WHERE hotel_id = ?";
            PreparedStatement pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotelId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dataset.setValue("Rezervasyon Sayısı", rs.getInt("COUNT(*)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public static JPanel createReservationCountChart(int hotelId) {
        DefaultPieDataset dataset = getReservationCount(hotelId);
        JFreeChart chart = ChartFactory.createPieChart(
                "Rezervasyon Sayısı", // Grafiğin başlığı
                dataset,
                true,
                true,
                false
        );
        chart.getPlot().setForegroundAlpha(0.7f);
        ((PiePlot) chart.getPlot()).setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
        return new ChartPanel(chart);
    }

    public static DefaultPieDataset getHotelTotalIncome(int hotelId) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        String query = "SELECT SUM(total_price) FROM reservations WHERE hotel_id = ?";
        double totalIncome = 0.0;
        PreparedStatement pst = null;
        try {
            pst = DBConnector.getInstance().prepareStatement(query);
            pst.setInt(1, hotelId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                totalIncome = rs.getDouble("SUM(total_price)");
            }
            dataset.setValue("Toplam Gelir", totalIncome);

            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataset;
    }

    public static JPanel createHotelTotalIncomeChart(int hotelId) {
        DefaultPieDataset dataset = getHotelTotalIncome(hotelId);
        JFreeChart chart = ChartFactory.createPieChart(
                "Toplam Gelir", // Grafiğin başlığı
                dataset,
                true,
                true,
                false
        );
        chart.getPlot().setForegroundAlpha(0.7f);
        ((PiePlot) chart.getPlot()).setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
        return new ChartPanel(chart);
    }


}
