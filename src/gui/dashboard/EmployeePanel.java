package gui.dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeePanel extends JPanel {

    public EmployeePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblTitle = new JLabel("👥 Quản Lý Nhân Viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);

        String[] columns = {"Mã NV", "Họ Tên", "SĐT", "Chức Vụ", "Trạng Thái"};
        Object[][] data = {
            {"NV001", "Nguyễn Văn A", "0901234567", "Quản lý", "Đang làm"},
            {"NV002", "Trần Thị B", "0912345678", "Thủ kho", "Đang làm"},
            {"NV003", "Lê Văn C", "0923456789", "Nhân viên", "Đang làm"},
            {"NV004", "Phạm Thị D", "0934567890", "Thủ kho", "Nghỉ phép"}
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
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(20, 0));
        bottomPanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setOpaque(false);
        formPanel.add(new JLabel("Mã NV:")); formPanel.add(new JTextField());
        formPanel.add(new JLabel("Họ Tên:")); formPanel.add(new JTextField());
        formPanel.add(new JLabel("SĐT:")); formPanel.add(new JTextField());
        formPanel.add(new JLabel("Chức Vụ:")); formPanel.add(new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(createButton("Thêm", new Color(46, 139, 87)));
        buttonPanel.add(createButton("Sửa", new Color(30, 144, 255)));
        buttonPanel.add(createButton("Xóa", new Color(220, 50, 50)));

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
