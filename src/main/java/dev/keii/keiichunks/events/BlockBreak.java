package dev.keii.keiichunks.events;

import dev.keii.keiichunks.DatabaseConnector;
import dev.keii.keiichunks.KeiiChunks;
import dev.keii.keiichunks.PlayerChunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static dev.keii.keiichunks.PlayerChunk.getChunkPermissionString;

public class BlockBreak implements Listener {

    public static boolean getPlayerPermissionForChunk(Player player, Chunk chunk, PlayerChunk.ChunkPermission perm)
    {
        boolean canBreak = true;

        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();

            ResultSet claimResultSet = statement.executeQuery("SELECT * FROM claim WHERE chunk_x = " + chunk.getX() + " AND chunk_z = " + chunk.getZ());

            if(!claimResultSet.next()) // Claim doesn't exist
            {
                claimResultSet.close();
                statement.close();
                connection.close();
                return true;
            }

            long claimId = claimResultSet.getLong("id");
            long claimOwnerId = claimResultSet.getLong("user_id");
            claimResultSet.close();

            ResultSet claimPermissionResultSet = statement.executeQuery("SELECT * FROM claim_permission WHERE user_id IS NULL AND claim_id = " + claimId);

            if(claimPermissionResultSet.next())
            {
                canBreak = claimPermissionResultSet.getBoolean(getChunkPermissionString(perm));
            }

            claimPermissionResultSet.close();

            ResultSet userResultSet = statement.executeQuery( "SELECT id FROM user WHERE uuid = \"" + player.getUniqueId().toString() + "\"");

            if(userResultSet.next()) {
                if(userResultSet.getLong("id") == claimOwnerId)
                {
                    canBreak = true;
                } else {
                    ResultSet claimPermissionUserResultSet = statement.executeQuery("SELECT * FROM claim_permission WHERE user_id = " + userResultSet.getLong("id") + " AND claim_id = " + claimId);

                    if (claimPermissionUserResultSet.next()) {
                        canBreak = claimPermissionUserResultSet.getBoolean(getChunkPermissionString(perm));
                    }

                    claimPermissionUserResultSet.close();
                }
            }

            userResultSet.close();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            Bukkit.getServer().broadcast(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
        }

        return canBreak;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        boolean canBreak = getPlayerPermissionForChunk(player, chunk, PlayerChunk.ChunkPermission.BlockBreak);

        event.setCancelled(!canBreak);
        if(!canBreak)
        {
            player.sendActionBar(Component.text("You do not have the rights to break blocks in this chunk!").color(NamedTextColor.RED));
        }
    }
}
