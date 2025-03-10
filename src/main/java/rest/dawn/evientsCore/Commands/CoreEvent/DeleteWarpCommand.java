package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

import java.util.Arrays;

public class DeleteWarpCommand implements CommandExecutor {
    EvientsCore plugin;

    public DeleteWarpCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(plugin.chat.error(
                    "Invalid usage! /delwarp <name>"
            ));
            return true;
        }

        String name = strings[0].toLowerCase();
        if (!Arrays.asList(plugin.warps.getWarps()).contains(name)) {
            commandSender.sendMessage(plugin.chat.error(
                    "That warp does not exist"
            ));
            return true;
        }

        plugin.warps.deleteWarp(name);
        commandSender.sendMessage(plugin.chat.primary(
                "Deleted warp ",
                plugin.chat.accent(name),
                "!"
        ));
        return true;
    }
}
