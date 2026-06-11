package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

import dao.KhoDAO;
import dao.NhanVienDAO;
import model.Kho;
import model.NhanVien;
<<<<<<< HEAD

public class NhaKhoUI extends JPanel {

    private final Color jollibeeRed = new Color(227, 29, 43);
    private final Color creamWhite = new Color(255, 253, 240);
    private final Color darkGray = new Color(50, 50, 50);
    private static final Color tableHeaderBg = new Color(180, 30, 45);
    

    private JTextField txtMaKho, txtTenKho;
    private JComboBox<String> cbThuKho;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable khoTable;
    private DefaultTableModel tableModel;

    private KhoDAO khoDAO;
    private NhanVienDAO nhanVienDAO;
    private Kho currentKho;

    public NhaKhoUI() {
=======
import model.TaiKhoan;

public class NhaKhoUI extends JPanel {

    private final Color jollibeeRed = new Color(224, 31, 42);      // #E01F2A
    private final Color creamWhite = new Color(255, 253, 240);     // #FFFDF0
    private final Color darkCharcoal = new Color(45, 45, 45);
    private final Color tableHeaderBg = new Color(180, 30, 45);
    private final Color ROW_ODD         = new Color(255, 253, 245);
    private final Color ROW_EVEN        = new Color(245, 240, 230);

    private JTextField txtMaKho, txtTenKho, txtDiaChi, txtSucChua, txtGhiChu;
    private JComboBox<String> cbNhanVien;
    
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tableKho;
    private DefaultTableModel tableModel;
    
    private KhoDAO khoDAO;
    private NhanVienDAO nhanVienDAO;
    private Kho currentKho;
    private TaiKhoan currentUser;

    public NhaKhoUI(TaiKhoan user) {
        this.currentUser = user;
>>>>>>> origin/van
        this.khoDAO = new KhoDAO();
        this.nhanVienDAO = new NhanVienDAO();
        initComponents();
        loadKhoTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(creamWhite);

<<<<<<< HEAD
        // Title
        JLabel lblTitle = new JLabel("DANH MỤC NHÀ KHO JOLLIBEE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(tableHeaderBg);
        add(lblTitle, BorderLayout.NORTH);

        // Center Table Panel
        String[] columnNames = {"Mã Kho", "Tên Nhà Kho", "Thủ Kho Phụ Trách", "Sức Chứa", "Số Nguyên Liệu Đang Lưu"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        khoTable = new JTable(tableModel);
        khoTable.setRowHeight(30);
        khoTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = khoTable.getTableHeader();
        header.setBackground(tableHeaderBg);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        khoTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        khoTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        khoTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(khoTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Trong initComponents(), sau khi set header properties, thêm:
DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBackground(tableHeaderBg);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setHorizontalAlignment(JLabel.CENTER);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
        return this;
    }
};

for (int i = 0; i < khoTable.getColumnCount(); i++) {
    khoTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
}


        // East Form Panel
        add(createDetailPanel(), BorderLayout.EAST);

        // Selection Listener
        khoTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && khoTable.getSelectedRow() != -1) {
                displayKhoDetails(khoTable.getSelectedRow());
=======
        // --- TITLE ---
        JLabel lblTitle = new JLabel("DANH MỤC KHU VỰC LƯU TRỮ & NHÀ KHO JOLLIBEE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(jollibeeRed);
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: JTABLE ---
        String[] columns = {"Mã Kho", "Tên Nhà Kho", "Thủ Kho Phụ Trách", "Địa Chỉ", "Sức Chứa (Tấn)", "Số Loại Vật Tư Đang Lưu", "Ghi Chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableKho = new JTable(tableModel) {
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
        tableKho.setRowHeight(32);
        tableKho.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKho.setGridColor(new Color(220, 210, 195));
        tableKho.setShowGrid(true);
        tableKho.setIntercellSpacing(new Dimension(1, 1));

        tableKho.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(tableHeaderBg);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, jollibeeRed));
                return lbl;
            }
        });

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tableKho.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tableKho.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        tableKho.getColumnModel().getColumn(5).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(tableKho);
        scrollPane.getViewport().setBackground(ROW_ODD);
        scrollPane.setBorder(BorderFactory.createLineBorder(jollibeeRed, 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- EAST: CRUD FORM ---
        add(createDetailPanel(), BorderLayout.EAST);

        // Selection listener
        tableKho.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableKho.getSelectedRow() != -1) {
                int selectedRow = tableKho.getSelectedRow();
                int modelRow = tableKho.convertRowIndexToModel(selectedRow);
                displayDetails(modelRow);
>>>>>>> origin/van
            }
        });
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setOpaque(false);

