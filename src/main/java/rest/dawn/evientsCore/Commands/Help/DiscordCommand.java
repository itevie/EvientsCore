package rest.dawn.evientsCore.Commands.Help;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.HideMode;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class DiscordCommand implements CommandExecutor {
    EvientsCore plugin;

    public DiscordCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = plugin.permissions.requirePlayer(commandSender);
        if (player == null) return true;

        var mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize("<green>Join the Discord <u><click:open_url:\"https://discord.gg/brEcvy7A7y\">here</click></u>!</green>");

        ((Audience)player).sendMessage(parsed);
        return true;
    }
}
