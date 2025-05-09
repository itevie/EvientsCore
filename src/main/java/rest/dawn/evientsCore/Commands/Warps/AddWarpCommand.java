package rest.dawn.evientsCore.Commands.Warps;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

public class AddWarpCommand implements CommandExecutor {
    EvientsCore plugin;

    public AddWarpCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, "evients.host.warps.add")) return true;

        if (strings.length != 1 && strings.length != 4) {
            commandSender.sendMessage(
                    plugin.chat.error("Invalid usage! /setwarp <name> [x] [y] [z]")
            );
            return true;
        }

        String name = strings[0];
        Location location;
        Location playerLocation = ((Player)commandSender).getLocation();

        if (strings.length == 1) {
            location = playerLocation;
        } else {
            try {
                double x = Double.parseDouble(strings[1]);
                double y = Double.parseDouble(strings[2]);
                double z = Double.parseDouble(strings[3]);
                location = new Location(((Player)commandSender).getWorld(), x, y, z, playerLocation.getYaw(), playerLocation.getPitch());
            } catch (NumberFormatException e) {
                commandSender.sendMessage("Invalid input provided! Please provide a warp name, and 3 positional arguments");
                return true;
            }
        }

        plugin.warps.setWarp(name, location);
        commandSender.sendMessage(
                plugin.chat.primary(
                        "Created warp ",
                        plugin.chat.accent(name),
                        " which goes to [",
                        plugin.chat.accent(String.valueOf(location.getX())),
                        ", ",
                        plugin.chat.accent(String.valueOf(location.getY())),
                        ", ",
                        plugin.chat.accent(String.valueOf(location.getZ())),
                        "]! "
                )
        );
        return true;
    }
}
