package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import dao.NhanVienDAO;
import model.NhanVien;
import model.TaiKhoan;

public class NhanVienUI extends JPanel {

    private static final Color JOLLIBEE_RED = new Color(227, 29, 43);
    private static final Color TABLE_HEADER_BG = new Color(180, 30, 45);
    private static final Color ROW_ODD         = new Color(255, 253, 245);
    private static final Color ROW_EVEN        = new Color(245, 240, 230);
    private static final Color JOLLIBEE_YELLOW = new Color(0xFFC72C);
    private static final Color CREAM_WHITE = new Color(255, 253, 240);
    private static final Color DARK_CHARCOAL = new Color(45, 45, 45);

    private JTextField txtMaNV, txtTenNV, txtSDT, txtEmail, txtNgaySinh, txtGioiTinh, txtChucVu;
    
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
        this.nhanVienDAO = new NhanVienDAO();
        initComponents();
        loadNhanVienTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
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

        tableNV = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(255, 180, 0, 200)); // highlight vàng Jollibee
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        tableNV.setRowHeight(32);
        tableNV.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableNV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNV.setGridColor(new Color(220, 210, 195));
        tableNV.setShowGrid(true);
        tableNV.setIntercellSpacing(new Dimension(1, 1));
        tableNV.setAutoCreateRowSorter(true);
        tableNV.getTableHeader().setReorderingAllowed(false);

        tableNV.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(TABLE_HEADER_BG);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, JOLLIBEE_RED));
                return lbl;
            }
        });

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tableNV.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(7).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(tableNV);
        scrollPane.getViewport().setBackground(ROW_ODD);
        scrollPane.setBorder(BorderFactory.createLineBorder(JOLLIBEE_RED, 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- EAST: FORM INTEGRATION ---
        add(createDetailPanel(), BorderLayout.EAST);

        // Selection Listener
        tableNV.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableNV.getSelectedRow() != -1) {
                displayDetails(tableNV.convertRowIndexToModel(tableNV.getSelectedRow()));
            }
        });
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(390, 0));
        panel.setOpaque(false);

        // Sub-Form 1: Employee CRUD
        JPanel formEmp = new JPanel(new GridBagLayout());
        formEmp.setBackground(Color.WHITE);
        TitledBorder borderEmp = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(JOLLIBEE_RED, 2), " 👤  Thông tin Cá nhân Nhân viên "
        );
        borderEmp.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        borderEmp.setTitleColor(JOLLIBEE_RED);
        formEmp.setBorder(borderEmp);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(235, 235, 230));
        txtMaNV.setFont(new Font("SansSerif", Font.PLAIN, 13));

        txtTenNV = new JTextField(); txtTenNV.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtSDT = new JTextField(); txtSDT.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtEmail = new JTextField(); txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtNgaySinh = new JTextField(); txtNgaySinh.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtGioiTinh = new JTextField(); txtGioiTinh.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtChucVu = new JTextField(); txtChucVu.setFont(new Font("SansSerif", Font.PLAIN, 13));

        Dimension tfSize = new Dimension(210, 32);
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
            BorderFactory.createLineBorder(new Color(242, 142, 43), 2), " 🔑  Liên kết tài khoản truy cập hệ thống "
        );
        borderAcc.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        borderAcc.setTitleColor(new Color(242, 142, 43));
        formAcc.setBorder(borderAcc);

        chkCapTaiKhoan = new JCheckBox("Cấp quyền tài khoản đăng nhập");
        chkCapTaiKhoan.setFont(new Font("SansSerif", Font.BOLD, 13));
        chkCapTaiKhoan.setOpaque(false);
        
        txtTenDangNhap = new JTextField(); txtTenDangNhap.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMatKhau = new JPasswordField(); txtMatKhau.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbQuyen = new JComboBox<>(new String[]{"Nhân viên", "WarehouseManager", "Admin"}); cbQuyen.setFont(new Font("SansSerif", Font.PLAIN, 13));

        txtTenDangNhap.setPreferredSize(tfSize);
        txtMatKhau.setPreferredSize(tfSize);
        cbQuyen.setPreferredSize(tfSize);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formAcc.add(chkCapTaiKhoan, gbc);
        
        gbc.gridwidth = 1;
        
        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUser.setForeground(DARK_CHARCOAL);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formAcc.add(lblUser, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formAcc.add(txtTenDangNhap, gbc);

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPass.setForeground(DARK_CHARCOAL);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formAcc.add(lblPass, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formAcc.add(txtMatKhau, gbc);

        JLabel lblQuyen = new JLabel("Quyền hạn:");
        lblQuyen.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblQuyen.setForeground(DARK_CHARCOAL);
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formAcc.add(lblQuyen, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formAcc.add(cbQuyen, gbc);

        btnSaveAccount = new JButton(" Lưu Thông Tin Tài Khoản");
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

        btnAdd = new JButton("➕ Thêm NV");
        btnUpdate = new JButton("✏ Cập nhật");
        btnDelete = new JButton("🗑 Xóa NV");
        btnClear = new JButton("🔄 Nhập mới");

        styleButton(btnAdd, new Color(40, 167, 69));
        styleButton(btnUpdate, JOLLIBEE_YELLOW.darker());
        styleButton(btnDelete, JOLLIBEE_RED);
        styleButton(btnClear, DARK_CHARCOAL);

        Dimension btnSize = new Dimension(84, 34);
        btnAdd.setPreferredSize(btnSize);
        btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);
        btnClear.setPreferredSize(btnSize);

        btnAdd.addActionListener(e -> addNhanVien());
        btnUpdate.addActionListener(e -> updateNhanVien());
        btnDelete.addActionListener(e -> deleteNhanVien());
        btnClear.addActionListener(e -> clearForm());
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

        return panel;
    }

    private void addField(JPanel p, String label, JTextField field, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(DARK_CHARCOAL);
        p.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        p.add(field, gbc);
    }

    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        // b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void toggleAccountFields(boolean enabled) {
        txtTenDangNhap.setEnabled(enabled);
        txtMatKhau.setEnabled(enabled);
        cbQuyen.setEnabled(enabled);
        btnSaveAccount.setEnabled(enabled);
    }

    private void loadNhanVienTable() {
        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        if (list != null) {
            for (NhanVien nv : list) {
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
                cbQuyen.setSelectedItem(tk.getQuyen());
            } else {
                chkCapTaiKhoan.setSelected(false);
                toggleAccountFields(false);
                txtTenDangNhap.setText("");
                txtMatKhau.setText("");
                cbQuyen.setSelectedIndex(0);
            }

            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
    }

    private void clearForm() {
        txtMaNV.setText(nhanVienDAO.generateNextMaNV());
        txtTenNV.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtNgaySinh.setText("");
        txtGioiTinh.setText("");
        txtChucVu.setText("");

        chkCapTaiKhoan.setSelected(false);
        toggleAccountFields(false);
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
        }
    }

    private void updateNhanVien() {
        if (currentNhanVien == null) return;
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
        }
    }

    private void deleteNhanVien() {
        if (currentNhanVien == null) return;

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

    public void refreshData() {
        loadNhanVienTable();
    }
}
