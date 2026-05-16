// package model;

// public class ChiTietPhieuXuat {
//     private String maPX;
//     private String maNL;
//     private int soLuong;

//     public String getMaPX() { return maPX; }
//     public void setMaPX(String maPX) { this.maPX = maPX; }

//     public String getMaNL() { return maNL; }
//     public void setMaNL(String maNL) { this.maNL = maNL; }

//     public int getSoLuong() { return soLuong; }
//     public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
// }
package model;

import java.math.BigDecimal;

/**
 * Model ánh xạ bảng ChiTietPhieuXuat trong SQL Server.
 *
 * CREATE TABLE ChiTietPhieuXuat (
 *     MaCTPX  VARCHAR(20)   PRIMARY KEY,
 *     SoLuong INT           CHECK (SoLuong > 0),
 *     DonGia  DECIMAL(18,2) CHECK (DonGia  > 0),
 *     MaNL    VARCHAR(20),
 *     MaPX    VARCHAR(20)
 * )
 */
public class ChiTietPhieuXuat {

    private String     maCTPX;
    private int        soLuong;
    private BigDecimal donGia;  // DECIMAL(18,2) — tránh sai số float
    private String     maNL;
    private String     maPX;

    // Dùng khi hiển thị (query từ VW_ChiTietPhieuXuat)
    private String     tenNL;
    private String     tenKho;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ChiTietPhieuXuat() {
        this.donGia = BigDecimal.ZERO;
    }

    /** Thứ tự tham số khớp thứ tự cột trong DB: MaCTPX, SoLuong, DonGia, MaNL, MaPX */
    public ChiTietPhieuXuat(String maCTPX, int soLuong, BigDecimal donGia,
                             String maNL, String maPX) {
        this.maCTPX  = maCTPX;
        setSoLuong(soLuong);   // dùng setter để chạy validation
        setDonGia(donGia);
        this.maNL    = maNL;
        this.maPX    = maPX;
    }

    // =========================================================
    // GETTER / SETTER — có validation khớp CHECK constraint của DB
    // =========================================================

    public String getMaCTPX() { return maCTPX; }
    public void setMaCTPX(String maCTPX) { this.maCTPX = maCTPX; }

    public int getSoLuong() { return soLuong; }
    /** Khớp CHECK (SoLuong > 0) trong DB — chặn lỗi sớm trước khi xuống DB */
    public void setSoLuong(int soLuong) {
        if (soLuong <= 0)
            throw new IllegalArgumentException("SoLuong phải > 0, nhận: " + soLuong);
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() { return donGia; }
    /** Khớp CHECK (DonGia > 0) trong DB — chặn lỗi sớm trước khi xuống DB */
    public void setDonGia(BigDecimal donGia) {
        if (donGia == null || donGia.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("DonGia phải > 0, nhận: " + donGia);
        this.donGia = donGia;
    }

    public String getMaNL() { return maNL; }
    public void setMaNL(String maNL) { this.maNL = maNL; }

    public String getMaPX() { return maPX; }
    public void setMaPX(String maPX) { this.maPX = maPX; }

    /** Field phụ — chỉ dùng khi đọc từ VW_ChiTietPhieuXuat, không ghi xuống DB */
    public String getTenNL() { return tenNL; }
    public void setTenNL(String tenNL) { this.tenNL = tenNL; }

    public String getTenKho() { return tenKho; }
    public void setTenKho(String tenKho) { this.tenKho = tenKho; }

    // =========================================================
    // UTILITY
    // =========================================================

    /**
     * Tính thành tiền = DonGia × SoLuong.
     * Đặt ở model vì đây là logic thuần túy của object,
     * không phụ thuộc DB hay tầng DAO.
     */
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