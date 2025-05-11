package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class MarkDeadCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public MarkDeadCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (plugin.permissions.ensurePermission(commandSender, Permissions.host("markdead"))) return true;

        if (strings.length != 1) {
            plugin.chat.replyError(commandSender, "Please provide a user!");
            return true;
        }

        Player player = Bukkit.getPlayer(strings[0]);
        if (player == null) {
            plugin.chat.replyError(commandSender, "Unknown player!");
            return true;
        }

        if (plugin.listManager.dead.contains(player.getUniqueId())) {
            plugin.chat.replyError(commandSender, "That player is already dead!");
            return true;
        }

        plugin.listManager.setDead(player.getUniqueId());
        plugin.chat.announce(
                "<¬a>%s</¬a> has marked <¬a>%s</¬a> as dead!",
                commandSender.getName(),
                player.getName()
        );
        return true;
    }
}
