package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class UndoCommand implements CommandExecutor {
    EvientsCore plugin;

    public UndoCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, Permissions.host("undo"))) return true;

        if (plugin.state.undo == null || plugin.state.undo.expired()) {
            plugin.chat.replyError(commandSender, "Nothing to undo!");
            return true;
        }

        plugin.state.undo.run();
        plugin.chat.announce(
                "<¬a>%s</¬a> undid the last <¬a>%s</¬a> command!",
                commandSender.getName(),
                plugin.state.undo.type
        );
        return true;
    }
}
