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
    // TỰ ĐỘNG TẠO MÃ PHIẾU NHẬP TIẾP THEO (Format: PN001, PN002...)
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
        // Đọc trực tiếp từ VW_PhieuNhap giúp tối ưu hiệu năng hệ thống
        String sql = "SELECT MaPN, NgayNhap, TenNV, TenNCC, TongTien FROM VW_PhieuNhap";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPN(rs.getString("MaPN"));
                pn.setNgayNhap(rs.getDate("NgayNhap"));
                pn.setTongTien(rs.getBigDecimal("TongTien")); // Lấy chuẩn dữ liệu DECIMAL(18,2)
                pn.setTenNV(rs.getString("TenNV"));           // Lấy từ View
                pn.setTenNCC(rs.getString("TenNCC"));         // Lấy từ View
                
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
            conn.setAutoCommit(false); // BẤT ĐẦU TRANSACTION TRÊN JAVA

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
                    ct.setMaPN(phieuNhap.getMaPN()); // Đồng bộ mã phiếu nhập
                    
                    // Thực hiện lưu chi tiết hóa đơn
                    chiTietPhieuNhapDAO.addChiTietPhieuNhap(conn, ct);
                    
                    // 🌟 KHÔNG CẦN CẬP NHẬT TỒN KHO THỦ CÔNG! 
                    // Nhờ Trigger `TRG_NhapKho` của nhóm bạn viết rất tốt, khi câu lệnh insert 
                    // chi tiết chạy, SQL Server sẽ tự động cộng dồn số lượng vào bảng `NguyenLieu` 
                    // và tự tính toán lại `TongTien` của bảng `PhieuNhap`.
                }
            }

            conn.commit(); // Hoàn tất giao dịch an toàn
            success = true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hủy bỏ thao tác nếu phát hiện lỗi (Ví dụ: Đơn giá > 1.000.000đ sẽ bị Trigger chặn và ném lỗi về đây)
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
    // XÓA PHIẾU NHẬP VÀ HOÀN TRẢ TỒN KHO NGUYÊN LIỆU (TRANSACTION)
    // =========================================================
    public boolean deletePhieuNhapTransaction(String maPN) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Lấy danh sách nguyên liệu chi tiết để trừ tồn kho tương ứng
            List<ChiTietPhieuNhap> listCT = chiTietPhieuNhapDAO.getChiTietPhieuNhapByMaPN(maPN);
            if (listCT == null) {
                listCT = chiTietPhieuNhapDAO.getChiTietByMaPN(maPN);
            }

            if (listCT != null && !listCT.isEmpty()) {
                NguyenLieuDAO nlDAO = new NguyenLieuDAO();
                for (ChiTietPhieuNhap ct : listCT) {
                    // Trừ số lượng tồn kho nguyên liệu đã nhập
                    nlDAO.updateStockQuantity(conn, ct.getMaNL(), -ct.getSoLuong());
                }
            }

            // 2. Xóa chi tiết phiếu nhập
            chiTietPhieuNhapDAO.deleteChiTietPhieuNhapByMaPN(conn, maPN);

            // 3. Xóa header phiếu nhập
            String sqlHeader = "DELETE FROM PhieuNhap WHERE MaPN = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHeader)) {
                pstmt.setString(1, maPN);
                pstmt.executeUpdate();
            }

            conn.commit(); // Hoàn tất giao dịch
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác nếu lỗi
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