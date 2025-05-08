package rest.dawn.evientsCore.Util;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

public class UndoAction {
    public List<Runnable> funcs;
    public Instant setAt;
    public String type;

    public UndoAction(List<Runnable> funcs, String type) {
        this.funcs = funcs;
        this.setAt = Instant.now();
        this.type = type;
    }

    public boolean expired() {
        return setAt.isBefore(Instant.now().minus(Duration.ofMinutes(1)));
    }

    public void run() {
        for (Runnable func : funcs) {
            func.run();
        }
    }
}
