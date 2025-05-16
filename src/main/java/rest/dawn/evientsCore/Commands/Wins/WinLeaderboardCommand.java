
package rest.dawn.evientsCore.Commands.Wins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Win;
import rest.dawn.evientsCore.Util.Leaderboard;

import java.util.List;
import java.util.stream.Collectors;

public class WinLeaderboardCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public WinLeaderboardCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<Win> wins = plugin.wins.getAllWins();

        int pageIndex = 0;
        if (strings.length == 1) {
            try {
                pageIndex = Integer.parseInt(strings[0]);
            } catch (Exception e) {
                plugin.chat.replyError(commandSender, "Invalid page number!");
                return true;
            }
        }

        Leaderboard leaderboard = new Leaderboard(
                plugin,
                "Wins",
                wins.stream().collect(
                        Collectors.toMap(
                                obj -> obj.uuid,
                                obj -> 1,
                                Integer::sum
                        )
                ),
                "wins"
        );

        plugin.chat.reply(commandSender, leaderboard.toMinecraftString("winleaderboard", pageIndex));

        return true;
    }
}
