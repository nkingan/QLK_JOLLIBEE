package dao;

import util.DBConnection;
import model.ChiTietPhieuNhap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhapDAO {

    // 1. Tìm các chi tiết nguyên liệu thuộc về một Phiếu Nhập cụ thể nào đó
    public List<ChiTietPhieuNhap> getByMaPN(String maPN) {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
                    ctpn.setMaCTPN(rs.getString("MaCTPN"));
                    ctpn.setSoLuong(rs.getInt("SoLuong"));
                    ctpn.setDonGia(rs.getDouble("DonGia"));
                    ctpn.setMaNL(rs.getString("MaNL"));
                    ctpn.setMaPN(rs.getString("MaPN"));
                    ctpn.setHanSuDung(rs.getDate("HanSuDung")); 
                    
                    list.add(ctpn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới một dòng chi tiết vật tư nhập kho
    public boolean insert(ChiTietPhieuNhap ctpn) {
        String sql = "INSERT INTO ChiTietPhieuNhap (MaCTPN, SoLuong, DonGia, MaNL, MaPN, HanSuDung) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ctpn.getMaCTPN());
            ps.setInt(2, ctpn.getSoLuong());
            ps.setDouble(3, ctpn.getDonGia());
            ps.setString(4, ctpn.getMaNL());
            ps.setString(5, ctpn.getMaPN());
            
            // Xử lý Hạn sử dụng: Kiểm tra nếu có hạn sử dụng thì lưu, không thì để NULL trong DB
            if (ctpn.getHanSuDung() != null) {
                ps.setDate(6, new java.sql.Date(ctpn.getHanSuDung().getTime()));
            } else {
                ps.setNull(6, Types.DATE);
            }
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}