package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;
import rest.dawn.evientsCore.Util.Util;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class RevivePastCommand implements CommandExecutor {
    private final public EvientsCore plugin;

    public RevivePastCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (plugin.permissions.ensurePermission(commandSender, Permissions.host("revive.revivepast"))) return true;

        if (strings.length != 1) {
            plugin.chat.replyError(commandSender, "Please provide a a time frame! Like: /revivepast 5s");
            return true;
        }

        int seconds = Util.parseTimeInput(strings[0]);
        if (seconds == -1) {
            plugin.chat.replyError(commandSender, "Invalid time amount!");
            return true;
        }

        long cutoffTime = System.currentTimeMillis() - (seconds * 1000L);

        var players = plugin.listManager.deadTimes
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= cutoffTime)
                .map(Map.Entry::getKey)
                .toList();

        for (UUID uuid : players) {
            plugin.listManager.setAlive(uuid);
            var player = Bukkit.getPlayer(uuid);
            assert player != null;
            player.teleport(((Player) commandSender).getLocation());
        }

        plugin.chat.announce(
                "<¬a>%s</¬a> has revived <¬a>%d</¬a> from the last <¬a>%d seconds</¬a>",
                commandSender.getName(),
                players.size(),
                seconds,
                plugin.chat.underString(
                        String.join(", ", Util.uuidsToUsernameString(new HashSet<>(players)))
                )
        );
        return true;
    }
}
