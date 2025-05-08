package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.PlayerType;
import rest.dawn.evientsCore.Util.UndoAction;
import rest.dawn.evientsCore.Util.Util;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MultiPlayerCommands implements CommandExecutor {
    EvientsCore plugin;
    public static String selectorRegex = "(all|alive|dead|random|randomalive|randomdead)$";

    public MultiPlayerCommands(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player comamndPlayer = plugin.chat.requirePlayer(commandSender);
        if (comamndPlayer == null) return true;

        List<String> args = new ArrayList<>(Arrays.asList(strings));
        Set<UUID> players;
        String typeString = "?";
        PlayerType selector = null;

        // Check if the command is give<selector>
        if (command.getName().matches(".+" + MultiPlayerCommands.selectorRegex)) {
            PlayerType type = PlayerType.getFromString(command.getName());
            players = plugin.listManager.getPlayersFromPlayerType(type);
            typeString = type.toHumanString();
            selector = type;
        } else {
            // Otherwise, if it's empty give an error
            if (args.isEmpty()) {
                commandSender.sendMessage(plugin.chat.error("Please provide a user or a selector!"));
                return true;
            }

            // Check if it is a player
            Player playerArg = Bukkit.getPlayer(args.getFirst());
            if (playerArg == null) {
                // Check if it is a selector but with a space
                if (args.getFirst().toLowerCase().matches(MultiPlayerCommands.selectorRegex)) {
                    PlayerType type = PlayerType.getFromString(args.getFirst().toLowerCase());
                    players = plugin.listManager.getPlayersFromPlayerType(type);
                    typeString = type.toHumanString();
                } else {
                    // Error
                    commandSender.sendMessage(plugin.chat.error("Invalid user provided!"));
                    return true;
                }
            } else {
                // It is a player
                players = Set.of(playerArg.getUniqueId());
                typeString = playerArg.getName();
            }

            // Remove the first argument for future checks
            args.removeFirst();
        }

        if (players == null) {
            commandSender.sendMessage(plugin.chat.error(
                    "No players to select!"
            ));
            return true;
        }


        List<String> usernames = players.stream().map(x -> Bukkit.getPlayer(x).getName()).toList();

        Consumer<Player> func = null;
        String name = command.getName().replaceAll(MultiPlayerCommands.selectorRegex, "");

        if (!plugin.chat.ensurePermission(commandSender, MultiPlayerCommands.getPermissionString(name, selector))) {
            return true;
        }

        // Check special commands
        String specialPart = "";
        if (name.equals("give")) {
            if (args.isEmpty()) {
                commandSender.sendMessage(plugin.chat.error(
                        "Usage: /give <item> [amount]"
                ));
                return true;
            }

            Material material = Material.matchMaterial(args.getFirst());
            if (material == null) {
                commandSender.sendMessage(plugin.chat.error(
                        "Invalid item: " + args.getFirst()
                ));
                return true;
            }

            int amount = 1;
            if (args.size() >= 2) {
                try {
                    amount =Integer.parseInt(args.get(1));
                    if (amount <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(plugin.chat.error("Invalid amount: " + args.get(1)));
                    return true;
                }
            }

            specialPart = plugin.chat.accent(amount + "x " + material.name());
        }

        // Apply undoes
        switch (name)  {
            case "tp" -> {
                List<Runnable> funcs = new ArrayList<>();
                for (UUID uuid : players) {
                    var _player = Bukkit.getPlayer(uuid);
                    var location = _player.getLocation().clone();
                    funcs.add(() -> _player.teleport(location));
                }
                plugin.state.undo = new UndoAction(funcs, "tp");
            }
        }

        func = switch (name) {
            case "tp" -> player -> player.teleport(comamndPlayer.getLocation());
            case "kill" -> player -> {
                player.damage(1000000);
            };
            case "clear" -> player -> {
                player.getInventory().clear();
            };
            case "revive" -> player -> {
                player.teleport(comamndPlayer.getLocation());
                plugin.listManager.setAlive(player.getUniqueId());
            };
            case "give" -> player -> {
              Material material = Material.matchMaterial(args.getFirst());

              int amount = 1;
              if (args.size() >= 2) {
                  amount = Integer.parseInt(args.get(1));
              }

              ItemStack stack = new ItemStack(Objects.requireNonNull(material), amount);
              player.getInventory().addItem(stack);

            };
            default -> null;
        };

        if (func == null) {
            commandSender.sendMessage(plugin.chat.error("Invalid command!"));
            return true;
        }

        players.stream().map(Bukkit::getPlayer).forEach(func);

        plugin.chat.announce(
                plugin.chat.primary(
                        plugin.chat.accent(commandSender.getName()),
                        " " + getString(name) + " ",
                        plugin.chat.accent(typeString),
                        !specialPart.isEmpty() ? " " + specialPart : "",
                        "!",
                        "\n     > " +
                        ChatColor.GRAY.toString() +
                        String.join(", ", usernames)
                )
        );

        return true;
    }

    private static String getPermissionString(String commandName, PlayerType selector) {
        if (selector != null) {
            return "evients.host." + commandName + "." + selector.toString().toLowerCase();
        } else {
            return "evients.host." + commandName;
        }
    }

    private static String getString(String commandName) {
        return switch (commandName) {
            case "tp" -> "teleported";
            case "revive" -> "revived";
            case "clear" -> "cleared";
            case "kill" -> "killed";
            case "give" -> "gave";
            default -> commandName;
        };
    }
}
