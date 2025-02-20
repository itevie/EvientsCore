package rest.dawn.evientsCore;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import rest.dawn.evientsCore.Commands.CoreEvent.*;
import rest.dawn.evientsCore.Managers.*;
import rest.dawn.evientsCore.Util.Util;

import java.time.Instant;
import java.util.*;

public final class EvientsCore extends JavaPlugin implements Listener {
    public ListManager listManager = new ListManager();
    public ConfigManager config = new ConfigManager(this);
    public ChatManager chat = new ChatManager(this);
    public StateManager state = new StateManager(this);
    public ScoreboardManager scoreboard = new ScoreboardManager(this);
    public WarpManager warps = new WarpManager(this);

    public Map<UUID, Long> leaveTimes = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        scoreboard.start();

        for (var player : Bukkit.getOnlinePlayers()) {
            listManager.setDead(player.getUniqueId());
        }

        try {
            // ----- Singles -----
            getCommand("alive").setExecutor(new AliveListCommand(this));
            getCommand("dead").setExecutor(new DeadListCommand(this));
            getCommand("revive").setExecutor(new ReviveCommand(this));
            getCommand("markdead").setExecutor(new MarkDeadCommand(this));
            getCommand("timer").setExecutor(new TimerCommand(this));
            getCommand("hide").setExecutor(new HideCommand(this));
            getCommand("mutechat").setExecutor(new MutechatCommand(this));
            getCommand("setwarp").setExecutor(new AddWarpCommand(this));
            getCommand("warp").setExecutor(new WarpCommand(this));
            getCommand("warplist").setExecutor(new WarpListCommand(this));
            getCommand("delwarp").setExecutor(new DeleteWarpCommand(this));

            // ----- Multies -----
            Util.loadManyCommandsInto(this, new RegionFlagCommands(this), new String[]
                    {"pvp", "break", "build", "falldamage"}
            );
            Util.loadManyCommandsInto(this, new MultiPlayerCommands(this), new String[]
                    {
                            "tpall", "tpdead", "tpalive", "tprandom", "tprandomalive", "tprandomdead",

                            "reviveall", "reviverandomdead",

                            "clearall", "clearalive", "cleardead",

                            "killall", "killdead", "killalive", "killrandom", "killrandomalive", "killrandomdead"
                    }
            );
        } catch (NullPointerException e) {
            getLogger().warning(e.toString());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        this.scoreboard.createScoreboardFor(event.getPlayer());

        if (!leaveTimes.containsKey(uuid)) {
            this.listManager.setDead(event.getPlayer().getUniqueId());
        } else {
            chat.announce(
                    chat.primary(
                            chat.accent(event.getPlayer().getName()),
                            " has joined back in ",
                            chat.accent(String.valueOf(Instant.now().getEpochSecond() - leaveTimes.get(uuid))),
                            " seconds!"
                    )
            );
            leaveTimes.remove(event.getPlayer().getUniqueId());
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (listManager.alive.contains(uuid)) {
            leaveTimes.put(uuid, Instant.now().getEpochSecond());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.listManager.setDead(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (state.chatMuted && !event.getPlayer().hasPermission("evients.host")) {
            event.getPlayer().sendMessage(chat.error("Sorry, but chat is muted!"));
            event.setCancelled(true);
        }
    }
}
