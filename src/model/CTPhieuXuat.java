package model;

public class CTPhieuXuat {
    private String maPX;
    private String maNL; 
    private int soLuong;
    private int donGia;
    private int thanhTien;

    // Thuộc tính phụ phục vụ hiển thị JTable
    private String tenNL; 
    public CTPhieuXuat() {
    }


    public CTPhieuXuat(String maPX, String maNL, int soLuong, int donGia, int thanhTien) {
        this.maPX = maPX;
        this.maNL = maNL; 
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

   
    
    public String getMaPX() { return maPX; }
    public void setMaPX(String maPX) { this.maPX = maPX; }

   
    public String getMaNL() { return maNL; }
    public void setMaNL(String maNL) { this.maNL = maNL; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public int getDonGia() { return donGia; }
    public void setDonGia(int donGia) { this.donGia = donGia; }

    public int getThanhTien() { return thanhTien; }
    public void setThanhTien(int thanhTien) { this.thanhTien = thanhTien; }

    public String getTenNL() { return tenNL; }
    public void setTenNL(String tenNL) { this.tenNL = tenNL; }
}