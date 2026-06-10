package dao;

import util.DBConnection; // Đảm bảo đường dẫn gói kết nối này là chính xác
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.NhaCungCap;

public class NhaCungCapDAO {

    
    public NhaCungCapDAO() {
    }

    // 1. Tự động sinh mã Nhà cung cấp tiếp theo (NCC01, NCC02,...)
    public synchronized String generateNextMaNCC() {
        String latestMaNCC = null;
        String sql = "SELECT TOP 1 MaNCC FROM NhaCungCap WHERE MaNCC LIKE 'NCC%' ORDER BY MaNCC DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                latestMaNCC = rs.getString("MaNCC");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "NCC01";
        }

        if (latestMaNCC != null && latestMaNCC.startsWith("NCC")) {
            try {
                String numberPart = latestMaNCC.substring(3); // Cắt chuỗi bỏ chữ 'NCC' lấy phần số
                int number = Integer.parseInt(numberPart);
                int nextNumber = number + 1;
                return String.format("NCC%02d", nextNumber); // Định dạng hiển thị NCC01, NCC02...
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "NCC01";
            }
        }
        return "NCC01"; // Trả về mã đầu tiên nếu bảng trống
    }

    // 2. Thêm mới một Nhà cung cấp
    public boolean addNhaCungCap(NhaCungCap ncc) {
        String newMaNCC = generateNextMaNCC();
        ncc.setMaNCC(newMaNCC); // Gán mã tự động sinh vào đối tượng

        String sql = "INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, SDT, Email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ncc.getMaNCC());
            pstmt.setString(2, ncc.getTenNCC());
            pstmt.setString(3, ncc.getDiachi());
            pstmt.setString(4, ncc.getSdt());
            pstmt.setString(5, ncc.getEmail());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật thông tin Nhà cung cấp
    public boolean updateNhaCungCap(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET TenNCC = ?, DiaChi = ?, SDT = ?, Email = ? WHERE MaNCC = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ncc.getTenNCC());
            pstmt.setString(2, ncc.getDiachi());
            pstmt.setString(3, ncc.getSdt());
            pstmt.setString(4, ncc.getEmail());
            pstmt.setString(5, ncc.getMaNCC()); // Điều kiện WHERE

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa Nhà cung cấp theo mã
    public boolean deleteNhaCungCap(String maNCC) {
        String sql = "DELETE FROM NhaCungCap WHERE MaNCC = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNCC);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể xóa nhà cung cấp này do vướng ràng buộc khóa ngoại (ví dụ: đã có PhieuNhap liên kết)!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Lấy toàn bộ danh sách Nhà cung cấp
    public List<NhaCungCap> getAllNhaCungCap() {
        List<NhaCungCap> danhSach = new ArrayList<>();
        String sql = "SELECT MaNCC, TenNCC, DiaChi, SDT, Email FROM NhaCungCap";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setDiachi(rs.getString("DiaChi")); // Khớp cột SQL
                ncc.setSdt(rs.getString("SDT"));       // Khớp cột SQL
                ncc.setEmail(rs.getString("Email"));   // Khớp cột SQL
                
                danhSach.add(ncc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // 6. Tìm kiếm Nhà cung cấp theo Mã chuẩn xác
    public NhaCungCap getNhaCungCapById(String maNCC) {
        String sql = "SELECT MaNCC, TenNCC, DiaChi, SDT, Email FROM NhaCungCap WHERE MaNCC = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNCC);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    NhaCungCap ncc = new NhaCungCap();
                    ncc.setMaNCC(rs.getString("MaNCC"));
                    ncc.setTenNCC(rs.getString("TenNCC"));
                    ncc.setDiachi(rs.getString("DiaChi"));
                    ncc.setSdt(rs.getString("SDT"));
                    ncc.setEmail(rs.getString("Email"));
                    return ncc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 7. Tìm kiếm Nhà cung cấp gần đúng theo Tên (dành cho ô tìm kiếm trên UI)
    public List<NhaCungCap> searchNhaCungCapByTen(String searchTerm) {
        List<NhaCungCap> danhSach = new ArrayList<>();
        String sql = "SELECT MaNCC, TenNCC, DiaChi, SDT, Email FROM NhaCungCap WHERE TenNCC LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NhaCungCap ncc = new NhaCungCap();
                    ncc.setMaNCC(rs.getString("MaNCC"));
                    ncc.setTenNCC(rs.getString("TenNCC"));
                    ncc.setDiachi(rs.getString("DiaChi"));
                    ncc.setSdt(rs.getString("SDT"));
                    ncc.setEmail(rs.getString("Email"));
                    danhSach.add(ncc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}