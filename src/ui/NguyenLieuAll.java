
package ui;

import dao.KhoDAO;
import dao.NguyenLieuDAO;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import model.Kho;
import model.TaiKhoan; // Đồng bộ cấu trúc sử dụng lớp TaiKhoan mới
import model.NguyenLieu;

/**
 * Hệ thống Quản lý Kho nguyên liệu Jollibee
 * Phân hệ: Giao diện tổng quan danh sách Nguyên liệu theo phân kho
 */
public class NguyenLieuAll extends JPanel { // Đổi thành JPanel để nhúng mượt mà vào MainGUI tabs

    private NguyenLieuDAO nguyenLieuDAO;
    private KhoDAO khoDAO;
    private JLabel lblStatus;
    private JPanel mainPanel;
    private JComboBox<Kho> cbKho;
    private JPanel filterPanel;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnReset;
    private String activeFilter = null;
    private TaiKhoan loggedInUser; // Đồng bộ kiểu dữ liệu phân quyền thành TaiKhoan
    private Map<String, Kho> khoCache;
    private JProgressBar progressBar;
    private JPanel loadingPanel;
    private JButton btnSort;
    private String currentSort = "default";

    private TaiKhoan currentUser;
    private NguyenLieuUI nguyenLieuUI; // Khai báo đối tượng UI con

    // BẢNG MÀU CHUẨN THƯƠNG HIỆU JOLLIBEE
    private final Color primaryColor = new Color(214, 0, 28);       // Đỏ Jollibee Red
    private final Color secondaryColor = new Color(248, 249, 250);  // Trắng xám sạch sẽ
    private final Color textColor = new Color(43, 43, 43);          // Xám tối dễ đọc
    private final Color accentYellow = new Color(254, 219, 0);      // Vàng Jollibee Yellow
    private final Color accentGreen = new Color(40, 167, 69);       // Xanh lục an toàn kho
    private final Color accentBlue = new Color(0, 123, 255);        // Xanh dương thao tác

    public NguyenLieuAll() {
        this(null, null);
    }

    // public NguyenLieuAll(TaiKhoan loggedInUser) {
    //     this(loggedInUser, null);
    // }
    public NguyenLieuAll(TaiKhoan user) {
        this.currentUser = user;
        
        // Thiết lập Layout cho panel tổng (thường dùng BorderLayout)
        setLayout(new BorderLayout());
        
        initComponents();
    }
    private void initComponents() {
        // 3. LIÊN KẾT: Khởi tạo NguyenLieuUI và truyền tiếp đối tượng currentUser vào
        this.nguyenLieuUI = new NguyenLieuUI(this.currentUser);
        
        // 4. NHÚNG GIAO DIỆN: Thêm panel CRUD này vào vùng trung tâm của NguyenLieuAll
        this.add(this.nguyenLieuUI, BorderLayout.CENTER);
        
        // Nếu nhóm bạn có thêm các nút thống kê, hoặc biểu đồ ở trên/dưới, 
        // bạn có thể add thêm vào BorderLayout.NORTH hoặc BorderLayout.SOUTH tùy ý.
    }

