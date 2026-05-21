package dao;

import model.ChiTietPhieuXuat;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ChiTietPhieuXuatDAO {

    
    public synchronized String generateNextMaCTPX() {

       
        String sql =
                "SELECT TOP 1 MaCTPX " +
                "FROM ChiTietPhieuXuat " +
                "ORDER BY LEN(MaCTPX) DESC, MaCTPX DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                String lastId = rs.getString("MaCTPX");
                if (lastId != null && !lastId.isEmpty()) {
                    int number = Integer.parseInt(lastId.substring(4));
                    return String.format("CTPX%04d", number + 1);
                }
            }
        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi generateNextMaCTPX(): " + e.getMessage());
            e.printStackTrace();
        }

        return "CTPX0001";
    }

    
    // 2. THÊM CHI TIẾT (tự quản lý connection)


   
    public boolean insert(ChiTietPhieuXuat ct) {

        String sql =
                "INSERT INTO ChiTietPhieuXuat " +
                "(MaCTPX, SoLuong, DonGia, MaNL, MaPX) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            setInsertParams(ps, ct);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("không đủ")) {
                System.err.println("[ChiTietPhieuXuatDAO] Tồn kho không đủ để xuất: " + ct.getMaNL());
            } else {
                System.err.println("[ChiTietPhieuXuatDAO] Lỗi insert(): " + e.getMessage());
                e.printStackTrace();
            }
        }

        return false;
    }

    // 3. THÊM CHI TIẾT TRONG TRANSACTION 
    

    
    public boolean insert(Connection conn, ChiTietPhieuXuat ct) throws SQLException {

        String sql =
                "INSERT INTO ChiTietPhieuXuat " +
                "(MaCTPX, SoLuong, DonGia, MaNL, MaPX) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setInsertParams(ps, ct);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            
            if (e.getMessage() != null && e.getMessage().contains("không đủ")) {
                throw new SQLException("Tồn kho không đủ cho nguyên liệu: " + ct.getMaNL(), e);
            }
            throw e;
        }
    }

    
    // 4. LẤY TOÀN BỘ CHI TIẾT PHIẾU XUẤT


    public List<ChiTietPhieuXuat> getAll() {

        List<ChiTietPhieuXuat> list = new ArrayList<>();

        String sql =
                "SELECT MaCTPX, SoLuong, DonGia, MaNL, MaPX " +
                "FROM ChiTietPhieuXuat " +
                "ORDER BY MaPX DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) list.add(mapResultSet(rs));

        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi getAll(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    
    // 5. LẤY CHI TIẾT THEO MÃ PHIẾU XUẤT
    

    public List<ChiTietPhieuXuat> getByMaPX(String maPX) {

        List<ChiTietPhieuXuat> list = new ArrayList<>();

        String sql =
                "SELECT MaCTPX, SoLuong, DonGia, MaNL, MaPX " +
                "FROM ChiTietPhieuXuat " +
                "WHERE MaPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maPX);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi getByMaPX(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

   
    // 6. TÌM THEO MÃ CHI TIẾT
   
    public ChiTietPhieuXuat findById(String maCTPX) {

        String sql =
                "SELECT MaCTPX, SoLuong, DonGia, MaNL, MaPX " +
                "FROM ChiTietPhieuXuat " +
                "WHERE MaCTPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maCTPX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi findById(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    
    // 7. CẬP NHẬT CHI TIẾT
   
    public boolean update(ChiTietPhieuXuat ct) {

        String sql =
                "UPDATE ChiTietPhieuXuat " +
                "SET SoLuong = ?, DonGia = ?, MaNL = ?, MaPX = ? " +
                "WHERE MaCTPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, ct.getSoLuong());
            ps.setBigDecimal(2, ct.getDonGia());
            ps.setString(3, ct.getMaNL());
            ps.setString(4, ct.getMaPX());
            ps.setString(5, ct.getMaCTPX());

            int rows = ps.executeUpdate();
            if (rows == 0)
                System.err.println("[ChiTietPhieuXuatDAO] update(): Không tìm thấy " + ct.getMaCTPX());
            return rows > 0;

        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi update(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }


    // 8. XÓA THEO MÃ CHI TIẾT
    

    public boolean delete(String maCTPX) {

        String sql = "DELETE FROM ChiTietPhieuXuat WHERE MaCTPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maCTPX);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi delete(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // 9. XÓA THEO MÃ PHIẾU XUẤT
    
    public boolean deleteByMaPX(String maPX) {

        String sql = "DELETE FROM ChiTietPhieuXuat WHERE MaPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maPX);
            ps.executeUpdate(); 
            return true;

        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi deleteByMaPX(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    // 10. KIỂM TRA TỒN TẠI
    

    public boolean exists(String maCTPX) {

        String sql = "SELECT COUNT(*) FROM ChiTietPhieuXuat WHERE MaCTPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maCTPX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi exists(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

   
    // 11. TỔNG TIỀN THEO MÃ PHIẾU XUẤT
   
    public BigDecimal getTongTienByMaPX(String maPX) {

        String sql =
                "SELECT SUM(SoLuong * DonGia) AS TongTien " +
                "FROM ChiTietPhieuXuat " +
                "WHERE MaPX = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maPX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("TongTien");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }
        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi getTongTienByMaPX(): " + e.getMessage());
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

   
    // 12. ĐẾM SỐ DÒNG
   

    public int count() {

        String sql = "SELECT COUNT(*) FROM ChiTietPhieuXuat";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.err.println("[ChiTietPhieuXuatDAO] Lỗi count(): " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    
    private void setInsertParams(PreparedStatement ps, ChiTietPhieuXuat ct) throws SQLException {
        ps.setString(1, ct.getMaCTPX());
        ps.setInt(2, ct.getSoLuong());
        ps.setBigDecimal(3, ct.getDonGia());
        ps.setString(4, ct.getMaNL());
        ps.setString(5, ct.getMaPX());
    }

    
    private ChiTietPhieuXuat mapResultSet(ResultSet rs) throws SQLException {
        ChiTietPhieuXuat ct = new ChiTietPhieuXuat();
        ct.setMaCTPX(rs.getString("MaCTPX"));
        ct.setSoLuong(rs.getInt("SoLuong"));       
        ct.setDonGia(rs.getBigDecimal("DonGia"));  
        ct.setMaNL(rs.getString("MaNL"));
        ct.setMaPX(rs.getString("MaPX"));
        return ct;
    }
}