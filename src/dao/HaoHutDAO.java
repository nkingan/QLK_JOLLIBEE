package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.HaoHut;
import util.DBConnection;

public class HaoHutDAO {

    // Lấy toàn bộ danh sách hao hụt (JOIN để lấy TenNL, TenNV)
    public List<HaoHut> getAllHaoHut() {
        List<HaoHut> list = new ArrayList<>();
        String sql = "SELECT hh.MaHH, hh.NgayBaoCao, hh.MaNL, nl.TenNL, " +
                     "hh.SoLuongHeThong, hh.SoLuongThucTe, hh.SoLuongHaoHut, " +
                     "hh.PhanTramHaoHut, hh.LyDo, hh.MaNV, nv.TenNV " +
                     "FROM HaoHut hh " +
                     "JOIN NguyenLieu nl ON hh.MaNL = nl.MaNL " +
                     "JOIN NhanVien nv  ON hh.MaNV = nv.MaNV " +
                     "ORDER BY hh.NgayBaoCao DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                HaoHut hh = mapResultSet(rs);
                list.add(hh);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getAllHaoHut: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Lấy theo mã hao hụt
    public HaoHut getHaoHutById(String maHH) {
        String sql = "SELECT hh.MaHH, hh.NgayBaoCao, hh.MaNL, nl.TenNL, " +
                     "hh.SoLuongHeThong, hh.SoLuongThucTe, hh.SoLuongHaoHut, " +
                     "hh.PhanTramHaoHut, hh.LyDo, hh.MaNV, nv.TenNV " +
                     "FROM HaoHut hh " +
                     "JOIN NguyenLieu nl ON hh.MaNL = nl.MaNL " +
                     "JOIN NhanVien nv  ON hh.MaNV = nv.MaNV " +
                     "WHERE hh.MaHH = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHH);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getHaoHutById: " + e.getMessage());
        }
        return null;
    }

    // Tìm kiếm theo tên nguyên liệu
    public List<HaoHut> searchByTenNL(String keyword) {
        List<HaoHut> list = new ArrayList<>();
        String sql = "SELECT hh.MaHH, hh.NgayBaoCao, hh.MaNL, nl.TenNL, " +
                     "hh.SoLuongHeThong, hh.SoLuongThucTe, hh.SoLuongHaoHut, " +
                     "hh.PhanTramHaoHut, hh.LyDo, hh.MaNV, nv.TenNV " +
                     "FROM HaoHut hh " +
                     "JOIN NguyenLieu nl ON hh.MaNL = nl.MaNL " +
                     "JOIN NhanVien nv  ON hh.MaNV = nv.MaNV " +
                     "WHERE nl.TenNL LIKE ? " +
                     "ORDER BY hh.NgayBaoCao DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi searchByTenNL: " + e.getMessage());
        }
        return list;
    }

    // Thêm mới bản ghi hao hụt
    public boolean addHaoHut(HaoHut hh) {
        String sql = "INSERT INTO HaoHut " +
                     "(MaHH, NgayBaoCao, MaNL, SoLuongHeThong, SoLuongThucTe, " +
                     "SoLuongHaoHut, PhanTramHaoHut, LyDo, MaNV) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hh.getMaHH());
            pstmt.setDate(2, new java.sql.Date(hh.getNgayBaoCao().getTime()));
            pstmt.setString(3, hh.getMaNL());
            pstmt.setInt(4, hh.getSoLuongHeThong());
            pstmt.setInt(5, hh.getSoLuongThucTe());
            pstmt.setInt(6, hh.getSoLuongHaoHut());
            pstmt.setDouble(7, hh.getPhanTramHaoHut());
            pstmt.setString(8, hh.getLyDo());
            pstmt.setString(9, hh.getMaNV());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi addHaoHut: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật bản ghi hao hụt
    public boolean updateHaoHut(HaoHut hh) {
        String sql = "UPDATE HaoHut SET NgayBaoCao=?, MaNL=?, SoLuongHeThong=?, " +
                     "SoLuongThucTe=?, SoLuongHaoHut=?, PhanTramHaoHut=?, " +
                     "LyDo=?, MaNV=? WHERE MaHH=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new java.sql.Date(hh.getNgayBaoCao().getTime()));
            pstmt.setString(2, hh.getMaNL());
            pstmt.setInt(3, hh.getSoLuongHeThong());
            pstmt.setInt(4, hh.getSoLuongThucTe());
            pstmt.setInt(5, hh.getSoLuongHaoHut());
            pstmt.setDouble(6, hh.getPhanTramHaoHut());
            pstmt.setString(7, hh.getLyDo());
            pstmt.setString(8, hh.getMaNV());
            pstmt.setString(9, hh.getMaHH());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi updateHaoHut: " + e.getMessage());
            return false;
        }
    }

    // Xóa bản ghi hao hụt
    public boolean deleteHaoHut(String maHH) {
        String sql = "DELETE FROM HaoHut WHERE MaHH = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHH);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi deleteHaoHut: " + e.getMessage());
            return false;
        }
    }

    // Lấy mã HH cuối để tự sinh mã mới
    public String getLastMaHH() {
        String sql = "SELECT TOP 1 MaHH FROM HaoHut ORDER BY MaHH DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getString("MaHH");
        } catch (SQLException e) {
            System.err.println("Lỗi getLastMaHH: " + e.getMessage());
        }
        return null;
    }

    // Helper: map ResultSet → HaoHut object
    private HaoHut mapResultSet(ResultSet rs) throws SQLException {
        HaoHut hh = new HaoHut();
        hh.setMaHH(rs.getString("MaHH"));
        hh.setNgayBaoCao(rs.getDate("NgayBaoCao"));
        hh.setMaNL(rs.getString("MaNL"));
        hh.setTenNL(rs.getString("TenNL"));
        hh.setSoLuongHeThong(rs.getInt("SoLuongHeThong"));
        hh.setSoLuongThucTe(rs.getInt("SoLuongThucTe"));
        hh.setSoLuongHaoHut(rs.getInt("SoLuongHaoHut"));
        hh.setPhanTramHaoHut(rs.getDouble("PhanTramHaoHut"));
        hh.setLyDo(rs.getString("LyDo"));
        hh.setMaNV(rs.getString("MaNV"));
        hh.setTenNV(rs.getString("TenNV"));
        return hh;
    }
}
