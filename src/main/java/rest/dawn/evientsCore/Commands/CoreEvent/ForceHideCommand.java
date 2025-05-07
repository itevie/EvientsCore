package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.HideMode;

import java.util.UUID;

/// This isn't actually used, it's just a template command.
public class ForceHideCommand implements CommandExecutor {
    EvientsCore plugin;

    public ForceHideCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(
                    plugin.chat.error("Usage: /forcehide <all|staff|off>")
            );
            return true;
        }

        for (UUID uuid : plugin.listManager.alive) {
            switch (strings[0]) {
                case "all" -> plugin.hides.setHideMode(Bukkit.getPlayer(uuid), HideMode.All);
                case "staff" -> plugin.hides.setHideMode(Bukkit.getPlayer(uuid), HideMode.Staff);
                case "off" -> plugin.hides.setHideMode(Bukkit.getPlayer(uuid), HideMode.Off);
                default -> {
                    commandSender.sendMessage(
                            plugin.chat.error("Usage: /forcehide <all|staff|off>")
                    );
                    return true;
                }
            }
        }

        plugin.chat.announce(
                plugin.chat.primary(
                        plugin.chat.accent(commandSender.getName()),
                        " has hidden ",
                        plugin.chat.accent(HideMode.fromCommandString(strings[0]).toHumanString()),
                        " for everyone!"
                )
        );

        return true;
    }
}
