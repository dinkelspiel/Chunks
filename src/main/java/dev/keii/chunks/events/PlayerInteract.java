package dev.keii.chunks.events;

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
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Chunk chunk = event.getClickedBlock().getChunk();

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

        boolean hasPermission = claimPermission.getInteract();

        event.setCancelled(!hasPermission);
        if(!hasPermission)
        {
            player.sendActionBar(Component.text("You do not have the rights to interact with this chunk!").color(NamedTextColor.RED));
        }
    }
}
