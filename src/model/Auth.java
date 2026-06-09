package model;

public class Auth {
    // Lưu trữ thông tin nhân viên đang đăng nhập
    public static NhanVien user = null;

    // Kiểm tra xem đã đăng nhập chưa
    public static boolean isLogin() {
        return user != null;
    }

    // Đăng xuất (xóa phiên)
    public static void clear() {
        user = null;
    }
}
