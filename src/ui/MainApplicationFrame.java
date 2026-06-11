
package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.HashMap; 
import java.util.Map; 
import javax.swing.*;
import javax.swing.border.EmptyBorder; 
import model.TaiKhoan; 

public class MainApplicationFrame extends JFrame {
    private TaiKhoan currentUser;

    // 🌟 Hệ màu sắc nhận diện thương hiệu Jollibee chống mỏi mắt
    private final Color jollibeeRed = new Color(224, 31, 42);     // Đỏ tươi Jollibee
    private final Color jollibeeOrange = new Color(242, 142, 43); // Vàng cam mật ong
    private final Color jollibeeBrown = new Color(139, 69, 19);   // Nâu trầm ấm
    private final Color lightCream = new Color(255, 251, 245);    // Nền kem sữa ấm áp
    private final Color darkCharcoal = new Color(45, 45, 45);     // Xám đen thanh menu

    // UI Components điều khiển Layout chính
    private JPanel contentPanel; 
    private CardLayout cardLayout; 
    private JLabel lblAppTitle; 
    private JLabel lblLogo; 
    private JLabel lblWelcome; 
    private JButton btnLogout; 
    private JPanel menuPanel;

    // Các phân hệ UI liên thông nghiệp vụ thực tế của nhóm bạn
    private NhapKhoUI nhapKhoUI; 
    private NguyenLieuAll NguyenLieuAll; // Phân hệ quản lý nguyên liệu gốc của bạn

    private Map<String, JPanel> uiPanels;
    private Map<String, JButton> menuButtons;

    // 🌟 Định nghĩa danh mục chức năng chuẩn chỉnh cho Quản lý Kho Jollibee
    private static final String[] MENU_ORDER = {
         "Tổng quan kho",
         "Quản lý Nguyên liệu", // Đồng bộ chữ 'liệu' viết thường toàn hệ thống
         "Quản lý Nhập kho",
         "Quản lý Xuất kho",
         "Kiểm kê & Hạn sử dụng",
         "Quản lý Nhà cung cấp",
         "Danh mục Nhà kho",
         "Quản lý Nhân viên",
         "Báo cáo hao hụt"
    };

    /**
     * Constructor tiếp nhận ca làm việc từ LoginDialog truyền sang
     * @param user Đối tượng TaiKhoan chứa thông tin người trực ca.
     */
    public MainApplicationFrame(TaiKhoan user) {
        this.currentUser = user; 

        // Cấu hình khung JFrame tổng
        setTitle("HỆ THỐNG QUẢN LÝ KHO NGUYÊN LIỆU JOLLIBEE");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1500, 850); 
        setLocationRelativeTo(null); 
        setMinimumSize(new Dimension(1300, 750)); 

        JPanel mainContentPane = new JPanel(new BorderLayout());
        setContentPane(mainContentPane);

