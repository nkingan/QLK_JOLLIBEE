package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
<<<<<<< HEAD
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
=======
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
>>>>>>> origin/van

import dao.NhanVienDAO;
import model.NhanVien;
import model.TaiKhoan;

public class NhanVienUI extends JPanel {

<<<<<<< HEAD
    private final Color jollibeeRed = new Color(227, 29, 43);
    private final Color creamWhite = new Color(255, 253, 240);
    private final Color darkGray = new Color(50, 50, 50);
    private final Color tableHeaderBg = new Color(139, 69, 19);

    private JTextField txtMaNV, txtTenNV, txtSDT, txtEmail, txtNgaySinh, txtChucVu;
    private JComboBox<String> cbGioiTinh;
    
    // Linked Account Components
    private JCheckBox chkCapTaiKhoan;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbQuyen;
    
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable nhanVienTable;
    private DefaultTableModel tableModel;

    private NhanVienDAO nhanVienDAO;
    private NhanVien currentNhanVien;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public NhanVienUI() {
=======
    private static final Color JOLLIBEE_RED = new Color(0xE31837);
    private static final Color JOLLIBEE_YELLOW = new Color(0xFFC72C);
    private static final Color CREAM_WHITE = new Color(255, 253, 240);
    private static final Color DARK_CHARCOAL = new Color(45, 45, 45);
    private static final Color ROW_HOVER = new Color(255, 242, 204);
    private static final Color ROW_ALT = new Color(250, 248, 244);

    private JTextField txtMaNV, txtTenNV, txtSDT, txtEmail, txtNgaySinh, txtGioiTinh, txtChucVu;
    private int hoveredRow = -1;
    
    // Account details sub-form fields
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cbQuyen;
    private JCheckBox chkCapTaiKhoan;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSaveAccount;
    
    private JTable tableNV;
    private DefaultTableModel tableModel;
    
    private NhanVienDAO nhanVienDAO;
    private NhanVien currentNhanVien;
    private TaiKhoan currentUser;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public NhanVienUI(TaiKhoan user) {
        this.currentUser = user;
>>>>>>> origin/van
        this.nhanVienDAO = new NhanVienDAO();
        initComponents();
        loadNhanVienTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
<<<<<<< HEAD
        setBackground(creamWhite);

        // Title
        JLabel lblTitle = new JLabel("QUẢN LÝ THÀNH VIÊN BAN ĐIỀU HÀNH KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(tableHeaderBg);
        add(lblTitle, BorderLayout.NORTH);

        // Center Table Panel
        String[] columnNames = {"Mã NV", "Họ tên", "Giới tính", "Ngày sinh", "Số ĐT", "Email", "Chức vụ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        nhanVienTable = new JTable(tableModel);
        nhanVienTable.setRowHeight(30);
        nhanVienTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = nhanVienTable.getTableHeader();
        header.setBackground(tableHeaderBg);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        nhanVienTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        nhanVienTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        nhanVienTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        nhanVienTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(nhanVienTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // East Form Panel
        add(createDetailPanel(), BorderLayout.EAST);

        // Selection Listener
        nhanVienTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && nhanVienTable.getSelectedRow() != -1) {
                displayNhanVienDetails(nhanVienTable.getSelectedRow());
=======
        setBackground(CREAM_WHITE);

        // --- TITLE ---
        JLabel lblTitle = new JLabel("DANH MỤC THỦ KHO & QUẢN TRỊ TÀI KHOẢN HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(JOLLIBEE_RED);
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: JTABLE ---
        String[] columns = {"Mã NV", "Họ & Tên", "Số Điện Thoại", "Email", "Ngày Sinh", "Giới Tính", "Chức Vụ", "Tài Khoản HT"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableNV = new JTable(tableModel);
        tableNV.setRowHeight(36);
        tableNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableNV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNV.setFillsViewportHeight(true);
        tableNV.setIntercellSpacing(new Dimension(0, 0));
        tableNV.setShowGrid(false);
        tableNV.setBackground(Color.WHITE);
        tableNV.setForeground(Color.DARK_GRAY);
        tableNV.setSelectionBackground(new Color(255, 225, 205));
        tableNV.setSelectionForeground(Color.BLACK);
        tableNV.setRowMargin(4);
        tableNV.setAutoCreateRowSorter(true);
        tableNV.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tableNV.getTableHeader();
        header.setPreferredSize(new Dimension(0, 38));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(JOLLIBEE_RED);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        });

        DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    setBackground(new Color(255, 225, 205));
                    setForeground(Color.BLACK);
                } else if (row == hoveredRow) {
                    setBackground(ROW_HOVER);
                    setForeground(Color.DARK_GRAY);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT);
                    setForeground(Color.DARK_GRAY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
        tableNV.setDefaultRenderer(Object.class, rowRenderer);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    setBackground(new Color(255, 225, 205));
                    setForeground(Color.BLACK);
                } else if (row == hoveredRow) {
                    setBackground(ROW_HOVER);
                    setForeground(Color.DARK_GRAY);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT);
                    setForeground(Color.DARK_GRAY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        };
        tableNV.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(7).setCellRenderer(centerRender);

        tableNV.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = tableNV.rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    tableNV.repaint();
                }
            }
        });
        tableNV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                tableNV.repaint();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableNV);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(JOLLIBEE_RED, 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- EAST: FORM INTEGRATION ---
        add(createDetailPanel(), BorderLayout.EAST);

        // Selection Listener
        tableNV.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableNV.getSelectedRow() != -1) {
                displayDetails(tableNV.convertRowIndexToModel(tableNV.getSelectedRow()));
>>>>>>> origin/van
            }
        });
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
<<<<<<< HEAD
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setOpaque(false);

        // Form fields container
        JPanel pnlScrollContainer = new JPanel();
        pnlScrollContainer.setLayout(new BoxLayout(pnlScrollContainer, BoxLayout.Y_AXIS));
        pnlScrollContainer.setOpaque(false);

        JPanel formInfo = new JPanel(new GridBagLayout());
        formInfo.setOpaque(false);
        
        TitledBorder formBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(jollibeeRed, 1), "Thông tin cá nhân"
        );
        formBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        formBorder.setTitleColor(jollibeeRed);
        formInfo.setBorder(formBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
=======
        panel.setPreferredSize(new Dimension(390, 0));
        panel.setOpaque(false);

        // Sub-Form 1: Employee CRUD
        JPanel formEmp = new JPanel(new GridBagLayout());
        formEmp.setBackground(Color.WHITE);
        TitledBorder borderEmp = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(JOLLIBEE_RED, 2), "Thông tin Cá nhân Nhân viên"
        );
        borderEmp.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        borderEmp.setTitleColor(JOLLIBEE_RED);
        formEmp.setBorder(borderEmp);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
