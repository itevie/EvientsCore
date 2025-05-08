package rest.dawn.evientsCore.Models;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public class Win {
    public final UUID uuid;
    public final Instant date;
    public final @Nullable String eventName;

    public Win(UUID uuid) {
        this.uuid = uuid;
        this.date = Instant.now();
        this.eventName = null;
    }

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

    public void apply(PreparedStatement stmt) throws SQLException {
        stmt.setString(1, this.uuid.toString());
        stmt.setString(2, this.date.toString());
        stmt.setString(3, this.eventName);
    }

    public static Win fromResultSet(ResultSet rs) throws SQLException {
        return new Win(
            rs.getString("uuid"),
            rs.getString("added_at"),
            rs.getString("event_name")
        );
    }
}
