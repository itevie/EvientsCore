package rest.dawn.evientsCore.Util;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class UndoAction {
    public final List<Runnable> funcs;
    public final Instant setAt;
    public final String type;

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
