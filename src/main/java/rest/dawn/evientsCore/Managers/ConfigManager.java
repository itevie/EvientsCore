package rest.dawn.evientsCore.Managers;

import org.bukkit.ChatColor;
import rest.dawn.evientsCore.EvientsCore;

public class ConfigManager {
    public ChatColor accentColor;
    public ChatColor errorColor;
    public ChatColor chatColor;

    public boolean ignoreHostsInCommands;

    public String announcementPrefix;

    public ConfigManager(EvientsCore plugin) {
        var config = plugin.getConfig();
        this.accentColor = ChatColor.valueOf(config.getString("accentColor", "YELLOW"));
        this.errorColor = ChatColor.valueOf(config.getString("errorColor", "RED"));
        this.chatColor = ChatColor.valueOf(config.getString("chatColor", "GREEN"));

        this.ignoreHostsInCommands = config.getBoolean("ignoreHostsInCommands", true);

        this.announcementPrefix = config.getString("announcementPrefix", "⚠");
    }
}
