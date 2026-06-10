// // package model;


// // public class NguyenLieu {

// //     // --- Các thuộc tính (Cột trong cơ sở dữ liệu) ---
// //     private String maNL;     // Mã nguyên liệu (Khóa chính - VD: NL001)
// //     private String tenNL;    // Tên nguyên liệu (VD: Tương cà, Gà phi lê)
// //     private String maloai;   // Mã loại nguyên liệu (Khóa ngoại liên kết bảng Loai)
// //     private int gianhap;     // Giá nhập vào trên một đơn vị tính
// //     private int soluong;     // Số lượng tồn kho hiện tại trong hệ thống
// //     private String donvi;    // Đơn vị tính (VD: Kg, Lít, Chai, Bao...)
// //     private String anh;      // Đường dẫn hoặc tên file ảnh của nguyên liệu



// //     public NguyenLieu() {
// //     }

// //     /**
// //      * Hàm khởi tạo đầy đủ tham số (Parameter Constructor).
// //      * Thường dùng khi tạo mới một đối tượng nguyên liệu từ Form giao diện để chuẩn bị thêm vào CSDL.
// //      *
// //      * @param maNL     
// //      * @param tenNL   
// //      * @param maloai  
// //      * @param gianhap  
// //      * @param soluong 
// //      * @param donvi   
// //      * @param anh      
// //      */
// //     public NguyenLieu(String maNL, String tenNL, String maloai, int gianhap, int soluong, String donvi, String anh) {
// //         this.maNL = maNL;
// //         this.tenNL = tenNL;
// //         this.maloai = maloai;
// //         this.gianhap = gianhap;
// //         this.soluong = soluong;
// //         this.donvi = donvi;
// //         this.anh = anh;
// //     }

// //     public String getMaNL() {
// //         return maNL;
// //     }

// //     public void setMaNL(String maNL) {
// //         this.maNL = maNL;
// //     }

// //     public String getTenNL() {
// //         return tenNL;
// //     }

// //     public void setTenNL(String tenNL) {
// //         this.tenNL = tenNL;
// //     }

// //     public String getMaloai() {
// //         return maloai;
// //     }

// //     public void setMaloai(String maloai) {
// //         this.maloai = maloai;
// //     }

// //     public int getGianhap() {
// //         return gianhap;
// //     }

// //     public void setGianhap(int gianhap) {
// //         this.gianhap = gianhap;
// //     }

// //     public int getSoluong() {
// //         return soluong;
// //     }

// //     public void setSoluong(int soluong) {
// //         this.soluong = soluong;
// //     }

// //     public String getDonvi() {
// //         return donvi;
// //     }

// //     public void setDonvi(String donvi) {
// //         this.donvi = donvi;
// //     }

// //     public String getAnh() {
// //         return anh;
// //     }

// //     public void setAnh(String anh) {
// //         this.anh = anh;
// //     }

    
// //     /**
// //      * Ghi đè phương thức toString() để hỗ trợ kiểm tra nhanh dữ liệu (In ra Console khi Debug).
// //      */
// //     @Override
// //     public String toString() {
// //         return "NguyenLieu{" +
// //                 "maNL='" + maNL + '\'' +
// //                 ", tenNL='" + tenNL + '\'' +
// //                 ", maloai='" + maloai + '\'' +
// //                 ", gianhap=" + gianhap +
// //                 ", soluong=" + soluong +
// //                 ", donvi='" + donvi + '\'' +
// //                 ", anh='" + anh + '\'' +
// //                 '}';
// //     }
// // }
// package model;

// public class NguyenLieu {

//     // --- Các thuộc tính chuẩn khớp 100% với Database và Giao diện ---
//     private String maNL;      // Mã nguyên liệu (Khóa chính)
//     private String tenNL;     // Tên nguyên liệu
//     private String donViTinh; // Đơn vị tính (Tương ứng txtNL_DVT)
//     private int soLuong;      // Số lượng tồn kho (Tương ứng txtNL_SL)
//     private String maKho;     // Mã kho (Khóa ngoại - Tương ứng txtNL_MaKho)

//     // Hàm khởi tạo không tham số
//     public NguyenLieu() {
//     }

//     // Hàm khởi tạo 5 tham số - Khớp hoàn toàn với các ô nhập liệu trên MainGUI
//     public NguyenLieu(String maNL, String tenNL, String donViTinh, int soLuong, String maKho) {
//         this.maNL = maNL;
//         this.tenNL = tenNL;
//         this.donViTinh = donViTinh;
//         this.soLuong = soLuong;
//         this.maKho = maKho;
//     }

//     // --- Các hàm Getter và Setter ---
//     public String getMaNL() {
//         return maNL;
//     }

//     public void setMaNL(String maNL) {
//         this.maNL = maNL;
//     }

//     public String getTenNL() {
//         return tenNL;
//     }

//     public void setTenNL(String tenNL) {
//         this.tenNL = tenNL;
//     }

//     public String getDonViTinh() {
//         return donViTinh;
//     }

//     public void setDonViTinh(String donViTinh) {
//         this.donViTinh = donViTinh;
//     }

//     public int getSoLuong() {
//         return soLuong;
//     }

//     public void setSoLuong(int soLuong) {
//         this.soLuong = soLuong;
//     }

//     public String getMaKho() {
//         return maKho;
//     }

//     public void setMaKho(String maKho) {
//         this.maKho = maKho;
//     }

//     // Ghi đè phương thức toString() để hỗ trợ in test console (Debug)
//     @Override
//     public String toString() {
//         return "NguyenLieu{" +
//                 "maNL='" + maNL + '\'' +
//                 ", tenNL='" + tenNL + '\'' +
//                 ", donViTinh='" + donViTinh + '\'' +
//                 ", soLuong=" + soLuong +
//                 ", maKho='" + maKho + '\'' +
//                 '}';
//     }
// }
package model;

public class NguyenLieu {

    // --- Các thuộc tính cũ của bạn ---
    private String maNL;     // Mã nguyên liệu (Khóa chính - VD: NL001)
    private String tenNL;    // Tên nguyên liệu (VD: Tương cà, Gà phi lê)
    private String maKho;   // Mã loại nguyên liệu (Khóa ngoại liên kết bảng Loai)
    private int gianhap;     // Giá nhập vào trên một đơn vị tính
    private int soluong;     // Số lượng tồn kho hiện tại trong hệ thống
    private String donvi;    // Đơn vị tính (VD: Kg, Lít, Chai, Bao...)

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
    }

    // 3. Hàm khởi tạo đầy đủ 6 tham số (không có ảnh)
    public NguyenLieu(String maNL, String tenNL, String maKho, int gianhap, int soluong, String donvi) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.maKho = maKho;
        this.gianhap = gianhap;
        this.soluong = soluong;
        this.donvi = donvi;
    }

    // --- Hệ thống Getter và Setter ---
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

    public String getMaKho() {
        return maKho;
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

    @Override
    public String toString() {
        return "NguyenLieu{" +
                "maNL='" + maNL + '\'' +
                ", tenNL='" + tenNL + '\'' +
                ", maKho='" + maKho + '\'' +
                ", gianhap=" + gianhap +
                ", soluong=" + soluong +
                ", donvi='" + donvi + '\'' +
                '}';
    }
}