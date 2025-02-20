package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimerCommand implements CommandExecutor {
    EvientsCore plugin;
    BukkitTask current;

    public TimerCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(plugin.chat.error("Please provide an amount like: /timer 20s, /timer 5m, /timer cancel"));
            return true;
        }

        if (strings[0].equalsIgnoreCase("cancel")) {
            this.stop();
            return true;
        }

        if (current != null) {
            commandSender.sendMessage(plugin.chat.error("A timer is already ticking!"));
            return true;
        }

        int seconds = this.parseTimeInput(strings[0]);
        if (seconds == -1) {
            commandSender.sendMessage(plugin.chat.error("Invalid time amount!"));
            return true;
        }

        this.start(seconds);
        return true;
    }

    private void start(int seconds) {
        EvientsCore plugin = this.plugin;

        String amount = seconds > 60
                ? Math.round((float) seconds / 60) + " minutes"
                : seconds + " seconds";

        plugin.chat.announce(
                plugin.chat.primary(
                        "A new timer for ",
                        plugin.chat.accent(amount),
                        " has just begun!"
                )
        );

        this.current = new BukkitRunnable() {
            int secondsLeft = seconds;

            @Override
            public void run() {
                if (secondsLeft <= 0) {
                    plugin.chat.announce(
                            plugin.chat.primary("The timer has finished!")
                    );
                    cancel();
                    current = null;
                    plugin.scoreboard.timer = null;
                    return;
                }

                if (secondsLeft >= 60 && secondsLeft % 60 == 0) {
                    int minutes = Math.round((float) secondsLeft / 60);
                    plugin.chat.announce(
                            plugin.chat.primary(
                                    "There are ",
                                    plugin.chat.accent(minutes + " minutes"),
                                    " remaining!"
                            )
                    );
                } else if (secondsLeft < 60 && secondsLeft > 10 && secondsLeft % 10 == 0) {
                    plugin.chat.announce(
                            plugin.chat.primary(
                                    "There are ",
                                    plugin.chat.accent(secondsLeft + " seconds"),
                                    " remaining!"
                            )
                    );
                } else if (secondsLeft <= 10) {
                    plugin.chat.announce(
                            plugin.chat.primary(
                                    "There are ",
                                    plugin.chat.accent(secondsLeft + " seconds"),
                                    " remaining!"
                            )
                    );
                }

                secondsLeft--;
                plugin.scoreboard.timer = secondsLeft;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void stop() {
        if (this.current != null) {
            plugin.scoreboard.timer = null;
            this.current.cancel();
            this.current = null;
            this.plugin.chat.announce(
                    this.plugin.chat.primary("The timer has been cancelled!")
            );
        }
    }

    // Stolen from ChatGPT
    private int parseTimeInput(String input) {
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
}
