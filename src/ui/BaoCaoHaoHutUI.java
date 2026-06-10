package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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
    private NguyenLieuDAO nguyenLieuDAO;
    private TaiKhoan currentUser;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableHaoHut = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
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
        tableHaoHut.setRowHeight(32);
        tableHaoHut.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableHaoHut.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHaoHut.setGridColor(new Color(220, 210, 195));
        tableHaoHut.setShowGrid(true);
        tableHaoHut.setIntercellSpacing(new Dimension(1, 1));

        tableHaoHut.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(new Color(180, 30, 45)); // Đỏ sậm Jollibee giống NhapKhoUI
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, jollibeeRed));
                return lbl;
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
        scrollPane.getViewport().setBackground(new Color(255, 253, 245));
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
        if (cbNguyenLieu.getSelectedItem() != null) {
            String selected = cbNguyenLieu.getSelectedItem().toString();
            String maNL = selected.split(" \\| ")[0];
            int qty = nguyenLieuDAO.getStockQuantity(maNL);
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
        }
    }

    private void performExportPDF() {
        JOptionPane.showMessageDialog(this,
            "Chức năng xuất PDF hiện không khả dụng trong môi trường này.\n" +
            "Vui lòng thêm thư viện PDFBox vào classpath hoặc kiểm tra cài đặt dự án.",
            "Chức năng tạm thời bị vô hiệu hoá",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
