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
        String[] columns = {"Mã Kho", "Tên Nhà Kho", "Thủ Kho Phụ Trách", "Địa Chỉ", "Sức Chứa (Tấn)", "Số Loại Vật Tư Đang Lưu", "Ghi Chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableKho = new JTable(tableModel);
        tableKho.setRowHeight(30);
        tableKho.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tableKho.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(139, 69, 19)); // Nâu ấm Jollibee
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        });

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tableKho.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tableKho.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        tableKho.getColumnModel().getColumn(5).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(tableKho);
        scrollPane.getViewport().setBackground(Color.WHITE);
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
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setOpaque(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        TitledBorder formBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(jollibeeRed, 2), "Cấu hình Thông tin Nhà Kho"
        );
        formBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        formBorder.setTitleColor(jollibeeRed);
        form.setBorder(formBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaKho = new JTextField();
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

        btnAdd.addActionListener(e -> addKho());
        btnUpdate.addActionListener(e -> updateKho());
        btnDelete.addActionListener(e -> deleteKho());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

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
                        break;
                    }
                }
            }
            if (!found) {
                cbNhanVien.setSelectedIndex(-1);
            }
            
            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
    }

    private void clearForm() {
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
}
