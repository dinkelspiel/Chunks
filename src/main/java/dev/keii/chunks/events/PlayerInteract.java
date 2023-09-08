package dev.keii.chunks.events;

import dev.keii.chunks.PlayerChunk;
import dev.keii.chunks.commands.ChunkOverride;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if(event.getClickedBlock() == null)
        {
            event.setCancelled(false);
            return;
        }

        if(ChunkOverride.getChunkOverrideForPlayer(player))
        {
            event.setCancelled(false);
            return;
        }

        Chunk chunk = event.getClickedBlock().getChunk();

        boolean canBreak = BlockBreak.getPlayerPermissionForChunk(player, chunk, PlayerChunk.ChunkPermission.Interact);

        event.setCancelled(!canBreak);
        if(!canBreak)
        {
            player.sendActionBar(Component.text("You do not have the rights to interact with this chunk!").color(NamedTextColor.RED));
        }
    }
}
