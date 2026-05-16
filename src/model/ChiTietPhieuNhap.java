package model;
import java.util.Date;
public class ChiTietPhieuNhap {
    private String maCTPN;
    private String maNL;
    private String maPN;
    private int soLuong;
    private double donGia;
    private Date hanSuDung;

    public ChiTietPhieuNhap() {
        this.maCTPN = "";
        this.maNL = "";
        this.maPN = "";
        this.soLuong = 0;
        this.donGia = 0;
        this.hanSuDung = new Date();
    }
    public ChiTietPhieuNhap(String maCTPN, int soLuong, String maNL, double donGia, String maPN) {
        this.maCTPN = maCTPN;
        this.soLuong = soLuong;
        this.maNL = maNL;
        this.donGia = donGia;
        this.maPN = maPN;
    }

    public String getMaCTPN() {
        return maCTPN;
    }

    public String getMaNL() {
        return maNL;
    }

    public String getMaPX() { 
        return maPN;          
    }

    public String getMaPN() {  
        return maPN;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public Date getHanSuDung() { 
        return hanSuDung;
    }

    public void setMaCTPN(String maCTPN) {
        this.maCTPN = maCTPN;
    }

    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public void setMaPX(String maPX) { 
        this.maPN = maPX;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public void setHanSuDung(Date hanSuDung) { 
        this.hanSuDung = hanSuDung;
    }
}