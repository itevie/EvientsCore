package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Util;

import javax.annotation.Nullable;
import java.util.List;

public class ConfigManager {
    private final EvientsCore plugin;
    public String accentColor;
    public String errorColor;
    public String chatColor;

    public boolean ignoreHostsInCommands;
    public @Nullable String spawnWarp;
//    public int undoTime;

    public boolean randomAnnouncementsEnabled;
    public int randomAnnouncementFrequency;
    public List<String> randomAnnouncements;

    public String announcementPrefix;

    public @Nullable String discordBotToken;
    public @Nullable String discordBotServer;

    public ConfigManager(EvientsCore plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        plugin.reloadConfig();
        var config = plugin.getConfig();
        this.accentColor = config.getString("accentColor", "yellow");
        this.errorColor = config.getString("errorColor", "red");
        this.chatColor = config.getString("chatColor", "green");

        this.spawnWarp = config.getString("spawnWarp", null);
        this.ignoreHostsInCommands = config.getBoolean("ignoreHostsInCommands", true);
        //this.undoTime = config.getInt("undoTime", 10);

        this.randomAnnouncementsEnabled = config.getBoolean("randomAnnouncementsEnabled", false);
        this.randomAnnouncementFrequency = Util.parseTimeInput(config.getString("randomAnnouncementFrequency", "5m"));
        this.randomAnnouncements = config.getStringList("randomAnnouncements");

        this.discordBotToken = config.getString("discordBotToken", null);
        this.discordBotServer = config.getString("discordBotServer", null);

        this.announcementPrefix = config.getString("announcementPrefix", "⚠");

    }
}
