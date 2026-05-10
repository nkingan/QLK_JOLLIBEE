// package model;

// public class TonKho {
//     private String maKho;
//     private String maNL;
//     private int soLuongTon;

//     public String getMaKho() { return maKho; }
//     public void setMaKho(String maKho) { this.maKho = maKho; }

//     public String getMaNL() { return maNL; }
//     public void setMaNL(String maNL) { this.maNL = maNL; }

//     public int getSoLuongTon() { return soLuongTon; }
//     public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }
// }
package model;

public class TonKho {
    private String maNL, tenNL, maKho;
    private int soLuong;

    public TonKho(String maNL, String tenNL, int soLuong, String maKho) {
        this.maNL = maNL; this.tenNL = tenNL; this.soLuong = soLuong; this.maKho = maKho;
    }

    public String getMaNL()  { return maNL; }
    public String getTenNL() { return tenNL; }
    public int getSoLuong()  { return soLuong; }
    public String getMaKho() { return maKho; }
}