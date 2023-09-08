package dev.keii.chunks.events;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.PlayerChunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        event.blockList().removeIf(b -> !PlayerChunk.getExplosionPolicy(b.getChunk()));
    }
}
