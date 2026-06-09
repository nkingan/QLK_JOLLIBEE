
package ui;

import dao.NguyenLieuDAO;
import model.NguyenLieu;
import model.TaiKhoan;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class NguyenLieuUI extends JPanel {

    // =====================================================================
    // BẢNG MÀU THƯƠNG HIỆU JOLLIBEE
    // =====================================================================
    private static final Color JOLLIBEE_RED    = new Color(227, 29, 43);
    private static final Color JOLLIBEE_YELLOW = new Color(255, 210, 0);
    private static final Color CREAM_WHITE     = new Color(255, 253, 240);
    private static final Color DARK_GRAY       = new Color(50, 50, 50);
    private static final Color TABLE_HEADER_BG = new Color(180, 30, 45);
    private static final Color ROW_ODD         = new Color(255, 253, 245);
    private static final Color ROW_EVEN        = new Color(245, 240, 230);
    private static final Color BTN_GREEN       = new Color(34, 139, 34);
    private static final Color BTN_BLUE        = new Color(30, 100, 200);

    // =====================================================================
    // THÀNH PHẦN GIAO DIỆN
    // =====================================================================
    private JTextField txtMaNL, txtTenNL, txtMaKho, txtGiaNhap,
                       txtSoLuong, txtDonVi, txtAnh, txtSearch;
    private JComboBox<String> cbSearchType;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable tableNL;
    private DefaultTableModel tableModel;

    // =====================================================================
    // LOGIC & STATE
    // =====================================================================
    private final NguyenLieuDAO nguyenLieuDAO = new NguyenLieuDAO();
    private NguyenLieu currentNguyenLieu = null;
    private final TaiKhoan currentUser;

    // =====================================================================
    // CONSTRUCTOR
    // =====================================================================
    public NguyenLieuUI(TaiKhoan user) {
        this.currentUser = user;

        // Fix UTF-8 console output cho debug tiếng Việt
        try { System.setOut(new java.io.PrintStream(System.out, true, "UTF-8")); }
        catch (Exception ignored) {}

        // QUAN TRỌNG: Override LAF button defaults để màu nền hiển thị đúng
        // trên tất cả hệ điều hành (Windows/macOS/Linux)
        UIManager.put("Button.select",             JOLLIBEE_RED);
        UIManager.put("Button.background",         Color.GRAY);
        UIManager.put("Button.opaque",             Boolean.TRUE);

        setLayout(new BorderLayout(12, 12));
        setBackground(CREAM_WHITE);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        buildSearchBar();
        buildTable();
        buildRightForm();

        // Load data SAU khi toàn bộ component đã được add vào layout
        SwingUtilities.invokeLater(() -> {
            loadTableData();
            clearForm();
        });
    }

    // =====================================================================
    // 1. THANH TÌM KIẾM (NORTH)
    // =====================================================================
    private void buildSearchBar() {
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        panelSearch.setBackground(new Color(245, 245, 245));
        panelSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, JOLLIBEE_RED),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));

        JLabel lblSearch = new JLabel("🔍  Tìm kiếm theo:");
        lblSearch.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSearch.setForeground(DARK_GRAY);
        panelSearch.add(lblSearch);

        cbSearchType = new JComboBox<>(new String[]{"Tên NL", "Mã NL", "Mã Kho"});
        cbSearchType.setPreferredSize(new Dimension(115, 32));
        cbSearchType.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbSearchType.setBackground(Color.WHITE);
        panelSearch.add(cbSearchType);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(230, 32));
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 13));
        // Cho phép nhấn Enter để tìm kiếm
        txtSearch.addActionListener(e -> performSearchIngredients());
        panelSearch.add(txtSearch);

        btnSearch = createStyledButton("🔍 Tìm kiếm", JOLLIBEE_RED, Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(120, 32));
        btnSearch.addActionListener(e -> performSearchIngredients());
        panelSearch.add(btnSearch);

        JButton btnReload = createStyledButton("↺ Tải lại", DARK_GRAY, Color.WHITE);
        btnReload.setPreferredSize(new Dimension(90, 32));
        btnReload.addActionListener(e -> { txtSearch.setText(""); loadTableData(); });
        panelSearch.add(btnReload);

        add(panelSearch, BorderLayout.NORTH);
    }

    // =====================================================================
    // 2. BẢNG DỮ LIỆU (CENTER)
    // =====================================================================
    private void buildTable() {
        String[] columns = {
            "Mã NL", "Tên Nguyên Liệu", "Mã Kho",
            "Giá Nhập (đ)", "Số Lượng", "Đơn Vị", "Hình Ảnh"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tableNL = new JTable(tableModel) {
            // Alternating row colors — giúp bảng dễ đọc hơn
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
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

        tableNL.setRowHeight(32);
        tableNL.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableNL.setGridColor(new Color(220, 210, 195));
        tableNL.setShowGrid(true);
        tableNL.setIntercellSpacing(new Dimension(1, 1));
        tableNL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNL.setFillsViewportHeight(true); // Bảng luôn lấp đầy vùng scroll

        // ── HEADER ────────────────────────────────────────────────────────
        JTableHeader header = tableNL.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                lbl.setBackground(TABLE_HEADER_BG);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, JOLLIBEE_RED));
                return lbl;
            }
        });
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        // ── ĐỘ RỘNG CỘT ──────────────────────────────────────────────────
        int[] colWidths = {70, 200, 90, 120, 85, 80, 130};
        for (int i = 0; i < colWidths.length; i++) {
            tableNL.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
        }

        // ── RENDERER CĂN GIỮA CHO MỘT SỐ CỘT ────────────────────────────
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        for (int col : new int[]{0, 2, 3, 4, 5}) {
            tableNL.getColumnModel().getColumn(col).setCellRenderer(centerRender);
        }

        JScrollPane scrollPane = new JScrollPane(tableNL);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 185, 170), 1));
        scrollPane.getViewport().setBackground(ROW_ODD);

        add(scrollPane, BorderLayout.CENTER);

        // ── SỰ KIỆN CHỌN HÀNG ────────────────────────────────────────────
        tableNL.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableNL.getSelectedRow() != -1) {
                displayDetailsFromTable(tableNL.getSelectedRow());
            }
        });
    }

    // =====================================================================
    // 3. FORM CHI TIẾT + NÚT CHỨC NĂNG (EAST)
    // =====================================================================
    private void buildRightForm() {
        JPanel panelRight = new JPanel(new BorderLayout(8, 8));
        panelRight.setPreferredSize(new Dimension(370, 0));
        panelRight.setOpaque(false);

        // ── FORM NHẬP LIỆU ────────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);

        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(JOLLIBEE_RED, 2),
            " 📋  Thông tin chi tiết "
        );
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        border.setTitleColor(JOLLIBEE_RED);
        panelForm.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(7, 12, 7, 10);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        // Khởi tạo các trường nhập liệu
        txtMaNL    = createReadOnlyField();
        txtTenNL   = createInputField();
        txtMaKho  = createInputField();
        txtGiaNhap = createInputField();
        txtSoLuong = createInputField();
        txtDonVi   = createInputField();
        txtAnh     = createInputField();

        // Thêm từng hàng label + textfield vào form
        String[][] rows = {
            {"Mã NL:",         "maNL"},
            {"Tên NL:",        "tenNL"},
            {"Mã Kho:",       "maKho"},
            {"Giá Nhập (đ):",  "giaNhap"},
            {"Số Lượng:",      "soLuong"},
            {"Đơn Vị:",        "donVi"},
            {"Tên File Ảnh:",  "anh"},
        };
        JTextField[] fields = {txtMaNL, txtTenNL, txtMaKho, txtGiaNhap, txtSoLuong, txtDonVi, txtAnh};

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(rows[i][0]);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
            lbl.setForeground(DARK_GRAY);
            panelForm.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 1.0;
            panelForm.add(fields[i], gbc);
        }

        // ── PANEL NÚT CHỨC NĂNG ───────────────────────────────────────────
        JPanel panelButtons = new JPanel(new GridLayout(2, 2, 8, 8));
        panelButtons.setBackground(new Color(245, 245, 240));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

        btnAdd    = createStyledButton("➕  Thêm",    BTN_GREEN,    Color.WHITE);
        btnUpdate = createStyledButton("✏  Sửa",     BTN_BLUE,     Color.WHITE);
        btnDelete = createStyledButton("🗑  Xóa",    JOLLIBEE_RED, Color.WHITE);
        btnClear  = createStyledButton("🔄  Làm mới", DARK_GRAY,    Color.WHITE);

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnClear);

        panelRight.add(panelForm,    BorderLayout.CENTER);
        panelRight.add(panelButtons, BorderLayout.SOUTH);

        add(panelRight, BorderLayout.EAST);

        // ── ĐĂNG KÝ SỰ KIỆN NÚT ─────────────────────────────────────────
        btnAdd.addActionListener(e    -> performAddIngredient());
        btnUpdate.addActionListener(e -> performUpdateIngredient());
        btnDelete.addActionListener(e -> performDeleteIngredient());
        btnClear.addActionListener(e  -> clearForm());

        applyRoleAuthorization();
    }

    // =====================================================================
    // HELPER: Tạo nút với màu nền rõ ràng — cross-platform
    // =====================================================================
    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // Vẽ nền thủ công để đảm bảo hiển thị màu trên mọi LAF/OS
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(isEnabled() ? bg : bg.darker().darker());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setText(text);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); // Tắt fill mặc định, dùng custom paintComponent
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(130, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // =====================================================================
    // HELPER: Tạo TextField nhập liệu chuẩn
    // =====================================================================
    private JTextField createInputField() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(200, 32));
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setForeground(Color.BLACK);
        tf.setBackground(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 170, 160)),
            BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));
        return tf;
    }

    // =====================================================================
    // HELPER: Tạo TextField chỉ đọc (Mã NL tự sinh)
    // =====================================================================
    private JTextField createReadOnlyField() {
        JTextField tf = createInputField();
        tf.setEditable(false);
        tf.setBackground(new Color(235, 235, 230));
        tf.setForeground(new Color(80, 80, 80));
        return tf;
    }

    // =====================================================================
    // PHÂN QUYỀN
    // =====================================================================
    private void applyRoleAuthorization() {
        if (currentUser != null && currentUser.getQuyen() != null) {
            if ("Nhân viên".equalsIgnoreCase(currentUser.getQuyen().trim())) {
                btnDelete.setVisible(false);
                btnAdd.setEnabled(false);
                btnUpdate.setEnabled(false);
            }
        }
    }

    // =====================================================================
    // TẢI DỮ LIỆU VÀO BẢNG
    // =====================================================================
    public void loadTableData() {
        tableModel.setRowCount(0);
        List<NguyenLieu> list = nguyenLieuDAO.getAllNguyenLieu();
        if (list == null) {
        JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    } else if (list.isEmpty()) {
        System.out.println("Danh sách nguyên liệu trống.");
    } else {
        // if (list != null && !list.isEmpty()) {
            for (NguyenLieu nl : list) {
                tableModel.addRow(new Object[]{
                    nl.getMaNL(),
                    nl.getTenNL(),
                    nl.getMaKho(),
                    String.format("%,d đ", nl.getGianhap()),
                    nl.getSoluong(),
                    nl.getDonvi(),
                    nl.getAnh()
                });
            }
        }
        tableNL.revalidate();
        tableNL.repaint();
    }

    // =====================================================================
    // HIỂN THỊ CHI TIẾT KHI CHỌN HÀNG
    // =====================================================================
    private void displayDetailsFromTable(int row) {
        String maNL = (String) tableModel.getValueAt(row, 0);
        currentNguyenLieu = nguyenLieuDAO.getNguyenLieuById(maNL);

        if (currentNguyenLieu == null) return;

        txtMaNL.setText(currentNguyenLieu.getMaNL());
        txtTenNL.setText(currentNguyenLieu.getTenNL());
        txtMaKho.setText(currentNguyenLieu.getMaKho());
        txtGiaNhap.setText(String.valueOf(currentNguyenLieu.getGianhap()));
        txtSoLuong.setText(String.valueOf(currentNguyenLieu.getSoluong()));
        txtDonVi.setText(currentNguyenLieu.getDonvi());
        txtAnh.setText(currentNguyenLieu.getAnh() != null ? currentNguyenLieu.getAnh() : "");

        boolean isEmployee = currentUser != null
            && "Nhân viên".equalsIgnoreCase(currentUser.getQuyen());

        btnAdd.setEnabled(false);
        btnUpdate.setEnabled(!isEmployee);
        btnDelete.setEnabled(!isEmployee);
    }

    // =====================================================================
    // XÓA FORM & SINH MÃ MỚI
    // =====================================================================
    private void clearForm() {
        // Sinh mã NL tiếp theo dựa trên record cuối cùng trong CSDL
        String nextId = "NL01";
        String lastId = nguyenLieuDAO.getLastIngredientId();
        if (lastId != null && lastId.length() > 2) {
            try {
                int num = Integer.parseInt(lastId.substring(2).trim());
                nextId = String.format("NL%02d", num + 1);
            } catch (NumberFormatException ignored) {}
        }

        txtMaNL.setText(nextId);
        txtTenNL.setText("");
        txtMaKho.setText("");
        txtGiaNhap.setText("");
        txtSoLuong.setText("0");
        txtDonVi.setText("");
        txtAnh.setText("");
        txtSearch.setText("");

        currentNguyenLieu = null;
        tableNL.clearSelection();

        boolean isEmployee = currentUser != null
            && "Nhân viên".equalsIgnoreCase(currentUser.getQuyen());
        btnAdd.setEnabled(!isEmployee);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    // =====================================================================
    // THÊM NGUYÊN LIỆU
    // =====================================================================
    private void performAddIngredient() {
        if (!validateForm()) return;

        try {
            NguyenLieu nl = buildNguyenLieuFromForm();
            if (nguyenLieuDAO.addNguyenLieu(nl)) {
                showInfo("✅ Thêm nguyên liệu vào kho thành công!");
                loadTableData();
                clearForm();
            } else {
                showError("❌ Thêm thất bại!\nKiểm tra lại Mã Loại (khóa ngoại) có tồn tại không.");
            }
        } catch (NumberFormatException ex) {
            showError("⚠ Giá nhập và Số lượng phải là số nguyên hợp lệ!");
        }
    }

    // =====================================================================
    // CẬP NHẬT NGUYÊN LIỆU
    // =====================================================================
    private void performUpdateIngredient() {
        if (currentNguyenLieu == null) {
            showWarning("⚠ Vui lòng chọn một nguyên liệu trong bảng để cập nhật!");
            return;
        }
        if (!validateForm()) return;

        try {
            NguyenLieu nl = buildNguyenLieuFromForm();
            nl.setMaNL(currentNguyenLieu.getMaNL()); // giữ nguyên mã gốc
            if (nguyenLieuDAO.updateNguyenLieu(nl)) {
                showInfo("✅ Cập nhật nguyên liệu thành công!");
                loadTableData();
                clearForm();
            }
        } catch (NumberFormatException ex) {
            showError("⚠ Giá nhập và Số lượng phải có định dạng số!");
        }
    }

    // =====================================================================
    // XÓA NGUYÊN LIỆU
    // =====================================================================
    private void performDeleteIngredient() {
        if (currentNguyenLieu == null) {
            showWarning("⚠ Vui lòng chọn một nguyên liệu trong bảng để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa nguyên liệu:\n"
            + "[ " + currentNguyenLieu.getMaNL() + " ] "
            + currentNguyenLieu.getTenNL() + "?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (nguyenLieuDAO.deleteNguyenLieu(currentNguyenLieu.getMaNL())) {
                showInfo("✅ Đã xóa nguyên liệu thành công!");
                loadTableData();
                clearForm();
            } else {
                showError("❌ Không thể xóa!\nNguyên liệu này đã có lịch sử trong phiếu nhập/xuất kho.");
            }
        }
    }

    // =====================================================================
    // TÌM KIẾM
    // =====================================================================
    private void performSearchIngredients() {
        String criteria = (String) cbSearchType.getSelectedItem();
        String keyword  = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }

        List<NguyenLieu> result = nguyenLieuDAO.searchNguyenLieu(criteria, keyword);
        tableModel.setRowCount(0);

        if (result != null && !result.isEmpty()) {
            for (NguyenLieu nl : result) {
                tableModel.addRow(new Object[]{
                    nl.getMaNL(), nl.getTenNL(), nl.getMaKho(),
                    String.format("%,d đ", nl.getGianhap()),
                    nl.getSoluong(), nl.getDonvi(), nl.getAnh()
                });
            }
        } else {
            showWarning("Không tìm thấy nguyên liệu nào phù hợp với từ khóa: \"" + keyword + "\"");
        }

        tableNL.revalidate();
        tableNL.repaint();
    }

    // =====================================================================
    // HELPER: Validate form trước khi lưu
    // =====================================================================
    private boolean validateForm() {
        if (txtTenNL.getText().trim().isEmpty()) {
            showWarning("⚠ Tên nguyên liệu không được để trống!"); return false;
        }
        if (txtMaKho.getText().trim().isEmpty()) {
            showWarning("⚠ Mã Kho không được để trống!"); return false;
        }
        if (txtGiaNhap.getText().trim().isEmpty()) {
            showWarning("⚠ Giá nhập không được để trống!"); return false;
        }
        if (txtDonVi.getText().trim().isEmpty()) {
            showWarning("⚠ Đơn vị không được để trống!"); return false;
        }
        try { Integer.parseInt(txtGiaNhap.getText().trim()); }
        catch (NumberFormatException e) {
            showWarning("⚠ Giá nhập phải là số nguyên!"); return false;
        }
        try { Integer.parseInt(txtSoLuong.getText().trim()); }
        catch (NumberFormatException e) {
            showWarning("⚠ Số lượng phải là số nguyên!"); return false;
        }
        return true;
    }

    // =====================================================================
    // HELPER: Build model từ form
    // =====================================================================
    private NguyenLieu buildNguyenLieuFromForm() {
        NguyenLieu nl = new NguyenLieu();
        nl.setMaNL(txtMaNL.getText().trim());
        nl.setTenNL(txtTenNL.getText().trim());
        nl.setMaKho(txtMaKho.getText().trim());
        nl.setGianhap(Integer.parseInt(txtGiaNhap.getText().trim()));
        nl.setSoluong(Integer.parseInt(txtSoLuong.getText().trim()));
        nl.setDonvi(txtDonVi.getText().trim());
        String anh = txtAnh.getText().trim();
        nl.setAnh(anh.isEmpty() ? "default.png" : anh);
        return nl;
    }

    // =====================================================================
    // HELPER: Dialog thông báo
    // =====================================================================
    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}