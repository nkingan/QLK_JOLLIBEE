package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dao.NhanVienDAO;
import model.NhanVien;
import model.TaiKhoan;

public class NhanVienUI extends JPanel {

    private final Color jollibeeRed = new Color(224, 31, 42);
    private final Color creamWhite = new Color(255, 253, 240);
    private final Color darkCharcoal = new Color(45, 45, 45);

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
        setBackground(creamWhite);

        // --- TITLE ---
        JLabel lblTitle = new JLabel("DANH MỤC THỦ KHO & QUẢN TRỊ TÀI KHOẢN HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(jollibeeRed);
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: JTABLE ---
        String[] columns = {"Mã NV", "Họ & Tên", "Số Điện Thoại", "Email", "Ngày Sinh", "Giới Tính", "Chức Vụ", "Tài Khoản HT"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableNV = new JTable(tableModel);
        tableNV.setRowHeight(30);
        tableNV.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableNV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tableNV.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(139, 69, 19)); // Nâu ấm
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        });

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tableNV.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        tableNV.getColumnModel().getColumn(7).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(tableNV);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(jollibeeRed, 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- EAST: FORM INTEGRATION ---
        add(createDetailPanel(), BorderLayout.EAST);

        // Selection Listener
        tableNV.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableNV.getSelectedRow() != -1) {
                displayDetails(tableNV.getSelectedRow());
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
            BorderFactory.createLineBorder(jollibeeRed, 2), "Thông tin Cá nhân Nhân viên"
        );
        borderEmp.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        borderEmp.setTitleColor(jollibeeRed);
        formEmp.setBorder(borderEmp);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(235, 235, 230));

        txtTenNV = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();
        txtNgaySinh = new JTextField();
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
        styleButton(btnUpdate, new Color(0, 123, 255));
        styleButton(btnDelete, jollibeeRed);
        styleButton(btnClear, darkCharcoal);

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
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(lbl, gbc);
        gbc.gridx = 1;
        p.add(field, gbc);
    }

    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setFocusPainted(false);
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

    private void addNhanVien() {
        String ten = txtTenNV.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
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
        nv.setMaNV(txtMaNV.getText());
        nv.setTenNV(ten);
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

        String ten = txtTenNV.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
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

        currentNhanVien.setTenNV(ten);
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
}
