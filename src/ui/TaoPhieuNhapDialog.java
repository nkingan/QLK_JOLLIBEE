package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.PhieuNhapDAO;
import dao.ChiTietPhieuNhapDAO;
import dao.NhaCungCapDAO;
import dao.NguyenLieuDAO;
import dao.NhanVienDAO;
import model.PhieuNhap;
import model.ChiTietPhieuNhap;
import model.NhaCungCap;
import model.NguyenLieu;
import model.NhanVien;
import model.TaiKhoan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class TaoPhieuNhapDialog extends JDialog {

    private JTextField txtMaPN;
    private JTextField txtNgayNhap;
    private JComboBox<String> cbNhaCungCap;
    private JComboBox<String> cbNhanVien;
    private JComboBox<String> cbNguyenLieu;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    private JTextField txtHanSuDung; // dd/MM/yyyy
    
    private JButton btnAddRow;
    private JButton btnDeleteRow;
    private JButton btnSave;
    private JTable tableChiTiet;
    private DefaultTableModel tableModel;
    private JLabel lblTongTien;
    private JButton btnExcel;

    private PhieuNhapDAO phieuNhapDAO;
    private ChiTietPhieuNhapDAO ctPhieuNhapDAO;
    private NhaCungCapDAO nhaCungCapDAO;
    private NguyenLieuDAO nguyenLieuDAO;
    private NhanVienDAO nhanVienDAO;
    
    private String viewModeMaPN;
    private boolean isViewMode = false;
    private TaiKhoan currentUser;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Chế độ Tạo mới
    public TaoPhieuNhapDialog(Frame owner, TaiKhoan user) {
        super(owner, "Tạo Phiếu Nhập Kho Mới - Jollibee", true);
        this.currentUser = user;
        this.isViewMode = false;
        initServices();
        initUI();
        loadComboboxData();
        generateAutoMaPN();
        txtNgayNhap.setText(sdf.format(new Date()));
    }

    // Chế độ Xem chi tiết
    public TaoPhieuNhapDialog(Frame owner, String maPN) {
        super(owner, "Chi Tiết Phiếu Nhập Kho: " + maPN, true);
        this.viewModeMaPN = maPN;
        this.isViewMode = true;
        initServices();
        initUI();
        loadComboboxData();
        loadDataViewMode();
    }

    private void initServices() {
        this.phieuNhapDAO = new PhieuNhapDAO();
        this.ctPhieuNhapDAO = new ChiTietPhieuNhapDAO();
        this.nhaCungCapDAO = new NhaCungCapDAO();
        this.nguyenLieuDAO = new NguyenLieuDAO();
        this.nhanVienDAO = new NhanVienDAO();
    }

    private void initUI() {
        setSize(1200, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(255, 252, 245));

        JPanel pnlHeader = new JPanel(new GridBagLayout());
        pnlHeader.setBorder(BorderFactory.createTitledBorder("Thông tin chứng từ nhập kho"));
        pnlHeader.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        pnlHeader.add(new JLabel("Mã phiếu nhập:"), gbc);
        gbc.gridx = 1;
        txtMaPN = new JTextField(12);
        txtMaPN.setEditable(false);
        pnlHeader.add(txtMaPN, gbc);

        gbc.gridx = 2;
        pnlHeader.add(new JLabel("Ngày nhập (dd/MM/yyyy):"), gbc);
        gbc.gridx = 3;
        txtNgayNhap = new JTextField(12);
        pnlHeader.add(txtNgayNhap, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        pnlHeader.add(new JLabel("Nhà cung cấp:"), gbc);
        gbc.gridx = 1;
        cbNhaCungCap = new JComboBox<>();
        pnlHeader.add(cbNhaCungCap, gbc);

        gbc.gridx = 2;
        pnlHeader.add(new JLabel("Nhân viên lập:"), gbc);
        gbc.gridx = 3;
        cbNhanVien = new JComboBox<>();
        pnlHeader.add(cbNhanVien, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        pnlHeader.add(new JLabel("Chọn nguyên liệu:"), gbc);
        gbc.gridx = 1;
        cbNguyenLieu = new JComboBox<>();
        pnlHeader.add(cbNguyenLieu, gbc);

        gbc.gridx = 2;
        pnlHeader.add(new JLabel("SL / Đơn giá / Hạn SD:"), gbc);
        gbc.gridx = 3;
        JPanel pnlInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlInputs.setOpaque(false);
        pnlInputs.add(new JLabel("SL:"));
        txtSoLuong = new JTextField("1", 4);
        pnlInputs.add(txtSoLuong);
        pnlInputs.add(new JLabel("Giá:"));
        txtDonGia = new JTextField("50000", 6);
        pnlInputs.add(txtDonGia);
        pnlInputs.add(new JLabel("HSD:"));
        txtHanSuDung = new JTextField("", 8); // dd/MM/yyyy
        pnlInputs.add(txtHanSuDung);
        pnlHeader.add(pnlInputs, gbc);

        add(pnlHeader, BorderLayout.NORTH);

        String[] columns = {"Mã CTPN", "Mã NL", "Tên Nguyên Liệu", "Số Lượng", "Đơn Giá (VNĐ)", "Hạn Sử Dụng", "Thành Tiền (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


         tableChiTiet = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 240, 230) : new Color(255, 253, 245));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(255, 180, 0, 200)); // highlight vàng Jollibee
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        tableChiTiet.setRowHeight(32);
        tableChiTiet.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableChiTiet.setGridColor(new Color(220, 210, 195));
        tableChiTiet.setShowGrid(true);
        tableChiTiet.setIntercellSpacing(new Dimension(1, 1));
        tableChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tableChiTiet.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                lbl.setBackground(new Color(180, 30, 45));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(227, 29, 43)));
                return lbl;
            }
        });
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);


         DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        for (int col : new int[]{0, 1, 2, 3, 4, 5, 6}) {
            tableChiTiet.getColumnModel().getColumn(col).setCellRenderer(centerRender);
        }

        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout(10, 10));
        pnlSouth.setOpaque(false);
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        lblTongTien = new JLabel("TỔNG TIỀN PHIẾU: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongTien.setForeground(new Color(192,0,0));
        pnlSouth.add(lblTongTien, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        btnAddRow = new JButton("+ Thêm dòng");
        btnDeleteRow = new JButton("- Xóa dòng");
        btnSave = new JButton("✓ Xác nhận & Lưu Kho");

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

        btnExcel = new JButton("Xuất Excel");

btnExcel.setBackground(new Color(40, 167, 69));
btnExcel.setForeground(Color.WHITE);
btnExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));
btnExcel.setFocusPainted(false);
btnExcel.setBorderPainted(false);

