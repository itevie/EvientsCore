package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Kit;
import rest.dawn.evientsCore.Util.Permissions;
import rest.dawn.evientsCore.Util.PlayerType;
import rest.dawn.evientsCore.Util.UndoAction;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

// Rules:
// /x -> Perform on self
// /x user -> do x on user
// /xselector -> do x on the selection
// Exceptions:
// /give[selector|user] item amount

record CommandAction(@Nullable Function<Player, Runnable> undoFactory, Consumer<Player> action) {}

public class MultiPlayerCommands implements CommandExecutor {
    private final EvientsCore plugin;
    public static final String selectorRegex = "(all|alive|dead|random|randomalive|randomdead)$";

    public MultiPlayerCommands(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // Only players can run
        Player commandPlayer = plugin.permissions.requirePlayer(commandSender);
        if (commandPlayer == null) return true;

        List<String> args = new ArrayList<>(Arrays.asList(strings));
        String name = command.getName().replaceAll(MultiPlayerCommands.selectorRegex, "");
        String specialPart = "";

        List<Player> players;
        PlayerType playerSelectorType =
                command.getName().matches(".+" + MultiPlayerCommands.selectorRegex)
                    ? PlayerType.getFromString(command.getName())
                        : null;

        // The command has a selector
        if (playerSelectorType != null) {
            players = plugin.listManager.getPlayersFromPlayerType(playerSelectorType)
                    .stream()
                    .map(Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else { // The command does not have a selector
            Player bukkitPlayer = args.isEmpty() ? null : Bukkit.getPlayer(args.getFirst());
            // The command has a user as the first parameter
            if (bukkitPlayer != null) {
                players = List.of(bukkitPlayer);

                // Remove first arg for commands like /give
                args.removeFirst();
            } else {
                // The command does not have selector or user
                players = List.of(commandPlayer);
            }
        }

        // Check for permission
        String permissionString = Permissions.host(
                name +
                        (playerSelectorType != null
                                ? ("." + playerSelectorType.toString().toLowerCase())
                                : ""));

        if (plugin.permissions.ensurePermission(commandSender, permissionString))
            return true;

        if (players.isEmpty()) {
            plugin.chat.replyError(
                    commandSender,
                    "No players found!"
            );
            return true;
        }

        // Check arguments
        if (name.equals("give")) {
            if (args.isEmpty()) {
                plugin.chat.replyError(
                        commandSender,
                        "Usage: /give \\<item\\> [amount]"
                );
                return true;
            }

            Material material = Material.matchMaterial(args.getFirst());
            if (material == null) {
                plugin.chat.announce(args.getFirst());
                plugin.chat.replyError(
                        commandSender,
                        "Invalid item: %s",
                        args.getFirst()
                );
                return true;
            }

            int amount = 1;
            if (args.size() >= 2) {
                try {
                    amount = Integer.parseInt(args.get(1));
                    if (amount <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    plugin.chat.replyError(
                            commandSender,
                            "Invalid amount: %d",
                            args.get(1)
                    );
                    return true;
                }
            }


            specialPart = String.format("<¬a>%dx %s</¬a>", amount, material.name());
        }
        else if (name.equals("kit")) {
            if (args.size() != 1) {
                plugin.chat.replyError(
                        commandSender,
                        "Please give a kit name!"
                );
                return true;
            }

            if (!plugin.kits.kitExists(args.getFirst())) {
                plugin.chat.replyError(
                        commandSender,
                        "That kit does not exist!"
                );
                return true;
            }
        }

        CommandAction commandAction = switch (name) {
            case "tp" -> new CommandAction(
                    player -> {
                        var location = player.getLocation().clone();
                        return () -> player.teleport(location);
                    },
                    player -> player.teleport(commandPlayer.getLocation())
            );
            case "revive" -> new CommandAction(
                    player -> {
                        var location = player.getLocation().clone();
                        var isAlive = plugin.listManager.alive.contains(player.getUniqueId());
                        return () -> {
                            player.teleport(location);
                            if (isAlive) plugin.listManager.setAlive(player.getUniqueId());
                            else plugin.listManager.setDead(player.getUniqueId());
                        };
                    },
                    player -> {
                        player.teleport(commandPlayer.getLocation());
                        plugin.listManager.setAlive(player.getUniqueId());
                    }
            );
            case "clear" -> new CommandAction(
                    null,
                    player -> player.getInventory().clear()
            );
            case "kill" -> new CommandAction(
                    null,
                    player -> player.damage(1000000)
            );
            case "kit" -> new CommandAction(
                    null,
                    player -> {
                        Kit kit = plugin.kits.getKit(args.getFirst());
                        player.getInventory().setContents(Arrays.copyOfRange(kit.items(), 0, 36));
                        player.getInventory().setHelmet(kit.items().length > 36 ? kit.items()[36] : null);
                        player.getInventory().setChestplate(kit.items().length > 37 ? kit.items()[37] : null);
                        player.getInventory().setLeggings(kit.items().length > 38 ? kit.items()[38] : null);
                        player.getInventory().setBoots(kit.items().length > 39 ? kit.items()[39] : null);
                        player.getInventory().setItemInOffHand(kit.items().length > 40 ? kit.items()[40] : null);
                    }
            );
            case "give" -> new CommandAction(
                    null,
                    player -> {
                        Material material = Material.matchMaterial(args.getFirst());
                        int amount = args.size() >= 2 ? Integer.parseInt(args.get(1)) : 1;
                        ItemStack stack = new ItemStack(Objects.requireNonNull(material), amount);
                        player.getInventory().addItem(stack);
                    }
            );
            default -> null;
        };

        // Check if command existed
        if (commandAction == null) {
            plugin.chat.replyError(
                    commandSender,
                    "Sorry, but an error occurred!"
            );
            return true;
        }

        // Apply undoes
        if (commandAction.undoFactory() != null) {
            List<Runnable> undos = players.stream()
                    .filter(Objects::nonNull)
                    .map(commandAction.undoFactory())
                    .toList();

            plugin.state.undo = new UndoAction(undos, name);
        }

        // Run
        players.stream()
                .filter(Objects::nonNull)
                .forEach(commandAction.action());

        String message = String.format(
                "<¬a>%s</¬a> %s <¬a>%s</¬a>%s!%s",
                commandSender.getName(),
                getPastTenseAction(name),
                playerSelectorType != null
                        ? playerSelectorType.toHumanString()
                        : players.getFirst().getName(),
                !specialPart.isEmpty() ? " " + specialPart : "",
                plugin.chat.underString(String.join(", ", players.stream().map(Player::getName).toList()))
        );

        // Done
        if (players.size() == 1 && players.getFirst().getUniqueId() == commandPlayer.getUniqueId()) {
            plugin.chat.reply(commandSender, message);
        } else {
            plugin.chat.announce(message);
        }

        return true;
    }

    private static String getPastTenseAction(String commandName) {
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
