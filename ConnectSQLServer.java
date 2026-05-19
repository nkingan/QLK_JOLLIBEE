import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectSQLServer {

    private static final String url =
        "jdbc:sqlserver://DESKTOP-60DM5F0\\SQLEXPRESS;"
        + "databaseName=QuanLyKhoJollibee;"
        + "user=sa;"
        + "password=260226;"
        + "encrypt=true;"
        + "trustServerCertificate=true;";

    public static void main(String[] args) {

        try {
            Connection conn = DriverManager.getConnection(url);

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