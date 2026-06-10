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
        this.khoDAO = new KhoDAO();
        this.nhanVienDAO = new NhanVienDAO();
        initComponents();
        loadKhoTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(creamWhite);

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
            }
        });
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setOpaque(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        
        TitledBorder formBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(jollibeeRed, 1), "Thông tin chi tiết Phân khu Kho"
        );
        formBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        formBorder.setTitleColor(jollibeeRed);
        form.setBorder(formBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaKho = new JTextField();
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
                        break;
                    }
                }
            }
            updateButtonState();
        }
    }

    private void clearForm() {
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

        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà kho không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentKho.setTenKho(ten);
        currentKho.setMaNV(maNV);
        
        if (khoDAO.updateKho(currentKho)) {
            JOptionPane.showMessageDialog(this, "Cập nhật phân khu kho thành công!");
            loadKhoTable();
        }
    }

    private void deleteKho() {
        if (currentKho == null) return;
        
        int choice = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa phân khu kho này?\nMọi nguyên liệu chứa trong kho sẽ bị ảnh hưởng.", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (khoDAO.deleteKho(currentKho.getMaKho())) {
                JOptionPane.showMessageDialog(this, "Xóa phân khu kho thành công!");
                loadKhoTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa do vướng ràng buộc nguyên liệu đang được lưu trữ trong kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
