package dao;

import model.PhieuXuat;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


 
public class PhieuXuatDAO {

    
    // 1. TỰ ĐỘNG SINH MÃ PHIẾU XUẤT
    public synchronized String generateNextMaPX() {

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

   
    // 2. THÊM PHIẾU XUẤT (DÙNG TRANSACTION )
    
    public boolean insert(Connection conn, PhieuXuat px) throws SQLException {

        String sql =
                "INSERT INTO PhieuXuat (MaPX, NgayXuat, MaNV) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, px.getMaPX());

            
            if (px.getNgayXuat() == null)
                throw new IllegalArgumentException("NgayXuat không được null");
            ps.setDate(2, px.getNgayXuat()); 

            ps.setString(3, px.getMaNV());

            return ps.executeUpdate() > 0;
        }
    }

    
    // 3. THÊM PHIẾU XUẤT ĐƠN LẺ 
    
    public boolean insert(PhieuXuat px) {
        try (Connection conn = DBConnection.getConnection()) {
            return insert(conn, px);
        } catch (Exception e) {
            System.err.println("[PhieuXuatDAO] Lỗi insert(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    
    // 4. LẤY TOÀN BỘ DANH SÁCH PHIẾU XUẤT
   
    public List<PhieuXuat> getAll() {

        List<PhieuXuat> list = new ArrayList<>();

        
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

    
    // 5. TÌM PHIẾU XUẤT THEO MÃ
  
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

    // 6. CẬP NHẬT PHIẾU XUẤT
   
    public boolean update(PhieuXuat px) {

        
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

    
    // 7. XÓA PHIẾU XUẤT
   

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

    
    // 8. KIỂM TRA MÃ TỒN TẠI
    
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

   
    // 9. TÌM KIẾM PHIẾU XUẤT (theo mã, tên NV)
    
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

    
    // 10. LẤY PHIẾU XUẤT THEO NHÂN VIÊN
   
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

   
    // 11. LẤY PHIẾU XUẤT THEO KHOẢNG NGÀY
  
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

   
    // 12. ĐẾM TỔNG SỐ PHIẾU XUẤT
    

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

   
    // 13. TỔNG TIỀN TOÀN BỘ PHIẾU XUẤT
   
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

   
    // 14. MAP RESULTSET -> PHIEUXUAT


    private PhieuXuat mapResultSet(ResultSet rs) throws SQLException {

        PhieuXuat px = new PhieuXuat();

        px.setMaPX(rs.getString("MaPX"));
        px.setNgayXuat(rs.getDate("NgayXuat")); 
        px.setMaNV(rs.getString("MaNV"));
        px.setTongTien(rs.getBigDecimal("TongTien"));

       
        try { px.setTenNV(rs.getString("TenNV")); }
        catch (SQLException ignored) {  }

        return px;
    }
}