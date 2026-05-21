
package model;

import java.math.BigDecimal;


public class ChiTietPhieuXuat {

    private String     maCTPX;
    private int        soLuong;
    private BigDecimal donGia;  
    private String     maNL;
    private String     maPX;

    
    private String     tenNL;
    private String     tenKho;

    
    // CONSTRUCTOR
    

    public ChiTietPhieuXuat() {
        this.donGia = BigDecimal.ZERO;
    }

   
    public ChiTietPhieuXuat(String maCTPX, int soLuong, BigDecimal donGia,
                             String maNL, String maPX) {
        this.maCTPX  = maCTPX;
        setSoLuong(soLuong);   
        setDonGia(donGia);
        this.maNL    = maNL;
        this.maPX    = maPX;
    }


    public String getMaCTPX() { return maCTPX; }
    public void setMaCTPX(String maCTPX) { this.maCTPX = maCTPX; }

    public int getSoLuong() { return soLuong; }
   
    public void setSoLuong(int soLuong) {
        if (soLuong <= 0)
            throw new IllegalArgumentException("SoLuong phải > 0, nhận: " + soLuong);
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() { return donGia; }
    
    public void setDonGia(BigDecimal donGia) {
        if (donGia == null || donGia.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("DonGia phải > 0, nhận: " + donGia);
        this.donGia = donGia;
    }

    public String getMaNL() { return maNL; }
    public void setMaNL(String maNL) { this.maNL = maNL; }

    public String getMaPX() { return maPX; }
    public void setMaPX(String maPX) { this.maPX = maPX; }

    
    public String getTenNL() { return tenNL; }
    public void setTenNL(String tenNL) { this.tenNL = tenNL; }

    public String getTenKho() { return tenKho; }
    public void setTenKho(String tenKho) { this.tenKho = tenKho; }

    
    public BigDecimal tinhThanhTien() {
        if (donGia == null) return BigDecimal.ZERO;
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    @Override
    public String toString() {
        return "ChiTietPhieuXuat{" +
               "maCTPX='"  + maCTPX  + '\'' +
               ", soLuong=" + soLuong +
               ", donGia="  + donGia  +
               ", maNL='"   + maNL    + '\'' +
               ", maPX='"   + maPX    + '\'' +
               '}';
    }
}