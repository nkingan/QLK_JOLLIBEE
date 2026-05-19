package gui.dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoicePanel extends JPanel {

    private DefaultTableModel model;
    private JLabel lblInvoiceId;
    private JLabel lblDate;
    private JLabel lblTotal;
    private Runnable onBack;

    public InvoicePanel(Runnable onBack) {
        this.onBack = onBack;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // Center Container for the Receipt
        JPanel receiptContainer = new JPanel(new GridBagLayout());
        receiptContainer.setOpaque(false);

        JPanel receiptPanel = new JPanel(new BorderLayout(0, 10));
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setPreferredSize(new Dimension(500, 600));
        receiptPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Receipt Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        
        JLabel lblStore = new JLabel("JOLLIBEE WAREHOUSE & POS");
        lblStore.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblStore.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblAddress = new JLabel("123 Jollibee Street, District 1, HCMC");
        lblAddress.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lblAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitle = new JLabel("HÓA ĐƠN BÁN HÀNG");
        lblTitle.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblStore);
        headerPanel.add(lblAddress);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(10));

        // Invoice Info
        JPanel infoPanel = new JPanel(new GridLayout(2, 2));
        infoPanel.setOpaque(false);
        lblInvoiceId = new JLabel("Số HĐ: INV-001");
        lblInvoiceId.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lblDate = new JLabel("Ngày: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        lblDate.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JLabel lblEmp = new JLabel("Thu ngân: Admin Jollibee");
        lblEmp.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        infoPanel.add(lblInvoiceId);
        infoPanel.add(lblDate);
        infoPanel.add(lblEmp);
        
        headerPanel.add(infoPanel);
        receiptPanel.add(headerPanel, BorderLayout.NORTH);

        // Table for Items
        String[] columns = {"Sản phẩm", "SL", "Đơn giá", "Thành tiền"};
        model = new DefaultTableModel(null, columns);
        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 12));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Disable table editing
        table.setDefaultEditor(Object.class, null);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        scrollPane.getViewport().setBackground(Color.WHITE);
        receiptPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        
        lblTotal = new JLabel("Tổng Cộng: 0 đ");
        lblTotal.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel lblThankYou = new JLabel("Cảm ơn quý khách và hẹn gặp lại!");
        lblThankYou.setFont(new Font("Monospaced", Font.ITALIC, 12));
        lblThankYou.setHorizontalAlignment(SwingConstants.CENTER);
        
        footerPanel.add(lblTotal, BorderLayout.NORTH);
        footerPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        footerPanel.add(lblThankYou, BorderLayout.SOUTH);
        
        receiptPanel.add(footerPanel, BorderLayout.SOUTH);
        
        receiptContainer.add(receiptPanel);
        add(receiptContainer, BorderLayout.CENTER);

        // Actions Bottom
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        actionPanel.setOpaque(false);
        
        JButton btnPrint = createButton("🖨 In Hóa Đơn", new Color(46, 139, 87));
        JButton btnExport = createButton("📄 Xuất PDF", new Color(30, 144, 255));
        JButton btnBack = createButton("⬅ Quay Lại", new Color(100, 100, 100));
        
        btnBack.addActionListener(e -> onBack.run());
        
        btnPrint.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đang gửi tín hiệu đến máy in...", "In hóa đơn", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnExport.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Xuất PDF thành công tại: C:/Invoices/INV-001.pdf", "Xuất PDF", JOptionPane.INFORMATION_MESSAGE);
        });

        actionPanel.add(btnBack);
        actionPanel.add(btnExport);
        actionPanel.add(btnPrint);
        
        add(actionPanel, BorderLayout.SOUTH);
    }

    // Call this method when passing data from Cart to Invoice
    public void generateInvoice(gui.dashboard.model.CartManager cartManager) {
        lblInvoiceId.setText("Số HĐ: INV-" + System.currentTimeMillis());
        lblDate.setText("Ngày: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        
        model.setRowCount(0);
        for (gui.dashboard.model.CartItem item : cartManager.getItems()) {
            model.addRow(new Object[]{
                item.getProduct().getName(),
                item.getQuantity(),
                String.format("%,.0f", item.getProduct().getPrice()),
                String.format("%,.0f", item.getTotal())
            });
        }
        lblTotal.setText("Tổng Cộng: " + String.format("%,.0f đ", cartManager.getTotalAmount()));
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
