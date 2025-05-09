package rest.dawn.evientsCore.Managers;

import org.bukkit.inventory.ItemStack;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Kit;
import rest.dawn.evientsCore.Util.InventoryUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KitManager {
    private final EvientsCore plugin;

    public KitManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public boolean kitExists(String name) {
        String sql = "SELECT 1 FROM kits WHERE name = ?";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Kit getKit(String name) {
        String sql = "SELECT * FROM kits WHERE name = ?";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            return Kit.fromResultSet(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Kit> getAllKits(String name) {
        String sql = "SELECT * FROM kits";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Kit> kits = new ArrayList<>();

            while (rs.next()) {
                kits.add(Kit.fromResultSet(rs));
            }

            return kits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createKit(String name, ItemStack[] items) {
        String sql = "INSERT INTO kits (name, items) VALUES (?, ?)";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, InventoryUtil.serializeInventory(items));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
