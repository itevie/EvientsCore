package rest.dawn.evientsCore.Models;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

public class Win {
    public final UUID uuid;
    public final Instant date;
    public final @Nullable String eventName;

    public Win(UUID uuid, Instant date, @Nullable String eventName) {
        this.uuid = uuid;
        this.date = date;
        this.eventName = eventName;
    }

    public Win(String uuid, String date, @Nullable String eventName) {
        this.uuid = UUID.fromString(uuid);
        this.date = Instant.parse(date);
        this.eventName = eventName;
    }
}