>>>>>>> origin/van
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
<<<<<<< HEAD
        txtMaNV.setBackground(new Color(230, 230, 230));
=======
        txtMaNV.setBackground(new Color(235, 235, 230));

>>>>>>> origin/van
        txtTenNV = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();
        txtNgaySinh = new JTextField();
<<<<<<< HEAD
        txtChucVu = new JTextField();
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGioiTinh.setBackground(Color.WHITE);

        Dimension fieldSize = new Dimension(180, 28);
        txtMaNV.setPreferredSize(fieldSize);
        txtTenNV.setPreferredSize(fieldSize);
        txtSDT.setPreferredSize(fieldSize);
        txtEmail.setPreferredSize(fieldSize);
        txtNgaySinh.setPreferredSize(fieldSize);
        txtChucVu.setPreferredSize(fieldSize);
        cbGioiTinh.setPreferredSize(fieldSize);

        addField(formInfo, "Mã NV:", txtMaNV, 0, gbc);
        addField(formInfo, "Họ và tên:", txtTenNV, 1, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; formInfo.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1; formInfo.add(cbGioiTinh, gbc);

        addField(formInfo, "Ngày sinh:", txtNgaySinh, 3, gbc);
        addField(formInfo, "Số ĐT:", txtSDT, 4, gbc);
        addField(formInfo, "Email:", txtEmail, 5, gbc);
        addField(formInfo, "Chức vụ:", txtChucVu, 6, gbc);

        pnlScrollContainer.add(formInfo);
        pnlScrollContainer.add(Box.createVerticalStrut(10));

        // Form account linking
        JPanel formAccount = new JPanel(new GridBagLayout());
        formAccount.setOpaque(false);
        
        TitledBorder accountBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(jollibeeRed, 1), "Tài khoản đăng nhập"
        );
        accountBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        accountBorder.setTitleColor(jollibeeRed);
        formAccount.setBorder(accountBorder);

        chkCapTaiKhoan = new JCheckBox("Cấp tài khoản đăng nhập");
        chkCapTaiKhoan.setOpaque(false);
        chkCapTaiKhoan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkCapTaiKhoan.setForeground(jollibeeRed);

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cbQuyen = new JComboBox<>(new String[]{"Nhân Viên", "Quản lý", "Admin"});
        cbQuyen.setBackground(Color.WHITE);

        txtUsername.setPreferredSize(fieldSize);
        txtPassword.setPreferredSize(fieldSize);
        cbQuyen.setPreferredSize(fieldSize);

        // Grid positions
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formAccount.add(chkCapTaiKhoan, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; formAccount.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; formAccount.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formAccount.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; formAccount.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formAccount.add(new JLabel("Quyền hạn:"), gbc);
        gbc.gridx = 1; formAccount.add(cbQuyen, gbc);

        pnlScrollContainer.add(formAccount);
        
        JScrollPane scrollFields = new JScrollPane(pnlScrollContainer);
        scrollFields.setBorder(null);
        scrollFields.setOpaque(false);
        scrollFields.getViewport().setOpaque(false);
        panel.add(scrollFields, BorderLayout.CENTER);

        // Actions Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
        btnPanel.setOpaque(false);
        
        btnAdd = new JButtonCustom("Thêm", new Color(40, 167, 69), Color.WHITE);
        btnUpdate = new JButtonCustom("Cập nhật", new Color(0, 123, 255), Color.WHITE);
        btnDelete = new JButtonCustom("Xóa", jollibeeRed, Color.WHITE);
        btnClear = new JButtonCustom("Làm mới", darkGray, Color.WHITE);

        Dimension btnSize = new Dimension(82, 35);
        btnAdd.setPreferredSize(btnSize); 
        btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize); 
        btnClear.setPreferredSize(btnSize);


