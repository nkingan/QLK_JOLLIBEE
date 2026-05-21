package gui;

import bus.PhieuXuatBUS; 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Vector;


@SuppressWarnings("all")
public class TonKhoPanel extends JPanel {

    private JTextField txtTimKiem;
    private JComboBox<String> cbKho;
    private JButton btnLoc, btnLamMoi;
    
    private JTable tableTonKho;
    private DefaultTableModel modelTonKho;
    private JLabel lblTongLoNL, lblCanhBaoHeThong;

    private final Color JB_RED = new Color(214, 24, 34);       
    private final Color JB_YELLOW = new Color(254, 192, 6);    
    private final Color BG_PANEL = new Color(244, 245, 247);   
    private final Color TEXT_DARK = new Color(50, 50, 50);     
    private final Color BORDER_COLOR = new Color(218, 222, 229);

    private DecimalFormat currencyFormat = new DecimalFormat("#,##0");

    public TonKhoPanel() {
        initComponents();
        loadDataComboBoxKho(); 
        loadReportTonKho("", "Tất cả các kho"); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBackground(BG_PANEL);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        Font fontTitle = new Font("Segoe UI", Font.BOLD, 13);
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 12);

       
        // 1. TOP PANEL: KHU VỰC BỘ LỌC TÌM KIẾM CÂN ĐỐI
        
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 12, 4, 12);

        txtTimKiem = createModernTextField(true);
        cbKho = new JComboBox<>();
        cbKho.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbKho.setBackground(Color.WHITE);

        btnLoc = createModernButton("🔍 Lọc Báo Cáo", JB_RED, Color.WHITE);
        btnLamMoi = createModernButton("🔄 Tải Lại", new Color(108, 117, 125), Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; filterPanel.add(createLabel("Tìm Tên Nguyên Liệu:", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; filterPanel.add(txtTimKiem, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; filterPanel.add(createLabel("Khu Vực Phân Kho:", fontLabel), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.5; filterPanel.add(cbKho, gbc);
        
        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnGroup.setOpaque(false);
        btnGroup.add(btnLoc); btnGroup.add(btnLamMoi);
        gbc.gridx = 4; gbc.gridy = 0; gbc.weightx = 0; filterPanel.add(btnGroup, gbc);

        // =========================================================
        // 2. CENTER PANEL: LƯỚI GRID THEO DÕI LOGIC TRIGGER KHO
        // =========================================================
        JPanel gridPanel = new JPanel(new BorderLayout());
        gridPanel.setBackground(Color.WHITE);
        
        TitledBorder borderTitle = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR), 
                " BÁO CÁO TỒN KHO NGUYÊN LIỆU JOLLIBEE PHẠM NGỌC THẠCH ",
                TitledBorder.LEADING, TitledBorder.TOP, fontTitle, JB_RED
        );
        gridPanel.setBorder(BorderFactory.createCompoundBorder(borderTitle, new EmptyBorder(8, 8, 8, 8)));

        String[] headerColumns = {
            "Mã NL", "Tên Nguyên Liệu", "ĐVT", "Tồn Hiện Tại", 
            "Mã Kho", "Tên Kho", "Mã Lô Nhập", "Ngày Nhập Lô", 
            "Số Lượng Nhập", "Hạn Sử Dụng", "Trạng Thế Hạn", "Đơn Giá Nhập", "Giá Trị Tồn Lô"
        };

        modelTonKho = new DefaultTableModel(headerColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 3 || column == 8) return Integer.class; 
                return super.getColumnClass(column);
            }
        };
        tableTonKho = new JTable(modelTonKho);
        tableTonKho.setRowHeight(28); 
        tableTonKho.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableTonKho.setSelectionBackground(new Color(254, 237, 222));
        tableTonKho.setSelectionForeground(Color.BLACK);

        JTableHeader tableHeader = tableTonKho.getTableHeader();
        tableHeader.setFont(fontTitle);
        tableHeader.setBackground(new Color(230, 235, 240));
        tableHeader.setForeground(TEXT_DARK);
        tableHeader.setPreferredSize(new Dimension(100, 32));

        gridPanel.add(new JScrollPane(tableTonKho), BorderLayout.CENTER);

       
        // 3. BOTTOM PANEL
      
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(12, 15, 12, 15)
        ));

        lblTongLoNL = new JLabel("Tổng số dòng bản ghi: 0");
        lblTongLoNL.setFont(fontTitle);
        lblTongLoNL.setForeground(TEXT_DARK);

        lblCanhBaoHeThong = new JLabel("Hệ thống vận hành an toàn", SwingConstants.RIGHT);
        lblCanhBaoHeThong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCanhBaoHeThong.setForeground(new Color(46, 139, 87)); 

        bottomPanel.add(lblTongLoNL);
        bottomPanel.add(lblCanhBaoHeThong);

        add(filterPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnLoc.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            String selectedKho = cbKho.getSelectedItem() != null ? cbKho.getSelectedItem().toString() : "Tất cả các kho";
            loadReportTonKho(keyword, selectedKho);
        });

        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            cbKho.setSelectedIndex(0);
            loadReportTonKho("", "Tất cả các kho");
        });
    }

    private void loadDataComboBoxKho() {
        cbKho.removeAllItems();
        cbKho.addItem("Tất cả các kho");
        
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyKhoJollibee;encrypt=false;trustServerCertificate=true;";
        String user = "sa"; 
        String pass = "123456"; 

        String sql = "SELECT MaKho, TenKho FROM Kho";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cbKho.addItem(rs.getString("MaKho") + " - " + rs.getString("TenKho"));
            }
        } catch (Exception e) {
            System.err.println("Lỗi nạp danh sách kho lên ComboBox: " + e.getMessage());
        }
    }

   
    private void loadReportTonKho(String keyword, String selectedKho) {
        modelTonKho.setRowCount(0);

        String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyKhoJollibee;encrypt=false;trustServerCertificate=true;";
        String user = "sa";
        String pass = "123456"; 

   
        StringBuilder sql = new StringBuilder(
            "SELECT n.MaNL, n.TenNL, n.DonViTinh, n.SoLuong AS TonKhoHienTai, " +
            "       n.MaKho, k.TenKho, c.MaCTPN AS MaLo, p.NgayNhap AS NgayNhapLo, " +
            "       c.SoLuong AS SoLuongNhapLo, c.HanSuDung, " +
            "       ISNULL(CASE " +
            "           WHEN c.HanSuDung IS NULL THEN N'Chưa cập nhật' " +
            "           WHEN c.HanSuDung < CAST(GETDATE() AS DATE) THEN N'Hết hạn' " +
            "           WHEN c.HanSuDung <= DATEADD(DAY, 30, GETDATE()) THEN N'Sắp hết hạn' " +
            "           ELSE N'Còn hạn' " +
            "       END, N'Chưa cập nhật') AS TrangThaiHan, c.DonGia " +
            "FROM ChiTietPhieuNhap c " +
            "JOIN PhieuNhap p ON c.MaPN = p.MaPN " +
            "RIGHT JOIN NguyenLieu n ON c.MaNL = n.MaNL " + // Giữ lại toàn bộ 23 mặt hàng gốc
            "LEFT JOIN Kho k ON n.MaKho = k.MaKho " +
            "WHERE n.TenNL LIKE ?"
        );
        
        if (!selectedKho.equals("Tất cả các kho")) {
            String maKho = selectedKho.split(" - ")[0].trim();
            sql.append(" AND n.MaKho = '").append(maKho).append("'");
        }

        int countRows = 0;
        BigDecimal tongGiaTriTaiSanKho = BigDecimal.ZERO;
        boolean coCanhBaoTon = false;
        boolean coCanhBaoHan = false;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql.toString())) { 
            
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    countRows++;
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("MaNL"));
                    row.add(rs.getString("TenNL"));
                    row.add(rs.getString("DonViTinh"));
                    
                    int tonKhoHienTai = rs.getInt("TonKhoHienTai");
                    row.add(tonKhoHienTai); 
                    
                    row.add(rs.getString("MaKho"));
                    row.add(rs.getString("TenKho"));
                    
                    // Xử lý an toàn cho những mặt hàng chưa từng có lô nhập (Tránh văng lỗi Null)
                    String maLo = rs.getString("MaLo");
                    row.add(maLo != null ? maLo : "-");
                    
                    Date ngayNhap = rs.getDate("NgayNhapLo");
                    row.add(ngayNhap != null ? ngayNhap : "-");
                    
                    row.add(maLo != null ? rs.getInt("SoLuongNhapLo") : 0);
                    
                    Date hsd = rs.getDate("HanSuDung");
                    row.add(hsd != null ? hsd : "-");
                    
                    String trangThaiHan = rs.getString("TrangThaiHan");
                    row.add(trangThaiHan);

                    BigDecimal donGia = rs.getBigDecimal("DonGia");
                    if (donGia == null) donGia = BigDecimal.ZERO;
                    
                    BigDecimal giaTriTonLo = donGia.multiply(new BigDecimal(tonKhoHienTai));
                    tongGiaTriTaiSanKho = tongGiaTriTaiSanKho.add(giaTriTonLo);

                    row.add(currencyFormat.format(donGia) + " đ");
                    row.add(currencyFormat.format(giaTriTonLo) + " đ");

                    // Trigger 3: Nếu tổng lượng tồn trong kho dính định mức dưới 20 -> Kích hoạt cảnh báo chữ đỏ
                    if (tonKhoHienTai < 20) coCanhBaoTon = true;
                    if (trangThaiHan.equals("Hết hạn") || trangThaiHan.equals("Sắp hết hạn")) coCanhBaoHan = true;

                    modelTonKho.addRow(row);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi nạp báo cáo tồn kho: " + e.getMessage());
            e.printStackTrace();
        }

        applyCustomTableRenderer();

        lblTongLoNL.setText("Tổng số danh mục mặt hàng hiển thị: " + countRows);
        
        if (coCanhBaoHan) {
            lblCanhBaoHeThong.setText("⚠️ NGUY HIỂM: Có lô hàng ĐÃ HẾT HẠN hoặc SẮP HẾT HẠN! (Tổng vốn tài sản: " + currencyFormat.format(tongGiaTriTaiSanKho) + " đ)");
            lblCanhBaoHeThong.setForeground(JB_RED);
        } else if (coCanhBaoTon) {
            lblCanhBaoHeThong.setText("⚠️ CẢNH BÁO: Phát hiện nguyên liệu dưới định mức 20! (Tổng vốn tài sản: " + currencyFormat.format(tongGiaTriTaiSanKho) + " đ)");
            lblCanhBaoHeThong.setForeground(JB_YELLOW);
        } else {
            lblCanhBaoHeThong.setText("🟢 Tồn kho an toàn. Tổng giá trị tài sản lưu kho: " + currencyFormat.format(tongGiaTriTaiSanKho) + " VNĐ");
            lblCanhBaoHeThong.setForeground(new Color(46, 139, 87));
        }
    }

    private void applyCustomTableRenderer() {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                int tonKhoHienTai = (Integer) table.getValueAt(row, 3);
                String trangThaiHan = table.getValueAt(row, 10).toString();

                if (trangThaiHan.equals("Hết hạn")) {
                    c.setForeground(Color.WHITE);
                    c.setBackground(new Color(220, 53, 69)); 
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else if (trangThaiHan.equals("Sắp hết hạn")) {
                    c.setForeground(Color.BLACK);
                    c.setBackground(new Color(255, 193, 7)); 
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else if (tonKhoHienTai < 20) {
                    c.setForeground(JB_RED); 
                    c.setBackground(Color.WHITE);
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
                } else {
                    c.setForeground(Color.BLACK);
                    c.setBackground(Color.WHITE);
                    c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }

                if (column == 1) {
                    setHorizontalAlignment(JLabel.LEFT); 
                } else if (column == 11 || column == 12) {
                    setHorizontalAlignment(JLabel.RIGHT); 
                }

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                
                return c;
            }
        };

        for (int i = 0; i < tableTonKho.getColumnCount(); i++) {
            tableTonKho.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        tableTonKho.getColumnModel().getColumn(0).setPreferredWidth(55);
        tableTonKho.getColumnModel().getColumn(1).setPreferredWidth(170);
        tableTonKho.getColumnModel().getColumn(3).setPreferredWidth(95);
        tableTonKho.getColumnModel().getColumn(11).setPreferredWidth(100);
        tableTonKho.getColumnModel().getColumn(12).setPreferredWidth(110);
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
}