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
                
                // Đổi tên DonViTinh thành Donvi bằng sp_rename nếu Donvi chưa tồn tại
                String renameDonviSql = 
                    "IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('NguyenLieu') AND name = 'DonViTinh') " +
                    "AND NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('NguyenLieu') AND name = 'Donvi') " +
                    "BEGIN " +
                    "    EXEC sp_rename 'NguyenLieu.DonViTinh', 'Donvi', 'COLUMN'; " +
                    "    PRINT 'Đã đổi tên cột DonViTinh thành Donvi'; " +
                    "END";
                stmt.execute(renameDonviSql);

                // Thêm cột Gianhap nếu thiếu
                String addGiaNhapSql = 
                    "IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('NguyenLieu') AND name = 'Gianhap') " +
                    "BEGIN " +
                    "    ALTER TABLE NguyenLieu ADD Gianhap int NULL; " +
                    "    PRINT 'Đã thêm cột Gianhap vào bảng NguyenLieu'; " +
                    "END";
                stmt.execute(addGiaNhapSql);

                // Thêm cột Anh nếu thiếu
                String addAnhSql = 
                    "IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('NguyenLieu') AND name = 'Anh') " +
                    "BEGIN " +
                    "    ALTER TABLE NguyenLieu ADD Anh varchar(255) NULL; " +
                    "    PRINT 'Đã thêm cột Anh vào bảng NguyenLieu'; " +
                    "END";
                stmt.execute(addAnhSql);

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

                System.out.println("Hoàn tất di chuyển lược đồ (Schema Migration) thành công!");
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình di chuyển lược đồ:");
            e.printStackTrace();
        }
    }
}
