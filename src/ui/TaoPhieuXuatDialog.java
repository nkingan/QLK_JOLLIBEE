package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.PhieuXuatDAO;
import dao.CTPhieuXuatDAO;
import dao.NguyenLieuDAO;
import dao.NhanVienDAO;
import model.PhieuXuat;
import model.CTPhieuXuat;
import model.NguyenLieu;
import model.NhanVien;
import model.TaiKhoan;

public class TaoPhieuXuatDialog extends JDialog {

    private JTextField txtMaPX;
    private JTextField txtNgayXuat;
    private JComboBox<String> cbNhanVien;
    private JComboBox<String> cbNguyenLieu;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    
    private JButton btnAddRow;
    private JButton btnDeleteRow;
    private JButton btnSave;
    private JTable tableChiTiet;
    private DefaultTableModel tableModel;
    private JLabel lblTongTien;

    private PhieuXuatDAO phieuXuatDAO;
    private CTPhieuXuatDAO ctPhieuXuatDAO;
    private NguyenLieuDAO nguyenLieuDAO;
    private NhanVienDAO nhanVienDAO;
    
    private String viewModeMaPX;
    private boolean isViewMode = false;
    private TaiKhoan currentUser;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Chế độ Tạo mới
    public TaoPhieuXuatDialog(Frame owner, TaiKhoan user) {
        super(owner, "Tạo Phiếu Xuất Kho Mới - Jollibee", true);
        this.currentUser = user;
        this.isViewMode = false;
        initServices();
        initUI();
        loadComboboxData();
        generateAutoMaPX();
        txtNgayXuat.setText(sdf.format(new Date()));
    }

    // Chế độ Xem chi tiết
    public TaoPhieuXuatDialog(Frame owner, String maPX) {
        super(owner, "Chi Tiết Phiếu Xuất Kho: " + maPX, true);
        this.viewModeMaPX = maPX;
        this.isViewMode = true;
        initServices();
        initUI();
        loadComboboxData();
        loadDataViewMode();
    }

    private void initServices() {
        this.phieuXuatDAO = new PhieuXuatDAO();
        this.ctPhieuXuatDAO = new CTPhieuXuatDAO();
        this.nguyenLieuDAO = new NguyenLieuDAO();
        this.nhanVienDAO = new NhanVienDAO();
    }

    private void initUI() {
        setSize(900, 550);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(255, 252, 245));

        JPanel pnlHeader = new JPanel(new GridBagLayout());
        pnlHeader.setBorder(BorderFactory.createTitledBorder("Thông tin chứng từ xuất kho"));
        pnlHeader.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        pnlHeader.add(new JLabel("Mã phiếu xuất:"), gbc);
        gbc.gridx = 1;
        txtMaPX = new JTextField(12);
        txtMaPX.setEditable(false);
        pnlHeader.add(txtMaPX, gbc);

