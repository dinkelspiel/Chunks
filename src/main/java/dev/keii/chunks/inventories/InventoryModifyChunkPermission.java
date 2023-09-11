package dev.keii.chunks.inventories;

import dev.keii.chunks.models.Claim;
import dev.keii.chunks.models.ClaimPermission;
import dev.keii.chunks.models.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class InventoryModifyChunkPermission implements InventoryHolder {

    public static Component Name = Component.text("\uF805\uEffd\uF80B\uF80A\uF809\uF808\uF80A\uF808\uF806").color(NamedTextColor.WHITE);

    @Nullable String uuid;
    Chunk chunk;

    public InventoryModifyChunkPermission(@Nullable String uuid, Chunk chunk)
    {
        this.chunk = chunk;
        this.uuid = uuid;
    }


    @Override
    public @NotNull Inventory getInventory()
    {
        Inventory inventory = Bukkit.createInventory(this, 27, Name.append(Component.text((uuid != null ? User.fromUuid(uuid).getNickname() : "Everyone") + "'s permissions").color(NamedTextColor.DARK_GRAY)));

        if(uuid != null) {
            Bukkit.getConsoleSender().sendMessage(Component.text(uuid));
        }

        Claim claim = Claim.fromChunk(chunk);

        if(claim == null)
        {
            return inventory;
        }

        ClaimPermission claimPermission = claim.getPermissionsForUser(User.fromUuid(uuid));

        HashMap<Claim.ChunkPermission, Boolean> permissionHashmap = new HashMap<>();
        permissionHashmap.put(Claim.ChunkPermission.Interact, claimPermission.getInteract());
        permissionHashmap.put(Claim.ChunkPermission.BlockBreak, claimPermission.getBlockBreak());
        permissionHashmap.put(Claim.ChunkPermission.BlockPlace, claimPermission.getBlockPlace());
        permissionHashmap.put(Claim.ChunkPermission.BucketEmpty, claimPermission.getBucketEmpty());
        permissionHashmap.put(Claim.ChunkPermission.BucketFill, claimPermission.getBucketFill());

        for (var permission : permissionHashmap.entrySet()) {
            String permissionString = "";
//                Bukkit.getServer().broadcastMessage(permission.getKey().toString());
            switch (permission.getKey()) {
                case Interact -> permissionString = "Interact";
                case BlockBreak -> permissionString = "Block Break";
                case BlockPlace -> permissionString = "Block Place";
                case BucketFill -> permissionString = "Bucket Fill";
                case BucketEmpty -> permissionString = "Bucket Empty";
            }

            ItemStack item = new ItemStack(Material.STICK);
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(permission.getValue() ? 1003 : 1004);
            meta.displayName(Component.text((permission.getValue() ? "Disable " : "Enable ") + permissionString).color(NamedTextColor.YELLOW));
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        // 18 26

        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1005);
        meta.displayName(Component.text("Back").color(NamedTextColor.YELLOW));
        item.setItemMeta(meta);
        inventory.setItem(18, item);

        return inventory;
    }
}
