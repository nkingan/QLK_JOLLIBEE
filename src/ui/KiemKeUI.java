package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
<<<<<<< HEAD
import javax.swing.border.LineBorder;
=======
>>>>>>> origin/van
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
<<<<<<< HEAD
import java.io.BufferedWriter;
=======
>>>>>>> origin/van
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
<<<<<<< HEAD
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
=======
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.ReportDAO;
import model.TonKhoModel;

public class KiemKeUI extends JPanel {

    private JTable tableTonKho;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbFilterTrangThai;
    private JButton btnExcel;
    private JButton btnReload;
    
    private ReportDAO reportDAO;
    private List<TonKhoModel> fullList;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public KiemKeUI() {
        this.reportDAO = new ReportDAO();
>>>>>>> origin/van
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
<<<<<<< HEAD
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
=======
        setBackground(new Color(255, 253, 240)); // Nền kem Jollibee #FFFDF0
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- 1. TITLE PANEL ---
        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);

        JLabel lblTitle = new JLabel("KIỂM SOÁT HẠN SỬ DỤNG & TRA CỨU LÔ TỒN KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(224, 31, 42)); // Đỏ Jollibee
        pnlNorth.add(lblTitle, BorderLayout.WEST);

        // --- FILTER & EXCEL ---
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActions.setOpaque(false);

        pnlActions.add(new JLabel("Bộ lọc trạng thái hạn:"));
        cbFilterTrangThai = new JComboBox<>(new String[]{"Tất cả", "Còn hạn", "Sắp hết hạn", "Hết hạn"});
        cbFilterTrangThai.setPreferredSize(new Dimension(130, 30));
        cbFilterTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlActions.add(cbFilterTrangThai);

        btnReload = new JButton("🔄 Làm mới");
        btnReload.setBackground(new Color(45, 45, 45));
        btnReload.setForeground(Color.WHITE);
        btnReload.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnReload.setFocusPainted(false);
        btnReload.setBorderPainted(false);
        btnReload.putClientProperty("JButton.buttonType", "roundRect");
        pnlActions.add(btnReload);

        btnExcel = new JButton("📥 Xuất Excel");
        btnExcel.setBackground(new Color(40, 167, 69)); // Xanh lá Excel
        btnExcel.setForeground(Color.WHITE);
        btnExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcel.setFocusPainted(false);
        btnExcel.setBorderPainted(false);
        btnExcel.putClientProperty("JButton.buttonType", "roundRect");
        pnlActions.add(btnExcel);

        pnlNorth.add(pnlActions, BorderLayout.EAST);
        add(pnlNorth, BorderLayout.NORTH);

        // --- 2. JTABLE ---
        String[] columns = {
            "Mã NL", "Tên Nguyên Liệu", "ĐVT", "Tổng Tồn Kho", "Nhà Kho", "Mã Lô Nhập", "Ngày Nhập Lô", "SL Nhập Lô", "Hạn Sử Dụng", "Trạng Thái Hạn"
        };
>>>>>>> origin/van
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableTonKho = new JTable(tableModel);
<<<<<<< HEAD
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
=======
        tableTonKho.setRowHeight(28);
        tableTonKho.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableTonKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom Header Bảng
        JTableHeader header = tableTonKho.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(new Color(180, 30, 45)); // Đỏ sậm Jollibee giống NhapKhoUI
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(227, 29, 43)));
                return lbl;
            }
        });

        // Cell Renderer for Conditional Highlighting
        tableTonKho.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String status = table.getValueAt(row, 9).toString();
                if (isSelected) {
                    comp.setBackground(new Color(255, 180, 0, 180)); // Highlight Jollibee
                    comp.setForeground(Color.BLACK);
                } else {
                    switch (status) {
                        case "Hết hạn":
                            comp.setBackground(new Color(248, 215, 218)); // Soft red
                            comp.setForeground(new Color(114, 28, 36));
                            break;
                        case "Sắp hết hạn":
                            comp.setBackground(new Color(255, 243, 205)); // Soft orange
                            comp.setForeground(new Color(133, 100, 4));
                            break;
                        case "Còn hạn":
                            comp.setBackground(new Color(212, 237, 218)); // Soft green
                            comp.setForeground(new Color(21, 87, 36));
                            break;
                        default:
                            comp.setBackground(Color.WHITE);
                            comp.setForeground(Color.BLACK);
                    }
                }

                // Căn chỉnh cột
                if (column == 0 || column == 2 || column == 5 || column == 6 || column == 8 || column == 9) {
                    setHorizontalAlignment(JLabel.CENTER);
                } else if (column == 3 || column == 7) {
                    setHorizontalAlignment(JLabel.RIGHT);
>>>>>>> origin/van
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }

                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableTonKho);
        scrollPane.getViewport().setBackground(Color.WHITE);
