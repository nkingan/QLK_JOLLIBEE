// package model;

// import java.util.Date;

// public class NhanVien {
//     private String maNV;
//     private String tenNV;
//     private String sdt;
//     private String email;
//     private Date ngaySinh;
//     private String gioiTinh;
//     private String chucVu;

//     public NhanVien() {}

//     public NhanVien(String maNV, String tenNV) {
//         this.maNV = maNV;
//         this.tenNV = tenNV;
//     }

//     public String getMaNV() { return maNV; }
//     public void setMaNV(String maNV) { this.maNV = maNV; }

//     public String getTenNV() { return tenNV; }
//     public void setTenNV(String tenNV) { this.tenNV = tenNV; }

//     public String getSdt() { return sdt; }
//     public void setSdt(String sdt) { this.sdt = sdt; }

//     public String getEmail() { return email; }
//     public void setEmail(String email) { this.email = email; }

//     public Date getNgaySinh() { return ngaySinh; }
//     public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

//     public String getGioiTinh() { return gioiTinh; }
//     public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

//     public String getChucVu() { return chucVu; }
//     public void setChucVu(String chucVu) { this.chucVu = chucVu; }
// }
package model;

public class NhanVien {
    private String maNV, tenNV, sdt, email, ngaySinh, gioiTinh, chucVu;

    public NhanVien(String maNV, String tenNV, String sdt, String email,
                    String ngaySinh, String gioiTinh, String chucVu) {
        this.maNV = maNV; this.tenNV = tenNV; this.sdt = sdt; this.email = email;
        this.ngaySinh = ngaySinh; this.gioiTinh = gioiTinh; this.chucVu = chucVu;
    }

    public String getMaNV()    { return maNV; }
    public String getTenNV()   { return tenNV; }
    public String getSdt()     { return sdt; }
    public String getEmail()   { return email; }
    public String getNgaySinh(){ return ngaySinh; }
    public String getGioiTinh(){ return gioiTinh; }
    public String getChucVu()  { return chucVu; }

    public void setMaNV(String v)    { maNV = v; }
    public void setTenNV(String v)   { tenNV = v; }
    public void setSdt(String v)     { sdt = v; }
    public void setEmail(String v)   { email = v; }
    public void setNgaySinh(String v){ ngaySinh = v; }
    public void setGioiTinh(String v){ gioiTinh = v; }
    public void setChucVu(String v)  { chucVu = v; }
}