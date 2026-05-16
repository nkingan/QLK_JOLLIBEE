package gui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainGUI extends JFrame {

    private String currentUser;
    private JTabbedPane tabs;

    // ===== NGUYÊN LIỆU =====
    private DefaultTableModel modelNL;
    private JTable tableNL;
    private JTextField txtNL_Ma, txtNL_Ten, txtNL_DVT, txtNL_SL, txtNL_MaKho;

    // ===== NHÀ CUNG CẤP =====
    private DefaultTableModel modelNCC;
    private JTable tableNCC;
    private JTextField txtNCC_Ma, txtNCC_Ten, txtNCC_DC, txtNCC_SDT, txtNCC_Email;

    // ===== NHÂN VIÊN =====
    private DefaultTableModel modelNV;
    private JTable tableNV;
    private JTextField txtNV_Ma, txtNV_Ten, txtNV_SDT, txtNV_Email, txtNV_NS, txtNV_GT, txtNV_CV;

    // ===== PHIẾU NHẬP =====
    private DefaultTableModel modelPN, modelCTPN;
    private JTable tablePN, tableCTPN;
    private JTextField txtPN_Ma, txtPN_Ngay, txtPN_MaNV, txtPN_MaNCC;
    private JTextField txtCTPN_Ma, txtCTPN_MaNL, txtCTPN_SL, txtCTPN_DG;

    // ===== PHIẾU XUẤT =====
    private DefaultTableModel modelPX, modelCTPX;
    private JTable tablePX, tableCTPX;
    private JTextField txtPX_Ma, txtPX_Ngay, txtPX_MaNV;
    private JTextField txtCTPX_Ma, txtCTPX_MaNL, txtCTPX_SL, txtCTPX_DG;

    // DAOs
    private NguyenLieuDAO         nlDAO    = new NguyenLieuDAO();
    private NhaCungCapDAO         nccDAO   = new NhaCungCapDAO();
    private NhanVienDAO           nvDAO    = new NhanVienDAO();
    private PhieuNhapDAO          pnDAO    = new PhieuNhapDAO();
    private PhieuXuatDAO          pxDAO    = new PhieuXuatDAO();
    private ChiTietPhieuXuatDAO   ctpxDAO  = new ChiTietPhieuXuatDAO(); // FIX: tách DAO riêng

    public MainGUI(String currentUser) {
        this.currentUser = currentUser;
        setTitle("Quản Lý Kho Jollibee - [" + currentUser + "]");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(220, 50, 50));
        header.setBorder(new EmptyBorder(8, 15, 8, 15));
        JLabel lblTitle = new JLabel("🍗 Quản Lý Kho Jollibee");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(new Color(220, 50, 50));
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            new LoginGUI().setVisible(true);
            dispose();
        });
        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);

        // Tabs
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.addTab("Nguyên Liệu",  buildNLPanel());
        tabs.addTab("Nhà Cung Cấp", buildNCCPanel());
        tabs.addTab("Nhân Viên",    buildNVPanel());
        tabs.addTab("Phiếu Nhập",   buildPNPanel());
        tabs.addTab("Phiếu Xuất",   buildPXPanel());

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabs,   BorderLayout.CENTER);
    }

    // =====================================================================
    // TAB NGUYÊN LIỆU
    // =====================================================================
    private JPanel buildNLPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 6, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin nguyên liệu"));
        txtNL_Ma = new JTextField(); txtNL_Ten = new JTextField();
        txtNL_DVT = new JTextField(); txtNL_SL = new JTextField();
        txtNL_MaKho = new JTextField();
        form.add(new JLabel(" Mã NL:"));    form.add(txtNL_Ma);
        form.add(new JLabel(" Tên NL:"));   form.add(txtNL_Ten);
        form.add(new JLabel(" Đơn vị:"));   form.add(txtNL_DVT);
        form.add(new JLabel(" Số lượng:")); form.add(txtNL_SL);
        form.add(new JLabel(" Mã kho:"));   form.add(txtNL_MaKho);
        form.add(new JLabel(""));

        modelNL = new DefaultTableModel(new String[]{"Mã NL","Tên","ĐVT","Số lượng","Mã kho"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableNL = new JTable(modelNL);
        tableNL.setRowHeight(24);
        tableNL.getSelectionModel().addListSelectionListener(e -> {
            int r = tableNL.getSelectedRow();
            if (r >= 0) {
                txtNL_Ma.setText(modelNL.getValueAt(r,0).toString());
                txtNL_Ten.setText(modelNL.getValueAt(r,1).toString());
                txtNL_DVT.setText(modelNL.getValueAt(r,2).toString());
                txtNL_SL.setText(modelNL.getValueAt(r,3).toString());
                txtNL_MaKho.setText(modelNL.getValueAt(r,4).toString());
            }
        });

        JPanel btns = new JPanel();
        JButton btnThem = btn("Thêm",     new Color(46,139,87));
        JButton btnSua  = btn("Sửa",      new Color(30,144,255));
        JButton btnXoa  = btn("Xóa",      new Color(220,50,50));
        JButton btnLM   = btn("Làm mới",  Color.GRAY);
        btns.add(btnThem); btns.add(btnSua); btns.add(btnXoa); btns.add(btnLM);

        btnThem.addActionListener(e -> {
            if (!validateFields(txtNL_Ma, txtNL_Ten, txtNL_MaKho)) return;
            if (nlDAO.existsById(txtNL_Ma.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Mã đã tồn tại!"); return;
            }
            nlDAO.insert(new NguyenLieu(
                txtNL_Ma.getText().trim(), txtNL_Ten.getText().trim(),
                txtNL_DVT.getText().trim(),
                txtNL_SL.getText().isEmpty() ? 0 : Integer.parseInt(txtNL_SL.getText().trim()),
                txtNL_MaKho.getText().trim()
            ));
            loadNL(); clearNL();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        });
        btnSua.addActionListener(e -> {
            if (!validateFields(txtNL_Ma, txtNL_Ten, txtNL_MaKho)) return;
            nlDAO.update(new NguyenLieu(
                txtNL_Ma.getText().trim(), txtNL_Ten.getText().trim(),
                txtNL_DVT.getText().trim(),
                txtNL_SL.getText().isEmpty() ? 0 : Integer.parseInt(txtNL_SL.getText().trim()),
                txtNL_MaKho.getText().trim()
            ));
            loadNL(); clearNL();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        });
        btnXoa.addActionListener(e -> {
            int r = tableNL.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!"); return; }
            if (JOptionPane.showConfirmDialog(this, "Xóa nguyên liệu này?") == JOptionPane.YES_OPTION) {
                try { nlDAO.delete(modelNL.getValueAt(r,0).toString()); loadNL(); clearNL(); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Không thể xóa: đang dùng trong phiếu!"); }
            }
        });
        btnLM.addActionListener(e -> clearNL());

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableNL), BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);
        loadNL();
        return panel;
    }

    private void loadNL() {
        modelNL.setRowCount(0);
        for (NguyenLieu nl : nlDAO.getAll())
            modelNL.addRow(new Object[]{nl.getMaNL(), nl.getTenNL(), nl.getDonViTinh(), nl.getSoLuong(), nl.getMaKho()});
    }
    private void clearNL() {
        txtNL_Ma.setText(""); txtNL_Ten.setText(""); txtNL_DVT.setText("");
        txtNL_SL.setText(""); txtNL_MaKho.setText(""); tableNL.clearSelection();
    }

    // =====================================================================
    // TAB NHÀ CUNG CẤP
    // =====================================================================
    private JPanel buildNCCPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 6, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin nhà cung cấp"));
        txtNCC_Ma = new JTextField(); txtNCC_Ten   = new JTextField();
        txtNCC_DC = new JTextField(); txtNCC_SDT   = new JTextField();
        txtNCC_Email = new JTextField();
        form.add(new JLabel(" Mã NCC:")); form.add(txtNCC_Ma);
        form.add(new JLabel(" Tên NCC:")); form.add(txtNCC_Ten);
        form.add(new JLabel(" Địa chỉ:")); form.add(txtNCC_DC);
        form.add(new JLabel(" SĐT:"));     form.add(txtNCC_SDT);
        form.add(new JLabel(" Email:"));   form.add(txtNCC_Email);
        form.add(new JLabel(""));

        modelNCC = new DefaultTableModel(new String[]{"Mã","Tên NCC","Địa chỉ","SĐT","Email"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableNCC = new JTable(modelNCC);
        tableNCC.setRowHeight(24);
        tableNCC.getSelectionModel().addListSelectionListener(e -> {
            int r = tableNCC.getSelectedRow();
            if (r >= 0) {
                txtNCC_Ma.setText(modelNCC.getValueAt(r,0).toString());
                txtNCC_Ten.setText(modelNCC.getValueAt(r,1).toString());
                txtNCC_DC.setText(modelNCC.getValueAt(r,2).toString());
                txtNCC_SDT.setText(modelNCC.getValueAt(r,3).toString());
                txtNCC_Email.setText(modelNCC.getValueAt(r,4) != null ? modelNCC.getValueAt(r,4).toString() : "");
            }
        });

        JPanel btns = new JPanel();
        JButton btnThem = btn("Thêm",    new Color(46,139,87));
        JButton btnSua  = btn("Sửa",     new Color(30,144,255));
        JButton btnXoa  = btn("Xóa",     new Color(220,50,50));
        JButton btnLM   = btn("Làm mới", Color.GRAY);
        btns.add(btnThem); btns.add(btnSua); btns.add(btnXoa); btns.add(btnLM);

        btnThem.addActionListener(e -> {
            if (!validateFields(txtNCC_Ma, txtNCC_Ten)) return;
            if (nccDAO.existsById(txtNCC_Ma.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Mã đã tồn tại!"); return;
            }
            nccDAO.insert(new NhaCungCap(
                txtNCC_Ma.getText().trim(), txtNCC_Ten.getText().trim(),
                txtNCC_DC.getText().trim(), txtNCC_SDT.getText().trim(), txtNCC_Email.getText().trim()
            ));
            loadNCC(); clearNCC();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        });
        btnSua.addActionListener(e -> {
            if (!validateFields(txtNCC_Ma, txtNCC_Ten)) return;
            nccDAO.update(new NhaCungCap(
                txtNCC_Ma.getText().trim(), txtNCC_Ten.getText().trim(),
                txtNCC_DC.getText().trim(), txtNCC_SDT.getText().trim(), txtNCC_Email.getText().trim()
            ));
            loadNCC(); clearNCC();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        });
        btnXoa.addActionListener(e -> {
            int r = tableNCC.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!"); return; }
            if (JOptionPane.showConfirmDialog(this, "Xóa nhà cung cấp này?") == JOptionPane.YES_OPTION) {
                try { nccDAO.delete(modelNCC.getValueAt(r,0).toString()); loadNCC(); clearNCC(); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Không thể xóa: đang liên kết với phiếu nhập!"); }
            }
        });
        btnLM.addActionListener(e -> clearNCC());

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableNCC), BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);
        loadNCC();
        return panel;
    }

    private void loadNCC() {
        modelNCC.setRowCount(0);
        for (NhaCungCap n : nccDAO.getAll())
            modelNCC.addRow(new Object[]{n.getMaNCC(), n.getTenNCC(), n.getDiaChi(), n.getSdt(), n.getEmail()});
    }
    private void clearNCC() {
        txtNCC_Ma.setText(""); txtNCC_Ten.setText(""); txtNCC_DC.setText("");
        txtNCC_SDT.setText(""); txtNCC_Email.setText(""); tableNCC.clearSelection();
    }

    // =====================================================================
    // TAB NHÂN VIÊN
    // =====================================================================
    private JPanel buildNVPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 6, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        txtNV_Ma = new JTextField(); txtNV_Ten   = new JTextField();
        txtNV_SDT = new JTextField(); txtNV_Email = new JTextField();
        txtNV_NS = new JTextField(); txtNV_GT    = new JTextField(); txtNV_CV = new JTextField();
        form.add(new JLabel(" Mã NV:"));               form.add(txtNV_Ma);
        form.add(new JLabel(" Họ tên:"));              form.add(txtNV_Ten);
        form.add(new JLabel(" SĐT:"));                 form.add(txtNV_SDT);
        form.add(new JLabel(" Email:"));               form.add(txtNV_Email);
        form.add(new JLabel(" Ngày sinh (yyyy-MM-dd):")); form.add(txtNV_NS);
        form.add(new JLabel(" Giới tính:"));           form.add(txtNV_GT);
        form.add(new JLabel(" Chức vụ:"));             form.add(txtNV_CV);
        form.add(new JLabel(""));

        modelNV = new DefaultTableModel(new String[]{"Mã NV","Họ tên","SĐT","Email","Ngày sinh","Giới tính","Chức vụ"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableNV = new JTable(modelNV);
        tableNV.setRowHeight(24);
        tableNV.getSelectionModel().addListSelectionListener(e -> {
            int r = tableNV.getSelectedRow();
            if (r >= 0) {
                txtNV_Ma.setText(safe(modelNV.getValueAt(r,0)));
                txtNV_Ten.setText(safe(modelNV.getValueAt(r,1)));
                txtNV_SDT.setText(safe(modelNV.getValueAt(r,2)));
                txtNV_Email.setText(safe(modelNV.getValueAt(r,3)));
                txtNV_NS.setText(safe(modelNV.getValueAt(r,4)));
                txtNV_GT.setText(safe(modelNV.getValueAt(r,5)));
                txtNV_CV.setText(safe(modelNV.getValueAt(r,6)));
            }
        });

        JPanel btns = new JPanel();
        JButton btnThem = btn("Thêm",    new Color(46,139,87));
        JButton btnSua  = btn("Sửa",     new Color(30,144,255));
        JButton btnXoa  = btn("Xóa",     new Color(220,50,50));
        JButton btnLM   = btn("Làm mới", Color.GRAY);
        btns.add(btnThem); btns.add(btnSua); btns.add(btnXoa); btns.add(btnLM);

        btnThem.addActionListener(e -> {
            if (!validateFields(txtNV_Ma, txtNV_Ten)) return;
            if (nvDAO.existsById(txtNV_Ma.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Mã đã tồn tại!"); return;
            }
            nvDAO.insert(new NhanVien(txtNV_Ma.getText().trim(), txtNV_Ten.getText().trim(),
                txtNV_SDT.getText().trim(), txtNV_Email.getText().trim(),
                txtNV_NS.getText().trim(), txtNV_GT.getText().trim(), txtNV_CV.getText().trim()));
            loadNV(); clearNV();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        });
        btnSua.addActionListener(e -> {
            if (!validateFields(txtNV_Ma, txtNV_Ten)) return;
            nvDAO.update(new NhanVien(txtNV_Ma.getText().trim(), txtNV_Ten.getText().trim(),
                txtNV_SDT.getText().trim(), txtNV_Email.getText().trim(),
                txtNV_NS.getText().trim(), txtNV_GT.getText().trim(), txtNV_CV.getText().trim()));
            loadNV(); clearNV();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        });
        btnXoa.addActionListener(e -> {
            int r = tableNV.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!"); return; }
            if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên này?") == JOptionPane.YES_OPTION) {
                try { nvDAO.delete(modelNV.getValueAt(r,0).toString()); loadNV(); clearNV(); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Không thể xóa: nhân viên đang liên kết dữ liệu!"); }
            }
        });
        btnLM.addActionListener(e -> clearNV());

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableNV), BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);
        loadNV();
        return panel;
    }

    private void loadNV() {
        modelNV.setRowCount(0);
        for (NhanVien nv : nvDAO.getAll())
            modelNV.addRow(new Object[]{nv.getMaNV(), nv.getTenNV(), nv.getSdt(),
                nv.getEmail(), nv.getNgaySinh(), nv.getGioiTinh(), nv.getChucVu()});
    }
    private void clearNV() {
        txtNV_Ma.setText(""); txtNV_Ten.setText(""); txtNV_SDT.setText("");
        txtNV_Email.setText(""); txtNV_NS.setText(""); txtNV_GT.setText(""); txtNV_CV.setText("");
        tableNV.clearSelection();
    }

    // =====================================================================
    // TAB PHIẾU NHẬP
    // =====================================================================
    private JPanel buildPNPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPN = new JPanel(new GridLayout(1, 8, 8, 8));
        formPN.setBorder(BorderFactory.createTitledBorder("Phiếu nhập"));
        txtPN_Ma   = new JTextField(); txtPN_Ngay  = new JTextField(today());
        txtPN_MaNV = new JTextField(currentUser); txtPN_MaNCC = new JTextField();
        formPN.add(new JLabel(" Mã PN:"));  formPN.add(txtPN_Ma);
        formPN.add(new JLabel(" Ngày:"));   formPN.add(txtPN_Ngay);
        formPN.add(new JLabel(" Mã NV:"));  formPN.add(txtPN_MaNV);
        formPN.add(new JLabel(" Mã NCC:")); formPN.add(txtPN_MaNCC);

        JPanel btnsPN = new JPanel();
        JButton btnTaoPN = btn("Tạo phiếu", new Color(46,139,87));
        JButton btnXoaPN = btn("Xóa phiếu", new Color(220,50,50));
        JButton btnLMPN  = btn("Làm mới",   Color.GRAY);
        btnsPN.add(btnTaoPN); btnsPN.add(btnXoaPN); btnsPN.add(btnLMPN);

        modelPN = new DefaultTableModel(new String[]{"Mã PN","Ngày nhập","Mã NV","Mã NCC","Tổng tiền"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePN = new JTable(modelPN);
        tablePN.setRowHeight(24);

        JPanel formCT = new JPanel(new GridLayout(1, 8, 8, 8));
        formCT.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));
        txtCTPN_Ma  = new JTextField(); txtCTPN_MaNL = new JTextField();
        txtCTPN_SL  = new JTextField(); txtCTPN_DG   = new JTextField();
        formCT.add(new JLabel(" Mã CTPN:")); formCT.add(txtCTPN_Ma);
        formCT.add(new JLabel(" Mã NL:"));   formCT.add(txtCTPN_MaNL);
        formCT.add(new JLabel(" Số lượng:")); formCT.add(txtCTPN_SL);
        formCT.add(new JLabel(" Đơn giá:"));  formCT.add(txtCTPN_DG);

        JPanel btnsCTPN = new JPanel();
        JButton btnThemCTPN = btn("Thêm chi tiết", new Color(30,144,255));
        btnsCTPN.add(btnThemCTPN);

        modelCTPN = new DefaultTableModel(new String[]{"Mã CTPN","Mã NL","Số lượng","Đơn giá","Mã PN"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCTPN = new JTable(modelCTPN);
        tableCTPN.setRowHeight(24);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(tablePN), new JScrollPane(tableCTPN));
        split.setDividerLocation(200);

        tablePN.getSelectionModel().addListSelectionListener(e -> {
            int r = tablePN.getSelectedRow();
            if (r >= 0) {
                String maPN = modelPN.getValueAt(r,0).toString();
                txtPN_Ma.setText(maPN);
                loadCTPN(maPN);
            }
        });

        btnTaoPN.addActionListener(e -> {
            if (!validateFields(txtPN_MaNCC)) return;
            String maPN = pnDAO.genMaPN();
            pnDAO.insert(new PhieuNhap(maPN, txtPN_Ngay.getText().trim(),
                currentUser, txtPN_MaNCC.getText().trim(), 0));
            txtPN_Ma.setText(maPN);
            loadPN();
            JOptionPane.showMessageDialog(this, "Tạo phiếu " + maPN + " thành công! Hãy thêm chi tiết.");
        });

        btnXoaPN.addActionListener(e -> {
            int r = tablePN.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn phiếu cần xóa!"); return; }
            if (JOptionPane.showConfirmDialog(this, "Xóa phiếu này và toàn bộ chi tiết?") == JOptionPane.YES_OPTION) {
                pnDAO.delete(modelPN.getValueAt(r,0).toString());
                loadPN(); modelCTPN.setRowCount(0);
            }
        });

        btnLMPN.addActionListener(e -> {
            txtPN_Ma.setText(""); txtPN_MaNCC.setText("");
            txtPN_Ngay.setText(today()); modelCTPN.setRowCount(0);
            tablePN.clearSelection();
        });

        btnThemCTPN.addActionListener(e -> {
            String maPN = txtPN_Ma.getText().trim();
            if (maPN.isEmpty()) { JOptionPane.showMessageDialog(this, "Tạo phiếu nhập trước!"); return; }
            if (!validateFields(txtCTPN_MaNL, txtCTPN_SL, txtCTPN_DG)) return;
            try {
                String maCT = pnDAO.genMaCTPN();
                pnDAO.insertChiTiet(new ChiTietPhieuNhap(
                    maCT,
                    Integer.parseInt(txtCTPN_SL.getText().trim()),
                    txtCTPN_MaNL.getText().trim(),
                    Double.parseDouble(txtCTPN_DG.getText().trim()),
                    maPN
                ));
                loadCTPN(maPN); loadPN(); loadNL();
                txtCTPN_MaNL.setText(""); txtCTPN_SL.setText(""); txtCTPN_DG.setText("");
                JOptionPane.showMessageDialog(this, "Thêm chi tiết thành công! Kho đã được cập nhật.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng và đơn giá phải là số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(formPN, BorderLayout.NORTH);
        top.add(btnsPN, BorderLayout.CENTER);

        JPanel bot = new JPanel(new BorderLayout());
        bot.add(formCT, BorderLayout.NORTH);
        bot.add(btnsCTPN, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        panel.add(bot, BorderLayout.SOUTH);

        loadPN();
        return panel;
    }

    private void loadPN() {
        modelPN.setRowCount(0);
        for (PhieuNhap pn : pnDAO.getAll())
            modelPN.addRow(new Object[]{
                pn.getMaPN(), pn.getNgayNhap(),
                pn.getMaNV(), pn.getMaNCC(),
                String.format("%,.0f đ", pn.getTongTien())
            });
    }
    private void loadCTPN(String maPN) {
        modelCTPN.setRowCount(0);
        for (ChiTietPhieuNhap ct : pnDAO.getChiTiet(maPN))
            modelCTPN.addRow(new Object[]{
                ct.getMaCTPN(), ct.getMaNL(),
                ct.getSoLuong(),
                String.format("%,.0f đ", ct.getDonGia()),
                ct.getMaPN()
            });
    }

    // =====================================================================
    // TAB PHIẾU XUẤT
    // =====================================================================
    private JPanel buildPXPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPX = new JPanel(new GridLayout(1, 6, 8, 8));
        formPX.setBorder(BorderFactory.createTitledBorder("Phiếu xuất"));
        txtPX_Ma   = new JTextField(); txtPX_Ngay = new JTextField(today());
        txtPX_MaNV = new JTextField(currentUser);
        formPX.add(new JLabel(" Mã PX:")); formPX.add(txtPX_Ma);
        formPX.add(new JLabel(" Ngày:"));  formPX.add(txtPX_Ngay);
        formPX.add(new JLabel(" Mã NV:")); formPX.add(txtPX_MaNV);

        JPanel bntsPX = new JPanel();
        JButton btnTaoPX = btn("Tạo phiếu", new Color(46,139,87));
        JButton btnXoaPX = btn("Xóa phiếu", new Color(220,50,50));
        JButton btnLMPX  = btn("Làm mới",   Color.GRAY);
        bntsPX.add(btnTaoPX); bntsPX.add(btnXoaPX); bntsPX.add(btnLMPX);

        modelPX = new DefaultTableModel(new String[]{"Mã PX","Ngày xuất","Mã NV","Tổng tiền"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePX = new JTable(modelPX);
        tablePX.setRowHeight(24);

        JPanel formCT = new JPanel(new GridLayout(1, 8, 8, 8));
        formCT.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu xuất"));
        txtCTPX_Ma  = new JTextField(); txtCTPX_MaNL = new JTextField();
        txtCTPX_SL  = new JTextField(); txtCTPX_DG   = new JTextField();
        formCT.add(new JLabel(" Mã CTPX:")); formCT.add(txtCTPX_Ma);
        formCT.add(new JLabel(" Mã NL:"));   formCT.add(txtCTPX_MaNL);
        formCT.add(new JLabel(" Số lượng:")); formCT.add(txtCTPX_SL);
        formCT.add(new JLabel(" Đơn giá:"));  formCT.add(txtCTPX_DG);

        JPanel bntsCT = new JPanel();
        JButton btnThemCT = btn("Thêm chi tiết", new Color(30,144,255));
        bntsCT.add(btnThemCT);

        modelCTPX = new DefaultTableModel(new String[]{"Mã CTPX","Mã NL","Số lượng","Đơn giá","Mã PX"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCTPX = new JTable(modelCTPX);
        tableCTPX.setRowHeight(24);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(tablePX), new JScrollPane(tableCTPX));
        split.setDividerLocation(200);

        tablePX.getSelectionModel().addListSelectionListener(e -> {
            int r = tablePX.getSelectedRow();
            if (r >= 0) {
                String maPX = modelPX.getValueAt(r,0).toString();
                txtPX_Ma.setText(maPX);
                loadCTPX(maPX);
            }
        });

        // FIX: dùng generateNextMaPX() đúng tên, parse ngày đúng kiểu java.sql.Date
        btnTaoPX.addActionListener(e -> {
            try {
                String maPX = pxDAO.generateNextMaPX();
                Date ngayXuat = Date.valueOf(txtPX_Ngay.getText().trim()); // "yyyy-MM-dd" → java.sql.Date
                PhieuXuat px = new PhieuXuat(maPX, ngayXuat, currentUser, BigDecimal.ZERO);
                boolean ok = pxDAO.insert(px);
                if (ok) {
                    txtPX_Ma.setText(maPX);
                    loadPX();
                    JOptionPane.showMessageDialog(this, "Tạo phiếu " + maPX + " thành công! Hãy thêm chi tiết.");
                } else {
                    JOptionPane.showMessageDialog(this, "Tạo phiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                // Date.valueOf ném IllegalArgumentException nếu format sai
                JOptionPane.showMessageDialog(this, "Ngày không hợp lệ! Định dạng: yyyy-MM-dd", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi tạo phiếu xuất: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnXoaPX.addActionListener(e -> {
            int r = tablePX.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn phiếu cần xóa!"); return; }
            if (JOptionPane.showConfirmDialog(this, "Xóa phiếu này và toàn bộ chi tiết?") == JOptionPane.YES_OPTION) {
                pxDAO.delete(modelPX.getValueAt(r,0).toString());
                loadPX(); modelCTPX.setRowCount(0);
            }
        });

        btnLMPX.addActionListener(e -> {
            txtPX_Ma.setText(""); txtPX_Ngay.setText(today());
            modelCTPX.setRowCount(0); tablePX.clearSelection();
        });

        // FIX: dùng ctpxDAO, build đúng object ChiTietPhieuXuat với đủ tham số
        btnThemCT.addActionListener(e -> {
            String maPX = txtPX_Ma.getText().trim();
            if (maPX.isEmpty()) { JOptionPane.showMessageDialog(this, "Tạo phiếu xuất trước!"); return; }
            if (!validateFields(txtCTPX_MaNL, txtCTPX_SL, txtCTPX_DG)) return;
            try {
                int soLuong = Integer.parseInt(txtCTPX_SL.getText().trim());
                BigDecimal donGia = new BigDecimal(txtCTPX_DG.getText().trim());
                String maCTPX = ctpxDAO.generateNextMaCTPX();
                String maNL   = txtCTPX_MaNL.getText().trim();

                ChiTietPhieuXuat ct = new ChiTietPhieuXuat(maCTPX, soLuong, donGia, maNL, maPX);
                boolean ok = ctpxDAO.insert(ct);
                if (ok) {
                    loadCTPX(maPX); loadPX(); loadNL();
                    txtCTPX_MaNL.setText(""); txtCTPX_SL.setText(""); txtCTPX_DG.setText("");
                    JOptionPane.showMessageDialog(this, "Xuất kho thành công! Kho đã được cập nhật.");
                } else {
                    // insert trả false khi tồn kho không đủ (trigger chặn)
                    JOptionPane.showMessageDialog(this, "Xuất thất bại! Có thể tồn kho không đủ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng và đơn giá phải là số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                // Bắt lỗi từ validation trong setter ChiTietPhieuXuat
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(formPX, BorderLayout.NORTH);
        top.add(bntsPX, BorderLayout.CENTER);

        JPanel bot = new JPanel(new BorderLayout());
        bot.add(formCT, BorderLayout.NORTH);
        bot.add(bntsCT, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        panel.add(bot, BorderLayout.SOUTH);

        loadPX();
        return panel;
    }

    // FIX: cột đầu phải là getMaPX(), không phải getMaNV()
    private void loadPX() {
        modelPX.setRowCount(0);
        for (PhieuXuat px : pxDAO.getAll())
            modelPX.addRow(new Object[]{
                px.getMaPX(),       // FIX: trước đây là getMaNV() — sai cột
                px.getNgayXuat(),
                px.getMaNV(),
                String.format("%,.0f đ", px.getTongTien())
            });
    }

    // FIX: dùng ctpxDAO.getByMaPX() thay vì pxDAO.insertChiTiet()
    private void loadCTPX(String maPX) {
        modelCTPX.setRowCount(0);
        for (ChiTietPhieuXuat ct : ctpxDAO.getByMaPX(maPX))
            modelCTPX.addRow(new Object[]{
                ct.getMaCTPX(), ct.getMaNL(),
                ct.getSoLuong(),
                String.format("%,.0f đ", ct.getDonGia()),
                ct.getMaPX()
            });
    }

    // =====================================================================
    // HELPER
    // =====================================================================
    private JButton btn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 32));
        return b;
    }

    private boolean validateFields(JTextField... fields) {
        for (JTextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                f.requestFocus();
                return false;
            }
        }
        return true;
    }

    private String safe(Object o) { return o == null ? "" : o.toString(); }

    private String today() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    }
}