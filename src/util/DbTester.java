package util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DbTester {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Connection successful!");
                DatabaseMetaData dbmd = conn.getMetaData();
                String[] types = {"TABLE", "VIEW"};
                ResultSet rs = dbmd.getTables("QuanLyKhoJollibee", "dbo", "%", types);
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableType = rs.getString("TABLE_TYPE");
                    System.out.println("\n" + tableType + ": " + tableName);
                    
                    // Let's print columns and count for this table
                    try (Statement stmt = conn.createStatement()) {
                        int rowCount = 0;
                        try (ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
                            if (rsCount.next()) {
                                rowCount = rsCount.getInt(1);
                            }
                        }
                        System.out.println("  Rows: " + rowCount);
                        
                        try (ResultSet rows = stmt.executeQuery("SELECT TOP 0 * FROM " + tableName)) {
                            ResultSetMetaData rsmd = rows.getMetaData();
                            int colCount = rsmd.getColumnCount();
                            System.out.print("  Columns: ");
                            for (int i = 1; i <= colCount; i++) {
                                System.out.print(rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ")" + (i < colCount ? ", " : ""));
                            }
                            System.out.println();
                        }
                        
                        // Print top 5 rows for NguyenLieu table specifically
                        if ("NguyenLieu".equals(tableName)) {
                            System.out.println("  Sample Rows:");
                            try (ResultSet rsSample = stmt.executeQuery("SELECT TOP 5 MaNL, TenNL, SoLuong FROM NguyenLieu")) {
                                while (rsSample.next()) {
                                    System.out.println("    " + rsSample.getString(1) + " | " + rsSample.getString(2) + " | " + rsSample.getInt(3));
                                }
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("  Error reading columns/count: " + ex.getMessage());
                    }
                }
                rs.close();
                conn.close();
            } else {
                System.out.println("Connection failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
