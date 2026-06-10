package util;

import java.sql.Connection;
import java.sql.Statement;

public class DbMigrate {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                System.out.println("Migrating database views with aliases...");
                
                // Drop old views
                try {
                    stmt.execute("DROP VIEW IF EXISTS VW_TonKho");
                    System.out.println("Dropped view VW_TonKho.");
                } catch (Exception e) {}

                try {
                    stmt.execute("DROP VIEW IF EXISTS VW_ChiTietPhieuNhap");
                    System.out.println("Dropped view VW_ChiTietPhieuNhap.");
                } catch (Exception e) {}

                try {
                    stmt.execute("DROP VIEW IF EXISTS VW_ChiTietPhieuXuat");
                    System.out.println("Dropped view VW_ChiTietPhieuXuat.");
                } catch (Exception e) {}

                // Create new views referencing Donvi AS DonViTinh
                String vwTonKho = 
                    "CREATE VIEW VW_TonKho AS " +
                    "SELECT " +
                    "    NL.MaNL, " +
                    "    NL.TenNL, " +
                    "    NL.Donvi AS DonViTinh, " +
                    "    NL.SoLuong AS TonKhoHienTai, " +
                    "    K.MaKho, " +
                    "    K.TenKho, " +
                    "    CTPN.MaCTPN AS MaLo, " +
                    "    PN.NgayNhap AS NgayNhapLo, " +
                    "    CTPN.SoLuong AS SoLuongNhapLo, " +
                    "    CTPN.HanSuDung, " +
                    "    CASE " +
                    "        WHEN CTPN.HanSuDung IS NULL THEN N'Chưa cập nhật' " +
                    "        WHEN CTPN.HanSuDung < CAST(GETDATE() AS DATE) THEN N'Hết hạn' " +
                    "        WHEN CTPN.HanSuDung <= DATEADD(DAY, 30, GETDATE()) THEN N'Sắp hết hạn' " +
                    "        ELSE N'Còn hạn' " +
                    "    END AS TrangThaiHan " +
                    "FROM NguyenLieu NL " +
                    "JOIN Kho K ON NL.MaKho = K.MaKho " +
                    "LEFT JOIN ChiTietPhieuNhap CTPN ON NL.MaNL = CTPN.MaNL " +
                    "LEFT JOIN PhieuNhap PN ON CTPN.MaPN = PN.MaPN;";
                
                String vwChiTietPhieuNhap = 
                    "CREATE VIEW VW_ChiTietPhieuNhap AS " +
                    "SELECT " +
                    "    CTPN.MaCTPN, " +
                    "    PN.MaPN, " +
                    "    PN.NgayNhap, " +
                    "    NCC.TenNCC, " +
                    "    NV.TenNV, " +
                    "    NL.MaNL, " +
                    "    NL.TenNL, " +
                    "    NL.Donvi AS DonViTinh, " +
                    "    CTPN.SoLuong, " +
                    "    CTPN.DonGia, " +
                    "    CTPN.SoLuong * CTPN.DonGia AS ThanhTien, " +
                    "    CTPN.HanSuDung, " +
                    "    CASE " +
                    "        WHEN CTPN.HanSuDung IS NULL THEN N'Chưa cập nhật' " +
                    "        WHEN CTPN.HanSuDung < CAST(GETDATE() AS DATE) THEN N'Hết hạn' " +
                    "        WHEN CTPN.HanSuDung <= DATEADD(DAY, 30, CAST(GETDATE() AS DATE)) THEN N'Sắp hết hạn' " +
                    "        ELSE N'Còn hạn' " +
                    "    END AS TrangThaiHan, " +
                    "    PN.TongTien " +
                    "FROM ChiTietPhieuNhap CTPN " +
                    "JOIN NguyenLieu NL ON CTPN.MaNL = NL.MaNL " +
                    "JOIN PhieuNhap PN ON CTPN.MaPN = PN.MaPN " +
                    "JOIN NhaCungCap NCC ON PN.MaNCC = NCC.MaNCC " +
                    "JOIN NhanVien NV ON PN.MaNV = NV.MaNV;";

                String vwChiTietPhieuXuat = 
                    "CREATE VIEW VW_ChiTietPhieuXuat AS " +
                    "SELECT " +
                    "    CTPX.MaCTPX, " +
                    "    PX.MaPX, " +
                    "    PX.NgayXuat, " +
                    "    NV.TenNV, " +
                    "    NL.MaNL, " +
                    "    NL.TenNL, " +
                    "    NL.Donvi AS DonViTinh, " +
                    "    K.MaKho, " +
                    "    K.TenKho, " +
                    "    CTPX.SoLuong, " +
                    "    CTPX.DonGia, " +
                    "    CTPX.SoLuong * CTPX.DonGia AS ThanhTien, " +
                    "    PX.TongTien " +
                    "FROM ChiTietPhieuXuat CTPX " +
                    "JOIN NguyenLieu NL ON CTPX.MaNL = NL.MaNL " +
                    "JOIN PhieuXuat PX ON CTPX.MaPX = PX.MaPX " +
                    "JOIN NhanVien NV ON PX.MaNV = NV.MaNV " +
                    "JOIN Kho K ON NL.MaKho = K.MaKho;";

                stmt.execute(vwTonKho);
                System.out.println("Created view VW_TonKho successfully.");
                stmt.execute(vwChiTietPhieuNhap);
                System.out.println("Created view VW_ChiTietPhieuNhap successfully.");
                stmt.execute(vwChiTietPhieuXuat);
                System.out.println("Created view VW_ChiTietPhieuXuat successfully.");

                System.out.println("Migration of views complete!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
