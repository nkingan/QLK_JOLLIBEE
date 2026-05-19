package gui.dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductPanel extends JPanel {

    public ProductPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header Title
        JLabel lblTitle = new JLabel("📦 Quản Lý Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        // Center Panel for Table
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);

        // Table Model
        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Đơn Giá", "Số Lượng Tồn", "Đơn Vị"};
        Object[][] data = {
            {"SP001", "Gà Giòn Vui Vẻ", "35,000 đ", "120", "Miếng"},
            {"SP002", "Mì Ý Sốt Xúc Xích", "40,000 đ", "85", "Phần"},
            {"SP003", "Khoai Tây Chiên", "20,000 đ", "200", "Gói"},
            {"SP004", "Burger Gà", "45,000 đ", "50", "Cái"},
            {"SP005", "Nước Ngọt", "15,000 đ", "500", "Ly"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 60, 20));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(255, 200, 150));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Form & Buttons at the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 0));
        bottomPanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setOpaque(false);
        formPanel.add(new JLabel("Mã SP:")); formPanel.add(new JTextField());
        formPanel.add(new JLabel("Tên Sản Phẩm:")); formPanel.add(new JTextField());
        formPanel.add(new JLabel("Đơn Giá:")); formPanel.add(new JTextField());
        formPanel.add(new JLabel("Số Lượng:")); formPanel.add(new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnAdd = createButton("Thêm", new Color(46, 139, 87));
        JButton btnEdit = createButton("Sửa", new Color(30, 144, 255));
        JButton btnDelete = createButton("Xóa", new Color(220, 50, 50));
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
