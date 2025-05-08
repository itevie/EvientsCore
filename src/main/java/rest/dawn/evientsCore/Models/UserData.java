package rest.dawn.evientsCore.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserData {
    public UUID uuid;
    public boolean dmsDisabled;

    public static UserData fromResultSet(ResultSet rs) throws SQLException {
        UserData ud = new UserData();
        ud.dmsDisabled = rs.getBoolean("dms_disabled");
        ud.uuid = UUID.fromString(rs.getString("uuid"));
        return ud;
    }
}
