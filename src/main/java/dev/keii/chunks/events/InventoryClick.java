package dev.keii.chunks.events;

import dev.keii.chunks.models.Claim;
import dev.keii.chunks.inventories.InventoryChunkPermission;
import dev.keii.chunks.inventories.InventoryMap;
import dev.keii.chunks.inventories.InventoryModifyChunk;
import dev.keii.chunks.inventories.InventoryModifyChunkPermission;
import dev.keii.chunks.models.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InventoryClick implements Listener {
    public static Map<String, Vector2i> modifyChunk = new HashMap<>();
    public static Map<String, Integer> modifyChunkPermissionUser = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();

        if(inventory.getHolder() instanceof InventoryMap) {
            event.setCancelled(true);
            Player player = (Player)event.getWhoClicked();

            if(event.getCurrentItem() == null)
            {
                return;
            }

            List<String> lore = Objects.requireNonNull(event.getCurrentItem()).getLore();

            Integer chunkX = null;
            Integer chunkZ = null;

            for(String line : Objects.requireNonNull(lore))
            {
                if(line.startsWith("ChunkX")) {
                    String[] split = line.split(",");
                    chunkX = Integer.parseInt(split[0].substring(8));
                    chunkZ = Integer.parseInt(split[1].substring(9));
                }
            }

            if(chunkX != null)
            {
                Claim claim = Claim.fromChunk(event.getWhoClicked().getWorld().getChunkAt(chunkX, chunkZ));

                if(claim != null) {
                    if (claim.getOwner() != null) {
                        if (claim.getOwner().getUuid().toString().equals(player.getUniqueId().toString())) {
                            player.closeInventory();
                            InventoryModifyChunk mc = new InventoryModifyChunk();
                            InventoryClick.modifyChunk.remove(player.getUniqueId().toString());
                            InventoryClick.modifyChunk.put(player.getUniqueId().toString(), new Vector2i(chunkX, chunkZ));
                            player.openInventory(mc.getInventory());
                        }
                        return;
                    }
                }

                if(Claim.claim(player, event.getWhoClicked().getWorld().getChunkAt(chunkX, chunkZ))) {
                    claim = Claim.fromChunk(event.getWhoClicked().getWorld().getChunkAt(chunkX, chunkZ));
                    assert claim != null;
                    claim.addChunkPermissionsForUser(player, null);
                    player.sendMessage(Component.text("Claimed Chunk").color(NamedTextColor.YELLOW));
                } else {
                    player.sendMessage(Component.text("Failed to claim chunk").color(NamedTextColor.RED));
                }

                player.closeInventory();
                InventoryMap map = new InventoryMap(player);
                player.openInventory(map.getInventory());
            }
        } else if(inventory.getHolder() instanceof InventoryModifyChunk) {
            event.setCancelled(true);

            int slot = event.getSlot();
            if(slot > 17)
            {
                slot -= 9;
            }
            if(slot > 8)
            {
                slot -= 9;
            }

            Player player = (Player) event.getWhoClicked();

            Claim claim = Claim.fromChunk(event.getWhoClicked().getWorld().getChunkAt(modifyChunk.get(player.getUniqueId().toString()).x, modifyChunk.get(player.getUniqueId().toString()).y));

            assert claim != null;

            if(slot > 5)
            {
                if(claim.unClaim(player)) {
                    player.sendMessage(Component.text("Unclaimed chunk").color(NamedTextColor.YELLOW));
                    player.closeInventory();

                    InventoryMap map = new InventoryMap(player);
                    player.openInventory(map.getInventory());
                } else {
                    player.sendMessage(Component.text("Failed to unclaim chunk").color(NamedTextColor.RED));
                }
            } else if(slot > 2)
            {
                if(!claim.toggleExplosionPolicy(player))
                {
                    player.sendMessage(Component.text("Failed to toggle explosions for chunk").color(NamedTextColor.RED));
                    return;
                }

                if(claim.getAllowExplosions())
                {
                    player.sendMessage(Component.text("Enabled explosions in chunk").color(NamedTextColor.YELLOW));
                } else
                {
                    player.sendMessage(Component.text("Disabled explosions in chunk").color(NamedTextColor.YELLOW));
                }


            } else {
                player.closeInventory();
                InventoryChunkPermission cp = new InventoryChunkPermission(claim.getChunk());
                player.openInventory(cp.getInventory());
            }

        } else if(inventory.getHolder() instanceof InventoryChunkPermission)
        {
            event.setCancelled(true);

            if(event.getCurrentItem() == null)
            {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            Chunk chunk = event.getWhoClicked().getWorld().getChunkAt(modifyChunk.get(player.getUniqueId().toString()).x, modifyChunk.get(player.getUniqueId().toString()).y);

            if(event.getCurrentItem().displayName().toString().contains("Everyone")) {
//                player.sendMessage("Found");
                modifyChunkPermissionUser.put(player.getUniqueId().toString(), null);

                player.closeInventory();
                InventoryModifyChunkPermission mcp = new InventoryModifyChunkPermission(null, chunk);
                player.openInventory(mcp.getInventory());
            } else if (!event.getCurrentItem().displayName().toString().contains("Add User")) {
                String userid = null;
                for(var i : Objects.requireNonNull(event.getCurrentItem().lore()))
                {
                    if(i.toString().contains("User")) {
                        userid = i.toString().split("\"")[1].split(":")[1];
                    }
                }
                if(userid == null)
                {
                    return;
                }

                modifyChunkPermissionUser.put(player.getUniqueId().toString(), Integer.parseInt(userid.trim()));

                player.closeInventory();
                InventoryModifyChunkPermission mcp = new InventoryModifyChunkPermission(User.fromId(Integer.parseInt(userid.trim())).getUuid().toString(), chunk);
                player.openInventory(mcp.getInventory());
            } else {
                PlayerChat.chunkListener.put(player.getUniqueId().toString(), new ChunkPermissionAddPlayer(chunk));
                player.sendMessage(Component.text("Type name of player you wish to add permissions for in chat!").color(NamedTextColor.YELLOW));
                player.closeInventory();
            }
        } else if(inventory.getHolder() instanceof InventoryModifyChunkPermission)
        {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            Claim claim = Claim.fromChunk(event.getWhoClicked().getWorld().getChunkAt(modifyChunk.get(player.getUniqueId().toString()).x, modifyChunk.get(player.getUniqueId().toString()).y));

            if(event.getCurrentItem() == null)
            {
                return;
            }

            if(claim == null)
            {
                return;
            }

            String displayName = event.getCurrentItem().displayName().toString();

            User user = User.fromId(modifyChunkPermissionUser.get(player.getUniqueId().toString()));
            if(modifyChunkPermissionUser.get(player.getUniqueId().toString()) == null)
            {
                user = null;
            }

            if (displayName.contains("Interact")) {
                claim.setPermission(player, user, Claim.ChunkPermission.Interact, displayName.contains("Enable"));
            } else if (displayName.contains("Block Break")) {
                claim.setPermission(player, user, Claim.ChunkPermission.BlockBreak, displayName.contains("Enable"));
            } else if (displayName.contains("Block Place")) {
                claim.setPermission(player, user, Claim.ChunkPermission.BlockPlace, displayName.contains("Enable"));
            } else if (displayName.contains("Bucket Empty")) {
                claim.setPermission(player, user, Claim.ChunkPermission.BucketEmpty, displayName.contains("Enable"));
            } else if (displayName.contains("Bucket Fill")) {
                claim.setPermission(player, user, Claim.ChunkPermission.BucketFill, displayName.contains("Enable"));
            }

            if(displayName.contains("Back"))
            {
                player.closeInventory();
                InventoryChunkPermission cp = new InventoryChunkPermission(claim.getChunk());
                player.openInventory(cp.getInventory());
                return;
            }

            player.closeInventory();
            if(modifyChunkPermissionUser.get(player.getUniqueId().toString()) != null) {
                InventoryModifyChunkPermission mcp = new InventoryModifyChunkPermission(User.fromId(modifyChunkPermissionUser.get(player.getUniqueId().toString())).getUuid().toString(), claim.getChunk());
                player.openInventory(mcp.getInventory());
            } else {
                InventoryModifyChunkPermission mcp = new InventoryModifyChunkPermission(null, claim.getChunk());
                player.openInventory(mcp.getInventory());
            }
        }
    }
}
