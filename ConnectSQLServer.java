

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectSQLServer {

    private static final String URL =
    "jdbc:sqlserver://localhost;"
    + "instanceName=SQLEXPRESS01;"
    + "databaseName=QuanLyKhoJollibee;"
    + "encrypt=true;"
    + "trustServerCertificate=true;";

    private static final String USER = "sa";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM NguyenLieu");

            while (rs.next()) {
                System.out.println(
                    rs.getString("MaNL") + " | " +
                    rs.getString("TenNL") + " | " +
                    rs.getString("DonViTinh") + " | " +
                    rs.getInt("SoLuong") + " | " +
                    rs.getString("MaKho")
                );
            }

            conn.close();

        } catch (Exception e) {
            System.out.println("Lỗi:");
            e.printStackTrace();
        }
    }
}