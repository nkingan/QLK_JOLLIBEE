package model;

import java.util.Date;

public class HaoHut {
    private String maHH;
    private Date ngayBaoCao;
    private String maNL, tenNL;
    private int soLuongHeThong, soLuongThucTe, soLuongHaoHut;
    private double phanTramHaoHut;
    private String lyDo, maNV, tenNV;

    public HaoHut() {}

    // Getters & Setters
    public String getMaHH() { return maHH; }
    public void setMaHH(String maHH) { this.maHH = maHH; }

    public Date getNgayBaoCao() { return ngayBaoCao; }
    public void setNgayBaoCao(Date ngayBaoCao) { this.ngayBaoCao = ngayBaoCao; }

    public String getMaNL() { return maNL; }
    public void setMaNL(String maNL) { this.maNL = maNL; }

    public String getTenNL() { return tenNL; }
    public void setTenNL(String tenNL) { this.tenNL = tenNL; }

    public int getSoLuongHeThong() { return soLuongHeThong; }
    public void setSoLuongHeThong(int soLuongHeThong) { this.soLuongHeThong = soLuongHeThong; }

    public int getSoLuongThucTe() { return soLuongThucTe; }
    public void setSoLuongThucTe(int soLuongThucTe) { this.soLuongThucTe = soLuongThucTe; }

    public int getSoLuongHaoHut() { return soLuongHaoHut; }
    public void setSoLuongHaoHut(int soLuongHaoHut) { this.soLuongHaoHut = soLuongHaoHut; }

    public double getPhanTramHaoHut() { return phanTramHaoHut; }
    public void setPhanTramHaoHut(double phanTramHaoHut) { this.phanTramHaoHut = phanTramHaoHut; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }
}