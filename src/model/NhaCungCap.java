package model;

public class NhaCungCap {
    private String maNCC;
    private String tenNCC;
    private String diachi;
    private String sdt;
    private String email;

    // Constructor rỗng
    public NhaCungCap() {
    }

    // Constructor đầy đủ tham số
    public NhaCungCap(String maNCC, String tenNCC, String diachi, String sdt, String email) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.diachi = diachi;
        this.sdt = sdt;
        this.email = email;
    }

    // ==========================================
    // GETTERS VÀ SETTERS CHUẨN
    // ==========================================
    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}