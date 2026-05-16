package model;

import java.util.Date; // Thư viện xử lý ngày tháng chuẩn của Java

public class TonKho {

    
    private String maNL;
    private String tenNL;
    private String donViTinh;
    private int tonKhoHienTai;

    private String maKho;
    private String tenKho;

    private String maLo;
    private Date ngayNhapLo;   
    private int soLuongNhapLo;

    private Date hanSuDung;    
    private String trangThaiHan;

    // 2. Hàm khởi tạo không tham số 
    public TonKho() {
    }

    // 3. Hàm khởi tạo đầy đủ tham số
    public TonKho(String maNL, String tenNL, String donViTinh, int tonKhoHienTai, 
                  String maKho, String tenKho, String maLo, Date ngayNhapLo, 
                  int soLuongNhapLo, Date hanSuDung, String trangThaiHan) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.donViTinh = donViTinh;
        this.tonKhoHienTai = tonKhoHienTai;
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.maLo = maLo;
        this.ngayNhapLo = ngayNhapLo;
        this.soLuongNhapLo = soLuongNhapLo;
        this.hanSuDung = hanSuDung;
        this.trangThaiHan = trangThaiHan;
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

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public int getTonKhoHienTai() {
        return tonKhoHienTai;
    }

    public void setTonKhoHienTai(int tonKhoHienTai) {
        this.tonKhoHienTai = tonKhoHienTai;
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

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }

    public Date getNgayNhapLo() {
        return ngayNhapLo;
    }

    public void setNgayNhapLo(Date ngayNhapLo) {
        this.ngayNhapLo = ngayNhapLo;
    }

    public int getSoLuongNhapLo() {
        return soLuongNhapLo;
    }

    public void setSoLuongNhapLo(int soLuongNhapLo) {
        this.soLuongNhapLo = soLuongNhapLo;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public String getTrangThaiHan() {
        return trangThaiHan;
    }

    public void setTrangThaiHan(String trangThaiHan) {
        this.trangThaiHan = trangThaiHan;
    }

    // 5. Hàm bổ trợ đặc biệt: Chuyển đối tượng thành mảng Object để nạp vào JTable
    public Object[] toRowObject(int stt) {
        return new Object[]{
            stt,                 // Số thứ tự
            maNL,                // Mã nguyên liệu
            tenNL,               // Tên nguyên liệu
            donViTinh,           // Đơn vị tính
            tonKhoHienTai,       // Tồn kho hiện tại (tổng)
            tenKho,              // Tên kho chứa
            maLo,                // Mã lô (Mã CTPN)
            ngayNhapLo,          // Ngày nhập lô hàng
            soLuongNhapLo,       // Số lượng lúc nhập
            hanSuDung,           // Hạn sử dụng lô
            trangThaiHan         // Trạng thái hạn (Còn hạn/Sắp hết hạn/Hết hạn)
        };
    }

    @Override
    public String toString() {
        return "TonKho [maNL=" + maNL + ", tenNL=" + tenNL + ", donViTinh=" + donViTinh + ", tonKhoHienTai="
                + tonKhoHienTai + ", maKho=" + maKho + ", tenKho=" + tenKho + ", maLo=" + maLo + ", ngayNhapLo="
                + ngayNhapLo + ", soLuongNhapLo=" + soLuongNhapLo + ", hanSuDung=" + hanSuDung + ", trangThaiHan="
                + trangThaiHan + "]";
    }
    
}