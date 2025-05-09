package rest.dawn.evientsCore.Commands.DirectMessages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

import java.util.Arrays;

public class MessageCommand implements CommandExecutor {
    EvientsCore plugin;

    public MessageCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player commandPlayer = plugin.permissions.requirePlayer(commandSender);
        if (commandPlayer == null) return true;

        if (strings.length < 2) {
            commandSender.sendMessage(plugin.chat.error(
                    "Usage: /msg <user> <message>"
            ));
            return true;
        }

        Player player = Bukkit.getPlayer(strings[0]);
        String message = String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));

        if (player == null) {
            commandSender.sendMessage(plugin.chat.error(
                    "Cannot find that user, are they online?"
            ));
            return true;
        }

        plugin.privateMessages.send(commandPlayer, player, message);
        return true;
    }
}