        gbc.gridx = 2;
        pnlHeader.add(new JLabel("Ngày xuất (dd/MM/yyyy):"), gbc);
        gbc.gridx = 3;
        txtNgayXuat = new JTextField(12);
        pnlHeader.add(txtNgayXuat, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        pnlHeader.add(new JLabel("Nhân viên lập:"), gbc);
        gbc.gridx = 1;
        cbNhanVien = new JComboBox<>();
        pnlHeader.add(cbNhanVien, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        pnlHeader.add(new JLabel("Chọn nguyên liệu:"), gbc);
        gbc.gridx = 1;
        cbNguyenLieu = new JComboBox<>();
        pnlHeader.add(cbNguyenLieu, gbc);

        gbc.gridx = 2;
        pnlHeader.add(new JLabel("Số lượng & Đơn giá:"), gbc);
        gbc.gridx = 3;
        JPanel pnlInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlInputs.setOpaque(false);
        pnlInputs.add(new JLabel("SL:"));
        txtSoLuong = new JTextField("1", 4);
        pnlInputs.add(txtSoLuong);
        pnlInputs.add(new JLabel("Giá xuất:"));
        txtDonGia = new JTextField("60000", 6);
        pnlInputs.add(txtDonGia);
        pnlHeader.add(pnlInputs, gbc);

        add(pnlHeader, BorderLayout.NORTH);

        String[] columns = {"Mã NL", "Tên Nguyên Liệu", "Số Lượng", "Đơn Giá (VNĐ)", "Thành Tiền (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTiet = new JTable(tableModel);
        tableChiTiet.setRowHeight(25);
        tableChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableChiTiet.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(139, 69, 19));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout(10, 10));
        pnlSouth.setOpaque(false);
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        lblTongTien = new JLabel("TỔNG TIỀN PHIẾU: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongTien.setForeground(new Color(192, 0, 0));
        pnlSouth.add(lblTongTien, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        btnAddRow = new JButtonCustom("+ Thêm dòng", new Color(139, 69, 19), Color.WHITE);
        btnDeleteRow = new JButtonCustom("- Xóa dòng", new Color(45, 45, 45), Color.WHITE);
        btnSave = new JButtonCustom("✓ Xác nhận & Lưu Kho", new Color(224, 31, 42), Color.WHITE);

        pnlButtons.add(btnAddRow);
        pnlButtons.add(btnDeleteRow);
        pnlButtons.add(btnSave);
        pnlSouth.add(pnlButtons, BorderLayout.EAST);


        add(pnlSouth, BorderLayout.SOUTH);

        btnAddRow.addActionListener(e -> performAddMaterialRow());
        btnDeleteRow.addActionListener(e -> performDeleteSelectedRow());
        btnSave.addActionListener(e -> performSaveToDatabase());
    }

    private void loadComboboxData() {
        // 1. Load Nguyen Lieu
        cbNguyenLieu.removeAllItems();
        List<NguyenLieu> nlList = nguyenLieuDAO.getAllNguyenLieu();
        if (nlList != null) {
            for (NguyenLieu nl : nlList) {
                cbNguyenLieu.addItem(nl.getMaNL() + " | " + nl.getTenNL());
            }
        }

        // 2. Load Nhan Vien
        cbNhanVien.removeAllItems();
        List<NhanVien> nvList = nhanVienDAO.getAllNhanVien();
        if (nvList != null) {
            for (NhanVien nv : nvList) {
                cbNhanVien.addItem(nv.getMaNV() + " | " + nv.getTenNV());
            }
        }

        // Select current logged in employee by default if matching
        if (currentUser != null && currentUser.getMaNV() != null) {
            for (int i = 0; i < cbNhanVien.getItemCount(); i++) {
                if (cbNhanVien.getItemAt(i).startsWith(currentUser.getMaNV())) {
                    cbNhanVien.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void generateAutoMaPX() {
        if (!isViewMode && phieuXuatDAO != null) {
            String nextCode = phieuXuatDAO.generateNextPhieuXuatCode();
            if (nextCode != null) {
                txtMaPX.setText(nextCode);
            } else {
                txtMaPX.setText("PX01");
            }
        }
    }

    private void loadDataViewMode() {
        txtNgayXuat.setEditable(false);
        cbNhanVien.setEnabled(false);
        cbNguyenLieu.setEnabled(false);
        txtSoLuong.setEditable(false);
        txtDonGia.setEditable(false);
        
        btnAddRow.setEnabled(false);
        btnDeleteRow.setEnabled(false);
        btnSave.setEnabled(false);

        if (viewModeMaPX != null) {
            txtMaPX.setText(viewModeMaPX);
        }

        List<CTPhieuXuat> listCT = ctPhieuXuatDAO.getChiTietByMaPX(viewModeMaPX);
        double totalSum = 0;
        
        tableModel.setRowCount(0);
        if (listCT != null) {
            for (CTPhieuXuat ct : listCT) {
                String donGiaStr = String.format("%,d", ct.getDonGia());
                String thanhTienStr = String.format("%,d", ct.getThanhTien());

                tableModel.addRow(new Object[]{
                    ct.getMaNL(),
                    (ct.getTenNL() != null) ? ct.getTenNL() : "Nguyên liệu",
                    ct.getSoLuong(),
                    donGiaStr,
                    thanhTienStr
                });
                totalSum += ct.getThanhTien();
            }
        }
        lblTongTien.setText("TỔNG TIỀN PHIẾU: " + String.format("%,.0f", totalSum) + " VNĐ");
        
        // Load additional info (NgayXuat, NhanVien)
        List<PhieuXuat> all = phieuXuatDAO.getAllPhieuXuat();
        for (PhieuXuat px : all) {
            if (px.getMaPX().equals(viewModeMaPX)) {
                txtNgayXuat.setText(px.getNgayXuat() != null ? sdf.format(px.getNgayXuat()) : "");
                for (int i = 0; i < cbNhanVien.getItemCount(); i++) {
                    if (cbNhanVien.getItemAt(i).contains(px.getTenNV())) {
                        cbNhanVien.setSelectedIndex(i);
                        break;
                    }
                }
                break;
            }
        }
    }

    private void performAddMaterialRow() {
        try {
            if (cbNguyenLieu.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Không có nguyên liệu nào trong kho!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedNL = (String) cbNguyenLieu.getSelectedItem();
            String maNL = selectedNL.split(" \\| ")[0];
            String tenNL = selectedNL.split(" \\| ")[1];
            
            int soLuongXuat = Integer.parseInt(txtSoLuong.getText().trim());
            int donGia = Integer.parseInt(txtDonGia.getText().trim());
            
            if (soLuongXuat <= 0 || donGia <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng và đơn giá xuất phải lớn hơn 0!", "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Real-time stock validation
            int currentStock = nguyenLieuDAO.getStockQuantity(maNL);
            
            // Check if already in table to accumulate
            int existingQtyInTable = 0;
            int existingRowIndex = -1;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equals(maNL)) {
                    existingQtyInTable = (int) tableModel.getValueAt(i, 2);
                    existingRowIndex = i;
                    break;
                }
            }

            int totalTargetXuat = existingQtyInTable + soLuongXuat;
            if (totalTargetXuat > currentStock) {
                JOptionPane.showMessageDialog(this, 
                        "Lượng tồn kho hiện tại không đủ để xuất!\n" +
                        "Tồn kho thực tế: " + currentStock + " | Lượng yêu cầu: " + totalTargetXuat,
                        "Không đủ hàng tồn", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Low stock warning (if remaining after export is < 10)
            int remainingStock = currentStock - totalTargetXuat;
            if (remainingStock < 10) {
                JOptionPane.showMessageDialog(this,
                        "CẢNH BÁO: Số lượng nguyên liệu này trong kho sau khi xuất (" + remainingStock + ") sẽ giảm xuống dưới 10 đơn vị!",
                        "Cảnh báo tồn kho thấp", JOptionPane.WARNING_MESSAGE);
            }

            double thanhTien = totalTargetXuat * donGia;

            if (existingRowIndex >= 0) {
                tableModel.setValueAt(totalTargetXuat, existingRowIndex, 2);
                tableModel.setValueAt(String.format("%,d", donGia), existingRowIndex, 3);
                tableModel.setValueAt(String.format("%,.0f", thanhTien), existingRowIndex, 4);
            } else {
                tableModel.addRow(new Object[]{
                    maNL,
                    tenNL,
                    soLuongXuat,
                    String.format("%,d", donGia),
                    String.format("%,.0f", thanhTien)
                });
            }

            updateTotalSumLabel();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập định dạng số hợp lệ!", "Sai định dạng số", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performDeleteSelectedRow() {
        int selectedRow = tableChiTiet.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            updateTotalSumLabel();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng trên bảng để tiến hành xóa vật tư!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateTotalSumLabel() {
        double sum = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String thanhTienStr = tableModel.getValueAt(i, 4).toString().replace(",", "");
            sum += Double.parseDouble(thanhTienStr);
        }
        lblTongTien.setText("TỔNG TIỀN PHIẾU: " + String.format("%,.0f", sum) + " VNĐ");
    }

    private void performSaveToDatabase() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Phiếu xuất kho trống rỗng! Không thể lưu hóa đơn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ngayXuatStr = txtNgayXuat.getText().trim();
        Date ngayXuat;
        try {
            ngayXuat = sdf.parse(ngayXuatStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày xuất không đúng định dạng dd/MM/yyyy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PhieuXuat px = new PhieuXuat();
        px.setMaPX(txtMaPX.getText().trim());
        px.setNgayXuat(ngayXuat);
        
        String selectedNV = (String) cbNhanVien.getSelectedItem();
        px.setMaNV(selectedNV.split(" \\| ")[0]);

        List<CTPhieuXuat> listCT = new ArrayList<>();
        double totalMoney = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            CTPhieuXuat ct = new CTPhieuXuat();
            ct.setMaPX(px.getMaPX());
            ct.setMaNL(tableModel.getValueAt(i, 0).toString());
            
            int soLuong = (int) tableModel.getValueAt(i, 2);
            ct.setSoLuong(soLuong);
            
            String giaStr = tableModel.getValueAt(i, 3).toString().replace(",", "");
            int donGia = Integer.parseInt(giaStr);
            ct.setDonGia(donGia);
            
            int thanhTien = soLuong * donGia;
            ct.setThanhTien(thanhTien);

            totalMoney += thanhTien;
            listCT.add(ct);
        }
        px.setTongTien(totalMoney);

        boolean result = phieuXuatDAO.savePhieuXuatTransaction(px, listCT);
        
        if (result) {
            JOptionPane.showMessageDialog(this, "Ghi nhận phiếu xuất kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu phiếu xuất thất bại! Hãy chắc chắn lượng hàng tồn trong kho còn đủ.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }
}
