package dao;

import model.PhieuXuat;
import util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("all")
public class PhieuXuatDAO {

    // 1. TỰ ĐỘNG SINH MÃ
    public synchronized String generateNextMaPX() {
        String sql = "SELECT TOP 1 MaPX FROM PhieuXuat ORDER BY MaPX DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("MaPX");
                int number = Integer.parseInt(lastId.substring(2));
                return String.format("PX%04d", number + 1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "PX0001";
    }

    // 2. THÊM PHIẾU XUẤT (Dùng cho Transaction)
    public boolean insert(Connection conn, PhieuXuat px) throws SQLException {
        String sql = "INSERT INTO PhieuXuat (MaPX, NgayXuat, MaNV, TongTien) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, px.getMaPX());
            ps.setDate(2, px.getNgayXuat());
            ps.setString(3, px.getMaNV());
            ps.setBigDecimal(4, px.getTongTien() != null ? px.getTongTien() : BigDecimal.ZERO);
            return ps.executeUpdate() > 0;
        }
    }

    // 3. THÊM PHIẾU XUẤT ĐƠN LẺ
    public boolean insert(PhieuXuat px) {
        try (Connection conn = DBConnection.getConnection()) {
            return insert(conn, px);
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. LẤY TẤT CẢ
    public List<PhieuXuat> getAll() {
        List<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM VW_PhieuXuat ORDER BY NgayXuat DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 5. TÌM THEO MÃ
    public PhieuXuat findById(String maPX) {
        String sql = "SELECT * FROM VW_PhieuXuat WHERE MaPX = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 6. CẬP NHẬT
    public boolean update(PhieuXuat px) {
        String sql = "UPDATE PhieuXuat SET NgayXuat = ?, MaNV = ? WHERE MaPX = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, px.getNgayXuat());
            ps.setString(2, px.getMaNV());
            ps.setString(3, px.getMaPX());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 7. XÓA
    public boolean delete(String maPX) throws SQLException {
        String sql = "DELETE FROM PhieuXuat WHERE MaPX = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            return ps.executeUpdate() > 0;
        }
    }

    // 8. KIỂM TRA TỒN TẠI
    public boolean exists(String maPX) {
        String sql = "SELECT COUNT(*) FROM PhieuXuat WHERE MaPX = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 9. TÌM KIẾM
    public List<PhieuXuat> search(String keyword) {
        List<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM VW_PhieuXuat WHERE MaPX LIKE ? OR TenNV LIKE ? ORDER BY NgayXuat DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 10. LẤY THEO NHÂN VIÊN
    public List<PhieuXuat> getByNhanVien(String maNV) {
        List<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM VW_PhieuXuat WHERE MaNV = ? ORDER BY NgayXuat DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 11. LẤY THEO NGÀY
    public List<PhieuXuat> getByDateRange(Date from, Date to) {
        List<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM VW_PhieuXuat WHERE NgayXuat BETWEEN ? AND ? ORDER BY NgayXuat DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, from);
            ps.setDate(2, to);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 12. ĐẾM SỐ PHIẾU
    public int count() {
        String sql = "SELECT COUNT(*) FROM PhieuXuat";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // 13. TỔNG TIỀN
    public BigDecimal getTotalAmount() {
        String sql = "SELECT SUM(TongTien) FROM PhieuXuat";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } catch (SQLException e) { e.printStackTrace(); }
        return BigDecimal.ZERO;
    }

    // 14. MAP DỮ LIỆU
    private PhieuXuat mapResultSet(ResultSet rs) throws SQLException {
        PhieuXuat px = new PhieuXuat();
        px.setMaPX(rs.getString("MaPX"));
        px.setNgayXuat(rs.getDate("NgayXuat"));
        px.setMaNV(rs.getString("MaNV"));
        px.setTongTien(rs.getBigDecimal("TongTien"));
        try { px.setTenNV(rs.getString("TenNV")); } catch (Exception e) {}
        return px;
    }
}