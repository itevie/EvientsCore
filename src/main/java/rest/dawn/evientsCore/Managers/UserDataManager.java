package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDataManager {
    private final EvientsCore plugin;

    public UserDataManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    private UserData setupUserdata(UUID uuid) throws SQLException {
        String sql = "INSERT INTO user_data (uuid) VALUES (?) RETURNING *";

        PreparedStatement stmt = plugin.database.connection.prepareStatement(sql);
        stmt.setString(1, uuid.toString());
        ResultSet rs = stmt.executeQuery();
        return UserData.fromResultSet(rs);
    }

    public UserData getFor(UUID uuid) {
        String sql = "SELECT * FROM user_data WHERE uuid = ?";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return UserData.fromResultSet(rs);
            } else {
                return setupUserdata(uuid);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDmsDisabled(UUID uuid, boolean value) {
        String sql = "UPDATE user_data SET dms_disabled = ? WHERE uuid = ?";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setBoolean(1, value);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
