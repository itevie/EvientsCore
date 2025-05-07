package rest.dawn.evientsCore.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Warp;

import javax.annotation.Nullable;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class WarpManager {
    final EvientsCore plugin;
    Map<String, Object> warpList;

    public WarpManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public Map<String, Warp> getWarps() {
        Map<String, Warp> warps = new HashMap<>();
        String sql = "SELECT * FROM warps";

        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Warp warp = Warp.fromResultSet(rs);
                warps.put(warp.name, warp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return warps;
    }

    public void setWarp(String name, Location location) {
        String sql = "INSERT INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, location.getWorld().getName());
            stmt.setDouble(3, location.getX());
            stmt.setDouble(4, location.getY());
            stmt.setDouble(5, location.getZ());
            stmt.setFloat(6, location.getYaw());
            stmt.setFloat(7, location.getPitch());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable Location getWarp(String name) {
        String sql = "SELECT * FROM warps WHERE name = ?";

        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Warp.fromResultSet(rs).location;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void deleteWarp(String name) {
        String sql = "DELETE FROM warps WHERE name = ?";

        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
