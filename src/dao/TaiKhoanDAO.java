package dao;

import util.DBConnection;
import java.sql.*;

public class TaiKhoanDAO {

    public boolean login(String user, String pass) {
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap=? AND MatKhau=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}