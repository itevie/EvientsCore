package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.EvientsCore;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DatabaseManager {
    private final EvientsCore plugin;
    public Connection connection;

    public DatabaseManager(EvientsCore plugin) {
        this.plugin = plugin;
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + new File(
                            plugin.getDataFolder(),
                            "data.db")
            );
            initialise();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialise() {
        ensureTable("warps", new HashMap<>(){{
            put("name", "TEXT PRIMARY KEY");
            put("world", "TEXT NOT NULL");
            put("x", "REAL NOT NULL");
            put("y", "REAL NOT NULL");
            put("z", "REAL NOT NULL");
            put("yaw", "REAL NOT NULL DEFAULT 0");
            put("pitch", "REAL NOT NULL DEFAULT 0");
        }});

        ensureTable("wins", new HashMap<>(){{
            put("uuid", "TEXT NOT NULL");
            put("added_at", "TEXT NOT NULL");
            put("event_name", "TEXT DEFAULT NULL");
        }});

        ensureTable("user_data", new HashMap<>(){{
            put("uuid", "TEXT PRIMARY KEY");
            put("dms_disabled", "BOOLEAN NOT NULL DEFAULT FALSE");
        }});

        ensureTable("kits", new HashMap<>(){{
            put("name", "TEXT PRIMARY KEY");
            put("items", "TEXT NOT NULL");
        }});
    }

    private void ensureTable(String tableName, Map<String, String> definitions) {
        String definition = "CREATE TABLE IF NOT EXISTS "
                + tableName
                + "("
                + definitions.entrySet().stream()
                    .map(e -> e.getKey() + " " + e.getValue())
                    .collect(Collectors.joining(",\n"))
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(definition);

            Set<String> existingColumns = new HashSet<>();
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
            while (rs.next()) {
                existingColumns.add(rs.getString("name"));
            }

            for (var key : definitions.entrySet()) {
                if (!existingColumns.contains(key.getKey())) {
                    stmt.execute("ALTER TABLE " + tableName + " ADD COLUMN " + key.getValue() + ";");
                    plugin.getLogger().info("Your database was missing " + key.getKey() + " in table " + tableName + ", it was added");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
