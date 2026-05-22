package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainGUI extends JFrame {

    private String currentUser;
    private JTabbedPane tabs;

    public MainGUI(String currentUser) {
        this.currentUser = currentUser;
        initUI();
    }

    private void initUI() {
        // =====================================================
        // FRAME CONFIG
        // =====================================================
        setTitle("Hệ Thống Quản Lý Kho Jollibee - Cơ Sở Phạm Ngọc Thạch");
        setSize(1350, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

        setLayout(new BorderLayout());

        // =====================================================
        // HEADER
        // =====================================================
        JPanel header = buildHeader();

        // =====================================================
        // TABS WINDOW
        // =====================================================
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // =====================================================
        // LOAD CÁC PANEL MODULE CỦA THÀNH VIÊN KHÁC (Bọc an toàn)
        // =====================================================
        
        // 1. Tab Nguyên Liệu
        try {
            Class<?> panelClass = Class.forName("gui.NguyenLieuPanel");
            JPanel panelInstance = (JPanel) panelClass.getDeclaredConstructor().newInstance();
            tabs.addTab("📦 Nguyên Liệu", panelInstance);
        } catch (Throwable e) {
            tabs.addTab("📦 Nguyên Liệu", buildErrorPanel(new Exception("Module Nguyên Liệu chưa được tích hợp vào hệ thống.")));
        }

        // 2. Tab Nhà Cung Cấp
        try {
            Class<?> panelClass = Class.forName("gui.NhaCungCapPanel");
            JPanel panelInstance = (JPanel) panelClass.getDeclaredConstructor().newInstance();
            tabs.addTab("🚚 Nhà Cung Cấp", panelInstance);
        } catch (Throwable e) {
            tabs.addTab("🚚 Nhà Cung Cấp", buildErrorPanel(new Exception("Module Nhà Cung Cấp chưa được tích hợp vào hệ thống.")));
        }

        // 3. Tab Nhân Viên
        try {
            Class<?> panelClass = Class.forName("gui.NhanVienPanel");
            JPanel panelInstance = (JPanel) panelClass.getDeclaredConstructor().newInstance();
            tabs.addTab("👨‍🍳 Nhân Viên", panelInstance);
        } catch (Throwable e) {
            tabs.addTab("👨‍🍳 Nhân Viên", buildErrorPanel(new Exception("Module Nhân Viên chưa được tích hợp vào hệ thống.")));
        }

        // 4. Tab Phiếu Nhập
        try {
            Class<?> panelClass = Class.forName("gui.PhieuNhapPanel");
            JPanel panelInstance = (JPanel) panelClass.getDeclaredConstructor(String.class).newInstance(currentUser);
            tabs.addTab("📥 Phiếu Nhập", panelInstance);
        } catch (Throwable e) {
            tabs.addTab("📥 Phiếu Nhập", buildErrorPanel(new Exception("Module Phiếu Nhập chưa được tích hợp vào hệ thống.")));
        }

        // =====================================================
        // GỌI TRỰC TIẾP 2 MODULE HOÀN THIỆN CỦA BẠN (AN TOÀN TUYỆT ĐỐI)
        // =====================================================
        try {
            PhieuXuatPanel pxPanel = new PhieuXuatPanel(currentUser);
            tabs.addTab("📤 Phiếu Xuất", pxPanel);
        } catch (Throwable ex) {
            tabs.addTab("📤 Phiếu Xuất", buildErrorPanel(new Exception(ex.getMessage())));
        }

        try {
            TonKhoPanel tkPanel = new TonKhoPanel();
            tabs.addTab("📊 Báo Cáo Tồn Kho", tkPanel);
        } catch (Throwable ex) {
            tabs.addTab("📊 Báo Cáo Tồn Kho", buildErrorPanel(new Exception(ex.getMessage())));
        }

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(214, 24, 34)); 
        header.setBorder(new EmptyBorder(10, 18, 10, 18));

        JLabel lblTitle = new JLabel("🍗 Jollibee Warehouse Management");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // Cập nhật động: Tài khoản nhân viên nào đăng nhập thì hiển thị mã của nhân viên đó
        JLabel lblUser = new JLabel("Mã Nhân Viên: " + currentUser);
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(new Color(214, 24, 34));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn đăng xuất hệ thống?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginGUI().setVisible(true); 
            }
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(lblUser);
        rightPanel.add(btnLogout);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel buildErrorPanel(Exception ex) {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setForeground(new Color(150, 150, 150));
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        area.setText("💡 Trạng thái Module:\n\n" + ex.getMessage() + "\n\n(Bạn vẫn có thể thao tác bình thường trên các Tab chức năng khác)");
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI("NV01").setVisible(true);
        });
    }
}