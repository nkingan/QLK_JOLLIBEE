package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.NguyenLieu;
import model.TonKhoModel;
import util.DBConnection;

public class NguyenLieuDAO {

    /**
     * Thêm mới một bản ghi nguyên liệu vào cơ sở dữ liệu.
     *
     * @param nguyenLieu Đối tượng nguyên liệu cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addNguyenLieu(NguyenLieu nguyenLieu) {
        String sql = "INSERT INTO NguyenLieu (MaNL, TenNL, MaKho, Gianhap, SoLuong, Donvi, Anh) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nguyenLieu.getMaNL());
            pstmt.setString(2, nguyenLieu.getTenNL());
            pstmt.setString(3, nguyenLieu.getMaKho());
            pstmt.setInt(4, nguyenLieu.getGianhap());
            pstmt.setInt(5, nguyenLieu.getSoluong());
            pstmt.setString(6, nguyenLieu.getDonvi());
            pstmt.setString(7, nguyenLieu.getAnh());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi thêm nguyên liệu:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin của một nguyên liệu đã tồn tại dựa trên Mã Nguyên Liệu (MaNL).
     *
     * @param nguyenLieu Đối tượng chứa thông tin mới cần cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean updateNguyenLieu(NguyenLieu nguyenLieu) {
        String sql = "UPDATE NguyenLieu SET TenNL = ?, MaKho = ?, Gianhap = ?, SoLuong = ?, Donvi = ?, Anh = ? WHERE MaNL = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nguyenLieu.getTenNL());
            pstmt.setString(2, nguyenLieu.getMaKho());
            pstmt.setInt(3, nguyenLieu.getGianhap());
            pstmt.setInt(4, nguyenLieu.getSoluong());
            pstmt.setString(5, nguyenLieu.getDonvi());
            pstmt.setString(6, nguyenLieu.getAnh());
            pstmt.setString(7, nguyenLieu.getMaNL());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi cập nhật nguyên liệu:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy mã nguyên liệu cuối cùng hiện có trong cơ sở dữ liệu.
     *
     * @return Chuỗi mã nguyên liệu cuối cùng, hoặc null nếu bảng trống.
     */
    public String getLastIngredientId() {
        String lastId = null;
        String query = "SELECT TOP 1 MaNL FROM NguyenLieu ORDER BY MaNL DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                lastId = rs.getString("MaNL");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã nguyên liệu cuối cùng: " + e.getMessage());
            e.printStackTrace();
        }
        return lastId;
    }

    /**
     * Xóa một bản ghi nguyên liệu ra khỏi cơ sở dữ liệu dựa vào mã khóa chính.
     *
     * @param maNL Mã nguyên liệu cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deleteNguyenLieu(String maNL) {
        String sql = "DELETE FROM NguyenLieu WHERE MaNL = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNL);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi xóa nguyên liệu (Có thể do ràng buộc khóa ngoại):");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy toàn bộ danh sách nguyên liệu hiện có trong bảng.
     *
     * @return Danh sách chứa các đối tượng NguyenLieu.
     */
    public List<NguyenLieu> getAllNguyenLieu() {
        List<NguyenLieu> danhSachNguyenLieu = new ArrayList<>();
        String sql = "SELECT MaNL, TenNL, MaKho, Gianhap, SoLuong, Donvi, Anh FROM NguyenLieu";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                NguyenLieu nl = new NguyenLieu();
                nl.setMaNL(rs.getString("MaNL"));
                nl.setTenNL(rs.getString("TenNL"));
                nl.setMaKho(rs.getString("MaKho"));
                nl.setGianhap(rs.getInt("Gianhap"));
                nl.setSoluong(rs.getInt("SoLuong"));
                nl.setDonvi(rs.getString("Donvi"));
                nl.setAnh(rs.getString("Anh"));
                
                danhSachNguyenLieu.add(nl);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tải toàn bộ danh sách nguyên liệu:");
            e.printStackTrace();
        }
        return danhSachNguyenLieu;
    }

    /**
     * Tìm kiếm thông tin chi tiết của một nguyên liệu cụ thể bằng Mã Nguyên Liệu.
     *
     * @param maNL Mã nguyên liệu cần tìm kiếm.
     * @return Đối tượng NguyenLieu hoặc null nếu không tồn tại.
     */
    public NguyenLieu getNguyenLieuById(String maNL) {
        NguyenLieu nl = null;
        String sql = "SELECT MaNL, TenNL, MaKho, Gianhap, SoLuong, Donvi, Anh FROM NguyenLieu WHERE MaNL = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNL);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    nl = new NguyenLieu();
                    nl.setMaNL(rs.getString("MaNL"));
                    nl.setTenNL(rs.getString("TenNL"));
                    nl.setMaKho(rs.getString("MaKho"));
                    nl.setGianhap(rs.getInt("Gianhap"));
                    nl.setSoluong(rs.getInt("SoLuong"));
                    nl.setDonvi(rs.getString("Donvi"));
                    nl.setAnh(rs.getString("Anh"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm nguyên liệu theo mã khóa chính:");
            e.printStackTrace();
        }
        return nl;
    }

    // --- QUẢN LÝ TỒN KHO NGUYÊN LIỆU (Nhập / Xuất / Cảnh báo) ---
    
    /**
     * Thay đổi số lượng tồn kho của nguyên liệu ngay trong một Transaction chung.
     */
    public void updateStockQuantity(Connection conn, String maNL, int quantityChange) throws SQLException {
        String sql = "UPDATE NguyenLieu SET SoLuong = SoLuong + ? WHERE MaNL = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityChange);
            pstmt.setString(2, maNL);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Cảnh báo kho: Không tìm thấy nguyên liệu để cập nhật số lượng tồn (MaNL: " + maNL + ").");
            }
        }
    }

    /**
     * Lấy số lượng hàng tồn hiện tại của một nguyên liệu.
     *
     * @param maNL Mã nguyên liệu cần kiểm tra.
     * @return Số lượng tồn trong kho.
     */
    public int getStockQuantity(String maNL) {
        int stock = 0;
        String sql = "SELECT SoLuong FROM NguyenLieu WHERE MaNL = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNL);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stock = rs.getInt("SoLuong");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn số lượng tồn kho nguyên liệu:");
            e.printStackTrace();
        }
        return stock;
    }

    /**
     * Lấy danh sách các nguyên liệu sắp hết hàng dựa trên một hạn mức chỉ định.
     */
    public List<NguyenLieu> getLowStockProducts(int threshold) {
        List<NguyenLieu> lowStockList = new ArrayList<>();
        String sql = "SELECT MaNL, TenNL, SoLuong, Donvi, MaKho, Gianhap, Anh FROM NguyenLieu WHERE SoLuong <= ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, threshold);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NguyenLieu nl = new NguyenLieu();
                    nl.setMaNL(rs.getString("MaNL"));
                    nl.setTenNL(rs.getString("TenNL"));
                    nl.setSoluong(rs.getInt("SoLuong"));
                    nl.setDonvi(rs.getString("Donvi"));
                    nl.setMaKho(rs.getString("MaKho"));
                    nl.setGianhap(rs.getInt("Gianhap"));
                    nl.setAnh(rs.getString("Anh"));
                    lowStockList.add(nl);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tải dữ liệu nguyên liệu sắp hết kho:");
            e.printStackTrace();
        }
        return lowStockList;
    }

    public int getLowStockCount(int threshold) {
        return getLowStockProducts(threshold).size();
    }

    // --- BỘ CÁC PHƯƠNG THỨC TÌM KIẾM NÂNG CAO VÀ PHÂN LOẠI ---

    /**
     * Phương thức tìm kiếm tổng hợp đa năng dựa trên tiêu chí lựa chọn từ ComboBox.
     */
    public List<NguyenLieu> searchNguyenLieu(String criteria, String searchTerm) {
        List<NguyenLieu> nguyenLieuList = new ArrayList<>();
        String sql = "SELECT nl.MaNL, nl.TenNL, nl.MaKho, nl.Gianhap, nl.SoLuong, nl.Donvi, nl.Anh " +
                     "FROM NguyenLieu nl " +
                     "WHERE ";

        switch (criteria) {
            case "Tên NL":
                sql += "nl.TenNL LIKE ?";
                searchTerm = "%" + searchTerm + "%";
                break;
            case "Mã NL":
                sql += "nl.MaNL LIKE ?";
                searchTerm = "%" + searchTerm + "%";
                break;
            case "Mã Kho":
                sql += "nl.MaKho LIKE ?";
                searchTerm = "%" + searchTerm + "%";
                break;
            default:
                System.err.println("Tiêu chí tìm kiếm truyền vào sai định dạng: " + criteria);
                return nguyenLieuList;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, searchTerm);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NguyenLieu nl = new NguyenLieu();
                    nl.setMaNL(rs.getString("MaNL"));
                    nl.setTenNL(rs.getString("TenNL"));
                    nl.setMaKho(rs.getString("MaKho"));
                    nl.setGianhap(rs.getInt("Gianhap"));
                    nl.setSoluong(rs.getInt("SoLuong"));
                    nl.setDonvi(rs.getString("Donvi"));
                    nl.setAnh(rs.getString("Anh"));

                    nguyenLieuList.add(nl);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi xảy ra trong quá trình thực thi tìm kiếm đa năng:");
            e.printStackTrace();
        }

        return nguyenLieuList;
    }

    /**
     * Tìm kiếm nhanh nguyên liệu theo tên.
     */
    public List<NguyenLieu> searchNguyenLieuByName(String tenNL) {
        List<NguyenLieu> danhSach = new ArrayList<>();
        String sql = "SELECT MaNL, TenNL, MaKho, Gianhap, SoLuong, Donvi, Anh FROM NguyenLieu WHERE TenNL LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + tenNL + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NguyenLieu nl = new NguyenLieu();
                    nl.setMaNL(rs.getString("MaNL"));
                    nl.setTenNL(rs.getString("TenNL"));
                    nl.setMaKho(rs.getString("MaKho"));
                    nl.setGianhap(rs.getInt("Gianhap"));
                    nl.setSoluong(rs.getInt("SoLuong"));
                    nl.setDonvi(rs.getString("Donvi"));
                    nl.setAnh(rs.getString("Anh"));
                    danhSach.add(nl);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm nhanh nguyên liệu theo chuỗi tên:");
            e.printStackTrace();
        }
        return danhSach;
    }

    /**
     * Lọc danh sách nguyên liệu thuộc về một kho nhất định.
     */
    public List<NguyenLieu> getNguyenLieuByKho(String maKho) {
        List<NguyenLieu> nguyenLieuList = new ArrayList<>();
        String sql = "SELECT MaNL, TenNL, MaKho, Gianhap, SoLuong, Donvi, Anh FROM NguyenLieu WHERE MaKho = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maKho);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NguyenLieu nl = new NguyenLieu();
                    nl.setMaNL(rs.getString("MaNL"));
                    nl.setTenNL(rs.getString("TenNL"));
                    nl.setMaKho(rs.getString("MaKho"));
                    nl.setGianhap(rs.getInt("Gianhap"));
                    nl.setSoluong(rs.getInt("SoLuong"));
                    nl.setDonvi(rs.getString("Donvi"));
                    nl.setAnh(rs.getString("Anh"));
                    nguyenLieuList.add(nl);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lọc danh sách nguyên liệu theo Mã kho:");
            e.printStackTrace();
        }
        return nguyenLieuList;
    }

    public List<TonKhoModel> getDanhSachTonKho() {
        List<TonKhoModel> list = new ArrayList<>();
        String sql = "SELECT * FROM VW_TonKho"; 
        
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
}