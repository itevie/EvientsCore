package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.HideMode;
import rest.dawn.evientsCore.Util.Permissions;

import java.util.UUID;

public class ForceHideCommand implements CommandExecutor {
    EvientsCore plugin;

    public ForceHideCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, Permissions.host("forcehide"))) return true;

        if (strings.length == 0) {
            plugin.chat.replyError(commandSender,
                    "Usage: /forcehide \\<all|staff|off\\>"
            );
            return true;
        }

        for (UUID uuid : plugin.listManager.alive) {
            switch (strings[0]) {
                case "all" -> plugin.hides.setHideMode(Bukkit.getPlayer(uuid), HideMode.All);
                case "staff" -> plugin.hides.setHideMode(Bukkit.getPlayer(uuid), HideMode.Staff);
                case "off" -> plugin.hides.setHideMode(Bukkit.getPlayer(uuid), HideMode.Off);
                default -> {
                    plugin.chat.replyError(commandSender,
                            "Usage: /forcehide \\<all|staff|off\\>"
                    );
                    return true;
                }
            }
        }

        plugin.chat.announce(
                "<¬a>%s</¬a> has hidden <¬a>%s</¬a> for everyone!",
                commandSender.getName(),
                HideMode.fromCommandString(strings[0]).toHumanString()
        );

        return true;
    }
}
