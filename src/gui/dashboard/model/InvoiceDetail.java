package gui.dashboard.model;

public class InvoiceDetail {
    private String productName;
    private int quantity;
    private double price;
    private double subTotal;

    public InvoiceDetail(String productName, int quantity, double price, double subTotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getSubTotal() { return subTotal; }
}
