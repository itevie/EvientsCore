package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;
import rest.dawn.evientsCore.Util.Util;

public class TimerCommand implements CommandExecutor {
    private final EvientsCore plugin;
    private BukkitTask current;

    public TimerCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, Permissions.host("timer"))) return true;

        if (strings.length != 1) {
            plugin.chat.replyError(
                    commandSender,
                    "Please provide an amount like: /timer 20s, /timer 5m, /timer cancel"
            );
            return true;
        }

        String input = strings[0];
        if (input.equalsIgnoreCase("cancel")) {
            if (current == null) {
                plugin.chat.replyError(
                        commandSender,
                        "There is no timer!"
                );
            } else {
                stop();
            }
            return true;
        }

        if (current != null) {
            plugin.chat.replyError(
                    commandSender,
                    "A timer is already ticking!"
            );
            return true;
        }

        int seconds = Util.parseTimeInput(input);
        if (seconds == -1) {
            plugin.chat.replyError(
                    commandSender,
                    "Invalid time amount!"
            );
            return true;
        }

        start(seconds);
        return true;
    }

    private void start(int seconds) {
        plugin.chat.announce(
                "A new timer for <¬a>%s</¬a> has just started!",
                getTimeMessage(seconds)
        );

        current = new BukkitRunnable() {
            int secondsLeft = seconds;

            @Override
            public void run() {
                if (secondsLeft <= 0) {
                    plugin.chat.announce(
                            "The timer has finished!"
                    );
                    cancel();
                    current = null;
                    plugin.scoreboard.timer = null;
                    return;
                }

                String timeMessage = getTimeMessage(secondsLeft);
                if (timeMessage != null) {
                    plugin.chat.announce(
                            "There are <¬a>%s</¬a> remaining",
                            timeMessage
                    );
                }

                secondsLeft--;
                plugin.scoreboard.timer = secondsLeft;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private String getTimeMessage(int secondsLeft) {
        if (secondsLeft <= 10 || (secondsLeft < 60 && secondsLeft % 10 == 0)) {
            return secondsLeft + " seconds";
        } else if (secondsLeft >= 60 && secondsLeft % 60 == 0) {
            return Math.round((float) secondsLeft / 60) + " minutes";
        }
        return null;
    }

    private void stop() {
        if (current != null) {
            plugin.scoreboard.timer = null;
            current.cancel();
            current = null;
            plugin.chat.announce("The timer has been cancelled!");
        }
    }
}
