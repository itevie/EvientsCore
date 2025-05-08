package rest.dawn.evientsCore.Managers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import rest.dawn.evientsCore.EvientsCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        Objective objective = board.registerNewObjective("evients", Criteria.DUMMY, plugin.config.accentColor + "Evients");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateScoreboardFor(player, board, objective);
        player.setScoreboard(board);
        scoreboards.put(player.getUniqueId(), board);
    }

    private void updateScoreboardFor(Player player, Scoreboard scoreboard, Objective objective) {
        scoreboard.getEntries().forEach(scoreboard::resetScores);

        String[] lines = {
                " ",

                plugin.chat.accent(player.getName()),

                ChatColor.GRAY + "| " + plugin.chat.primary("Status: ")
                        + plugin.chat.accent(plugin.listManager.alive.contains(player.getUniqueId()) ? "Alive" : "Dead"),

                " ",
                plugin.chat.accent("Event"),

                ChatColor.GRAY + "| " + plugin.chat.primary("Alive: ")
                        + plugin.chat.accent(String.valueOf(plugin.listManager.alive.size())),
                ChatColor.GRAY + "| " + plugin.chat.primary("Dead: ")
                        + plugin.chat.accent(String.valueOf(plugin.listManager.dead.size())),
                ChatColor.GRAY + "| " + plugin.chat.primary("Timer: ")
                        + plugin.chat.accent(timer == null ? "None"
                        : timer >= 60 ? (Math.round((float) timer / 60) + "m")
                        : timer + "s"),
                ChatColor.GRAY + "EvientsCore"
        };

        for (int i = 0; i < lines.length; i++) {
            objective.getScore(lines[i]).setScore(lines.length - i);
        }
    }
}
