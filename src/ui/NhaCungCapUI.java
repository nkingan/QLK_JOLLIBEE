package ui;

import model.NhaCungCap;
import model.NhanVien;
import dao.NhaCungCapDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapUI extends JPanel {

    // --- BẢNG MÀU THƯƠNG HIỆU JOLLIBEE (Đồng bộ hoàn hoàn với Login & NguyenLieuUI) ---
    private final Color jollibeeRed = new Color(227, 29, 43);      // #E31D2B
    private final Color creamWhite = new Color(255, 253, 240);     // Nền kem nhẹ
    private final Color darkGray = new Color(50, 50, 50);
    private final Color tableHeaderBg = new Color(180, 30, 45);    // Màu đỏ Jollibee sậm đồng nhất
    private final Color ROW_ODD         = new Color(255, 253, 245);
    private final Color ROW_EVEN        = new Color(245, 240, 230);

    private JTextField txtMaNCC, txtTenNCC, txtDiachi, txtSDT, txtEmail;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTextField txtSearch;
    private JComboBox<String> cbSearchType;
    private JTable nhaCungCapTable;
    private DefaultTableModel tableModel;
    private NhaCungCapDAO nhaCungCapDAO;
    private NhaCungCap currentNhaCungCap;

    public NhaCungCapUI(NhanVien currentUser) {
        nhaCungCapDAO = new NhaCungCapDAO();
        initComponents();
        loadNhaCCTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(creamWhite);

        // ==========================================
        // 1. TOP PANEL: THANH TÌM KIẾM
        // ==========================================
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Tìm kiếm theo:");
        lblSearch.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchPanel.add(lblSearch);

        cbSearchType = new JComboBox<>(new String[]{"Mã NCC", "Tên NCC"});
        cbSearchType.setPreferredSize(new Dimension(110, 32));
        searchPanel.add(cbSearchType);

        txtSearch = new JTextField(22);
        txtSearch.setPreferredSize(new Dimension(200, 32));
        searchPanel.add(txtSearch);

        btnSearch = new JButton("Tìm kiếm");
        styleButton(btnSearch, jollibeeRed, Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(100, 32));
        searchPanel.add(btnSearch);
        
        btnSearch.addActionListener(e -> performSearch());
        add(searchPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. CENTER PANEL: BẢNG DANH SÁCH NHÀ CUNG CẤP
        // ==========================================
        String[] columnNames = {"Mã NCC", "Tên Nhà Cung Cấp", "Địa chỉ", "Số Điện Thoại", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override 
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        nhaCungCapTable = new JTable(tableModel) {
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
        nhaCungCapTable.setRowHeight(32);
        nhaCungCapTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        nhaCungCapTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nhaCungCapTable.setGridColor(new Color(220, 210, 195));
        nhaCungCapTable.setShowGrid(true);
        nhaCungCapTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Custom Header Bảng
        nhaCungCapTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
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

        // Căn giữa cột Mã NCC và SĐT
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        nhaCungCapTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        nhaCungCapTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(nhaCungCapTable);
        scrollPane.getViewport().setBackground(ROW_ODD);
        scrollPane.setBorder(BorderFactory.createLineBorder(jollibeeRed, 1));
        add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // 3. EAST PANEL: FORM CHI TIẾT (Thay cho GridLayout cố định)
        // ==========================================
        add(createDetailPanel(), BorderLayout.EAST);

        // Sự kiện Table
        nhaCungCapTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && nhaCungCapTable.getSelectedRow() != -1) {
                int selectedRow = nhaCungCapTable.getSelectedRow();
                int modelRow = nhaCungCapTable.convertRowIndexToModel(selectedRow);
                displayNhaCungCapDetails(modelRow);
            }
        });
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(380, 0)); // Tạo kích thước khung bên phải chuẩn chỉnh
        panel.setOpaque(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        
        TitledBorder formBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(jollibeeRed, 1), "Thông tin chi tiết Nhà cung cấp"
        );
        formBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 14));
        formBorder.setTitleColor(jollibeeRed);
        form.setBorder(formBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaNCC = new JTextField(); txtMaNCC.setEditable(false); txtMaNCC.setBackground(new Color(230, 230, 230));
        txtTenNCC = new JTextField();
        txtDiachi = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();

        Dimension fieldSize = new Dimension(200, 30);
        txtMaNCC.setPreferredSize(fieldSize); txtTenNCC.setPreferredSize(fieldSize);
        txtDiachi.setPreferredSize(fieldSize); txtSDT.setPreferredSize(fieldSize);
        txtEmail.setPreferredSize(fieldSize);

        addField(form, "Mã NCC:", txtMaNCC, 0, gbc);
        addField(form, "Tên NCC:", txtTenNCC, 1, gbc);
        addField(form, "Địa chỉ:", txtDiachi, 2, gbc);
        addField(form, "SĐT:", txtSDT, 3, gbc);
        addField(form, "Email:", txtEmail, 4, gbc);

        panel.add(form, BorderLayout.CENTER);

        // Khối các nút chức năng bên dưới form
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
        btnPanel.setOpaque(false);
        
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");

        styleButton(btnAdd, new Color(40, 167, 69), Color.WHITE); // Xanh lá
        styleButton(btnUpdate, new Color(0, 123, 255), Color.WHITE); // Xanh dương
        styleButton(btnDelete, jollibeeRed, Color.WHITE); // Đỏ thương hiệu
        styleButton(btnClear, darkGray, Color.WHITE); // Xám đậm

        Dimension btnSize = new Dimension(82, 35);
        btnAdd.setPreferredSize(btnSize); btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize); btnClear.setPreferredSize(btnSize);

        btnAdd.addActionListener(e -> addNhaCungCap());
        btnUpdate.addActionListener(e -> updateNhaCungCap());
        btnDelete.addActionListener(e -> deleteNhaCungCap());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addField(JPanel p, String label, JTextField field, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row; p.add(new JLabel(label), gbc);
        gbc.gridx = 1; p.add(field, gbc);
    }

    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg); b.setForeground(fg); b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.putClientProperty("JButton.buttonType", "roundRect");
    }

    // --- Các hàm Logic chính ---
    private void loadNhaCCTable() {
        tableModel.setRowCount(0);
        List<NhaCungCap> list = nhaCungCapDAO.getAllNhaCungCap();
        if (list != null) {
            for (NhaCungCap n : list) {
                tableModel.addRow(new Object[]{n.getMaNCC(), n.getTenNCC(), n.getDiachi(), n.getSdt(), n.getEmail()});
            }
        }
        // Tự động giãn cột bảng nhà cung cấp
        util.UIHelper.autoResizeColumnWidths(nhaCungCapTable);
        clearForm();
    }

    private void displayNhaCungCapDetails(int row) {
        String ma = (String) tableModel.getValueAt(row, 0);
        currentNhaCungCap = nhaCungCapDAO.getNhaCungCapById(ma); 
        if (currentNhaCungCap != null) {
            txtMaNCC.setText(currentNhaCungCap.getMaNCC());
            txtTenNCC.setText(currentNhaCungCap.getTenNCC());
            txtDiachi.setText(currentNhaCungCap.getDiachi());
            txtSDT.setText(currentNhaCungCap.getSdt());
            txtEmail.setText(currentNhaCungCap.getEmail());
            updateButtonState();
        }
    }

    private void clearForm() {
        txtMaNCC.setText(nhaCungCapDAO.generateNextMaNCC()); 
        txtTenNCC.setText(""); txtDiachi.setText(""); txtSDT.setText(""); txtEmail.setText("");
        currentNhaCungCap = null;
        nhaCungCapTable.clearSelection();
        updateButtonState();
    }

    private void updateButtonState() {
        boolean selected = nhaCungCapTable.getSelectedRow() != -1;
        btnAdd.setEnabled(!selected);
        btnUpdate.setEnabled(selected);
        btnDelete.setEnabled(selected);
    }

    private void addNhaCungCap() {
        String ten = txtTenNCC.getText().trim();
        String dc = txtDiachi.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();

        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên NCC và Số điện thoại không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate SĐT 10 số
        if (!sdt.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại nhà cung cấp phải có đúng 10 chữ số!", "Sai định dạng SĐT", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate Email chứa @ và đúng định dạng
        if (!email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ! Email phải chứa ký tự '@' và có định dạng đúng.", "Sai định dạng Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NhaCungCap ncc = new NhaCungCap(txtMaNCC.getText(), ten, dc, sdt, email);
        if (nhaCungCapDAO.addNhaCungCap(ncc)) {
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
            loadNhaCCTable();
        }
    }

    private void updateNhaCungCap() {
        if (currentNhaCungCap == null) return;
        
        String ten = txtTenNCC.getText().trim();
        String dc = txtDiachi.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();

        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên NCC và Số điện thoại không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate SĐT 10 số
        if (!sdt.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại nhà cung cấp phải có đúng 10 chữ số!", "Sai định dạng SĐT", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate Email chứa @ và đúng định dạng
        if (!email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ! Email phải chứa ký tự '@' và có định dạng đúng.", "Sai định dạng Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentNhaCungCap.setTenNCC(ten);
        currentNhaCungCap.setDiachi(dc);
        currentNhaCungCap.setSdt(sdt);
        currentNhaCungCap.setEmail(email);
        
        if (nhaCungCapDAO.updateNhaCungCap(currentNhaCungCap)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
            loadNhaCCTable();
        }
    }

    private void deleteNhaCungCap() {
        if (currentNhaCungCap == null) return;
        
        int choice = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa nhà cung cấp này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (nhaCungCapDAO.deleteNhaCungCap(currentNhaCungCap.getMaNCC())) {
                JOptionPane.showMessageDialog(this, "Xóa nhà cung cấp thành công!");
                loadNhaCCTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa do vướng hóa đơn nhập với nhà cung cấp này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performSearch() {
        String key = txtSearch.getText().trim();
        List<NhaCungCap> res = new ArrayList<>();
        
        if (key.isEmpty()) {
            loadNhaCCTable();
            return;
        }

        if (cbSearchType.getSelectedIndex() == 0) { 
            NhaCungCap n = nhaCungCapDAO.getNhaCungCapById(key);
            if (n != null) res.add(n);
        } else { 
            res = nhaCungCapDAO.searchNhaCungCapByTen(key);
        }
        
        tableModel.setRowCount(0);
        for (NhaCungCap n : res) {
            tableModel.addRow(new Object[]{n.getMaNCC(), n.getTenNCC(), n.getDiachi(), n.getSdt(), n.getEmail()});
        }
        // Tự động giãn cột bảng sau khi tìm kiếm kết quả
        util.UIHelper.autoResizeColumnWidths(nhaCungCapTable);
    }
}