package rest.dawn.evientsCore.Managers;

import org.bukkit.ChatColor;
import rest.dawn.evientsCore.EvientsCore;

public class ConfigManager {
    public ChatColor accentColor;
    public ChatColor errorColor;
    public ChatColor chatColor;

    public String announcementEmoji;

    public ConfigManager(EvientsCore plugin) {
        var config = plugin.getConfig();
        this.accentColor = ChatColor.valueOf(config.getString("accentColor"));
        this.errorColor = ChatColor.valueOf(config.getString("errorColor"));
        this.chatColor = ChatColor.valueOf(config.getString("chatColor"));

        this.announcementEmoji = config.getString("announcementEmoji");
    }
}
