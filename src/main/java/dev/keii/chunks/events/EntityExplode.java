package dev.keii.chunks.events;

import dev.keii.chunks.models.Claim;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {

        event.blockList().removeIf(b -> {
            Claim claim = Claim.fromChunk(event.getEntity().getChunk());
            if(claim == null)
            {
                return true;
            }
            return !claim.getAllowExplosions();
        });
    }
}
