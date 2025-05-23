package rest.dawn.evientsCore.Commands.Kits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Kit;

import java.util.List;

public class KitsCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public KitsCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<Kit> kits = plugin.kits.getAllKits();
        plugin.chat.reply(
                commandSender,
                "Here are the last of kits:\n\n<white>%s</white>",
                String.join(", ", kits.stream().map(Kit::name).toList())
        );
        return true;
    }
}
