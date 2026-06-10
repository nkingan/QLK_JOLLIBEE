package ui;

import dao.TaiKhoanDAO;
import model.TaiKhoan;

public class TestLogin {
    public static void main(String[] args) {
        // 1. Khởi tạo đối tượng xử lý SQL dữ liệu tài khoản
        TaiKhoanDAO loginDAO = new TaiKhoanDAO();
        
        // 🌟 CỦA ĐÁNG TỘI: Đảm bảo tài khoản này ĐÃ CÓ THẬT trong bảng TaiKhoan dưới SQL Server của bạn
        String userTest = "admin"; 
        String passTest = "123";
        
        System.out.println("================================================");
        System.out.println("🐝 HỆ THỐNG KHO JOLLIBEE - KIỂM THỬ ĐĂNG NHẬP 🐝");
        System.out.println("================================================");
        System.out.println("=> Đang kết nối SQL Server và xác thực tài khoản: [" + userTest + "]");
        
        // 🌟 ĐÃ FIX: Đổi từ checkLogin() thành kiemTraDangNhap() khớp hoàn toàn với TaiKhoanDAO của bạn
        TaiKhoan result = loginDAO.kiemTraDangNhap(userTest, passTest);
        
        // 2. Kiểm tra kết quả trả về từ database
        if (result != null) {
            System.out.println("\n🎉 ĐĂNG NHẬP THÀNH CÔNG RỒI BẠN ƠI!");
            System.out.println("------------------------------------------------");
            System.out.println("📌 Mã tài khoản  : " + result.getMaTK());
            System.out.println("📌 Tên đăng nhập : " + result.getTenDangNhap());
            System.out.println("📌 Quyền hạn     : " + result.getQuyen());
            System.out.println("📌 Mã nhân viên  : " + result.getMaNV());
            System.out.println("------------------------------------------------");
            System.out.println("🚀 Trạng thái: Dữ liệu liên thông SQL an toàn.");

            javax.swing.SwingUtilities.invokeLater(() -> {
                // Khởi tạo khung giao diện chính và truyền đối tượng tài khoản vừa đăng nhập vào
                MainApplicationFrame mainFrame = new MainApplicationFrame(result);
                mainFrame.setVisible(true); // Lệnh bắt buộc để cửa sổ hiện lên
            });
        } else {
            System.out.println("\n❌ ĐĂNG NHẬP THẤT BẠI!");
            System.out.println("------------------------------------------------");
            System.out.println("⚠️ Lý do có thể xảy ra:");
            System.out.println("  1. Sai tên đăng nhập hoặc mật khẩu.");
            System.out.println("  2. Tài khoản '" + userTest + "' chưa có trong bảng TaiKhoan SQL Server.");
            System.out.println("  3. Chưa bật SQL Server hoặc kết nối trong DBConnection lỗi.");
        }
        System.out.println("================================================");
    }
}