package rest.dawn.evientsCore.Models;

import org.bukkit.inventory.ItemStack;
import rest.dawn.evientsCore.Util.InventoryUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public record Kit(String name, ItemStack[] items) {
    public void apply(PreparedStatement stmt) throws SQLException {
        stmt.setString(1, this.name);
        stmt.setString(2, InventoryUtil.serializeInventory(this.items));
    }

    public static Kit fromResultSet(ResultSet rs) throws SQLException {
        return new Kit(
                rs.getString("name"),
                InventoryUtil.deserializeInventory(rs.getString("items"))
        );
    }
}
