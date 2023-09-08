package dev.keii.chunks.events;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.PlayerChunk;
import dev.keii.chunks.commands.ChunkOverride;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BucketFill implements Listener {
    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event)
    {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        if(ChunkOverride.getChunkOverrideForPlayer(player))
        {
            event.setCancelled(false);
            return;
        }

        boolean canBreak = BlockBreak.getPlayerPermissionForChunk(player, chunk, PlayerChunk.ChunkPermission.BucketFill);

        event.setCancelled(!canBreak);
        if(!canBreak)
        {
            player.sendActionBar(Component.text("You do not have the rights to fill buckets in this chunk!").color(NamedTextColor.RED));
        }
    }
}
