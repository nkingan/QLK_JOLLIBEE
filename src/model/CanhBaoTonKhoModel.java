package model;

import java.sql.Timestamp;

public class CanhBaoTonKhoModel {
    private int maCanhBao;
    private String maNL;
    private String tenNL;
    private int soLuongHienTai;
    private int nguongCanhBao;
    private Timestamp thoiGianGhiNhan;
    private String trangThai;

    public CanhBaoTonKhoModel() {
    }

    public CanhBaoTonKhoModel(int maCanhBao, String maNL, String tenNL, int soLuongHienTai, int nguongCanhBao, Timestamp thoiGianGhiNhan, String trangThai) {
        this.maCanhBao = maCanhBao;
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.soLuongHienTai = soLuongHienTai;
        this.nguongCanhBao = nguongCanhBao;
        this.thoiGianGhiNhan = thoiGianGhiNhan;
        this.trangThai = trangThai;
    }

    public int getMaCanhBao() {
        return maCanhBao;
    }

    public void setMaCanhBao(int maCanhBao) {
        this.maCanhBao = maCanhBao;
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

    public int getSoLuongHienTai() {
        return soLuongHienTai;
    }

    public void setSoLuongHienTai(int soLuongHienTai) {
        this.soLuongHienTai = soLuongHienTai;
    }

    public int getNguongCanhBao() {
        return nguongCanhBao;
    }

    public void setNguongCanhBao(int nguongCanhBao) {
        this.nguongCanhBao = nguongCanhBao;
    }

    public Timestamp getThoiGianGhiNhan() {
        return thoiGianGhiNhan;
    }

    public void setThoiGianGhiNhan(Timestamp thoiGianGhiNhan) {
        this.thoiGianGhiNhan = thoiGianGhiNhan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
