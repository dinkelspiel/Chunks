package dev.keii.chunks.events;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
//        Player player = event.getPlayer();

//        Bukkit.broadcast(Component.text(player.getStatistic(Statistic.PLAY_ONE_MINUTE)));
    }
}
