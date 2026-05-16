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
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Panel chính
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(220, 50, 50));
        main.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Header
        JLabel lblTitle = new JLabel("JOLLIBEE KHO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(25, 0, 15, 0));
        main.add(lblTitle, BorderLayout.NORTH);

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        // Tên đăng nhập
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        form.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridy = 1;
        txtUser = new JTextField(20);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(txtUser, gbc);

        // Mật khẩu
        gbc.gridy = 2;
        form.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridy = 3;
        txtPass = new JPasswordField(20);
        txtPass.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(txtPass, gbc);

        // Nút đăng nhập
        gbc.gridy = 4;
        gbc.insets = new Insets(16, 0, 0, 0);
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBackground(new Color(220, 50, 50));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        form.add(btnLogin, gbc);

        main.add(form, BorderLayout.CENTER);
        setContentPane(main);

        // Sự kiện
        btnLogin.addActionListener(e -> login());
        txtPass.addActionListener(e -> login()); // Enter trong ô mật khẩu
        txtUser.addActionListener(e -> txtPass.requestFocus());
    }

    private void login() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoanDAO dao = new TaiKhoanDAO();
        if (dao.login(user, pass)) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Xin chào, " + user, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            new MainGUI(user).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtPass.setText("");
            txtPass.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}