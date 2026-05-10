// package dao;

// import model.NhaCungCap;
// import util.DBConnection;

// import java.sql.*;
// import java.util.*;

// public class NhaCungCapDAO implements CrudDAO<NhaCungCap> {

//     public void insert(NhaCungCap ncc) {
//         String sql = "INSERT INTO NhaCungCap VALUES (?, ?, ?, ?, ?)";

//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement(sql)) {

//             ps.setString(1, ncc.getMaNCC());
//             ps.setString(2, ncc.getTenNCC());
//             ps.setString(3, ncc.getDiaChi());
//             ps.setString(4, ncc.getSdt());
//             ps.setString(5, ncc.getEmail());

//             ps.executeUpdate();
//         } catch (Exception e) { e.printStackTrace(); }
//     }

//     public List<NhaCungCap> getAll() {
//         List<NhaCungCap> list = new ArrayList<>();
//         String sql = "SELECT * FROM NhaCungCap";

//         try (Connection conn = DBConnection.getConnection();
//              Statement st = conn.createStatement();
//              ResultSet rs = st.executeQuery(sql)) {

//             while (rs.next()) {
//                 NhaCungCap ncc = new NhaCungCap();
//                 ncc.setMaNCC(rs.getString("MaNCC"));
//                 ncc.setTenNCC(rs.getString("TenNCC"));
//                 list.add(ncc);
//             }
//         } catch (Exception e) { e.printStackTrace(); }

//         return list;
//     }

//     public void update(NhaCungCap t) {}
//     public void delete(String id) {}
//     public NhaCungCap findById(String id) { return null; }
// }
package dao;

import model.NhaCungCap;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class NhaCungCapDAO {

    public List<NhaCungCap> getAll() {
        List<NhaCungCap> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM NhaCungCap")) {
            while (rs.next()) {
                list.add(new NhaCungCap(
                    rs.getString("MaNCC"),
                    rs.getString("TenNCC"),
                    rs.getString("DiaChi"),
                    rs.getString("SDT"),
                    rs.getString("Email")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void insert(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap (MaNCC,TenNCC,DiaChi,SDT,Email) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getSdt());
            ps.setString(5, ncc.getEmail());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void update(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET TenNCC=?,DiaChi=?,SDT=?,Email=? WHERE MaNCC=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getDiaChi());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getMaNCC());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void delete(String maNCC) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM NhaCungCap WHERE MaNCC=?")) {
            ps.setString(1, maNCC);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean existsById(String maNCC) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM NhaCungCap WHERE MaNCC=?")) {
            ps.setString(1, maNCC);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}