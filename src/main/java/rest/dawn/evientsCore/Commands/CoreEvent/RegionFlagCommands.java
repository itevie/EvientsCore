package rest.dawn.evientsCore.Commands.CoreEvent;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class RegionFlagCommands implements CommandExecutor {
    public EvientsCore plugin;

    public RegionFlagCommands(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!plugin.permissions.ensurePermission(commandSender, Permissions.host("region"))) return true;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        RegionManager regionManager = container.get(
                BukkitAdapter.adapt(((Player) commandSender).getWorld())
        );
        Location playerLocation = BukkitAdapter.adapt(((Player) commandSender).getLocation());

        assert regionManager != null;
        ApplicableRegionSet regions = regionManager.getApplicableRegions(playerLocation.toVector().toBlockPoint());

        if (regions.size() == 0) {
            commandSender.sendMessage(plugin.chat.error("You are not in a region!"));
            return true;
        }

        ProtectedRegion last = null;
        for (var region : regions) {
            last = region;
        }

        if (last == null) {
            commandSender.sendMessage(plugin.chat.error("An unknown error occurred."));
            return true;
        }

        StateFlag flag;

        switch (command.getName()) {
            case "pvp":
                flag = Flags.PVP;
                break;
            case "break":
                flag = Flags.BLOCK_BREAK;
                break;
            case "build":
                flag = Flags.BLOCK_PLACE;
                break;
            case "falldamage":
                flag = Flags.FALL_DAMAGE;
                break;
            default:
                commandSender.sendMessage(plugin.chat.error("An unknown error occurred."));
                return true;
        }

        var old = last.getFlag(flag);
        last.setFlag(flag, old == StateFlag.State.ALLOW ? StateFlag.State.DENY : StateFlag.State.ALLOW);

        plugin.chat.announce(
                plugin.chat.primary(
                        plugin.chat.accent(commandSender.getName()),
                        " set ",
                        plugin.chat.accent(flag.getName()),
                        " to ",
                        (old == StateFlag.State.ALLOW
                                ? (ChatColor.RED + "DENY")
                                        : (ChatColor.GREEN + "ALLOW")
                        ),
                        " in region ",
                        plugin.chat.accent(last.getId()),
                        "!"
                )
        );

        return true;
    }
}
