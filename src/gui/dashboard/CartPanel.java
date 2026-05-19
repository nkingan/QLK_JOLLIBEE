package gui.dashboard;

import gui.dashboard.model.CartItem;
import gui.dashboard.model.CartManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartPanel extends JPanel {

    private CartManager cartManager;
    private DefaultTableModel model;
    private JTable table;
    private JLabel lblTotalAmount;
    private Runnable onCheckoutSuccess;

    public CartPanel(CartManager cartManager, Runnable onCheckoutSuccess) {
        this.cartManager = cartManager;
        this.onCheckoutSuccess = onCheckoutSuccess;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblTitle = new JLabel("🛒 Giỏ Hàng & Thanh Toán");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);

        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Thành Tiền"};
        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 60, 20));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setOpaque(false);
        JButton btnRemove = createButton("Xóa Sản Phẩm", new Color(220, 50, 50));
        JButton btnClear = createButton("Làm Sạch Giỏ", new Color(100, 100, 100));
        actionPanel.add(btnRemove);
        actionPanel.add(btnClear);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        totalPanel.setOpaque(false);
        
        lblTotalAmount = new JLabel("Tổng cộng: 0 đ");
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotalAmount.setForeground(new Color(220, 60, 20));
        
        JButton btnCheckout = createButton("Thanh Toán & Hóa Đơn", new Color(46, 139, 87));
        btnCheckout.setPreferredSize(new Dimension(200, 45));
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 16));

        totalPanel.add(lblTotalAmount);
        totalPanel.add(btnCheckout);

        bottomPanel.add(actionPanel, BorderLayout.WEST);
        bottomPanel.add(totalPanel, BorderLayout.EAST);

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Actions
        btnRemove.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                cartManager.removeItem(id);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            }
        });

        btnClear.addActionListener(e -> {
            cartManager.clearCart();
            loadData();
        });

        btnCheckout.addActionListener(e -> {
            if (cartManager.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
                return;
            }
            int result = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán và in hóa đơn?", "Thanh toán", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                onCheckoutSuccess.run();
                cartManager.clearCart();
            }
        });

        // Listen for updates from other panels
        cartManager.setOnCartChanged(() -> loadData());
    }

    public void loadData() {
        model.setRowCount(0);
        List<CartItem> items = cartManager.getItems();
        for (CartItem item : items) {
            model.addRow(new Object[]{
                item.getProduct().getId(),
                item.getProduct().getName(),
                String.format("%,.0f đ", item.getProduct().getPrice()),
                item.getQuantity(),
                String.format("%,.0f đ", item.getTotal())
            });
        }
        lblTotalAmount.setText("Tổng cộng: " + String.format("%,.0f đ", cartManager.getTotalAmount()));
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
