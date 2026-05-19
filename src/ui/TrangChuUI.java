package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TrangChuUI extends JPanel {

    private JLabel lblTime;

    public TrangChuUI(String currentUser) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        mainContent.add(buildTopBar(currentUser), BorderLayout.NORTH);
        mainContent.add(buildBody(), BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
        startClock();
    }

    private JPanel buildTopBar(String currentUser) {
        JPanel topBar = new JPanel(new BorderLayout(20, 0));
        topBar.setBackground(new Color(220, 50, 50));
        topBar.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel titleGroup = new JPanel(new BorderLayout(4, 4));
        titleGroup.setOpaque(false);
        JLabel lblTitle = new JLabel("Quản Lý Kho Jollibee");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblSubtitle = new JLabel("Dashboard");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        titleGroup.add(lblTitle, BorderLayout.NORTH);
        titleGroup.add(lblSubtitle, BorderLayout.SOUTH);

        JPanel searchGroup = new JPanel(new BorderLayout(8, 0));
        searchGroup.setOpaque(false);
        JTextField txtSearch = new JTextField("Tìm kiếm sản phẩm...");
        txtSearch.setBackground(new Color(255, 245, 245));
        txtSearch.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        searchGroup.add(txtSearch, BorderLayout.CENTER);
        JButton btnSearch = new JButton("🔍");
        btnSearch.setFocusPainted(false);
        btnSearch.setBackground(Color.WHITE);
        searchGroup.add(btnSearch, BorderLayout.EAST);

        JPanel userGroup = new JPanel(new GridLayout(2, 1));
        userGroup.setOpaque(false);
        lblTime = new JLabel();
        lblTime.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTime.setForeground(Color.WHITE);
        JLabel lblUser = new JLabel(currentUser != null && !currentUser.isEmpty() ? currentUser : "Admin Jollibee");
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        lblUser.setForeground(Color.WHITE);
        userGroup.add(lblTime);
        userGroup.add(lblUser);

        topBar.add(titleGroup, BorderLayout.WEST);
        topBar.add(searchGroup, BorderLayout.CENTER);
        topBar.add(userGroup, BorderLayout.EAST);

        return topBar;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(20, 20));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(20, 20, 20, 20));

        body.add(buildHeaderCards(), BorderLayout.NORTH);
        body.add(buildMainSection(), BorderLayout.CENTER);

        return body;
    }

    private JPanel buildHeaderCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 20, 0));
        cards.setOpaque(false);
        cards.add(createStatCard("1,250", "Tổng sản phẩm", "📦", new Color(37, 99, 235)));
        cards.add(createStatCard("15", "Hàng sắp hết", "⚠️", new Color(234, 88, 12)));
        cards.add(createStatCard("320", "Nhập hôm nay", "⬆️", new Color(16, 185, 129)));
        cards.add(createStatCard("45.5M", "Doanh thu", "💰", new Color(220, 153, 26)));
        return cards;
    }

    private JPanel buildMainSection() {
        JPanel section = new JPanel(new BorderLayout(20, 20));
        section.setOpaque(false);

        section.add(buildFeaturedProducts(), BorderLayout.NORTH);
        section.add(buildChartPanel(), BorderLayout.CENTER);

        return section;
    }

    private JPanel buildFeaturedProducts() {
        JPanel products = new JPanel(new GridLayout(1, 6, 16, 0));
        products.setOpaque(false);
        products.add(createProductCard("Gà Giòn Vui Vẻ", "35,000 đ", "Kho: 120", "🍗"));
        products.add(createProductCard("Mì Ý Sốt Xúc Xích", "40,000 đ", "Kho: 85", "🍝"));
        products.add(createProductCard("Khoai Tây Chiên", "20,000 đ", "Kho: 200", "🍟"));
        products.add(createProductCard("Burger Gà", "45,000 đ", "Kho: 50", "🍔"));
        products.add(createProductCard("Nước Ngọt", "15,000 đ", "Kho: 500", "🥤"));
        products.add(createProductCard("Kem Sundae", "25,000 đ", "Kho: 75", "🍨"));
        return products;
    }

    private JPanel buildChartPanel() {
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setOpaque(false);
        chartContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Thống Kê Giao Dịch", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(30, 30, 30));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        chartContainer.add(lblTitle, BorderLayout.NORTH);
        chartContainer.add(new ChartPanel(), BorderLayout.CENTER);

        return chartContainer;
    }

    private JPanel createStatCard(String value, String label, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        lblIcon.setForeground(color);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblValue.setForeground(new Color(30, 30, 30));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLabel.setForeground(new Color(120, 120, 120));

        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);
        text.add(lblValue);
        text.add(lblLabel);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    private JPanel createProductCard(String title, String price, String stock, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        JLabel lblName = new JLabel(title, SwingConstants.CENTER);
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setForeground(new Color(40, 40, 40));

        JLabel lblPrice = new JLabel(price, SwingConstants.CENTER);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrice.setForeground(new Color(220, 80, 60));

        JLabel lblStock = new JLabel(stock, SwingConstants.CENTER);
        lblStock.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStock.setForeground(new Color(130, 130, 130));

        JPanel info = new JPanel(new GridLayout(3, 1, 4, 4));
        info.setOpaque(false);
        info.add(lblName);
        info.add(lblPrice);
        info.add(lblStock);

        card.add(lblIcon, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    private void startClock() {
        lblTime.setText(getCurrentTime());
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                lblTime.setText(getCurrentTime());
            }
        }, 1000, 1000);
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    private static class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            int chartHeight = height - 60;
            int chartWidth = width - 80;
            int x0 = 60;
            int y0 = height - 30;

            g2.setColor(new Color(240, 240, 240));
            for (int i = 0; i <= 5; i++) {
                int y = y0 - i * chartHeight / 5;
                g2.drawLine(x0, y, x0 + chartWidth, y);
            }

            int[] valuesNhap = {120, 150, 180, 140, 210};
            int[] valuesXuat = {90, 110, 155, 135, 205};
            String[] labels = {"T1", "T2", "T3", "T4", "T5"};
            int barWidth = 24;
            int gap = (chartWidth - valuesNhap.length * barWidth * 2) / (valuesNhap.length + 1);

            for (int i = 0; i < valuesNhap.length; i++) {
                int x = x0 + gap + i * (barWidth * 2 + gap);
                int yNhap = y0 - valuesNhap[i] * chartHeight / 220;
                int yXuat = y0 - valuesXuat[i] * chartHeight / 220;
                g2.setColor(new Color(16, 185, 129));
                g2.fillRoundRect(x, yNhap, barWidth, y0 - yNhap, 10, 10);
                g2.setColor(new Color(239, 68, 68));
                g2.fillRoundRect(x + barWidth + 8, yXuat, barWidth, y0 - yXuat, 10, 10);
                g2.setColor(new Color(120, 120, 120));
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                int labelX = x + barWidth / 2;
                g2.drawString(labels[i], labelX, y0 + 18);
            }

            g2.setColor(new Color(120, 120, 120));
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Nhập", x0 + chartWidth - 110, 18);
            g2.setColor(new Color(16, 185, 129));
            g2.fillRect(x0 + chartWidth - 140, 10, 12, 12);
            g2.setColor(new Color(239, 68, 68));
            g2.fillRect(x0 + chartWidth - 60, 10, 12, 12);
            g2.setColor(new Color(120, 120, 120));
            g2.drawString("Xuất", x0 + chartWidth - 45, 18);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Trang Chủ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 720);
        frame.setLocationRelativeTo(null);
        frame.add(new TrangChuUI("Admin"));
        frame.setVisible(true);
    }
}
