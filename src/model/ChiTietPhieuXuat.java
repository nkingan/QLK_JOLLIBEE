// package model;

// public class ChiTietPhieuXuat {
//     private String maPX;
//     private String maNL;
//     private int soLuong;

//     public String getMaPX() { return maPX; }
//     public void setMaPX(String maPX) { this.maPX = maPX; }

//     public String getMaNL() { return maNL; }
//     public void setMaNL(String maNL) { this.maNL = maNL; }

//     public int getSoLuong() { return soLuong; }
//     public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
// }
package model;

public class ChiTietPhieuXuat {
    private String maCTPX, maNL, maPX;
    private int soLuong;
    private double donGia;

    public ChiTietPhieuXuat(String maCTPX, int soLuong, String maNL, double donGia, String maPX) {
        this.maCTPX = maCTPX; this.soLuong = soLuong; this.maNL = maNL;
        this.donGia = donGia; this.maPX = maPX;
    }

    public String getMaCTPX() { return maCTPX; }
    public int    getSoLuong(){ return soLuong; }
    public String getMaNL()   { return maNL; }
    public double getDonGia() { return donGia; }
    public String getMaPX()   { return maPX; }
}