package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.Managers.ChatManager;
import rest.dawn.evientsCore.EvientsCore;

public class MarkDeadCommand implements CommandExecutor {
    public EvientsCore plugin;

    public MarkDeadCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, "evients.host.markdead")) return true;

        if (strings.length != 1) {
            commandSender.sendMessage(plugin.chat.error("Please provide a user!"));
            return true;
        }

        Player player = Bukkit.getPlayer(strings[0]);
        if (player == null) {
            commandSender.sendMessage(plugin.chat.error("Unknown player!"));
            return true;
        }

        if (plugin.listManager.dead.contains(player.getUniqueId())) {
            commandSender.sendMessage(plugin.chat.error("That player is already dead!"));
            return true;
        }

        plugin.listManager.setDead(player.getUniqueId());
        plugin.chat.announce(
                plugin.chat.primary(
                        ChatManager.usernameString(commandSender.getName()),
                        " has marked ",
                        ChatManager.usernameString(player.getName()),
                        " as dead!"
                )
        );

        return true;
    }
}
