package rest.dawn.evientsCore.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import rest.dawn.evientsCore.EvientsCore;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WarpManager {
    final EvientsCore plugin;
    final File configFile;
    final Gson gson;
    Map<String, Object> warpList;

    public WarpManager(EvientsCore plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "warps.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        if (!configFile.exists()) {
            plugin.saveResource("warps.json", false);
        }

        try (Reader reader = new FileReader(configFile)) {
            warpList = gson.fromJson(reader, Map.class);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load warps.josn");
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(warpList, writer);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save warp.json");
        }
    }

    public void setWarp(String name, Location location) {
        List<Object> list = new ArrayList<>();
        list.add(location.getWorld().getName());
        list.add(location.getX());
        list.add(location.getY());
        list.add(location.getZ());
        list.add(location.getYaw());
        list.add(location.getPitch());
        warpList.put(name, list);
        save();
    }

    public @Nullable Location getWarp(String name) {
        if (warpList.containsKey(name)) {
            Object warp = warpList.get(name);
            if (warp instanceof List<?>) {
                try {
                    List<?> list = (List<?>) warp;
                    String world = (String)list.get(0);
                    double x = (double)list.get(1);
                    double y = (double)list.get(2);
                    double z = (double)list.get(3);
                    float yaw = ((Number) list.get(4)).floatValue();
                    float pitch = ((Number) list.get(5)).floatValue();

                    return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                } catch (Exception e) {
                    plugin.getLogger().warning(e.toString());
                    plugin.getLogger().warning(Arrays.toString(e.getStackTrace()));
                    plugin.getLogger().warning("An error occurred while trying to parse warp " + name);
                }
            } else {
                plugin.getLogger().warning("Warp " + name + " has invalid positional data.");
            }
        }

        return null;
    }

    public void deleteWarp(String name) {
        warpList.remove(name);
        save();
    }

    public String[] getWarps() {
        return warpList.keySet().toArray(new String[0]);
    }
}
