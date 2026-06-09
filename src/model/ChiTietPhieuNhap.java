
package model;

import java.util.Date;
import java.math.BigDecimal;

public class ChiTietPhieuNhap {
    private String maCTPN;
    private int soLuong;
    private BigDecimal donGia; // Khớp với DECIMAL(18,2) trong SQL
    private Date hanSuDung;
    private String maNL;
    private String maPN;

    // Các thuộc tính mở rộng lấy từ VIEW (VW_ChiTietPhieuNhap) để đẩy dữ liệu lên JTable
    private String tenNL;
    private String donViTinh;
    private BigDecimal thanhTien; // Trường tính sẵn từ SQL (SoLuong * DonGia)
    private String trangThaiHan;  // "Còn hạn", "Sắp hết hạn", "Hết hạn"

    // Constructor không tham số
    public ChiTietPhieuNhap() {
    }

    // Constructor đầy đủ tham số gốc (Dùng khi Thêm mới vào Database)
    public ChiTietPhieuNhap(String maCTPN, int soLuong, BigDecimal donGia, Date hanSuDung, String maNL, String maPN) {
        this.maCTPN = maCTPN;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.hanSuDung = hanSuDung;
        this.maNL = maNL;
        this.maPN = maPN;
    }

    // ==========================================
    // GETTERS VÀ SETTERS CHUẨN GỐC
    // ==========================================
    public String getMaCTPN() {
        return maCTPN;
    }

    public void setMaCTPN(String maCTPN) {
        this.maCTPN = maCTPN;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public String getMaNL() {
        return maNL;
    }

    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public String getMaPN() {
        return maPN;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    // ==========================================
    // GETTERS VÀ SETTERS CHO CÁC TRƯỜNG BỔ TRỢ TỪ VIEW
    // ==========================================
    public String getTenNL() {
        return tenNL;
    }

    public void setTenNL(String tenNL) {
        this.tenNL = tenNL;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getTrangThaiHan() {
        return trangThaiHan;
    }

    public void setTrangThaiHan(String trangThaiHan) {
        this.trangThaiHan = trangThaiHan;
    }
}