=======
        txtGioiTinh = new JTextField();
        txtChucVu = new JTextField();

        Dimension tfSize = new Dimension(220, 26);
        txtMaNV.setPreferredSize(tfSize);
        txtTenNV.setPreferredSize(tfSize);
        txtSDT.setPreferredSize(tfSize);
        txtEmail.setPreferredSize(tfSize);
        txtNgaySinh.setPreferredSize(tfSize);
        txtGioiTinh.setPreferredSize(tfSize);
        txtChucVu.setPreferredSize(tfSize);

        addField(formEmp, "Mã nhân viên:", txtMaNV, 0, gbc);
        addField(formEmp, "Họ và tên:", txtTenNV, 1, gbc);
        addField(formEmp, "Số điện thoại:", txtSDT, 2, gbc);
        addField(formEmp, "Email:", txtEmail, 3, gbc);
        addField(formEmp, "Ngày sinh (dd/MM/yyyy):", txtNgaySinh, 4, gbc);
        addField(formEmp, "Giới tính:", txtGioiTinh, 5, gbc);
        addField(formEmp, "Chức vụ:", txtChucVu, 6, gbc);

        // Sub-Form 2: Account Linking
        JPanel formAcc = new JPanel(new GridBagLayout());
        formAcc.setBackground(Color.WHITE);
        TitledBorder borderAcc = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(242, 142, 43), 2), "Liên kết tài khoản truy cập hệ thống"
        );
        borderAcc.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        borderAcc.setTitleColor(new Color(242, 142, 43));
        formAcc.setBorder(borderAcc);

        chkCapTaiKhoan = new JCheckBox("Cấp quyền tài khoản đăng nhập");
        chkCapTaiKhoan.setFont(new Font("Segoe UI", Font.BOLD, 11));
        chkCapTaiKhoan.setOpaque(false);
        
        txtTenDangNhap = new JTextField();
        txtMatKhau = new JPasswordField();
        cbQuyen = new JComboBox<>(new String[]{"Nhân viên", "WarehouseManager", "Admin"});

        txtTenDangNhap.setPreferredSize(tfSize);
        txtMatKhau.setPreferredSize(tfSize);
        cbQuyen.setPreferredSize(tfSize);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formAcc.add(chkCapTaiKhoan, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        formAcc.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        formAcc.add(txtTenDangNhap, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formAcc.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        formAcc.add(txtMatKhau, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formAcc.add(new JLabel("Quyền hạn:"), gbc);
        gbc.gridx = 1;
        formAcc.add(cbQuyen, gbc);

        btnSaveAccount = new JButton("💾 Lưu Thông Tin Tài Khoản");
        btnSaveAccount.setBackground(new Color(242, 142, 43));
        btnSaveAccount.setForeground(Color.WHITE);
        btnSaveAccount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSaveAccount.setFocusPainted(false);
        btnSaveAccount.setBorderPainted(false);
        btnSaveAccount.putClientProperty("JButton.buttonType", "roundRect");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        formAcc.add(btnSaveAccount, gbc);

        // CRUD Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 5));
        btnPanel.setOpaque(false);

        btnAdd = new JButton("Thêm NV");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa NV");
        btnClear = new JButton("Làm mới");

        styleButton(btnAdd, new Color(40, 167, 69));
        styleButton(btnUpdate, JOLLIBEE_YELLOW.darker());
        styleButton(btnDelete, JOLLIBEE_RED);
        styleButton(btnClear, DARK_CHARCOAL);

        Dimension btnSize = new Dimension(84, 34);
        btnAdd.setPreferredSize(btnSize);
        btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);
        btnClear.setPreferredSize(btnSize);

>>>>>>> origin/van
        btnAdd.addActionListener(e -> addNhanVien());
        btnUpdate.addActionListener(e -> updateNhanVien());
        btnDelete.addActionListener(e -> deleteNhanVien());
        btnClear.addActionListener(e -> clearForm());
<<<<<<< HEAD
        
        // Disable account fields when checkbox is off
        chkCapTaiKhoan.addActionListener(e -> toggleAccountFields(chkCapTaiKhoan.isSelected()));
        toggleAccountFields(false);

        btnPanel.add(btnAdd); 
        btnPanel.add(btnUpdate); 
        btnPanel.add(btnDelete); 
        btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

=======
        btnSaveAccount.addActionListener(e -> saveAccountDetails());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        // Assembly
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);
        formContainer.add(formEmp);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(formAcc);

        panel.add(formContainer, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Handle checkbox toggles
        chkCapTaiKhoan.addActionListener(e -> toggleAccountFields(chkCapTaiKhoan.isSelected()));
        toggleAccountFields(false);

>>>>>>> origin/van
        return panel;
    }

    private void addField(JPanel p, String label, JTextField field, int row, GridBagConstraints gbc) {
<<<<<<< HEAD
        gbc.gridx = 0; gbc.gridy = row; p.add(new JLabel(label), gbc);
        gbc.gridx = 1; p.add(field, gbc);
    }

    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg); 
        b.setForeground(Color.WHITE); 
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void toggleAccountFields(boolean enabled) {
        txtUsername.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
        cbQuyen.setEnabled(enabled);
        if (!enabled) {
            txtUsername.setText("");
            txtPassword.setText("");
            cbQuyen.setSelectedIndex(0);
        }
=======
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(DARK_CHARCOAL);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.anchor = GridBagConstraints.WEST;
        p.add(field, gbc);
    }

    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void toggleAccountFields(boolean enabled) {
        txtTenDangNhap.setEnabled(enabled);
        txtMatKhau.setEnabled(enabled);
        cbQuyen.setEnabled(enabled);
        btnSaveAccount.setEnabled(enabled);
>>>>>>> origin/van
    }

    private void loadNhanVienTable() {
        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        if (list != null) {
            for (NhanVien nv : list) {
<<<<<<< HEAD
                String birthStr = nv.getNgaySinh() != null ? sdf.format(nv.getNgaySinh()) : "";
                tableModel.addRow(new Object[]{
                        nv.getMaNV(),
                        nv.getTenNV(),
                        nv.getGioiTinh(),
                        birthStr,
                        nv.getSdt(),
                        nv.getEmail(),
                        nv.getChucVu()
                });
            }
        }
        clearForm();
    }

    private void displayNhanVienDetails(int row) {
        String ma = (String) tableModel.getValueAt(row, 0);
        currentNhanVien = nhanVienDAO.getNhanVienById(ma);
        if (currentNhanVien != null) {
            txtMaNV.setText(currentNhanVien.getMaNV());
            txtTenNV.setText(currentNhanVien.getTenNV());
            
            cbGioiTinh.setSelectedItem(currentNhanVien.getGioiTinh() != null ? currentNhanVien.getGioiTinh() : "Nam");
            txtNgaySinh.setText(currentNhanVien.getNgaySinh() != null ? sdf.format(currentNhanVien.getNgaySinh()) : "");
            txtSDT.setText(currentNhanVien.getSdt());
            txtEmail.setText(currentNhanVien.getEmail());
            txtChucVu.setText(currentNhanVien.getChucVu());

            // Check linked account
            TaiKhoan tk = nhanVienDAO.getTaiKhoanByMaNV(ma);
            if (tk != null) {
                chkCapTaiKhoan.setSelected(true);
                toggleAccountFields(true);
                txtUsername.setText(tk.getTenDangNhap());
                txtPassword.setText(tk.getMatKhau());
=======
                TaiKhoan tk = nhanVienDAO.getTaiKhoanByMaNV(nv.getMaNV());
                String accStr = tk != null ? tk.getTenDangNhap() + " [" + tk.getQuyen() + "]" : "Không có";
                String birthStr = nv.getNgaySinh() != null ? sdf.format(nv.getNgaySinh()) : "";

                tableModel.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getSdt(),
                    nv.getEmail(),
                    birthStr,
                    nv.getGioiTinh(),
                    nv.getChucVu(),
                    accStr
                });
            }
        }
        // Tự động giãn cột bảng nhân viên
        util.UIHelper.autoResizeColumnWidths(tableNV);
        clearForm();
    }

    private void displayDetails(int row) {
        String maNV = (String) tableModel.getValueAt(row, 0);
        currentNhanVien = nhanVienDAO.getNhanVienById(maNV);
        if (currentNhanVien != null) {
            txtMaNV.setText(currentNhanVien.getMaNV());
            txtTenNV.setText(currentNhanVien.getTenNV());
            txtSDT.setText(currentNhanVien.getSdt());
            txtEmail.setText(currentNhanVien.getEmail());
            txtNgaySinh.setText(currentNhanVien.getNgaySinh() != null ? sdf.format(currentNhanVien.getNgaySinh()) : "");
            txtGioiTinh.setText(currentNhanVien.getGioiTinh());
            txtChucVu.setText(currentNhanVien.getChucVu());

            // Load Tai Khoan link
            TaiKhoan tk = nhanVienDAO.getTaiKhoanByMaNV(maNV);
            if (tk != null) {
                chkCapTaiKhoan.setSelected(true);
                toggleAccountFields(true);
                txtTenDangNhap.setText(tk.getTenDangNhap());
                txtMatKhau.setText(tk.getMatKhau());
>>>>>>> origin/van
                cbQuyen.setSelectedItem(tk.getQuyen());
            } else {
                chkCapTaiKhoan.setSelected(false);
                toggleAccountFields(false);
<<<<<<< HEAD
            }
            
            updateButtonState();
=======
                txtTenDangNhap.setText("");
                txtMatKhau.setText("");
                cbQuyen.setSelectedIndex(0);
            }

            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
>>>>>>> origin/van
        }
    }

    private void clearForm() {
        txtMaNV.setText(nhanVienDAO.generateNextMaNV());
        txtTenNV.setText("");
<<<<<<< HEAD
        cbGioiTinh.setSelectedIndex(0);
        txtNgaySinh.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
=======
        txtSDT.setText("");
        txtEmail.setText("");
        txtNgaySinh.setText("");
        txtGioiTinh.setText("");
>>>>>>> origin/van
        txtChucVu.setText("");

        chkCapTaiKhoan.setSelected(false);
        toggleAccountFields(false);
<<<<<<< HEAD

        currentNhanVien = null;
        nhanVienTable.clearSelection();
        updateButtonState();
    }

    private void updateButtonState() {
        boolean selected = nhanVienTable.getSelectedRow() != -1;
        btnAdd.setEnabled(!selected);
        btnUpdate.setEnabled(selected);
        btnDelete.setEnabled(selected);
    }

    private boolean validateForm() {
        String ten = txtTenNV.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
        String birth = txtNgaySinh.getText().trim();

        if (ten.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên, SĐT, Email không được để trống!", "Lỗi xác thực", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate SĐT: 10 numbers
        if (!sdt.matches("^\\d{10,11}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải chứa từ 10 đến 11 số!", "Lỗi xác thực", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate Email
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ (phải chứa ký tự @ và dấu chấm)!", "Lỗi xác thực", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate Date of Birth
        if (!birth.isEmpty()) {
            try {
                sdf.parse(birth);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng dd/MM/yyyy!", "Lỗi xác thực", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // Validate Account Linkage if checked
        if (chkCapTaiKhoan.isSelected()) {
            String user = txtUsername.getText().trim();
            String pass = new String(txtPassword.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Thông tin tài khoản (Username, Mật khẩu) không được để trống!", "Lỗi xác thực", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private void addNhanVien() {
        if (!validateForm()) return;

        Date birth = null;
        try {
            if (!txtNgaySinh.getText().trim().isEmpty()) {
                birth = sdf.parse(txtNgaySinh.getText().trim());
            }
        } catch (ParseException ignored) {}

        NhanVien nv = new NhanVien();
        nv.setTenNV(txtTenNV.getText().trim());
        nv.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
        nv.setNgaySinh(birth);
        nv.setSdt(txtSDT.getText().trim());
        nv.setEmail(txtEmail.getText().trim());
        nv.setChucVu(txtChucVu.getText().trim());

        // Perform insert
        if (nhanVienDAO.addNhanVien(nv)) {
            // Check linked account
            if (chkCapTaiKhoan.isSelected()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(txtUsername.getText().trim());
                tk.setMatKhau(new String(txtPassword.getPassword()).trim());
                tk.setQuyen(cbQuyen.getSelectedItem().toString());
                tk.setMaNV(nv.getMaNV());
                nhanVienDAO.saveTaiKhoan(tk);
            }
            JOptionPane.showMessageDialog(this, "Khai báo nhân viên kho mới thành công!");
            loadNhanVienTable();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên kho thất bại! Kiểm tra ràng buộc CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
=======
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        cbQuyen.setSelectedIndex(0);

        currentNhanVien = null;
        tableNV.clearSelection();

        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    private boolean validateForm(boolean isNew) {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();

        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã NV không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (isNew && nhanVienDAO.existsMaNV(maNV)) {
            JOptionPane.showMessageDialog(this, "Mã NV đã tồn tại. Vui lòng làm mới hoặc chọn mã khác.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (tenNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!sdt.matches("\\d{9,12}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là số và có 9-12 chữ số.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matcher(email).matches();
    }

    private void addNhanVien() {
        if (!validateForm(true)) {
            return;
        }

        Date birth = null;
        String bStr = txtNgaySinh.getText().trim();
        if (!bStr.isEmpty()) {
            try {
                birth = sdf.parse(bStr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ngày sinh sai định dạng dd/MM/yyyy!", "Sai ngày", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMaNV.getText().trim());
        nv.setTenNV(txtTenNV.getText().trim());
        nv.setSdt(txtSDT.getText().trim());
        nv.setEmail(txtEmail.getText().trim());
        nv.setNgaySinh(birth);
        nv.setGioiTinh(txtGioiTinh.getText().trim());
        nv.setChucVu(txtChucVu.getText().trim());

        if (nhanVienDAO.addNhanVien(nv)) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadNhanVienTable();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
>>>>>>> origin/van
        }
    }

    private void updateNhanVien() {
        if (currentNhanVien == null) return;
<<<<<<< HEAD
        if (!validateForm()) return;

        Date birth = null;
        try {
            if (!txtNgaySinh.getText().trim().isEmpty()) {
                birth = sdf.parse(txtNgaySinh.getText().trim());
            }
        } catch (ParseException ignored) {}

        currentNhanVien.setTenNV(txtTenNV.getText().trim());
        currentNhanVien.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
        currentNhanVien.setNgaySinh(birth);
        currentNhanVien.setSdt(txtSDT.getText().trim());
        currentNhanVien.setEmail(txtEmail.getText().trim());
        currentNhanVien.setChucVu(txtChucVu.getText().trim());

        if (nhanVienDAO.updateNhanVien(currentNhanVien)) {
            // Check linked account
            if (chkCapTaiKhoan.isSelected()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(txtUsername.getText().trim());
                tk.setMatKhau(new String(txtPassword.getPassword()).trim());
                tk.setQuyen(cbQuyen.getSelectedItem().toString());
                tk.setMaNV(currentNhanVien.getMaNV());
                nhanVienDAO.saveTaiKhoan(tk);
            } else {
                nhanVienDAO.deleteTaiKhoanByMaNV(currentNhanVien.getMaNV());
            }
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thành công!");
            loadNhanVienTable();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
=======
        if (!validateForm(false)) {
            return;
        }

        Date birth = null;
        String bStr = txtNgaySinh.getText().trim();
        if (!bStr.isEmpty()) {
            try {
                birth = sdf.parse(bStr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ngày sinh sai định dạng dd/MM/yyyy!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        currentNhanVien.setTenNV(txtTenNV.getText().trim());
        currentNhanVien.setSdt(txtSDT.getText().trim());
        currentNhanVien.setEmail(txtEmail.getText().trim());
        currentNhanVien.setNgaySinh(birth);
        currentNhanVien.setGioiTinh(txtGioiTinh.getText().trim());
        currentNhanVien.setChucVu(txtChucVu.getText().trim());

        if (nhanVienDAO.updateNhanVien(currentNhanVien)) {
            JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadNhanVienTable();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
>>>>>>> origin/van
        }
    }

    private void deleteNhanVien() {
        if (currentNhanVien == null) return;
<<<<<<< HEAD
        
        int choice = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa nhân sự này?\nMọi thông tin tài khoản đăng nhập cũng sẽ bị hủy bỏ.", "Xác nhận xóa nhân viên", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (nhanVienDAO.deleteNhanVien(currentNhanVien.getMaNV())) {
                JOptionPane.showMessageDialog(this, "Đã gạch tên nhân viên khỏi danh sách kho thành công!");
                loadNhanVienTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa do nhân viên này đã có lịch sử ký duyệt phiếu nhập/xuất kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
=======

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa nhân viên này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (nhanVienDAO.deleteNhanVien(currentNhanVien.getMaNV())) {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadNhanVienTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa do nhân viên này đã có chứng từ nhập/xuất kho!", "Lỗi ràng buộc khóa ngoại", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAccountDetails() {
        if (currentNhanVien == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc thêm nhân viên trước khi liên kết tài khoản!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = txtTenDangNhap.getText().trim();
        String password = new String(txtMatKhau.getPassword()).trim();
        String role = cbQuyen.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập và mật khẩu tài khoản không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setMaNV(currentNhanVien.getMaNV());
        tk.setTenDangNhap(username);
        tk.setMatKhau(password);
        tk.setQuyen(role);

        if (nhanVienDAO.saveTaiKhoan(tk)) {
            JOptionPane.showMessageDialog(this, "Lưu liên kết tài khoản hệ thống thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadNhanVienTable();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu tài khoản thất bại!", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }
>>>>>>> origin/van
}
