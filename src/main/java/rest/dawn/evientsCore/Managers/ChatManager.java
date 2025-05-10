package rest.dawn.evientsCore.Managers;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.EvientsCore;

import java.util.Arrays;

public class ChatManager {
    private final EvientsCore plugin;

    public ChatManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public void announce(String format, Object... args) {
        Component result = deserialize(
                "<white>\n %s</white> %s\n",
                plugin.config.announcementPrefix,
                String.format(format, args)
        );
        Bukkit.getOnlinePlayers().forEach(x -> send((Audience)x, result));
        playSoundToAll();
    }

    public void playSoundToAll() {
        var players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
    }

    public static String applyColor(ChatColor color, String... parts) {
        return String.join("", Arrays.stream(parts).map(old -> color + old).toArray(String[]::new));
    }

    public String underString(String string) {
        return "\n     > <gray>" + string + "</gray>";
    }

    /// Used for replacing custom config colors which start with ¬
    /// ¬p = primary
    /// ¬a = accent
    /// ¬e = error
    public String parse(String string) {
        return string
                .replaceAll("¬p", plugin.config.chatColor)
                .replaceAll("¬a", plugin.config.accentColor)
                .replaceAll("¬e", plugin.config.errorColor);
    }

    private Component deserializeWrap(String string, String wrap, Object... args) {
        var mm = MiniMessage.miniMessage();
        return mm.deserialize(parse("<" + wrap + ">" + String.format(string, args) + "</" + wrap + ">"));
    }

    /// Deserializes a format string into an Adventure component
    public Component deserialize(String string, Object... args) {
        return deserializeWrap(string, "¬p", args);
    }

    /// Converts from adventure to legacy color codes
    /// It goes from string -> component -> legacy string
    public String legacy(String format, Object... args) {
        return LegacyComponentSerializer.legacySection().serialize(
                deserialize(format, args)
        );
    }

    /// Converts from adventure to legacy color codes
    /// It goes from component -> legacy string
    public String legacy(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public void send(Audience player, Component component) {
        player.sendMessage(component);
    }

    public void reply(Player player, String format, Object... args) {
        send((Audience)player, deserialize(format, args));
    }

    public void reply(CommandSender sender, String format, Object... args) {
        reply((Player)sender, format, args);
    }

    public void replyError(CommandSender sender, String format, Object... args) {
        send((Audience)sender, deserializeWrap(format, "¬e", args));
    }
}
