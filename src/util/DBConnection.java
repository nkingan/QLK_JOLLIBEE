
package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static String DB_URL =
        "jdbc:sqlserver://localhost:1433;" +
        "databaseName=QuanLyKhoJollibee;" +
        "encrypt=true;trustServerCertificate=true;";
    private static String USER_NAME = "sa";
    private static String PASSWORD  = "123456";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            System.out.println("Kết nối DB thành công!");
            return conn;
        } catch (Exception ex) {
            System.out.println("Kết nối DB thất bại: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}