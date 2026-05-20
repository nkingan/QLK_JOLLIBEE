package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class NhaCungCapPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMa, txtTen, txtDiaChi, txtSdt, txtEmail;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public NhaCungCapPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlInput = new JPanel(new GridLayout(6, 2, 5, 5));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        pnlInput.add(new JLabel("Mã NCC:"));
        txtMa = new JTextField(15);
        pnlInput.add(txtMa);

        pnlInput.add(new JLabel("Tên NCC:"));
        txtTen = new JTextField();
        pnlInput.add(txtTen);

        pnlInput.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        pnlInput.add(txtDiaChi);

        pnlInput.add(new JLabel("Số điện thoại:"));
        txtSdt = new JTextField();
        pnlInput.add(txtSdt);

        pnlInput.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnlInput.add(txtEmail);

        JPanel pnlButtons = new JPanel(new FlowLayout());
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);

        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.add(pnlInput, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlLeft, BorderLayout.WEST);

        String[] columns = {"Mã NCC", "Tên Nhà Cung Cấp", "Địa Chỉ", "SĐT", "Email"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setTableData(java.util.List<Object[]> dataList) {
        model.setRowCount(0);
        for (Object[] row : dataList) {
            model.addRow(row);
        }
    }
}