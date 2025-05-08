package rest.dawn.evientsCore.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.HideMode;

public class HideManager {
    private final EvientsCore plugin;

    public HideManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public void showAll(Player target) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            target.showPlayer(plugin, player);
        }
    }

    public void setHideMode(Player target, HideMode mode) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            switch (mode) {
                case HideMode.Off:
                    showAll(target);
                    break;
                case HideMode.Staff:
                    if (player.hasPermission("evients.host")) {
                        target.showPlayer(plugin, player);
                    } else {
                        target.hidePlayer(plugin, player);
                    }
                    break;
                case HideMode.All:
                    target.hidePlayer(plugin, player);
                    break;
            }
        }
    }
}
