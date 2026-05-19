package gui.dashboard;

import gui.dashboard.model.Product;
import gui.dashboard.model.CartManager;
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
import java.util.ArrayList;
import java.util.List;

public class DashboardPanel extends JPanel {

    private CartManager cartManager;
    private List<Product> allProducts;
    private JPanel productList;
    private boolean showMetrics;
    
    public DashboardPanel(CartManager cartManager) {
        this(cartManager, true);
    }

    public DashboardPanel(CartManager cartManager, boolean showMetrics) {
        this.cartManager = cartManager;
        this.showMetrics = showMetrics;
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);
        
        JLabel lblGreeting = new JLabel("Chào mừng, Admin 👋");
        lblGreeting.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblGreeting.setForeground(new Color(40, 40, 40));
        topPanel.add(lblGreeting, BorderLayout.NORTH);

        if (showMetrics) {
            JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            statsPanel.setOpaque(false);
            statsPanel.add(createStatCard("Tổng sản phẩm", "1,250", "📦", new Color(65, 105, 225)));
            statsPanel.add(createStatCard("Hàng sắp hết", "15", "⚠️", new Color(220, 50, 50)));
            statsPanel.add(createStatCard("Nhập hôm nay", "320", "📥", new Color(46, 139, 87)));
            statsPanel.add(createStatCard("Doanh thu", "45.5M", "💰", new Color(255, 140, 0)));
            topPanel.add(statsPanel, BorderLayout.CENTER);
        }

        JPanel middlePanel = new JPanel(new BorderLayout(0, 5));
        middlePanel.setOpaque(false);
        
        JPanel headerMiddlePanel = new JPanel(new BorderLayout());
        headerMiddlePanel.setOpaque(false);
        
        JLabel lblProductTitle = new JLabel("Danh sách sản phẩm nổi bật");
        lblProductTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblProductTitle.setForeground(new Color(60, 60, 60));
        headerMiddlePanel.add(lblProductTitle, BorderLayout.WEST);
        
        // Category filters
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        categoryPanel.setOpaque(false);
        String[] categories = {"Tất cả", "Gà Rán", "Thức Uống", "Ăn Kèm"};
        for (String cat : categories) {
            JButton btnCat = new JButton(cat);
            btnCat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnCat.setBackground(Color.WHITE);
            btnCat.setFocusPainted(false);
            btnCat.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCat.addActionListener(e -> filterProducts(cat.equals("Tất cả") ? "" : cat));
            categoryPanel.add(btnCat);
        }
        headerMiddlePanel.add(categoryPanel, BorderLayout.EAST);
        
        middlePanel.add(headerMiddlePanel, BorderLayout.NORTH);

        productList = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        productList.setOpaque(false);
        
        // Mock Products
        allProducts = new ArrayList<>();
        allProducts.add(new Product("SP001", "Gà Giòn Vui Vẻ", 35000, 120, "🍗")); // Gà Rán
        allProducts.add(new Product("SP002", "Mì Ý Sốt Xúc Xích", 40000, 85, "🍝")); // Ăn Kèm
        allProducts.add(new Product("SP003", "Khoai Tây Chiên", 20000, 200, "🍟")); // Ăn Kèm
        allProducts.add(new Product("SP004", "Burger Gà", 45000, 50, "🍔")); // Ăn Kèm
        allProducts.add(new Product("SP005", "Nước Ngọt", 15000, 500, "🥤")); // Thức Uống
        allProducts.add(new Product("SP006", "Kem Sundae", 25000, 75, "🍦")); // Ăn Kèm
        allProducts.add(new Product("SP007", "Gà Sốt Cay", 38000, 90, "🍗")); // Gà Rán
        allProducts.add(new Product("SP008", "Trà Đào", 22000, 150, "🍹")); // Thức Uống

        renderProducts(allProducts);

        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        middlePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JFreeChart chart = null;
        if (showMetrics) {
            chart = createChart();
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 240)); 
            chartPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));
            bottomPanel.add(chartPanel, BorderLayout.CENTER);
        }

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setOpaque(false);
        
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        middlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (showMetrics) {
            bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        
        contentWrapper.add(topPanel);
        contentWrapper.add(Box.createVerticalStrut(15));
        contentWrapper.add(middlePanel);
        if (showMetrics) {
            contentWrapper.add(Box.createVerticalStrut(15));
            contentWrapper.add(bottomPanel);
        }
        
        add(contentWrapper, BorderLayout.NORTH);
    }

    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(240, 110));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblIcon.setForeground(color);
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        infoPanel.setOpaque(false);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(color);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(100, 100, 100));

        infoPanel.add(lblValue);
        infoPanel.add(lblTitle);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(lblIcon, BorderLayout.EAST);
        return card;
    }

    private JFreeChart createChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(120, "Nhập", "T1");
        dataset.addValue(150, "Nhập", "T2");
        dataset.addValue(180, "Nhập", "T3");
        dataset.addValue(140, "Nhập", "T4");
        dataset.addValue(210, "Nhập", "T5");
        
        dataset.addValue(90, "Xuất", "T1");
        dataset.addValue(130, "Xuất", "T2");
        dataset.addValue(160, "Xuất", "T3");
        dataset.addValue(150, "Xuất", "T4");
        dataset.addValue(200, "Xuất", "T5");

        JFreeChart chart = ChartFactory.createBarChart("Thống Kê Giao Dịch", "", "", dataset, PlotOrientation.VERTICAL, true, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(46, 139, 87));
        renderer.setSeriesPaint(1, new Color(220, 60, 20));
        return chart;
    }

    public void filterProducts(String keyword) {
        if (keyword == null) keyword = "";
        String kw = keyword.toLowerCase().trim();
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            boolean match = false;
            if (p.getName().toLowerCase().contains(kw) || p.getId().toLowerCase().contains(kw)) {
                match = true;
            }
            // Category check based on mock names
            if (kw.equalsIgnoreCase("Gà Rán") && p.getName().contains("Gà")) match = true;
            if (kw.equalsIgnoreCase("Thức Uống") && (p.getName().contains("Nước") || p.getName().contains("Trà"))) match = true;
            if (kw.equalsIgnoreCase("Ăn Kèm") && !p.getName().contains("Gà") && !p.getName().contains("Nước") && !p.getName().contains("Trà")) match = true;
            
            if (kw.isEmpty() || match) {
                filtered.add(p);
            }
        }
        renderProducts(filtered);
    }

    private void renderProducts(List<Product> products) {
        productList.removeAll();
        for (Product p : products) {
            productList.add(new ProductCard(p, cartManager));
        }
        productList.revalidate();
        productList.repaint();
    }
}
