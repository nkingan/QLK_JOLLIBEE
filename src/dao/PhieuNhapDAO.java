package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import model.PhieuNhap;
import model.ChiTietPhieuNhap;
import util.DBConnection; // Thay thế bằng DatabaseConnection nếu file của bạn đặt tên thế
public class PhieuNhapDAO {

    private ChiTietPhieuNhapDAO chiTietPhieuNhapDAO;

    public PhieuNhapDAO() {
        this.chiTietPhieuNhapDAO = new ChiTietPhieuNhapDAO();
    }

    // =========================================================
    // TỰ ĐỘNG TẠO MÃ PHIẾU NHẬP TIẾP THEO 
    // =========================================================
    public synchronized String generateNextPhieuNhapCode() {
        String latestMaPN = null;
        String sql = "SELECT TOP 1 MaPN FROM PhieuNhap WHERE MaPN LIKE 'PN%' ORDER BY MaPN DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                latestMaPN = rs.getString("MaPN");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã Phiếu Nhập cuối cùng:");
            e.printStackTrace();
            return null;
        }

        int nextNumber = 1;
        if (latestMaPN != null && latestMaPN.startsWith("PN")) {
            try {
                String numberPart = latestMaPN.substring(2); 
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                System.err.println("Lỗi phân tích mã PN (" + latestMaPN + "), bắt đầu lại từ 1.");
                nextNumber = 1;
            }
        }
        return String.format("PN%02d", nextNumber);
    }

    // =========================================================
    // LẤY TOÀN BỘ DANH SÁCH PHIẾU NHẬP - ĐỌC TỪ VIEW TRONG SQL
    // =========================================================
    public List<PhieuNhap> getAllPhieuNhap() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT MaPN, NgayNhap, TenNV, TenNCC, TongTien FROM VW_PhieuNhap";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPN(rs.getString("MaPN"));
                pn.setNgayNhap(rs.getDate("NgayNhap"));
                pn.setTongTien(rs.getBigDecimal("TongTien")); 
                pn.setTenNV(rs.getString("TenNV"));          
                pn.setTenNCC(rs.getString("TenNCC"));        
                
                list.add(pn);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách từ VW_PhieuNhap:");
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================
    // LƯU PHIẾU NHẬP ĐỒNG THỜI VỚI TRANSACTION
    // =========================================================
    public boolean savePhieuNhapTransaction(PhieuNhap phieuNhap, List<ChiTietPhieuNhap> danhSachChiTiet) {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            // 1. Lưu thông tin chứng từ Phiếu Nhập (Header)
            String insertPhieuNhapSql = "INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNV, MaNCC, TongTien) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertPhieuNhapSql)) {
                pstmt.setString(1, phieuNhap.getMaPN());
                pstmt.setDate(2, new java.sql.Date(phieuNhap.getNgayNhap().getTime()));
                pstmt.setString(3, phieuNhap.getMaNV());
                pstmt.setString(4, phieuNhap.getMaNCC());
                pstmt.setBigDecimal(5, phieuNhap.getTongTien());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Lưu danh sách nguyên liệu chi tiết đi kèm Phiếu Nhập
            if (danhSachChiTiet != null && !danhSachChiTiet.isEmpty()) {
                for (ChiTietPhieuNhap ct : danhSachChiTiet) {
                    ct.setMaPN(phieuNhap.getMaPN()); 

                    chiTietPhieuNhapDAO.addChiTietPhieuNhap(conn, ct);
                    
                }
            }

            conn.commit(); 
            success = true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Đã tiến hành Rollback dữ liệu Phiếu Nhập thành công!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            success = false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return success;
    }

    // =========================================================
    // XÓA PHIẾU NHẬP VÀ HOÀN TRẢ TỒN KHO NGUYÊN LIỆU 
    // =========================================================
    public boolean deletePhieuNhapTransaction(String maPN) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            // 1. Xóa chi tiết phiếu nhập
            chiTietPhieuNhapDAO.deleteChiTietPhieuNhapByMaPN(conn, maPN);

            // 2. Xóa header phiếu nhập
            String sqlHeader = "DELETE FROM PhieuNhap WHERE MaPN = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHeader)) {
                pstmt.setString(1, maPN);
                pstmt.executeUpdate();
            }

            conn.commit(); 
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Đã tiến hành Rollback khi xóa phiếu nhập: " + maPN);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            success = false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return success;
    }
}