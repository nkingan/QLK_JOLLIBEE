package dao;

import model.NhaCungCap;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {

    public List<NhaCungCap> getAll() {

        List<NhaCungCap> list = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM NhaCungCap")
        ) {

            while (rs.next()) {

                NhaCungCap ncc = new NhaCungCap(
                        rs.getString("MaNCC"),
                        rs.getString("TenNCC"),
                        rs.getString("DiaChi"),
                        rs.getString("SDT"),
                        rs.getString("Email")
                );

                list.add(ncc);
            }

        } catch (Exception e) {

            System.out.println("Lỗi lấy danh sách nhà cung cấp!");
            e.printStackTrace();
        }

        return list;
    }

    public void insert(NhaCungCap ncc) {

        String sql = """
                INSERT INTO NhaCungCap
                (MaNCC, TenNCC, DiaChi, SDT, Email)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getSdt());
            ps.setString(5, ncc.getEmail());

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println("Lỗi thêm nhà cung cấp!");
            e.printStackTrace();
        }
    }

    public void update(NhaCungCap ncc) {

        String sql = """
                UPDATE NhaCungCap
                SET TenNCC=?, DiaChi=?, SDT=?, Email=?
                WHERE MaNCC=?
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getDiaChi());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getMaNCC());

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println("Lỗi cập nhật nhà cung cấp!");
            e.printStackTrace();
        }
    }

    public void delete(String maNCC) {

        String sql = "DELETE FROM NhaCungCap WHERE MaNCC=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, maNCC);

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println("Lỗi xóa nhà cung cấp!");
            e.printStackTrace();
        }
    }

    public boolean existsById(String maNCC) {

        String sql = "SELECT 1 FROM NhaCungCap WHERE MaNCC=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, maNCC);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            System.out.println("Lỗi kiểm tra mã nhà cung cấp!");
            e.printStackTrace();
        }

        return false;
    }
}