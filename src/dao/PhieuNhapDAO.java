// package dao;

// import model.PhieuNhap;
// import model.ChiTietPhieuNhap;
// import util.DBConnection;
// import java.sql.*;
// import java.util.*;

// public class PhieuNhapDAO {

//     public List<PhieuNhap> getAll() {
//         List<PhieuNhap> list = new ArrayList<>();
//         try (Connection conn = DBConnection.getConnection();
//              Statement st = conn.createStatement();
//              ResultSet rs = st.executeQuery("SELECT * FROM PhieuNhap ORDER BY NgayNhap DESC")) {
//             while (rs.next()) {
//                 list.add(new PhieuNhap(
//                     rs.getString("MaPN"),
//                     rs.getString("NgayNhap"),
//                     rs.getString("MaNV"),
//                     rs.getString("MaNCC"),
//                     rs.getDouble("TongTien")
//                 ));
//             }
//         } catch (Exception e) { e.printStackTrace(); }
//         return list;
//     }

//     public void insert(PhieuNhap pn) {
//         String sql = "INSERT INTO PhieuNhap (MaPN,NgayNhap,MaNV,MaNCC,TongTien) VALUES (?,?,?,?,?)";
//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement(sql)) {
//             ps.setString(1, pn.getMaPN());
//             ps.setString(2, pn.getNgayNhap());
//             ps.setString(3, pn.getMaNV());
//             ps.setString(4, pn.getMaNCC());
//             ps.setDouble(5, pn.getTongTien());
//             ps.executeUpdate();
//         } catch (Exception e) { e.printStackTrace(); }
//     }

//     public void delete(String maPN) {
//         try (Connection conn = DBConnection.getConnection()) {
//             PreparedStatement ps1 = conn.prepareStatement("DELETE FROM ChiTietPhieuNhap WHERE MaPN=?");
//             ps1.setString(1, maPN); ps1.executeUpdate();
//             PreparedStatement ps2 = conn.prepareStatement("DELETE FROM PhieuNhap WHERE MaPN=?");
//             ps2.setString(1, maPN); ps2.executeUpdate();
//         } catch (Exception e) { e.printStackTrace(); }
//     }

//     public List<ChiTietPhieuNhap> getChiTiet(String maPN) {
//         List<ChiTietPhieuNhap> list = new ArrayList<>();
//         String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN=?";
//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement(sql)) {
//             ps.setString(1, maPN);
//             ResultSet rs = ps.executeQuery();
//             while (rs.next()) {
//                 list.add(new ChiTietPhieuNhap(
//                     rs.getString("MaCTPN"),
//                     rs.getInt("SoLuong"),
//                     rs.getString("MaNL"),
//                     rs.getDouble("DonGia"),
//                     rs.getString("MaPN")
//                 ));
//             }
//         } catch (Exception e) { e.printStackTrace(); }
//         return list;
//     }

//     // Trigger DB sẽ tự cộng kho & tổng tiền
//     public void insertChiTiet(ChiTietPhieuNhap ct) {
//         String sql = "INSERT INTO ChiTietPhieuNhap (MaCTPN,SoLuong,MaNL,DonGia,MaPN) VALUES (?,?,?,?,?)";
//         try (Connection conn = DBConnection.getConnection();
//              PreparedStatement ps = conn.prepareStatement(sql)) {
//             ps.setString(1, ct.getMaCTPN());
//             ps.setInt(2, ct.getSoLuong());
//             ps.setString(3, ct.getMaNL());
//             ps.setDouble(4, ct.getDonGia());
//             ps.setString(5, ct.getMaPN());
//             ps.executeUpdate();
//         } catch (Exception e) { e.printStackTrace(); }
//     }

//     public String genMaPN() {
//         try (Connection conn = DBConnection.getConnection();
//              Statement st = conn.createStatement();
//              ResultSet rs = st.executeQuery("SELECT MAX(MaPN) FROM PhieuNhap")) {
//             if (rs.next() && rs.getString(1) != null) {
//                 int num = Integer.parseInt(rs.getString(1).replace("PN", "")) + 1;
//                 return String.format("PN%02d", num);
//             }
//         } catch (Exception e) { e.printStackTrace(); }
//         return "PN01";
//     }

//     public String genMaCTPN() {
//         try (Connection conn = DBConnection.getConnection();
//              Statement st = conn.createStatement();
//              ResultSet rs = st.executeQuery("SELECT MAX(MaCTPN) FROM ChiTietPhieuNhap")) {
//             if (rs.next() && rs.getString(1) != null) {
//                 int num = Integer.parseInt(rs.getString(1).replace("CTPN", "")) + 1;
//                 return String.format("CTPN%02d", num);
//             }
//         } catch (Exception e) { e.printStackTrace(); }
//         return "CTPN01";
//     }
// }
package dao;

import model.PhieuNhap;
import model.ChiTietPhieuNhap;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class PhieuNhapDAO {

    public List<PhieuNhap> getAll() {
        List<PhieuNhap> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM PhieuNhap ORDER BY NgayNhap DESC")) {
            while (rs.next()) {
                list.add(new PhieuNhap(
                    rs.getString("MaPN"),
                    rs.getString("NgayNhap"),
                    rs.getString("MaNV"),
                    rs.getString("MaNCC"),
                    rs.getDouble("TongTien")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void insert(PhieuNhap pn) {
        String sql = "INSERT INTO PhieuNhap (MaPN,NgayNhap,MaNV,MaNCC,TongTien) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pn.getMaPN());
            ps.setString(2, pn.getNgayNhap());
            ps.setString(3, pn.getMaNV());
            ps.setString(4, pn.getMaNCC());
            ps.setDouble(5, pn.getTongTien());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void delete(String maPN) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM ChiTietPhieuNhap WHERE MaPN=?");
            ps1.setString(1, maPN); ps1.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM PhieuNhap WHERE MaPN=?");
            ps2.setString(1, maPN); ps2.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<ChiTietPhieuNhap> getChiTiet(String maPN) {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietPhieuNhap(
                    rs.getString("MaCTPN"),
                    rs.getInt("SoLuong"),
                    rs.getString("MaNL"),
                    rs.getDouble("DonGia"),
                    rs.getString("MaPN")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Trigger DB sẽ tự cộng kho & tổng tiền
    public void insertChiTiet(ChiTietPhieuNhap ct) {
        String sql = "INSERT INTO ChiTietPhieuNhap (MaCTPN,SoLuong,MaNL,DonGia,MaPN) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaCTPN());
            ps.setInt(2, ct.getSoLuong());
            ps.setString(3, ct.getMaNL());
            ps.setDouble(4, ct.getDonGia());
            ps.setString(5, ct.getMaPN());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public String genMaPN() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT MAX(MaPN) FROM PhieuNhap")) {
            if (rs.next() && rs.getString(1) != null) {
                int num = Integer.parseInt(rs.getString(1).replace("PN", "")) + 1;
                return String.format("PN%02d", num);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "PN01";
    }

    public String genMaCTPN() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT MAX(MaCTPN) FROM ChiTietPhieuNhap")) {
            if (rs.next() && rs.getString(1) != null) {
                int num = Integer.parseInt(rs.getString(1).replace("CTPN", "")) + 1;
                return String.format("CTPN%02d", num);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "CTPN01";
    }
}