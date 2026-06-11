package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CTPhieuXuat;

public class CTPhieuXuatDAO {

    public void addChiTietPhieuXuat(Connection conn, CTPhieuXuat ct) throws SQLException {
      
        String sql = "INSERT INTO ChiTietPhieuXuat (MaCTPX, MaNL, SoLuong, DonGia, MaPX) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
         
            pst.setString(1, ct.getMaCTPX()); 
            pst.setString(2, ct.getMaNL()); 
            pst.setInt(3, ct.getSoLuong());
            pst.setInt(4, ct.getDonGia());
            pst.setString(5, ct.getMaPX());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi chèn ChiTietPhieuXuat cho MaNL=" + ct.getMaNL() + ": " + e.getMessage());
            throw e; 
        }
    }

    public List<CTPhieuXuat> getChiTietPhieuXuatByMaPX(Connection conn, String maPX) throws SQLException {
        List<CTPhieuXuat> list = new ArrayList<>();
        String sql = "SELECT MaCTPX, MaPX, MaNL, SoLuong, DonGia FROM ChiTietPhieuXuat WHERE MaPX = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maPX);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    CTPhieuXuat ct = new CTPhieuXuat();
                    ct.setMaCTPX(rs.getString("MaCTPX"));
                    ct.setMaPX(rs.getString("MaPX"));
                    ct.setMaNL(rs.getString("MaNL"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getInt("DonGia"));
                    ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());
                    list.add(ct);
                }
            }
        }
        return list;
    }

    public void deleteChiTietPhieuXuatByMaPX(Connection conn, String maPX) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuXuat WHERE MaPX = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maPX);
            pst.executeUpdate();
        }
    }
}