package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.HaoHut;
import util.DBConnection;

public class HaoHutDAO {

    public synchronized String generateNextMaHH() {
        String latestCode = null;
        String sql = "SELECT TOP 1 MaHH FROM HaoHut ORDER BY MaHH DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                latestCode = rs.getString("MaHH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int number = 1;
        if (latestCode != null && latestCode.startsWith("HH")) {
            try {
                number = Integer.parseInt(latestCode.substring(2)) + 1;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("HH%03d", number);
    }

    public boolean addHaoHut(HaoHut hh) {
        String sql = "INSERT INTO HaoHut (MaHH, MaNL, SoLuongHeThong, SoLuongThucTe, SoLuongHaoHut, PhanTramHaoHut, LyDo, NgayGhiNhan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, hh.getMaHH());
            pst.setString(2, hh.getMaNL());
            pst.setInt(3, hh.getSoLuongHeThong());
            pst.setInt(4, hh.getSoLuongThucTe());
            pst.setInt(5, hh.getSoLuongHaoHut());
            pst.setDouble(6, hh.getPhanTramHaoHut());
            pst.setString(7, hh.getLyDo());
            pst.setDate(8, new java.sql.Date(hh.getNgayGhiNhan().getTime()));
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HaoHut> getAllHaoHut() {
        List<HaoHut> list = new ArrayList<>();
        String sql = "SELECT hh.MaHH, hh.MaNL, nl.TenNL, hh.SoLuongHeThong, hh.SoLuongThucTe, hh.SoLuongHaoHut, hh.PhanTramHaoHut, hh.LyDo, hh.NgayGhiNhan " +
                     "FROM HaoHut hh JOIN NguyenLieu nl ON hh.MaNL = nl.MaNL ORDER BY hh.MaHH DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                HaoHut hh = new HaoHut();
                hh.setMaHH(rs.getString("MaHH"));
                hh.setMaNL(rs.getString("MaNL"));
                hh.setTenNL(rs.getString("TenNL"));
                hh.setSoLuongHeThong(rs.getInt("SoLuongHeThong"));
                hh.setSoLuongThucTe(rs.getInt("SoLuongThucTe"));
                hh.setSoLuongHaoHut(rs.getInt("SoLuongHaoHut"));
                hh.setPhanTramHaoHut(rs.getDouble("PhanTramHaoHut"));
                hh.setLyDo(rs.getString("LyDo"));
                hh.setNgayGhiNhan(rs.getDate("NgayGhiNhan"));
                
                list.add(hh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
