package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import dao.ReportDAO;
import model.NguyenLieu;

public class TongQuanKhoUI extends JPanel {

    private final Color jollibeeRed = new Color(224, 31, 42);
    private final Color jollibeeOrange = new Color(242, 142, 43);
    private final Color lightCream = new Color(255, 253, 240); // Nền kem Jollibee #FFFDF0
    private final Color darkCharcoal = new Color(45, 45, 45);

    private JLabel lblTotalIngredients;
    private JLabel lblTotalValue;
    private JLabel lblTodayImports;
    private JLabel lblTodayExports;
    private JLabel lblLowStockWarning;

    private JTable tblLowStock;
    private DefaultTableModel tableModel;
    private BarChartPanel chartPanel;

    private ReportDAO reportDAO;

    public TongQuanKhoUI() {
        this.reportDAO = new ReportDAO();
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(lightCream);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- 1. TITLE ---
        JLabel lblTitle = new JLabel("BẢNG ĐIỀU HÀNH & GIÁM SÁT KHO JOLLIBEE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(jollibeeRed);
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblTitle, BorderLayout.NORTH);

        // --- 2. MAIN CENTER BODY ---
        JPanel pnlCenter = new JPanel(new BorderLayout(15, 15));
        pnlCenter.setOpaque(false);

        // KPI Panel (Grid layout of cards)
        JPanel pnlKPI = new JPanel(new GridLayout(1, 5, 15, 0));
        pnlKPI.setOpaque(false);
        pnlKPI.setPreferredSize(new Dimension(0, 120));

        lblTotalIngredients = new JLabel("0", SwingConstants.CENTER);
        lblTotalValue = new JLabel("0 VNĐ", SwingConstants.CENTER);
        lblTodayImports = new JLabel("0", SwingConstants.CENTER);
        lblTodayExports = new JLabel("0", SwingConstants.CENTER);
        lblLowStockWarning = new JLabel("0", SwingConstants.CENTER);

        pnlKPI.add(createKPICard("TỔNG NGUYÊN LIỆU", lblTotalIngredients, jollibeeRed));
        pnlKPI.add(createKPICard("TỔNG GIÁ TRỊ KHO", lblTotalValue, jollibeeOrange));
        pnlKPI.add(createKPICard("PHIẾU NHẬP HÔM NAY", lblTodayImports, new Color(40, 167, 69)));
        pnlKPI.add(createKPICard("PHIẾU XUẤT HÔM NAY", lblTodayExports, new Color(0, 123, 255)));
        pnlKPI.add(createKPICard("CẢNH BÁO HẾT HÀNG", lblLowStockWarning, Color.MAGENTA));

        pnlCenter.add(pnlKPI, BorderLayout.NORTH);

        // Split Pane containing Chart (Left) and Low Stock Table (Right)
        JPanel pnlSplit = new JPanel(new GridLayout(1, 2, 15, 15));
        pnlSplit.setOpaque(false);

        // Left Panel: Top 10 materials chart
        JPanel pnlChart = new JPanel(new BorderLayout());
        pnlChart.setBackground(Color.WHITE);
        pnlChart.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(jollibeeRed, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel lblChartTitle = new JLabel("TOP 10 NGUYÊN LIỆU TỒN KHO NHIỀU NHẤT", SwingConstants.CENTER);
        lblChartTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblChartTitle.setForeground(darkCharcoal);
        pnlChart.add(lblChartTitle, BorderLayout.NORTH);

        chartPanel = new BarChartPanel();
        pnlChart.add(chartPanel, BorderLayout.CENTER);

        pnlSplit.add(pnlChart);

        // Right Panel: Low Stock Table
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(jollibeeRed, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel lblTableTitle = new JLabel("CẢNH BÁO NGUYÊN LIỆU SẮP HẾT HÀNG (TỒN KHO <= 10)", SwingConstants.CENTER);
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTableTitle.setForeground(jollibeeRed);
        pnlTable.add(lblTableTitle, BorderLayout.NORTH);

        String[] columns = {"Mã NL", "Tên Nguyên Liệu", "Số Lượng", "Đơn Vị", "Giá Trị"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblLowStock = new JTable(tableModel);
        tblLowStock.setRowHeight(28);
        tblLowStock.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Custom Table Header
        JTableHeader header = tblLowStock.getTableHeader();
        header.setBackground(jollibeeRed);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Render highlight red for low stock
        tblLowStock.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(table, val, isSelected, hasFocus, r, c);
                
                int qty = Integer.parseInt(table.getValueAt(r, 2).toString());
                if (qty == 0) {
                    comp.setBackground(new Color(255, 204, 204)); // Darker pink/red
                    comp.setForeground(Color.RED);
                } else {
                    comp.setBackground(new Color(255, 230, 230)); // Soft pink/red
                    comp.setForeground(darkCharcoal);
                }
                
                if (isSelected) {
                    comp.setBackground(new Color(255, 180, 0, 150));
                    comp.setForeground(Color.BLACK);
                }
                
                setHorizontalAlignment(c == 0 || c == 2 || c == 3 ? JLabel.CENTER : JLabel.LEFT);
                if (c == 4) setHorizontalAlignment(JLabel.RIGHT);
                
                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblLowStock);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlTable.add(scrollPane, BorderLayout.CENTER);

        pnlSplit.add(pnlTable);
        pnlCenter.add(pnlSplit, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);
    }

    private JPanel createKPICard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(accentColor, 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(darkCharcoal);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    public void refreshData() {
        // Load KPIs
        int totalIngredients = reportDAO.getTotalIngredientsCount();
        double totalValue = reportDAO.getTotalInventoryValue();
        int todayImports = reportDAO.getTodayImportReceiptsCount();
        int todayExports = reportDAO.getTodayExportReceiptsCount();
        
        List<NguyenLieu> lowStockList = reportDAO.getLowStockIngredients(10);
        int lowStockCount = lowStockList.size();

        lblTotalIngredients.setText(String.valueOf(totalIngredients));
        DecimalFormat df = new DecimalFormat("#,##0");
        lblTotalValue.setText(df.format(totalValue) + " đ");
        lblTodayImports.setText(String.valueOf(todayImports));
        lblTodayExports.setText(String.valueOf(todayExports));
        lblLowStockWarning.setText(String.valueOf(lowStockCount));

        if (lowStockCount > 0) {
            lblLowStockWarning.setForeground(Color.RED);
        } else {
            lblLowStockWarning.setForeground(darkCharcoal);
        }

        // Load Table
        tableModel.setRowCount(0);
        for (NguyenLieu nl : lowStockList) {
            double value = nl.getGianhap() * nl.getSoluong();
            tableModel.addRow(new Object[]{
                    nl.getMaNL(),
                    nl.getTenNL(),
                    nl.getSoluong(),
                    nl.getDonvi(),
                    df.format(value) + " đ"
            });
        }
        // Tự động giãn cột bảng cảnh báo tồn kho thấp
        util.UIHelper.autoResizeColumnWidths(tblLowStock);

        // Load Chart
        List<Object[]> chartData = reportDAO.getTop10IngredientsByQuantity();
        chartPanel.setChartData(chartData);
    }

    // --- Custom JPanel for Dynamic Graph rendering ---
    private class BarChartPanel extends JPanel {
        private List<Object[]> data;

        public BarChartPanel() {
            setBackground(Color.WHITE);
        }

        public void setChartData(List<Object[]> data) {
            this.data = data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) {
                g.setColor(Color.GRAY);
                g.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                g.drawString("Không có dữ liệu tồn kho", getWidth() / 2 - 80, getHeight() / 2);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int paddingLeft = 130;
            int paddingRight = 40;
            int paddingTop = 20;
            int paddingBottom = 20;

            int chartWidth = width - paddingLeft - paddingRight;
            int chartHeight = height - paddingTop - paddingBottom;

            // Draw Y-axis line
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(paddingLeft, paddingTop, paddingLeft, height - paddingBottom);

            // Find max value to scale chart
            int maxVal = 0;
            for (Object[] row : data) {
                int val = (int) row[1];
                if (val > maxVal) maxVal = val;
            }
            if (maxVal == 0) maxVal = 1;

            int numBars = data.size();
            int barHeight = Math.max(12, (chartHeight / numBars) - 10);
            int yInterval = chartHeight / numBars;

            for (int i = 0; i < numBars; i++) {
                Object[] row = data.get(i);
                String name = (String) row[0];
                int val = (int) row[1];

                int barWidth = (int) (((double) val / maxVal) * chartWidth);
                int x = paddingLeft;
                int y = paddingTop + (i * yInterval) + (yInterval - barHeight) / 2;

                // Draw Bar
                // Alternate bar colors Jollibee Red and Jollibee Orange
                Color barColor = (i % 2 == 0) ? jollibeeRed : jollibeeOrange;
                g2.setColor(barColor);
                g2.fillRoundRect(x, y, barWidth, barHeight, 5, 5);

                // Draw label name on the left
                g2.setColor(darkCharcoal);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                FontMetrics fm = g2.getFontMetrics();
                int labelWidth = fm.stringWidth(name);
                int labelX = paddingLeft - labelWidth - 8;
                int labelY = y + (barHeight + fm.getAscent() - fm.getDescent()) / 2;
                
                // Truncate name if too long
                String displayName = name;
                if (labelX < 5) {
                    displayName = name.substring(0, Math.min(name.length(), 15)) + "..";
                    labelWidth = fm.stringWidth(displayName);
                    labelX = paddingLeft - labelWidth - 8;
                }
                g2.drawString(displayName, labelX, labelY);

                // Draw quantity text on the right of each bar
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2.drawString(String.valueOf(val), x + barWidth + 5, labelY);
            }
        }
    }
}