        JPanel form = new JPanel(new GridBagLayout());
<<<<<<< HEAD
        form.setOpaque(false);
        
        TitledBorder formBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(jollibeeRed, 1), "Thông tin chi tiết Phân khu Kho"
=======
        form.setBackground(Color.WHITE);
        TitledBorder formBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(jollibeeRed, 2), "Cấu hình Thông tin Nhà Kho"
>>>>>>> origin/van
        );
        formBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        formBorder.setTitleColor(jollibeeRed);
        form.setBorder(formBorder);

        GridBagConstraints gbc = new GridBagConstraints();
<<<<<<< HEAD
        gbc.insets = new Insets(8, 10, 8, 10);
=======
        gbc.insets = new Insets(8, 12, 8, 12);
>>>>>>> origin/van
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaKho = new JTextField();
<<<<<<< HEAD
        txtTenKho = new JTextField();
        cbThuKho = new JComboBox<>();
        cbThuKho.setBackground(Color.WHITE);

        Dimension fieldSize = new Dimension(200, 30);
        txtMaKho.setPreferredSize(fieldSize);
        txtTenKho.setPreferredSize(fieldSize);
        cbThuKho.setPreferredSize(fieldSize);

        // Load Nhan Vien combobox
        List<NhanVien> nvList = nhanVienDAO.getAllNhanVien();
        cbThuKho.addItem("-- Chọn thủ kho --");
        if (nvList != null) {
            for (NhanVien nv : nvList) {
                cbThuKho.addItem(nv.getMaNV() + " | " + nv.getTenNV());
            }
        }

        addField(form, "Mã Phân Kho:", txtMaKho, 0, gbc);
        addField(form, "Tên Nhà Kho:", txtTenKho, 1, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Thủ Kho Phụ Trách:"), gbc);
        gbc.gridx = 1; form.add(cbThuKho, gbc);

        panel.add(form, BorderLayout.CENTER);

        // Buttons Panel
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
        txtMaKho.setEditable(false);
        txtMaKho.setBackground(new Color(235, 235, 230));

        txtTenKho = new JTextField();
        txtDiaChi = new JTextField();
        txtSucChua = new JTextField("0");
        txtGhiChu = new JTextField();

        cbNhanVien = new JComboBox<>();

        Dimension fieldSize = new Dimension(210, 30);
        txtMaKho.setPreferredSize(fieldSize);
        txtTenKho.setPreferredSize(fieldSize);
        txtDiaChi.setPreferredSize(fieldSize);
        txtSucChua.setPreferredSize(fieldSize);
        txtGhiChu.setPreferredSize(fieldSize);
        cbNhanVien.setPreferredSize(fieldSize);

        addField(form, "Mã kho:", txtMaKho, 0, gbc);
        addField(form, "Tên nhà kho:", txtTenKho, 1, gbc);
        addField(form, "Địa chỉ:", txtDiaChi, 2, gbc);
        addField(form, "Sức chứa (Tấn):", txtSucChua, 3, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Thủ kho:"), gbc);
        gbc.gridx = 1;
        form.add(cbNhanVien, gbc);

        addField(form, "Ghi chú:", txtGhiChu, 5, gbc);

        panel.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        btnPanel.setOpaque(false);

        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");

        styleButton(btnAdd, new Color(40, 167, 69)); // Xanh lá
        styleButton(btnUpdate, new Color(0, 123, 255)); // Xanh dương
        styleButton(btnDelete, jollibeeRed); // Đỏ
        styleButton(btnClear, darkCharcoal); // Xám

        Dimension btnSize = new Dimension(80, 35);
        btnAdd.setPreferredSize(btnSize);
        btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);
        btnClear.setPreferredSize(btnSize);

>>>>>>> origin/van
        btnAdd.addActionListener(e -> addKho());
        btnUpdate.addActionListener(e -> updateKho());
        btnDelete.addActionListener(e -> deleteKho());
        btnClear.addActionListener(e -> clearForm());

<<<<<<< HEAD
        btnPanel.add(btnAdd); 
        btnPanel.add(btnUpdate); 
        btnPanel.add(btnDelete); 
=======
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
>>>>>>> origin/van
        btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

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

    private void loadKhoTable() {
        tableModel.setRowCount(0);
        List<Kho> list = khoDAO.getAllKho();
        if (list != null) {
            for (Kho k : list) {
                String tenNV = nhanVienDAO.getTenNhanVienByMaNV(k.getMaNV());
                int count = khoDAO.getIngredientCountByKho(k.getMaKho());
                tableModel.addRow(new Object[]{
                        k.getMaKho(),
                        k.getTenKho(),
                        k.getMaNV() != null ? (k.getMaNV() + " - " + tenNV) : "Chưa bàn giao",
                        "2,000 Kg", // Sức chứa tiêu chuẩn
                        count + " loại NL"
                });
            }
        }
        clearForm();
    }

    private void displayKhoDetails(int row) {
        String ma = (String) tableModel.getValueAt(row, 0);
        currentKho = khoDAO.getKhoById(ma);
        if (currentKho != null) {
            txtMaKho.setText(currentKho.getMaKho());
            txtMaKho.setEditable(false);
            txtMaKho.setBackground(new Color(230, 230, 230));
            txtTenKho.setText(currentKho.getTenKho());
            
            cbThuKho.setSelectedIndex(0);
            if (currentKho.getMaNV() != null) {
                for (int i = 0; i < cbThuKho.getItemCount(); i++) {
                    if (cbThuKho.getItemAt(i).startsWith(currentKho.getMaNV())) {
                        cbThuKho.setSelectedIndex(i);
=======
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
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.putClientProperty("JButton.buttonType", "roundRect");
    }
    
    private void loadKhoTable() {
        tableModel.setRowCount(0);

        // Load cbNhanVien
        cbNhanVien.removeAllItems();
        List<NhanVien> listNV = nhanVienDAO.getAllNhanVien();
        if (listNV != null) {
            for (NhanVien nv : listNV) {
                cbNhanVien.addItem(nv.getMaNV() + " | " + nv.getTenNV());
            }
        }

        List<Kho> list = khoDAO.getAllKho();
        if (list != null) {
            for (Kho k : list) {
                String managerName = "Chưa bổ nhiệm";
                if (k.getMaNV() != null) {
                    managerName = nhanVienDAO.getTenNhanVienByMaNV(k.getMaNV());
                }
                int count = khoDAO.getIngredientCountByKho(k.getMaKho());

                tableModel.addRow(new Object[]{
                    k.getMaKho(),
                    k.getTenKho(),
                    k.getMaNV() + " - " + managerName,
                    k.getDiaChi(),
                    k.getSucChua(),
                    count,
                    k.getGhiChu()
                });
            }
        }
        // Tự động giãn cách các cột bảng nhà kho
        util.UIHelper.autoResizeColumnWidths(tableKho);
        clearForm();
    }

    private void displayDetails(int row) {
        String maKho = (String) tableModel.getValueAt(row, 0);
        currentKho = khoDAO.getKhoById(maKho);
        if (currentKho != null) {
            txtMaKho.setText(currentKho.getMaKho());
            txtTenKho.setText(currentKho.getTenKho());
            txtDiaChi.setText(currentKho.getDiaChi());
            txtSucChua.setText(String.valueOf(currentKho.getSucChua()));
            txtGhiChu.setText(currentKho.getGhiChu());

            // Select manager combo box
            boolean found = false;
            if (currentKho.getMaNV() != null) {
                for (int i = 0; i < cbNhanVien.getItemCount(); i++) {
                    if (cbNhanVien.getItemAt(i).startsWith(currentKho.getMaNV())) {
                        cbNhanVien.setSelectedIndex(i);
                        found = true;
>>>>>>> origin/van
                        break;
                    }
                }
            }
<<<<<<< HEAD
            updateButtonState();
=======
            if (!found) {
                cbNhanVien.setSelectedIndex(-1);
            }
            
            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
>>>>>>> origin/van
        }
    }

    private void clearForm() {
<<<<<<< HEAD
        txtMaKho.setText("");
        txtMaKho.setEditable(true);
        txtMaKho.setBackground(Color.WHITE);
        txtTenKho.setText("");
        cbThuKho.setSelectedIndex(0);
        currentKho = null;
        khoTable.clearSelection();
        updateButtonState();
    }

    private void updateButtonState() {
        boolean selected = khoTable.getSelectedRow() != -1;
        btnAdd.setEnabled(!selected);
        btnUpdate.setEnabled(selected);
        btnDelete.setEnabled(selected);
    }

    private void addKho() {
        String ma = txtMaKho.getText().trim();
        String ten = txtTenKho.getText().trim();
        String selectedNV = cbThuKho.getSelectedItem().toString();
        String maNV = selectedNV.contains("|") ? selectedNV.split(" \\| ")[0] : null;

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã kho và Tên nhà kho không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check duplicates
        if (khoDAO.getKhoById(ma) != null) {
            JOptionPane.showMessageDialog(this, "Mã kho này đã tồn tại trên hệ thống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Kho k = new Kho(ma, ten, maNV);
        if (khoDAO.addKho(k)) {
            JOptionPane.showMessageDialog(this, "Thêm phân khu kho thành công!");
            loadKhoTable();
        }
    }

    private void updateKho() {
        if (currentKho == null) return;
        
        String ten = txtTenKho.getText().trim();
        String selectedNV = cbThuKho.getSelectedItem().toString();
        String maNV = selectedNV.contains("|") ? selectedNV.split(" \\| ")[0] : null;

=======
        txtMaKho.setText(khoDAO.generateNextMaKho());
        txtTenKho.setText("");
        txtDiaChi.setText("");
        txtSucChua.setText("0");
        txtGhiChu.setText("");
        currentKho = null;
        tableKho.clearSelection();
        
        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    private void addKho() {
        String ten = txtTenKho.getText().trim();
>>>>>>> origin/van
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà kho không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

<<<<<<< HEAD
        currentKho.setTenKho(ten);
        currentKho.setMaNV(maNV);
        
        if (khoDAO.updateKho(currentKho)) {
            JOptionPane.showMessageDialog(this, "Cập nhật phân khu kho thành công!");
            loadKhoTable();
=======
        int sucChua = 0;
        try {
            sucChua = Integer.parseInt(txtSucChua.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là một số nguyên hợp lệ!", "Sai định dạng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String maNV = "";
        if (cbNhanVien.getSelectedItem() != null) {
            maNV = cbNhanVien.getSelectedItem().toString().split(" \\| ")[0];
        }

        Kho k = new Kho();
        k.setMaKho(txtMaKho.getText());
        k.setTenKho(ten);
        k.setDiaChi(txtDiaChi.getText().trim());
        k.setSucChua(sucChua);
        k.setMaNV(maNV);
        k.setGhiChu(txtGhiChu.getText().trim());

        if (khoDAO.addKho(k)) {
            JOptionPane.showMessageDialog(this, "Thêm nhà kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadKhoTable();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm nhà kho thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateKho() {
        if (currentKho == null) return;

        String ten = txtTenKho.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà kho không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sucChua = 0;
        try {
            sucChua = Integer.parseInt(txtSucChua.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên!", "Sai định dạng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String maNV = "";
        if (cbNhanVien.getSelectedItem() != null) {
            maNV = cbNhanVien.getSelectedItem().toString().split(" \\| ")[0];
        }

        currentKho.setTenKho(ten);
        currentKho.setDiaChi(txtDiaChi.getText().trim());
        currentKho.setSucChua(sucChua);
        currentKho.setMaNV(maNV);
        currentKho.setGhiChu(txtGhiChu.getText().trim());

        if (khoDAO.updateKho(currentKho)) {
            JOptionPane.showMessageDialog(this, "Cập nhật nhà kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadKhoTable();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
>>>>>>> origin/van
        }
    }

    private void deleteKho() {
        if (currentKho == null) return;
        
<<<<<<< HEAD
        int choice = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa phân khu kho này?\nMọi nguyên liệu chứa trong kho sẽ bị ảnh hưởng.", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (khoDAO.deleteKho(currentKho.getMaKho())) {
                JOptionPane.showMessageDialog(this, "Xóa phân khu kho thành công!");
                loadKhoTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa do vướng ràng buộc nguyên liệu đang được lưu trữ trong kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
=======
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa nhà kho này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (khoDAO.deleteKho(currentKho.getMaKho())) {
                JOptionPane.showMessageDialog(this, "Xóa nhà kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadKhoTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa do nhà kho này đang chứa các nguyên liệu!", "Lỗi ràng buộc khóa ngoại", JOptionPane.ERROR_MESSAGE);
>>>>>>> origin/van
            }
        }
    }
}
