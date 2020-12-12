package com.songoda.epicfarming.database.migrations;

import com.songoda.core.database.DataMigration;
import com.songoda.core.database.MySQLConnector;
import com.songoda.epicfarming.EpicFarming;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _1_InitialMigration extends DataMigration {

    public _1_InitialMigration() {
        super(1);
    }

    @Override
    public void migrate(Connection connection, String tablePrefix) throws SQLException {
        String autoIncrement = EpicFarming.getInstance().getDatabaseConnector() instanceof MySQLConnector ? " AUTO_INCREMENT" : "";

        // Create farms table.
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE " + tablePrefix + "active_farms (" +
                    "id INTEGER PRIMARY KEY" + autoIncrement + ", " +
                    "farm_type TEXT NOT NULL, " +
                    "level INTEGER NOT NULL, " +
                    "placed_by VARCHAR(36), " +
                    "world TEXT NOT NULL, " +
                    "x DOUBLE NOT NULL, " +
                    "y DOUBLE NOT NULL, " +
                    "z DOUBLE NOT NULL " +
                    ")");
        }

        // Create items.
        // Items are stored as Base64. Dunno if this is the most efficient way to
        // store them, though.
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE " + tablePrefix + "items (" +
                    "farm_id INTEGER NOT NULL, " +
                    "item TEXT NOT NULL" +
                    ")");
        }

        // Create player boosts
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE " + tablePrefix + "boosted_players (" +
                    "player VARCHAR(36) NOT NULL, " +
                    "multiplier INTEGER NOT NULL," +
                    "end_time BIGINT NOT NULL " +
                    ")");
        }
    }
}
