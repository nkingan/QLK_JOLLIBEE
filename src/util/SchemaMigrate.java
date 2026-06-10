package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SchemaMigrate {
    public static void main(String[] args) {
        System.out.println("Bắt đầu di chuyển lược đồ cơ sở dữ liệu (Schema Migration)...");
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            if (conn != null) {
                // 1. Cập nhật bảng NguyenLieu
                System.out.println("Đang kiểm tra và cập nhật bảng NguyenLieu...");
                // Giữ nguyên cột DonViTinh không đổi tên thành Donvi

                // Thêm cột Gianhap nếu thiếu
                String addGiaNhapSql = 
                    "IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('NguyenLieu') AND name = 'Gianhap') " +
                    "BEGIN " +
                    "    ALTER TABLE NguyenLieu ADD Gianhap int NULL; " +
                    "    PRINT 'Đã thêm cột Gianhap vào bảng NguyenLieu'; " +
                    "END";
                stmt.execute(addGiaNhapSql);

                // Xóa cột Anh nếu tồn tại (dự án không dùng dữ liệu ảnh)
                String dropAnhSql = 
                    "IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('NguyenLieu') AND name = 'Anh') " +
                    "BEGIN " +
                    "    ALTER TABLE NguyenLieu DROP COLUMN Anh; " +
                    "    PRINT 'Đã xóa cột Anh khỏi bảng NguyenLieu'; " +
                    "END";
                stmt.execute(dropAnhSql);

                // 2. Cập nhật bảng Kho
                System.out.println("Đang kiểm tra và cập nhật bảng Kho...");
                
                String addDiaChiKhoSql = 
                    "IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Kho') AND name = 'DiaChi') " +
                    "BEGIN " +
                    "    ALTER TABLE Kho ADD DiaChi nvarchar(255) NULL; " +
                    "    PRINT 'Đã thêm cột DiaChi vào bảng Kho'; " +
                    "END";
                stmt.execute(addDiaChiKhoSql);

                String addSucChuaKhoSql = 
                    "IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Kho') AND name = 'SucChua') " +
                    "BEGIN " +
                    "    ALTER TABLE Kho ADD SucChua int NULL; " +
                    "    PRINT 'Đã thêm cột SucChua vào bảng Kho'; " +
                    "END";
                stmt.execute(addSucChuaKhoSql);

                String addGhiChuKhoSql = 
                    "IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Kho') AND name = 'GhiChu') " +
                    "BEGIN " +
                    "    ALTER TABLE Kho ADD GhiChu nvarchar(255) NULL; " +
                    "    PRINT 'Đã thêm cột GhiChu vào bảng Kho'; " +
                    "END";
                stmt.execute(addGhiChuKhoSql);

                // 3. Truy vấn độ dài và kiểu dữ liệu chính xác của NguyenLieu.MaNL
                System.out.println("Đang truy vấn độ dài chính xác của NguyenLieu.MaNL...");
                String getColumnSpecSql = 
                    "SELECT DATA_TYPE, CHARACTER_MAXIMUM_LENGTH " +
                    "FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_NAME = 'NguyenLieu' AND COLUMN_NAME = 'MaNL'";
                
                String maNLSpec = "varchar(50)"; // Mặc định nếu không tìm thấy
                try (ResultSet rs = stmt.executeQuery(getColumnSpecSql)) {
                    if (rs.next()) {
                        String dataType = rs.getString("DATA_TYPE");
                        int maxLen = rs.getInt("CHARACTER_MAXIMUM_LENGTH");
                        maNLSpec = dataType + "(" + (maxLen > 0 ? maxLen : 50) + ")";
                        System.out.println("-> Tìm thấy định nghĩa MaNL trong DB: " + maNLSpec);
                    }
                }

                // 4. Tạo bảng HaoHut nếu chưa có
                System.out.println("Đang kiểm tra và tạo bảng HaoHut...");
                String createTableHaoHutSql = 
                    "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'HaoHut') " +
                    "BEGIN " +
                    "    CREATE TABLE HaoHut ( " +
                    "        MaHH varchar(50) PRIMARY KEY, " +
                    "        MaNL " + maNLSpec + " FOREIGN KEY REFERENCES NguyenLieu(MaNL), " +
                    "        SoLuongHeThong int, " +
                    "        SoLuongThucTe int, " +
                    "        SoLuongHaoHut int, " +
                    "        PhanTramHaoHut decimal(5,2), " +
                    "        LyDo nvarchar(255), " +
                    "        NgayGhiNhan date " +
                    "    ); " +
                    "    PRINT 'Đã tạo bảng HaoHut thành công!'; " +
                    "END";
                stmt.execute(createTableHaoHutSql);

                // 5. Tạo bảng CanhBaoTonKho
                System.out.println("Đang kiểm tra và tạo bảng CanhBaoTonKho...");
                String createTableCanhBaoSql = 
                    "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'CanhBaoTonKho') " +
                    "BEGIN " +
                    "    CREATE TABLE CanhBaoTonKho ( " +
                    "        MaCanhBao INT IDENTITY(1,1) PRIMARY KEY, " +
                    "        MaNL " + maNLSpec + " FOREIGN KEY REFERENCES NguyenLieu(MaNL), " +
                    "        TenNL nvarchar(100), " +
                    "        SoLuongHienTai INT, " +
                    "        NguongCanhBao INT, " +
                    "        ThoiGianGhiNhan DATETIME DEFAULT GETDATE(), " +
                    "        TrangThai nvarchar(50) DEFAULT N'Chưa xử lý' " +
                    "    ); " +
                    "    PRINT 'Đã tạo bảng CanhBaoTonKho thành công!'; " +
                    "END";
                stmt.execute(createTableCanhBaoSql);

                // 6. Tạo trigger TRG_KiemTraTonKhoThap
                System.out.println("Đang kiểm tra và tạo Trigger TRG_KiemTraTonKhoThap...");
                stmt.execute("IF EXISTS (SELECT * FROM sys.triggers WHERE name = 'TRG_KiemTraTonKhoThap') DROP TRIGGER TRG_KiemTraTonKhoThap;");
                
                String createTriggerSql = 
                    "CREATE TRIGGER TRG_KiemTraTonKhoThap " +
                    "ON NguyenLieu " +
                    "AFTER UPDATE, INSERT " +
                    "AS " +
                    "BEGIN " +
                    "    SET NOCOUNT ON; " +
                    "    DECLARE @NguongCanhBao INT = 15; " +
                    "    INSERT INTO CanhBaoTonKho (MaNL, TenNL, SoLuongHienTai, NguongCanhBao) " +
                    "    SELECT i.MaNL, i.TenNL, i.SoLuong, @NguongCanhBao " +
                    "    FROM inserted i " +
                    "    WHERE i.SoLuong <= @NguongCanhBao " +
                    "      AND NOT EXISTS ( " +
                    "          SELECT 1 " +
                    "          FROM CanhBaoTonKho cb " +
                    "          WHERE cb.MaNL = i.MaNL AND cb.TrangThai = N'Chưa xử lý' " +
                    "      ); " +
                    "END;";
                stmt.execute(createTriggerSql);
                System.out.println("Đã tạo trigger TRG_KiemTraTonKhoThap thành công!");

                System.out.println("Hoàn tất di chuyển lược đồ (Schema Migration) thành công!");
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình di chuyển lược đồ:");
            e.printStackTrace();
        }
    }
}
