package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Kho;
import util.DBConnection;

public class KhoDAO {

    public List<Kho> getAllKho() {
        List<Kho> list = new ArrayList<>();
        String sql = "SELECT MaKho, TenKho, MaNV, DiaChi, SucChua, GhiChu FROM Kho";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Kho(
                    rs.getString("MaKho"),
                    rs.getString("TenKho"),
                    rs.getString("MaNV"),
                    rs.getString("DiaChi") != null ? rs.getString("DiaChi") : "",
                    rs.getInt("SucChua"),
                    rs.getString("GhiChu") != null ? rs.getString("GhiChu") : ""
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addKho(Kho kho) {
        String sql = "INSERT INTO Kho (MaKho, TenKho, MaNV, DiaChi, SucChua, GhiChu) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, kho.getMaKho());
            pst.setString(2, kho.getTenKho());
            pst.setString(3, kho.getMaNV());
            pst.setString(4, kho.getDiaChi());
            pst.setInt(5, kho.getSucChua());
            pst.setString(6, kho.getGhiChu());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateKho(Kho kho) {
        String sql = "UPDATE Kho SET TenKho = ?, MaNV = ?, DiaChi = ?, SucChua = ?, GhiChu = ? WHERE MaKho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, kho.getTenKho());
            pst.setString(2, kho.getMaNV());
            pst.setString(3, kho.getDiaChi());
            pst.setInt(4, kho.getSucChua());
            pst.setString(5, kho.getGhiChu());
            pst.setString(6, kho.getMaKho());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteKho(String maKho) {
        String sql = "DELETE FROM Kho WHERE MaKho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maKho);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getIngredientCountByKho(String maKho) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM NguyenLieu WHERE MaKho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maKho);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public Kho getKhoById(String maKho) {
        String sql = "SELECT MaKho, TenKho, MaNV, DiaChi, SucChua, GhiChu FROM Kho WHERE MaKho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maKho);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Kho(
                        rs.getString("MaKho"),
                        rs.getString("TenKho"),
                        rs.getString("MaNV"),
                        rs.getString("DiaChi") != null ? rs.getString("DiaChi") : "",
                        rs.getInt("SucChua"),
                        rs.getString("GhiChu") != null ? rs.getString("GhiChu") : ""
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Auto-generate next code
    public synchronized String generateNextMaKho() {
        String latestCode = null;
        String sql = "SELECT TOP 1 MaKho FROM Kho WHERE MaKho LIKE 'K%' ORDER BY MaKho DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                latestCode = rs.getString("MaKho");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int number = 1;
        if (latestCode != null && latestCode.startsWith("K")) {
            try {
                number = Integer.parseInt(latestCode.substring(1)) + 1;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("K%02d", number);
    }
}
