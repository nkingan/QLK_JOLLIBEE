package util;

import java.sql.Connection;
import java.sql.Statement;

public class CreateHaoHutTable {
    public static void main(String[] args) {
        System.out.println("Checking and creating HaoHut table...");
        String sql = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'HaoHut')\n" +
                     "BEGIN\n" +
                     "    CREATE TABLE HaoHut (\n" +
                     "        MaHH VARCHAR(20) PRIMARY KEY,\n" +
                     "        NgayBaoCao DATETIME DEFAULT GETDATE(),\n" +
                     "        MaNL VARCHAR(20),\n" +
                     "        SoLuongHeThong INT,\n" +
                     "        SoLuongThucTe INT,\n" +
                     "        SoLuongHaoHut INT,\n" +
                     "        PhanTramHaoHut DECIMAL(5,2),\n" +
                     "        LyDo NVARCHAR(255),\n" +
                     "        MaNV VARCHAR(20),\n" +
                     "        CONSTRAINT FK_HaoHut_NL FOREIGN KEY (MaNL) REFERENCES NguyenLieu(MaNL),\n" +
                     "        CONSTRAINT FK_HaoHut_NV FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)\n" +
                     "    );\n" +
                     "    PRINT 'Table HaoHut created successfully.';\n" +
                     "END\n" +
                     "ELSE\n" +
                     "BEGIN\n" +
                     "    PRINT 'Table HaoHut already exists.';\n" +
                     "END;";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sql);
                System.out.println("HaoHut table verification completed successfully.");
            } else {
                System.err.println("Could not establish connection.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
