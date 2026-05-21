package dao;

import model.TonKho;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TonKhoDAO {

    public List<TonKho> getAllTonKho() {
        List<TonKho> list = new ArrayList<>();
        String sql = "SELECT MaNL, TenNL, DonViTinh, TonKhoHienTai, MaKho, TenKho, MaLo, NgayNhapLo, SoLuongNhapLo, HanSuDung, TrangThaiHan FROM VW_TonKho";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                TonKho tk = new TonKho(
                        rs.getString("MaNL"),
                        rs.getString("TenNL"),
                        rs.getString("DonViTinh"),
                        rs.getInt("TonKhoHienTai"),
                        rs.getString("MaKho"),
                        rs.getString("TenKho"),
                        rs.getString("MaLo"),
                        rs.getDate("NgayNhapLo"),
                        rs.getInt("SoLuongNhapLo"),
                        rs.getDate("HanSuDung"),
                        rs.getString("TrangThaiHan")
                );
                list.add(tk);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tại hàm getAllTonKho():");
            e.printStackTrace();
        }
        return list;
    }

    
    // LỌC TỒN KHO THEO TRẠNG THÁI HẠN 
    public List<TonKho> getByTrangThaiHan(String trangThai) {
        List<TonKho> list = new ArrayList<>();
        String sql = "SELECT * FROM VW_TonKho WHERE TrangThaiHan = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new TonKho(
                            rs.getString("MaNL"),
                            rs.getString("TenNL"),
                            rs.getString("DonViTinh"),
                            rs.getInt("TonKhoHienTai"),
                            rs.getString("MaKho"),
                            rs.getString("TenKho"),
                            rs.getString("MaLo"),
                            rs.getDate("NgayNhapLo"),
                            rs.getInt("SoLuongNhapLo"),
                            rs.getDate("HanSuDung"),
                            rs.getString("TrangThaiHan")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lọc theo trạng thái hạn:");
            e.printStackTrace();
        }
        return list;
    }


    public int getTongTonByMaNL(String maNL) {
       
        throw new UnsupportedOperationException("Unimplemented method 'getTongTonByMaNL'");
    }
}