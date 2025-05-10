package rest.dawn.evientsCore.Commands.Warps;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

public class WarpListCommand implements CommandExecutor {
    EvientsCore plugin;

    public WarpListCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        plugin.chat.reply(
                commandSender,
                "Here is the list of warps:\n\n<white>%s</white>",
                String.join(", ", plugin.warps.getWarps().keySet())
        );
        return true;
    }
}