    public NguyenLieuAll(TaiKhoan loggedInUser, String initialFilterWarehouse) {
        this.loggedInUser = loggedInUser;
        this.activeFilter = initialFilterWarehouse;
        this.khoCache = new HashMap<>();
        
        nguyenLieuDAO = new NguyenLieuDAO();
        khoDAO = new KhoDAO();

        initUI();
        loadAndDisplayIngredients();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(secondaryColor);

        // Khởi tạo Header (Tìm kiếm, Lọc kho)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Khởi tạo vùng hiển thị chính dạng cuộn (Scrollable)
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(secondaryColor);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Khởi tạo thanh trạng thái dưới cùng
        add(createStatusPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setBackground(secondaryColor);
        headerPanel.setBorder(new EmptyBorder(12, 12, 0, 12));

        JLabel titleLabel = new JLabel("DANH SÁCH NGUYÊN LIỆU KHO JOLLIBEE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(primaryColor);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(secondaryColor);

        JLabel lblSearch = new JLabel("Tìm kiếm NL:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterPanel.add(lblSearch);

        txtSearch = new JTextField(18);
        filterPanel.add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        styleButton(btnSearch, primaryColor, Color.WHITE);
        btnSearch.addActionListener(e -> searchIngredients(txtSearch.getText().trim()));
        filterPanel.add(btnSearch);

        filterPanel.add(new JSeparator(JSeparator.VERTICAL));

        JLabel lblFilter = new JLabel("Lọc theo Phân Kho:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterPanel.add(lblFilter);

        cbKho = new JComboBox<>();
        cbKho.setPreferredSize(new Dimension(180, 28));
        loadWarehouses();
        filterPanel.add(cbKho);

        JButton btnFilter = new JButton("Lọc Kho");
        styleButton(btnFilter, accentBlue, Color.WHITE);
        btnFilter.addActionListener(e -> {
            Kho selectedKho = (Kho) cbKho.getSelectedItem();
            if (selectedKho != null && selectedKho.getMaKho() != null && !selectedKho.getMaKho().isEmpty()) {
                activeFilter = selectedKho.getMaKho();
                loadAndDisplayIngredients();
            } else {
                resetFilters();
            }
        });
        filterPanel.add(btnFilter);

        btnSort = new JButton("Sắp Xếp");
        styleButton(btnSort, accentBlue, Color.WHITE);
        btnSort.addActionListener(e -> showSortMenu());
        filterPanel.add(btnSort);

        btnReset = new JButton("Tổng Kho Jollibee");
        styleButton(btnReset, accentGreen, Color.WHITE);
        btnReset.addActionListener(e -> resetFilters());
        filterPanel.add(btnReset);

        headerPanel.add(filterPanel, BorderLayout.CENTER);
        
        if (loggedInUser != null) {
            headerPanel.add(createActionPanel(), BorderLayout.SOUTH);
        }

        return headerPanel;
    }

    private void showSortMenu() {
        JPopupMenu sortMenu = new JPopupMenu();
        JMenuItem nameAsc = new JMenuItem("Tên nguyên liệu (A-Z)");
        nameAsc.addActionListener(e -> { currentSort = "name_asc"; loadAndDisplayIngredients(); });
        sortMenu.add(nameAsc);
        
        JMenuItem qtyAsc = new JMenuItem("Tồn kho: Thấp -> Cao");
        qtyAsc.addActionListener(e -> { currentSort = "qty_asc"; loadAndDisplayIngredients(); });
        sortMenu.add(qtyAsc);
        
        sortMenu.show(btnSort, 0, btnSort.getHeight());
    }

    private void resetFilters() {
        txtSearch.setText("");
        cbKho.setSelectedIndex(0);
        activeFilter = null;
        currentSort = "default";
        loadAndDisplayIngredients();
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionPanel.setBackground(secondaryColor);
        
        // Sử dụng phương thức an toàn getQuyen() của lớp TaiKhoan mới
        String role = loggedInUser.getQuyen(); 
        
        if ("Admin".equalsIgnoreCase(role) || "Quản lý".equalsIgnoreCase(role) || "WarehouseManager".equalsIgnoreCase(role)) {
            JButton btnAddIngredient = new JButton("+ Khai báo Nguyên Liệu Mới");
            styleButton(btnAddIngredient, accentGreen, Color.WHITE);
            actionPanel.add(btnAddIngredient);
        }
        return actionPanel;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(secondaryColor);
        statusPanel.setBorder(new EmptyBorder(5, 12, 5, 12));

        lblStatus = new JLabel("Hệ thống vận hành ổn định.", SwingConstants.LEFT);
        statusPanel.add(lblStatus, BorderLayout.WEST);

        if (loggedInUser != null) {
            // Sửa đổi phương thức hiển thị lấy đúng chuỗi quyền an toàn tài khoản
            JLabel lblLoggedIn = new JLabel("Tài khoản trực ca: " + loggedInUser.getTenDangNhap() + " [" + loggedInUser.getQuyen() + "]", SwingConstants.RIGHT);
            lblLoggedIn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblLoggedIn.setForeground(primaryColor);
            statusPanel.add(lblLoggedIn, BorderLayout.EAST);
        }
        return statusPanel;
    }

    private void loadWarehouses() {
        cbKho.removeAllItems();
        cbKho.addItem(new Kho("", "-- Tất cả các phân kho --"));
        List<Kho> khoList = khoDAO.getAllKho();
        if (khoList != null) {
            for (Kho kho : khoList) {
                cbKho.addItem(kho);
                khoCache.put(kho.getMaKho(), kho);
            }
        }
    }

    private void loadAndDisplayIngredients() {
        mainPanel.removeAll();
        
        // Đọc danh sách thực tế từ Database thông qua DAO
        List<NguyenLieu> list = nguyenLieuDAO.getAllNguyenLieu();
        
        // Xử lý bộ lọc tìm kiếm theo từ khóa (nếu có)
        String searchKey = txtSearch != null ? txtSearch.getText().trim().toLowerCase() : "";
        
        if (list == null || list.isEmpty()) {
            showEmptyMessage("Không có dữ liệu nguyên liệu nào trong hệ thống cơ sở dữ liệu!");
            return;
        }

        // Tạo panel chứa lưới dạng dòng chảy (FlowLayout) giúp các Card co giãn tự nhiên không bị lỗi khoảng trắng to
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        wrapperPanel.setBackground(secondaryColor);
        int matchCount = 0;

        // Tiến hành sắp xếp danh sách theo tiêu chí lựa chọn
        if ("name_asc".equals(currentSort)) {
            list.sort((n1, n2) -> n1.getTenNL().compareToIgnoreCase(n2.getTenNL()));
        } else if ("qty_asc".equals(currentSort)) {
            list.sort((n1, n2) -> Integer.compare(n1.getSoluong(), n2.getSoluong()));
        }

        for (NguyenLieu nl : list) {
            // 1. Áp dụng bộ lọc theo phân kho
            if (activeFilter != null && !activeFilter.isEmpty() && !activeFilter.equalsIgnoreCase(nl.getMaKho())) {
                continue;
            }
            
            // 2. Áp dụng bộ lọc theo từ khóa tìm kiếm (Tên hoặc Mã)
            if (!searchKey.isEmpty() && !nl.getTenNL().toLowerCase().contains(searchKey) && !nl.getMaNL().toLowerCase().contains(searchKey)) {
                continue;
            }

            matchCount++;
            
            // THIẾT KẾ THẺ CARD VẬT TƯ CHUẨN JOLLIBEE STYLE
            JPanel card = new JPanel(new BorderLayout(10, 10));
            card.setPreferredSize(new Dimension(280, 140)); // Cố định kích thước chuẩn thẻ trực quan
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225), 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            
            // Tiêu đề thẻ: Tên nguyên liệu
            JLabel lblName = new JLabel(nl.getTenNL().toUpperCase());
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblName.setForeground(textColor);
            card.add(lblName, BorderLayout.NORTH);
            
            // Thân thẻ: Hiển thị thông số chi tiết mã vật tư & lượng tồn kho thực tế
            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            infoPanel.setBackground(Color.WHITE);
            
            JLabel lblId = new JLabel("Mã vật tư: " + nl.getMaNL());
            lblId.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblId.setForeground(Color.GRAY);
            
            int soLuong = nl.getSoluong();
            JLabel lblQty = new JLabel("Tồn kho hiện tại: " + soLuong);
            lblQty.setFont(new Font("Segoe UI", Font.BOLD, 13));
            
            // Cảnh báo màu sắc thông minh: Đỏ rực nếu sắp hết, xanh lục an toàn
            if (soLuong <= 10) { 
                lblQty.setForeground(primaryColor);
                lblQty.setText(lblQty.getText() + " (Sắp hết hàng!)");
            } else {
                lblQty.setForeground(accentGreen);
            }
            
            infoPanel.add(lblId);
            infoPanel.add(lblQty);
            card.add(infoPanel, BorderLayout.CENTER);
            
            // Chân thẻ: Thanh Highlight vàng nhận diện thương hiệu Jollibee
            JPanel accentBar = new JPanel();
            accentBar.setPreferredSize(new Dimension(280, 4));
            accentBar.setBackground(accentYellow);
            card.add(accentBar, BorderLayout.SOUTH);
            
            wrapperPanel.add(card);
        }
        
        if (matchCount == 0) {
            showEmptyMessage("Không tìm thấy nguyên liệu phù hợp với tiêu chí tìm kiếm/lọc!");
            return;
        }

        mainPanel.add(wrapperPanel);
        
        if (lblStatus != null) {
            lblStatus.setText("Đồng bộ dữ liệu thành công. Tìm thấy " + matchCount + " nguyên liệu.");
        }
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showEmptyMessage(String message) {
        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setBackground(secondaryColor);
        JLabel lblEmpty = new JLabel(message);
        lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblEmpty.setForeground(Color.GRAY);
        emptyPanel.add(lblEmpty);
        mainPanel.add(emptyPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void searchIngredients(String key) {
        // Tự động kích hoạt luồng quét lại thông qua bộ lọc từ khóa tích hợp trong hàm hiển thị chính
        loadAndDisplayIngredients();
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}