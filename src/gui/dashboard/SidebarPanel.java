package gui.dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {

    public interface SidebarListener {
        void onMenuSelected(String menuName);
    }

    private SidebarListener listener;
    private List<JButton> menuButtons = new ArrayList<>();
    private JButton selectedButton = null;

    public SidebarPanel(SidebarListener listener) {
        this.listener = listener;
        setPreferredSize(new Dimension(240, 0)); // Fixed width 240px
        setBackground(new Color(30, 30, 35)); // Modern dark theme
        setLayout(new BorderLayout());

        // Logo Area
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(30, 10, 30, 10));
        
        JLabel lblLogoIcon = new JLabel("🐝", SwingConstants.CENTER);
        lblLogoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42)); // Slightly smaller
        lblLogoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblLogoText = new JLabel("JOLLIBEE WAREHOUSE", SwingConstants.CENTER);
        lblLogoText.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLogoText.setForeground(new Color(255, 180, 0)); // Yellow
        lblLogoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(lblLogoIcon);
        logoPanel.add(Box.createVerticalStrut(10));
        logoPanel.add(lblLogoText);

        // Menu Buttons Area
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(0, 15, 10, 15));

        String[][] menus = {
            {"HOME", "🏠", "Trang chủ"},
            {"PRODUCT", "📦", "Quản lý sản phẩm"},
            {"IMPORT", "📥", "Quản lý nhập kho"},
            {"EXPORT", "📤", "Quản lý xuất kho"},
            {"EMPLOYEE", "👥", "Quản lý nhân viên"},
            {"STATISTIC", "📊", "Thống kê báo cáo"}
        };

        for (String[] m : menus) {
            JButton btn = createMenuButton(m[0], m[1], m[2]);
            menuButtons.add(btn);
            menuPanel.add(btn);
            menuPanel.add(Box.createVerticalStrut(8)); // Consistent spacing
        }

        // Logout Area
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        JButton btnLogout = createMenuButton("LOGOUT", "🚪", "Đăng xuất");
        btnLogout.setForeground(new Color(255, 100, 100)); // Light red for logout
        logoutPanel.add(btnLogout, BorderLayout.CENTER);

        add(logoPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
        add(logoutPanel, BorderLayout.SOUTH);

        // Set default selection
        if (!menuButtons.isEmpty()) {
            selectButton(menuButtons.get(0));
        }
    }

    private JButton createMenuButton(String commandName, String icon, String text) {
        JButton btn = new JButton(icon + "   " + text);
        btn.setActionCommand(commandName);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Normal weight, 14px
        btn.setForeground(new Color(220, 220, 220));
        btn.setBackground(new Color(30, 30, 35));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(210, 45)); // Height 45px
        btn.setPreferredSize(new Dimension(210, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addActionListener(e -> {
            if (commandName.equals("LOGOUT")) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else {
                selectButton(btn);
                if (listener != null) {
                    listener.onMenuSelected(commandName);
                }
            }
        });

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn != selectedButton) {
                    btn.setBackground(new Color(50, 50, 55));
                    btn.setForeground(new Color(255, 180, 0)); // Yellow on hover
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn != selectedButton) {
                    btn.setBackground(new Color(30, 30, 35));
                    if (commandName.equals("LOGOUT")) {
                        btn.setForeground(new Color(255, 100, 100));
                    } else {
                        btn.setForeground(new Color(220, 220, 220));
                    }
                }
            }
        });

        return btn;
    }

    private void selectButton(JButton btn) {
        if (selectedButton != null) {
            selectedButton.setBackground(new Color(30, 30, 35));
            selectedButton.setForeground(new Color(220, 220, 220));
            selectedButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        selectedButton = btn;
        selectedButton.setBackground(new Color(220, 60, 20)); // Jollibee Red/Orange
        selectedButton.setForeground(Color.WHITE);
        selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}
