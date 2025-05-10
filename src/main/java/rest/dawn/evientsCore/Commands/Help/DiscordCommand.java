package rest.dawn.evientsCore.Commands.Help;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;

public class DiscordCommand implements CommandExecutor {
    EvientsCore plugin;

    public DiscordCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        plugin.chat.reply(
                commandSender,
                "Join the Discord <u><click:open_url:\"https://discord.gg/brEcvy7A7y\">here</click></u>!"
        );
        return true;
    }
}
