package dao;

import model.NhanVien;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class NhanVienDAO {

    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new NhanVien(
                    rs.getString("MNV"),
                    rs.getString("TenNV"),
                    rs.getString("SDT"),
                    rs.getString("Email"),
                    rs.getString("NgaySinh"),
                    rs.getString("GioiTinh"),
                    rs.getString("ChucVu")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MNV,TenNV,SDT,Email,NgaySinh,GioiTinh,ChucVu) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getSdt());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getNgaySinh());
            ps.setString(6, nv.getGioiTinh());
            ps.setString(7, nv.getChucVu());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNV=?,SDT=?,Email=?,NgaySinh=?,GioiTinh=?,ChucVu=? WHERE MNV=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSdt());
            ps.setString(3, nv.getEmail());
            ps.setString(4, nv.getNgaySinh());
            ps.setString(5, nv.getGioiTinh());
            ps.setString(6, nv.getChucVu());
            ps.setString(7, nv.getMaNV());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void delete(String maNV) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM NhanVien WHERE MNV=?")) {
            ps.setString(1, maNV);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean existsById(String maNV) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM NhanVien WHERE MNV=?")) {
            ps.setString(1, maNV);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}