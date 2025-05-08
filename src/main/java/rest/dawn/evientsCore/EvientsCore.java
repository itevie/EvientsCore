package rest.dawn.evientsCore;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import rest.dawn.evientsCore.Commands.CoreEvent.*;
import rest.dawn.evientsCore.Commands.CoreEvent.AddWinCommand;
import rest.dawn.evientsCore.Commands.DirectMessages.MessageCommand;
import rest.dawn.evientsCore.Commands.DirectMessages.ToggleMessagesCommand;
import rest.dawn.evientsCore.Commands.Help.DiscordCommand;
import rest.dawn.evientsCore.Commands.Wins.WinsCommand;
import rest.dawn.evientsCore.Commands.Warps.AddWarpCommand;
import rest.dawn.evientsCore.Commands.Warps.DeleteWarpCommand;
import rest.dawn.evientsCore.Commands.Warps.WarpCommand;
import rest.dawn.evientsCore.Commands.Warps.WarpListCommand;
import rest.dawn.evientsCore.Managers.*;
import rest.dawn.evientsCore.Models.Warp;
import rest.dawn.evientsCore.Util.Util;

import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

public final class EvientsCore extends JavaPlugin implements Listener {
    public DatabaseManager database = new DatabaseManager(this);
    public ListManager listManager = new ListManager(this);
    public ConfigManager config = new ConfigManager(this);
    public ChatManager chat = new ChatManager(this);
    public StateManager state = new StateManager(this);
    public ScoreboardManager scoreboard = new ScoreboardManager(this);
    public WarpManager warps = new WarpManager(this);
    public WinManager wins = new WinManager(this);
    public HideManager hides = new HideManager(this);
    public RandomAnnouncementManager randomAnnouncements = new RandomAnnouncementManager(this);
    public PrivateMessageManager privateMessages = new PrivateMessageManager(this);
    public UserDataManager userData = new UserDataManager(this);

    public Map<UUID, Long> leaveTimes = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        scoreboard.start();
        listManager.initCacheReloader();
        randomAnnouncements.start();

        try {
            Map<String, Class<? extends CommandExecutor>> commandMap = new HashMap<>() {{
                put("alive", AliveListCommand.class);
                put("dead", DeadListCommand.class);
                put("markdead", MarkDeadCommand.class);
                put("timer", TimerCommand.class);
                put("hide", HideCommand.class);
                put("mutechat", MutechatCommand.class);
                put("setwarp", AddWarpCommand.class);
                put("warp", WarpCommand.class);
                put("warplist", WarpListCommand.class);
                put("delwarp", DeleteWarpCommand.class);
                put("forcehide", ForceHideCommand.class);
                put("addwin", AddWinCommand.class);
                put("wins", WinsCommand.class);
                put("rejoins", RejoinsCommand.class);
                put("undocommand", UndoCommand.class);
                put("discord", DiscordCommand.class);
                put("message", MessageCommand.class);
                put("togglemessages", ToggleMessagesCommand.class);
            }};

            for (var entry : commandMap.entrySet()) {
                try {
                    getLogger().info("Loading command " + entry.getKey());
                    getCommand(entry.getKey()).setExecutor(entry.getValue().getDeclaredConstructor(getClass()).newInstance(this));
                } catch (Exception e) {
                    getLogger().warning("Failed to load command " + entry.getKey() + "\n " + e.toString());
                }
            }

            // ----- Multies -----
            Util.loadManyCommandsInto(this, new RegionFlagCommands(this), new String[]
                    {"pvp", "break", "build", "falldamage"}
            );
            Util.loadManyCommandsInto(this, new MultiPlayerCommands(this), new String[]
                    {
                            "tpall", "tpdead", "tpalive", "tprandom", "tprandomalive", "tprandomdead",

                            "revive", "reviveall", "reviverandomdead", "revivedead",

                            "clear", "clearall", "clearalive", "cleardead",

                            "give", "giveall", "givedead", "givealive", "giverandom", "giverandomdead", "giverandomalive",

                            "kill", "killall", "killdead", "killalive", "killrandom", "killrandomalive", "killrandomdead"
                    }
            );
        } catch (NullPointerException e) {
            getLogger().warning(e.toString());
        }

        getLogger().info("Loaded EvientsCore!");
    }

    @Override
    public void onDisable() {
        try {
            database.connection.close();
        } catch (SQLException e) {
            getLogger().warning("Failed to close database connection");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        this.scoreboard.createScoreboardFor(event.getPlayer());
        System.out.println(String.join(",", leaveTimes.keySet().stream().map(UUID::toString).toArray(String[]::new)));
        if (!leaveTimes.containsKey(uuid)) {
            this.listManager.setDead(event.getPlayer().getUniqueId());
        } else {
            if (state.rejoinsDisabled) {
                listManager.setDead(uuid);
                if (config.spawnWarp != null) {
                    Warp warp = warps.getWarp(config.spawnWarp);
                    if (warp != null) {
                        warp.teleport(event.getPlayer());
                        getLogger().info("Teleporting " + uuid + " to spawnWarp as rejoins are disabled");
                    } else {
                        event.getPlayer().damage(10000);
                        getLogger().info("Killing " + uuid + " as there is no spawnWawrp and rejoins are disabled");
                    }
                } else {
                    event.getPlayer().damage(10000);
                    getLogger().info("Killing " + uuid + " as there is no spawnWawrp and rejoins are disabled");
                }

                event.getPlayer().sendMessage(chat.primary(
                        "You have been sent to spawn as rejoins are disabled"
                ));
            } else {
                chat.announce(
                        chat.primary(
                                chat.accent(event.getPlayer().getName()),
                                " has joined back in ",
                                chat.accent(String.valueOf(Instant.now().getEpochSecond() - leaveTimes.get(uuid))),
                                " seconds!"
                        )
                );
            }
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
