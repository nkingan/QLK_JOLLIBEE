package dao;

import model.NguyenLieu;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguyenLieuDAO {

    public List<NguyenLieu> getAll() {

        List<NguyenLieu> list = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM NguyenLieu")
        ) {

            while (rs.next()) {

                NguyenLieu nl = new NguyenLieu(
                        rs.getString("MaNL"),
                        rs.getString("TenNL"),
                        rs.getString("DonViTinh"),
                        rs.getInt("SoLuong"),
                        rs.getString("MaKho")
                );

                list.add(nl);
            }

        } catch (Exception e) {

            System.out.println("Lỗi lấy danh sách nguyên liệu!");
            e.printStackTrace();
        }

        return list;
    }

    public void insert(NguyenLieu nl) {

        String sql = """
                INSERT INTO NguyenLieu
                (MaNL, TenNL, DonViTinh, SoLuong, MaKho)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, nl.getMaNL());
            ps.setString(2, nl.getTenNL());
            ps.setString(3, nl.getDonViTinh());
            ps.setInt(4, nl.getSoLuong());
            ps.setString(5, nl.getMaKho());

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println("Lỗi thêm nguyên liệu!");
            e.printStackTrace();
        }
    }

    public void update(NguyenLieu nl) {

        String sql = """
                UPDATE NguyenLieu
                SET TenNL=?, DonViTinh=?, SoLuong=?, MaKho=?
                WHERE MaNL=?
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, nl.getTenNL());
            ps.setString(2, nl.getDonViTinh());
            ps.setInt(3, nl.getSoLuong());
            ps.setString(4, nl.getMaKho());
            ps.setString(5, nl.getMaNL());

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println("Lỗi cập nhật nguyên liệu!");
            e.printStackTrace();
        }
    }

    public void delete(String maNL) {

        String sql = "DELETE FROM NguyenLieu WHERE MaNL=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, maNL);

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println("Lỗi xóa nguyên liệu!");
            e.printStackTrace();
        }
    }

    public boolean existsById(String maNL) {

        String sql = "SELECT 1 FROM NguyenLieu WHERE MaNL=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, maNL);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            System.out.println("Lỗi kiểm tra mã nguyên liệu!");
            e.printStackTrace();
        }

        return false;
    }
}