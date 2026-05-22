package gui;

import dao.TaiKhoanDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginGUI extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public LoginGUI() {
        setTitle("Quản Lý Kho Jollibee - Đăng Nhập");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(214, 24, 34)); 

        JLabel lblTitle = new JLabel("🍗 JOLLIBEE KHO HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(25, 0, 15, 0));
        main.add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        JLabel lblU = new JLabel("Tên đăng nhập:");
        lblU.setFont(new Font("Segoe UI", Font.BOLD, 12));
        form.add(lblU, gbc);
        
        gbc.gridy = 1;
        txtUser = new JTextField(20);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 222, 229), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        form.add(txtUser, gbc);

        gbc.gridy = 2;
        JLabel lblP = new JLabel("Mật khẩu:");
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        form.add(lblP, gbc);
        
        gbc.gridy = 3;
        txtPass = new JPasswordField(20);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 222, 229), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        form.add(txtPass, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(18, 0, 0, 0);
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBackground(new Color(214, 24, 34));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        form.add(btnLogin, gbc);

        main.add(form, BorderLayout.CENTER);
        setContentPane(main);

        btnLogin.addActionListener(e -> login());
        txtPass.addActionListener(e -> login()); 
        txtUser.addActionListener(e -> txtPass.requestFocus());
    }

    private void login() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin tài khoản!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            TaiKhoanDAO dao = new TaiKhoanDAO();
            if (dao.login(user, pass)) {
                JOptionPane.showMessageDialog(this, "Đăng nhập hệ thống thành công! Xin chào, " + user, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // Khởi chạy MainGUI và truyền mã tài khoản đăng nhập thật vào phiên làm việc
                MainGUI mainSystem = new MainGUI(user);
                mainSystem.setVisible(true);
                
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi xác thực", JOptionPane.ERROR_MESSAGE);
                txtPass.setText("");
                txtPass.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối kiểm tra tài khoản: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}