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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.Permissions;

public class RegionFlagCommands implements CommandExecutor {
    private final EvientsCore plugin;

    public RegionFlagCommands(EvientsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (plugin.permissions.ensurePermission(commandSender, Permissions.host("region"))) return true;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        RegionManager regionManager = container.get(
                BukkitAdapter.adapt(((Player) commandSender).getWorld())
        );
        Location playerLocation = BukkitAdapter.adapt(((Player) commandSender).getLocation());

        assert regionManager != null;
        ApplicableRegionSet regions = regionManager.getApplicableRegions(playerLocation.toVector().toBlockPoint());

        if (regions.size() == 0) {
            plugin.chat.replyError(commandSender, "You are not in a region!");
            return true;
        }

        ProtectedRegion last = null;
        for (var region : regions) {
            last = region;
        }

        if (last == null) {
            plugin.chat.replyError(commandSender, "An unknown error occurred.");
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
            case "mobspawning":
                flag = Flags.MOB_SPAWNING;
                break;
            default:
                plugin.chat.replyError(commandSender, "An unknown error occurred.");
                return true;
        }

        var old = last.getFlag(flag);
        last.setFlag(flag, old == StateFlag.State.ALLOW ? StateFlag.State.DENY : StateFlag.State.ALLOW);

        plugin.chat.announce(
                "<¬a>%s</¬a> set <¬a>%s</¬a> to %s in region <¬a>%s</¬a>!",
                commandSender.getName(),
                flag.getName(),
                old == StateFlag.State.ALLOW
                ? "<red>DENY</red>"
                        : "<green>ALLOW</green>",
                last.getId()
        );

        return true;
    }
}
