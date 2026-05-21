
package model;

import java.math.BigDecimal;
import java.sql.Date;


public class PhieuXuat {

    private String   maPX;
    private Date     ngayXuat;   
    private String   maNV;
    private BigDecimal tongTien; 

    private String tenNV;

    
    // CONSTRUCTOR
  

    public PhieuXuat() {
        this.tongTien = BigDecimal.ZERO; 
    }

    public PhieuXuat(String maPX, Date ngayXuat, String maNV, BigDecimal tongTien) {
        this.maPX     = maPX;
        this.ngayXuat = ngayXuat;
        this.maNV     = maNV;
        this.tongTien = tongTien != null ? tongTien : BigDecimal.ZERO;
    }


    public String getMaPX() { return maPX; }
    public void setMaPX(String maPX) { this.maPX = maPX; }

    public Date getNgayXuat() { return ngayXuat; }
    public void setNgayXuat(Date ngayXuat) { this.ngayXuat = ngayXuat; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) {
       
        this.tongTien = tongTien != null ? tongTien : BigDecimal.ZERO;
    }

  
    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    @Override
    public String toString() {
        return "PhieuXuat{" +
               "maPX='"     + maPX     + '\'' +
               ", ngayXuat=" + ngayXuat +
               ", maNV='"   + maNV     + '\'' +
               ", tongTien=" + tongTien +
               '}';
    }
}