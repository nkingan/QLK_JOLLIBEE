package model;

import java.util.Date;

public class PhieuXuat {
    private String maPX;      
    private Date ngayXuat;    
    private String maNV;       
    private double tongTien;   
    
    
    private String tenNV;      

    public PhieuXuat() {
    }

    public PhieuXuat(String maPX, Date ngayXuat, String maNV, double tongTien) {
        this.maPX = maPX;
        this.ngayXuat = ngayXuat;
        this.maNV = maNV;
        this.tongTien = tongTien;
    }


    public String getMaPX() { return maPX; }
    public void setMaPX(String maPX) { this.maPX = maPX; }

    public Date getNgayXuat() { return ngayXuat; }
    public void setNgayXuat(Date ngayXuat) { this.ngayXuat = ngayXuat; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }
}