package dev.keii.chunks.events;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.commands.ChunkOverride;
import dev.keii.chunks.models.Claim;
import dev.keii.chunks.models.ClaimPermission;
import dev.keii.chunks.models.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        Claim claim = Claim.fromChunk(chunk);

        if(ChunkOverride.getChunkOverrideForPlayer(player) || claim == null)
        {
            event.setCancelled(false);
            return;
        }

        User user = User.fromUuid(player.getUniqueId().toString());

        if(user == null)
        {
            event.setCancelled(false);
            return;
        }

        ClaimPermission claimPermission = claim.getPermissionsForUser(user);

        if(claimPermission == null)
        {
            event.setCancelled(false);
            return;
        }

        boolean hasPermission = claimPermission.getBlockPlace();

        event.setCancelled(!hasPermission);
        if(!hasPermission)
        {
            player.sendActionBar(Component.text("You do not have the rights to place blocks in this chunk!").color(NamedTextColor.RED));
        }
    }
}
