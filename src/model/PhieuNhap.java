package model;

import java.util.Date;

public class PhieuNhap {

    private String maPN;
    private Date ngayNhap; 
    private String maNV;
    private String maNCC;
    private double tongTien;

  
    public PhieuNhap() {
        maPN =" ";
        ngayNhap=new Date();
        maNV=" ";
        maNCC=" ";
        tongTien=0;
    }

   
    public PhieuNhap(String maPN, Date ngayNhap, String maNV, String maNCC, double tongTien) {
        this.maPN = maPN;
        this.ngayNhap = ngayNhap;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.tongTien = tongTien;
    }

    // --- GETTER & SETTER 

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

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}