package rest.dawn.evientsCore.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rest.dawn.evientsCore.Util.PlayerType;
import rest.dawn.evientsCore.Util.Util;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ListManager {
    public Set<UUID> alive = new HashSet<>();
    public Set<UUID> dead = new HashSet<>();
    public Map<UUID, Long> deadTimes = new HashMap<>();

    public void reload() {
        Set<UUID> online = Bukkit.getOnlinePlayers().stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toSet());

        for (UUID player : online) {
            boolean isHost = Util.userIsHost(player);

            if (!alive.contains(player) && !dead.contains(player) && !Util.userIsHost(player)) {
                dead.add(player);
            }

            if (alive.contains(player) && isHost) {
                alive.remove(player);
            }

            if (dead.contains(player) && isHost) {
                dead.remove(player);
            }
        }

        dead.removeIf(uuid -> !online.contains(uuid));
        alive.removeIf(uuid -> !online.contains(uuid));
    }

    public void initCacheReloader() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                reload();
            }
        };

        timer.schedule(task, 0, 10000);
    }

    public void setAlive(UUID player) {
        if (Util.userIsHost(player)) return;
        dead.remove(player);
        deadTimes.remove(player);
        alive.add(player);
    }

    public void setDead(UUID player) {
        if (Util.userIsHost(player)) return;
        alive.remove(player);
        dead.add(player);
        deadTimes.put(player, Instant.now().getEpochSecond());
    }

    public void remove(UUID player) {
        alive.remove(player);
        dead.remove(player);
        deadTimes.remove(player);
    }

    public Set<UUID> getPlayersFromPlayerType(PlayerType type) {
        reload();
        switch (type) {
            case PlayerType.ALL:
                Set<UUID> all = new HashSet<>(alive);
                all.addAll(dead);
                return all;
            case PlayerType.DEAD:
                return dead;
            case PlayerType.ALIVE:
                return alive;
            case PlayerType.RANDOM:
                Set<UUID> allRandom = new HashSet<>(alive);
                allRandom.addAll(dead);
                if (allRandom.isEmpty()) return null;
                return new HashSet<>(Collections.singletonList(Util.getRandomElement(allRandom)));
            case PlayerType.RANDOMALIVE:
                if (alive.isEmpty()) return null;
                return new HashSet<>(Collections.singletonList(Util.getRandomElement(alive)));
            case PlayerType.RANDOMDEAD:
                if (dead.isEmpty()) return null;
                return new HashSet<>(Collections.singletonList(Util.getRandomElement(dead)));

        }

        return new HashSet<>();
    }
}
