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
        try (Statement statement = connection.createStatement()) {
            try {
                statement.execute("ALTER TABLE " + tablePrefix + "items ALTER COLUMN item SET DATA TYPE TEXT");
            } catch (SQLException e) {
                System.err.println("H2 syntax failed, trying with MySQL...");
                statement.execute("ALTER TABLE " + tablePrefix + "items MODIFY COLUMN item TEXT");
            }
        }
    }
}