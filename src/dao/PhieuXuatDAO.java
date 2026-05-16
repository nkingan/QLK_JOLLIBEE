package dao;

import model.PhieuXuat;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO xử lý bảng PhieuXuat.
 *
 * CREATE TABLE PhieuXuat (
 *     MaPX      VARCHAR(20)   PRIMARY KEY,
 *     NgayXuat  DATE          DEFAULT GETDATE(),
 *     MaNV      VARCHAR(20),
 *     TongTien  DECIMAL(18,2) DEFAULT 0 CHECK (TongTien >= 0)
 * )
 *
 * Lưu ý: TongTien được trigger TRG_XuatKho tự cập nhật sau mỗi thao tác
 * trên ChiTietPhieuXuat — không cần truyền giá trị thực khi INSERT.
 */
public class PhieuXuatDAO {

    // =========================================================
    // 1. TỰ ĐỘNG SINH MÃ PHIẾU XUẤT
    // =========================================================

    /**
     * Sinh mã PX tiếp theo dạng PX0001, PX0002, ...
     * Tương thích ngược với format cũ PX01, PX02 (dữ liệu mẫu).
     * Dùng synchronized để tránh trùng mã trong môi trường đơn JVM.
     * Khuyến nghị: thêm UNIQUE constraint ở DB để đảm bảo toàn vẹn.
     */
    public synchronized String generateNextMaPX() {

        // ORDER BY LEN DESC rồi MaPX DESC → lấy số lớn nhất
        // dù DB lẫn format cũ (PX01) và mới (PX0001)
        String sql =
                "SELECT TOP 1 MaPX " +
                "FROM PhieuXuat " +
                "ORDER BY LEN(MaPX) DESC, MaPX DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                String lastId = rs.getString("MaPX");
                if (lastId != null && lastId.toUpperCase().startsWith("PX")) {
                    // parseInt tự xử lý leading zeros: "01"→1, "0001"→1
                    int number = Integer.parseInt(lastId.substring(2));
                    return String.format("PX%04d", number + 1);
                }
            }
        } catch (Exception e) {
            System.err.println("[PhieuXuatDAO] Lỗi generateNextMaPX(): " + e.getMessage());
            e.printStackTrace();
        }

        return "PX0001";
    }

    // =========================================================
    // 2. THÊM PHIẾU XUẤT (DÙNG TRANSACTION — caller quản lý conn)
    // =========================================================

    /**
     * INSERT phiếu xuất trong một transaction do caller kiểm soát.
     * Chỉ insert MaPX, NgayXuat, MaNV — TongTien để DB tự DEFAULT 0,
     * trigger sẽ cập nhật sau khi chi tiết được insert.
     */
    public boolean insert(Connection conn, PhieuXuat px) throws SQLException {

        String sql =
                "INSERT INTO PhieuXuat (MaPX, NgayXuat, MaNV) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, px.getMaPX());

            // NgayXuat bắt buộc — không cho phép null
            if (px.getNgayXuat() == null)
                throw new IllegalArgumentException("NgayXuat không được null");
            ps.setDate(2, px.getNgayXuat()); // java.sql.Date trực tiếp

            ps.setString(3, px.getMaNV());

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // 3. THÊM PHIẾU XUẤT ĐƠN LẺ (tự quản lý connection)
    // =========================================================

    public boolean insert(PhieuXuat px) {
        try (Connection conn = DBConnection.getConnection()) {
            return insert(conn, px);
        } catch (Exception e) {
            System.err.println("[PhieuXuatDAO] Lỗi insert(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // =========================================================
    // 4. LẤY TOÀN BỘ DANH SÁCH PHIẾU XUẤT
    // =========================================================

    public List<PhieuXuat> getAll() {

        List<PhieuXuat> list = new ArrayList<>();

        // Dùng VW_PhieuXuat để lấy thêm TenNV mà không cần JOIN thủ công
        String sql =
                "SELECT MaPX, NgayXuat, MaNV, TenNV, TongTien " +
                "FROM VW_PhieuXuat " +
                "ORDER BY NgayXuat DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi getAll(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // 5. TÌM PHIẾU XUẤT THEO MÃ
    // =========================================================

    public PhieuXuat findById(String maPX) {

        String sql =
                "SELECT MaPX, NgayXuat, MaNV, TenNV, TongTien " +
                "FROM VW_PhieuXuat " +
                "WHERE MaPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maPX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi findById(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // 6. CẬP NHẬT PHIẾU XUẤT
    // =========================================================

    /**
     * Chỉ cho phép cập nhật NgayXuat và MaNV.
     * TongTien do trigger quản lý — không update thủ công.
     */
    public boolean update(PhieuXuat px) {

        // NgayXuat bắt buộc — phiếu xuất phải có ngày
        if (px.getNgayXuat() == null)
            throw new IllegalArgumentException("NgayXuat không được null khi update");

        String sql =
                "UPDATE PhieuXuat " +
                "SET NgayXuat = ?, MaNV = ? " +
                "WHERE MaPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, px.getNgayXuat());
            ps.setString(2, px.getMaNV());
            ps.setString(3, px.getMaPX());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi update(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // 7. XÓA PHIẾU XUẤT
    // =========================================================

    public boolean delete(String maPX) {

        String sql = "DELETE FROM PhieuXuat WHERE MaPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maPX);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi delete(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // 8. KIỂM TRA MÃ TỒN TẠI
    // =========================================================

    public boolean exists(String maPX) {

        String sql = "SELECT COUNT(*) FROM PhieuXuat WHERE MaPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maPX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi exists(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // 9. TÌM KIẾM PHIẾU XUẤT (theo mã, tên NV)
    // =========================================================

    /**
     * Tìm kiếm qua VW_PhieuXuat để hỗ trợ tìm theo TenNV.
     */
    public List<PhieuXuat> search(String keyword) {

        List<PhieuXuat> list = new ArrayList<>();

        String sql =
                "SELECT MaPX, NgayXuat, MaNV, TenNV, TongTien " +
                "FROM VW_PhieuXuat " +
                "WHERE MaPX LIKE ? OR TenNV LIKE ? " +
                "ORDER BY NgayXuat DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi search(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // 10. LẤY PHIẾU XUẤT THEO NHÂN VIÊN
    // =========================================================

    public List<PhieuXuat> getByNhanVien(String maNV) {

        List<PhieuXuat> list = new ArrayList<>();

        String sql =
                "SELECT MaPX, NgayXuat, MaNV, TenNV, TongTien " +
                "FROM VW_PhieuXuat " +
                "WHERE MaNV = ? " +
                "ORDER BY NgayXuat DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi getByNhanVien(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // 11. LẤY PHIẾU XUẤT THEO KHOẢNG NGÀY
    // =========================================================

    public List<PhieuXuat> getByDateRange(Date from, Date to) {

        List<PhieuXuat> list = new ArrayList<>();

        String sql =
                "SELECT MaPX, NgayXuat, MaNV, TenNV, TongTien " +
                "FROM VW_PhieuXuat " +
                "WHERE NgayXuat BETWEEN ? AND ? " +
                "ORDER BY NgayXuat DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, from);
            ps.setDate(2, to);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi getByDateRange(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // 12. ĐẾM TỔNG SỐ PHIẾU XUẤT
    // =========================================================

    public int count() {

        String sql = "SELECT COUNT(*) FROM PhieuXuat";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi count(): " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    // =========================================================
    // 13. TỔNG TIỀN TOÀN BỘ PHIẾU XUẤT
    // =========================================================

    public BigDecimal getTotalAmount() {

        String sql = "SELECT SUM(TongTien) FROM PhieuXuat";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("[PhieuXuatDAO] Lỗi getTotalAmount(): " + e.getMessage());
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    // =========================================================
    // 14. MAP RESULTSET -> PHIEUXUAT
    // =========================================================

    private PhieuXuat mapResultSet(ResultSet rs) throws SQLException {

        PhieuXuat px = new PhieuXuat();

        px.setMaPX(rs.getString("MaPX"));
        px.setNgayXuat(rs.getDate("NgayXuat")); // java.sql.Date — khớp model
        px.setMaNV(rs.getString("MaNV"));
        px.setTongTien(rs.getBigDecimal("TongTien"));

        // TenNV chỉ có khi query từ VW_PhieuXuat
        try { px.setTenNV(rs.getString("TenNV")); }
        catch (SQLException ignored) { /* query từ bảng gốc, không có cột này */ }

        return px;
    }
}