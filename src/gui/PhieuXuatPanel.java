package gui;

import model.ChiTietPhieuXuat;
import model.PhieuXuat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
public class PhieuXuatPanel extends JPanel {
    
    private String currentUser;

    private DefaultTableModel modelPX, modelCTPX;
    private JTable tablePX, tableCTPX;
    
    private JTextField txtPX_Ma, txtPX_Ngay, txtPX_MaNV;
    private JTextField txtCTPX_Ma, txtCTPX_MaNL, txtCTPX_SL, txtCTPX_DG;
    private JButton btnTaoPX, btnXoaPX, btnLMPX, btnThemCT, btnLuuPhieu, btnInPhieu;
    private JLabel lblTongTien;

    // Danh sách lưu tạm thời các mặt hàng chờ xuất trước khi lưu xuống SQL Server
    private List<ChiTietPhieuXuat> dsChiTietTamThoi;
    private BigDecimal tongTienTamTinh = BigDecimal.ZERO;

    private final Color JB_RED = new Color(214, 24, 34);       
    private final Color JB_YELLOW = new Color(254, 192, 6);    
    private final Color BG_PANEL = new Color(244, 245, 247);   
    private final Color TEXT_DARK = new Color(50, 50, 50);     
    private final Color BORDER_COLOR = new Color(218, 222, 229);

    private DecimalFormat currencyFormat = new DecimalFormat("#,##0");

