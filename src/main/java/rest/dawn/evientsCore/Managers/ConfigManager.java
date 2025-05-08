package rest.dawn.evientsCore.Managers;

import org.bukkit.ChatColor;
import rest.dawn.evientsCore.EvientsCore;

import javax.annotation.Nullable;

public class ConfigManager {
    public ChatColor accentColor;
    public ChatColor errorColor;
    public ChatColor chatColor;

    public boolean ignoreHostsInCommands;
    public @Nullable String spawnWarp;

    public String announcementPrefix;

    public ConfigManager(EvientsCore plugin) {
        var config = plugin.getConfig();
        this.accentColor = ChatColor.valueOf(config.getString("accentColor", "YELLOW"));
        this.errorColor = ChatColor.valueOf(config.getString("errorColor", "RED"));
        this.chatColor = ChatColor.valueOf(config.getString("chatColor", "GREEN"));

        this.spawnWarp = config.getString("spawnWarp", null);
        this.ignoreHostsInCommands = config.getBoolean("ignoreHostsInCommands", true);

        this.announcementPrefix = config.getString("announcementPrefix", "⚠");
    }
}
