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
        this.khoDAO = new KhoDAO();
        this.nhanVienDAO = new NhanVienDAO();
        initComponents();
        loadKhoTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(creamWhite);

        // --- TITLE ---
        JLabel lblTitle = new JLabel("DANH MỤC KHU VỰC LƯU TRỮ & NHÀ KHO JOLLIBEE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(jollibeeRed);
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: JTABLE ---
        String[] columns = {"Mã Kho", "Tên Nhà Kho", "Thủ Kho Phụ Trách", "Sức chứa", "Ghi Chú"};
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
        tableKho.getColumnModel().getColumn(3).setCellRenderer(centerRender);

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
            }
        });
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setOpaque(false);

        // ── FORM NHẬP LIỆU ──────────────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);

        TitledBorder formBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(jollibeeRed, 2),
            " 🏪  Cấu hình Thông tin Nhà Kho "
        );
        formBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        formBorder.setTitleColor(jollibeeRed);
        form.setBorder(formBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 12, 7, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaKho = new JTextField();
        txtMaKho.setEditable(false);
        txtMaKho.setBackground(new Color(235, 235, 230));
        txtMaKho.setFont(new Font("SansSerif", Font.PLAIN, 13));

        txtTenKho = new JTextField(); txtTenKho.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDiaChi = new JTextField(); txtDiaChi.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtSucChua = new JTextField("0"); txtSucChua.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtGhiChu = new JTextField(); txtGhiChu.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbNhanVien = new JComboBox<>(); cbNhanVien.setFont(new Font("SansSerif", Font.PLAIN, 13));

        Dimension fieldSize = new Dimension(210, 32);
        txtMaKho.setPreferredSize(fieldSize); txtTenKho.setPreferredSize(fieldSize);
        txtDiaChi.setPreferredSize(fieldSize); txtSucChua.setPreferredSize(fieldSize);
        txtGhiChu.setPreferredSize(fieldSize); cbNhanVien.setPreferredSize(fieldSize);

        addField(form, "Mã kho:", txtMaKho, 0, gbc);
        addField(form, "Tên nhà kho:", txtTenKho, 1, gbc);
        addField(form, "Sức chứa (Tấn):", txtSucChua, 2, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        JLabel lblNV = new JLabel("Thủ kho:");
        lblNV.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNV.setForeground(darkCharcoal);
        form.add(lblNV, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        form.add(cbNhanVien, gbc);

        addField(form, "Ghi chú:", txtGhiChu, 4, gbc);

        panel.add(form, BorderLayout.CENTER);

        // ── PANEL NÚT CHỦC NĂNG (GridLayout 2x2 giống NguyenLieuUI) ────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(new Color(245, 245, 240));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

        btnAdd    = createStyledButton("➕  Thêm",    new Color(34, 139, 34), Color.WHITE);
        btnUpdate = createStyledButton("✏  Cập nhật", new Color(30, 100, 200), Color.WHITE);
        btnDelete = createStyledButton("🗑  Xóa",     jollibeeRed,           Color.WHITE);
        btnClear  = createStyledButton("🔄  Nhập mới", darkCharcoal,          Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addKho());
        btnUpdate.addActionListener(e -> updateKho());
        btnDelete.addActionListener(e -> deleteKho());
        btnClear.addActionListener(e -> clearForm());

        return panel;
    }

    private void addField(JPanel p, String label, JTextField field, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(darkCharcoal);
        p.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        p.add(field, gbc);
    }

    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.putClientProperty("JButton.buttonType", "roundRect");
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        return btn;
    }
    
    private void loadKhoTable() {
        tableModel.setRowCount(0);

        // Load cbNhanVien
        cbNhanVien.removeAllItems();
        List<NhanVien> listNV = nhanVienDAO.getAllNhanVien();
        if (listNV != null) {
            for (NhanVien nv : listNV) {
                cbNhanVien.addItem(nv.getMaNV().trim() + " | " + nv.getTenNV().trim());
            }
        }

        List<Kho> list = khoDAO.getAllKho();
        if (list != null) {
            for (Kho k : list) {
                String managerName = "Chưa bổ nhiệm";
                if (k.getMaNV() != null && !k.getMaNV().trim().isEmpty()) {
                    managerName = nhanVienDAO.getTenNhanVienByMaNV(k.getMaNV().trim());
                }

                tableModel.addRow(new Object[]{
                    k.getMaKho().trim(),
                    k.getTenKho().trim(),
                    (k.getMaNV() != null ? k.getMaNV().trim() : "") + " - " + managerName.trim(),
                    k.getSucChua(),
                    k.getGhiChu() != null ? k.getGhiChu().trim() : ""
                });
            }
        }
        // Tự động giãn cách các cột bảng nhà kho
        util.UIHelper.autoResizeColumnWidths(tableKho);
        resetFormAndSelection();
    }

    private void displayDetails(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) return;
        String maKho = (String) tableModel.getValueAt(row, 0);
        if (maKho == null || maKho.isEmpty()) return;
        maKho = maKho.trim();

        // Đọc giá trị sức chứa trực tiếp từ bảng (cột 3)
        Object sucChuaObj = tableModel.getValueAt(row, 3);
        String sucChuaStr = (sucChuaObj != null) ? sucChuaObj.toString() : "0";

        currentKho = khoDAO.getKhoById(maKho);
        if (currentKho != null) {
            txtMaKho.setText(currentKho.getMaKho().trim());
            txtTenKho.setText(currentKho.getTenKho().trim());
            txtDiaChi.setText(currentKho.getDiaChi() != null ? currentKho.getDiaChi().trim() : "");
            txtSucChua.setText(sucChuaStr);   // Hiển thị giá trị từ cột bảng
            txtGhiChu.setText(currentKho.getGhiChu() != null ? currentKho.getGhiChu().trim() : "");

            // Chọn thủ kho trong ComboBox
            boolean found = false;
            if (currentKho.getMaNV() != null && !currentKho.getMaNV().trim().isEmpty()) {
                String maNVTrimmed = currentKho.getMaNV().trim();
                for (int i = 0; i < cbNhanVien.getItemCount(); i++) {
                    String item = cbNhanVien.getItemAt(i);
                    if (item != null && item.trim().startsWith(maNVTrimmed)) {
                        cbNhanVien.setSelectedIndex(i);
                        found = true;
                        break;
                    }
                }
            }
            if (!found && cbNhanVien.getItemCount() > 0) {
                cbNhanVien.setSelectedIndex(0);
            }

            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        } else {
            System.err.println("[NhaKhoUI] displayDetails: không tìm thấy kho với mã: " + maKho);
        }
    }

    private void clearForm() {
        txtMaKho.setText(khoDAO.generateNextMaKho());
        txtTenKho.setText("");
        txtDiaChi.setText("");
        txtSucChua.setText("0");
        txtGhiChu.setText("");
        currentKho = null;
        // Không gọi tableKho.clearSelection() ở đây để tránh vòng lặp sự kiện
        // tableKho.clearSelection() sẽ được gọi riêng khi cần thiết
        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    /** Gọi khi cần reset cả form lẫn selection trong bảng */
    private void resetFormAndSelection() {
        clearForm();
        tableKho.clearSelection();
    }

    private void addKho() {
        String ten = txtTenKho.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà kho không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
        }
    }

    private void deleteKho() {
        if (currentKho == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa nhà kho này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (khoDAO.deleteKho(currentKho.getMaKho())) {
                JOptionPane.showMessageDialog(this, "Xóa nhà kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadKhoTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa do nhà kho này đang chứa các nguyên liệu!", "Lỗi ràng buộc khóa ngoại", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        loadKhoTable();
    }
}
