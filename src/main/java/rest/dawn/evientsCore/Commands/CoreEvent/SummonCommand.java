package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

public class SummonCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public SummonCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = plugin.permissions.requirePlayer(commandSender);
        if (player == null) {
            return true;
        }

        if (strings.length == 0) {
            plugin.chat.replyError(commandSender, "Usage: /summon <entity> [amount]");
            return true;
        }

        String entityName = strings[0].toUpperCase();
        int amount = 1;

        if (strings.length > 1) {
            try {
                amount = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                plugin.chat.replyError(player, "Invalid amount: " + strings[1]);
                return true;
            }
        }

        EntityType type;
        try {
            type = EntityType.valueOf(entityName);
        } catch (IllegalArgumentException e) {
            plugin.chat.replyError(player, "Unknown entity: " + entityName);
            return true;
        }

        for (int i = 0; i < amount; i++) {
            player.getWorld().spawnEntity(player.getLocation(), type);
        }

        plugin.chat.reply(commandSender, "Summoned <¬a>%d</¬a> <¬a>%s</¬a>", amount, entityName);
        return true;
    }
}
