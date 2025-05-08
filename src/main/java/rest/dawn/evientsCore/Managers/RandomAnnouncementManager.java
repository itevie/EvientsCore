package rest.dawn.evientsCore.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Util;

public class RandomAnnouncementManager {
    private final EvientsCore plugin;

    public RandomAnnouncementManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (!plugin.config.randomAnnouncementsEnabled) return;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            String message = Util.getRandomElement(plugin.config.randomAnnouncements);
            plugin.chat.announce(plugin.chat.primary(
                    plugin.chat.accent("Random announcement: "),
                    message
            ));
        }, 20L, plugin.config.randomAnnouncementFrequency * 20L);
    }
}
