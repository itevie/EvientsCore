package rest.dawn.evientsCore.Models;

import org.bukkit.inventory.ItemStack;
import rest.dawn.evientsCore.Util.InventoryUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Kit {
    public final String name;
    public final ItemStack[] items;

    public Kit(String name, ItemStack[] items) {
        this.name = name;
        this.items = items;
    }

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
