package model;

import java.util.Date;

public class NhanVien {
    private String maNV;     // Mã nhân viên (Khóa chính)
    private String tenNV;    // Họ và tên
    private String sdt;      // Số điện thoại
    private String email;    // Địa chỉ Email
    private Date ngaySinh;   // Ngày sinh
    private String gioiTinh; // Giới tính: "Nam", "Nữ"
    private String chucVu;   // Chức vụ: "Admin", "Quản lý", "Nhân Viên"

    // Constructor mặc định
    public NhanVien() {
    }

    // Constructor đầy đủ tham số
    public NhanVien(String maNV, String tenNV, String sdt, String email, Date ngaySinh, String gioiTinh, String chucVu) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.chucVu = chucVu;
    }

    // Getter và Setter
    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    @Override
    public String toString() {
        return this.tenNV; // Hỗ trợ hiển thị trên ComboBox
    }
}