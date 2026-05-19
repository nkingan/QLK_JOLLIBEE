package gui.dashboard.model;

import java.util.List;

public class Invoice {
    private String id;
    private String date;
    private String employeeName;
    private double totalAmount;
    private List<InvoiceDetail> details;

    public Invoice(String id, String date, String employeeName, double totalAmount, List<InvoiceDetail> details) {
        this.id = id;
        this.date = date;
        this.employeeName = employeeName;
        this.totalAmount = totalAmount;
        this.details = details;
    }

    public String getId() { return id; }
    public String getDate() { return date; }
    public String getEmployeeName() { return employeeName; }
    public double getTotalAmount() { return totalAmount; }
    public List<InvoiceDetail> getDetails() { return details; }
}
