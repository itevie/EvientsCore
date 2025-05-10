package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class RejoinsCommand implements CommandExecutor {
    EvientsCore plugin;

    public RejoinsCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, Permissions.host("rejoins.toggle"))) return true;

        plugin.state.rejoinsDisabled = !plugin.state.rejoinsDisabled;
        plugin.chat.announce(
                "<¬a>%s</¬a> has <¬a>%s</¬a> rejoins!",
                commandSender.getName(),
                plugin.state.rejoinsDisabled
                ? "<red>disabled</red>"
                        : "<green>enabled</green>"
        );
        return true;
    }
}
