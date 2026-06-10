package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import dao.PhieuXuatDAO;
import model.PhieuXuat;
import model.TaiKhoan;

public class XuatKhoUI extends JPanel {

    // =====================================================================
    // BẢNG MÀU THƯƠNG HIỆU JOLLIBEE (ĐỒNG BỘ VỚI NHAPKHOUI VÀ NGUYENLIEUUI)
    // =====================================================================
    private static final Color JOLLIBEE_RED    = new Color(227, 29, 43);
    private static final Color TABLE_HEADER_BG = new Color(180, 30, 45);
    private static final Color ROW_ODD         = new Color(255, 253, 245);
    private static final Color ROW_EVEN        = new Color(245, 240, 230);

    private JTable tablePhieuXuat;
    private DefaultTableModel tableModel;
    private JButton btnThemMoi;
    private JButton btnXemChiTiet;
    private JButton btnLamMoi;
    private JTextField txtTimKiem;

    private PhieuXuatDAO phieuXuatDAO;
    private TaiKhoan currentUser;

    public XuatKhoUI(TaiKhoan user) {
        this.currentUser = user;
        this.phieuXuatDAO = new PhieuXuatDAO();
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(255, 253, 240)); // Nền kem Jollibee #FFFDF0
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ KHO JOLLIBEE - PHIẾU XUẤT KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(224, 31, 42)); // Đỏ Jollibee
        pnlNorth.add(lblTitle, BorderLayout.WEST);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearch.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Tìm kiếm mã phiếu: ");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlSearch.add(lblSearch);
        
        txtTimKiem = new JTextField(15);
        txtTimKiem.setPreferredSize(new Dimension(150, 28));
        pnlSearch.add(txtTimKiem);
        
        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(new Color(242, 142, 43));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setBorderPainted(false);
        btnTimKiem.putClientProperty("JButton.buttonType", "roundRect");
        pnlSearch.add(btnTimKiem);
        pnlNorth.add(pnlSearch, BorderLayout.EAST);

        add(pnlNorth, BorderLayout.NORTH);

        String[] columns = {"Mã Phiếu Xuất", "Ngày Xuất Kho", "Nhân Viên Lập", "Tổng Tiền (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePhieuXuat = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(255, 180, 0, 200)); // highlight vàng Jollibee
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        tablePhieuXuat.setRowHeight(32);
        tablePhieuXuat.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablePhieuXuat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePhieuXuat.setGridColor(new Color(220, 210, 195));
        tablePhieuXuat.setShowGrid(true);
        tablePhieuXuat.setIntercellSpacing(new Dimension(1, 1));

        tablePhieuXuat.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(TABLE_HEADER_BG);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, JOLLIBEE_RED));
                return lbl;
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablePhieuXuat.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablePhieuXuat.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tablePhieuXuat.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        JScrollPane scrollPane = new JScrollPane(tablePhieuXuat);
        scrollPane.getViewport().setBackground(ROW_ODD);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 31, 42), 1));
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlSouth.setOpaque(false);

        btnThemMoi = new JButton("+ Tạo Phiếu Xuất Mới");
        btnXemChiTiet = new JButton("👁 Xem Chi Tiết");
        btnLamMoi = new JButton("🔄 Làm Mới");

        btnThemMoi.setBackground(new Color(224, 31, 42));
        btnThemMoi.setForeground(Color.WHITE);
        btnThemMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThemMoi.setFocusPainted(false);
        btnThemMoi.setBorderPainted(false);
        btnThemMoi.putClientProperty("JButton.buttonType", "roundRect");

        btnXemChiTiet.setBackground(new Color(242, 142, 43));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXemChiTiet.setFocusPainted(false);
        btnXemChiTiet.setBorderPainted(false);
        btnXemChiTiet.putClientProperty("JButton.buttonType", "roundRect");

        btnLamMoi.setBackground(new Color(45, 45, 45));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setBorderPainted(false);
        btnLamMoi.putClientProperty("JButton.buttonType", "roundRect");

        pnlSouth.add(btnThemMoi);
        pnlSouth.add(btnXemChiTiet);
        pnlSouth.add(btnLamMoi);

        add(pnlSouth, BorderLayout.SOUTH);

        btnThemMoi.addActionListener(e -> btnThemMoiActionPerformed());
        btnXemChiTiet.addActionListener(e -> btnXemChiTietActionPerformed());
        btnLamMoi.addActionListener(e -> refreshData());
        btnTimKiem.addActionListener(e -> performSearch());
        txtTimKiem.addActionListener(e -> performSearch());
    }

    public void refreshData() {
        if (phieuXuatDAO != null) {
            List<PhieuXuat> list = phieuXuatDAO.getAllPhieuXuat();
            loadDataPhieuXuat(list);
        }
    }

    private void loadDataPhieuXuat(List<PhieuXuat> list) {
        tableModel.setRowCount(0);
        if (list != null) {
            for (PhieuXuat px : list) {
                String tongTienStr = String.format("%,.0f", px.getTongTien());

                tableModel.addRow(new Object[]{
                    px.getMaPX(),
                    (px.getNgayXuat() != null) ? px.getNgayXuat().toString() : "",
                    px.getTenNV(),
                    tongTienStr
                });
            }
        }
        // Tự động giãn cột bảng danh sách phiếu xuất
        util.UIHelper.autoResizeColumnWidths(tablePhieuXuat);
    }

    private void btnThemMoiActionPerformed() {
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        TaoPhieuXuatDialog dialog = new TaoPhieuXuatDialog(mainFrame, currentUser);
        dialog.setVisible(true);
        refreshData();
    }

    private void btnXemChiTietActionPerformed() {
        int selectedRow = tablePhieuXuat.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = tablePhieuXuat.convertRowIndexToModel(selectedRow);
            String maPX = tablePhieuXuat.getValueAt(modelRow, 0).toString();
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            TaoPhieuXuatDialog dialog = new TaoPhieuXuatDialog(mainFrame, maPX);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng phiếu xuất trên bảng để xem chi tiết!", "Chưa chọn chứng từ", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performSearch() {
        String keyword = txtTimKiem.getText().trim().toUpperCase();
        if (keyword.isEmpty()) {
            refreshData();
            return;
        }
        
        if (phieuXuatDAO != null) {
            List<PhieuXuat> allList = phieuXuatDAO.getAllPhieuXuat();
            List<PhieuXuat> filteredList = new java.util.ArrayList<>();
            
            for (PhieuXuat px : allList) {
                if (px.getMaPX().toUpperCase().contains(keyword)) {
                    filteredList.add(px);
                }
            }
            loadDataPhieuXuat(filteredList);
        }
    }
}
