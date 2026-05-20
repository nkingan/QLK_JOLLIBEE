package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class NguyenLieuPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public NguyenLieuPanel() {
        setLayout(new BorderLayout());

        String[] columns = {"Mã NL", "Tên Nguyên Liệu", "Số Lượng", "Đơn Giá", "Đơn Vị"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        loadData();
    }

    public void loadData() {
        model.setRowCount(0); 
    }
}