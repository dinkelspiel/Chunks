package dev.keii.keiichunks.inventories;

import dev.keii.keiichunks.DatabaseConnector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InventoryChunkPermission implements InventoryHolder {

    public static Component Name = Component.text("\uF805\uEffd\uF80B\uF80A\uF809\uF808\uF80A\uF808\uF806").color(NamedTextColor.WHITE).append(Component.text("Modify Chunk Permissions").color(NamedTextColor.DARK_GRAY));

    private final Chunk chunk;

    public InventoryChunkPermission(Chunk chunk)
    {
        this.chunk = chunk;
    }


    private List<Component> getChunkPermissions(ResultSet claimPermissionEveryoneResultSet) throws SQLException {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Block Break: " + claimPermissionEveryoneResultSet.getBoolean("block_break")));
        lore.add(Component.text("Block Place: " + claimPermissionEveryoneResultSet.getBoolean("block_place")));
        lore.add(Component.text("Bucket Empty: " + claimPermissionEveryoneResultSet.getBoolean("bucket_empty")));
        lore.add(Component.text("Bucket Place: " + claimPermissionEveryoneResultSet.getBoolean("bucket_fill")));
        lore.add(Component.text("Interact: " + claimPermissionEveryoneResultSet.getBoolean("interact")));
        return lore;
    }

    @Override
    public @NotNull Inventory getInventory()
    {
        Inventory inventory = Bukkit.createInventory(this, 27, Name);

        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();

            String claimQuery = "SELECT * FROM claim WHERE chunk_x = " + chunk.getX() + " AND chunk_z = " + chunk.getZ();
            ResultSet claimResultSet = statement.executeQuery(claimQuery);

            if (!claimResultSet.next()) {
                claimResultSet.close();
                statement.close();
                connection.close();
                throw new Exception("No claim exists for chunk " + chunk.getX() + " " + chunk.getZ());
            }

            long claimId = claimResultSet.getLong("id");

            String claimPermissionQuery = "SELECT * FROM claim_permission WHERE user_id IS NULL AND claim_id = " + claimId;
            ResultSet claimPermissionEveryoneResultSet = statement.executeQuery(claimPermissionQuery);

            if(claimPermissionEveryoneResultSet.next())
            {
                ItemStack item = new ItemStack(Material.STICK);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text("Everyone").color(NamedTextColor.AQUA));
                meta.lore(getChunkPermissions(claimPermissionEveryoneResultSet));
                meta.setCustomModelData(1001);
                item.setItemMeta(meta);
                inventory.addItem(item);
            }

            claimPermissionEveryoneResultSet.close();

            claimPermissionQuery = "SELECT * FROM claim_permission WHERE user_id IS NOT NULL AND claim_id = " + claimId;
            ResultSet claimPermissionResultSet = statement.executeQuery(claimPermissionQuery);

            while(claimPermissionResultSet.next())
            {
                Statement userStatement = connection.createStatement();
                ItemStack item = new ItemStack(Material.STICK);
                ItemMeta meta = item.getItemMeta();

                String userQuery = "SELECT * FROM user WHERE id = " + claimPermissionResultSet.getInt("user_id");
                ResultSet userResultSet = userStatement.executeQuery(userQuery);

                String username;
                if(userResultSet.next())
                {
                    username = userResultSet.getString("nickname");
                } else {
                    username = "Invalid User";
                }

                meta.displayName(Component.text(username).color(NamedTextColor.YELLOW));
                List<Component> lore = getChunkPermissions(claimPermissionResultSet);
                lore.add(Component.text("User: " + claimPermissionResultSet.getInt("user_id")));
                meta.setCustomModelData(1001);
                meta.lore(lore);
                item.setItemMeta(meta);
                inventory.addItem(item);
                userResultSet.close();
            }

            ItemStack item = new ItemStack(Material.STICK);
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(1002);
            meta.displayName(Component.text("Add User").color(NamedTextColor.YELLOW));
            item.setItemMeta(meta);
            inventory.setItem(18, item);

            claimPermissionResultSet.close();
            claimResultSet.close();
            statement.close();
            connection.close();
            return inventory;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Component.text(e.getMessage()));
        }

        return inventory;
    }
}
