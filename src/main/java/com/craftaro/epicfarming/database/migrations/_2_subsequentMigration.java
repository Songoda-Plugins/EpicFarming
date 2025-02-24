package com.craftaro.epicfarming.database.migrations;

import com.craftaro.core.database.DataMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _2_subsequentMigration extends DataMigration {
    public _2_subsequentMigration() {
        super(2);
    }

    @Override
    public void migrate(Connection connection, String tablePrefix) throws SQLException {

        // Edit items.
        try (Statement statement = connection.createStatement()) {
            // Change the data type of the 'item' column in the 'items'  table
            statement.execute("ALTER TABLE " + tablePrefix + "items ALTER COLUMN item SET DATA TYPE TEXT");
        }
    }
}
