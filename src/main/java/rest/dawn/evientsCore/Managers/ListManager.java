package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.Util.PlayerType;
import rest.dawn.evientsCore.Util.Util;

import java.time.Instant;
import java.util.*;

public class ListManager {
    public Set<UUID> alive = new HashSet<>();
    public Set<UUID> dead = new HashSet<>();
    public Map<UUID, Long> deadTimes = new HashMap<>();

    public void setAlive(UUID player) {
        dead.remove(player);
        deadTimes.remove(player);
        alive.add(player);
    }

    public void setDead(UUID player) {
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
