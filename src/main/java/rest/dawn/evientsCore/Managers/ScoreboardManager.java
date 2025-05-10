package rest.dawn.evientsCore.Managers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import rest.dawn.evientsCore.EvientsCore;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class ScoreboardManager {
    private final EvientsCore plugin;
    Map<UUID, Scoreboard> scoreboards = new HashMap<>();
    public Integer timer = null;

    public ScoreboardManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = scoreboards.get(player.getUniqueId());
                if (scoreboard != null) {
                    updateScoreboardFor(player, scoreboard, scoreboard.getObjective("evients"));
                } else {
                    createScoreboardFor(player);
                }
            }
        }, 20L, 20L);
    }

    public void createScoreboardFor(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective(
                "evients",
                Criteria.DUMMY,
                plugin.chat.legacy("<¬a>Evients</¬a>")
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateScoreboardFor(player, board, objective);
        player.setScoreboard(board);
        scoreboards.put(player.getUniqueId(), board);
    }

    private void updateScoreboardFor(Player player, Scoreboard scoreboard, Objective objective) {
        scoreboard.getEntries().forEach(scoreboard::resetScores);

        List<String> lines = List.of(
                " ",
                "<¬a>$player_name</¬a>",
                "<gray>| </gray> Status: <¬a>$alive_string</¬a>",
                " ",
                "<¬a>Event</¬a>",
                "<gray>| </gray> Alive: <¬a>$alive_count</¬a>",
                "<gray>| </gray> Dead: <¬a>$dead_count</¬a>",
                "<gray>| </gray> Timer: <¬a>$timer</¬a>",
                "<gray>EvientsCore</gray>"
        );

        lines = lines.stream().map(x -> x
                .replaceAll("\\$player_name", player.getName())
                .replaceAll("\\$alive_string", plugin.listManager.alive.contains(player.getUniqueId()) ? "Alive" : "Dead")
                .replaceAll("\\$alive_count", String.valueOf(plugin.listManager.alive.size()))
                .replaceAll("\\$dead_count", String.valueOf(plugin.listManager.dead.size()))
                .replaceAll("\\$timer", timer == null ? "None"
                        : timer >= 60 ? (Math.round((float) timer / 60) + "m")
                        : timer + "s")
        ).toList();

        List<String> finalLines = lines;
        IntStream.range(0, lines.size())
                .forEach(i -> {
                    objective.getScore(plugin.chat.legacy(finalLines.get(i))).setScore(finalLines.size() - i);
                });
    }
}
