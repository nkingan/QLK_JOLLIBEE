package model;

public class NguyenLieu {

    private String maNL;
    private String tenNL;
    private String donViTinh;
    private int soLuong;
    private String maKho;

    public NguyenLieu(String maNL, String tenNL, String donViTinh, int soLuong, String maKho) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.donViTinh = donViTinh;
        this.soLuong = soLuong;
        this.maKho = maKho;
    }

   
    public String getMaNL() {
        return maNL;
    }

    public String getTenNL() {
        return tenNL;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getMaKho() {
        return maKho;
    }

 
    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public void setTenNL(String tenNL) {
        this.tenNL = tenNL;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }
}