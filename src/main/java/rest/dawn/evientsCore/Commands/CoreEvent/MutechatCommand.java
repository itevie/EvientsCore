package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class MutechatCommand implements CommandExecutor {
    private final EvientsCore plugin;

    public MutechatCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (plugin.permissions.ensurePermission(commandSender, Permissions.host("mutechat"))) return true;

        plugin.state.chatMuted = !plugin.state.chatMuted;
        plugin.chat.announce(
                "<¬a>%s</¬a> has %s the chat!",
                commandSender.getName(),
                plugin.state.chatMuted
                ? "<red>muted</red>"
                        : "<green>unmuted</green>"
        );

        return true;
    }
}
