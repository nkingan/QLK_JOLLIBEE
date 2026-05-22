package dao;

import util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TonKhoDAO {

    
    public int getTongTonByMaNL(String maNL) {
        String sql = "SELECT SoLuong FROM NguyenLieu WHERE MaNL = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNL);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoLuong");
                }
            }
        } catch (Exception e) {
            System.err.println("[TonKhoDAO] Lỗi hàm getTongTonByMaNL(): " + e.getMessage());
        }
        return -1; 
    }

    
    public List<Vector<Object>> getBaoCaoTonKho(String keyword, String selectedKho) {
        List<Vector<Object>> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT n.MaNL, n.TenNL, n.DonViTinh, n.SoLuong AS TonKhoHienTai, " +
            "       n.MaKho, k.TenKho, c.MaCTPN AS MaLo, p.NgayNhap AS NgayNhapLo, " +
            "       c.SoLuong AS SoLuongNhapLo, c.HanSuDung, " +
            "       ISNULL(CASE " +
            "           WHEN c.HanSuDung IS NULL THEN N'Chưa cập nhật' " +
            "           WHEN c.HanSuDung < CAST(GETDATE() AS DATE) THEN N'Hết hạn' " +
            "           WHEN c.HanSuDung <= DATEADD(DAY, 30, GETDATE()) THEN N'Sắp hết hạn' " +
            "           ELSE N'Còn hạn' " +
            "       END, N'Chưa cập nhật') AS TrangThaiHan, c.DonGia " +
            "FROM ChiTietPhieuNhap c " +
            "JOIN PhieuNhap p ON c.MaPN = p.MaPN " +
            "RIGHT JOIN NguyenLieu n ON c.MaNL = n.MaNL " + 
            "LEFT JOIN Kho k ON n.MaKho = k.MaKho " +
            "WHERE n.TenNL LIKE ?"
        );
        
        if (selectedKho != null && !selectedKho.equals("Tất cả các kho")) {
            String maKho = selectedKho.split(" - ")[0].trim();
            sql.append(" AND n.MaKho = '").append(maKho).append("'");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("MaNL"));
                    row.add(rs.getString("TenNL"));
                    row.add(rs.getString("DonViTinh"));
                    row.add(rs.getInt("TonKhoHienTai")); 
                    row.add(rs.getString("MaKho"));
                    row.add(rs.getString("TenKho"));
                    
                    String maLo = rs.getString("MaLo");
                    row.add(maLo != null ? maLo : "-");
                    
                    Date ngayNhap = rs.getDate("NgayNhapLo");
                    row.add(ngayNhap != null ? ngayNhap : null); 
                    row.add(maLo != null ? rs.getInt("SoLuongNhapLo") : 0);
                    
                    Date hsd = rs.getDate("HanSuDung");
                    row.add(hsd != null ? hsd : null);
                    
                    row.add(rs.getString("TrangThaiHan"));
                    
                    BigDecimal donGia = rs.getBigDecimal("DonGia");
                    row.add(donGia != null ? donGia : BigDecimal.ZERO);
                    
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.err.println("[TonKhoDAO] Lỗi nạp dữ liệu báo cáo tồn kho: " + e.getMessage());
        }
        return list;
    }

    public List<String> getDanhSachKho() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaKho, TenKho FROM Kho";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("MaKho") + " - " + rs.getString("TenKho"));
            }
        } catch (Exception e) {
            System.err.println("[TonKhoDAO] Lỗi lấy danh sách kho: " + e.getMessage());
        }
        return list;
    }
}