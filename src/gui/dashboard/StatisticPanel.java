package gui.dashboard;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatisticPanel extends JPanel {

    public StatisticPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblTitle = new JLabel("📊 Thống Kê Báo Cáo");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));
        
        centerPanel.add(chartPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JFreeChart createChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(120, "Nhập kho", "T1");
        dataset.addValue(150, "Nhập kho", "T2");
        dataset.addValue(180, "Nhập kho", "T3");
        dataset.addValue(140, "Nhập kho", "T4");
        dataset.addValue(210, "Nhập kho", "T5");
        dataset.addValue(250, "Nhập kho", "T6");
        
        dataset.addValue(90, "Xuất kho", "T1");
        dataset.addValue(130, "Xuất kho", "T2");
        dataset.addValue(160, "Xuất kho", "T3");
        dataset.addValue(150, "Xuất kho", "T4");
        dataset.addValue(200, "Xuất kho", "T5");
        dataset.addValue(220, "Xuất kho", "T6");

        JFreeChart chart = ChartFactory.createBarChart(
                "Thống Kê Nhập / Xuất Kho (6 Tháng Gần Nhất)",
                "Tháng",
                "Số Lượng (Đơn vị)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(46, 139, 87)); // Green
        renderer.setSeriesPaint(1, new Color(220, 60, 20)); // Red-Orange (Jollibee style)
        
        return chart;
    }
}
