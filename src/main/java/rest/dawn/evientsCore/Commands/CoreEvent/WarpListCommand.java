package rest.dawn.evientsCore.Commands.CoreEvent;

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
        commandSender.sendMessage(plugin.chat.primary(
                "Here is the list of warps:\n",
                ChatColor.WHITE + String.join(", ", plugin.warps.getWarps())
        ));
        return true;
    }
}
