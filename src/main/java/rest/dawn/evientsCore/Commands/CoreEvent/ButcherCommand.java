package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

public class ButcherCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public ButcherCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int count = 0;

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (shouldKill(entity)) {
                    entity.remove();
                    count++;
                }
            }
        }

        plugin.chat.reply(sender, "Removed " + count + " entities.");
        return true;
    }

    private boolean shouldKill(Entity entity) {
        return entity.getType() != EntityType.PLAYER;
    }
}