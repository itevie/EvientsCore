package rest.dawn.evientsCore.Commands.Help;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class ReloadConfigCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public ReloadConfigCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (plugin.permissions.ensurePermission(commandSender, Permissions.host("reloadconfig"))) return true;
        plugin.config.load();
        plugin.chat.reply(commandSender, "Reloaded confing!");
        return true;
    }
}
