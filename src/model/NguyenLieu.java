package model;

public class NguyenLieu {

    // --- Các thuộc tính cũ của bạn ---
    private String maNL;     // Mã nguyên liệu (Khóa chính - VD: NL001)
    private String tenNL;    // Tên nguyên liệu (VD: Tương cà, Gà phi lê)
    private String maKho;   // Mã loại nguyên liệu (Khóa ngoại liên kết bảng Loai)
    private int gianhap;     // Giá nhập vào trên một đơn vị tính
    private int soluong;     // Số lượng tồn kho hiện tại trong hệ thống
    private String donvi;    // Đơn vị tính (VD: Kg, Lít, Chai, Bao...)
    private int thanhtien;   // Tổng thành tiền tính dựa trên các lô nhập kho thực tế (FIFO)

    // 1. Hàm khởi tạo không tham số
    public NguyenLieu() {
    }

    // 2. KHỞI TẠO ĐẶC BIỆT (5 THAM SỐ): Sửa lỗi đỏ lòm cho file MainGUI.java hiện tại
    // Các trường không có trên giao diện như maloai, gianhap sẽ được gán mặc định hoặc để null
    public NguyenLieu(String maNL, String tenNL, String donvi, int soluong, String maKho) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.donvi = donvi;
        this.soluong = soluong;
        this.maKho = maKho; // Tạm thời map mã kho vào biến maKho nếu bạn muốn tận dụng trường này
        this.gianhap = 0;    // Giá nhập mặc định bằng 0
        this.thanhtien = 0;
    }

    // 3. Hàm khởi tạo đầy đủ 6 tham số (không có ảnh)
    public NguyenLieu(String maNL, String tenNL, String maKho, int gianhap, int soluong, String donvi) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.maKho = maKho;
        this.gianhap = gianhap;
        this.soluong = soluong;
        this.donvi = donvi;
        this.thanhtien = 0;
    }
   
    public String getMaNL() {
        return maNL;
    }

    public String getTenNL() {
        return tenNL;
    }

    public String getMaKho() {
        return maKho;
    }

 
    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public void setTenNL(String tenNL) {
        this.tenNL = tenNL;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public int getGianhap() {
        return gianhap;
    }

    public void setGianhap(int gianhap) {
        this.gianhap = gianhap;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public int getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }

    @Override
    public String toString() {
        return "NguyenLieu{" +
                "maNL='" + maNL + '\'' +
                ", tenNL='" + tenNL + '\'' +
                ", maKho='" + maKho + '\'' +
                ", gianhap=" + gianhap +
                ", soluong=" + soluong +
                ", donvi='" + donvi + '\'' +
                ", thanhtien=" + thanhtien +
                '}';
    }
}