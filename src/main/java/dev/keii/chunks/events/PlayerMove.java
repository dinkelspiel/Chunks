package dev.keii.chunks.events;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.PlayerChunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if (!event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            if(PlayerChunk.getPlayerOwnsChunk(player, event.getTo().getChunk()) && !PlayerChunk.getPlayerOwnsChunk(player, event.getFrom().getChunk())) {
                player.sendActionBar(Component.text("You have entered your chunk").color(NamedTextColor.YELLOW));
            } else if(!PlayerChunk.getPlayerOwnsChunk(player, event.getTo().getChunk()) && PlayerChunk.getPlayerOwnsChunk(player, event.getFrom().getChunk())) {
                player.sendActionBar(Component.text("You have left your chunk").color(NamedTextColor.YELLOW));
            } else if(PlayerChunk.getChunkOwner(event.getTo().getChunk()) != null && PlayerChunk.getChunkOwner(event.getFrom().getChunk()) == null) {
                player.sendActionBar(Component.text("You have entered " + PlayerChunk.getChunkOwner(event.getTo().getChunk()) + "'s chunk").color(NamedTextColor.RED));
            } else if(PlayerChunk.getChunkOwner(event.getTo().getChunk()) == null && PlayerChunk.getChunkOwner(event.getFrom().getChunk()) != null) {
                player.sendActionBar(Component.text("You have entered neutral chunks").color(NamedTextColor.AQUA));
            }
        }
    }
}
