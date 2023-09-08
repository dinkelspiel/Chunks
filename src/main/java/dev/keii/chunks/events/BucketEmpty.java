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
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BucketEmpty implements Listener {
    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event)
    {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        if(ChunkOverride.getChunkOverrideForPlayer(player))
        {
            event.setCancelled(false);
            return;
        }

        boolean canBreak = BlockBreak.getPlayerPermissionForChunk(player, chunk, PlayerChunk.ChunkPermission.BucketEmpty);

        event.setCancelled(!canBreak);
        if(!canBreak)
        {
            player.sendActionBar(Component.text("You do not have the rights to empty buckets in this chunk!").color(NamedTextColor.RED));
        }
    }
}
