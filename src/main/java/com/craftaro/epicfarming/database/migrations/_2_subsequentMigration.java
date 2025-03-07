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
        // First, update only rows with invalid Base64 data.
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
     * Iterates over all rows in the items table and replaces any invalid Base64 data with an empty string.
     * Only rows where the data is invalid will be updated.
     */
    private void fixInvalidData(Connection connection, String tablePrefix) throws SQLException {
        String selectSQL = "SELECT id, item FROM " + tablePrefix + "items";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String item = rs.getString("item");

                if (!isValidBase64(item)) {
                    updateInvalidItem(connection, tablePrefix, id);
                }
            }
        }
    }

    /**
     * Checks if a string is a valid Base64-encoded value.
     * An empty or null string is considered valid.
     */
    private boolean isValidBase64(String data) {
        if (data == null || data.isEmpty()) {
            return true;
        }
        // Base64 strings should have a length that's a multiple of 4.
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
     * Updates a row (identified by its id) by replacing the invalid item data with an empty string.
     */
    private void updateInvalidItem(Connection connection, String tablePrefix, int id) throws SQLException {
        String updateSQL = "UPDATE " + tablePrefix + "items SET item = '' WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
