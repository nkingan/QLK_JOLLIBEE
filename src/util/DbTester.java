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
                    
                    // Let's print columns for this table
                    try (Statement stmt = conn.createStatement();
                         ResultSet rows = stmt.executeQuery("SELECT TOP 0 * FROM " + tableName)) {
                        ResultSetMetaData rsmd = rows.getMetaData();
                        int colCount = rsmd.getColumnCount();
                        System.out.print("  Columns: ");
                        for (int i = 1; i <= colCount; i++) {
                            System.out.print(rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ")" + (i < colCount ? ", " : ""));
                        }
                        System.out.println();
                    } catch (Exception ex) {
                        System.out.println("  Error reading columns: " + ex.getMessage());
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
