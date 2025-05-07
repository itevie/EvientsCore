package rest.dawn.evientsCore.Models;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Warp {
    public final String name;
    public Location location;

    public Warp(String name, String world, double x, double y, double z, float yaw, float pitch) {
        this.name = name;
        this.location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static Warp fromResultSet(ResultSet rs) throws SQLException {
        return new Warp(
                rs.getString("name"),
                rs.getString("world"),
                rs.getDouble("x"),
                rs.getDouble("y"),
                rs.getDouble("z"),
                rs.getFloat("yaw"),
                rs.getFloat("pitch")
        );
    }
}
