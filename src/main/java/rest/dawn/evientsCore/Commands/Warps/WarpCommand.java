package rest.dawn.evientsCore.Commands.Warps;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Warp;

public class WarpCommand implements CommandExecutor {
    EvientsCore plugin;

    public WarpCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = plugin.permissions.requirePlayer(commandSender);
        if (player == null) return true;

        if (strings.length != 1) {
            commandSender.sendMessage(plugin.chat.error(
                    "Please provide a warp name! /warp <name>"
            ));
            return true;
        }

        String name = strings[0].toLowerCase();
        Warp warp = plugin.warps.getWarp(name);
        if (warp == null) {
            commandSender.sendMessage(plugin.chat.error(
                    "A warp with that name does not exist!"
            ));
            return true;
        }

        /*
        if (!commandSender.hasPermission("evients.warps.warp." + name)) {
            commandSender.sendMessage(plugin.chat.error(
                    "You do not have permission to warp to this warp!"
            ));
            return true;
        }*/

        if (!commandSender.hasPermission("evients.host") && plugin.listManager.alive.contains(((Player)commandSender).getUniqueId())) {
            commandSender.sendMessage(plugin.chat.error(
                    "Sorry, but only dead people can warp."
            ));
            return true;
        }

        warp.teleport(player);
        commandSender.sendMessage(plugin.chat.primary(
                "Whoosh!"
        ));
        return true;
    }
}
