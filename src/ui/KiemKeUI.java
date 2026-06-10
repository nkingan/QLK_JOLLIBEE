package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.DBConnection;

public class KiemKeUI extends JPanel {

    private final Color jollibeeRed = new Color(227, 29, 43);
    private final Color creamWhite = new Color(255, 253, 240);
    private final Color darkCharcoal = new Color(50, 50, 50);
    private static final Color JOLLIBEE_RED    = new Color(227, 29, 43);
    private final Color tableHeaderBg = new Color(180, 30, 45);

    


    private JTable tableTonKho;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbFilterTrangThai;
    private JButton btnReload;
    private JButton btnExportExcel;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat displaySdf = new SimpleDateFormat("dd/MM/yyyy");

    public KiemKeUI() {
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(creamWhite);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- North Panel: Filter & Control ---
        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);

        JLabel lblTitle = new JLabel("KIỂM KÊ KHO & QUẢN LÝ HẠN SỬ DỤNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(139, 69, 19));
        pnlNorth.add(lblTitle, BorderLayout.WEST);

        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlControls.setOpaque(false);

        pnlControls.add(new JLabel("Lọc trạng thái hạn:"));
        cbFilterTrangThai = new JComboBox<>(new String[]{"Tất cả", "Còn hạn", "Sắp hết hạn", "Hết hạn", "Chưa cập nhật"});
        cbFilterTrangThai.setBackground(Color.WHITE);
        pnlControls.add(cbFilterTrangThai);

        btnReload = new JButtonCustom("🔄 Làm mới", darkCharcoal, Color.WHITE);
        pnlControls.add(btnReload);

        btnExportExcel = new JButtonCustom("📊 Xuất Excel", new Color(40, 167, 69), Color.WHITE);
        btnExportExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlControls.add(btnExportExcel);

        // QUAN TRỌNG: Override LAF button defaults để màu nền hiển thị đúng
        // trên tất cả hệ điều hành (Windows/macOS/Linux)
        UIManager.put("Button.select",             JOLLIBEE_RED);
        UIManager.put("Button.background",         Color.GRAY);
        UIManager.put("Button.opaque",             Boolean.TRUE);


        pnlNorth.add(pnlControls, BorderLayout.EAST);
        add(pnlNorth, BorderLayout.NORTH);

        // --- Center Panel: Table ---
        String[] columns = {"Mã NL", "Tên Nguyên Liệu", "Đơn Vị", "Tồn Kho Hiện Tại", "Phân Phối Kho", "Mã Lô", "Ngày Nhập Lô", "Hạn Sử Dụng", "Trạng Thái Hạn"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableTonKho = new JTable(tableModel);
        tableTonKho.setRowHeight(30);
        tableTonKho.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableTonKho.setShowGrid(true);
        tableTonKho.setGridColor(new Color(230, 220, 210));

        JTableHeader header = tableTonKho.getTableHeader();
        header.setBackground(tableHeaderBg);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Custom Expiration highlight render
        tableTonKho.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(table, val, isSelected, hasFocus, r, c);
                
                String trangThai = table.getValueAt(r, 8).toString();
                comp.setForeground(darkCharcoal);
                comp.setBackground(Color.WHITE);
                
                if ("Hết hạn".equals(trangThai)) {
                    comp.setBackground(new Color(255, 230, 230)); // Soft red
                    if (c == 8) comp.setForeground(Color.RED);
                } else if ("Sắp hết hạn".equals(trangThai)) {
                    comp.setBackground(new Color(255, 245, 204)); // Soft orange/yellow
                    if (c == 8) comp.setForeground(new Color(214, 117, 0));
                } else if ("Còn hạn".equals(trangThai)) {
                    comp.setBackground(new Color(230, 245, 230)); // Soft green
                    if (c == 8) comp.setForeground(new Color(40, 140, 40));
                }

                if (isSelected) {
                    comp.setBackground(new Color(255, 180, 0, 150));
                    comp.setForeground(Color.BLACK);
                }

                // Căn lề
                if (c == 0 || c == 2 || c == 3 || c == 5 || c == 6 || c == 7 || c == 8) {
                    setHorizontalAlignment(JLabel.CENTER);
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }

                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableTonKho);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // --- Listeners ---
        cbFilterTrangThai.addActionListener(e -> applyFilter());
        btnReload.addActionListener(e -> refreshData());
        btnExportExcel.addActionListener(e -> performExcelExport());
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        String sql = "SELECT MaNL, TenNL, DonViTinh, TonKhoHienTai, TenKho, MaLo, NgayNhapLo, HanSuDung, TrangThaiHan FROM VW_TonKho";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                String maNL = rs.getString("MaNL");
                String tenNL = rs.getString("TenNL");
                String dvt = rs.getString("DonViTinh");
                int qty = rs.getInt("TonKhoHienTai");
                String tenKho = rs.getString("TenKho");
                String maLo = rs.getString("MaLo");
                
                java.sql.Date NgayNhapLo = rs.getDate("NgayNhapLo");
                java.sql.Date HanSuDung = rs.getDate("HanSuDung");
                
                String hsdStr = (HanSuDung != null) ? displaySdf.format(HanSuDung) : "Không có";
                String ngayNhapStr = (NgayNhapLo != null) ? displaySdf.format(NgayNhapLo) : "";
                
                String trangThai = rs.getString("TrangThaiHan");

                tableModel.addRow(new Object[]{
                    maNL, tenNL, dvt, qty, tenKho, maLo, ngayNhapStr, hsdStr, trangThai
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        applyFilter();
    }

    private void applyFilter() {
        String filter = cbFilterTrangThai.getSelectedItem().toString();
        
        // Custom row filtering dynamically
        tableModel.setRowCount(0);
        String sql = "SELECT MaNL, TenNL, DonViTinh, TonKhoHienTai, TenKho, MaLo, NgayNhapLo, HanSuDung, TrangThaiHan FROM VW_TonKho";
        
        if (!"Tất cả".equals(filter)) {
            sql += " WHERE TrangThaiHan = ?";
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            if (!"Tất cả".equals(filter)) {
                pst.setNString(1, filter);
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String maNL = rs.getString("MaNL");
                    String tenNL = rs.getString("TenNL");
                    String dvt = rs.getString("DonViTinh");
                    int qty = rs.getInt("TonKhoHienTai");
                    String tenKho = rs.getString("TenKho");
                    String maLo = rs.getString("MaLo");
                    
                    java.sql.Date NgayNhapLo = rs.getDate("NgayNhapLo");
                    java.sql.Date HanSuDung = rs.getDate("HanSuDung");
                    
                    String hsdStr = (HanSuDung != null) ? displaySdf.format(HanSuDung) : "Không có";
                    String ngayNhapStr = (NgayNhapLo != null) ? displaySdf.format(NgayNhapLo) : "";
                    
                    String trangThai = rs.getString("TrangThaiHan");

                    tableModel.addRow(new Object[]{
                        maNL, tenNL, dvt, qty, tenKho, maLo, ngayNhapStr, hsdStr, trangThai
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void performExcelExport() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trên bảng để xuất file Excel!", "Bảng trống", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file xuất kho kiểm kê");
        fileChooser.setSelectedFile(new File("BaoCao_KiemKe_TonKho.xls"));

        int choice = fileChooser.showSaveDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File destFile = fileChooser.getSelectedFile();
            
            // If user did not type extension, add .xls
            if (!destFile.getName().toLowerCase().endsWith(".xls")) {
                destFile = new File(destFile.getParentFile(), destFile.getName() + ".xls");
            }

            try (FileOutputStream fos = new FileOutputStream(destFile);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {

                // Write HTML Excel document with styling
                writer.write("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" ");
                writer.write("xmlns:x=\"urn:schemas-microsoft-com:office:excel\" ");
                writer.write("xmlns=\"http://www.w3.org/TR/REC-html40\">\n");
                writer.write("<head>\n");
                writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
                writer.write("</head>\n");
                writer.write("<body>\n");
                
                writer.write("<h2>BÁO CÁO CHI TIẾT TỒN KHO NGUYÊN LIỆU & HẠN SỬ DỤNG</h2>\n");
                writer.write("<p><i>Xuất từ hệ thống kho Jollibee Phạm Ngọc Thạch - Ngày: " + displaySdf.format(new Date()) + "</i></p>\n");
                
                writer.write("<table border=\"1\">\n");
                
                // Headers Row
                writer.write("  <tr bgcolor=\"#E31D2B\">\n");
                for (int c = 0; c < tableModel.getColumnCount(); c++) {
                    writer.write("    <th><b><font color=\"white\">" + tableModel.getColumnName(c) + "</font></b></th>\n");
                }
                writer.write("  </tr>\n");

                // Data Rows
                for (int r = 0; r < tableModel.getRowCount(); r++) {
                    String trangThai = tableModel.getValueAt(r, 8).toString();
                    
                    // Style row color based on status
                    String rowBg = "#FFFFFF";
                    if ("Hết hạn".equals(trangThai)) {
                        rowBg = "#FFCCCC"; // Light red
                    } else if ("Sắp hết hạn".equals(trangThai)) {
                        rowBg = "#FFF5CC"; // Light orange
                    } else if ("Còn hạn".equals(trangThai)) {
                        rowBg = "#E6F5E6"; // Light green
                    }

                    writer.write("  <tr bgcolor=\"" + rowBg + "\">\n");
                    for (int c = 0; c < tableModel.getColumnCount(); c++) {
                        String cellVal = tableModel.getValueAt(r, c).toString();
                        
                        // Align cells
                        String align = "left";
                        if (c == 0 || c == 2 || c == 3 || c == 5 || c == 6 || c == 7 || c == 8) {
                            align = "center";
                        }
                        
                        writer.write("    <td align=\"" + align + "\">" + cellVal + "</td>\n");
                    }
                    writer.write("  </tr>\n");
                }
                writer.write("</table>\n");
                writer.write("</body>\n");
                writer.write("</html>\n");

                JOptionPane.showMessageDialog(this, "Xuất file báo cáo kiểm kê thành công!\nLưu tại: " + destFile.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xảy ra trong quá trình xuất Excel: " + ex.getMessage(), "Lỗi xuất file", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
