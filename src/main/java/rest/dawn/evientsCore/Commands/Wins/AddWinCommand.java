package rest.dawn.evientsCore.Commands.Wins;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Win;

import java.util.List;
import java.util.UUID;

public class AddWinCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public AddWinCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (plugin.permissions.ensurePermission(commandSender, "evients.host.wins.add")) return true;

        if (strings.length != 1) {
            plugin.chat.replyError(
                    commandSender,
                    "Usage: //addwin \\<user\\>"
            );
            return true;
        }

        Player player = Bukkit.getPlayer(strings[0]);
        UUID uuid = player.getUniqueId();
        plugin.wins.addWin(new Win(uuid));
        List<Win> wins = plugin.wins.getWinsFor(uuid);

        plugin.chat.announce(
                "<¬a>%s</¬a> added a win for <¬a>%s</¬a>!%s",
                commandSender.getName(),
                player.getName(),
                plugin.chat.underString("They now have " + wins.size() + " wins")
        );

        return true;
    }
}
