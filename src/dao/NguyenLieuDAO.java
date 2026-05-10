// package dao;

// import util.DBConnection;
// import java.sql.*;
// import java.util.*;

// public class NguyenLieuDAO {

//     public List<Vector> getAll() {
//         List<Vector> list = new ArrayList<>();
//         String sql = "SELECT * FROM NguyenLieu";

//         try (Connection conn = DBConnection.getConnection();
//              Statement st = conn.createStatement();
//              ResultSet rs = st.executeQuery(sql)) {

//             while (rs.next()) {
//                 Vector row = new Vector();
//                 row.add(rs.getString("MaNL"));
//                 row.add(rs.getString("TenNL"));
//                 row.add(rs.getString("DonViTinh"));
//                 row.add(rs.getInt("SoLuong"));
//                 row.add(rs.getString("MaKho"));
//                 list.add(row);
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return list;
//     }

//     public void insertOrUpdate(String id, String name, String unit, int qty, String maKho) {
//         try (Connection conn = DBConnection.getConnection()) {

//             String check = "SELECT SoLuong FROM NguyenLieu WHERE MaNL=?";
//             PreparedStatement ps = conn.prepareStatement(check);
//             ps.setString(1, id);
//             ResultSet rs = ps.executeQuery();

//             if (rs.next()) {
//                 int newQty = rs.getInt("SoLuong") + qty;

//                 String update = "UPDATE NguyenLieu SET SoLuong=?, TenNL=?, DonViTinh=?, MaKho=? WHERE MaNL=?";
//                 ps = conn.prepareStatement(update);
//                 ps.setInt(1, newQty);
//                 ps.setString(2, name);
//                 ps.setString(3, unit);
//                 ps.setString(4, maKho);
//                 ps.setString(5, id);
//                 ps.executeUpdate();
//             } else {
//                 String insert = "INSERT INTO NguyenLieu VALUES (?,?,?,?,?)";
//                 ps = conn.prepareStatement(insert);
//                 ps.setString(1, id);
//                 ps.setString(2, name);
//                 ps.setString(3, unit);
//                 ps.setInt(4, qty);
//                 ps.setString(5, maKho);
//                 ps.executeUpdate();
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     public void delete(String id) {
//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement("DELETE FROM NguyenLieu WHERE MaNL=?")) {
//             ps.setString(1, id);
//             ps.executeUpdate();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
package dao;

import model.NguyenLieu;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class NguyenLieuDAO {

    public List<NguyenLieu> getAll() {
        List<NguyenLieu> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM NguyenLieu")) {
            while (rs.next()) {
                list.add(new NguyenLieu(
                    rs.getString("MaNL"),
                    rs.getString("TenNL"),
                    rs.getString("DonViTinh"),
                    rs.getInt("SoLuong"),
                    rs.getString("MaKho")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void insert(NguyenLieu nl) {
        String sql = "INSERT INTO NguyenLieu (MaNL,TenNL,DonViTinh,SoLuong,MaKho) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nl.getMaNL());
            ps.setString(2, nl.getTenNL());
            ps.setString(3, nl.getDonViTinh());
            ps.setInt(4, nl.getSoLuong());
            ps.setString(5, nl.getMaKho());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void update(NguyenLieu nl) {
        String sql = "UPDATE NguyenLieu SET TenNL=?,DonViTinh=?,SoLuong=?,MaKho=? WHERE MaNL=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nl.getTenNL());
            ps.setString(2, nl.getDonViTinh());
            ps.setInt(3, nl.getSoLuong());
            ps.setString(4, nl.getMaKho());
            ps.setString(5, nl.getMaNL());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void delete(String maNL) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM NguyenLieu WHERE MaNL=?")) {
            ps.setString(1, maNL);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean existsById(String maNL) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM NguyenLieu WHERE MaNL=?")) {
            ps.setString(1, maNL);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}