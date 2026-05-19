package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TrangChuUI extends JPanel {

    public TrangChuUI(String currentUser) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 50, 50)); // Jollibee Red
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblWelcome = new JLabel("Chào mừng trở lại, " + currentUser + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcome.setForeground(Color.WHITE);
        
        JLabel lblSub = new JLabel("Hệ thống Quản Lý Kho Jollibee");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSub.setForeground(new Color(255, 255, 255, 200));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(lblWelcome);
        titlePanel.add(lblSub);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Dashboard/Cards Section
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setBackground(new Color(245, 245, 245));
        cardsPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        cardsPanel.add(createCard("Nguyên Liệu", "Quản lý danh sách nguyên liệu, tồn kho", "🍗", new Color(255, 165, 0)));
        cardsPanel.add(createCard("Nhà Cung Cấp", "Thông tin đối tác cung cấp", "🏢", new Color(30, 144, 255)));
        cardsPanel.add(createCard("Nhân Viên", "Quản lý thông tin nhân sự", "👥", new Color(46, 139, 87)));
        cardsPanel.add(createCard("Giao Dịch Kho", "Quản lý phiếu nhập và xuất kho", "📦", new Color(138, 43, 226)));

        add(headerPanel, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String desc, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcon.setForeground(color);
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(50, 50, 50));

        JLabel lblDesc = new JLabel("<html><center>" + desc + "</center></html>", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(120, 120, 120));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(lblDesc);

        card.add(lblIcon, BorderLayout.CENTER);
        card.add(textPanel, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color, 2, true),
                        new EmptyBorder(19, 19, 19, 19)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        return card;
    }

    // For testing standalone
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Trang Chủ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new TrangChuUI("Admin"));
        frame.setVisible(true);
    }
}
