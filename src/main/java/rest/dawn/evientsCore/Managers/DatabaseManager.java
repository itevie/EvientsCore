package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.EvientsCore;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public EvientsCore plugin;
    public Connection connection;

    public DatabaseManager(EvientsCore plugin) {
        this.plugin = plugin;
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + new File(
                            plugin.getDataFolder(),
                            "data.db")
                            .toString()
            );
            initialise();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialise() {
        String tableCreation = """
                CREATE TABLE IF NOT EXISTS warps (
                    name TEXT PRIMARY KEY,
                    world TEXT NOT NULL,
                    x REAL NOT NULL,
                    y REAL NOT NULL,
                    z REAL NOT NULL,
                    yaw REAL NOT NULL DEFAULT 0,
                    pitch REAL NOT NULL DEFAULT 0
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(tableCreation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
