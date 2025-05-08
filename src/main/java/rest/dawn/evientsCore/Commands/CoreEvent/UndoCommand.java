package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.HideMode;

public class UndoCommand implements CommandExecutor {
    EvientsCore plugin;

    public UndoCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (plugin.state.undo == null || plugin.state.undo.expired()) {
            commandSender.sendMessage(plugin.chat.error(
                    "Nothing to undo!"
            ));
            return true;
        }

        plugin.state.undo.run();
        plugin.chat.announce(plugin.chat.primary(
                plugin.chat.accent(commandSender.getName()),
                " undid the last ",
                plugin.chat.accent(plugin.state.undo.type),
                " command!"
        ));
        return true;
    }
}
