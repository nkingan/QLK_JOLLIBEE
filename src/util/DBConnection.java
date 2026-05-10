// // package util;
// // import java.sql.Connection;
// // import java.sql.DriverManager;

// // public class DBConnection {
// //     // Thông số của SQL Server (Dựa trên file ConnectSQLServer đã chạy được)
// //     private static String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyKhoJollibee;encrypt=true;trustServerCertificate=true;";
// //     private static String USER_NAME = "sa";
// //     private static String PASSWORD = "123456";

// //     /**
// //      * Hàm kết nối dùng chung cho toàn hệ thống
// //      * @return connection
// //      */
// //     public static Connection getConnection() {
// //         Connection conn = null;
// //         try {
// //             // Bước 1: Khai báo Driver của SQL Server 
// //             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
// //             // Bước 2: Thiết lập kết nối
// //             conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
// //             System.out.println("Kết nối SQL Server thành công!");
// //         } catch (Exception ex) {
// //             System.out.println("Kết nối thất bại! Hãy kiểm tra lại Port 1433 hoặc SQL Service.");
// //             ex.printStackTrace();
// //         }
// //         return conn;
// //     }
// // }
// package util;   

// import java.sql.Connection;
// import java.sql.DriverManager;

// public class DBConnection {
//     private static String DB_URL = 
//         "jdbc:sqlserver://localhost:1433;" +
//         "databaseName=QuanLyKhoJollibee;" +
//         "encrypt=true;trustServerCertificate=true;";
//     private static String USER_NAME = "sa";
//     private static String PASSWORD  = "123456";

//     public static Connection getConnection() {
//         try {
//             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//             return DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
//         } catch (Exception ex) {
//             ex.printStackTrace();
//             return null;
//         }
//     }
// }
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