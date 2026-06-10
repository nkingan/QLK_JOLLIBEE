package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
<<<<<<< HEAD
import javax.swing.border.LineBorder;
=======
>>>>>>> origin/van
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
<<<<<<< HEAD
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.NguyenLieuDAO;
import model.NguyenLieu;
import model.TaiKhoan;
import util.DBConnection;

// Import PDFBox
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class BaoCaoHaoHutUI extends JPanel {

    private final Color jollibeeRed = new Color(227, 29, 43);
    private final Color creamWhite = new Color(255, 253, 240);
    private final Color darkCharcoal = new Color(50, 50, 50);
    private final Color tableHeaderBg = new Color(180, 30, 45);

    private JComboBox<String> cbNguyenLieu;
    private JTextField txtSoluongHeThong;
    private JTextField txtSoluongThucTe;
    private JComboBox<String> cbLyDo;
    
    private JButton btnAddRow;
    private JButton btnDeleteRow;
    private JButton btnSave;
    private JButton btnExportPDF;
    
    private JTable tableReconcile;
    private DefaultTableModel tableModel;
    
=======
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dao.HaoHutDAO;
import dao.NguyenLieuDAO;
import model.HaoHut;
import model.NguyenLieu;
import model.TaiKhoan;

public class BaoCaoHaoHutUI extends JPanel {

    private final Color jollibeeRed = new Color(224, 31, 42);
    private final Color creamWhite = new Color(255, 253, 240);
    private final Color darkCharcoal = new Color(45, 45, 45);

    private JTextField txtMaHH, txtSystemQty, txtPhysicalQty, txtDiscrepancyQty, txtPercentage, txtNgayGhiNhan;
    private JComboBox<String> cbNguyenLieu;
    private JComboBox<String> cbLyDo;
    
    private JButton btnAdd, btnClear, btnExportPDF;
    private JTable tableHaoHut;
    private DefaultTableModel tableModel;

    private HaoHutDAO haoHutDAO;
>>>>>>> origin/van
    private NguyenLieuDAO nguyenLieuDAO;
    private TaiKhoan currentUser;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
<<<<<<< HEAD

    public BaoCaoHaoHutUI(TaiKhoan user) {
        this.currentUser = user;
        this.nguyenLieuDAO = new NguyenLieuDAO();
        initUI();
        loadIngredients();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(creamWhite);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- North Panel: Form Reconcile ---
        JPanel pnlNorth = new JPanel(new GridBagLayout());
        pnlNorth.setOpaque(false);
        
        TitledBorder formBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(jollibeeRed, 1), "Nhập biên bản đối chiếu kiểm kê thực tế"
        );
        formBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        formBorder.setTitleColor(jollibeeRed);
        pnlNorth.setBorder(formBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        cbNguyenLieu = new JComboBox<>();
        cbNguyenLieu.setBackground(Color.WHITE);
        txtSoluongHeThong = new JTextField("0", 6);
        txtSoluongHeThong.setEditable(false);
        txtSoluongHeThong.setBackground(new Color(230, 230, 230));
        txtSoluongThucTe = new JTextField("0", 6);
        
        cbLyDo = new JComboBox<>(new String[]{"Mất mát / Thất thoát", "Hỏng hóc / Hết hạn", "Sai lệch cân đo", "Hao hụt tự nhiên", "Lý do khác"});
        cbLyDo.setBackground(Color.WHITE);

        Dimension fieldSize = new Dimension(160, 28);
        cbNguyenLieu.setPreferredSize(new Dimension(220, 28));
        txtSoluongHeThong.setPreferredSize(fieldSize);
        txtSoluongThucTe.setPreferredSize(fieldSize);
        cbLyDo.setPreferredSize(fieldSize);

        gbc.gridx = 0; gbc.gridy = 0; pnlNorth.add(new JLabel("Chọn nguyên liệu:"), gbc);
        gbc.gridx = 1; pnlNorth.add(cbNguyenLieu, gbc);

        gbc.gridx = 2; pnlNorth.add(new JLabel("SL Hệ thống:"), gbc);
        gbc.gridx = 3; pnlNorth.add(txtSoluongHeThong, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlNorth.add(new JLabel("SL Thực tế đếm:"), gbc);
        gbc.gridx = 1; pnlNorth.add(txtSoluongThucTe, gbc);

        gbc.gridx = 2; pnlNorth.add(new JLabel("Lý do hao hụt:"), gbc);
        gbc.gridx = 3; pnlNorth.add(cbLyDo, gbc);

        gbc.gridx = 4; gbc.gridy = 1;
        btnAddRow = new JButtonCustom("+ Thêm dòng đối chiếu", new Color(40, 167, 69), Color.WHITE);
        pnlNorth.add(btnAddRow, gbc);

        add(pnlNorth, BorderLayout.NORTH);

        // --- Center Panel: Reconcile List Table ---
        String[] columns = {"Mã NL", "Tên Nguyên Liệu", "SL Hệ Thống", "SL Thực Tế", "SL Hao Hụt", "% Hao Hụt", "Lý Do"};
=======
    private DecimalFormat df = new DecimalFormat("#,##0.00");

    public BaoCaoHaoHutUI(TaiKhoan user) {
        this.currentUser = user;
        this.haoHutDAO = new HaoHutDAO();
        this.nguyenLieuDAO = new NguyenLieuDAO();
        initComponents();
        loadHaoHutData();
    }

    public void refreshData() {
        loadHaoHutData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(creamWhite);

        // --- TITLE ---
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel lblTitle = new JLabel("BÁO CÁO HAO HỤT & BIÊN ĐỘ SAI LỆCH KIỂM KÊ KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(jollibeeRed);
        titlePanel.add(lblTitle, BorderLayout.WEST);

        if (currentUser != null && currentUser.getTenDangNhap() != null) {
            JLabel lblUser = new JLabel("Người dùng: " + currentUser.getTenDangNhap());
            lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblUser.setForeground(darkCharcoal);
            titlePanel.add(lblUser, BorderLayout.EAST);
        }

        add(titlePanel, BorderLayout.NORTH);

        // --- CENTER: JTABLE ---
        String[] columns = {"Mã Báo Cáo", "Mã NL", "Tên Nguyên Liệu", "SL Hệ Thống", "SL Thực Tế", "SL Hao Hụt", "% Hao Hụt", "Lý Do", "Ngày Ghi Nhận"};
>>>>>>> origin/van
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

<<<<<<< HEAD
        tableReconcile = new JTable(tableModel);
        tableReconcile.setRowHeight(30);
        tableReconcile.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = tableReconcile.getTableHeader();
        header.setBackground(tableHeaderBg);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableReconcile.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableReconcile.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableReconcile.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableReconcile.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableReconcile.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        // Highlight cells where hao hut > 0
        tableReconcile.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(table, val, isSelected, hasFocus, r, c);
                
                int system = Integer.parseInt(table.getValueAt(r, 2).toString());
                int actual = Integer.parseInt(table.getValueAt(r, 3).toString());
                
                comp.setForeground(darkCharcoal);
                comp.setBackground(Color.WHITE);
                
                if (system != actual) {
                    comp.setBackground(new Color(255, 235, 235));
                    if (c == 4 || c == 5) comp.setForeground(Color.RED);
                }

                if (isSelected) {
                    comp.setBackground(new Color(255, 180, 0, 150));
                    comp.setForeground(Color.BLACK);
                }

                if (c == 0 || c == 2 || c == 3 || c == 4 || c == 5) {
                    setHorizontalAlignment(JLabel.CENTER);
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }

                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableReconcile);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // --- South Panel: Operations ---
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlSouth.setOpaque(false);

        btnDeleteRow = new JButtonCustom("- Xóa dòng đã chọn", darkCharcoal, Color.WHITE);
        
        btnSave = new JButtonCustom("✓ Lưu Biên Bản Hao Hụt", jollibeeRed, Color.WHITE);

        btnExportPDF = new JButtonCustom("📄 Xuất Báo Cáo PDF", new Color(0, 123, 255), Color.WHITE);

        pnlSouth.add(btnDeleteRow);
        pnlSouth.add(btnExportPDF);
        pnlSouth.add(btnSave);
        add(pnlSouth, BorderLayout.SOUTH);

        // --- Actions ---
        cbNguyenLieu.addActionListener(e -> updateSystemQuantity());
        btnAddRow.addActionListener(e -> addReconcileRow());
        btnDeleteRow.addActionListener(e -> deleteSelectedRow());
        btnSave.addActionListener(e -> saveShortagesToDB());
        btnExportPDF.addActionListener(e -> performExportPDF());
    }

    private void loadIngredients() {
        cbNguyenLieu.removeAllItems();
        List<NguyenLieu> list = nguyenLieuDAO.getAllNguyenLieu();
        if (list != null) {
            for (NguyenLieu nl : list) {
                cbNguyenLieu.addItem(nl.getMaNL() + " | " + nl.getTenNL());
            }
        }
        updateSystemQuantity();
    }

    private void updateSystemQuantity() {
=======
        tableHaoHut = new JTable(tableModel);
        tableHaoHut.setRowHeight(28);
        tableHaoHut.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableHaoHut.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tableHaoHut.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(139, 69, 19)); // Nâu ấm
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        });

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tableHaoHut.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tableHaoHut.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        tableHaoHut.getColumnModel().getColumn(8).setCellRenderer(centerRender);

        DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
        rightRender.setHorizontalAlignment(JLabel.RIGHT);
        tableHaoHut.getColumnModel().getColumn(3).setCellRenderer(rightRender);
        tableHaoHut.getColumnModel().getColumn(4).setCellRenderer(rightRender);
        tableHaoHut.getColumnModel().getColumn(5).setCellRenderer(rightRender);
        tableHaoHut.getColumnModel().getColumn(6).setCellRenderer(rightRender);

        JScrollPane scrollPane = new JScrollPane(tableHaoHut);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(jollibeeRed, 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- EAST: FORM ---
        add(createDetailPanel(), BorderLayout.EAST);
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setOpaque(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        
        TitledBorder formBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(jollibeeRed, 2), "Ghi nhận Hao Hụt Thực Tế"
        );
        formBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        formBorder.setTitleColor(jollibeeRed);
        form.setBorder(formBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtMaHH = new JTextField();
        txtMaHH.setEditable(false);
        txtMaHH.setBackground(new Color(235, 235, 230));

        cbNguyenLieu = new JComboBox<>();
        
        txtSystemQty = new JTextField("0");
        txtSystemQty.setEditable(false);
        txtSystemQty.setBackground(new Color(235, 235, 230));

        txtPhysicalQty = new JTextField();
        
        txtDiscrepancyQty = new JTextField("0");
        txtDiscrepancyQty.setEditable(false);
        txtDiscrepancyQty.setBackground(new Color(235, 235, 230));

        txtPercentage = new JTextField("0.00 %");
        txtPercentage.setEditable(false);
        txtPercentage.setBackground(new Color(235, 235, 230));

        cbLyDo = new JComboBox<>(new String[]{
            "Hỏng hóc / Biến chất",
            "Mất mát / Thất thoát",
            "Sai lệch cân đo bàn giao",
            "Hết hạn sử dụng hư hỏng",
            "Lý do khác"
        });

        txtNgayGhiNhan = new JTextField();
        txtNgayGhiNhan.setEditable(false);
        txtNgayGhiNhan.setBackground(new Color(235, 235, 230));

        Dimension fieldSize = new Dimension(200, 30);
        txtMaHH.setPreferredSize(fieldSize);
        cbNguyenLieu.setPreferredSize(fieldSize);
        txtSystemQty.setPreferredSize(fieldSize);
        txtPhysicalQty.setPreferredSize(fieldSize);
        txtDiscrepancyQty.setPreferredSize(fieldSize);
        txtPercentage.setPreferredSize(fieldSize);
        cbLyDo.setPreferredSize(fieldSize);
        txtNgayGhiNhan.setPreferredSize(fieldSize);

        // Grid Mapping
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Mã báo cáo:"), gbc);
        gbc.gridx = 1; form.add(txtMaHH, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Nguyên liệu:"), gbc);
        gbc.gridx = 1; form.add(cbNguyenLieu, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Tồn hệ thống:"), gbc);
        gbc.gridx = 1; form.add(txtSystemQty, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Tồn thực tế:"), gbc);
        gbc.gridx = 1; form.add(txtPhysicalQty, gbc);

        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Hao hụt (Lượng):"), gbc);
        gbc.gridx = 1; form.add(txtDiscrepancyQty, gbc);

        gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Biên độ (%):"), gbc);
        gbc.gridx = 1; form.add(txtPercentage, gbc);

        gbc.gridx = 0; gbc.gridy = 6; form.add(new JLabel("Lý do hao hụt:"), gbc);
        gbc.gridx = 1; form.add(cbLyDo, gbc);

        gbc.gridx = 0; gbc.gridy = 7; form.add(new JLabel("Ngày ghi nhận:"), gbc);
        gbc.gridx = 1; form.add(txtNgayGhiNhan, gbc);

        panel.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        btnPanel.setOpaque(false);

        btnAdd = new JButton("Lưu báo cáo");
        btnClear = new JButton("Làm mới");
        btnExportPDF = new JButton("Xuất PDF");

        styleButton(btnAdd, new Color(40, 167, 69)); // Xanh lá
        styleButton(btnClear, darkCharcoal); // Xám
        styleButton(btnExportPDF, jollibeeRed); // Đỏ

        Dimension btnSize = new Dimension(105, 35);
        btnAdd.setPreferredSize(btnSize);
        btnClear.setPreferredSize(btnSize);
        btnExportPDF.setPreferredSize(btnSize);

        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);
        btnPanel.add(btnExportPDF);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Listeners for inputs
        cbNguyenLieu.addActionListener(e -> updateSystemQty());
        
        // Live calculation when typing physical quantity
        txtPhysicalQty.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculateDiscrepancy();
            }
        });

        btnAdd.addActionListener(e -> performSaveHaoHut());
        btnClear.addActionListener(e -> clearForm());
        btnExportPDF.addActionListener(e -> performExportPDF());

        return panel;
    }

    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void loadHaoHutData() {
        // Load cbNguyenLieu
        cbNguyenLieu.removeAllItems();
        List<NguyenLieu> listNL = nguyenLieuDAO.getAllNguyenLieu();
        if (listNL != null) {
            for (NguyenLieu nl : listNL) {
                cbNguyenLieu.addItem(nl.getMaNL() + " | " + nl.getTenNL());
            }
        }

        tableModel.setRowCount(0);
        List<HaoHut> list = haoHutDAO.getAllHaoHut();
        if (list != null) {
            for (HaoHut hh : list) {
                tableModel.addRow(new Object[]{
                    hh.getMaHH(),
                    hh.getMaNL(),
                    hh.getTenNL(),
                    hh.getSoLuongHeThong(),
                    hh.getSoLuongThucTe(),
                    hh.getSoLuongHaoHut(),
                    df.format(hh.getPhanTramHaoHut()) + " %",
                    hh.getLyDo(),
                    sdf.format(hh.getNgayGhiNhan())
                });
            }
        }
        // Tự động giãn cột bảng báo cáo hao hụt
        util.UIHelper.autoResizeColumnWidths(tableHaoHut);
        clearForm();
    }

    private void updateSystemQty() {
>>>>>>> origin/van
        if (cbNguyenLieu.getSelectedItem() != null) {
            String selected = cbNguyenLieu.getSelectedItem().toString();
            String maNL = selected.split(" \\| ")[0];
            int qty = nguyenLieuDAO.getStockQuantity(maNL);
<<<<<<< HEAD
            txtSoluongHeThong.setText(String.valueOf(qty));
            txtSoluongThucTe.setText(String.valueOf(qty)); // Default to matching
        }
    }

    private void addReconcileRow() {
        try {
            if (cbNguyenLieu.getSelectedItem() == null) return;
            String selected = cbNguyenLieu.getSelectedItem().toString();
            String maNL = selected.split(" \\| ")[0];
            String tenNL = selected.split(" \\| ")[1];

            int system = Integer.parseInt(txtSoluongHeThong.getText());
            int actual = Integer.parseInt(txtSoluongThucTe.getText().trim());
            String lyDo = cbLyDo.getSelectedItem().toString();

            if (actual < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng thực tế không được âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int haoHut = system - actual;
            double phanTram = 0;
            if (system > 0) {
                phanTram = ((double) haoHut / system) * 100;
            }

            // Check duplicates
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equals(maNL)) {
                    tableModel.setValueAt(actual, i, 3);
                    tableModel.setValueAt(haoHut, i, 4);
                    tableModel.setValueAt(String.format("%.1f%%", phanTram), i, 5);
                    tableModel.setValueAt(lyDo, i, 6);
                    return;
                }
            }

            tableModel.addRow(new Object[]{
                    maNL, tenNL, system, actual, haoHut, String.format("%.1f%%", phanTram), lyDo
            });
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng thực tế hợp lệ!", "Lỗi nhập số", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedRow() {
        int selected = tableReconcile.getSelectedRow();
        if (selected >= 0) {
            tableModel.removeRow(selected);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveShortagesToDB() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Biên bản đối chiếu trống rỗng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String latestMaHH = "HH01";
            String sqlMax = "SELECT TOP 1 MaHH FROM HaoHut ORDER BY MaHH DESC";
            try (PreparedStatement ps = conn.prepareStatement(sqlMax);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String last = rs.getString("MaHH");
                    if (last != null && last.startsWith("HH")) {
                        String base = last;
                        if (last.contains("_")) {
                            base = last.split("_")[0];
                        }
                        try {
                            int num = Integer.parseInt(base.substring(2)) + 1;
                            latestMaHH = String.format("HH%02d", num);
                        } catch (NumberFormatException e) {
                            latestMaHH = "HH01";
                        }
                    }
                }
            }

            String sqlInsert = "INSERT INTO HaoHut (MaHH, NgayBaoCao, MaNL, SoLuongHeThong, SoLuongThucTe, SoLuongHaoHut, PhanTramHaoHut, LyDo, MaNV) " +
                               "VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?, ?)";
            
            String maNV = currentUser != null ? currentUser.getMaNV() : "NV01";

            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                int index = 0;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String maNL = tableModel.getValueAt(i, 0).toString();
                    int system = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                    int actual = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
                    int haoHut = Integer.parseInt(tableModel.getValueAt(i, 4).toString());
                    
                    String pctStr = tableModel.getValueAt(i, 5).toString().replace("%", "");
                    double pct = Double.parseDouble(pctStr);
                    
                    String lyDo = tableModel.getValueAt(i, 6).toString();

                    String maHHRow = latestMaHH + "_" + (++index);

                    ps.setString(1, maHHRow);
                    ps.setString(2, maNL);
                    ps.setInt(3, system);
                    ps.setInt(4, actual);
                    ps.setInt(5, haoHut);
                    ps.setBigDecimal(6, new BigDecimal(pct));
                    ps.setString(7, lyDo);
                    ps.setString(8, maNV);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Lưu biên bản đối chiếu hao hụt kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lưu biên bản thất bại: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
=======
            txtSystemQty.setText(String.valueOf(qty));
            calculateDiscrepancy();
        }
    }

    private void calculateDiscrepancy() {
        try {
            int system = Integer.parseInt(txtSystemQty.getText());
            String physStr = txtPhysicalQty.getText().trim();
            if (physStr.isEmpty()) {
                txtDiscrepancyQty.setText("0");
                txtPercentage.setText("0.00 %");
                return;
            }
            int physical = Integer.parseInt(physStr);
            
            int diff = system - physical;
            double percent = 0.00;
            if (system > 0) {
                percent = ((double) diff / system) * 100;
            }
            
            txtDiscrepancyQty.setText(String.valueOf(diff));
            txtPercentage.setText(df.format(percent) + " %");
        } catch (NumberFormatException e) {
            txtDiscrepancyQty.setText("0");
            txtPercentage.setText("0.00 %");
        }
    }

    private void clearForm() {
        txtMaHH.setText(haoHutDAO.generateNextMaHH());
        txtPhysicalQty.setText("");
        txtDiscrepancyQty.setText("0");
        txtPercentage.setText("0.00 %");
        txtNgayGhiNhan.setText(sdf.format(new Date()));
        cbLyDo.setSelectedIndex(0);
        if (cbNguyenLieu.getItemCount() > 0) {
            cbNguyenLieu.setSelectedIndex(0);
        }
        updateSystemQty();
    }

    private void performSaveHaoHut() {
        if (txtPhysicalQty.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng thực tế kiểm kê!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String selectedNL = cbNguyenLieu.getSelectedItem().toString();
            String maNL = selectedNL.split(" \\| ")[0];
            int system = Integer.parseInt(txtSystemQty.getText());
            int physical = Integer.parseInt(txtPhysicalQty.getText().trim());
            int diff = system - physical;
            
            double percent = 0.00;
            if (system > 0) {
                percent = ((double) diff / system) * 100;
            }

            if (physical < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng kiểm kê thực tế không được âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            HaoHut hh = new HaoHut();
            hh.setMaHH(txtMaHH.getText());
            hh.setMaNL(maNL);
            hh.setSoLuongHeThong(system);
            hh.setSoLuongThucTe(physical);
            hh.setSoLuongHaoHut(diff);
            hh.setPhanTramHaoHut(percent);
            hh.setLyDo(cbLyDo.getSelectedItem().toString());
            hh.setNgayGhiNhan(new Date());

            if (haoHutDAO.addHaoHut(hh)) {
                // Đồng thời cập nhật lại số lượng trong bảng NguyenLieu
                try (Connection conn = util.DBConnection.getConnection()) {
                    if (conn != null) {
                        conn.setAutoCommit(false);
                        nguyenLieuDAO.updateStockQuantity(conn, maNL, -diff);
                        conn.commit();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(this, "Lưu báo cáo hao hụt và cân bằng kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadHaoHutData();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu báo cáo hao hụt thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng thực tế nhập vào phải là số nguyên!", "Sai định dạng", JOptionPane.ERROR_MESSAGE);
>>>>>>> origin/van
        }
    }

    private void performExportPDF() {
<<<<<<< HEAD
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu đối chiếu hao hụt để xuất báo cáo!", "Bảng trống", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Báo Cáo Hao Hụt PDF");
        fileChooser.setSelectedFile(new File("BaoCao_HaoHut_Kho.pdf"));

        int choice = fileChooser.showSaveDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File destFile = fileChooser.getSelectedFile();
            if (!destFile.getName().toLowerCase().endsWith(".pdf")) {
                destFile = new File(destFile.getParentFile(), destFile.getName() + ".pdf");
            }

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    // Using PDType1Font.HELVETICA_BOLD and unsensitized Vietnamese text to prevent encoding issues!
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    contentStream.newLineAtOffset(50, 750);
                    contentStream.showText("JOLLIBEE PAM NGOC THACH - BIEN BAN HAO HUT KHO");
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(50, 720);
                    contentStream.showText("Ngay lap bao cao: " + sdf.format(new Date()));
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(50, 705);
                    String nvStr = currentUser != null ? currentUser.getTenDangNhap() : "Admin";
                    contentStream.showText("Nguoi lap bien ban: " + nvStr);
                    contentStream.endText();

                    // Table Header
                    int yPosition = 660;
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText("Ma NL");
                    contentStream.newLineAtOffset(70, 0);
                    contentStream.showText("Ten Nguyen Lieu");
                    contentStream.newLineAtOffset(180, 0);
                    contentStream.showText("SL He thong");
                    contentStream.newLineAtOffset(80, 0);
                    contentStream.showText("SL Thuc te");
                    contentStream.newLineAtOffset(80, 0);
                    contentStream.showText("Hao hut");
                    contentStream.endText();

                    // Line separator
                    contentStream.moveTo(50, yPosition - 5);
                    contentStream.lineTo(550, yPosition - 5);
                    contentStream.stroke();

                    yPosition -= 20;

                    // Write rows
                    contentStream.setFont(PDType1Font.HELVETICA, 9);
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        String maNL = tableModel.getValueAt(i, 0).toString();
                        String tenNL = tableModel.getValueAt(i, 1).toString();
                        // Unsensitize Vietnamese characters for tenNL to prevent PDFBox Helvetica rendering crash!
                        String safeTenNL = removeVietnameseAccents(tenNL);
                        
                        String system = tableModel.getValueAt(i, 2).toString();
                        String actual = tableModel.getValueAt(i, 3).toString();
                        String haoHut = tableModel.getValueAt(i, 4).toString();

                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yPosition);
                        contentStream.showText(maNL);
                        contentStream.newLineAtOffset(70, 0);
                        contentStream.showText(safeTenNL);
                        contentStream.newLineAtOffset(180, 0);
                        contentStream.showText(system);
                        contentStream.newLineAtOffset(80, 0);
                        contentStream.showText(actual);
                        contentStream.newLineAtOffset(80, 0);
                        contentStream.showText(haoHut);
                        contentStream.endText();

                        yPosition -= 20;
                        if (yPosition < 50) {
                            // Simple layout breaks
                            break;
                        }
                    }
                }

                document.save(destFile);
                JOptionPane.showMessageDialog(this, "Biên bản báo cáo hao hụt PDF được xuất thành công!\nLưu tại: " + destFile.getAbsolutePath(), "Xuất PDF thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xảy ra trong quá trình xuất PDF: " + ex.getMessage(), "Lỗi xuất file", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private String removeVietnameseAccents(String str) {
        if (str == null) return "";
        String[] accents = {
            "aàảãáạăằẳẵắặâầẩẫấự",
            "AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤỰ",
            "eèẻẽéẹêềểễếệ",
            "EÈẺẼÉẸÊỀỂỄẾỆ",
            "iìỉĩíị",
            "IÌỈĨÍỊ",
            "oòỏõóọôồổỗốộơờởỡớợ",
            "OÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢ",
            "uùủũúụưừửữứự",
            "UÙỦŨÚỤƯỪỬỮỨỰ",
            "yỳỷỹýỵ",
            "YỲỶỸÝỴ",
            "dđ",
            "DĐ"
        };
        String[] replaces = {
            "a", "A", "e", "E", "i", "I", "o", "O", "u", "U", "y", "Y", "d", "D"
        };
        for (int i = 0; i < accents.length; i++) {
            for (char c : accents[i].toCharArray()) {
                str = str.replace(c, replaces[i].charAt(0));
            }
        }
        return str;
=======
        JOptionPane.showMessageDialog(this,
            "Chức năng xuất PDF hiện không khả dụng trong môi trường này.\n" +
            "Vui lòng thêm thư viện PDFBox vào classpath hoặc kiểm tra cài đặt dự án.",
            "Chức năng tạm thời bị vô hiệu hoá",
            JOptionPane.INFORMATION_MESSAGE);
>>>>>>> origin/van
    }
}
