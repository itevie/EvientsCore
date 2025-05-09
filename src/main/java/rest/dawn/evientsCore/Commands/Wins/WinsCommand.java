package rest.dawn.evientsCore.Commands.Wins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Win;

import java.util.List;

/// This isn't actually used, it's just a template command.
public class WinsCommand implements CommandExecutor {
    EvientsCore plugin;

    public WinsCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = plugin.permissions.requirePlayer(commandSender);
        if (player == null) return true;

        List<Win> wins = plugin.wins.getAllWins();
        long winsForSender = wins.stream().filter(x -> x.uuid.equals(player.getUniqueId())).count();

        // TODO: Add wins leaderboard

        commandSender.sendMessage(plugin.chat.primary(
                "You have ",
                plugin.chat.accent(String.valueOf(winsForSender)),
                " wins!"
        ));
        return true;
    }
}
