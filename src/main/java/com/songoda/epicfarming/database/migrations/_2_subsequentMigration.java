package com.songoda.epicfarming.database.migrations;

import com.songoda.core.database.DataMigration;

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
        // handle rows with invalid Base64 data.
        wipeNonBase64Values(connection, tablePrefix);

        fixInvalidData(connection, tablePrefix);

        // Then, alter the column to TEXT.
        try (Statement statement = connection.createStatement()) {
            try {
                statement.execute("ALTER TABLE " + tablePrefix + "items ALTER COLUMN item SET DATA TYPE TEXT");
            } catch (SQLException e) {
                statement.execute("ALTER TABLE " + tablePrefix + "items MODIFY COLUMN item TEXT");
            }
        }
    }

    /**
     * Iterates over all rows in the items table and replaces any invalid Base64 data with an empty string.
     * Only rows where the data is invalid will be updated.
     */
    private void fixInvalidData(Connection connection, String tablePrefix) throws SQLException {
        if (hasIdColumn(connection, tablePrefix)) {
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
    }

    private void wipeNonBase64Values(Connection connection, String tablePrefix) throws SQLException {
        String tbl = tablePrefix + "items";
        try (Statement st = connection.createStatement()) {
            // MySQL / MariaDB path
            int rows = st.executeUpdate(
                    "UPDATE " + tbl + " SET item = '' " +
                            "WHERE item IS NOT NULL AND item REGEXP('[^A-Za-z0-9+/=]')");
            if (rows == 0) {
                // H2 or drivers that don’t support the REGEXP operator above
                st.executeUpdate(
                        "UPDATE " + tbl + " SET item = '' " +
                                "WHERE item IS NOT NULL AND " +
                                "REGEXP_REPLACE(item, '[A-Za-z0-9+/=]', '') <> ''");
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
    private boolean hasIdColumn(Connection connection, String tablePrefix) throws SQLException {
        try (ResultSet rs = connection.getMetaData().getColumns(null, null, tablePrefix + "items", "id")) {
            return rs.next();
        }
    }
}