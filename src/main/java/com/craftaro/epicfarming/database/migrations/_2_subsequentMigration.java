package com.craftaro.epicfarming.database.migrations;

import com.craftaro.core.database.DataMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class _2_subsequentMigration extends DataMigration {
    public _2_subsequentMigration() {
        super(2);
    }

    @Override
    public void migrate(Connection connection, String tablePrefix) throws SQLException {
        // First, fix any invalid Base64 data in the items table.
        fixInvalidData(connection, tablePrefix);

        // Then, alter the column to TEXT.
        try (Statement statement = connection.createStatement()) {
            try {
                statement.execute("ALTER TABLE " + tablePrefix + "items ALTER COLUMN item SET DATA TYPE TEXT");
            } catch (SQLException e) {
                System.err.println("H2 syntax failed, trying with MySQL...");
                statement.execute("ALTER TABLE " + tablePrefix + "items MODIFY COLUMN item TEXT");
            }
        }
    }

    /**
     * Scans the items table and replaces any invalid Base64 data with an empty string.
     */
    private void fixInvalidData(Connection connection, String tablePrefix) throws SQLException {
        String selectSQL = "SELECT farm_id, item FROM " + tablePrefix + "items";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                String item = rs.getString("item");
                // Check if this data is valid Base64.
                if (!isValidBase64(item)) {
                    // Update the row: use both farm_id and the current item value as conditions.
                    updateInvalidItem(connection, tablePrefix, rs.getInt("farm_id"), item);
                }
            }
        }
    }

    /**
     * Returns true if the provided string is a valid Base64 encoded string.
     * An empty string is considered valid.
     */
    private boolean isValidBase64(String data) {
        if (data == null || data.isEmpty()) {
            return true;
        }
        // Check if the length is a multiple of 4.
        if (data.length() % 4 != 0) {
            return false;
        }
        try {
            Base64.getDecoder().decode(data);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Updates a specific row by replacing the invalid item data with an empty string.
     */
    private void updateInvalidItem(Connection connection, String tablePrefix, int farmId, String item) throws SQLException {
        String updateSQL = "UPDATE " + tablePrefix + "items SET item = '' WHERE farm_id = ? AND item = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setInt(1, farmId);
            pstmt.setString(2, item);
            pstmt.executeUpdate();
        }
    }
}
