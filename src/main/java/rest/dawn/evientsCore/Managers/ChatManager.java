package rest.dawn.evientsCore.Managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.EvientsCore;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ChatManager {
    private final EvientsCore plugin;

    public ChatManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public void announce(String what) {
        Bukkit.broadcastMessage("\n " + plugin.config.announcementPrefix + " " + plugin.config.chatColor + what + "\n");
        playSoundToAll();
    }

    public void playSoundToAll() {
        var players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
    }

    public static String applyColor(ChatColor color, String... parts) {
        return String.join("", Arrays.stream(parts).map(old -> color + old).toArray(String[]::new));
    }

    public String primary(String... parts) {
        return ChatManager.applyColor(plugin.config.chatColor, parts);
    }

    public String error(String ...parts) {
        return ChatManager.applyColor(plugin.config.errorColor, parts);
    }

    public String accent(String ...parts) {
        return ChatManager.applyColor(plugin.config.accentColor, parts);
    }

    public static String usernameString(String playerName) {
        return ChatColor.YELLOW + playerName + ChatColor.WHITE;
    }

    public String underString(String string) {
        return "\n     > " +
        ChatColor.GRAY.toString() + string;
    }



    public @Nullable Player requirePlayer(CommandSender sender) {
        if (sender instanceof Player player) {
            return player;
        } else {
            sender.sendMessage(plugin.chat.error(
                    "This command can only be sent in-game!"
            ));
            return null;
        }
    }
}
