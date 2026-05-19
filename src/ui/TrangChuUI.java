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

    private JTabbedPane tabs;
    private JLabel lblTime;
    private String currentUser;

    public TrangChuUI(String currentUser) {
        this(currentUser, null);
    }

    public TrangChuUI(String currentUser, JTabbedPane tabs) {
        this.currentUser = currentUser;
        this.tabs = tabs;
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

        JLabel lblTitle = new JLabel("Quản Lý Kho Jollibee");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        JTextField txtSearch = new JTextField("Tìm kiếm...");
        txtSearch.setPreferredSize(new Dimension(360, 38));
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setOpaque(false);
        searchWrapper.add(txtSearch, BorderLayout.CENTER);

        JPanel userGroup = new JPanel(new GridLayout(2, 1, 4, 4));
        userGroup.setOpaque(false);
        lblTime = new JLabel();
        lblTime.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTime.setForeground(Color.WHITE);
        JLabel lblUser = new JLabel(formatUserName(currentUser));
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        lblUser.setForeground(Color.WHITE);
        userGroup.add(lblTime);
        userGroup.add(lblUser);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(searchWrapper, BorderLayout.CENTER);
        topBar.add(userGroup, BorderLayout.EAST);

        return topBar;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(20, 0));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(20, 20, 20, 20));

        body.add(buildSidebar(), BorderLayout.WEST);
        body.add(buildHomeContent(), BorderLayout.CENTER);

        return body;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(18, 23, 28));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel lblBrand = new JLabel("JOLLIBEE WAREHOUSE");
        lblBrand.setFont(new Font("Arial", Font.BOLD, 16));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);

        String[] navLabels = {"Bán Hàng", "Nguyên Liệu", "Nhà Cung Cấp", "Nhân Viên", "Phiếu Nhập", "Phiếu Xuất"};
        int[] navIndex = {1, 2, 3, 4, 5, 6};
        for (int i = 0; i < navLabels.length; i++) {
            JButton btn = new JButton(navLabels[i]);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(35, 40, 47));
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            int index = navIndex[i];
            btn.addActionListener(e -> {
                if (tabs != null && tabs.getTabCount() > index) {
                    tabs.setSelectedIndex(index);
                }
            });
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navPanel.add(btn);
            navPanel.add(Box.createVerticalStrut(10));
        }

        JTextField searchField = new JTextField("Tìm kiếm sản phẩm...");
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        searchField.setBackground(new Color(255, 255, 255, 230));
        searchField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogout.setForeground(new Color(220, 50, 50));
        btnLogout.setBackground(new Color(255, 255, 255));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogout.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(TrangChuUI.this);
            if (window instanceof JFrame) {
                ((JFrame) window).dispose();
                new gui.LoginGUI().setVisible(true);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(Box.createVerticalGlue());
        bottomPanel.add(btnLogout);

        JPanel sidebarContent = new JPanel();
        sidebarContent.setOpaque(false);
        sidebarContent.setLayout(new BoxLayout(sidebarContent, BoxLayout.Y_AXIS));
        sidebarContent.add(lblBrand);
        sidebarContent.add(searchField);
        sidebarContent.add(Box.createVerticalStrut(20));
        sidebarContent.add(navPanel);
        sidebarContent.add(Box.createVerticalGlue());

        sidebar.add(sidebarContent, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel buildHomeContent() {
        JPanel homeContent = new JPanel(new BorderLayout(0, 20));
        homeContent.setOpaque(false);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(buildGreetingSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildHeaderCards());
        content.add(Box.createVerticalStrut(20));
        content.add(buildFeaturedProducts());
        content.add(Box.createVerticalStrut(20));
        content.add(buildTransactionSection());

        JScrollPane contentScroll = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScroll.setBorder(BorderFactory.createEmptyBorder());
        contentScroll.getViewport().setOpaque(false);
        contentScroll.setOpaque(false);
        contentScroll.getVerticalScrollBar().setUnitIncrement(16);

        homeContent.add(contentScroll, BorderLayout.CENTER);
        return homeContent;
    }


    private JPanel buildGreetingSection() {
        JPanel greeting = new JPanel(new BorderLayout());
        greeting.setOpaque(false);
        JLabel lblGreeting = new JLabel("Chào mừng, " + formatUserName(currentUser) + " 👋");
        lblGreeting.setFont(new Font("Arial", Font.BOLD, 24));
        lblGreeting.setForeground(new Color(30, 30, 30));
        greeting.add(lblGreeting, BorderLayout.WEST);
        return greeting;
    }

    private String formatUserName(String user) {
        if (user == null || user.trim().isEmpty()) {
            return "admin";
        }
        String normalized = user.trim();
        if (normalized.replaceAll("\\s+", "").equalsIgnoreCase("admin")) {
            return "admin";
        }
        return normalized;
    }

    private JPanel buildHeaderCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 20, 0));
        cards.setOpaque(false);
        cards.add(createStatCard("1,250", "Tổng sản phẩm", "📦", new Color(37, 99, 235), "+12% so với hôm qua", 1));
        cards.add(createStatCard("15", "Hàng sắp hết", "⚠️", new Color(234, 88, 12), "-5% so với tuần trước", 1));
        cards.add(createStatCard("320", "Nhập hôm nay", "⬆️", new Color(16, 185, 129), "+8% so với hôm qua", 4));
        cards.add(createStatCard("45.5M", "Doanh thu", "💰", new Color(220, 153, 26), "+18% so với tháng trước", -1));
        return cards;
    }

    private JPanel buildMainSection() {
        JPanel section = new JPanel(new BorderLayout(20, 20));
        section.setOpaque(false);

        section.add(buildFeaturedProducts(), BorderLayout.NORTH);
        section.add(buildTransactionSection(), BorderLayout.CENTER);

        return section;
    }

    private JPanel buildFeaturedProducts() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setOpaque(false);

        JLabel lblTitle = new JLabel("Danh sách sản phẩm nổi bật");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 30, 30));
        section.add(lblTitle, BorderLayout.NORTH);

        JPanel products = new JPanel(new GridLayout(1, 6, 16, 0));
        products.setOpaque(false);
        products.setPreferredSize(new Dimension(0, 220));
        products.setMinimumSize(new Dimension(0, 220));
        products.add(createProductCard("Gà Giòn Vui Vẻ", "35,000 đ", "Kho: 120", "🍗"));
        products.add(createProductCard("Mì Ý Sốt Xúc Xích", "40,000 đ", "Kho: 85", "🍝"));
        products.add(createProductCard("Khoai Tây Chiên", "20,000 đ", "Kho: 200", "🍟"));
        products.add(createProductCard("Burger Gà", "45,000 đ", "Kho: 50", "🍔"));
        products.add(createProductCard("Nước Ngọt", "15,000 đ", "Kho: 500", "🥤"));
        products.add(createProductCard("Kem Sundae", "25,000 đ", "Kho: 75", "🍨"));

        JScrollPane scrollPane = new JScrollPane(products, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(0, 240));
        section.add(scrollPane, BorderLayout.CENTER);

        return section;
    }

    private JPanel buildTransactionSection() {
        JPanel transaction = new JPanel(new BorderLayout(0, 20));
        transaction.setOpaque(false);
        transaction.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel chartWrapper = buildChartPanel();
        chartWrapper.setPreferredSize(new Dimension(0, 340));
        transaction.add(chartWrapper, BorderLayout.CENTER);
        transaction.add(buildRatioPanel(), BorderLayout.SOUTH);

        return transaction;
    }

    private JPanel buildRatioPanel() {
        JPanel ratioPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        ratioPanel.setOpaque(false);

        ratioPanel.add(createMiniStatCard("Nhập", "1.890", "+16% so với tuần trước", new Color(16, 185, 129)));
        ratioPanel.add(createMiniStatCard("Xuất", "1.650", "+10% so với tuần trước", new Color(239, 68, 68)));
        ratioPanel.add(createMiniStatCard("Tỉ lệ N/X", "114%", "Nhập nhiều hơn xuất", new Color(59, 130, 246)));

        return ratioPanel;
    }

    private JPanel createMiniStatCard(String label, String value, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 20));
        lblValue.setForeground(color);

        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLabel.setForeground(new Color(100, 100, 100));

        JLabel lblDesc = new JLabel(description, SwingConstants.CENTER);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(130, 130, 130));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(lblLabel);
        text.add(Box.createVerticalStrut(8));
        text.add(lblValue);
        text.add(Box.createVerticalStrut(8));
        text.add(lblDesc);

        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildChartPanel() {
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setOpaque(true);
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
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

    private JPanel createStatCard(String value, String label, String icon, Color color, String ratio, int targetTab) {
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

        JLabel lblRatio = new JLabel(ratio);
        lblRatio.setFont(new Font("Arial", Font.PLAIN, 11));
        lblRatio.setForeground(new Color(100, 140, 100));

        JPanel text = new JPanel(new GridLayout(3, 1, 4, 4));
        text.setOpaque(false);
        text.add(lblValue);
        text.add(lblLabel);
        text.add(lblRatio);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        if (targetTab >= 0) {
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(new Color(250, 250, 250));
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(color, 2, true),
                            new EmptyBorder(17, 17, 17, 17)
                    ));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBackground(Color.WHITE);
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(230, 230, 230)),
                            new EmptyBorder(18, 18, 18, 18)
                    ));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (tabs != null) {
                        tabs.setSelectedIndex(targetTab);
                    }
                }
            });
        }

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
            g2.drawString("Nhập", x0 + chartWidth - 170, 18);
            g2.setColor(new Color(16, 185, 129));
            g2.fillRect(x0 + chartWidth - 200, 10, 12, 12);
            g2.setColor(new Color(239, 68, 68));
            g2.fillRect(x0 + chartWidth - 110, 10, 12, 12);
            g2.setColor(new Color(120, 120, 120));
            g2.drawString("Xuất", x0 + chartWidth - 90, 18);

            g2.setColor(new Color(170, 170, 170));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(x0, y0, x0 + chartWidth, y0);
            g2.drawLine(x0, y0, x0, y0 - chartHeight);
            for (int i = 0; i <= 5; i++) {
                int y = y0 - i * chartHeight / 5;
                g2.setColor(new Color(200, 200, 200));
                g2.drawLine(x0 - 5, y, x0, y);
                if (i < 5) {
                    g2.setColor(new Color(120, 120, 120));
                    g2.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2.drawString(String.valueOf(i * 50), x0 - 35, y + 4);
                }
            }
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
