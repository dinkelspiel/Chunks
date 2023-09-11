package dev.keii.chunks.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class PlayerResourcePack implements Listener {
    @EventHandler
    public void onPlayerUpdateServerResourcePack(PlayerResourcePackStatusEvent event)
    {
        Player player = event.getPlayer();
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED)
        {
            player.kick(Component.text("You must accept the server resource pack!\n").color(NamedTextColor.RED));
        } else if(event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)
        {
            player.kick(Component.text("Server resource pack download failed!\n").color(NamedTextColor.RED));
        }
    }
}
