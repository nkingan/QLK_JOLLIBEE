// package dao;

// import util.DBConnection;
// import java.sql.*;

// public class TaiKhoanDAO {

//     public boolean login(String user, String pass) {
//         String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap=? AND MatKhau=?";

//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement(sql)) {

//             ps.setString(1, user);
//             ps.setString(2, pass);

//             ResultSet rs = ps.executeQuery();
//             return rs.next();

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return false;
//     }
// }
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.TaiKhoan;
import util.DBConnection;


public class TaiKhoanDAO {

    /**
     * Xác thực tài khoản đăng nhập từ giao diện LoginForm.
     * * @param username Tên đăng nhập người dùng điền.
     * @param password Mật khẩu người dùng điền.
     * @return Đối tượng TaiKhoan nếu khớp thông tin, ngược lại trả về null.
     */
    public TaiKhoan kiemTraDangNhap(String username, String password) {
        TaiKhoan tk = null;
        
        String sql = "SELECT MaTK, TenDangNhap, MatKhau, Quyen, MaNV FROM TaiKhoan WHERE TenDangNhap = ? AND MatKhau = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoan();
                    tk.setMaTK(rs.getString("MaTK"));
                    tk.setTenDangNhap(rs.getString("TenDangNhap"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setQuyen(rs.getString("Quyen")); 
                    tk.setMaNV(rs.getString("MaNV"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi kiểm tra đăng nhập hệ thống Jollibee:");
            e.printStackTrace();
        }
        return tk;
    }

    /**
     * Đổi mật khẩu cho một tài khoản cụ thể.
     */
    public boolean doiMatKhau(String username, String newPassword) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TenDangNhap = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi thực hiện đổi mật khẩu tài khoản:");
            e.printStackTrace();
            return false;
        }
    }
}