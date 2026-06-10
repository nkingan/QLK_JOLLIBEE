package util;

import java.sql.Connection;
import java.sql.Statement;

public class RenameColumn {
    public static void main(String[] args) {
        System.out.println("Running column rename query...");
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                // Check if we need to rename by catching potential exception if already renamed
                try {
                    stmt.execute("EXEC sp_rename 'NguyenLieu.DonViTinh', 'Donvi', 'COLUMN';");
                    System.out.println("Column DonViTinh successfully renamed to Donvi.");
                } catch (Exception ex) {
                    System.out.println("Could not rename or already renamed: " + ex.getMessage());
                }
                
                // Run DbMigrate to re-create the views with the new column name
                System.out.println("Re-running DbMigrate to update views...");
                DbMigrate.main(new String[0]);
                System.out.println("Migration complete.");
            } else {
                System.err.println("Could not establish connection.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
