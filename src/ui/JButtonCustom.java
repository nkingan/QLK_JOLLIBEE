package ui;

import javax.swing.*;
import java.awt.*;

public class JButtonCustom extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;

    public JButtonCustom(String text, Color bg, Color fg) {
        super(text);
        this.backgroundColor = bg;
        // Generate nice hover and pressed colors
        this.hoverColor = getBrighterColor(bg);
        this.pressedColor = getDarkerColor(bg);
        
        setForeground(fg);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setBackgroundColor(Color bg) {
        this.backgroundColor = bg;
        this.hoverColor = getBrighterColor(bg);
        this.pressedColor = getDarkerColor(bg);
        repaint();
    }

    private Color getBrighterColor(Color c) {
        int r = Math.min(255, c.getRed() + 30);
        int g = Math.min(255, c.getGreen() + 30);
        int b = Math.min(255, c.getBlue() + 30);
        return new Color(r, g, b);
    }

    private Color getDarkerColor(Color c) {
        int r = Math.max(0, c.getRed() - 30);
        int g = Math.max(0, c.getGreen() - 30);
        int b = Math.max(0, c.getBlue() - 30);
        return new Color(r, g, b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (!isEnabled()) {
            g2.setColor(new Color(200, 200, 200));
        } else if (getModel().isPressed()) {
            g2.setColor(pressedColor);
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(backgroundColor);
        }
        
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        g2.dispose();
        
        super.paintComponent(g);
    }
}
