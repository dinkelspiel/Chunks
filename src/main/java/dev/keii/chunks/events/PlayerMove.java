package dev.keii.chunks.events;

import dev.keii.chunks.models.Claim;
import dev.keii.chunks.models.User;
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
            Claim toClaim = Claim.fromChunk(event.getTo().getChunk());

            if(toClaim == null)
            {
                player.sendActionBar(Component.text("You have entered neutral chunks").color(NamedTextColor.AQUA));
                return;
            }

            User user = User.fromPlayer(player);

            if(user == null)
            {
                return;
            }

            Claim fromClaim = Claim.fromChunk(event.getFrom().getChunk());

            if(fromClaim == null)
            {
                if(toClaim.getOwner().getId() == user.getId()) {
                    player.sendActionBar(Component.text("You have entered your chunk").color(NamedTextColor.YELLOW));
                } else {
                    player.sendActionBar(Component.text("You have entered " + toClaim.getOwner().getNickname() + "'s chunk").color(NamedTextColor.RED));
                }
                return;
            }

            if(toClaim.getOwner().getId() == user.getId() && fromClaim.getOwner().getId() != user.getId()) {
                player.sendActionBar(Component.text("You have entered your chunk").color(NamedTextColor.YELLOW));
            } else if(toClaim.getOwner().getId() != fromClaim.getOwner().getId()) {
                player.sendActionBar(Component.text("You have entered " + toClaim.getOwner().getNickname() + "'s chunk").color(NamedTextColor.RED));
            }
        }
    }
}
