
// package model;

// import java.util.Date;

// public class ChiTietPhieuNhap {
//     private String maPN;
//     private String maNL;
//     private int soLuong;
//     private double donGiaNhap;
//     private Date hsd;

//     public String getMaPN() { return maPN; }
//     public void setMaPN(String maPN) { this.maPN = maPN; }

//     public String getMaNL() { return maNL; }
//     public void setMaNL(String maNL) { this.maNL = maNL; }

//     public int getSoLuong() { return soLuong; }
//     public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

//     public double getDonGiaNhap() { return donGiaNhap; }
//     public void setDonGiaNhap(double donGiaNhap) { this.donGiaNhap = donGiaNhap; }

//     public Date getHsd() { return hsd; }
//     public void setHsd(Date hsd) { this.hsd = hsd; }
// }
package model;

public class ChiTietPhieuNhap {
    private String maCTPN, maNL, maPN;
    private int soLuong;
    private double donGia;

    public ChiTietPhieuNhap(String maCTPN, int soLuong, String maNL, double donGia, String maPN) {
        this.maCTPN = maCTPN; this.soLuong = soLuong; this.maNL = maNL;
        this.donGia = donGia; this.maPN = maPN;
    }

    public String getMaCTPN() { return maCTPN; }
    public int    getSoLuong(){ return soLuong; }
    public String getMaNL()   { return maNL; }
    public double getDonGia() { return donGia; }
    public String getMaPN()   { return maPN; }
}