package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.PlayerType;
import rest.dawn.evientsCore.Util.Util;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class MultiPlayerCommands implements CommandExecutor {
    EvientsCore plugin;

    public MultiPlayerCommands(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        System.out.println("hi");
        PlayerType type = PlayerType.getFromString(command.getName());
        Set<UUID> players = plugin.listManager.getPlayersFromPlayerType(type);
        if (players == null) {
            commandSender.sendMessage(plugin.chat.error("Couldn't find any players!"));
            return true;
        }

        Function<Player, ?> func = null;
        String name = command.getName().replaceAll("(all|alive|dead|random|randomalive|randomdead)$", "");

        func = switch (name) {
            case "tp" -> player -> player.teleport(((Player)commandSender).getLocation());
            case "kill" -> player -> {
                player.damage(1000000);
                return null;
            };
            case "clear" -> player -> {
                player.getInventory().clear();
                return null;
            };
            case "revive" -> player -> {
                player.teleport(((Player)commandSender).getLocation());
                plugin.listManager.setAlive(player.getUniqueId());
                return null;
            };
            default -> null;
        };

        if (func == null) {
            commandSender.sendMessage(plugin.chat.error("Invalid command!"));
            return true;
        }

        Util.uuidToPlayerAndExecute(players, func);

        plugin.chat.announce(
                plugin.chat.primary(
                        plugin.chat.accent(commandSender.getName()),
                        " " + getString(name) + " ",
                        plugin.chat.accent(type.toString()),
                        "!",
                        "\n     > ",
                        ChatColor.GRAY.toString() +
                        String.join(", ", Util.uuidsToUsernameString(players))
                )
        );

        return true;
    }

    private String getString(String commandName) {
        return switch (commandName) {
            case "tp" -> "teleported";
            case "revive" -> "revived";
            case "clear" -> "cleared";
            case "kill" -> "killed";
            default -> commandName;
        };
    }
}
