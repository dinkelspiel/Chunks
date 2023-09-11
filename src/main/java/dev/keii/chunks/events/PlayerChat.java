package dev.keii.chunks.events;

import dev.keii.chunks.error.Success;
import dev.keii.chunks.inventories.InventoryChunkPermission;
import dev.keii.chunks.models.Claim;
import dev.keii.chunks.models.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerChat implements Listener {

    public static Map<String, ChatListener> chunkListener = new HashMap<>();

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if(chunkListener.get(player.getUniqueId().toString()) == null)
        {
            return;
        }

        if(chunkListener.get(player.getUniqueId().toString()) instanceof ChunkPermissionAddPlayer)
            event.setCancelled(true);

        Claim claim = Claim.fromChunk(((ChunkPermissionAddPlayer)chunkListener.get(player.getUniqueId().toString())).getChunk());

        if(claim == null)
        {
            return;
        }

        User user = User.fromNickname(event.getMessage());
        if(user == null)
        {
            player.sendMessage(Component.text("Failed: Invalid username").color(NamedTextColor.RED));
            return;
        }

        var result = claim.addChunkPermissionsForUser(player, user);

        if(result instanceof Success) {
            player.sendMessage(Component.text("Permissions added for player").color(NamedTextColor.GREEN));
            player.closeInventory();
            InventoryChunkPermission cp = new InventoryChunkPermission(((ChunkPermissionAddPlayer)chunkListener.get(player.getUniqueId().toString())).getChunk());
            player.openInventory(cp.getInventory());
        } else {
            player.sendMessage(Component.text("Failed: " + result.getMessage()).color(NamedTextColor.RED));

        }

        chunkListener.remove(player.getUniqueId().toString());
    }
}
