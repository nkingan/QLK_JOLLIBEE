package model;

public class Kho {

    private String maKho;
    private String tenKho;
    private String maNV;
    private String diaChi;
    private int sucChua;
    private String ghiChu;

    public Kho() {
    }

    public Kho(String maKho, String tenKho, String maNV) {
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.maNV = maNV;
        this.diaChi = "";
        this.sucChua = 0;
        this.ghiChu = "";
    }

    public Kho(String maKho, String tenKho, String maNV, String diaChi, int sucChua, String ghiChu) {
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.maNV = maNV;
        this.diaChi = diaChi;
        this.sucChua = sucChua;
        this.ghiChu = ghiChu;
    }

    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public String getTenKho() {
        return tenKho;
    }

    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Constructor for dropdown compatibility
    public Kho(String maKho, String tenKho) {
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.maNV = "";
        this.diaChi = "";
        this.sucChua = 0;
        this.ghiChu = "";
    }

    @Override
    public String toString() {
        return this.tenKho;
    }
}