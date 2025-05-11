package rest.dawn.evientsCore.Managers;

import org.bukkit.Bukkit;
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
            plugin.chat.announce(
                    "<¬a>Random announcement:</¬a> %s",
                    message
            );
        }, 20L, plugin.config.randomAnnouncementFrequency * 20L);
    }
}
