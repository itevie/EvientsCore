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
            plugin.chat.replyError(
                    sender,
                    "You cannot send DMs as you have disabled them!"
            );
            return;
        }

        if (recipientUserData.dmsDisabled && !isHost) {
            plugin.chat.replyError(
                    sender,
                    "They have DMs disabled!"
            );
            return;
        }

        plugin.chat.reply(sender, generateMessage("me", recipient.getName(), message));
        plugin.chat.reply(recipient, generateMessage(sender.getName(), "me", message));
    }

    private String generateMessage(String player1, String player2, String message) {
        return String.format(
                "<gray>[<¬a>%s</¬a> -> <¬a>%s</¬a>]</gray> <white>%s</white>",
                player1,
                player2,
                message
        );
    }
}
