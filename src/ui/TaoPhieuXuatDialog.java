package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
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
        setSize(950, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(255, 253, 240)); // Nền kem Jollibee #FFFDF0

        JPanel pnlHeader = new JPanel(new GridBagLayout());
        pnlHeader.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(224, 31, 42), 1),
            "Thông tin chứng từ xuất kho"
        ));
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
        pnlHeader.add(new JLabel("Nhân viên xuất:"), gbc);
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
        pnlHeader.add(new JLabel("SL / Đơn giá xuất:"), gbc);
        gbc.gridx = 3;
        JPanel pnlInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlInputs.setOpaque(false);
        pnlInputs.add(new JLabel("SL:"));
        txtSoLuong = new JTextField("1", 5);
        pnlInputs.add(txtSoLuong);
        pnlInputs.add(new JLabel("Đơn giá:"));
        txtDonGia = new JTextField("60000", 8);
        pnlInputs.add(txtDonGia);
        pnlHeader.add(pnlInputs, gbc);

        add(pnlHeader, BorderLayout.NORTH);

        String[] columns = {"Mã NL", "Tên Nguyên Liệu", "Số Lượng", "Đơn Giá Xuất (VNĐ)", "Thành Tiền (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTiet = new JTable(tableModel);
        tableChiTiet.setRowHeight(28);
        tableChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableChiTiet.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(180, 30, 45));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 31, 42), 1));
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout(10, 10));
        pnlSouth.setOpaque(false);
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        lblTongTien = new JLabel("TỔNG TIỀN PHIẾU: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongTien.setForeground(new Color(224, 31, 42));
        pnlSouth.add(lblTongTien, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        btnAddRow = new JButton("+ Thêm dòng");
        btnDeleteRow = new JButton("- Xóa dòng");
        btnSave = new JButton("✓ Xác nhận Xuất Kho");

        btnAddRow.setFocusPainted(false);
        btnAddRow.setBorderPainted(false);
        btnAddRow.putClientProperty("JButton.buttonType", "roundRect");

        btnDeleteRow.setFocusPainted(false);
        btnDeleteRow.setBorderPainted(false);
        btnDeleteRow.putClientProperty("JButton.buttonType", "roundRect");

        btnSave.setBackground(new Color(224, 31, 42));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.putClientProperty("JButton.buttonType", "roundRect");

        pnlButtons.add(btnAddRow);
        pnlButtons.add(btnDeleteRow);
        pnlButtons.add(btnSave);
        pnlSouth.add(pnlButtons, BorderLayout.EAST);

        add(pnlSouth, BorderLayout.SOUTH);

        btnAddRow.addActionListener(e -> performAddMaterialRow());
        btnDeleteRow.addActionListener(e -> performDeleteSelectedRow());
        btnSave.addActionListener(e -> performSaveToDatabase());
        
        // Auto load đơn giá từ nguyên liệu được chọn
        cbNguyenLieu.addActionListener(e -> {
            if (cbNguyenLieu.getSelectedItem() != null) {
                String selected = (String) cbNguyenLieu.getSelectedItem();
                String maNL = selected.split(" \\| ")[0];
                NguyenLieu nl = nguyenLieuDAO.getNguyenLieuById(maNL);
                if (nl != null) {
                    // Mặc định đơn giá xuất = giá nhập hoặc cộng thêm tí biên lợi nhuận
                    txtDonGia.setText(String.valueOf(nl.getGianhap()));
                }
            }
        });
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

        try (Connection conn = util.DBConnection.getConnection()) {
            List<CTPhieuXuat> listCT = ctPhieuXuatDAO.getChiTietPhieuXuatByMaPX(conn, viewModeMaPX);
            double totalSum = 0;
            
            tableModel.setRowCount(0);
            if (listCT != null) {
                for (CTPhieuXuat ct : listCT) {
                    NguyenLieu nl = nguyenLieuDAO.getNguyenLieuById(ct.getMaNL());
                    String name = nl != null ? nl.getTenNL() : "Nguyên liệu";
                    
                    double thanhTien = ct.getSoLuong() * ct.getDonGia();

                    tableModel.addRow(new Object[]{
                        ct.getMaNL(),
                        name,
                        ct.getSoLuong(),
                        String.format("%,d", ct.getDonGia()),
                        String.format("%,.0f", thanhTien)
                    });
                    
                    totalSum += thanhTien;
                }
            }
            lblTongTien.setText("TỔNG TIỀN PHIẾU: " + String.format("%,.0f", totalSum) + " VNĐ");
            
            // Tự động giãn cột bảng chi tiết phiếu xuất
            util.UIHelper.autoResizeColumnWidths(tableChiTiet);
            
            // Load header info
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performAddMaterialRow() {
        try {
            if (cbNguyenLieu.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng thêm nguyên liệu vào kho trước!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedNL = (String) cbNguyenLieu.getSelectedItem();
            String maNL = selectedNL.split(" \\| ")[0];
            String tenNL = selectedNL.split(" \\| ")[1];
            
            int soLuongXuat = Integer.parseInt(txtSoLuong.getText().trim());
            int donGiaXuat = Integer.parseInt(txtDonGia.getText().trim());
            
            if (soLuongXuat <= 0 || donGiaXuat <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng và đơn giá phải lớn hơn 0!", "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 1. Kiểm tra tồn kho hiện tại trong DB
            int stockQty = nguyenLieuDAO.getStockQuantity(maNL);
            
            // Tính số lượng đã được thêm trong bảng hiện tại
            int currentAdded = 0;
            int existingRow = -1;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equals(maNL)) {
                    currentAdded = (int) tableModel.getValueAt(i, 2);
                    existingRow = i;
                    break;
                }
            }

            int totalRequested = currentAdded + soLuongXuat;
            if (totalRequested > stockQty) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xuất! Số lượng xuất vượt quá tồn kho hiện tại.\n" +
                    "Tồn kho thực tế của [ " + tenNL + " ] là: " + stockQty + " đơn vị.",
                    "Lỗi vượt hạn mức tồn kho", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Cảnh báo nếu sau khi xuất, tồn kho < 10
            int remainingStock = stockQty - totalRequested;
            if (remainingStock <= 10) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ CẢNH BÁO: Sau khi hoàn thành xuất phiếu này, số lượng tồn kho của \n" +
                    "[ " + tenNL + " ] chỉ còn " + remainingStock + " đơn vị (<= 10)!",
                    "Cảnh báo tồn kho thấp", JOptionPane.WARNING_MESSAGE);
            }

            double thanhTien = totalRequested * donGiaXuat;

            if (existingRow >= 0) {
                tableModel.setValueAt(totalRequested, existingRow, 2);
                tableModel.setValueAt(String.format("%,d", donGiaXuat), existingRow, 3);
                tableModel.setValueAt(String.format("%,.0f", thanhTien), existingRow, 4);
            } else {
                tableModel.addRow(new Object[]{
                    maNL,
                    tenNL,
                    soLuongXuat,
                    String.format("%,d", donGiaXuat),
                    String.format("%,.0f", soLuongXuat * (double) donGiaXuat)
                });
            }

            updateTotalSumLabel();
            // Tự động giãn cột bảng sau khi thêm vật tư xuất
            util.UIHelper.autoResizeColumnWidths(tableChiTiet);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performDeleteSelectedRow() {
        int selectedRow = tableChiTiet.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = tableChiTiet.convertRowIndexToModel(selectedRow);
            tableModel.removeRow(modelRow);
            updateTotalSumLabel();
            // Tự động giãn cột bảng sau khi xóa dòng
            util.UIHelper.autoResizeColumnWidths(tableChiTiet);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng trên bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Phiếu xuất kho trống rỗng! Không thể lưu.", "Thông báo", JOptionPane.WARNING_MESSAGE);
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
        double totalSum = 0;

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
            totalSum += thanhTien;
            
            listCT.add(ct);
        }
        
        px.setTongTien(totalSum);

        boolean result = phieuXuatDAO.savePhieuXuatTransaction(px, listCT);
        
        if (result) {
            // Cập nhật tồn kho thực tế trong NguyenLieu table
            // SQL Server Trigger TRG_XuatKho tự động trừ kho nguyên liệu (NguyenLieu.SoLuong)
            // và tự động cập nhật tổng tiền bảng PhieuXuat. 
            // Ta chỉ cần cập nhật tồn kho phía Java thủ công nếu Trigger không chạy, 
            // nhưng do DB đã được viết Trigger, ta hoàn toàn yên tâm. 
            // Ta thực hiện cập nhật bổ sung qua NguyenLieuDAO để phòng xa:
            try (Connection conn = util.DBConnection.getConnection()) {
                if (conn != null) {
                    conn.setAutoCommit(false);
                    NguyenLieuDAO nlDAO = new NguyenLieuDAO();
                    for (CTPhieuXuat ct : listCT) {
                        nlDAO.updateStockQuantity(conn, ct.getMaNL(), -ct.getSoLuong());
                    }
                    conn.commit();
                }
            } catch (Exception ex) {
                System.err.println("Cảnh báo cập nhật tồn kho phụ: " + ex.getMessage());
            }

            JOptionPane.showMessageDialog(this, "Ghi nhận phiếu xuất kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu phiếu xuất thất bại! Kiểm tra số lượng tồn kho trong hệ thống.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }
}
