package rest.dawn.evientsCore.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.EvientsCore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Util {
    public static <T> T getRandomElement(Set<T> set) {
        if (set.isEmpty()) {
            throw new IllegalArgumentException("Set cannot be empty");
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(set.size());
        return new ArrayList<>(set).get(randomIndex);
    }

    public static void loadManyCommandsInto(EvientsCore plugin, CommandExecutor executor, String[] commands) {
        for (String command : commands) {
            plugin.getCommand(command).setExecutor(executor);
        }
    }

    public static void uuidToPlayerAndExecute(Set<UUID> uuids, Function<Player, ?> func) {
        for (UUID uuid : uuids) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                Bukkit.getLogger().warning("Failed to get user by UUID: " + uuid.toString());
                continue;
            }

            func.apply(player);
        }
    }

    public static String uuidsToUsernameString(Set<UUID> uuids) {
        Set<String> usernames = new HashSet<>();

        for (UUID uuid : uuids) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            usernames.add(player.getName());
        }

        if (usernames.isEmpty()) {
            return "No Players";
        }

        return String.join(", ", usernames.toArray(String[]::new));
    }
}