    // =========================================================
    // CẤU HÌNH KẾT NỐI SQL SERVER TRỰC TIẾP (SA / 123456)
    // =========================================================
    public static Connection getConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        // Thay 'JollibeeDB' bằng tên Database chính xác của bạn trong SQL Server
        String url = "jdbc:sqlserver://localhost:1433;databaseName=JollibeeDB;"
                   + "user=sa;password=123456;encrypt=true;trustServerCertificate=true;";
        return DriverManager.getConnection(url);
    }

    public PhieuXuatPanel(String currentUser) {
        this.currentUser = currentUser;
        this.dsChiTietTamThoi = new ArrayList<>();
        
        initComponents();
        loadDataPhieuXuat(); // Tải lịch sử phiếu từ SQL khi mở giao diện
        generateNewSessionCode();
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBackground(BG_PANEL);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        Font fontTitle = new Font("Segoe UI", Font.BOLD, 13);
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 12);

        // =========================================================
        // 1. KHU VỰC TRÊN: THÔNG TIN PHIẾU XUẤT CHA
        // =========================================================
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BG_PANEL);

        JPanel formPX = new JPanel(new GridBagLayout());
        formPX.setBackground(Color.WHITE);
        formPX.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(JB_RED, 1, true), 
                " THÔNG TIN PHIẾU XUẤT GỐC ", 
                TitledBorder.LEADING, TitledBorder.TOP, fontTitle, JB_RED));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtPX_Ma = createModernTextField(false);
        txtPX_Ngay = createModernTextField(false);
        txtPX_Ngay.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtPX_MaNV = createModernTextField(false);
        txtPX_MaNV.setText(currentUser);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; formPX.add(createLabel("Mã Phiếu:", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPX.add(txtPX_Ma, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; formPX.add(createLabel("Ngày Xuất:", fontLabel), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; formPX.add(txtPX_Ngay, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; formPX.add(createLabel("Nhân Viên Lập:", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; formPX.add(txtPX_MaNV, gbc);

        JPanel buttonsPX = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonsPX.setBackground(BG_PANEL);
        btnTaoPX = createModernButton("Khởi Tạo Phiếu", JB_RED, Color.WHITE);
        btnXoaPX = createModernButton("Xóa Phiếu DB", new Color(108, 117, 125), Color.WHITE);
        btnLMPX  = createModernButton("Làm Mới Form", new Color(220, 224, 230), TEXT_DARK);
        buttonsPX.add(btnTaoPX); buttonsPX.add(btnXoaPX); buttonsPX.add(btnLMPX);

        topPanel.add(formPX, BorderLayout.CENTER);
        topPanel.add(buttonsPX, BorderLayout.SOUTH);

        // =========================================================
        // 2. KHU VỰC TRUNG TÂM: LƯỚI BẢNG DỮ LIỆU
        // =========================================================
        modelPX = new DefaultTableModel(new String[]{"Mã Phiếu Xuất", "Ngày Xuất Kho", "Người Lập", "Tổng Tiền Hóa Đơn"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePX = createModernTable(modelPX);

        modelCTPX = new DefaultTableModel(new String[]{"Mã Chi Tiết", "Mã Nguyên Liệu", "Số Lượng Xuất", "Đơn Giá", "Thành Tiền"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCTPX = createModernTable(modelCTPX);

        JScrollPane scrollPX = new JScrollPane(tablePX);
        scrollPX.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "Lịch Sử Danh Sách Phiếu Đã Lưu Hệ Thống", TitledBorder.LEADING, TitledBorder.TOP, fontTitle, TEXT_DARK));
        
        JScrollPane scrollCTPX = new JScrollPane(tableCTPX);
        scrollCTPX.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "Chi Tiết Mặt Hàng Nguyên Liệu", TitledBorder.LEADING, TitledBorder.TOP, fontTitle, TEXT_DARK));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPX, scrollCTPX);
        splitPane.setDividerLocation(180);
        splitPane.setBorder(null);

        // =========================================================
        // 3. KHU VỰC DƯỚI: NHẬP MẶT HÀNG & TÍNH TỔNG TIỀN FOOTER
        // =========================================================
        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBackground(BG_PANEL);

        JPanel formCT = new JPanel(new GridBagLayout());
        formCT.setBackground(Color.WHITE);
        formCT.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(JB_YELLOW, 1, true), 
                " THÊM NGUYÊN LIỆU CHI TIẾT VÀO DANH SÁCH CHỜ ", 
                TitledBorder.LEADING, TitledBorder.TOP, fontTitle, TEXT_DARK));
        
        txtCTPX_Ma = createModernTextField(false);
        txtCTPX_MaNL = createModernTextField(true);
        txtCTPX_SL = createModernTextField(true);
        txtCTPX_DG = createModernTextField(true);

        GridBagConstraints gbcCT = new GridBagConstraints();
        gbcCT.insets = new Insets(8, 12, 8, 12);
        gbcCT.fill = GridBagConstraints.HORIZONTAL;

        gbcCT.gridx = 0; gbcCT.gridy = 0; gbcCT.weightx = 0; formCT.add(createLabel("Mã CTPX:", fontLabel), gbcCT);
        gbcCT.gridx = 1; gbcCT.gridy = 0; gbcCT.weightx = 1.0; formCT.add(txtCTPX_Ma, gbcCT);
        gbcCT.gridx = 2; gbcCT.gridy = 0; gbcCT.weightx = 0; formCT.add(createLabel("Mã Nguyên Liệu:", fontLabel), gbcCT);
        gbcCT.gridx = 3; gbcCT.gridy = 0; gbcCT.weightx = 1.0; formCT.add(txtCTPX_MaNL, gbcCT);
        gbcCT.gridx = 0; gbcCT.gridy = 1; gbcCT.weightx = 0; formCT.add(createLabel("Số Lượng:", fontLabel), gbcCT);
        gbcCT.gridx = 1; gbcCT.gridy = 1; gbcCT.weightx = 1.0; formCT.add(txtCTPX_SL, gbcCT);
        gbcCT.gridx = 2; gbcCT.gridy = 1; gbcCT.weightx = 0; formCT.add(createLabel("Đơn Giá Xuất:", fontLabel), gbcCT);
        gbcCT.gridx = 3; gbcCT.gridy = 1; gbcCT.weightx = 1.0; formCT.add(txtCTPX_DG, gbcCT);

        JPanel footerInfoPanel = new JPanel(new BorderLayout());
        footerInfoPanel.setBackground(BG_PANEL);
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        totalPanel.setBackground(BG_PANEL);
        totalPanel.add(createLabel("TỔNG TIỀN TẠM TÍNH:", fontTitle));
        lblTongTien = createLabel("0 đ", new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setForeground(JB_RED);
        totalPanel.add(lblTongTien);
        footerInfoPanel.add(totalPanel, BorderLayout.NORTH);

        JPanel buttonsCT = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonsCT.setBackground(BG_PANEL);
        btnThemCT = createModernButton("+ Thêm Vào Grid Chờ", JB_YELLOW, TEXT_DARK);
        btnLuuPhieu = createModernButton("💾 Lưu Toàn Bộ Phiếu", new Color(46, 139, 87), Color.WHITE);
        btnInPhieu = createModernButton("🖨️ Xuất PDF / In Phiếu", new Color(0, 102, 204), Color.WHITE); 
        
        btnLuuPhieu.setPreferredSize(new Dimension(180, 35));
        btnInPhieu.setPreferredSize(new Dimension(170, 35));
        
        buttonsCT.add(btnThemCT); buttonsCT.add(btnLuuPhieu); buttonsCT.add(btnInPhieu);
        footerInfoPanel.add(buttonsCT, BorderLayout.SOUTH);

        bottomPanel.add(formCT, BorderLayout.CENTER);
        bottomPanel.add(footerInfoPanel, BorderLayout.SOUTH);

        // =========================================================
        // XỬ LÝ SỰ KIỆN TƯƠNG TÁC SQL SERVER TRỰC TIẾP
        // =========================================================
        
        // 1. Click dòng lịch sử phiếu -> Tải chi tiết nguyên liệu từ SQL Server
        tablePX.getSelectionModel().addListSelectionListener(e -> {
            int r = tablePX.getSelectedRow();
            if (r >= 0 && !e.getValueIsAdjusting()) {
                String maPX = modelPX.getValueAt(r, 0).toString();
                txtPX_Ma.setText(maPX);
                btnLuuPhieu.setEnabled(false); 
                btnThemCT.setEnabled(false);
                btnInPhieu.setEnabled(true); 
                loadDataChiTietFromSQL(maPX);
            }
        });

        btnTaoPX.addActionListener(e -> generateNewSessionCode());

        // 2. Xóa phiếu xuất khỏi hệ thống DB SQL Server (Xóa cả gốc và chi tiết liên quan)
        btnXoaPX.addActionListener(e -> {
            int r = tablePX.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng phiếu cần xóa dưới hệ thống DB!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maPX = modelPX.getValueAt(r, 0).toString();
            if (JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa phiếu [" + maPX + "] khỏi SQL Server không?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                
                String sqlDeleteCT = "DELETE FROM ChiTietPhieuXuat WHERE MaPX = ?";
                String sqlDeletePX = "DELETE FROM PhieuXuat WHERE MaPX = ?";
                
                try (Connection conn = getConnection();
                     PreparedStatement pstCT = conn.prepareStatement(sqlDeleteCT);
                     PreparedStatement pstPX = conn.prepareStatement(sqlDeletePX)) {
                    
                    // Thực hiện cơ chế transaction thủ công nhằm đảm bảo an toàn dữ liệu
                    conn.setAutoCommit(false);
                    try {
                        pstCT.setString(1, maPX);
                        pstCT.executeUpdate();
                        
                        pstPX.setString(1, maPX);
                        pstPX.executeUpdate();
                        
                        conn.commit();
                        JOptionPane.showMessageDialog(this, "Đã xóa hoàn toàn dữ liệu phiếu " + maPX + " trong hệ thống SQL Server!");
                    } catch (Exception ex) {
                        conn.rollback();
                        throw ex;
                    }
                    
                    loadDataPhieuXuat();
                    modelCTPX.setRowCount(0);
                    clearFields();
                    generateNewSessionCode();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLMPX.addActionListener(e -> {
            clearFields();
            tablePX.clearSelection();
            dsChiTietTamThoi.clear();
            renderGridTamThoi();
            generateNewSessionCode();
        });

        // 3. Thêm nguyên liệu mới vào danh sách hàng chờ (Tạm thời trên lưới)
        btnThemCT.addActionListener(e -> {
            try {
                if(txtCTPX_MaNL.getText().trim().isEmpty() || txtCTPX_SL.getText().trim().isEmpty() || txtCTPX_DG.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ dữ liệu nguyên liệu chi tiết!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                ChiTietPhieuXuat ct = new ChiTietPhieuXuat();
                ct.setMaCTPX(txtCTPX_Ma.getText().trim());
                ct.setMaPX(txtPX_Ma.getText().trim());
                ct.setMaNL(txtCTPX_MaNL.getText().trim());
                ct.setSoLuong(Integer.parseInt(txtCTPX_SL.getText().trim()));
                ct.setDonGia(new BigDecimal(txtCTPX_DG.getText().trim()));

                if (ct.getSoLuong() <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng xuất ra phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dsChiTietTamThoi.add(ct);
                renderGridTamThoi(); 
                
                // Đẩy mã chi tiết kế tiếp dựa trên hàm đếm tự động dưới SQL Server
                txtCTPX_Ma.setText(generateNextMaCTPXFromSQL());
                txtCTPX_MaNL.setText(""); txtCTPX_SL.setText(""); txtCTPX_DG.setText("");
                txtCTPX_MaNL.requestFocusInWindow();
                
            } catch (NumberFormatException nex) {
                JOptionPane.showMessageDialog(this, "Số lượng và đơn giá yêu cầu phải nhập ký tự số nguyên!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Từ Chối Thao Tác", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 4. Đẩy toàn bộ danh sách hàng chờ từ client xuống lưu trữ vĩnh viễn trong SQL Server
        btnLuuPhieu.addActionListener(e -> {
            if (dsChiTietTamThoi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Danh sách hàng chờ xuất đang trống! Không thể lưu.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String sqlInsertPX = "INSERT INTO PhieuXuat(MaPX, NgayXuat, MaNV, TongTien) VALUES (?, ?, ?, ?)";
            String sqlInsertCT = "INSERT INTO ChiTietPhieuXuat(MaCTPX, MaPX, MaNL, SoLuong, DonGia) VALUES (?, ?, ?, ?, ?)";
            
            try (Connection conn = getConnection();
                 PreparedStatement pstPX = conn.prepareStatement(sqlInsertPX);
                 PreparedStatement pstCT = conn.prepareStatement(sqlInsertCT)) {
                
                conn.setAutoCommit(false); // Bật Transaction để chống nghẽn hoặc mất dữ liệu nửa chừng
                try {
                    // Chèn dữ liệu vào bảng PhieuXuat gốc
                    pstPX.setString(1, txtPX_Ma.getText().trim());
                    pstPX.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                    pstPX.setString(3, txtPX_MaNV.getText().trim());
                    pstPX.setBigDecimal(4, tongTienTamTinh);
                    pstPX.executeUpdate();
                    
                    // Vòng lặp chèn toàn bộ danh sách hàng chờ chi tiết
                    for (ChiTietPhieuXuat item : dsChiTietTamThoi) {
                        pstCT.setString(1, item.getMaCTPX());
                        pstCT.setString(2, item.getMaPX());
                        pstCT.setString(3, item.getMaNL());
                        pstCT.setInt(4, item.getSoLuong());
                        pstCT.setBigDecimal(5, item.getDonGia());
                        pstCT.addBatch(); // Sử dụng Batch xử lý tối ưu hóa hiệu năng
                    }
                    pstCT.executeBatch();
                    
                    conn.commit(); // Hoàn tất giao dịch
                    JOptionPane.showMessageDialog(this, "Đã đẩy dữ liệu thành công! Toàn bộ Phiếu Xuất Kho đã được lưu trữ an toàn trong SQL Server.");
                    
                    dsChiTietTamThoi.clear();
                    loadDataPhieuXuat(); 
                    btnInPhieu.setEnabled(true); 
                    btnLuuPhieu.setEnabled(false);
                    btnThemCT.setEnabled(false);
                } catch (Exception ex) {
                    conn.rollback(); // Hoàn tác nếu có lỗi bất kỳ xảy ra
                    throw ex;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối hoặc trùng lặp dữ liệu CSDL: " + ex.getMessage(), "Thất bại", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnInPhieu.addActionListener(e -> {
            String maPX = txtPX_Ma.getText().trim();
            if (maPX.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng lựa chọn một hóa đơn đã lưu để kết xuất!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("In Phieu Xuat Kho " + maPX);
            job.setPrintable(new PhieuXuatPrintable(maPX, tableCTPX));

            if (job.printDialog()) {
                try {
                    job.print();
                    JOptionPane.showMessageDialog(this, "Phiếu xuất kho đã được kết xuất thành công!");
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(this, "Quá trình xuất hóa đơn thất bại: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // =========================================================
    // CÁC HÀM TRUY VẤN - ĐỒNG BỘ DỮ LIỆU SQL SERVER
    // =========================================================
    
    private void generateNewSessionCode() {
        txtPX_Ma.setText(generateNextMaPXFromSQL());
        txtCTPX_Ma.setText(generateNextMaCTPXFromSQL());
        btnLuuPhieu.setEnabled(true);
        btnThemCT.setEnabled(true);
        btnInPhieu.setEnabled(false); 
    }

    // Tự động sinh mã Phiếu xuất tiếp theo (Ví dụ: PX001, PX002...) dựa trên dữ liệu thật ở SQL
    private String generateNextMaPXFromSQL() {
        String sql = "SELECT COUNT(*) FROM PhieuXuat";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return "PX" + String.format("%03d", rs.getInt(1) + 1);
            }
        } catch (Exception ignored) {}
        return "PX" + (System.currentTimeMillis() / 1000000);
    }

    // Tự động sinh mã Chi tiết phiếu kế tiếp (Ví dụ: CTPX001, CTPX002...) từ SQL Server
    private String generateNextMaCTPXFromSQL() {
        String sql = "SELECT COUNT(*) FROM ChiTietPhieuXuat";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return "CT" + String.format("%03d", rs.getInt(1) + 1 + dsChiTietTamThoi.size());
            }
        } catch (Exception ignored) {}
        return "CT" + (System.currentTimeMillis() / 1000000);
    }

    // Đồng bộ danh sách lưới hàng chờ tạm thời
    private void renderGridTamThoi() {
        modelCTPX.setRowCount(0);
        tongTienTamTinh = BigDecimal.ZERO;
        for (ChiTietPhieuXuat ct : dsChiTietTamThoi) {
            BigDecimal thanhTien = ct.getDonGia().multiply(new BigDecimal(ct.getSoLuong()));
            tongTienTamTinh = tongTienTamTinh.add(thanhTien);
            modelCTPX.addRow(new Object[]{ct.getMaCTPX(), ct.getMaNL(), ct.getSoLuong(), currencyFormat.format(ct.getDonGia()) + " đ", currencyFormat.format(thanhTien) + " đ"});
        }
        lblTongTien.setText(currencyFormat.format(tongTienTamTinh) + " VNĐ");
    }

    // Đọc danh sách lịch sử phiếu xuất tổng từ bảng PhieuXuat lên giao diện
    private void loadDataPhieuXuat() {
        modelPX.setRowCount(0);
        String sql = "SELECT MaPX, NgayXuat, MaNV, TongTien FROM PhieuXuat ORDER BY NgayXuat DESC";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                modelPX.addRow(new Object[]{
                    rs.getString("MaPX"),
                    rs.getDate("NgayXuat"),
                    rs.getString("MaNV"),
                    currencyFormat.format(rs.getBigDecimal("TongTien")) + " đ"
                });
            }
        } catch (Exception ex) {
            System.err.println("Không thể đọc lịch sử phiếu từ SQL: " + ex.getMessage());
        }
    }

    // Truy vấn dữ liệu chi tiết của 1 phiếu xuất kho cụ thể dựa trên liên kết mã khóa chính
    private void loadDataChiTietFromSQL(String maPX) {
        modelCTPX.setRowCount(0);
        String sql = "SELECT MaCTPX, MaNL, SoLuong, DonGia FROM ChiTietPhieuXuat WHERE MaPX = ?";
        BigDecimal tongTienCu = BigDecimal.ZERO;
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, maPX);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    BigDecimal dg = rs.getBigDecimal("DonGia");
                    int sl = rs.getInt("SoLuong");
                    BigDecimal thanhTien = dg.multiply(new BigDecimal(sl));
                    tongTienCu = tongTienCu.add(thanhTien);
                    
                    modelCTPX.addRow(new Object[]{
                        rs.getString("MaCTPX"),
                        rs.getString("MaNL"),
                        sl,
                        currencyFormat.format(dg) + " đ",
                        currencyFormat.format(thanhTien) + " đ"
                    });
                }
            }
            lblTongTien.setText(currencyFormat.format(tongTienCu) + " VNĐ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu chi tiết: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtPX_Ma.setText("");
        txtCTPX_Ma.setText("");
        txtCTPX_MaNL.setText("");
        txtCTPX_SL.setText("");
        txtCTPX_DG.setText("");
        lblTongTien.setText("0 VNĐ");
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JTextField createModernTextField(boolean editable) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setEditable(editable);
        tf.setBackground(editable ? Color.WHITE : new Color(242, 244, 247));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    private JButton createModernButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(145, 35));
        return b;
    }

    private JTable createModernTable(DefaultTableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setGridColor(new Color(230, 235, 240));
        table.setSelectionBackground(new Color(254, 237, 222)); 
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(230, 235, 240));
        header.setForeground(TEXT_DARK);
        header.setPreferredSize(new Dimension(100, 32));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        return table;
    }
}

// ============================================================================
// LỚP PHỤ TRỢ: ĐỒ HỌA BIỂU MẪU IN PHIẾU XUẤT KHO 
// ============================================================================
class PhieuXuatPrintable implements Printable {
    private String maPX;
    private JTable tableChiTiet;

    public PhieuXuatPrintable(String maPX, JTable tableChiTiet) {
        this.maPX = maPX;
        this.tableChiTiet = tableChiTiet;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE; 
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int x = 50;
        int y = 50;
        int width = (int) pageFormat.getImageableWidth() - 100;

        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("JOLLIBEE PHẠM NGỌC THẠCH", x, y);
        y += 18;
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString("Địa chỉ: Số 2 Phạm Ngọc Thạch, Trung Tự, Đống Đa, Hà Nội", x, y);
        y += 14;
        g2d.drawString("Hotline hỗ trợ chuỗi cửa hàng: 1900 1533", x, y);
        y += 35;

        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString("PHIẾU XUẤT KHO NGUYÊN LIỆU", x + 120, y);
        y += 20;
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("Mã số phiếu định danh: " + maPX, x + 140, y);
        y += 15;
        g2d.drawString("Thời gian xuất bản: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), x + 115, y);
        y += 35;

        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawLine(x, y, x + width, y); 
        y += 16;
        g2d.drawString("Mã CTPX", x + 5, y);
        g2d.drawString("Mã Nguyên Liệu", x + 80, y);
        g2d.drawString("Số Lượng", x + 200, y);
        g2d.drawString("Đơn Giá", x + 280, y);
        g2d.drawString("Thành Tiền", x + 380, y);
        y += 8;
        g2d.drawLine(x, y, x + width, y); 

        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        int totalRows = tableChiTiet.getRowCount();
        int tongTienChung = 0;

        for (int i = 0; i < totalRows; i++) {
            y += 22;
            g2d.drawString(tableChiTiet.getValueAt(i, 0).toString(), x + 5, y);
            g2d.drawString(tableChiTiet.getValueAt(i, 1).toString(), x + 80, y);
            g2d.drawString(tableChiTiet.getValueAt(i, 2).toString(), x + 215, y);
            g2d.drawString(tableChiTiet.getValueAt(i, 3).toString(), x + 280, y);
            g2d.drawString(tableChiTiet.getValueAt(i, 4).toString(), x + 380, y);
            
            String cleanMoney = tableChiTiet.getValueAt(i, 4).toString().replace(" đ", "").replace(",", "");
            tongTienChung += Integer.parseInt(cleanMoney);
        }

        y += 25;
        g2d.drawLine(x, y, x + width, y);
        y += 22;
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("TỔNG TIỀN DOANH THU XUẤT KHO:", x + 160, y);
        g2d.drawString(new DecimalFormat("#,##0").format(tongTienChung) + " VNĐ", x + 380, y);

        y += 50;
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString("Người Lập Phiếu", x + 30, y);
        g2d.drawString("Thủ Kho Xác Nhận", x + 200, y);
        g2d.drawString("Người Nhận Hàng", x + 370, y);
        y += 15;
        g2d.setFont(new Font("Arial", Font.ITALIC, 9));
        g2d.drawString("(Ký, ghi rõ họ tên)", x + 33, y);
        g2d.drawString("(Ký, ghi rõ họ tên)", x + 205, y);
        g2d.drawString("(Ký, ghi rõ họ tên)", x + 373, y);

        return PAGE_EXISTS;
    }
}