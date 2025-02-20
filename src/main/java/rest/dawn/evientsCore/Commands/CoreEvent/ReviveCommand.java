package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.Managers.ChatManager;
import rest.dawn.evientsCore.EvientsCore;

public class ReviveCommand implements CommandExecutor {
    public EvientsCore plugin;

    public ReviveCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(plugin.chat.error("Please provide a user!"));
            return true;
        }

        Player player = Bukkit.getPlayer(strings[0]);
        if (player == null) {
            commandSender.sendMessage(plugin.chat.error("Unknown player!"));
            return true;
        }

        if (plugin.listManager.alive.contains(player.getUniqueId())) {
            commandSender.sendMessage(plugin.chat.error("That player is already alive!"));
            return true;
        }

        plugin.listManager.setAlive(player.getUniqueId());
        player.teleport(((Player) commandSender).getLocation());
        plugin.chat.announce(
                plugin.chat.primary(
                        ChatManager.usernameString(commandSender.getName()),
                        " has revived ",
                        ChatManager.usernameString(player.getName()),
                        "!"
                )
        );
        return true;
    }
}
