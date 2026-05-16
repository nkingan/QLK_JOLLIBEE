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

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Model ánh xạ bảng PhieuXuat trong SQL Server.
 *
 * CREATE TABLE PhieuXuat (
 *     MaPX      VARCHAR(20)    PRIMARY KEY,
 *     NgayXuat  DATE           DEFAULT GETDATE(),
 *     MaNV      VARCHAR(20),
 *     TongTien  DECIMAL(18,2)  DEFAULT 0 CHECK (TongTien >= 0)
 * )
 */
public class PhieuXuat {

    private String   maPX;
    private Date     ngayXuat;   // java.sql.Date — khớp kiểu DATE của SQL Server
    private String   maNV;
    private BigDecimal tongTien; // DECIMAL(18,2) — tránh sai số float

    // Dùng khi hiển thị danh sách (query từ VW_PhieuXuat)
    private String tenNV;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PhieuXuat() {
        this.tongTien = BigDecimal.ZERO; // khớp DEFAULT 0 trong DB
    }

    public PhieuXuat(String maPX, Date ngayXuat, String maNV, BigDecimal tongTien) {
        this.maPX     = maPX;
        this.ngayXuat = ngayXuat;
        this.maNV     = maNV;
        this.tongTien = tongTien != null ? tongTien : BigDecimal.ZERO;
    }

    // =========================================================
    // GETTER / SETTER
    // =========================================================

    public String getMaPX() { return maPX; }
    public void setMaPX(String maPX) { this.maPX = maPX; }

    public Date getNgayXuat() { return ngayXuat; }
    public void setNgayXuat(Date ngayXuat) { this.ngayXuat = ngayXuat; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) {
        // TongTien do trigger tự tính — chỉ chặn null, không chặn 0
        this.tongTien = tongTien != null ? tongTien : BigDecimal.ZERO;
    }

    /** Field phụ — chỉ dùng khi đọc từ VW_PhieuXuat, không ghi xuống DB */
    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    // =========================================================
    // UTILITY
    // =========================================================

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