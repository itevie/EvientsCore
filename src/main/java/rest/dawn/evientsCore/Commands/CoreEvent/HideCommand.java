package rest.dawn.evientsCore.Commands.CoreEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.HideMode;


public class HideCommand implements CommandExecutor {
    private static final String usage = "Usage: /hide \\<all|staff|off\\>";
    EvientsCore plugin;

    public HideCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            plugin.chat.replyError(commandSender, HideCommand.usage);
            return true;
        }

        switch (strings[0]) {
            case "staff":
                plugin.hides.setHideMode((Player)commandSender, HideMode.Staff);
                break;
            case "all":
                plugin.hides.setHideMode((Player)commandSender, HideMode.All);
                break;
            case "off":
                plugin.hides.setHideMode((Player)commandSender, HideMode.Off);
                break;
            default:
                plugin.chat.replyError(commandSender, HideCommand.usage);
                return true;
        }

        plugin.chat.reply(commandSender,
                "Updated your hide mode to <¬a>%s</¬a>!",
                strings[0]
        );
        return true;
    }
}
