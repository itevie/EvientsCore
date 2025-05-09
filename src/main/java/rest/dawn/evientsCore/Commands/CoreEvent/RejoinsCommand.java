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

        plugin.chat.announce(plugin.chat.primary(
                plugin.chat.accent(commandSender.getName()),
                " has ",
                plugin.chat.accent(plugin.state.rejoinsDisabled ? "disabled" : "enabled"),
                " rejoins!"
        ));
        return true;
    }
}
