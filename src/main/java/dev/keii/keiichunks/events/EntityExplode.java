package dev.keii.keiichunks.events;

import dev.keii.keiichunks.KeiiChunks;
import dev.keii.keiichunks.PlayerChunk;
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
