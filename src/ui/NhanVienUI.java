package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dao.NhanVienDAO;
import model.NhanVien;
import model.TaiKhoan;

public class NhanVienUI extends JPanel {

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
        this.nhanVienDAO = new NhanVienDAO();
        initComponents();
        loadNhanVienTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
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
            }
        });
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(230, 230, 230));
        txtTenNV = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();
        txtNgaySinh = new JTextField();
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


        btnAdd.addActionListener(e -> addNhanVien());
        btnUpdate.addActionListener(e -> updateNhanVien());
        btnDelete.addActionListener(e -> deleteNhanVien());
        btnClear.addActionListener(e -> clearForm());
        
        // Disable account fields when checkbox is off
        chkCapTaiKhoan.addActionListener(e -> toggleAccountFields(chkCapTaiKhoan.isSelected()));
        toggleAccountFields(false);

        btnPanel.add(btnAdd); 
        btnPanel.add(btnUpdate); 
        btnPanel.add(btnDelete); 
        btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addField(JPanel p, String label, JTextField field, int row, GridBagConstraints gbc) {
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
    }

    private void loadNhanVienTable() {
        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        if (list != null) {
            for (NhanVien nv : list) {
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
                cbQuyen.setSelectedItem(tk.getQuyen());
            } else {
                chkCapTaiKhoan.setSelected(false);
                toggleAccountFields(false);
            }
            
            updateButtonState();
        }
    }

    private void clearForm() {
        txtMaNV.setText(nhanVienDAO.generateNextMaNV());
        txtTenNV.setText("");
        cbGioiTinh.setSelectedIndex(0);
        txtNgaySinh.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtChucVu.setText("");

        chkCapTaiKhoan.setSelected(false);
        toggleAccountFields(false);

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
        }
    }

    private void updateNhanVien() {
        if (currentNhanVien == null) return;
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
        }
    }

    private void deleteNhanVien() {
        if (currentNhanVien == null) return;
        
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
}
