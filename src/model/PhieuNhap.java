package model;

public class PhieuNhap {

    private String maPN;
    private String ngayNhap;
    private String maNV;
    private String maNCC;
    private double tongTien;

    public PhieuNhap(String maPN, String ngayNhap, String maNV, String maNCC, double tongTien) {
        this.maPN = maPN;
        this.ngayNhap = ngayNhap;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.tongTien = tongTien;
    }

    
    public String getMaPN() {
        return maPN;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public double getTongTien() {
        return tongTien;
    }

   
    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}