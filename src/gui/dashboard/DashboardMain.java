package gui.dashboard;

import gui.dashboard.model.CartManager;

import javax.swing.*;
import java.awt.*;

public class DashboardMain extends JFrame implements SidebarPanel.SidebarListener {

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private CartManager cartManager;
    private InvoicePanel invoicePanel;
    private CartPanel cartPanel;
    private SidebarPanel sidebar;

    public DashboardMain(String adminName) {
        setTitle("Quản Lý Kho Jollibee - Dashboard & POS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1280, 720); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cartManager = new CartManager();

        // Sidebar on the West
        sidebar = new SidebarPanel(this);
        add(sidebar, BorderLayout.WEST);

        // Center Panel containing Header and Content
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        
        HeaderPanel header = new HeaderPanel(adminName, cartManager, () -> {
            cardLayout.show(cardPanel, "CART");
        });
        mainContentPanel.add(header, BorderLayout.NORTH);
        
        // CardLayout Panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(245, 246, 250)); 
        
        // Initialize Panels
        DashboardPanel dashboard = new DashboardPanel(cartManager);
        
        invoicePanel = new InvoicePanel(() -> {
            cardLayout.show(cardPanel, "HOME"); // Back to home after invoice
        });
        
        cartPanel = new CartPanel(cartManager, () -> {
            // On Checkout Success
            invoicePanel.generateInvoice(cartManager);
            cardLayout.show(cardPanel, "INVOICE");
        });

        // Add to card
        cardPanel.add(dashboard, "HOME");
        cardPanel.add(new ProductPanel(), "PRODUCT");
        cardPanel.add(new EmployeePanel(), "EMPLOYEE");
        cardPanel.add(new ImportPanel(), "IMPORT");
        cardPanel.add(new ExportPanel(), "EXPORT");
        cardPanel.add(new StatisticPanel(), "STATISTIC");
        cardPanel.add(cartPanel, "CART");
        cardPanel.add(invoicePanel, "INVOICE");

        mainContentPanel.add(cardPanel, BorderLayout.CENTER);
        add(mainContentPanel, BorderLayout.CENTER);
        
        cardLayout.show(cardPanel, "HOME");
    }

    @Override
    public void onMenuSelected(String menuName) {
        if(menuName.equals("CART")) {
            cartPanel.loadData();
        }
        cardLayout.show(cardPanel, menuName);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new DashboardMain("Admin Jollibee").setVisible(true);
        });
    }
}
