package rest.dawn.evientsCore.Commands.Kits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class CreateKitCommand implements CommandExecutor {
    EvientsCore plugin;

    public CreateKitCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, Permissions.host("kits.create"))) return true;
        Player player = plugin.permissions.requirePlayer(commandSender);
        if (player == null) return true;

        if (strings.length != 1) {
            commandSender.sendMessage(plugin.chat.error(
                    "Please provide a name for your kit!"
            ));
            return true;
        }

        if (plugin.kits.kitExists(strings[0])) {
            commandSender.sendMessage(plugin.chat.error(
                    "A kit with that name already exists!"
            ));
            return true;
        }

        plugin.kits.createKit(strings[0], player.getInventory().getContents());
        commandSender.sendMessage(plugin.chat.primary(
                "Kit ",
                plugin.chat.accent(strings[0]),
                " has been created!"
        ));
        return true;
    }
}
