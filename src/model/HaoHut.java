package model;

import java.util.Date;

public class HaoHut {
    private String maHH;
    private String maNL;
    private String tenNL; // Auxiliary field for UI display
    private int soLuongHeThong;
    private int soLuongThucTe;
    private int soLuongHaoHut;
    private double phanTramHaoHut;
    private String lyDo;
    private Date ngayGhiNhan;

    public HaoHut() {
    }

    public HaoHut(String maHH, String maNL, int soLuongHeThong, int soLuongThucTe, int soLuongHaoHut, double phanTramHaoHut, String lyDo, Date ngayGhiNhan) {
        this.maHH = maHH;
        this.maNL = maNL;
        this.soLuongHeThong = soLuongHeThong;
        this.soLuongThucTe = soLuongThucTe;
        this.soLuongHaoHut = soLuongHaoHut;
        this.phanTramHaoHut = phanTramHaoHut;
        this.lyDo = lyDo;
        this.ngayGhiNhan = ngayGhiNhan;
    }

    // Getters and Setters
    public String getMaHH() {
        return maHH;
    }

    public void setMaHH(String maHH) {
        this.maHH = maHH;
    }

    public String getMaNL() {
        return maNL;
    }

    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public String getTenNL() {
        return tenNL;
    }

    public void setTenNL(String tenNL) {
        this.tenNL = tenNL;
    }

    public int getSoLuongHeThong() {
        return soLuongHeThong;
    }

    public void setSoLuongHeThong(int soLuongHeThong) {
        this.soLuongHeThong = soLuongHeThong;
    }

    public int getSoLuongThucTe() {
        return soLuongThucTe;
    }

    public void setSoLuongThucTe(int soLuongThucTe) {
        this.soLuongThucTe = soLuongThucTe;
    }

    public int getSoLuongHaoHut() {
        return soLuongHaoHut;
    }

    public void setSoLuongHaoHut(int soLuongHaoHut) {
        this.soLuongHaoHut = soLuongHaoHut;
    }

    public double getPhanTramHaoHut() {
        return phanTramHaoHut;
    }

    public void setPhanTramHaoHut(double phanTramHaoHut) {
        this.phanTramHaoHut = phanTramHaoHut;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public Date getNgayGhiNhan() {
        return ngayGhiNhan;
    }

    public void setNgayGhiNhan(Date ngayGhiNhan) {
        this.ngayGhiNhan = ngayGhiNhan;
    }
}
