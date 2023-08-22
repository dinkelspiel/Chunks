package dev.keii.keiichunks.events;

import dev.keii.keiichunks.KeiiChunks;
import dev.keii.keiichunks.PlayerChunk;
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

        boolean canBreak = BlockBreak.getPlayerPermissionForChunk(player, chunk, PlayerChunk.ChunkPermission.BucketEmpty);

        event.setCancelled(!canBreak);
        if(!canBreak)
        {
            player.sendActionBar(Component.text("You do not have the rights to empty buckets in this chunk!").color(NamedTextColor.RED));
        }
    }
}
