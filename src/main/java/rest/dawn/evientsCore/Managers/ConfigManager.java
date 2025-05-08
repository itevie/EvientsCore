package rest.dawn.evientsCore.Managers;

import org.bukkit.ChatColor;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Util;

import javax.annotation.Nullable;
import java.util.List;

public class ConfigManager {
    public ChatColor accentColor;
    public ChatColor errorColor;
    public ChatColor chatColor;

    public boolean ignoreHostsInCommands;
    public @Nullable String spawnWarp;

    public boolean randomAnnouncementsEnabled;
    public int randomAnnouncementFrequency;
    public List<String> randomAnnouncements;

    public String announcementPrefix;

    public ConfigManager(EvientsCore plugin) {
        var config = plugin.getConfig();
        this.accentColor = ChatColor.valueOf(config.getString("accentColor", "YELLOW"));
        this.errorColor = ChatColor.valueOf(config.getString("errorColor", "RED"));
        this.chatColor = ChatColor.valueOf(config.getString("chatColor", "GREEN"));

        this.spawnWarp = config.getString("spawnWarp", null);
        this.ignoreHostsInCommands = config.getBoolean("ignoreHostsInCommands", true);

        this.randomAnnouncementsEnabled = config.getBoolean("randomAnnouncementsEnabled", false);
        this.randomAnnouncementFrequency = Util.parseTimeInput(config.getString("randomAnnouncementFrequency", "5m"));
        this.randomAnnouncements = config.getStringList("randomAnnouncements");

        this.announcementPrefix = config.getString("announcementPrefix", "⚠");
    }
}
