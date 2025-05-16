package rest.dawn.evientsCore.Util;

import org.bukkit.Bukkit;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Managers.ChatManager;

import java.util.*;
import java.util.stream.Collectors;

public class Leaderboard {
    private static int pageSize = 15;
    private final EvientsCore plugin;
    public final Map<UUID, Integer> entries;
    public final String entryName;
    public final String leaderboardName;


    public Leaderboard(EvientsCore plugin, String leaderboardName, Map<UUID, Integer> entries, String entryName) {
        this.plugin = plugin;
        this.entries = entries;
        this.entryName = entryName;
        this.leaderboardName = leaderboardName;
    }

    public String toMinecraftString(String command, int pageIndex) {
        List<String> parts = new ArrayList<>();

        parts.add(String.format("--- <¬a>%s</¬a> ---", leaderboardName));
        int start = pageIndex * pageSize;
        int end = Math.min((pageIndex + 1) * pageSize, entries.size());

        if (entries.isEmpty()) {
            parts.add(" <¬e>No entries to display.</¬e>");
        } else {
            Map<UUID, Integer> sortedEntries = entries.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            List<UUID> sortedKeys = new ArrayList<>(sortedEntries.keySet());

            for (int i = start; i < end; i++) {
                if (i < sortedKeys.size()) {
                    UUID uuid = sortedKeys.get(i);
                    int value = sortedEntries.get(uuid);
                    parts.add(String.format(" <¬a>%s</¬a>: <¬a>%d</¬a>", Bukkit.getOfflinePlayer(uuid).getName(), value));
                }
            }
        }

        int firstPage = 0;
        int veryLastPage = (entries.size() - 1) / pageSize;
        int lastPage = Math.max(0, veryLastPage);
        int nextPage = Math.min(pageIndex + 1, lastPage);
        int previousPage = Math.max(0, pageIndex - 1);

        String commandName = "/" + command + " ";
        parts.add(String.format("%s %s <¬a>%d</¬a>/<¬a>%d</¬a> %s %s",
                ChatManager.clickable(commandName + firstPage, "First Page", "<<"),
                ChatManager.clickable(commandName + previousPage, "Previous Page", "<"),
                pageIndex + 1,
                lastPage + 1,
                ChatManager.clickable(commandName + nextPage, "Next Page", ">"),
                ChatManager.clickable(commandName + veryLastPage, "Last Page", ">>")
        ));
        return String.join("\n", parts);
    }
}
