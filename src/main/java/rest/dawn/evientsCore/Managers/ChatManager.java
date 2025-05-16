package rest.dawn.evientsCore.Managers;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.HexToMinecraft;

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

    public String underString(String string) {
        return "\n     > <gray>" + string + "</gray>";
    }

    /// Used for replacing custom config colors which start with ¬
    /// ¬p = primary
    /// ¬a = accent
    /// ¬e = error
    public String parse(String string) {
        return string
                .replaceAll("/¬p", getTagName(plugin.config.chatColor))
                .replaceAll("/¬a", getTagName(plugin.config.accentColor))
                .replaceAll("/¬e", getTagName(plugin.config.errorColor))
                .replaceAll("¬p", plugin.config.chatColor)
                .replaceAll("¬a", plugin.config.accentColor)
                .replaceAll("¬e", plugin.config.errorColor);
    }

    private String parseLegacy(String string) {
        String chat = getLegacyColor(plugin.config.chatColor);
        String accent = getLegacyColor(plugin.config.accentColor);
        String error = getLegacyColor(plugin.config.errorColor);
        return string
                .replaceAll("¬p", chat)
                .replaceAll("¬a", accent)
                .replaceAll("¬e", error);
    }

    private String getLegacyColor(String string) {
        if (string.contains(":")) {
            String[] parts = string.split(":");
            return HexToMinecraft.convertHexToMiniMessageTag(parts[1]);
        } else {
            return string;
        }
    }

    private String getTagName(String tag) {
        int colonIndex = tag.indexOf(':');
        if (colonIndex != -1) {
            return "/" + tag.substring(0, colonIndex);
        }
        return "/" + tag;
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
                deserialize(parseLegacy(String.format("<¬p>" + format + "</¬p>", args)))
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

    public static String clickable(String command, String tooltip, String inner) {
        return String.format(
                "<click:run_command:%s><hover:show_text:%s>%s</hover></click>",
                command,
                tooltip,
                inner
        );
    }
}
