// package dao;

// import util.DBConnection;
// import model.ChiTietPhieuNhap;
// import java.sql.*;
// import java.util.ArrayList;
// import java.util.List;

// public class ChiTietPhieuNhapDAO {

//     // 1. Tìm các chi tiết nguyên liệu thuộc về một Phiếu Nhập cụ thể nào đó
//     public List<ChiTietPhieuNhap> getByMaPN(String maPN) {
//         List<ChiTietPhieuNhap> list = new ArrayList<>();
//         String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN = ?";

//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement(sql)) {
            
//             ps.setString(1, maPN);
//             try (ResultSet rs = ps.executeQuery()) {
//                 while (rs.next()) {
//                     ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
//                     ctpn.setMaCTPN(rs.getString("MaCTPN"));
//                     ctpn.setSoLuong(rs.getInt("SoLuong"));
//                     ctpn.setDonGia(rs.getDouble("DonGia"));
//                     ctpn.setMaNL(rs.getString("MaNL"));
//                     ctpn.setMaPN(rs.getString("MaPN"));
//                     ctpn.setHanSuDung(rs.getDate("HanSuDung")); 
                    
//                     list.add(ctpn);
//                 }
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return list;
//     }

//     // 2. Thêm mới một dòng chi tiết vật tư nhập kho
//     public boolean insert(ChiTietPhieuNhap ctpn) {
//         String sql = "INSERT INTO ChiTietPhieuNhap (MaCTPN, SoLuong, DonGia, MaNL, MaPN, HanSuDung) VALUES (?, ?, ?, ?, ?, ?)";
        
//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement(sql)) {
            
//             ps.setString(1, ctpn.getMaCTPN());
//             ps.setInt(2, ctpn.getSoLuong());
//             ps.setDouble(3, ctpn.getDonGia());
//             ps.setString(4, ctpn.getMaNL());
//             ps.setString(5, ctpn.getMaPN());
            
//             // Xử lý Hạn sử dụng: Kiểm tra nếu có hạn sử dụng thì lưu, không thì để NULL trong DB
//             if (ctpn.getHanSuDung() != null) {
//                 ps.setDate(6, new java.sql.Date(ctpn.getHanSuDung().getTime()));
//             } else {
//                 ps.setNull(6, Types.DATE);
//             }
            
//             return ps.executeUpdate() > 0;
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return false;
//     }
// }
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import model.ChiTietPhieuNhap;
import util.DBConnection;

public class ChiTietPhieuNhapDAO {

    public ChiTietPhieuNhapDAO() {
        // Constructor rỗng
    }

    // =========================================================
    // 1. THÊM CHI TIẾT PHIẾU NHẬP (Tham gia vào Transaction)
    // =========================================================
    public void addChiTietPhieuNhap(Connection conn, ChiTietPhieuNhap ct) throws SQLException {
        // Khớp 100% với cấu trúc bảng ChiTietPhieuNhap trong SQL Server của bạn
        String sql = "INSERT INTO ChiTietPhieuNhap (MaCTPN, SoLuong, DonGia, HanSuDung, MaNL, MaPN) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ct.getMaCTPN());
            pstmt.setInt(2, ct.getSoLuong());
            pstmt.setBigDecimal(3, ct.getDonGia()); // Kiểu DECIMAL(18,2) -> setBigDecimal
            
            // Xử lý hạn sử dụng (nếu null thì setNull, có thì chuyển sang sql.Date)
            if (ct.getHanSuDung() != null) {
                pstmt.setDate(4, new java.sql.Date(ct.getHanSuDung().getTime()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            
            pstmt.setString(5, ct.getMaNL());
            pstmt.setString(6, ct.getMaPN());

            pstmt.executeUpdate();
            // Không bọc try-catch lỗi ở đây để ném về PhieuNhapDAO xử lý Rollback khi gặp sự cố
        } 
    }

    // =========================================================
    // 2. LẤY DANH SÁCH CHI TIẾT THEO MÃ PHIẾU - ĐỌC TỪ VIEW
    // =========================================================
    public List<ChiTietPhieuNhap> getChiTietPhieuNhapByMaPN(String maPN) {
        List<ChiTietPhieuNhap> chiTietList = new ArrayList<>();
        
        // Tận dụng View VW_ChiTietPhieuNhap bạn đã viết sẵn giúp lấy luôn TenNL, DonViTinh, ThanhTien
        String sql = "SELECT MaCTPN, MaNL, TenNL, DonViTinh, SoLuong, DonGia, ThanhTien, HanSuDung, TrangThaiHan " +
                     "FROM VW_ChiTietPhieuNhap " +
                     "WHERE MaPN = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maPN);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
                    ct.setMaCTPN(rs.getString("MaCTPN"));
                    ct.setMaPN(maPN);
                    ct.setMaNL(rs.getString("MaNL"));
                    
                    // Các trường bổ trợ hiển thị từ View lên JTable UI
                    ct.setTenNL(rs.getString("TenNL")); 
                    ct.setDonViTinh(rs.getString("DonViTinh"));
                    
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getBigDecimal("DonGia"));
                    ct.setThanhTien(rs.getBigDecimal("ThanhTien")); // Tự động lấy ô tính sẵn từ View
                    ct.setHanSuDung(rs.getDate("HanSuDung"));
                    ct.setTrangThaiHan(rs.getString("TrangThaiHan")); // "Còn hạn", "Hết hạn"...

                    chiTietList.add(ct);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chi tiết phiếu nhập từ View:");
            e.printStackTrace();
            return null;
        }
        return chiTietList;
    }

    // =========================================================
    // 3. XÓA TẤT CẢ CHI TIẾT THEO MÃ PHIẾU NHẬP (Dùng khi hủy/xóa phiếu)
    // =========================================================
    public void deleteChiTietPhieuNhapByMaPN(Connection conn, String maPN) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE MaPN = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPN);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("👉 Đã xóa " + affectedRows + " dòng chi tiết vật tư của mã phiếu: " + maPN);
        }
    }

    // Hàm lấy danh sách chi tiết nguyên liệu của một phiếu nhập
    public List<ChiTietPhieuNhap> getChiTietByMaPN(String maPN) {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT MaPN, MaNL, SoLuong, DonGia, ThanhTien FROM ChiTietPhieuNhap WHERE MaPN = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maPN);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
                    ct.setMaPN(rs.getString("MaPN"));
                    ct.setMaNL(rs.getString("MaNL"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getBigDecimal("DonGia"));       // Đọc chuẩn dữ liệu BigDecimal
                    ct.setThanhTien(rs.getBigDecimal("ThanhTien")); // Đọc chuẩn dữ liệu BigDecimal
                    
                    list.add(ct);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chi tiết phiếu nhập cho mã: " + maPN);
            e.printStackTrace();
        }
        return list;
    }
}