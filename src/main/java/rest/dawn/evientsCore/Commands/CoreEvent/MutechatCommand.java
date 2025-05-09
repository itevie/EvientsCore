package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

public class MutechatCommand implements CommandExecutor {
    EvientsCore plugin;

    public MutechatCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, "evients.host.mutechat")) return true;

        plugin.state.chatMuted = !plugin.state.chatMuted;
        plugin.chat.announce(
                plugin.chat.primary(
                        plugin.chat.accent(commandSender.getName()),
                        " has ",
                        plugin.state.chatMuted
                        ? (ChatColor.RED + "muted")
                                : (ChatColor.GREEN + "unmuted"),
                        " the chat!"
                )
        );

        return true;
    }
}
