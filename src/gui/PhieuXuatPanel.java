package gui;

import bus.PhieuXuatBUS;
import dao.NguyenLieuDAO;
import model.ChiTietPhieuXuat;
import model.NguyenLieu;
import model.PhieuXuat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class PhieuXuatPanel extends JPanel {

    // =====================================================
    // BUS + DAO
    // =====================================================
    private final PhieuXuatBUS bus;
    private final NguyenLieuDAO nguyenLieuDAO;

    // =====================================================
    // USER
    // =====================================================
    private final String currentUser;

    // =====================================================
    // TABLE
    // =====================================================
    private JTable tablePX;
    private JTable tableCT;

    private DefaultTableModel modelPX;
    private DefaultTableModel modelCT;

    // =====================================================
    // INPUT
    // =====================================================
    private JTextField txtMaPX;
    private JTextField txtNgayXuat;
    private JTextField txtMaNV;

    private JTextField txtMaCTPX;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;

    // DROPDOWN NGUYÊN LIỆU
    private JComboBox<String> cboNguyenLieu;

    // =====================================================
    // BUTTON
    // =====================================================
    private JButton btnThem;
    private JButton btnLuu;
    private JButton btnMoi;
    private JButton btnXoaCT;
    private JButton btnXoaPX;

    // =====================================================
    // LABEL
    // =====================================================
    private JLabel lblTongTien;

    // =====================================================
    // DATA
    // =====================================================
    private final List<ChiTietPhieuXuat> dsTam;
    private List<NguyenLieu> dsNguyenLieu;

    private BigDecimal tongTien;

    private final DecimalFormat df =
            new DecimalFormat("#,##0");

    // =====================================================
    // COLOR
    // =====================================================
    private final Color BG =
            new Color(242, 245, 250);

    private final Color CARD =
            Color.WHITE;

    private final Color PRIMARY =
            new Color(52, 73, 94);

    private final Color BLUE =
            new Color(52, 152, 219);

    private final Color GREEN =
            new Color(46, 204, 113);

    private final Color RED =
            new Color(231, 76, 60);

    private final Color ORANGE =
            new Color(243, 156, 18);

    private final Color BORDER =
            new Color(220, 220, 220);

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public PhieuXuatPanel(String currentUser) {

        this.currentUser = currentUser;

        bus = new PhieuXuatBUS();

        nguyenLieuDAO = new NguyenLieuDAO();

        dsTam = new ArrayList<>();

        tongTien = BigDecimal.ZERO;

        initComponents();

        loadNguyenLieu();

        loadData();

        taoPhieuMoi();
    }

    // =====================================================
    // UI
    // =====================================================
    private void initComponents() {

        setLayout(new BorderLayout(15, 15));

        setBackground(BG);

        setBorder(new EmptyBorder(15, 15, 15, 15));

        // =====================================================
        // TITLE
        // =====================================================
        JLabel lblTitle =
                new JLabel("QUẢN LÝ PHIẾU XUẤT KHO");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );

        lblTitle.setForeground(PRIMARY);

        JPanel pnlTitle =
                new JPanel(new FlowLayout(FlowLayout.CENTER));

        pnlTitle.setBackground(BG);

        pnlTitle.add(lblTitle);

        add(pnlTitle, BorderLayout.NORTH);

        // =====================================================
        // LEFT
        // =====================================================
        JPanel leftPanel =
                new JPanel(new BorderLayout(10, 10));

        leftPanel.setBackground(BG);

        JPanel formPanel =
                new JPanel(new GridLayout(7, 2, 12, 12));

        formPanel.setBackground(CARD);

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        txtMaPX = createTextField(false);

        txtNgayXuat = createTextField(false);

        txtMaNV = createTextField(false);

        txtMaCTPX = createTextField(false);

        txtSoLuong = createTextField(true);

        txtDonGia = createTextField(true);

        cboNguyenLieu = new JComboBox<>();

        styleComboBox(cboNguyenLieu);

        txtNgayXuat.setText(
                new SimpleDateFormat("yyyy-MM-dd")
                        .format(new java.util.Date())
        );

        txtMaNV.setText(currentUser);

        formPanel.add(createLabel("Mã Phiếu Xuất"));
        formPanel.add(txtMaPX);

        formPanel.add(createLabel("Ngày Xuất"));
        formPanel.add(txtNgayXuat);

        formPanel.add(createLabel("Nhân Viên"));
        formPanel.add(txtMaNV);

        formPanel.add(createLabel("Mã Chi Tiết"));
        formPanel.add(txtMaCTPX);

        formPanel.add(createLabel("Nguyên Liệu"));
        formPanel.add(cboNguyenLieu);

        formPanel.add(createLabel("Số Lượng"));
        formPanel.add(txtSoLuong);

        formPanel.add(createLabel("Đơn Giá"));
        formPanel.add(txtDonGia);

        // =====================================================
        // BUTTONS
        // =====================================================
        JPanel buttonPanel =
                new JPanel(new GridLayout(3, 2, 10, 10));

        buttonPanel.setBackground(BG);

        btnThem =
                createButton("THÊM CHI TIẾT", BLUE);

        btnLuu =
                createButton("LƯU PHIẾU", GREEN);

        btnMoi =
                createButton("PHIẾU MỚI", ORANGE);

        btnXoaCT =
                createButton("XÓA CHI TIẾT", RED);

        btnXoaPX =
                createButton("XÓA PHIẾU", PRIMARY);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnMoi);
        buttonPanel.add(btnXoaCT);
        buttonPanel.add(btnXoaPX);

        leftPanel.add(formPanel, BorderLayout.CENTER);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // =====================================================
        // RIGHT
        // =====================================================
        JPanel rightPanel =
                new JPanel(new BorderLayout(10, 10));

        rightPanel.setBackground(BG);

        // =====================================================
        // TABLE PX
        // =====================================================
        modelPX =
                new DefaultTableModel(
                        new String[]{
                                "Mã PX",
                                "Ngày Xuất",
                                "Nhân Viên",
                                "Tổng Tiền"
                        }, 0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        tablePX = createTable(modelPX);

        JScrollPane scrollPX =
                new JScrollPane(tablePX);

        scrollPX.setBorder(
                BorderFactory.createTitledBorder(
                        "LỊCH SỬ PHIẾU XUẤT"
                )
        );

        // =====================================================
        // TABLE DETAIL
        // =====================================================
        modelCT =
                new DefaultTableModel(
                        new String[]{
                                "Mã CT",
                                "Nguyên Liệu",
                                "Số Lượng",
                                "Đơn Giá",
                                "Thành Tiền"
                        }, 0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        tableCT = createTable(modelCT);

        JScrollPane scrollCT =
                new JScrollPane(tableCT);

        scrollCT.setBorder(
                BorderFactory.createTitledBorder(
                        "CHI TIẾT PHIẾU XUẤT"
                )
        );

        JSplitPane split =
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        scrollPX,
                        scrollCT
                );

        split.setDividerLocation(260);

        rightPanel.add(split, BorderLayout.CENTER);

        // =====================================================
        // TOTAL
        // =====================================================
        JPanel totalPanel =
                new JPanel(new FlowLayout(FlowLayout.RIGHT));

        totalPanel.setBackground(BG);

        JLabel lbl =
                new JLabel("TỔNG TIỀN:");

        lbl.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        lblTongTien =
                new JLabel("0 VNĐ");

        lblTongTien.setForeground(RED);

        lblTongTien.setFont(
                new Font("Segoe UI", Font.BOLD, 24)
        );

        totalPanel.add(lbl);

        totalPanel.add(lblTongTien);

        rightPanel.add(totalPanel, BorderLayout.SOUTH);

        // =====================================================
        // MAIN SPLIT
        // =====================================================
        JSplitPane mainSplit =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        leftPanel,
                        rightPanel
                );

        mainSplit.setDividerLocation(380);

        add(mainSplit, BorderLayout.CENTER);

        // =====================================================
        // EVENTS
        // =====================================================
        btnThem.addActionListener(e -> themChiTiet());

        btnLuu.addActionListener(e -> luuPhieu());

        btnMoi.addActionListener(e -> taoPhieuMoi());

        btnXoaCT.addActionListener(e -> xoaChiTiet());

        btnXoaPX.addActionListener(e -> {
            try {
                xoaPhieuXuat();
            } catch (Exception e1) {
                
                e1.printStackTrace();
            }
        });

        tablePX.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        hienThiChiTiet();
                    }
                });
    }

    // =====================================================
    // LOAD NGUYÊN LIỆU
    // =====================================================
    private void loadNguyenLieu() {

        dsNguyenLieu =
                nguyenLieuDAO.getAll();

        cboNguyenLieu.removeAllItems();

        for (NguyenLieu nl : dsNguyenLieu) {

            cboNguyenLieu.addItem(
                    nl.getMaNL()
                            + " - "
                            + nl.getTenNL()
                            + " (Tồn: "
                            + nl.getSoLuong()
                            + ")"
            );
        }
    }

    // =====================================================
    // ADD DETAIL
    // =====================================================
    private void themChiTiet() {

        try {

            if (cboNguyenLieu.getSelectedIndex() == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn nguyên liệu!"
                );

                return;
            }

            String selected =
                    cboNguyenLieu.getSelectedItem().toString();

            String maNL =
                    selected.split(" - ")[0];

            ChiTietPhieuXuat ct =
                    new ChiTietPhieuXuat();

            ct.setMaCTPX(txtMaCTPX.getText());

            ct.setMaPX(txtMaPX.getText());

            ct.setMaNL(maNL);

            ct.setSoLuong(
                    Integer.parseInt(
                            txtSoLuong.getText()
                    )
            );

            ct.setDonGia(
                    new BigDecimal(
                            txtDonGia.getText()
                    )
            );

            dsTam.add(ct);

            BigDecimal thanhTien =
                    ct.getDonGia().multiply(
                            BigDecimal.valueOf(
                                    ct.getSoLuong()
                            )
                    );

            tongTien =
                    tongTien.add(thanhTien);

            modelCT.addRow(
                    new Object[]{
                            ct.getMaCTPX(),
                            maNL,
                            ct.getSoLuong(),
                            df.format(ct.getDonGia()),
                            df.format(thanhTien)
                    }
            );

            updateTongTien();

            txtMaCTPX.setText(
                    bus.generateNextMaCTPX()
            );

            txtSoLuong.setText("");

            txtDonGia.setText("");

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );
        }
    }

    // =====================================================
    // SAVE
    // =====================================================
    private void luuPhieu() {

        try {

            if (dsTam.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Danh sách chi tiết đang trống!"
                );

                return;
            }

            PhieuXuat px =
                    new PhieuXuat();

            px.setMaPX(txtMaPX.getText());

            px.setNgayXuat(
                    Date.valueOf(
                            txtNgayXuat.getText()
                    )
            );

            px.setMaNV(txtMaNV.getText());

            px.setTongTien(tongTien);

            boolean success =
                    bus.luuTronGoiPhieuXuat(
                            px,
                            dsTam
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Lưu phiếu xuất thành công!"
                );

                loadData();

                loadNguyenLieu();

                taoPhieuMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Lưu thất bại!"
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );
        }
    }

    // =====================================================
    // DELETE PX
    // =====================================================
    private void xoaPhieuXuat() throws Exception {

        int row =
                tablePX.getSelectedRow();

        if (row < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Chọn phiếu xuất cần xóa!"
            );

            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Bạn chắc chắn muốn xóa?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String maPX =
                modelPX.getValueAt(row, 0).toString();

        boolean success =
                bus.xoaPhieuXuat(maPX);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Xóa thành công!"
            );

            loadData();

            modelCT.setRowCount(0);

            loadNguyenLieu();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Xóa thất bại!"
            );
        }
    }

    // =====================================================
    // LOAD DATA
    // =====================================================
    private void loadData() {

        modelPX.setRowCount(0);

        List<PhieuXuat> list =
                bus.getAllPhieuXuat();

        for (PhieuXuat px : list) {

            modelPX.addRow(
                    new Object[]{
                            px.getMaPX(),
                            px.getNgayXuat(),
                            px.getMaNV(),
                            df.format(px.getTongTien())
                    }
            );
        }
    }

    // =====================================================
    // SHOW DETAIL
    // =====================================================
    private void hienThiChiTiet() {

        int row =
                tablePX.getSelectedRow();

        if (row < 0) {
            return;
        }

        String maPX =
                modelPX.getValueAt(row, 0).toString();

        List<ChiTietPhieuXuat> list =
                bus.getChiTietByMaPX(maPX);

        modelCT.setRowCount(0);

        BigDecimal tong = BigDecimal.ZERO;

        for (ChiTietPhieuXuat ct : list) {

            BigDecimal thanhTien =
                    ct.getDonGia().multiply(
                            BigDecimal.valueOf(
                                    ct.getSoLuong()
                            )
                    );

            tong = tong.add(thanhTien);

            modelCT.addRow(
                    new Object[]{
                            ct.getMaCTPX(),
                            ct.getMaNL(),
                            ct.getSoLuong(),
                            df.format(ct.getDonGia()),
                            df.format(thanhTien)
                    }
            );
        }

        lblTongTien.setText(
                df.format(tong) + " VNĐ"
        );
    }

    // =====================================================
    // NEW
    // =====================================================
    private void taoPhieuMoi() {

        txtMaPX.setText(
                bus.generateNextMaPX()
        );

        txtMaCTPX.setText(
                bus.generateNextMaCTPX()
        );

        txtSoLuong.setText("");

        txtDonGia.setText("");

        dsTam.clear();

        modelCT.setRowCount(0);

        tongTien = BigDecimal.ZERO;

        updateTongTien();
    }

    // =====================================================
    // DELETE DETAIL
    // =====================================================
    private void xoaChiTiet() {

        int row =
                tableCT.getSelectedRow();

        if (row < 0) return;

        dsTam.remove(row);

        modelCT.removeRow(row);

        tinhLaiTongTien();
    }

    // =====================================================
    // TOTAL
    // =====================================================
    private void tinhLaiTongTien() {

        tongTien = BigDecimal.ZERO;

        for (ChiTietPhieuXuat ct : dsTam) {

            tongTien =
                    tongTien.add(
                            ct.getDonGia().multiply(
                                    BigDecimal.valueOf(
                                            ct.getSoLuong()
                                    )
                            )
                    );
        }

        updateTongTien();
    }

    private void updateTongTien() {

        lblTongTien.setText(
                df.format(tongTien)
                        + " VNĐ"
        );
    }

    // =====================================================
    // UI HELPER
    // =====================================================
    private JLabel createLabel(String text) {

        JLabel lbl =
                new JLabel(text);

        lbl.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        return lbl;
    }

    private JTextField createTextField(
            boolean editable
    ) {

        JTextField txt =
                new JTextField();

        txt.setEditable(editable);

        txt.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        txt.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER),
                        new EmptyBorder(10, 12, 10, 12)
                )
        );

        return txt;
    }

    private JButton createButton(
            String text,
            Color bg
    ) {

        JButton btn =
                new JButton(text);

        btn.setBackground(bg);

        btn.setForeground(Color.WHITE);

        btn.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btn.setFocusPainted(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setPreferredSize(
                new Dimension(140, 45)
        );

        return btn;
    }

    private JTable createTable(
            DefaultTableModel model
    ) {

        JTable table =
                new JTable(model);

        table.setRowHeight(32);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        JTableHeader header =
                table.getTableHeader();

        header.setBackground(PRIMARY);

        header.setForeground(Color.WHITE);

        header.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        table.setDefaultRenderer(
                Object.class,
                center
        );

        return table;
    }

    private void styleComboBox(
            JComboBox<String> combo
    ) {

        combo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        combo.setBackground(Color.WHITE);

        combo.setBorder(
                new LineBorder(BORDER)
        );
    }
}