<<<<<<< HEAD
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
=======
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 31, 42), 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. ACTIONS WIRING ---
        cbFilterTrangThai.addActionListener(e -> applyFilter());
        btnReload.addActionListener(e -> refreshData());
        btnExcel.addActionListener(e -> performExportExcel());
    }

    public void refreshData() {
        fullList = reportDAO.getInventoryReport();
>>>>>>> origin/van
        applyFilter();
    }

    private void applyFilter() {
        String filter = cbFilterTrangThai.getSelectedItem().toString();
<<<<<<< HEAD
        
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
=======
        tableModel.setRowCount(0);
        
        if (fullList != null) {
            for (TonKhoModel tk : fullList) {
                if (filter.equals("Tất cả") || tk.getTrangThaiHan().equals(filter)) {
                    String hsdStr = tk.getHanSuDung() != null ? sdf.format(tk.getHanSuDung()) : "Không có";
                    String ngayNhapStr = tk.getNgayNhapLo() != null ? sdf.format(tk.getNgayNhapLo()) : "Không có";
                    String maLoStr = tk.getMaLo() != null ? tk.getMaLo() : "Không có";
                    String slNhapStr = tk.getMaLo() != null ? String.valueOf(tk.getSoLuongNhapLo()) : "0";

                    tableModel.addRow(new Object[]{
                        tk.getMaNL(),
                        tk.getTenNL(),
                        tk.getDonViTinh() != null ? tk.getDonViTinh() : "",
                        tk.getTonKhoHienTai(),
                        tk.getTenKho() != null ? tk.getTenKho() : "Chưa xếp",
                        maLoStr,
                        ngayNhapStr,
                        slNhapStr,
                        hsdStr,
                        tk.getTrangThaiHan()
                    });
                }
            }
        }
        // Tự động giãn cột bảng kiểm kê lô hàng tồn
        util.UIHelper.autoResizeColumnWidths(tableTonKho);
    }

    private void performExportExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Báo Cáo Tồn Kho Excel");
        fileChooser.setSelectedFile(new File("BaoCao_TonKho_Jollibee.xls"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Xây dựng chuỗi HTML chứa bảng Excel có định dạng màu sắc cao cấp
            StringBuilder html = new StringBuilder();
            html.append("<html><head><meta charset='UTF-8'></head><body>");
            html.append("<h2 style='color:#E01F2A; text-align:center;'>BÁO CÁO CHI TIẾT TỒN KHO & HẠN SỬ DỤNG - JOLLIBEE</h2>");
            html.append("<table border='1' style='border-collapse:collapse; font-family:Arial, sans-serif; font-size:11pt; width:100%;'>");
            
            // Header
            html.append("<tr style='background-color:#B41E2D; color:white; font-weight:bold; height:30px;'>");
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                html.append("<th>").append(tableModel.getColumnName(col)).append("</th>");
            }
            html.append("</tr>");

            // Data rows
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                String status = tableModel.getValueAt(r, 9).toString();
                String bgColor = "#FFFFFF";
                String fgColor = "#000000";
                
                switch (status) {
                    case "Hết hạn":
                        bgColor = "#F8D7DA";
                        fgColor = "#721C24";
                        break;
                    case "Sắp hết hạn":
                        bgColor = "#FFF3CD";
                        fgColor = "#856404";
                        break;
                    case "Còn hạn":
                        bgColor = "#D4EDDA";
                        fgColor = "#155724";
                        break;
                }

                html.append("<tr style='height:25px;'>");
                for (int c = 0; c < tableModel.getColumnCount(); c++) {
                    String align = "left";
                    if (c == 0 || c == 2 || c == 5 || c == 6 || c == 8 || c == 9) align = "center";
                    if (c == 3 || c == 7) align = "right";

                    Object val = tableModel.getValueAt(r, c);
                    String cellContent = val != null ? val.toString() : "";

                    if (c == 9) {
                        html.append("<td style='background-color:").append(bgColor)
                            .append("; color:").append(fgColor)
                            .append("; text-align:").append(align)
                            .append("; font-weight:bold;'>")
                            .append(cellContent).append("</td>");
                    } else {
                        html.append("<td style='text-align:").append(align).append(";'>")
                            .append(cellContent).append("</td>");
                    }
                }
                html.append("</tr>");
            }
            html.append("</table></body></html>");

            // Lưu file với chuẩn UTF-8
            try (FileOutputStream fos = new FileOutputStream(fileToSave);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
                
                // Viết BOM để Excel hiển thị đúng dấu Tiếng Việt
                fos.write(0xEF);
                fos.write(0xBB);
                fos.write(0xBF);

                osw.write(html.toString());
                osw.flush();
                JOptionPane.showMessageDialog(this, "Xuất báo cáo Excel thành công!", "Xuất Excel thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi xảy ra khi xuất Excel: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
>>>>>>> origin/van
            }
        }
    }
}
