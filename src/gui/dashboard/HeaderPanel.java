package gui.dashboard;

import gui.dashboard.model.CartManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.function.Consumer;

public class HeaderPanel extends JPanel {

    private JLabel lblTime;
    private JLabel lblCartCount;

    public HeaderPanel(String adminName, CartManager cartManager, Runnable onCartClicked, Consumer<String> onSearch) {
        setLayout(new BorderLayout());
        setBackground(new Color(220, 60, 20)); // Jollibee Orange-Brown
        setPreferredSize(new Dimension(0, 70));
        setBorder(new EmptyBorder(0, 20, 0, 20));

        // Left side: Title
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Quản Lý Kho Jollibee");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        leftPanel.add(lblTitle);

        // Center: Search Bar
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setOpaque(false);
        
        JTextField txtSearch = new JTextField(20); 
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.setBackground(new Color(255, 255, 255, 240));
        
        JButton btnSearch = new JButton("🔍");
        btnSearch.setPreferredSize(new Dimension(40, 35));
        btnSearch.setBackground(new Color(255, 180, 0)); // Jollibee Yellow
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setBorder(BorderFactory.createEmptyBorder());
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSearch.addActionListener(e -> {
            if (onSearch != null) {
                onSearch.accept(txtSearch.getText());
            }
        });
        
        txtSearch.addActionListener(e -> {
            if (onSearch != null) {
                onSearch.accept(txtSearch.getText());
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; searchPanel.add(txtSearch, gbc);
        gbc.gridx = 1; gbc.gridy = 0; searchPanel.add(btnSearch, gbc);

        // Right side: Time, Cart & Avatar
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        lblTime = new JLabel();
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTime.setForeground(new Color(255, 220, 180));
        lblTime.setBorder(new EmptyBorder(0, 0, 0, 20));
        startTimer();
        
        // Cart Button
        JPanel cartPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        cartPanel.setOpaque(false);
        JButton btnCart = new JButton("🛒");
        btnCart.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        btnCart.setContentAreaFilled(false);
        btnCart.setBorderPainted(false);
        btnCart.setFocusPainted(false);
        btnCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCart.setForeground(Color.WHITE);
        btnCart.addActionListener(e -> onCartClicked.run());
        
        lblCartCount = new JLabel("(0)");
        lblCartCount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCartCount.setForeground(Color.YELLOW);
        cartPanel.add(btnCart);
        cartPanel.add(lblCartCount);
        cartPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

        // Listen to Cart Changes
        cartManager.setOnCartChanged(() -> {
            lblCartCount.setText("(" + cartManager.getTotalQuantity() + ")");
        });

        JLabel lblAdminName = new JLabel(adminName);
        lblAdminName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAdminName.setForeground(Color.WHITE);
        lblAdminName.setBorder(new EmptyBorder(0, 0, 0, 10));

        JLabel lblAvatar = new JLabel("👨‍💼");
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24)); 

        GridBagConstraints rGbc = new GridBagConstraints();
        rGbc.gridx = 0; rightPanel.add(lblTime, rGbc);
        rGbc.gridx = 1; rightPanel.add(cartPanel, rGbc);
        rGbc.gridx = 2; rightPanel.add(lblAdminName, rGbc);
        rGbc.gridx = 3; rightPanel.add(lblAvatar, rGbc);

        add(leftPanel, BorderLayout.WEST);
        add(searchPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void startTimer() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            lblTime.setText("🕒 " + sdf.format(new Date()));
        });
        timer.start();
    }
}
