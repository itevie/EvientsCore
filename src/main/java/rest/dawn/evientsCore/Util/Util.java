package rest.dawn.evientsCore.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.EvientsCore;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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



    // Stolen from ChatGPT
    public static int parseTimeInput(String input) {
        // Regular expression to match formats like "30s", "1m", "1m30s", etc.
        Pattern pattern = Pattern.compile("(?:(\\d+)m)?(?:(\\d+)s)?");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            return -1; // Invalid input
        }

        int minutes = matcher.group(1) != null ? Integer.parseInt(matcher.group(1)) : 0;
        int seconds = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;

        return minutes * 60 + seconds; // Total seconds
    }

    public static boolean userIsHost(UUID player) {
        LuckPerms luckPerms = LuckPermsProvider.get();

        // Get the LuckPerms user
        User user = luckPerms.getUserManager().getUser(player);

        boolean has =  user.getCachedData().getPermissionData()
                    .checkPermission("evients.host").asBoolean();
        return has;
    }
}
