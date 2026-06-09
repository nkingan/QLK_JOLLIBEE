// package model;

// import java.util.Date;

// public class PhieuNhap {

//     private String maPN;
//     private Date ngayNhap; 
//     private String maNV;
//     private String maNCC;
//     private double tongTien;

  
//     public PhieuNhap() {
//         maPN =" ";
//         ngayNhap=new Date();
//         maNV=" ";
//         maNCC=" ";
//         tongTien=0;
//     }

   
//     public PhieuNhap(String maPN, Date ngayNhap, String maNV, String maNCC, double tongTien) {
//         this.maPN = maPN;
//         this.ngayNhap = ngayNhap;
//         this.maNV = maNV;
//         this.maNCC = maNCC;
//         this.tongTien = tongTien;
//     }

//     // --- GETTER & SETTER 

//     public String getMaPN() {
//         return maPN;
//     }

//     public void setMaPN(String maPN) {
//         this.maPN = maPN;
//     }

//     public Date getNgayNhap() { 
//         return ngayNhap;
//     }

//     public void setNgayNhap(Date ngayNhap) {
//         this.ngayNhap = ngayNhap;
//     }

//     public String getMaNV() {
//         return maNV;
//     }

//     public void setMaNV(String maNV) {
//         this.maNV = maNV;
//     }

//     public String getMaNCC() {
//         return maNCC;
//     }

//     public void setMaNCC(String maNCC) {
//         this.maNCC = maNCC;
//     }

//     public double getTongTien() {
//         return tongTien;
//     }

//     public void setTongTien(double tongTien) {
//         this.tongTien = tongTien;
//     }
// }
package model;

import java.util.Date;
import java.math.BigDecimal;

public class PhieuNhap {
    private String maPN;
    private Date ngayNhap;
    private String maNV;
    private String maNCC;
    private BigDecimal tongTien; // Khớp với DECIMAL(18,2) trong SQL

    // Các thuộc tính mở rộng lấy từ VIEW (VW_PhieuNhap) để hiển thị lên UI Table
    private String tenNV;
    private String tenNCC;

    // Constructor không tham số
    public PhieuNhap() {
    }

    // Constructor đầy đủ tham số
    public PhieuNhap(String maPN, Date ngayNhap, String maNV, String maNCC, BigDecimal tongTien) {
        this.maPN = maPN;
        this.ngayNhap = ngayNhap;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.tongTien = tongTien;
    }

    // ==========================================
    // GETTERS VÀ SETTERS CHUẨN
    // ==========================================
    public String getMaPN() {
        return maPN;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    // ==========================================
    // GETTERS VÀ SETTERS CHO CÁC TRƯỜNG TỪ VIEW
    // ==========================================
    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }
}