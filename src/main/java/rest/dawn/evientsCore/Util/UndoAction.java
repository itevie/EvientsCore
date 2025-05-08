package rest.dawn.evientsCore.Util;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class UndoAction {
    public Consumer<Player> func;
    public List<Player> players;

    public UndoAction(Consumer<Player> func, List<Player> players) {
        this.func = func;
        this.players = players;
    }

    public void run() {
        for (Player player : players) {
            func.accept(player);
        }
    }
}
