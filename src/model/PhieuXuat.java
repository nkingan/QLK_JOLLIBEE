// package model;

// import java.util.Date;

// public class PhieuXuat {
//     private String maPX;
//     private String maNV;
//     private Date ngayXuat;

//     public String getMaPX() { return maPX; }
//     public void setMaPX(String maPX) { this.maPX = maPX; }

//     public String getMaNV() { return maNV; }
//     public void setMaNV(String maNV) { this.maNV = maNV; }

//     public Date getNgayXuat() { return ngayXuat; }
//     public void setNgayXuat(Date ngayXuat) { this.ngayXuat = ngayXuat; }
// }
package model;

public class PhieuXuat {
    private String maXuat, ngayXuat, maNV;
    private double tongTien;

    public PhieuXuat(String maXuat, String ngayXuat, String maNV, double tongTien) {
        this.maXuat = maXuat; this.ngayXuat = ngayXuat;
        this.maNV = maNV; this.tongTien = tongTien;
    }

    public String getMaXuat()   { return maXuat; }
    public String getNgayXuat() { return ngayXuat; }
    public String getMaNV()     { return maNV; }
    public double getTongTien() { return tongTien; }

    public void setMaXuat(String v)   { maXuat = v; }
    public void setNgayXuat(String v) { ngayXuat = v; }
    public void setMaNV(String v)     { maNV = v; }
    public void setTongTien(double v) { tongTien = v; }
}