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

    private JTable tablePhieuXuat;
    private DefaultTableModel tableModel;
    private JButton btnThemMoi;
    private JButton btnXemChiTiet;
    private JButton btnLamMoi;
    private JButton btnExportExcel;
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
        setBackground(new Color(255, 252, 245));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ KHO NGUYÊN LIỆU - PHIẾU XUẤT KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(139, 69, 19));
        pnlNorth.add(lblTitle, BorderLayout.WEST);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearch.setOpaque(false);
        pnlSearch.add(new JLabel("Tìm kiếm mã phiếu: "));
        txtTimKiem = new JTextField(15);
        pnlSearch.add(txtTimKiem);
        
        JButton btnTimKiem = new JButtonCustom("Tìm", new Color(139, 69, 19), Color.WHITE);
        btnTimKiem.setPreferredSize(new Dimension(80, 28));
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

        tablePhieuXuat = new JTable(tableModel);
        tablePhieuXuat.setRowHeight(30);
        tablePhieuXuat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablePhieuXuat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablePhieuXuat.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                setBackground(new Color(139, 69, 19));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                
                return this;
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
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlSouth.setOpaque(false);

        btnThemMoi = new JButtonCustom("+ Tạo Phiếu Xuất Mới", new Color(224, 31, 42), Color.WHITE);
        btnThemMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnXemChiTiet = new JButtonCustom("👁 Xem Chi Tiết Chứng Từ", new Color(242, 142, 43), Color.WHITE);
        btnXemChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        btnLamMoi = new JButtonCustom("🔄 Làm Mới Tải Lại", new Color(45, 45, 45), Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnExportExcel = new JButtonCustom("📊 Xuất Excel", new Color(40, 167, 69), Color.WHITE);
        btnExportExcel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        pnlSouth.add(btnThemMoi);
        pnlSouth.add(btnXemChiTiet);
        pnlSouth.add(btnLamMoi);
        pnlSouth.add(btnExportExcel);



        add(pnlSouth, BorderLayout.SOUTH);

        btnThemMoi.addActionListener(e -> btnThemMoiActionPerformed());
        btnXemChiTiet.addActionListener(e -> btnXemChiTietActionPerformed());
        btnLamMoi.addActionListener(e -> refreshData());
        btnExportExcel.addActionListener(e -> btnExportExcelActionPerformed());
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
            String maPX = tablePhieuXuat.getValueAt(selectedRow, 0).toString();
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            TaoPhieuXuatDialog dialog = new TaoPhieuXuatDialog(mainFrame, maPX);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng phiếu xuất kho trên bảng danh sách để xem chi tiết!", "Chưa chọn chứng từ", JOptionPane.WARNING_MESSAGE);
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

    private void btnExportExcelActionPerformed() {
        int selectedRow = tablePhieuXuat.getSelectedRow();
        if (selectedRow >= 0) {
            String maPX = tablePhieuXuat.getValueAt(selectedRow, 0).toString();
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            util.ExcelExporter.exportPhieuXuat(mainFrame, maPX);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng phiếu xuất kho trên bảng danh sách để xuất file Excel!", "Chưa chọn chứng từ", JOptionPane.WARNING_MESSAGE);
        }
    }
}
