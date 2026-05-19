package gui.dashboard.model;

public class Product {
    private String id;
    private String name;
    private double price;
    private int stock;
    private String icon;

    public Product(String id, String name, double price, int stock, String icon) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.icon = icon;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getIcon() { return icon; }
}
