package ui;

import dao.TaiKhoanDAO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.TaiKhoan;

public class LoginDialog extends JDialog {

    // Bảng màu thương hiệu Jollibee
    private final Color jollibeeRed = new Color(227, 29, 43);      // #E31D2B
    private final Color jollibeeYellow = new Color(255, 210, 0);   // #FFD200
    private final Color creamWhite = new Color(255, 253, 240);     // Nền kem nhẹ
    private final Color darkCharcoal = new Color(50, 50, 50);
    private final Color linkColor = new Color(0, 102, 204);
    private final Color hoverRed = new Color(245, 54, 68);
    private final Color hoverLink = new Color(30, 144, 255);

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private JButton btnRegister;
    private JButton btnForgotPassword;
    private JLabel lblTitle;
    private JLabel lblLoading;
    private JLabel lblLogo;
    private JLabel lblLoadingLogo;
    private JLabel lblWelcome;
    private JPanel loadingPanel;
    private JPanel mainContentPanel;
    private Timer loadingTimer;
    private JProgressBar progressBar;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private TaiKhoanDAO taiKhoanDAO;
    private TaiKhoan loggedInUser;

    public LoginDialog(Frame owner) {
        super(owner, "Đăng nhập Hệ thống - Jollibee Kho", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        taiKhoanDAO = new TaiKhoanDAO();
        
        setSize(600, 550);
        setLocationRelativeTo(owner);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 600, 550, 15, 15));
        setResizable(false);
        
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(creamWhite);
        contentPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(jollibeeRed, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.setOpaque(false);
        
        createMainPanel();
        createLoadingPanel();
        
        cardPanel.add(mainContentPanel, "main");
        cardPanel.add(loadingPanel, "loading");
        contentPane.add(cardPanel, BorderLayout.CENTER);
        
        // Nút Đóng (×) góc trên bên phải
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolBar.setOpaque(false);
        JButton btnClose = new JButton("×");
        btnClose.setForeground(jollibeeRed);
        btnClose.setFont(new Font("Arial", Font.BOLD, 22));
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> cancelLogin());
        btnClose.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnClose.setForeground(Color.BLACK); }
            public void mouseExited(MouseEvent e) { btnClose.setForeground(jollibeeRed); }
        });
        toolBar.add(btnClose);
        contentPane.add(toolBar, BorderLayout.NORTH);
        
        // Kéo thả di chuyển Form không viền
        MouseAdapter dragWindowAdapter = new MouseAdapter() {
            private Point initialClick;
            @Override public void mousePressed(MouseEvent e) { initialClick = e.getPoint(); }
            @Override public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getLocationOnScreen();
                setLocation(currentPoint.x - initialClick.x, currentPoint.y - initialClick.y);
            }
        };
        addMouseListener(dragWindowAdapter);
        addMouseMotionListener(dragWindowAdapter);
        
        // Sự kiện kích hoạt điều hướng nhanh
        btnLogin.addActionListener(e -> performLogin());
        btnCancel.addActionListener(e -> cancelLogin());
        txtPassword.addActionListener(e -> performLogin());
        txtUsername.addActionListener(e -> txtPassword.requestFocusInWindow());
        
        getContentPane().add(contentPane);
    }
    
    private void createMainPanel() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setOpaque(false);
        
        // 1. Khối chứa Logo (Nạp ảnh thực tế từ thư mục images)
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        lblLogo = new JLabel();
        
        try {
            java.net.URL imgURL = LoginDialog.class.getClassLoader().getResource("images/logo.png");
            if (imgURL != null) {
                ImageIcon logoIcon = new ImageIcon(imgURL);
                Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(scaledImage));
            } else {
                java.net.URL backupURL = getClass().getResource("/images/logo.png");
                if (backupURL != null) {
                    ImageIcon logoIcon = new ImageIcon(backupURL);
                    Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblLogo.setIcon(new ImageIcon(scaledImage));
                } else {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            lblLogo.setText("JOLLIBEE");
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 36));
            lblLogo.setForeground(jollibeeRed);
        }
        logoPanel.add(lblLogo);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContentPanel.add(logoPanel);
        mainContentPanel.add(Box.createVerticalStrut(15));
        
        // 2. Tiêu đề dự án
        lblTitle = new JLabel("QUẢN LÝ KHO NGUYÊN LIỆU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(jollibeeRed);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContentPanel.add(lblTitle);
        mainContentPanel.add(Box.createVerticalStrut(25));
        
        // 3. Form nhập liệu thông tin đăng nhập
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUsername.setForeground(darkCharcoal);
        formPanel.add(lblUsername, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUsername.setPreferredSize(new Dimension(250, 35));
        formPanel.add(txtUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPassword.setForeground(darkCharcoal);
        formPanel.add(lblPassword, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setPreferredSize(new Dimension(250, 35));
        formPanel.add(txtPassword, gbc);
        
        mainContentPanel.add(formPanel);
        mainContentPanel.add(Box.createVerticalStrut(25));
        
        // 4. Panel chứa bộ đôi Nút lệnh Đăng nhập / Hủy
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBackground(jollibeeRed);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setOpaque(true);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(jollibeeYellow, 1),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(hoverRed); }
            public void mouseExited(MouseEvent e) { btnLogin.setBackground(jollibeeRed); }
        });
        
        btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancel.setBackground(darkCharcoal);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setOpaque(true);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnCancel.setBackground(new Color(75, 75, 75)); }
            public void mouseExited(MouseEvent e) { btnCancel.setBackground(darkCharcoal); }
        });
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(Box.createHorizontalStrut(25));
        buttonPanel.add(btnCancel);
        
        mainContentPanel.add(buttonPanel);
        mainContentPanel.add(Box.createVerticalStrut(20));
        
        // 5. Liên kết mở rộng
        JPanel linksPanel = new JPanel();
        linksPanel.setOpaque(false);
        linksPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnRegister = new JButton("Đăng ký phân quyền");
        btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRegister.setForeground(linkColor);
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnForgotPassword = new JButton("Quên mật khẩu?");
        btnForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnForgotPassword.setForeground(linkColor);
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setFocusPainted(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        linksPanel.add(btnRegister);
        linksPanel.add(Box.createHorizontalStrut(30));
        linksPanel.add(btnForgotPassword);
        
        mainContentPanel.add(linksPanel);
    }
    
    private void createLoadingPanel() {
        loadingPanel = new JPanel();
        loadingPanel.setLayout(new BoxLayout(loadingPanel, BoxLayout.Y_AXIS));
        loadingPanel.setOpaque(false);
        
        lblLoadingLogo = new JLabel();
        try {
            java.net.URL imgURL = LoginDialog.class.getClassLoader().getResource("images/logo.png");
            if (imgURL != null) {
                ImageIcon logoIcon = new ImageIcon(imgURL);
                Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblLoadingLogo.setIcon(new ImageIcon(scaledImage));
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            lblLoadingLogo.setText("JOLLIBEE SYSTEM");
            lblLoadingLogo.setFont(new Font("SansSerif", Font.BOLD, 30));
            lblLoadingLogo.setForeground(jollibeeRed);
        }
        lblLoadingLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingPanel.add(Box.createVerticalStrut(30));
        loadingPanel.add(lblLoadingLogo);
        loadingPanel.add(Box.createVerticalStrut(30));
        
        lblLoading = new JLabel("Đang kết nối kho nguyên liệu...");
        lblLoading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLoading.setForeground(jollibeeRed);
        lblLoading.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingPanel.add(lblLoading);
        loadingPanel.add(Box.createVerticalStrut(20));
        
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(jollibeeRed);
        progressBar.setPreferredSize(new Dimension(250, 8));
        progressBar.setMaximumSize(new Dimension(250, 8));
        progressBar.setBackground(new Color(235, 235, 210));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingPanel.add(progressBar);
        loadingPanel.add(Box.createVerticalStrut(40));
        
        lblWelcome = new JLabel("");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcome.setForeground(darkCharcoal);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingPanel.add(lblWelcome);
    }
    
    private void performLogin() {
        String username = txtUsername.getText().trim();
        char[] passwordChars = txtPassword.getPassword();
        String password = new String(passwordChars);

        if (username.isEmpty() || passwordChars.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin tài khoản quản trị.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            clearPasswordField(passwordChars);
            return;
        }

        // Kiểm tra tài khoản bằng lớp DAO kết nối SQL Server
        loggedInUser = taiKhoanDAO.kiemTraDangNhap (username, password);

        if (loggedInUser != null) {
            cardLayout.show(cardPanel, "loading");
            
            String roleName = loggedInUser.getQuyen(); // Lấy trực tiếp quyền từ database
            lblWelcome.setText("Jollibee Team xin chào " + roleName + " (" + loggedInUser.getTenDangNhap() + ")");

            String[] loadingStates = {"Đang đăng nhập.", "Đang đăng nhập..", "Đang đăng nhập..."};
            final int[] currentState = {0};
            Timer animationTimer = new Timer(90, e -> {
                lblLoading.setText(loadingStates[currentState[0] % loadingStates.length]);
                currentState[0]++;
            });
            animationTimer.start();

            loadingTimer = new Timer(1200, e -> {
                animationTimer.stop();
                dispose(); // Đóng form đăng nhập hiện tại
                
                // Mở và truyền thực thể dữ liệu sang trang chủ
                SwingUtilities.invokeLater(() -> {
                    MainApplicationFrame mainFrame = new MainApplicationFrame(loggedInUser);
                    mainFrame.setVisible(true);
                });
                clearPasswordField(passwordChars);
            });
            loadingTimer.setRepeats(false);
            loadingTimer.start();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Tên đăng nhập hoặc Mật khẩu không chính xác.",
                    "Lỗi xác thực", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsername.requestFocusInWindow();
            clearPasswordField(passwordChars);
        }
    }
    
    private void cancelLogin() {
        loggedInUser = null;
        System.exit(0);
    }

    private void clearPasswordField(char[] field) {
        Arrays.fill(field, ' ');
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            LoginDialog dialog = new LoginDialog(frame);
            dialog.setVisible(true);
        });
    }
}