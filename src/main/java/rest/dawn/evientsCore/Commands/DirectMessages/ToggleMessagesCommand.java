package rest.dawn.evientsCore.Commands.DirectMessages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.UserData;
import rest.dawn.evientsCore.Util.HideMode;

public class ToggleMessagesCommand implements CommandExecutor {
    EvientsCore plugin;

    public ToggleMessagesCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = plugin.permissions.requirePlayer(commandSender);
        if (player == null) return true;

        UserData userData = plugin.userData.getFor(player.getUniqueId());
        try {
            plugin.userData.setDmsDisabled(player.getUniqueId(), !userData.dmsDisabled);
        } catch (Exception e) {
            plugin.userData.setDmsDisabled(player.getUniqueId(), !userData.dmsDisabled);
        }

        commandSender.sendMessage(plugin.chat.primary(
                "You have ",
                plugin.chat.accent(userData.dmsDisabled ? "enabled" : "disabled"),
                " direct messages!"
        ));
        return true;
    }
}