pnlButtons.add(btnExcel);

btnExcel.addActionListener(e -> exportExcel());
    }

    private void exportExcel() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Lưu Chi Tiết Phiếu Nhập");
    fileChooser.setSelectedFile(
        new File("ChiTietPhieuNhap_" + txtMaPN.getText() + ".xls")
    );

    if (fileChooser.showSaveDialog(this)
            == JFileChooser.APPROVE_OPTION) {

        File file = fileChooser.getSelectedFile();

        StringBuilder html = new StringBuilder();

        html.append("<html><head><meta charset='UTF-8'></head><body>");

        html.append("<h2 style='color:#E01F2A;text-align:center;'>");
        html.append("CHI TIẾT PHIẾU NHẬP KHO JOLLIBEE");
        html.append("</h2>");

        html.append("<p><b>Mã phiếu nhập:</b> ")
            .append(txtMaPN.getText())
            .append("</p>");

        html.append("<p><b>Ngày nhập:</b> ")
            .append(txtNgayNhap.getText())
            .append("</p>");

        html.append("<p><b>Nhân viên lập:</b> ")
            .append(cbNhanVien.getSelectedItem().toString())
            .append("</p>");

        html.append("<p><b>Nhà cung cấp:</b> ")
            .append(cbNhaCungCap.getSelectedItem().toString())
            .append("</p>");

        html.append("<table border='1' ")
            .append("style='border-collapse:collapse;width:100%;font-family:Arial;'>");

        html.append("<tr style='background:#B41E2D;color:white;'>");

        for (int c = 0; c < tableModel.getColumnCount(); c++) {
            html.append("<th>")
                .append(tableModel.getColumnName(c))
                .append("</th>");
        }

        html.append("</tr>");

        for (int r = 0; r < tableModel.getRowCount(); r++) {

            html.append("<tr>");

            for (int c = 0; c < tableModel.getColumnCount(); c++) {

                Object value = tableModel.getValueAt(r, c);

                String align = "left";

                if (c == 2 || c == 3 || c == 5) {
                    align = "right";
                }

                html.append("<td style='text-align:")
                    .append(align)
                    .append(";'>")
                    .append(value == null ? "" : value.toString())
                    .append("</td>");
            }

            html.append("</tr>");
        }
        html.append("</table>");

        html.append("<h3 style='color:#C00000;'>")
            .append(lblTongTien.getText())
            .append("</h3>");

        html.append("</body></html>");

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw =
                 new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            osw.write(html.toString());
            osw.flush();

            JOptionPane.showMessageDialog(
                this,
                "Xuất Excel thành công!"
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Lỗi xuất Excel: " + ex.getMessage()
            );
        }
    }
}

    private void loadComboboxData() {
        // 1. Load NCC
        cbNhaCungCap.removeAllItems();
        List<NhaCungCap> nccList = nhaCungCapDAO.getAllNhaCungCap();
        if (nccList != null) {
            for (NhaCungCap ncc : nccList) {
                cbNhaCungCap.addItem(ncc.getMaNCC() + " | " + ncc.getTenNCC());
            }
        }

        // 2. Load Nguyen Lieu
        cbNguyenLieu.removeAllItems();
        List<NguyenLieu> nlList = nguyenLieuDAO.getAllNguyenLieu();
        if (nlList != null) {
            for (NguyenLieu nl : nlList) {
                cbNguyenLieu.addItem(nl.getMaNL() + " | " + nl.getTenNL());
            }
        }

        // 3. Load Nhan Vien
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

    private void generateAutoMaPN() {
        if (!isViewMode && phieuNhapDAO != null) {
            String nextCode = phieuNhapDAO.generateNextPhieuNhapCode();
            if (nextCode != null) {
                txtMaPN.setText(nextCode);
            } else {
                txtMaPN.setText("PN01");
            }
        }
    }

    private void loadDataViewMode() {
        txtNgayNhap.setEditable(false);
        cbNhaCungCap.setEnabled(false);
        cbNhanVien.setEnabled(false);
        cbNguyenLieu.setEnabled(false);
        txtSoLuong.setEditable(false);
        txtDonGia.setEditable(false);
        txtHanSuDung.setEditable(false);
        
        btnAddRow.setEnabled(false);
        btnDeleteRow.setEnabled(false);
        btnSave.setEnabled(false);

        if (viewModeMaPN != null) {
            txtMaPN.setText(viewModeMaPN);
        }

        List<ChiTietPhieuNhap> listCT = ctPhieuNhapDAO.getChiTietPhieuNhapByMaPN(viewModeMaPN);
        BigDecimal totalSum = BigDecimal.ZERO;
        
        tableModel.setRowCount(0);
        if (listCT != null) {
            for (ChiTietPhieuNhap ct : listCT) {
                String donGiaStr = (ct.getDonGia() != null) ? String.format("%,.0f", ct.getDonGia().doubleValue()) : "0";
                String thanhTienStr = (ct.getThanhTien() != null) ? String.format("%,.0f", ct.getThanhTien().doubleValue()) : "0";
                String hsdStr = (ct.getHanSuDung() != null) ? sdf.format(ct.getHanSuDung()) : "Không có";

                tableModel.addRow(new Object[]{
                    ct.getMaCTPN(),
                    ct.getMaNL(),
                    (ct.getTenNL() != null) ? ct.getTenNL() : "Nguyên liệu",
                    ct.getSoLuong(),
                    donGiaStr,
                    hsdStr,
                    thanhTienStr
                });
                
                if (ct.getThanhTien() != null) {
                    totalSum = totalSum.add(ct.getThanhTien());
                }
            }
        }
        lblTongTien.setText("TỔNG TIỀN PHIẾU: " + String.format("%,.0f", totalSum.doubleValue()) + " VNĐ");
        
        // Tự động giãn cột bảng chi tiết phiếu nhập
        util.UIHelper.autoResizeColumnWidths(tableChiTiet);
        
        // Load additional info (NgayNhap, NhanVien, NCC)
        List<PhieuNhap> all = phieuNhapDAO.getAllPhieuNhap();
        for (PhieuNhap pn : all) {
            if (pn.getMaPN().equals(viewModeMaPN)) {
                txtNgayNhap.setText(pn.getNgayNhap() != null ? sdf.format(pn.getNgayNhap()) : "");
                for (int i = 0; i < cbNhaCungCap.getItemCount(); i++) {
                    if (cbNhaCungCap.getItemAt(i).contains(pn.getTenNCC())) {
                        cbNhaCungCap.setSelectedIndex(i);
                        break;
                    }
                }
                for (int i = 0; i < cbNhanVien.getItemCount(); i++) {
                    if (cbNhanVien.getItemAt(i).contains(pn.getTenNV())) {
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
                JOptionPane.showMessageDialog(this, "Vui lòng thêm nguyên liệu vào kho trước!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedNL = (String) cbNguyenLieu.getSelectedItem();
            String maNL = selectedNL.split(" \\| ")[0];
            String tenNL = selectedNL.split(" \\| ")[1];
            
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            double donGia = Double.parseDouble(txtDonGia.getText().trim());
            String hsdStr = txtHanSuDung.getText().trim();
            
            if (soLuong <= 0 || donGia <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng và đơn giá nhập vào phải lớn hơn 0!", "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check HSD format if provided
            if (!hsdStr.isEmpty()) {
                try {
                    sdf.parse(hsdStr);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Hạn sử dụng không đúng định dạng dd/MM/yyyy!", "Định dạng ngày sai", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                hsdStr = "Không có";
            }

            double thanhTien = soLuong * donGia;

            // If already exists, update row
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 1).toString().equals(maNL)) {
                    int currentSL = (int) tableModel.getValueAt(i, 3);
                    int newSL = currentSL + soLuong;
                    double newThanhTien = newSL * donGia;
                    
                    tableModel.setValueAt(newSL, i, 3);
                    tableModel.setValueAt(String.format("%,.0f", donGia), i, 4);
                    tableModel.setValueAt(hsdStr, i, 5);
                    tableModel.setValueAt(String.format("%,.0f", newThanhTien), i, 6);
                    
                    updateTotalSumLabel();
                    return;
                }
            }

            tableModel.addRow(new Object[]{
                "", // Will be populated dynamically by resequenceMaCTPN
                maNL,
                tenNL,
                soLuong,
                String.format("%,.0f", donGia),
                hsdStr,
                String.format("%,.0f", thanhTien)
            });

            resequenceMaCTPN();
            updateTotalSumLabel();
            // Tự động giãn cột bảng chi tiết phiếu nhập sau khi thêm hàng mới
            util.UIHelper.autoResizeColumnWidths(tableChiTiet);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập định dạng số hợp lệ!", "Sai định dạng số", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performDeleteSelectedRow() {
        int selectedRow = tableChiTiet.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = tableChiTiet.convertRowIndexToModel(selectedRow);
            tableModel.removeRow(modelRow);
            resequenceMaCTPN();
            updateTotalSumLabel();
            // Tự động giãn cột bảng chi tiết phiếu nhập sau khi xóa hàng
            util.UIHelper.autoResizeColumnWidths(tableChiTiet);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng trên bảng để tiến hành xóa vật tư!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateTotalSumLabel() {
        double sum = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String thanhTienStr = tableModel.getValueAt(i, 6).toString().replaceAll("[^0-9]", "");
            sum += Double.parseDouble(thanhTienStr);
        }
        lblTongTien.setText("TỔNG TIỀN PHIẾU: " + String.format("%,.0f", sum) + " VNĐ");
    }

    private void resequenceMaCTPN() {
        String maPN = txtMaPN.getText().trim();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String newMaCTPN = maPN + "_" + String.format("%02d", i + 1);
            tableModel.setValueAt(newMaCTPN, i, 0);
        }
    }

    private void performSaveToDatabase() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Phiếu nhập kho trống rỗng! Không thể lưu hóa đơn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ngayNhapStr = txtNgayNhap.getText().trim();
        Date ngayNhap;
        try {
            ngayNhap = sdf.parse(ngayNhapStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày nhập không đúng định dạng dd/MM/yyyy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PhieuNhap pn = new PhieuNhap();
        pn.setMaPN(txtMaPN.getText().trim());
        pn.setNgayNhap(ngayNhap);
        
        String selectedNCC = (String) cbNhaCungCap.getSelectedItem();
        pn.setMaNCC(selectedNCC.split(" \\| ")[0]);

        String selectedNV = (String) cbNhanVien.getSelectedItem();
        pn.setMaNV(selectedNV.split(" \\| ")[0]);

        List<ChiTietPhieuNhap> listCT = new ArrayList<>();
        BigDecimal totalMoney = BigDecimal.ZERO;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
            
            // Format auto-generated MaCTPN: PN001_01, PN001_02,...
            String maCTPN = tableModel.getValueAt(i, 0).toString();
            ct.setMaCTPN(maCTPN);
            ct.setMaPN(pn.getMaPN());
            ct.setMaNL(tableModel.getValueAt(i, 1).toString());
            
            int soLuong = (int) tableModel.getValueAt(i, 3);
            ct.setSoLuong(soLuong);
            
            String giaStr = tableModel.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
            BigDecimal donGia = new BigDecimal(giaStr);
            ct.setDonGia(donGia);
            
            String hsdStr = tableModel.getValueAt(i, 5).toString();
            if (!hsdStr.equals("Không có")) {
                try {
                    ct.setHanSuDung(sdf.parse(hsdStr));
                } catch (ParseException e) {
                    ct.setHanSuDung(null);
                }
            } else {
                ct.setHanSuDung(null);
            }

            BigDecimal thanhTien = donGia.multiply(new BigDecimal(soLuong));
            ct.setThanhTien(thanhTien);

            totalMoney = totalMoney.add(thanhTien);
            listCT.add(ct);
        }
        pn.setTongTien(totalMoney);

        boolean result = phieuNhapDAO.savePhieuNhapTransaction(pn, listCT);
        
        if (result) {
            JOptionPane.showMessageDialog(this, "Ghi nhận phiếu nhập kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thất bại!", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performExportExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file Excel (CSV)");
        fileChooser.setSelectedFile(new java.io.File("PhieuNhap_" + txtMaPN.getText().trim() + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }
            
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath);
                 java.io.OutputStreamWriter osw = new java.io.OutputStreamWriter(fos, java.nio.charset.StandardCharsets.UTF_8)) {
                
                // Write UTF-8 BOM
                fos.write(0xEF);
                fos.write(0xBB);
                fos.write(0xBF);
                
                // Title
                osw.write("CHI TIẾT PHIẾU NHẬP KHO\n");
                osw.write("Mã phiếu nhập," + txtMaPN.getText().trim() + "\n");
                osw.write("Ngày nhập," + txtNgayNhap.getText().trim() + "\n");
                
                String ncc = cbNhaCungCap.getSelectedItem() != null ? cbNhaCungCap.getSelectedItem().toString() : "";
                osw.write("Nhà cung cấp,\"" + ncc.replace("\"", "\"\"") + "\"\n");
                
                String nv = cbNhanVien.getSelectedItem() != null ? cbNhanVien.getSelectedItem().toString() : "";
                osw.write("Nhân viên lập,\"" + nv.replace("\"", "\"\"") + "\"\n");
                
                osw.write("Tổng tiền phiếu,\"" + lblTongTien.getText().replace("TỔNG TIỀN PHIẾU: ", "").replace(" VNĐ", "") + "\"\n\n");
                
                // Table Header
                osw.write("Mã CTPN,Mã NL,Tên Nguyên Liệu,Số Lượng,Đơn Giá (VNĐ),Hạn Sử Dụng,Thành Tiền (VNĐ)\n");
                
                // Rows
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String maCTPN = tableModel.getValueAt(i, 0).toString();
                    String maNL = tableModel.getValueAt(i, 1).toString();
                    String tenNL = tableModel.getValueAt(i, 2).toString();
                    String soLuong = tableModel.getValueAt(i, 3).toString();
                    String donGia = tableModel.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
                    String hsd = tableModel.getValueAt(i, 5).toString();
                    String thanhTien = tableModel.getValueAt(i, 6).toString().replaceAll("[^0-9]", "");
                    
                    osw.write(String.format("%s,%s,\"%s\",%s,%s,%s,%s\n", 
                        maCTPN, maNL, tenNL.replace("\"", "\"\""), soLuong, donGia, hsd, thanhTien));
                }
                
                JOptionPane.showMessageDialog(this, "Xuất file Excel (CSV) thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}