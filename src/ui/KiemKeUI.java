package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
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
        pnlActions.add(btnReload);

        btnExcel = new JButton("📥 Xuất Excel");
        btnExcel.setBackground(new Color(40, 167, 69)); // Xanh lá Excel
        btnExcel.setForeground(Color.WHITE);
        btnExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlActions.add(btnExcel);

        pnlNorth.add(pnlActions, BorderLayout.EAST);
        add(pnlNorth, BorderLayout.NORTH);

        // --- 2. JTABLE ---
        String[] columns = {
            "Mã NL", "Tên Nguyên Liệu", "ĐVT", "Tổng Tồn Kho", "Nhà Kho", "Mã Lô Nhập", "Ngày Nhập Lô", "SL Nhập Lô", "Hạn Sử Dụng", "Trạng Thái Hạn"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableTonKho = new JTable(tableModel);
        tableTonKho.setRowHeight(28);
        tableTonKho.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableTonKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom Header Bảng
        JTableHeader header = tableTonKho.getTableHeader();
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
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }

                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableTonKho);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 31, 42), 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. ACTIONS WIRING ---
        cbFilterTrangThai.addActionListener(e -> applyFilter());
        btnReload.addActionListener(e -> refreshData());
        btnExcel.addActionListener(e -> performExportExcel());
    }

    public void refreshData() {
        fullList = reportDAO.getInventoryReport();
        applyFilter();
    }

    private void applyFilter() {
        String filter = cbFilterTrangThai.getSelectedItem().toString();
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
            html.append("<tr style='background-color:#8B4513; color:white; font-weight:bold; height:30px;'>");
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
            }
        }
    }
}
