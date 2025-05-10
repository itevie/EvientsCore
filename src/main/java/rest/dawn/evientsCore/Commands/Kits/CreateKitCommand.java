package rest.dawn.evientsCore.Commands.Kits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class CreateKitCommand implements CommandExecutor {
    EvientsCore plugin;

    public CreateKitCommand(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, Permissions.host("kits.create"))) return true;
        Player player = plugin.permissions.requirePlayer(commandSender);
        if (player == null) return true;

        if (strings.length != 1) {
            plugin.chat.replyError(
                    commandSender,
                    "Please provide a name for your kit!"
            );
            return true;
        }

        if (plugin.kits.kitExists(strings[0])) {
            plugin.chat.replyError(
                    commandSender,
                    "A kit with that name already exists!"
            );
            return true;
        }

        ItemStack[] main = player.getInventory().getContents(); // 0–35
        ItemStack[] kitItems = new ItemStack[41];

        System.arraycopy(main, 0, kitItems, 0, 36); // copy main inventory

        kitItems[36] = player.getInventory().getHelmet();
        kitItems[37] = player.getInventory().getChestplate();
        kitItems[38] = player.getInventory().getLeggings();
        kitItems[39] = player.getInventory().getBoots();
        kitItems[40] = player.getInventory().getItemInOffHand();

        plugin.kits.createKit(strings[0], kitItems);
        plugin.chat.reply(
                commandSender,
                "Kit <¬a>%s</¬a> has been created!",
                strings[0]
        );
        return true;
    }
}
