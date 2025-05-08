package rest.dawn.evientsCore.Managers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.UserData;
import rest.dawn.evientsCore.Util.Util;

public class PrivateMessageManager {
    private final EvientsCore plugin;

    public PrivateMessageManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public void send(Player sender, Player recipient, String message) {
        UserData senderUserData = plugin.userData.getFor(sender.getUniqueId());
        UserData recipientUserData = plugin.userData.getFor(recipient.getUniqueId());
        boolean isHost = Util.userIsHost(sender.getUniqueId());

        if (senderUserData.dmsDisabled && !isHost) {
            sender.sendMessage(plugin.chat.error(
                "You cannot send DMs as you have disabled them!"
            ));
            return;
        }

        if (recipientUserData.dmsDisabled && !isHost) {
            sender.sendMessage(plugin.chat.error(
                    "They have DMs disabled!"
            ));
            return;
        }


        sender.sendMessage(generateMessage("me", recipient.getName(), message));
        recipient.sendMessage(generateMessage(sender.getName(), "me", message));
    }

    private String generateMessage(String player1, String player2, String message) {
        return plugin.chat.primary(
                ChatColor.GRAY + "[",
                plugin.chat.accent(player1),
                ChatColor.GRAY + " -> ",
                plugin.chat.accent(player2),
                ChatColor.GRAY + "] ",
                ChatColor.WHITE + message
        );
    }
}
