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
        setTitle(" Báo Cáo Tồn Kho Jollibee - Phạm Ngọc Thạch ");
        setSize(1350, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

        // =====================================================
        // MAIN LAYOUT
        // =====================================================
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
        } catch (Exception e) {
            tabs.addTab("📦 Nguyên Liệu", buildErrorPanel(new Exception("Lớp gui.NguyenLieuPanel đang bị lỗi hoặc thiếu file!")));
        }

        // 2. Tab Nhà Cung Cấp
        try {
            Class<?> panelClass = Class.forName("gui.NhaCungCapPanel");
            JPanel panelInstance = (JPanel) panelClass.getDeclaredConstructor().newInstance();
            tabs.addTab("🚚 Nhà Cung Cấp", panelInstance);
        } catch (Exception e) {
            tabs.addTab("🚚 Nhà Cung Cấp", buildErrorPanel(new Exception("Lớp gui.NhaCungCapPanel đang bị lỗi hoặc thiếu file!")));
        }

        // 3. Tab Nhân Viên
        try {
            Class<?> panelClass = Class.forName("gui.NhanVienPanel");
            JPanel panelInstance = (JPanel) panelClass.getDeclaredConstructor().newInstance();
            tabs.addTab("👨‍🍳 Nhân Viên", panelInstance);
        } catch (Exception e) {
            tabs.addTab("👨‍🍳 Nhân Viên", buildErrorPanel(new Exception("Lớp gui.NhanVienPanel đang bị lỗi hoặc thiếu file!")));
        }

        // 4. Tab Phiếu Nhập
        try {
            Class<?> panelClass = Class.forName("gui.PhieuNhapPanel");
            JPanel panelInstance = (JPanel) panelClass.getDeclaredConstructor(String.class).newInstance(currentUser);
            tabs.addTab("📥 Phiếu Nhập", panelInstance);
        } catch (Exception e) {
            tabs.addTab("📥 Phiếu Nhập", buildErrorPanel(new Exception("Lớp gui.PhieuNhapPanel đang bị lỗi hoặc thiếu file!")));
        }

        // =====================================================
        // LOAD 2 MODULE ĐÃ HOÀN THIỆN CỦA BẠN (TƯƠNG TÁC THỰC TẾ)
        // =====================================================

        // 5. Tab Phiếu Xuất Kho Jollibee
        try {
            PhieuXuatPanel pxPanel = new PhieuXuatPanel(currentUser);
            tabs.addTab("📤 Phiếu Xuất", pxPanel);
        } catch (Exception ex) {
            ex.printStackTrace();
            tabs.addTab("📤 Phiếu Xuất", buildErrorPanel(ex));
        }

        // 6. Tab Báo Cáo Tồn Kho Thực Tế Theo Lô
        try {
            TonKhoPanel tkPanel = new TonKhoPanel();
            tabs.addTab("📊 Báo Cáo Tồn Kho", tkPanel);
        } catch (Exception ex) {
            ex.printStackTrace();
            tabs.addTab("📊 Báo Cáo Tồn Kho", buildErrorPanel(ex));
        }

        // =====================================================
        // ADD COMPONENT TO FRAME
        // =====================================================
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // =========================================================
    // HEADER UI
    // =========================================================
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(214, 24, 34));
        header.setBorder(new EmptyBorder(10, 18, 10, 18));

        JLabel lblTitle = new JLabel("🍗 Jollibee Warehouse Management");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblUser = new JLabel("Xin chào: " + currentUser);
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(new Color(214, 24, 34));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn đăng xuất?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                try {
                    Class<?> loginClass = Class.forName("gui.LoginGUI");
                    JFrame loginFrame = (JFrame) loginClass.getDeclaredConstructor().newInstance();
                    loginFrame.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Không thể quay lại màn hình Đăng nhập do LoginGUI bị lỗi!");
                }
            }
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(lblUser);
        rightPanel.add(btnLogout);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    // =========================================================
    // PANEL HIỂN THỊ LỖI MODULE CHÉO
    // =========================================================
    private JPanel buildErrorPanel(Exception ex) {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setForeground(Color.RED);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setText(
                "Không thể khởi tạo module.\n\n" + ex.getMessage()
        );
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // MAIN RUN - KHỞI CHẠY HỆ THỐNG TỔNG HỢP JOLLIBEE
    // =========================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {}

            // Thực hiện chạy kiểm thử tổng hợp với tài khoản mặc định "NV01" có trong DB của bạn
            new MainGUI("NV01").setVisible(true);
        });
    }
}