package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.NhanVien;
import model.TaiKhoan;
import util.DBConnection;

public class NhanVienDAO {

    // 1. Tự động sinh mã nhân viên tiếp theo (NV01, NV02,...)
    public synchronized String generateNextMaNV() {
        String latestMaNV = null;
        String sql = "SELECT TOP 1 MaNV FROM NhanVien WHERE MaNV LIKE 'NV%' ORDER BY MaNV DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                latestMaNV = rs.getString("MaNV");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "NV01";
        }

        if (latestMaNV != null && latestMaNV.startsWith("NV")) {
            try {
                String numberPart = latestMaNV.substring(2);
                int number = Integer.parseInt(numberPart);
                int nextNumber = number + 1;
                return String.format("NV%02d", nextNumber);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "NV01";
            }
        }
        return "NV01";
    }

    public boolean existsMaNV(String maNV) {
        String sql = "SELECT 1 FROM NhanVien WHERE MaNV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Thêm nhân viên mới
    public boolean addNhanVien(NhanVien nv) {
        String newMaNV = generateNextMaNV();
        nv.setMaNV(newMaNV);

        String sql = "INSERT INTO NhanVien (MaNV, TenNV, SDT, Email, NgaySinh, GioiTinh, ChucVu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getMaNV());
            pstmt.setString(2, nv.getTenNV());
            pstmt.setString(3, nv.getSdt());
            pstmt.setString(4, nv.getEmail());
            if (nv.getNgaySinh() != null) {
                pstmt.setDate(5, new java.sql.Date(nv.getNgaySinh().getTime()));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }
            pstmt.setString(6, nv.getGioiTinh());
            pstmt.setString(7, nv.getChucVu());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật thông tin nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNV = ?, SDT = ?, Email = ?, NgaySinh = ?, GioiTinh = ?, ChucVu = ? WHERE MaNV = ?"; 

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getTenNV());
            pstmt.setString(2, nv.getSdt());
            pstmt.setString(3, nv.getEmail());
            if (nv.getNgaySinh() != null) {
                pstmt.setDate(4, new java.sql.Date(nv.getNgaySinh().getTime()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            pstmt.setString(5, nv.getGioiTinh());
            pstmt.setString(6, nv.getChucVu());
            pstmt.setString(7, nv.getMaNV());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa nhân viên theo mã (và cả tài khoản liên quan nếu có)
    public boolean deleteNhanVien(String maNV) {
        // Xóa tài khoản trước do ràng buộc khóa ngoại
        deleteTaiKhoanByMaNV(maNV);

        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi ràng buộc khóa ngoại (ví dụ nhân viên này đã lập hóa đơn, không thể xóa)!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Lấy danh sách toàn bộ nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> danhSach = new ArrayList<>();
        String sql = "SELECT MaNV, TenNV, SDT, Email, NgaySinh, GioiTinh, ChucVu FROM NhanVien";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setChucVu(rs.getString("ChucVu"));
                
                danhSach.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // 6. Tìm kiếm nhân viên bằng Mã
    public NhanVien getNhanVienById(String maNV) {
        String sql = "SELECT MaNV, TenNV, SDT, Email, NgaySinh, GioiTinh, ChucVu FROM NhanVien WHERE MaNV = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("MaNV"));
                    nv.setTenNV(rs.getString("TenNV"));
                    nv.setSdt(rs.getString("SDT"));
                    nv.setEmail(rs.getString("Email"));
                    nv.setNgaySinh(rs.getDate("NgaySinh"));
                    nv.setGioiTinh(rs.getString("GioiTinh"));
                    nv.setChucVu(rs.getString("ChucVu"));
                    return nv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 7. Tìm kiếm nhân viên gần đúng theo Tên
    public List<NhanVien> searchNhanVienByTenNV(String searchTerm) {
        List<NhanVien> danhSach = new ArrayList<>();
        String sql = "SELECT MaNV, TenNV, SDT, Email, NgaySinh, GioiTinh, ChucVu FROM NhanVien WHERE TenNV LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("MaNV"));
                    nv.setTenNV(rs.getString("TenNV"));
                    nv.setSdt(rs.getString("SDT"));
                    nv.setEmail(rs.getString("Email"));
                    nv.setNgaySinh(rs.getDate("NgaySinh"));
                    nv.setGioiTinh(rs.getString("GioiTinh"));
                    nv.setChucVu(rs.getString("ChucVu"));
                    danhSach.add(nv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // 8. Tên nhân viên từ MaNV (hỗ trợ các phần hiển thị khác)
    public String getTenNhanVienByMaNV(String maNV) {
        String tenNV = "Nhân viên";
        String sql = "SELECT TenNV FROM NhanVien WHERE MaNV = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tenNV = rs.getString("TenNV");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tenNV;
    }

    // ==========================================
    // LIÊN KẾT PHÂN QUYỀN VỚI BẢNG TÀI KHOẢN (TaiKhoan)
    // ==========================================

    public TaiKhoan getTaiKhoanByMaNV(String maNV) {
        String sql = "SELECT MaTK, TenDangNhap, MatKhau, Quyen, MaNV FROM TaiKhoan WHERE MaNV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maNV);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoan(
                        rs.getString("MaTK"),
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getString("Quyen"),
                        rs.getString("MaNV")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveTaiKhoan(TaiKhoan tk) {
        // Kiểm tra xem đã có tài khoản cho nhân viên này chưa
        TaiKhoan existing = getTaiKhoanByMaNV(tk.getMaNV());
        
        if (existing != null) {
            // Update
            String sql = "UPDATE TaiKhoan SET TenDangNhap = ?, MatKhau = ?, Quyen = ? WHERE MaNV = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, tk.getTenDangNhap());
                pstmt.setString(2, tk.getMatKhau());
                pstmt.setString(3, tk.getQuyen());
                pstmt.setString(4, tk.getMaNV());
                
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Insert
            // Tự sinh mã tài khoản
            String nextMaTK = "TK01";
            String sqlMax = "SELECT TOP 1 MaTK FROM TaiKhoan ORDER BY MaTK DESC";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sqlMax);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String last = rs.getString("MaTK");
                    if (last.startsWith("TK")) {
                        int num = Integer.parseInt(last.substring(2)) + 1;
                        nextMaTK = String.format("TK%02d", num);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String sql = "INSERT INTO TaiKhoan (MaTK, TenDangNhap, MatKhau, Quyen, MaNV) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, nextMaTK);
                pstmt.setString(2, tk.getTenDangNhap());
                pstmt.setString(3, tk.getMatKhau());
                pstmt.setString(4, tk.getQuyen());
                pstmt.setString(5, tk.getMaNV());
                
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean deleteTaiKhoanByMaNV(String maNV) {
        String sql = "DELETE FROM TaiKhoan WHERE MaNV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maNV);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}