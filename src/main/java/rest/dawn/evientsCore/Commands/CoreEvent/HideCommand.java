package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

enum HideMode {
    Staff,
    All,
    Off,
}

public class HideCommand implements CommandExecutor {
    EvientsCore plugin;

    public HideCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(plugin.chat.error("Usage: /hide <staff|all|off>"));
            return true;
        }

        switch (strings[0]) {
            case "staff":
                setHideMode((Player)commandSender, HideMode.Staff);
                break;
            case "all":
                setHideMode((Player)commandSender, HideMode.All);
                break;
            case "off":
                setHideMode((Player)commandSender, HideMode.Off);
                break;
            default:
                commandSender.sendMessage(plugin.chat.error("Usage: /hide <staff|all|off>"));
                return true;
        }

        commandSender.sendMessage(
                plugin.chat.primary(
                        "Updated your hide mode to ",
                        plugin.chat.accent(strings[0]),
                        "!"
                )
        );

        return true;
    }

    private void showAll(Player target) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            target.showPlayer(plugin, player);
        }
    }

    private void setHideMode(Player target, HideMode mode) {
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
