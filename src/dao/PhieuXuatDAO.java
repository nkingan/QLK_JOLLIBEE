// package dao;

// import model.PhieuXuat;
// import util.DBConnection;

// import java.sql.*;
// import java.util.*;

// public class PhieuXuatDAO implements CrudDAO<PhieuXuat> {

//     public void insert(PhieuXuat px) {
//         String sql = "INSERT INTO PhieuXuat VALUES (?, ?, ?)";

//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement(sql)) {

//             ps.setString(1, px.getMaPX());
//             ps.setString(2, px.getMaNV());
//             ps.setDate(3, new java.sql.Date(px.getNgayXuat().getTime()));

//             ps.executeUpdate();
//         } catch (Exception e) { e.printStackTrace(); }
//     }

//     public List<PhieuXuat> getAll() { return new ArrayList<>(); }
//     public void update(PhieuXuat t) {}
//     public void delete(String id) {}
//     public PhieuXuat findById(String id) { return null; }
// }
package dao;

import model.PhieuXuat;
import model.ChiTietPhieuXuat;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class PhieuXuatDAO {

    public List<PhieuXuat> getAll() {
        List<PhieuXuat> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM PhieuXuat ORDER BY NgayXuat DESC")) {
            while (rs.next()) {
                list.add(new PhieuXuat(
                    rs.getString("MaPX"),
                    rs.getString("NgayXuat"),
                    rs.getString("MaNV"),
                    rs.getDouble("TongTien")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void insert(PhieuXuat px) {
        String sql = "INSERT INTO PhieuXuat (MaPX,NgayXuat,MaNV,TongTien) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, px.getMaXuat());
            ps.setString(2, px.getNgayXuat());
            ps.setString(3, px.getMaNV());
            ps.setDouble(4, px.getTongTien());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void delete(String maPX) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM ChiTietPhieuXuat WHERE MaPX=?");
            ps1.setString(1, maPX); ps1.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM PhieuXuat WHERE MaPX=?");
            ps2.setString(1, maPX); ps2.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean existsById(String maPX) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM PhieuXuat WHERE MaPX=?")) {
            ps.setString(1, maPX);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public List<ChiTietPhieuXuat> getChiTiet(String maPX) {
        List<ChiTietPhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuXuat WHERE MaPX=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietPhieuXuat(
                    rs.getString("MaCTPX"),
                    rs.getInt("SoLuong"),
                    rs.getString("MaNL"),
                    rs.getDouble("DonGia"),
                    rs.getString("MaPX")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Insert chi tiết — trigger DB sẽ tự trừ kho & cộng tổng tiền
    public void insertChiTiet(ChiTietPhieuXuat ct) throws SQLException {
        String sql = "INSERT INTO ChiTietPhieuXuat (MaCTPX,SoLuong,MaNL,DonGia,MaPX) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaCTPX());
            ps.setInt(2, ct.getSoLuong());
            ps.setString(3, ct.getMaNL());
            ps.setDouble(4, ct.getDonGia());
            ps.setString(5, ct.getMaPX());
            ps.executeUpdate();
        }
        // SQLException được ném ra để GUI bắt thông báo "Không đủ hàng trong kho"
    }

    public String genMaPX() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT MAX(MaPX) FROM PhieuXuat")) {
            if (rs.next() && rs.getString(1) != null) {
                int num = Integer.parseInt(rs.getString(1).replace("PX", "")) + 1;
                return String.format("PX%02d", num);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "PX01";
    }

    public String genMaCTPX() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT MAX(MaCTPX) FROM ChiTietPhieuXuat")) {
            if (rs.next() && rs.getString(1) != null) {
                int num = Integer.parseInt(rs.getString(1).replace("CTPX", "")) + 1;
                return String.format("CTPX%02d", num);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "CTPX01";
    }
}