package gui.dashboard;

import gui.dashboard.model.Product;
import gui.dashboard.model.CartManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductCard extends JPanel {
    
    private Product product;

    public ProductCard(Product product, CartManager cartManager) {
        this.product = product;
        setPreferredSize(new Dimension(150, 180)); 
        setMaximumSize(new Dimension(150, 180));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 5));
        
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(235, 235, 235), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblImage = new JLabel(product.getIcon(), SwingConstants.CENTER);
        lblImage.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48)); 
        lblImage.setPreferredSize(new Dimension(130, 80));
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(250, 245, 240)); 
        lblImage.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245), 1, true));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblName = new JLabel(product.getName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        lblName.setForeground(new Color(40, 40, 40));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblPrice = new JLabel(String.format("%,.0f đ", product.getPrice()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        lblPrice.setForeground(new Color(220, 60, 20)); 
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblStock = new JLabel("Kho: " + product.getStock());
        lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStock.setForeground(new Color(120, 120, 120));
        lblStock.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(lblName);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblStock);
        
        add(lblImage, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        
        // Hover & Click Effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                setBackground(new Color(255, 252, 245));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 165, 0), 1, true),
                    new EmptyBorder(10, 10, 10, 10)
                ));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(235, 235, 235), 1, true),
                    new EmptyBorder(10, 10, 10, 10)
                ));
            }
            @Override
            public void mouseClicked(MouseEvent evt) {
                showAddToCartDialog(cartManager);
            }
        });
    }

    private void showAddToCartDialog(CartManager cartManager) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.add(new JLabel("Sản phẩm: " + product.getName()));
        panel.add(new JLabel("Đơn giá: " + String.format("%,.0f đ", product.getPrice())));
        panel.add(new JLabel("Kho hiện tại: " + product.getStock()));
        
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        qtyPanel.add(new JLabel("Số lượng mua: "));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, product.getStock(), 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        qtyPanel.add(spinner);
        panel.add(qtyPanel);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm vào giỏ hàng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int qty = (int) spinner.getValue();
            cartManager.addProduct(product, qty);
            JOptionPane.showMessageDialog(this, "Đã thêm " + qty + " " + product.getName() + " vào giỏ!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
