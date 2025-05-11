package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

public class AliveListCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public AliveListCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (plugin.listManager.alive.isEmpty()) {
            plugin.chat.reply(commandSender, "There are no alive players!");
            return true;
        }

        plugin.chat.reply(
                commandSender,
                "List of alive players:\n\n<white>%s</white>",
                String.join(", ",
                        this.plugin.listManager.alive
                                .stream()
                                .map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                                .toArray(String[]::new)
                )
        );

        return true;
    }
}
