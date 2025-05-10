package rest.dawn.evientsCore.Managers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.EvientsCore;

import javax.annotation.Nullable;

public class PermissionManager {
    private final EvientsCore plugin;

    public PermissionManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public boolean ensurePermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            plugin.chat.replyError(
                    sender,
                    "You do not have permission to do that!"
            );
            return false;
        }
        return true;
    }

    /// Checks if the sender is a player, if not it auto responds and returns null
    public @Nullable Player requirePlayer(CommandSender sender) {
        if (sender instanceof Player player) {
            return player;
        } else {
            plugin.chat.replyError(
                    sender,
                    "This command can only be sent in-game!"
            );
            return null;
        }
    }
}
