package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.*;
import util.DBConnection;
/**
 * ReportDAO: Hợp nhất các báo cáo kinh doanh và báo cáo kho.
 * Sử dụng lớp DBConnection để kết nối cơ sở dữ liệu.
 */
public class ReportDAO {

    // --- NHÓM 1: BÁO CÁO KINH DOANH (Doanh thu) ---

    public List<Object[]> getRevenueReport(Date startDate, Date endDate, String periodType) {
        List<Object[]> revenueData = new ArrayList<>();
        String sql;
        
        switch (periodType) {
            case "Ngày":
                sql = "SELECT CAST(NgayXuat AS DATE) AS Period, SUM(TongTien) AS TotalRevenue FROM PhieuXuat WHERE NgayXuat BETWEEN ? AND ? GROUP BY CAST(NgayXuat AS DATE) ORDER BY Period";
                break;
            case "Tháng":
                sql = "SELECT CAST(YEAR(NgayXuat) AS VARCHAR(4)) + '-' + RIGHT('0' + CAST(MONTH(NgayXuat) AS VARCHAR(2)), 2) AS Period, SUM(TongTien) AS TotalRevenue FROM PhieuXuat WHERE NgayXuat BETWEEN ? AND ? GROUP BY YEAR(NgayXuat), MONTH(NgayXuat) ORDER BY Period";
                break;
            default: // Năm
                sql = "SELECT YEAR(NgayXuat) AS Period, SUM(TongTien) AS TotalRevenue FROM PhieuXuat WHERE NgayXuat BETWEEN ? AND ? GROUP BY YEAR(NgayXuat) ORDER BY Period";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            pstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    revenueData.add(new Object[]{rs.getString("Period"), rs.getDouble("TotalRevenue")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueData;
    }

    // --- NHÓM 2: BÁO CÁO KHO (Dữ liệu từ View SQL) ---

    /**
     * Lấy danh sách tồn kho hiện tại từ View VW_TonKho
     */
    public List<TonKhoModel> getInventoryReport() {
        List<TonKhoModel> list = new ArrayList<>();
        String sql = "SELECT MaNL, TenNL, DonViTinh, TonKhoHienTai, MaKho, TenKho, MaLo, NgayNhapLo, SoLuongNhapLo, HanSuDung, TrangThaiHan FROM VW_TonKho";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                TonKhoModel tk = new TonKhoModel();
                tk.setMaNL(rs.getString("MaNL"));
                tk.setTenNL(rs.getString("TenNL"));
                tk.setDonViTinh(rs.getString("DonViTinh"));
                tk.setTonKhoHienTai(rs.getInt("TonKhoHienTai"));
                tk.setMaKho(rs.getString("MaKho"));
                tk.setTenKho(rs.getString("TenKho"));
                tk.setMaLo(rs.getString("MaLo"));
                tk.setNgayNhapLo(rs.getDate("NgayNhapLo"));
                tk.setSoLuongNhapLo(rs.getInt("SoLuongNhapLo"));
                tk.setHanSuDung(rs.getDate("HanSuDung"));
                tk.setTrangThaiHan(rs.getString("TrangThaiHan"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Báo cáo cảnh báo hàng hết hạn hoặc sắp hết hạn
     */
    public List<TonKhoModel> getExpiringProductsReport() {
        List<TonKhoModel> list = new ArrayList<>();
        String sql = "SELECT * FROM VW_TonKho WHERE TrangThaiHan IN (N'Sắp hết hạn', N'Hết hạn')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                TonKhoModel tk = new TonKhoModel();
                tk.setMaNL(rs.getString("MaNL"));
                tk.setTenNL(rs.getString("TenNL"));
                tk.setTonKhoHienTai(rs.getInt("TonKhoHienTai"));
                tk.setTrangThaiHan(rs.getString("TrangThaiHan"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- NHÓM 3: BÁO CÁO NGUYÊN LIỆU BÁN CHẠY ---

    public List<Object[]> getBestSellingProductsReport(Date startDate, Date endDate, int limit) {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT TOP (?) cthd.MaNL, nl.TenNL, SUM(cthd.SoLuong) AS TotalQty " +
                     "FROM ChiTietPhieuXuat cthd " +
                     "JOIN PhieuXuat px ON cthd.MaPX = px.MaPX " +
                     "JOIN NguyenLieu nl ON cthd.MaNL = nl.MaNL " +
                     "WHERE px.NgayXuat BETWEEN ? AND ? " +
                     "GROUP BY cthd.MaNL, nl.TenNL ORDER BY TotalQty DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            pstmt.setTimestamp(2, new Timestamp(startDate.getTime()));
            pstmt.setTimestamp(3, new Timestamp(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    data.add(new Object[]{rs.getString("MaNL"), rs.getString("TenNL"), rs.getInt("TotalQty")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // --- NHÓM 4: CÁC TIỂU MỤC THỐNG KÊ DASHBOARD TỔNG QUAN ---

    public int getTotalIngredientsCount() {
        String sql = "SELECT COUNT(*) FROM NguyenLieu";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalInventoryValue() {
        String sql = "SELECT SUM(CAST(Gianhap AS DECIMAL(18,2)) * SoLuong) FROM NguyenLieu";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayImportReceiptsCount() {
        String sql = "SELECT COUNT(*) FROM PhieuNhap WHERE NgayNhap = CAST(GETDATE() AS DATE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayExportReceiptsCount() {
        String sql = "SELECT COUNT(*) FROM PhieuXuat WHERE NgayXuat = CAST(GETDATE() AS DATE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Object[]> getTop10IngredientsByQuantity() {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT TOP 10 TenNL, SoLuong FROM NguyenLieu ORDER BY SoLuong DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                data.add(new Object[]{rs.getString("TenNL"), rs.getInt("SoLuong")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<NguyenLieu> getLowStockIngredients(int threshold) {
        List<NguyenLieu> list = new ArrayList<>();
        String sql = "SELECT MaNL, TenNL, SoLuong, DonViTinh, MaKho, Gianhap FROM NguyenLieu WHERE SoLuong <= ? ORDER BY SoLuong ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, threshold);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NguyenLieu nl = new NguyenLieu();
                    nl.setMaNL(rs.getString("MaNL"));
                    nl.setTenNL(rs.getString("TenNL"));
                    nl.setSoluong(rs.getInt("SoLuong"));
                    nl.setDonvi(rs.getString("DonViTinh"));
                    nl.setMaKho(rs.getString("MaKho"));
                    nl.setGianhap(rs.getInt("Gianhap"));
                    list.add(nl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
