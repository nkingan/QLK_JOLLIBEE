package model;

public class TaiKhoan {
    private String maTK;        
    private String tenDangNhap; 
    private String matKhau;    
    private String quyen;       
    private String maNV;       


    public TaiKhoan() {
    }

    public TaiKhoan(String maTK, String tenDangNhap, String matKhau, String quyen, String maNV) {
        this.maTK = maTK;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyen = quyen;
        this.maNV = maNV;
    }


    public String getMaTK() { return maTK; }
    public void setMaTK(String maTK) { this.maTK = maTK; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    @Override
    public String toString() {
        return "TaiKhoan{" + "maTK='" + maTK + '\'' + ", tenDangNhap='" + tenDangNhap + '\'' + ", quyen=N'" + quyen + '\'' + ", maNV='" + maNV + '\'' + '}';
    }
}