        // --- 1. Header Panel Thanh Tiêu Đề Trên (NORTH) ---
        JPanel headerPanel = new JPanel(new BorderLayout()); 
        headerPanel.setBackground(jollibeeRed); 
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15)); 

        // Cụm trái: Logo Ong Jollibee & Tên chi nhánh điều hành
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0)); 
        titlePanel.setBackground(jollibeeRed); 

        lblLogo = new JLabel(); 
        loadLogoFromResources("/images/logo.png"); 
        titlePanel.add(lblLogo); 

        lblAppTitle = new JLabel("<html><b><font color='white' size='+1'>JOLLIBEE - PHẠM NGỌC THẠCH</font></b><br><font color='#FFD200' size='3'>Hệ thống Kiểm soát & Quản lý Kho tổng</font></html>"); 
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        titlePanel.add(lblAppTitle); 

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Cụm phải: Tài khoản trực ca & Nút Đăng xuất ca làm việc
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5)); 
        userInfoPanel.setBackground(jollibeeRed); 

        String displayName = (currentUser != null && currentUser.getMaNV() != null) ? currentUser.getMaNV() : "ADMIN";
        String role = (currentUser != null && currentUser.getQuyen() != null) ? currentUser.getQuyen() : "Admin"; 

        lblWelcome = new JLabel("<html><font color='white'>Nhân viên: <b>" + displayName + "</b> | Bộ phận: <span style='background-color:#111; padding:2px 5px; color:#FFD200;'><b>" + role + "</b></span></font></html>"); 
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userInfoPanel.add(lblWelcome); 

        btnLogout = new JButton("Đăng xuất ca"); 
        styleButton(btnLogout, jollibeeBrown, Color.WHITE); 

        // Hiệu ứng hover mượt mà cho nút đăng xuất
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(jollibeeBrown.brighter()); 
                btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(jollibeeBrown); 
                btnLogout.setCursor(Cursor.getDefaultCursor());
            }
        });
        userInfoPanel.add(btnLogout);

        headerPanel.add(userInfoPanel, BorderLayout.EAST); 
        mainContentPane.add(headerPanel, BorderLayout.NORTH); 

        // --- 2. Content Panel Vùng Nội Dung Hiển Thị Giữa (CENTER) ---
        contentPanel = new JPanel();
        cardLayout = new CardLayout(); 
        contentPanel.setLayout(cardLayout); 
        contentPanel.setBackground(lightCream); 

        mainContentPane.add(contentPanel, BorderLayout.CENTER);

        // --- 3. Side Menu Panel Thanh Điều Hướng Trái (WEST) ---
        menuPanel = new JPanel();
        menuPanel.setBackground(darkCharcoal); 
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); 
        menuPanel.setBorder(new EmptyBorder(15, 0, 15, 0)); 

        uiPanels = new HashMap<>();
        menuButtons = new HashMap<>();

        // Khởi tạo các phân hệ và cấu hình nạp vào Layout thẻ CardLayout
        createMenuButtons(); 

        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setBorder(null);
        mainContentPane.add(menuScrollPane, BorderLayout.WEST);

        // Kiểm soát sự kiện tắt phần mềm đột ngột từ người dùng
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit(); 
            }
        });

        btnLogout.addActionListener(e -> performLogout()); 

        applyMenuPermissions(); 
        showInitialPanel();

        setVisible(true);
    }

    private void loadLogoFromResources(String resourcePath) {
        lblLogo.setIcon(null); 
        try {
            URL imgURL = MainApplicationFrame.class.getClassLoader().getResource(resourcePath.substring(1));
            if (imgURL == null) {
                imgURL = getClass().getResource(resourcePath);
            }
            if (imgURL != null) {
                ImageIcon logoIcon = new ImageIcon(imgURL);
                Image scaledImage = logoIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH); 
                lblLogo.setIcon(new ImageIcon(scaledImage));
            } else {
                lblLogo.setText("🐝"); 
                lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 28));
                lblLogo.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            lblLogo.setText("🐝");
            lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 28));
            lblLogo.setForeground(Color.WHITE);
        }
    }

    /**
     * Khởi tạo, cấp phát bộ nhớ và map liên thông dữ liệu các thực thể giao diện thật
     */
    private void createMenuButtons() {
        String userRole = (currentUser != null) ? currentUser.getQuyen() : "Admin";
        
        uiPanels.clear(); 
        contentPanel.removeAll(); 

        // Khởi tạo các panel chức năng thực tế của hệ thống
        this.nhapKhoUI = new NhapKhoUI(currentUser);
        this.NguyenLieuAll = new NguyenLieuAll(currentUser);

        uiPanels.put("Tổng quan kho", new TongQuanKhoUI());
        uiPanels.put("Quản lý Nguyên liệu", (JPanel) this.NguyenLieuAll);
        uiPanels.put("Quản lý Nhập kho", this.nhapKhoUI); 
        uiPanels.put("Quản lý Xuất kho", new XuatKhoUI(currentUser));
        uiPanels.put("Kiểm kê & Hạn sử dụng", new KiemKeUI());
        
        model.NhanVien dummyNhanVien = new model.NhanVien();
        if (currentUser != null) {
            dummyNhanVien.setMaNV(currentUser.getMaNV());
        }
        uiPanels.put("Quản lý Nhà cung cấp", new NhaCungCapUI(dummyNhanVien));
        uiPanels.put("Danh mục Nhà kho", new NhaKhoUI(currentUser));
        uiPanels.put("Quản lý Nhân viên", new NhanVienUI(currentUser));
        uiPanels.put("Báo cáo hao hụt", new BaoCaoHaoHutUI(currentUser));

        // Nạp toàn bộ map giao diện vào hệ thống quản lý CardLayout
        for (Map.Entry<String, JPanel> entry : uiPanels.entrySet()) {
            contentPanel.add(entry.getValue(), entry.getKey()); 
        }

        menuPanel.removeAll();
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        menuButtons.clear(); 

        // Tạo dựng thanh nút điều hướng dọc Jollibee bên trái
        for (String menuName : MENU_ORDER) {
            if (uiPanels.containsKey(menuName)) {
                JButton menuButton = new JButton(menuName);
                styleMenuButton(menuButton, false); 

                menuButton.addMouseListener(new MouseAdapter() {
                     @Override
                     public void mouseEntered(MouseEvent e) {
                         if (menuButton.getBackground().getRGB() != jollibeeRed.getRGB()) {
                             menuButton.setBackground(new Color(80, 80, 80)); 
                         }
                         menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
                     }

                     @Override
                     public void mouseExited(MouseEvent e) {
                         if (menuButton.getBackground().getRGB() != jollibeeRed.getRGB()) {
                            menuButton.setBackground(darkCharcoal); 
                         }
                         menuButton.setCursor(Cursor.getDefaultCursor()); 
                     }
                });

                menuButton.addActionListener(e -> {
                     if (isPanelAccessible(menuName, userRole)) {
                          cardLayout.show(contentPanel, menuName); 
                          highlightMenuButton(menuName);
                          
                          // Tự động làm mới dữ liệu lưới bảng khi nhân viên thay đổi Tab làm việc
                          if (menuName.equals("Quản lý Nhập kho")) {
                               if (nhapKhoUI != null) nhapKhoUI.refreshData();
                          } else if (menuName.equals("Quản lý Xuất kho")) {
                               XuatKhoUI p = (XuatKhoUI) uiPanels.get("Quản lý Xuất kho");
                               if (p != null) p.refreshData();
                          } else if (menuName.equals("Kiểm kê & Hạn sử dụng")) {
                               KiemKeUI p = (KiemKeUI) uiPanels.get("Kiểm kê & Hạn sử dụng");
                               if (p != null) p.refreshData();
                          } else if (menuName.equals("Tổng quan kho")) {
                               TongQuanKhoUI p = (TongQuanKhoUI) uiPanels.get("Tổng quan kho");
                               if (p != null) p.refreshData();
                          } else if (menuName.equals("Báo cáo hao hụt")) {
                               BaoCaoHaoHutUI p = (BaoCaoHaoHutUI) uiPanels.get("Báo cáo hao hụt");
                               if (p != null) p.refreshData();
                          }
                     } else {
                          JOptionPane.showMessageDialog(this, "Tài khoản của bạn không có thẩm quyền kiểm soát phân hệ này!", "Cảnh báo phân quyền", JOptionPane.WARNING_MESSAGE);
                     }
                });

                menuButton.setAlignmentX(Component.CENTER_ALIGNMENT); 
                menuPanel.add(menuButton); 

                menuButtons.put(menuName, menuButton);
            }
        }

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void styleMenuButton(JButton button, boolean isSelected) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        button.setFocusPainted(false); 
        button.setHorizontalAlignment(SwingConstants.LEFT); 
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorderPainted(true);

        if (isSelected) {
            button.setBackground(jollibeeRed); 
            button.setForeground(Color.WHITE); 
            button.setBorder(BorderFactory.createCompoundBorder(
                     BorderFactory.createMatteBorder(0, 6, 0, 0, jollibeeOrange), 
                     BorderFactory.createEmptyBorder(10, 15, 10, 15) 
            ));
        } else {
           button.setBackground(darkCharcoal); 
           button.setForeground(Color.WHITE); 
           button.setBorder(BorderFactory.createEmptyBorder(13, 21, 13, 15));
        }
    }

    private void highlightMenuButton(String selectedMenuName) {
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            JButton button = entry.getValue();
            String menuName = entry.getKey();
            styleMenuButton(button, menuName.equals(selectedMenuName));
        }
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false); 
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                         BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1), 
                         BorderFactory.createEmptyBorder(6, 14, 6, 14))); 
        button.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
    }

    private void confirmExit() {
        int option = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn ngắt kết nối và thoát hệ thống kho?", 
                    "Xác nhận đóng hệ thống", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE); 

        if (option == JOptionPane.YES_OPTION) {
            dispose(); 
            System.exit(0); 
        }
    }

    private void performLogout() {
        int option = JOptionPane.showConfirmDialog(this,
                    "Ghi nhận đóng ca làm việc hiện tại, bạn muốn đăng xuất ca trực?", 
                    "Xác nhận kết ca", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE); 

        if (option == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0); 
        }
    }

    private void applyMenuPermissions() {
        String role = (currentUser != null && currentUser.getQuyen() != null) ? currentUser.getQuyen() : "Admin";
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            String cardName = entry.getKey(); 
            JButton button = entry.getValue(); 

            boolean isPermitted = isPanelAccessible(cardName, role);
            button.setVisible(isPermitted); 
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private boolean isPanelAccessible(String panelName, String role) {
         if (role == null || role.isEmpty()) return false; 
         switch (panelName) {
             case "Tổng quan kho":
             case "Quản lý Nguyên liệu":
             case "Quản lý Nhập kho":
             case "Quản lý Xuất kho":
             case "Kiểm kê & Hạn sử dụng":
             case "Báo cáo hao hụt":
                  return true; 
             case "Quản lý Nhà cung cấp":
             case "Danh mục Nhà kho":
                  return "Admin".equalsIgnoreCase(role) || "WarehouseManager".equalsIgnoreCase(role);
             case "Quản lý Nhân viên":
                  return "Admin".equalsIgnoreCase(role);
             default:
                  return false; 
         }
    }

    private void showInitialPanel() {
         String firstPermittedMenuName = null;
         for (String menuName : MENU_ORDER) {
             if (uiPanels.containsKey(menuName) && isPanelAccessible(menuName, (currentUser != null ? currentUser.getQuyen() : "Admin"))) {
                  firstPermittedMenuName = menuName;
                  break; 
             }
         }
         if (firstPermittedMenuName != null) {
              cardLayout.show(contentPanel, firstPermittedMenuName);
              highlightMenuButton(firstPermittedMenuName);
         }
    }

    public static void main(String[] args) {
        try {
            try {
                Class<?> flat = Class.forName("com.formdev.flatlaf.FlatLightLaf");
                javax.swing.LookAndFeel laf = (javax.swing.LookAndFeel) flat.getDeclaredConstructor().newInstance();
                UIManager.setLookAndFeel(laf);
            } catch (ClassNotFoundException cnfe) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            TaiKhoan mockManager = new TaiKhoan();
            mockManager.setMaNV("NV01");
            mockManager.setQuyen("WarehouseManager"); 

            new MainApplicationFrame(mockManager);
        });
    }
}