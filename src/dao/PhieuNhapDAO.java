package dao;

import util.DBConnection;
import model.PhieuNhap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    // 1. Lấy toàn bộ danh sách phiếu nhập từ cơ sở dữ liệu
    public List<PhieuNhap> getAll() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap ORDER BY NgayNhap DESC"; 

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPN(rs.getString("MaPN"));
                // Chuyển đổi Timestamp/Date từ SQL Server sang java.util.Date trong Model
                pn.setNgayNhap(rs.getTimestamp("NgayNhap")); 
                pn.setMaNV(rs.getString("MaNV"));
                pn.setMaNCC(rs.getString("MaNCC"));
                pn.setTongTien(rs.getDouble("TongTien"));
                
                list.add(pn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm một phiếu nhập mới
    public boolean insert(PhieuNhap pn) {
        String sql = "INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNV, MaNCC, TongTien) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, pn.getMaPN());
            // Ép kiểu từ java.util.Date sang java.sql.Timestamp để lưu đầy đủ cả ngày lẫn giờ
            ps.setTimestamp(2, new java.sql.Timestamp(pn.getNgayNhap().getTime()));
            ps.setString(3, pn.getMaNV());
            ps.setString(4, pn.getMaNCC());
            ps.setDouble(5, pn.getTongTien());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}