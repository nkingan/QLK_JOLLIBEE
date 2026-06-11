package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.CTPhieuXuat; 
import model.PhieuXuat;
import util.DBConnection;
public class PhieuXuatDAO {

    // --- Tự động sinh mã phiếu xuất (Ví dụ: PX01, PX02...) ---
    public synchronized String generateNextPhieuXuatCode() {
        String latestMaPX = null;
        // Khớp với kiểu sắp xếp chuỗi mã của hệ thống
        String sql = "SELECT TOP 1 MaPX FROM PhieuXuat ORDER BY LEN(MaPX) DESC, MaPX DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                latestMaPX = rs.getString("MaPX");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "PX01"; // Nếu lỗi hoặc chưa có dữ liệu, trả về mã đầu tiên
        }

        String prefix = "PX";
        int lastNumber = 0;
        if (latestMaPX != null && latestMaPX.startsWith(prefix)) {
            try {
                String numberPart = latestMaPX.substring(prefix.length());
                lastNumber = Integer.parseInt(numberPart);
            } catch (NumberFormatException e) {
                lastNumber = 0;
            }
        }
       return prefix + String.format("%02d", lastNumber + 1);
    }

    // --- Lưu Transaction Phiếu Xuất Kho ---
    public boolean savePhieuXuatTransaction(PhieuXuat phieuXuat, List<CTPhieuXuat> chiTietList) {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Chạy chế độ an toàn Transaction

            // 1. Thêm thông tin phiếu xuất chung
            String sqlHeader = "INSERT INTO PhieuXuat (MaPX, NgayXuat, MaNV, TongTien) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(sqlHeader)) {
                pst.setString(1, phieuXuat.getMaPX());
                pst.setDate(2, new java.sql.Date(phieuXuat.getNgayXuat().getTime()));
                pst.setString(3, phieuXuat.getMaNV());
                pst.setDouble(4, 0); // Ban đầu truyền 0, Trigger SQL tự động tính tổng tiền dựa vào chi tiết
                
                int rows = pst.executeUpdate();
                if (rows <= 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Thêm danh sách chi tiết nguyên liệu xuất
            // Lưu ý: SQL của bạn đã có Trigger TRG_XuatKho tự động trừ kho nguyên liệu (NguyenLieu.SoLuong) 
            // nên code Java KHÔNG cần gọi hàm trừ kho thủ công nữa! Rất tiện lợi.
            String sqlDetail = "INSERT INTO ChiTietPhieuXuat (MaCTPX, SoLuong, DonGia, MaNL, MaPX) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstDetail = conn.prepareStatement(sqlDetail)) {
                int index = 1;
                for (CTPhieuXuat ct : chiTietList) {
                    String maCTPX = ct.getMaCTPX();
                    if (maCTPX == null || maCTPX.trim().isEmpty()) {
                        maCTPX = phieuXuat.getMaPX() + "_" + String.format("%02d", index++);
                    }
                    
                    pstDetail.setString(1, maCTPX);
                    pstDetail.setInt(2, ct.getSoLuong());
                    pstDetail.setDouble(3, ct.getDonGia());
                    pstDetail.setString(4, ct.getMaNL()); // Khớp với MaNL trong SQL của bạn
                    pstDetail.setString(5, phieuXuat.getMaPX());
                    pstDetail.addBatch();
                }
                pstDetail.executeBatch();
            }

            conn.commit(); 
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            // In lỗi ra màn hình (Ví dụ: Lỗi từ Trigger thông báo không đủ hàng tồn kho)
            System.err.println("Lỗi Transaction Xuất Kho: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return success;
    }

    // --- Lấy toàn bộ danh sách phiếu xuất hiển thị lên JTable ---
    public List<PhieuXuat> getAllPhieuXuat() {
        List<PhieuXuat> list = new ArrayList<>();
        // Tận dụng luôn View VW_PhieuXuat bạn đã viết sẵn trong SQL, đỡ phải ghi lệnh JOIN phức tạp!
        String sql = "SELECT * FROM VW_PhieuXuat";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PhieuXuat px = new PhieuXuat();
                px.setMaPX(rs.getString("MaPX"));
                px.setNgayXuat(rs.getDate("NgayXuat"));
                px.setTenNV(rs.getString("TenNV")); // Lấy từ View
                px.setTongTien(rs.getDouble("TongTien"));
                list.add(px);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    // --- Tìm kiếm phiếu xuất kho ---
    public List<PhieuXuat> searchPhieuXuat(String criteria, String searchTerm) {
        List<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM VW_PhieuXuat WHERE ";

        switch (criteria) {
            case "Mã PX":
                sql += "MaPX LIKE ?";
                searchTerm = "%" + searchTerm + "%";
                break;
            case "Tên NV":
                sql += "TenNV LIKE ?";
                searchTerm = "%" + searchTerm + "%";
                break;
            case "Ngày xuất":
                sql += "NgayXuat = ?";
                break;
            default:
                return list;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (criteria.equals("Ngày xuất")) {
                try {
                    java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(searchTerm);
                    pstmt.setDate(1, new java.sql.Date(date.getTime()));
                } catch (ParseException e) {
                    return list;
                }
            } else {
                pstmt.setString(1, searchTerm);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PhieuXuat px = new PhieuXuat();
                    px.setMaPX(rs.getString("MaPX"));
                    px.setNgayXuat(rs.getDate("NgayXuat"));
                    px.setTenNV(rs.getString("TenNV"));
                    px.setTongTien(rs.getDouble("TongTien"));
                    list.add(px);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================
    // XÓA PHIẾU XUẤT KHO VÀ HOÀN TRẢ TỒN KHO NGUYÊN LIỆU (TRANSACTION)
    // =========================================================
    public boolean deletePhieuXuatTransaction(String maPX) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Lấy danh sách nguyên liệu chi tiết để hoàn trả tồn kho nguyên liệu (Cộng lại lượng đã xuất)
            CTPhieuXuatDAO ctDAO = new CTPhieuXuatDAO();
            List<CTPhieuXuat> listCT = ctDAO.getChiTietPhieuXuatByMaPX(conn, maPX);

            if (listCT != null && !listCT.isEmpty()) {
                NguyenLieuDAO nlDAO = new NguyenLieuDAO();
                for (CTPhieuXuat ct : listCT) {
                    // Cộng lại số lượng tồn kho nguyên liệu đã xuất
                    nlDAO.updateStockQuantity(conn, ct.getMaNL(), ct.getSoLuong());
                }
            }

            // 2. Xóa chi tiết phiếu xuất
            ctDAO.deleteChiTietPhieuXuatByMaPX(conn, maPX);

            // 3. Xóa header phiếu xuất
            String sqlHeader = "DELETE FROM PhieuXuat WHERE MaPX = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHeader)) {
                pstmt.setString(1, maPX);
                pstmt.executeUpdate();
            }

            conn.commit(); // Hoàn tất giao dịch
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác nếu lỗi
                    System.err.println("Đã tiến hành Rollback khi xóa phiếu xuất: " + maPX);
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