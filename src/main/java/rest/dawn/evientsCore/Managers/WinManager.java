package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Models.Win;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WinManager {
    private final EvientsCore plugin;

    public WinManager(EvientsCore plugin) {
        this.plugin = plugin;
    }

    public void addWin(Win win) {
        String sql = "INSERT INTO wins (uuid, added_at, event_name) VALUES (?, ?, ?);";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            win.apply(stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Win> getAllWins() {
        String sql = "SELECT * FROM wins";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return resultSetListToWinList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Win> getWinsFor(UUID player) {
        String sql = "SELECT * FROM wins WHERE uuid = ?";
        try (PreparedStatement stmt = plugin.database.connection.prepareStatement(sql)) {
            stmt.setString(1, player.toString());
            ResultSet rs = stmt.executeQuery();
            return resultSetListToWinList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Win> resultSetListToWinList(ResultSet rs) throws SQLException {
        List<Win> wins = new ArrayList<>();

        while (rs.next()) {
            wins.add(Win.fromResultSet(rs));
        }

        return wins;
    }
}
