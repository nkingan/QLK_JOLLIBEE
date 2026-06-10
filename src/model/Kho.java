package model;

public class Kho {

    private String maKho;
    private String tenKho;
    private String maNV;

    public Kho(String maKho, String tenKho, String maNV) {
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.maNV = maNV;
    }

    public String getMaKho() {
        return maKho;
    }

    public String getTenKho() {
        return tenKho;
    }

    public String getMaNV() {
        return maNV;
    }

    
    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
}